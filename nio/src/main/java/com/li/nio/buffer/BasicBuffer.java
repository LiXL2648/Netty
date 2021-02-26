package com.li.nio.buffer;

import java.nio.IntBuffer;

public class BasicBuffer {

    public static void main(String[] args) {
        // 创建一个Buffer，大小为5，即可以存放5个int
        IntBuffer buffer = IntBuffer.allocate(5);

        // 向 buffer 中存放数据
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put(i * i);
        }

        // 进行反转，读写切换
        buffer.flip();

        // 从 buffer 中获取数据
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }

}
