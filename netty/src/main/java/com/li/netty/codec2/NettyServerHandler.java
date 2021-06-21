package com.li.netty.codec2;

import com.li.netty.codec.StudentPojo;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

// 自定义一个handler，需要继承 netty 规定好的某个 HandlerAdapter（规范）
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    // 读取数据（这里可以读取客户端发送的消息）
    // ChannelHandlerContext ctx：上下文对象，含有管道 pipeline，通道 channel以及地址
    // Object msg：就是客户端发送的数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 读取从客户端发送的StudentPojo.Student
        MyDataInfo.Person person = (MyDataInfo.Person) msg;
        if (person.getDataType().equals(MyDataInfo.Person.DataType.StudentType)) {
            System.out.println("客户端发送的数据：id=" + person.getStudent().getId() + ", name=" +  person.getStudent().getName());
        } else if (person.getDataType().equals(MyDataInfo.Person.DataType.WorkerType)){
            System.out.println("客户端发送的数据：id=" + person.getWorker().getId() + ", name=" +  person.getWorker().getName());
        } else {
            System.out.println("类型传输错误");
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

        // writeAndFlush：write + Flush，将数据写入缓存，并刷新
        // 需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("2hello, 客户端~", CharsetUtil.UTF_8));
    }

    // 处理异常，需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
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
