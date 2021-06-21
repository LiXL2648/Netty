package com.li.netty.outAndInbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) {

        System.out.println("从服务端接收消息：" + aLong);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);

        // "abcdabcdabcdabcd" 是16个字节
        // 该字处理的前一个处理器是MyLongToByteEnCode，其父类是 MessageToByteEncoder
        // MessageToByteEncoder中有一个write方法，在该方法中有一个判断if (this.acceptOutboundMessage(msg))，即当前 msg 是否应该处理的类型
        // 如果是，则调用encode方法，如果不是，则将mes直接写出去。
        // 因此编写 Encoder 时要注意传入的数据类型和处理的数据类型一致
        // ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
