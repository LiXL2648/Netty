package com.li.nio.buffer;

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering：依次将数据分散写入Buffer数组
 * Gathering：依次从Buffer数组聚集读取数据
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {
        // 使用 ServerSocketChannel 和
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口到ServerSocketChannel
        serverSocketChannel.bind(new InetSocketAddress(2648));
        // 获取输入流和通道
        FileOutputStream fileOutputStream = new FileOutputStream("file04.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        // 创建Buffer数组
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(5);
        buffers[1] = ByteBuffer.allocate(10);

        // 等待客户端的连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 获取客户端的消息
        long len;
        while ((len = socketChannel.read(buffers)) != -1) {
            // 对Buffer数组进行反转
            Arrays.stream(buffers).forEach(ByteBuffer::flip);
            // 将消息写入文件通道
            fileChannel.write(buffers);
            System.out.println(len);
            // 清空buffer数组
            Arrays.stream(buffers).forEach(ByteBuffer::clear);
        }
    }
}
