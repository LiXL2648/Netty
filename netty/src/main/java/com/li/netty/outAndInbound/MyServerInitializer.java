package com.li.netty.outAndInbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 加入自定义入站解码器
        pipeline.addLast(new MyByteToLongDecoder());
        // 加自定义出站编码器
        pipeline.addLast(new MyLongToByteEnCode());
        pipeline.addLast(new MyServerHandler());
    }
}
