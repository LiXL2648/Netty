package com.li.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class MyWebSocketServer {

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 由于基于http协议，因此需要使用http的编码解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块的方式读写，需要添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            // http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段进行聚合
                            // 因为浏览器发送大量数据时，会发送多次请求，因此需要将多次请求的数据聚合起来
                            pipeline.addLast(new HttpObjectAggregator(10240));
                            // 对于WebSocket，它的数据是以帧（frame）的形式传递
                            // WebSocketFrame 有六个子类
                            // WebSocketServerProtocolHandler 能识别请求的资源
                            // WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议，保持长连接
                            // 前端请求的状态码为101
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //自定义handler，处理业务
                            pipeline.addLast(new MyTextWebSocketFrame());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(2648).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
