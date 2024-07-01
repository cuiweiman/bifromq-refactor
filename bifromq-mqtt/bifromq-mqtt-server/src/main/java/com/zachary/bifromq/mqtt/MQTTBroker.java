package com.zachary.bifromq.mqtt;

import com.zachary.bifromq.baserpc.utils.NettyUtil;
import com.zachary.bifromq.dist.client.IDistClient;
import com.zachary.bifromq.inbox.client.IInboxReaderClient;
import com.zachary.bifromq.mqtt.handler.ByteBufToWebSocketFrameEncoder;
import com.zachary.bifromq.mqtt.handler.ChannelAttrs;
import com.zachary.bifromq.mqtt.handler.ClientAddrHandler;
import com.zachary.bifromq.mqtt.handler.ConnectionRateLimitHandler;
import com.zachary.bifromq.mqtt.handler.MQTTConnectHandler;
import com.zachary.bifromq.mqtt.handler.MQTTMessageDebounceHandler;
import com.zachary.bifromq.mqtt.handler.ws.WebSocketFrameToByteBufDecoder;
import com.zachary.bifromq.mqtt.service.ILocalSessionBrokerServer;
import com.zachary.bifromq.mqtt.session.MQTTSessionContext;
import com.zachary.bifromq.plugin.authprovider.IAuthProvider;
import com.zachary.bifromq.plugin.eventcollector.IEventCollector;
import com.zachary.bifromq.plugin.settingprovider.ISettingProvider;
import com.zachary.bifromq.retain.client.IRetainServiceClient;
import com.zachary.bifromq.sessiondict.client.ISessionDictionaryClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
abstract class MQTTBroker implements IMQTTBroker {
    private static final String MQTT_SUBPROTOCOL_CSV_LIST = "mqtt, mqttv3.1, mqttv3.1.1";
    private final String host;
    private final MQTTBrokerOptions options;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    // fields of the returned anonymous MQTTBroker instance
    private final ConnectionRateLimitHandler connRateLimitHandler;
    private final ClientAddrHandler remoteAddrHandler;
    private final MQTTSessionContext sessionContext;
    private final ILocalSessionBrokerServer subBroker;
    private final Optional<ChannelFuture> tcpChannelF;
    private final Optional<ChannelFuture> tlsChannelF;
    private final Optional<ChannelFuture> wsChannelF;
    private final Optional<ChannelFuture> wssChannelF;

    public MQTTBroker(String host,
                      EventLoopGroup bossGroup,
                      EventLoopGroup workerGroup,
                      MQTTBrokerOptions options,
                      IAuthProvider authProvider,
                      IEventCollector eventCollector,
                      ISettingProvider settingProvider,
                      IDistClient distClient,
                      IInboxReaderClient inboxClient,
                      IRetainServiceClient retainClient,
                      ISessionDictionaryClient sessionDictClient,
                      ConnListenerBuilder.TCPConnListenerBuilder tcpConnListenerBuilder,
                      ConnListenerBuilder.TLSConnListenerBuilder tlsConnListenerBuilder,
                      ConnListenerBuilder.WSConnListenerBuilder wsConnListenerBuilder,
                      ConnListenerBuilder.WSSConnListenerBuilder wssConnListenerBuilder) {
        this.host = host;
        this.options = options.toBuilder().build();
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        connRateLimitHandler = new ConnectionRateLimitHandler(options.connectRateLimit());
        remoteAddrHandler = new ClientAddrHandler();
        subBroker = buildLocalSessionBroker();
        sessionContext = MQTTSessionContext.builder()
                .authProvider(authProvider)
                .eventCollector(eventCollector)
                .settingProvider(settingProvider)
                .distClient(distClient)
                .inboxClient(inboxClient)
                .retainClient(retainClient)
                .sessionDictClient(sessionDictClient)
                .brokerServer(subBroker)
                .maxResendTimes(options.maxResendTimes())
                .resendDelayMillis(options.resendDelayMillis())
                .defaultKeepAliveTimeSeconds(options.defaultKeepAliveSeconds())
                .qos2ConfirmWindowSeconds(options.qos2ConfirmWindowSeconds())
                .build();
        tcpChannelF = Optional.ofNullable(tcpConnListenerBuilder).map(this::bindTCPChannel);
        tlsChannelF = Optional.ofNullable(tlsConnListenerBuilder).map(this::bindTLSChannel);
        wsChannelF = Optional.ofNullable(wsConnListenerBuilder).map(this::bindWSChannel);
        wssChannelF = Optional.ofNullable(wssConnListenerBuilder).map(this::bindWSSChannel);
    }

