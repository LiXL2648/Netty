package com.li.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.UUID;

public class MyTcpServer {

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup boosGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {

                                private int count = 0;
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {

                                    byte[] bytes = new byte[byteBuf.readableBytes()];
                                    byteBuf.readBytes(bytes);
                                    String msg = new String(bytes, CharsetUtil.UTF_8);
                                    System.out.println("服务端接收到消息：" + msg);
                                    System.out.println("服务端接收到消息量：" + (++count));

                                    channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString() + "\n", CharsetUtil.UTF_8));
                                }
                            });
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(2648).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
