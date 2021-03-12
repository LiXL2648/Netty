package com.li.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.time.Instant;

public class NewIOClient {

    private static final long TRANSFER_SIZE = 8 * 1024 * 1024;

    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 730));
        String filename = "C:\\Users\\Li\\Music\\hello.flac";

        // 得到一个文件channel
        FileChannel fileChannel = new FileInputStream(filename).getChannel();
        Instant start = Instant.now();

        // 在Linux下一次transferTo方法就可以完成传输
        // 在windows下一次transferTo只能发送8m，如果超过8m，就需要分段传输文件，而且要记录传输的位置以及大小
        // transferTo底层使用的就是零拷贝
        long fileSize = fileChannel.size();
        long count;
        if (fileSize % TRANSFER_SIZE == 0) {
            count = fileSize / TRANSFER_SIZE;
        } else {
            count = fileSize / TRANSFER_SIZE + 1;
        }

        long total = 0;

        if (count == 1) {
            total = fileChannel.transferTo(0, fileSize, socketChannel);
        }
        else {
            long position = 0;
            for (int i = 0; i < count; i++) {
                long size;
                if (i + 1 == count) {
                    size = fileSize - i * TRANSFER_SIZE;
                } else {
                    size = TRANSFER_SIZE;
                }
                total += fileChannel.transferTo(position, size, socketChannel);
                position = i * TRANSFER_SIZE;
            }
        }
        Instant end = Instant.now();
        System.out.println("发送总字节：" + total + "，耗时：" + Duration.between(start, end).toMillis()); // 24

        fileChannel.close();
        socketChannel.close();
    }
}
