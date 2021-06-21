package com.li.netty.protocoltcp;

import io.netty.bootstrap.ServerBootstrap;
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
                            // 添加自定义解码器
                            pipeline.addLast(new MessageDecoder());
                            // 添加自定义编码器
                            pipeline.addLast(new MessageEncoder());
                            // 添加自定义处理器，处理逻辑
                            pipeline.addLast(new SimpleChannelInboundHandler<MessageProtocol>() {

                                private int count = 0;
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) {

                                    System.out.println("服务接收到消息，长度：" + messageProtocol.getLen() + ", 内容："
                                            + new String(messageProtocol.getContent(), CharsetUtil.UTF_8));
                                    System.out.println("服务端接收的消息数量：" + ++count);

                                    byte[] content = UUID.randomUUID().toString().getBytes(CharsetUtil.UTF_8);
                                    MessageProtocol messageProtocol1 = new MessageProtocol(content);
                                    channelHandlerContext.writeAndFlush(messageProtocol1);
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
