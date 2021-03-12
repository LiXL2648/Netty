package com.li.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class ChatServer {

    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenerChannel;
    private static final int PORT = 2648;

    // 构造器，进行初始化操作
    public ChatServer() {
        init();
    }

    // 初始化方法
    public void init() {

        try {
            // 得到选择器对象selector
            selector = selector.open();
            // 得到ServerSocketChannel对象listenerChannel
            listenerChannel = ServerSocketChannel.open();
            // 绑定端口
            listenerChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞
            listenerChannel.configureBlocking(false);
            // 注册listenerChannel到selector中，关心的事件为：OP_ACCEPT
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听方法
    public void listener() {

        try {
            // 循环等待客户端连接
            while (true) {
                // 等待2秒钟，没有事件发生，则继续循环监听
                if (selector.select() == 0) {
                    System.out.println("服务器等待了2秒钟，无连接");
                    continue;
                }

                // 如果返回的有事件发生的通道个数大于0，获取SelectionKey（关注事件）集合，通过 SelectionKey反向获取channel
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    // 获取SelectionKey
                    SelectionKey selectionKey = iterator.next();
                    // 根据key对应的通道发生的事件做相应处理
                    if (selectionKey.isAcceptable()) { // 对应的是OP_ACCEPT，即有客户端连接
                        // 获取连接的客户端
                        SocketChannel socketChannel = listenerChannel.accept();
                        // 设置socketChannel为非阻塞
                        socketChannel.configureBlocking(false);
                        // 将SocketChannel注册到Selector，关注的事件是OP_READ
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        // 提示客户端上线
                        System.out.println(socketChannel.getRemoteAddress() + " 上线了");
                    }

                    if (selectionKey.isReadable()) { // 对应的是OP_READ，即有有数据可读
                        // 读取通道数据
                        read(selectionKey);
                    }

                    // 手动移除当前selectionKey，防止重复操作
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取通道数据
    private void read(SelectionKey selectionKey) {

        SocketChannel socketChannel = null;
        try {
            // 通过 SelectionKey 反向获取 SocketChannel
            socketChannel = (SocketChannel) selectionKey.channel();
            // 创建buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            // 读取socketChannel中的数据
            int len = socketChannel.read(byteBuffer);
            if (len > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("来自客户端 " + socketChannel.getRemoteAddress() + " 的消息：" + msg);
                // 向其他客户端转发消息
                write(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println("客户端 " + socketChannel.getRemoteAddress() + "离线了");
                // 取消注册
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 向其他客户端转发消息
    private void write(String msg, SocketChannel socketChannel) throws IOException {

        // 服务器转发消息
        System.out.println("服务器转发消息中……");
        // 遍历所有注册到selector上的socketChannel
        for (SelectionKey selectionKey : selector.keys()) {
            SelectableChannel targetChannel = selectionKey.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != socketChannel) {
                // 转型
                SocketChannel channel = (SocketChannel) targetChannel;
                // 将消息存储在buffer中
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer数据写入通道
                channel.write(byteBuffer);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.listener();
    }
}
