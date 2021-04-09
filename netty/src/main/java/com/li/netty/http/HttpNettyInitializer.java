package com.li.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpNettyInitializer extends ChannelInitializer<SocketChannel> {

    // 向管道中加入处理器
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        // 得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        // 加入 Netty 提供处理Http的编解码处理器 HttpServerCodec，codec = coder + decoder
        pipeline.addLast("myEodec", new HttpServerCodec());
        pipeline.addLast("myandler", new HttpNettyServerHandler());
    }
}
