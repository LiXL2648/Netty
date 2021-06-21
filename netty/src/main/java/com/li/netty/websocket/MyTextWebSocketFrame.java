package com.li.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// TextWebSocketFrame类型，表示一个文本帧
public class MyTextWebSocketFrame extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        System.out.println("服务器收到消息：" + textWebSocketFrame.text());

        // 回复消息
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("服务器时间：" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" + textWebSocketFrame.text()));
    }

    // 当web客户端连接后，触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("有连接加入：" + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {

        System.out.println("有连接退出：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println("连接出错：" + cause.getMessage());
        ctx.close();
    }
}