    @Override
    public void start() {
        try {
            log.info("Starting MQTT broker");
            log.debug("Starting sub broker server");
            subBroker.start();
            log.debug("Starting server channel");
            if (tcpChannelF.isPresent()) {
                Channel channel = tcpChannelF.get().sync().channel();
                log.debug("Accepting mqtt connection over tcp channel at {}", channel.localAddress());
            }
            if (tlsChannelF.isPresent()) {
                Channel channel = tlsChannelF.get().sync().channel();
                log.debug("Accepting mqtt connection over tls channel at {}", channel.localAddress());
            }
            if (wsChannelF.isPresent()) {
                Channel channel = wsChannelF.get().sync().channel();
                log.debug("Accepting mqtt connection over ws channel at {}", channel.localAddress());
            }
            if (wssChannelF.isPresent()) {
                Channel channel = wssChannelF.get().sync().channel();
                log.debug("Accepting mqtt connection over wss channel at {}", channel.localAddress());
            }
            log.info("MQTT broker started");
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void shutdown() {
        log.info("Shutting down MQTT broker");
        if (tcpChannelF.isPresent()) {
            tcpChannelF.get().channel().close().syncUninterruptibly();
            log.debug("Stopped accepting mqtt connection over tcp channel");
        }
        if (tlsChannelF.isPresent()) {
            tlsChannelF.get().channel().close().syncUninterruptibly();
            log.debug("Stopped accepting mqtt connection over tls channel");
        }
        if (wsChannelF.isPresent()) {
            wsChannelF.get().channel().close().syncUninterruptibly();
            log.debug("Stopped accepting mqtt connection over ws channel");
        }
        if (wssChannelF.isPresent()) {
            wssChannelF.get().channel().close().syncUninterruptibly();
            log.debug("Stopped accepting mqtt connection over wss channel");
        }
        subBroker.disconnectAll(options.disconnectRate()).join();
        log.debug("All mqtt connection closed");

        sessionContext.awaitBgTaskDone();
        log.debug("All background tasks done");

        subBroker.shutdown();
        log.debug("Transient session broker shutdown");

        bossGroup.shutdownGracefully().syncUninterruptibly();
        log.debug("Boss group shutdown");
        workerGroup.shutdownGracefully().syncUninterruptibly();
        log.debug("Worker group shutdown");
        log.info("MQTT broker shutdown");
    }

    protected abstract ILocalSessionBrokerServer buildLocalSessionBroker();

    private ChannelFuture bindTCPChannel(ConnListenerBuilder.TCPConnListenerBuilder builder) {
        return buildChannel(builder, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("connRateLimiter", connRateLimitHandler);
                pipeline.addLast("trafficShaper",
                        new ChannelTrafficShapingHandler(options.writeLimit(), options.readLimit()));
                pipeline.addLast("decoder", new MqttDecoder(options.maxBytesInMessage()));
                pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                pipeline.addLast(MQTTMessageDebounceHandler.NAME, new MQTTMessageDebounceHandler());
                pipeline.addLast(MQTTConnectHandler.NAME, new MQTTConnectHandler(options.connectTimeoutSeconds()));
            }
        });
    }

