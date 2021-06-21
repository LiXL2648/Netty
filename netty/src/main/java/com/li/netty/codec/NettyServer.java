package com.li.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {

    public static void main(String[] args) {

        // 创建 bossGroup 和 workerGroup
        // bossGroup只处理连接请求，真正和客户端业务处理，会交给 workerGroup 完成
        // 两个线程组都是无限循环
        // bossGroup 和 workerGroup 含有的子线程（NioEventLoop）的个数，默认是 cpu 核数 * 2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 给 workerGroup 的 EventLoop对应的管道设置处理器
                        // 给管道设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 加入解码器，必须指定对哪种对象进行解码
                            pipeline.addLast("ecoder", new ProtobufDecoder(StudentPojo.Student.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler()); // 加入自定义处理器
                        }
                    });
            System.out.println("服务器 is ready ......");

            // 绑定端口并且同步，生成一个ChannelFuture，启动服务器
            ChannelFuture channelFuture = bootstrap.bind(2648).sync();

            // 注册监听器，监听绑定端口成功的回调
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("监听端口 2648 成功");
                } else {
                    System.out.println("监听端口 2648 失败");
                }
            });
            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
