package com.li.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 定义一个 channel 组，管理所有 channel
    // GlobalEventExecutor.INSTANCE：全局事件执行器，是一个单例
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 表示连接建立，一旦连接，第一个被执行的方法，向其他客户端发送 xxx 加入聊天
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        // 将客户加入聊天的信息推送给其他在线的客户端
        // channelGroup的writeAndFlush方法会遍历所有channel，并发送消息
        channelGroup.writeAndFlush(LocalDateTime.now().format(formatter) + "\n[用户]" + channel.remoteAddress() + " 加入聊天\n");
        // 将当前 channel 加入 channelGroup
        channelGroup.add(channel);
    }

    // 表示 channel 处于活动状态，提示 xxx 上线了
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("[客户端]" + ctx.channel().remoteAddress() + " 上线了");
    }

    // 表示 channel 处于不活动状态，提示 XXX 下线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("[客户端]" + ctx.channel().remoteAddress() + " 下线了");
    }

    // 表示断开连接，将当前 channel 会自动从 channelGroup 中移除，向其他客户端发送 xxx 退出聊天
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        channelGroup.writeAndFlush(LocalDateTime.now().format(formatter) + "\n[用户]" + ctx.channel().remoteAddress() + " 退出聊天\n");
    }

    // 读取客户端发送的消息，进行转发和回显
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        Channel channel = ctx.channel();
        channelGroup.forEach(c -> {
            if(c != channel) { // 转发消息
                c.writeAndFlush(LocalDateTime.now().format(formatter) + "\n[用户]" + channel.remoteAddress() + " 说：" + s + "\n");
            } else { //回显消息
                channel.writeAndFlush(LocalDateTime.now().format(formatter) + "\n[自己]" + channel.remoteAddress() + " 说：" + s + "\n");
            }
        });
    }

    // 异常，关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
