package com.li.netty.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext ctx; //上下文

    private String result; // 调用服务器提供者返回的结果

    private String param; // 客户端调用方法时，传入的参数

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        this.ctx = ctx;
    }

    // 收到服务器的数据后，调用方法
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {

        result = msg.toString();
        notify(); // 唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        ctx.close();
    }

    // 被代理对象调用，发送数据给服务端，-> wait -> 等待被唤醒 -> 返回结果
    @Override
    public synchronized Object call() throws Exception {

        ctx.writeAndFlush(param);
        wait(); // 等待channelRead获取服务端的结果后等唤醒
        return result; // 服务端返回的结果
    }

    public void setParam(String param) {
        this.param = param;
    }
}
