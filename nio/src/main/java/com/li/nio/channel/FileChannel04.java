package com.li.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class FileChannel04 {

    public static void main(String[] args) throws Exception {

        // 获取输入流和输出流
        FileInputStream fileInputStream = new FileInputStream("file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("file03.txt");
        // 获取输入流和输出类对应的通道
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        // 使用transferFrom完成文件的拷贝
        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());
        // 关闭通道和流
        outputStreamChannel.close();
        inputStreamChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }
}
