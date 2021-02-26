package com.li.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {

        // 获取随机输入流
        RandomAccessFile randomAccessFile = new RandomAccessFile("file01.txt", "rw");
        // 获取输入流对应的channel
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 获取内存映射文件
         * FileChannel.MapMode.READ_WRITE：使用读写模式
         * position：可直接修改的起始位置
         * size：映射到内存的大小，即将文件多少个字节映射到内存，并不是指位置索引，如果超过该大小，会报
         * java.lang.IndexOutOfBoundsException异常
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.size());
        // 修改文件内容
        mappedByteBuffer.put(0, (byte) 'h');
        // 关闭通道和文件流
        channel.close();
        randomAccessFile.close();
    }
}
