package com.li.nio.buffer;

import java.nio.ByteBuffer;

public class ByteBufferPutGet {

    public static void main(String[] args) {

        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        // 类型化方式存入数据
        byteBuffer.put((byte) 26);
        byteBuffer.putShort((short) 22);
        byteBuffer.putInt(48);
        byteBuffer.putLong(30L);
        byteBuffer.putChar('李');
        // 对缓冲区进行反转，读取缓冲区的数据
        byteBuffer.flip();
        System.out.println(byteBuffer.get());
        System.out.println(byteBuffer.getShort());
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        // System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getLong());
    }
}