    private ChannelFuture bindTLSChannel(ConnListenerBuilder.TLSConnListenerBuilder builder) {
        return buildChannel(builder, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("connRateLimiter", connRateLimitHandler);
                pipeline.addLast("ssl", builder.sslContext.newHandler(ch.alloc()));
                pipeline.addLast("trafficShaper",
                        new ChannelTrafficShapingHandler(options.writeLimit(), options.readLimit()));
                pipeline.addLast("decoder", new MqttDecoder(options.maxBytesInMessage()));
                pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                pipeline.addLast(MQTTMessageDebounceHandler.NAME, new MQTTMessageDebounceHandler());
                pipeline.addLast(MQTTConnectHandler.NAME, new MQTTConnectHandler(options.connectTimeoutSeconds()));
            }
        });
    }

    private ChannelFuture bindWSChannel(ConnListenerBuilder.WSConnListenerBuilder builder) {
        return buildChannel(builder, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("connRateLimiter", connRateLimitHandler);
                pipeline.addLast("trafficShaper",
                        new ChannelTrafficShapingHandler(options.writeLimit(), options.readLimit()));
                pipeline.addLast("httpEncoder", new HttpResponseEncoder());
                pipeline.addLast("httpDecoder", new HttpRequestDecoder());
                pipeline.addLast("remoteAddr", remoteAddrHandler);
                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                pipeline.addLast("webSocketHandler", new WebSocketServerProtocolHandler(builder.path(),
                        MQTT_SUBPROTOCOL_CSV_LIST));
                pipeline.addLast("ws2bytebufDecoder", new WebSocketFrameToByteBufDecoder());
                pipeline.addLast("bytebuf2wsEncoder", new ByteBufToWebSocketFrameEncoder());
                pipeline.addLast("decoder", new MqttDecoder(options.maxBytesInMessage()));
                pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                pipeline.addLast(MQTTMessageDebounceHandler.NAME, new MQTTMessageDebounceHandler());
                pipeline.addLast(MQTTConnectHandler.NAME, new MQTTConnectHandler(options.connectTimeoutSeconds()));
            }
        });
    }

    private ChannelFuture bindWSSChannel(ConnListenerBuilder.WSSConnListenerBuilder builder) {
        return buildChannel(builder, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("connRateLimiter", connRateLimitHandler);
                pipeline.addLast("ssl", builder.sslContext.newHandler(ch.alloc()));
                pipeline.addLast("trafficShaper",
                        new ChannelTrafficShapingHandler(options.writeLimit(), options.readLimit()));
                pipeline.addLast("httpEncoder", new HttpResponseEncoder());
                pipeline.addLast("httpDecoder", new HttpRequestDecoder());
                pipeline.addLast("remoteAddr", remoteAddrHandler);
                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                pipeline.addLast("webSocketHandler", new WebSocketServerProtocolHandler(builder.path(),
                        MQTT_SUBPROTOCOL_CSV_LIST));
                pipeline.addLast("ws2bytebufDecoder", new WebSocketFrameToByteBufDecoder());
                pipeline.addLast("bytebuf2wsEncoder", new ByteBufToWebSocketFrameEncoder());
                pipeline.addLast("decoder", new MqttDecoder(options.maxBytesInMessage()));
                pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                pipeline.addLast(MQTTMessageDebounceHandler.NAME, new MQTTMessageDebounceHandler());
                pipeline.addLast(MQTTConnectHandler.NAME, new MQTTConnectHandler(options.connectTimeoutSeconds()));
            }
        });
    }

    private ChannelFuture buildChannel(ConnListenerBuilder<?> builder, final ChannelInitializer<?> channelInitializer) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NettyUtil.determineServerSocketChannelClass(bossGroup))
                .childHandler(channelInitializer)
                .childAttr(ChannelAttrs.MQTT_SESSION_CTX, sessionContext);
        builder.options.forEach((k, v) -> b.option((ChannelOption<? super Object>) k, v));
        builder.childOptions.forEach((k, v) -> b.childOption((ChannelOption<? super Object>) k, v));
        // Bind and start to accept incoming connections.
        return b.bind(host, builder.port);
    }
}
