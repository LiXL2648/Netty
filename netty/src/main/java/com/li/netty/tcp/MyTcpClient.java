package com.li.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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

                        private int count = 0;
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    for (int i = 0; i < 10; i++) {
                                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, Server, I am No." + i + "\n", CharsetUtil.UTF_8));
                                    }
                                }

                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {

                                    byte[] bytes = new byte[byteBuf.readableBytes()];
                                    byteBuf.readBytes(bytes);
                                    String msg = new String(bytes, CharsetUtil.UTF_8);
                                    System.out.println("客户端接收到消息：" + msg);
                                    System.out.println("客户端接收到消息量：" + (++count));
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
