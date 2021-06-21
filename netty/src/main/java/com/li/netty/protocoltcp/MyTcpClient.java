package com.li.netty.protocoltcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class MyTcpClient {

    public static void main(String[] args) throws Exception {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 添加自定义编码器
                            pipeline.addLast(new MessageEncoder());
                            // 添加自定义解码器
                            pipeline.addLast(new MessageDecoder());
                            // 添加自定义处理器，处理逻辑
                            pipeline.addLast(new SimpleChannelInboundHandler<MessageProtocol>() {

                                private int count = 0;

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    for (int i = 0; i < 5; i++) {
                                        String msg = "Hello, LiLX " + i;
                                        byte[] content = msg.getBytes(CharsetUtil.UTF_8);
                                        MessageProtocol messageProtocol = new MessageProtocol(content);
                                        ctx.writeAndFlush(messageProtocol);
                                    }
                                }

                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) {
                                    System.out.println("客户端接收消息，长度：" + messageProtocol.getLen() + ", 内容："
                                            + new String(messageProtocol.getContent(), CharsetUtil.UTF_8));
                                    System.out.println("客户端接收消息数量：" + ++count);
                                }
                            });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 2648).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
