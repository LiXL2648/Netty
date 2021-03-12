package com.li.netty.test.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    public static void main(String[] args) {

        // 客户端需要一个事件循环组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {


            // 创建客户端启动对象，使用的是Bootstrap，而不是ServerBootstrap
            Bootstrap bootstrap = new Bootstrap();
            // 设置相关参数
            bootstrap.group(eventExecutors) // 设置线程组
                .channel(NioSocketChannel.class) // 设置客户端通道的实现类（反射）
                .handler(new ChannelInitializer<SocketChannel>() { // 给 事件循环组 的 EventLoop对应的管道设置处理器
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyClientHandler()); // 加入自定义处理器
                    }
                });
            System.out.println("服务器 is ready ......");
            // 连接主机和端口并且同步，生成一个ChannelFuture，开启客户端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 2648).sync();
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
