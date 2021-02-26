package com.li.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) throws IOException {

        // 创建ServerSocketChannel -> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到Selector对象
        Selector selector = Selector.open();
        // 绑定端口2648，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(2648));
        // 设置ServerSocketChannel为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 把serverSocketChannel注册到selector中，关心的事件为：OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 循环等待客户端连接
        while(true) {
            // 等待1秒钟，没有事件发生，则继续循环
            if (selector.select(1000) == 0) { // 没有事件发生
                System.out.println("服务器等待了1秒钟，无连接");
                continue;
            }
            // 如果返回的有事件发生的通道个数大于0，获取SelectionKey（关注事件）集合，通过 SelectionKey反向获取channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 变量selectionKeys，使用迭代器
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取SelectionKey
                SelectionKey selectionKey = keyIterator.next();
                // 根据key对应的通道发生的事件做相应处理
                if (selectionKey.isAcceptable()) { // 对应的是OP_ACCEPT，即有客户端连接
                    // 虽然accept()方法阻塞，但是调用该方法的前提是已经有客户端连接，因此该方法会被立即执行
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 设置socketChannel为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将SocketChannel注册到Selector，关注的事件是OP_READ，同时关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) { // 对应的是OP_READ，即有有数据可读
                    // 通过 SelectionKey 反向获取 SocketChannel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    // 获取该channel关联的 buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    // 读取socketChannel中的数据
                    socketChannel.read(byteBuffer);
                    System.out.println("接收客户端数据：" + new String(byteBuffer.array()));
                }

                // 手动移除当前selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
