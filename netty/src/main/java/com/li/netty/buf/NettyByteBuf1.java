package com.li.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf1 {

    public static void main(String[] args) {

        // 创建一个 ByteBuf
        // 创建ByteBuf对象，该对象包含一个 byte[10] 的数组
        ByteBuf buffer = Unpooled.buffer(10);

        // 往ByteBuf中保存数据
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        // 输出ByteBuf，在 Netty 中，buffer 输出时不需要进行 flip 反转，因为Netty的底层维护了
        // readIndex 和 writeIndex
        System.out.println("capacity = " + buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            // System.out.println(buffer.getByte(i));
            System.out.println(buffer.readByte());
        }
    }
}
