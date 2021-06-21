package com.li.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyHeartbeatServer {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建启动程序
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) // 在bossGroup中增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入 Netty 提供的一个 IdleStateHandler 处理器
                            /*
                            1. IdleStateHandler 是 Netty 提供处理空闲状态的处理器
                            2. readerIdleTime：表示多长时间没有读操作，就会发送心跳检测包，检测是否仍是连接状态
                            3. writerIdleTime：表示多长时间没有写操作，也会发送心跳检测包，检测是否仍是连接状态
                            4. allIdleTime：表示多长时间没有读写操作，也会发送心跳检测包，检测是否仍是连接状态
                            5. 当IdleStateHandler 一旦被触发后，就会传递给管道的下一个handler去处理，通过回调触发下一个Handler的userEventTiggered，
                            在该方法中处理 IdleStateEvent（读空闲、写空闲和读写空闲）
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler（自定义）
                            pipeline.addLast(new MyHeartbeatHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(2648).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
