package com.li.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

public class FileChannel02 {

    public static void main(String[] args) throws Exception {

        // 创建输入流
        File file = Paths.get("file01.txt").toFile();
        FileInputStream fileInputStream = new FileInputStream(file);
        // 获取输入流的通道 fileChannel
        FileChannel fileChannel = fileInputStream.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 将通道的数据写入缓冲区
        fileChannel.read(byteBuffer);
        // 对缓冲区进行反转，读取缓冲区的数据
        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array()));
        // 关闭通道和输入流
        fileChannel.close();
        fileInputStream.close();
    }
}
