package com.li.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {

    public static void main(String[] args) throws IOException {

        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 提供服务器端ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 2648);
        // 连接服务器，该方法不会阻塞
        if(!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞");
            }
        }
        // 如果连接成功，则发送数据
        String text = "hello";
        // 将字节数组写入缓存中，指定缓存的大小为字节数组的大小
        ByteBuffer byteBuffer = ByteBuffer.wrap(text.getBytes());
        // 发送数据，将buffer数据写入channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
