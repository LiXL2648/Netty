package com.li.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MessageDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {

        System.out.println("MessageDecoder.decode被调用了");
        int len = byteBuf.readInt();
        byte[] content = new byte[len];
        byteBuf.readBytes(content);

        MessageProtocol messageProtocol = new MessageProtocol(content);
        list.add(messageProtocol);
    }
}
