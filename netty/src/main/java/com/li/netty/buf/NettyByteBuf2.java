package com.li.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class NettyByteBuf2 {

    public static void main(String[] args) {

        // 创建 ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello, LiLX", CharsetUtil.UTF_8);
        System.out.println("byteBuf = " + byteBuf);

        // 使用 ByteBuf 相关 API
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();

            // 将 content 转成字符串
            String msg = new String(content, CharsetUtil.UTF_8);
            System.out.println(msg);

            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 0
            System.out.println(byteBuf.writerIndex()); // 11
            System.out.println(byteBuf.capacity()); // 33

            // 可读的字节数
            System.out.println(byteBuf.readableBytes()); // 11

            byteBuf.readByte();

            System.out.println(byteBuf.arrayOffset()); // 0
            System.out.println(byteBuf.readerIndex()); // 1
            System.out.println(byteBuf.readableBytes()); // 10

            byteBuf.getByte(5);

            System.out.println(byteBuf.readerIndex()); // 1
            System.out.println(byteBuf.readableBytes()); // 10

            System.out.println(byteBuf.getCharSequence(7, 10, CharsetUtil.UTF_8));
        }
    }
}
