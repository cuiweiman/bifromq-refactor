package com.zachary.bifromq.mqtt.handler.ws;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

public class WebSocketFrameToByteBufDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

    @Override
    protected void decode(ChannelHandlerContext chc, BinaryWebSocketFrame frame, List<Object> out) {
        // convert the frame to a ByteBuf
        ByteBuf bb = frame.content();
        bb.retain();
        out.add(bb);
    }
}
