package com.li.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

// 自定义一个handler，需要继承 netty 规定好的某个 HandlerAdapter（规范）
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据（这里可以读取客户端发送的消息）
    // ChannelHandlerContext ctx：上下文对象，含有管道 pipeline，通道 channel以及地址
    // Object msg：就是客户端发送的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程：" + Thread.currentThread().getName());
        System.out.println("server ctx = " + ctx);
        // ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送消息是：" + byteBuf.toString(CharsetUtil.UTF_8));

        // 假如这里有个非常耗时的业务
        // 思路：异步执行 -> 提交channel 到对应的NioEventLoop 的 taskQueue 中

        // 解决方案1：用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ctx.writeAndFlush(Unpooled.copiedBuffer("1hello, 客户端", CharsetUtil.UTF_8));
        });

        // 用户程序自定义的定时任务
        ctx.channel().eventLoop().schedule(() -> {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ctx.writeAndFlush(Unpooled.copiedBuffer("3hello, 客户端", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        // writeAndFlush：write + Flush，将数据写入缓存，并刷新
        // 需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("2hello, 客户端~", CharsetUtil.UTF_8));
    }

    // 处理异常，需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
