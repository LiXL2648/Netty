package com.li.netty.outAndInbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyLongToByteEnCode extends MessageToByteEncoder<Long> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long aLong, ByteBuf byteBuf) throws Exception {

        System.out.println("MyLongToByteEnCode.encode 被调用");
        System.out.println("msg=" + aLong);
        byteBuf.writeLong(aLong);
    }
}
