package com.li.netty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    // 当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        // 随机发送Student或者Worker 对象
        int r = new Random().nextInt(3);
        MyDataInfo.Person person;
        if (0 == r) {
            person = MyDataInfo.Person.newBuilder().setDataType(MyDataInfo.Person.DataType.StudentType)
                    .setStudent(MyDataInfo.Student.newBuilder().setId(1).setName("LiXL").build()).build();
        } else {
            person = MyDataInfo.Person.newBuilder().setDataType(MyDataInfo.Person.DataType.WorkerType)
                    .setWorker(MyDataInfo.Worker.newBuilder().setId(2).setName("LiLX").build()).build();
        }
        ctx.writeAndFlush(person);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务端回复的消息是：" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
