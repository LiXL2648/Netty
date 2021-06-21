package com.li.netty.outAndInbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     * decode 会根据接收的数据，被调用多次，直到确定没有新的元素被添加到list，或者byteBuf没有更多的可读字节为止
     * 如果list 不为空，就会将list的内容传递给下一个 channelInboundhandler，channelInboundhandler的方法也会被调用多次
     * @param channelHandlerContext 上下文对象
     * @param byteBuf 入站的byteBuf
     * @param list List 集合，将解码后的数据传给下一个 handler
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        System.out.println("MyByteToLongDecoder.decode 被调用");
        if (byteBuf.readableBytes() >= 8) {
            list.add(byteBuf.readLong());
        }
    }
}
