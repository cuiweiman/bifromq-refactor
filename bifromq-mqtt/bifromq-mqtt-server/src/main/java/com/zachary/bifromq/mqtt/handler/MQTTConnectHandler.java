package com.zachary.bifromq.mqtt.handler;

import com.zachary.bifromq.mqtt.handler.v3.MQTT3ConnectHandler;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed.ChannelError;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed.ConnectTimeout;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed.IdentifierRejected;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.channelclosed.UnacceptedProtocolVer;
import com.zachary.bifromq.plugin.eventcollector.mqttbroker.clientdisconnect.ProtocolViolation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttIdentifierRejectedException;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttUnacceptableProtocolVersionException;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.zachary.bifromq.plugin.eventcollector.ThreadLocalEventPool.getLocal;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED;
import static io.netty.handler.codec.mqtt.MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION;

@Slf4j
public class MQTTConnectHandler extends MQTTMessageHandler {
    public static final String NAME = "MqttConnectMessageHandler";
    private final long timeoutInSec;
    private InetSocketAddress remoteAddr;
    private ScheduledFuture<?> timeoutCloseTask;

    public MQTTConnectHandler(int timeoutInSec) {
        this.timeoutInSec = timeoutInSec;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        remoteAddr = ChannelAttrs.socketAddress(ctx.channel());
        timeoutCloseTask = ctx.channel().eventLoop().schedule(() ->
                closeConnectionNow(getLocal(ConnectTimeout.class).peerAddress(remoteAddr)), timeoutInSec, TimeUnit.SECONDS);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        cancelIfUndone(timeoutCloseTask);
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        assert msg instanceof MqttMessage;
        // stop reading next message and resume reading once finish processing current one
        ctx.channel().config().setAutoRead(false);
        // cancel the scheduled connect timeout task
        timeoutCloseTask.cancel(true);
        MqttMessage message = (MqttMessage) msg;
        if (!message.decoderResult().isSuccess()) {
            // decoded with known protocol violation
            Throwable cause = message.decoderResult().cause();
            if (cause instanceof MqttIdentifierRejectedException) {
                closeConnectionWithSomeDelay(MqttMessageBuilders.connAck()
                                .returnCode(CONNECTION_REFUSED_IDENTIFIER_REJECTED)
                                .build(),
                        getLocal(IdentifierRejected.class)
                                .peerAddress(remoteAddr));
            } else if (cause instanceof MqttUnacceptableProtocolVersionException) {
                closeConnectionWithSomeDelay(MqttMessageBuilders.connAck()
                                .returnCode(CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION)
                                .build(),
                        getLocal(UnacceptedProtocolVer.class)
                                .peerAddress(remoteAddr));
            } else {
                // according to [MQTT-4.8.0-2]
                closeConnectionWithSomeDelay(
                        getLocal(ProtocolViolation.class).statement("MQTT-4.8.0-2")
                );
            }
            log.warn("Failed to decode mqtt connect message: remote={}",
                    remoteAddr, message.decoderResult().cause());
            return;
        } else if (!(message instanceof MqttConnectMessage)) {
            // according to [MQTT-3.1.0-1]
            closeConnectionWithSomeDelay(
                    getLocal(ProtocolViolation.class)
                            .statement("MQTT-3.1.0-1")
            );
            log.warn("First packet must be mqtt connect message: remote={}", remoteAddr);
            return;
        }

        MqttConnectMessage connectMessage = (MqttConnectMessage) message;
        switch (connectMessage.variableHeader().version()) {
            case 3:
            case 4:
                ctx.pipeline().addAfter(MQTTConnectHandler.NAME, MQTT3ConnectHandler.NAME, new MQTT3ConnectHandler());
                // delegate to MQTT 3 handler
                ctx.fireChannelRead(connectMessage);
                ctx.pipeline().remove(this);
                break;
            case 5:
            default:
                // TODO: MQTT5
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        cancelIfUndone(timeoutCloseTask);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // simple strategy: shutdown the channel directly
        log.warn("ctx: {}, cause:", ctx, cause);
        if (ctx.channel().isActive() && closeNotScheduled()) {
            // if disconnection is caused purely by channel error
            closeConnectionNow(getLocal(ChannelError.class)
                    .peerAddress(remoteAddr)
                    .cause(cause));
        }
    }
}
