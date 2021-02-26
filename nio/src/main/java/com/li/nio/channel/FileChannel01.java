package com.li.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileChannel01 {

    public static void main(String[] args) throws Exception {

        // 保存的文本，获取字节数组
        String text = "Hello, 李丽璇";
        byte[] bytes = text.getBytes(Charset.defaultCharset());
        // 创建一个输入流
        FileOutputStream fileOutputStream = new FileOutputStream("file01.txt");
        // 通过 FileOutputStream 获取对应的 FileChannel，该真实类型为 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        // 将文本写入缓冲区
        buffer.put(bytes);
        // 对缓冲区进行反转和读操作
        buffer.flip();
        fileChannel.write(buffer);
        // 关闭流和通道
        fileChannel.close();
        fileOutputStream.close();
    }
}
