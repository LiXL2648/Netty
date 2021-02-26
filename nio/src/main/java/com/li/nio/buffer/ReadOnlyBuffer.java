package com.li.nio.buffer;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {

    public static void main(String[] args) {

        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(7);
        // 向buffer中存入数据
        for (int i = 0; i < 7; i++) {
            byteBuffer.put((byte) i);
        }
        // 对缓冲区进行反转，读取缓冲区的数据
        byteBuffer.flip();
        // 将buffer转成只读
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        // 获取buffer中的数据
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
        // 再次向buffer中存入数据
        readOnlyBuffer.put((byte) 7);
    }
}
