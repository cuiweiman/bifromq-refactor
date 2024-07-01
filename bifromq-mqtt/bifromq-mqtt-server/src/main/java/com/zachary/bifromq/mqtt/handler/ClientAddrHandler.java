package com.zachary.bifromq.mqtt.handler;

import com.google.common.base.Strings;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@ChannelHandler.Sharable
public class ClientAddrHandler extends ChannelInboundHandlerAdapter {

    private static final String X_REAL_IP = "X-Real-IP";

    private static final String X_REAL_PORT = "X-Real-Port";


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            String realIP = req.headers().get(X_REAL_IP);
            String realPort = req.headers().get(X_REAL_PORT);

            if (!Strings.isNullOrEmpty(realIP) && !Strings.isNullOrEmpty(realPort)) {
                int port = 0;
                try {
                    port = Integer.parseInt(realPort);
                } catch (Exception e) {
                    log.warn("parseInt port fail, realPort: {}, use default port: 0", realPort);
                }
                ChannelAttrs.socketAddress(ctx, new InetSocketAddress(realIP, port));
            }
        }
        ctx.fireChannelRead(msg);
    }
}
