package com.zachary.bifromq.mqtt.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.ReferenceCountUtil;

import java.util.LinkedList;
import java.util.Queue;

public class MQTTMessageDebounceHandler extends ChannelDuplexHandler {
    public static final String NAME = "MQTTMessageDebounceHandler";

    private final Queue<MqttMessage> buffer = new LinkedList<>();
    private boolean readOne = false;

    @Override
    public void read(ChannelHandlerContext ctx) {
        if (ctx.channel().config().isAutoRead()) {
            MqttMessage msg;
            while ((msg = buffer.poll()) != null) {
                ctx.fireChannelRead(msg);
                ReferenceCountUtil.release(msg);
            }
            ctx.read();
        } else {
            MqttMessage msg = buffer.poll();
            if (msg != null) {
                ctx.fireChannelRead(msg);
                ReferenceCountUtil.release(msg);
            } else {
                readOne = true;
                ctx.read();
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        assert msg instanceof MqttMessage;
        if (ctx.channel().config().isAutoRead()) {
            ctx.fireChannelRead(msg);
        } else {
            buffer.offer(ReferenceCountUtil.retain((MqttMessage) msg));
            if (readOne) {
                MqttMessage mqttMsg = buffer.poll();
                ctx.fireChannelRead(mqttMsg);
                ReferenceCountUtil.release(mqttMsg);
                readOne = false;
            }
        }
    }
}
