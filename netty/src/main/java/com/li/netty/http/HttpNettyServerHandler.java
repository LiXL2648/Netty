package com.li.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 的子类
 * HttpObject 是客户端和服务器相互通讯数据的封装
 */
public class HttpNettyServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // channelRead0 读取客户端的数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        // 判断 msg 是不是
        if (httpObject instanceof HttpRequest) {

            // 过滤请求
            HttpRequest httpRequest = (HttpRequest) httpObject;
            URI uri = new URI(httpRequest.uri());
            System.out.println(uri.getPath());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico，不做响应");
                return;
            }

            // 回复信息给浏览器，满足Http协议
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);

            // 构造一个Http响应 FullHttpResponse，并设置响应头
            FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            // 将构建好的 FullHttpResponse 返回
            channelHandlerContext.writeAndFlush(httpResponse);
        }
    }
}
