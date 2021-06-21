package com.li.netty.rpc.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {

    // 创建线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler nettyClientHandler;

    // 使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass},
                (proxy, method, args) -> {
                    if (nettyClientHandler == null) {
                        initClient();
                    }

                    nettyClientHandler.setParam(providerName + args[0]);
                    return executorService.submit(nettyClientHandler).get();
                });
    }

    // 初始化客户端
    private static void initClient() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        nettyClientHandler = new NettyClientHandler();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());

                        pipeline.addLast(nettyClientHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 2648).sync();
        // channelFuture.channel().closeFuture().sync();
    }
}
