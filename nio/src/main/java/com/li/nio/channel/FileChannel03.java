package com.li.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannel03 {

    public static void main(String[] args) throws Exception {

        // 获取输入流和输出流
        FileInputStream fileInputStream = new FileInputStream("file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("file02.txt");
        // 获取输入流和输出类对应的通道
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将输入流通道的数据写入缓冲区
        while (inputStreamChannel.read(byteBuffer) != -1) {
            // 进行反转
            byteBuffer.flip();
            // 输出流通道从缓冲区中读取数据
            outputStreamChannel.write(byteBuffer);
            // 清空缓冲区
            // byteBuffer.clear();
        }
        // 关闭通道和流
        outputStreamChannel.close();
        inputStreamChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }
}
