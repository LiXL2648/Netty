package com.li.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChatClient {

    // 定义属性
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 2648;

    public ChatClient() {
        init();
    }

    // 初始化Selector和SocketChannel
    public void init() {
        try {
            // 获取选择器对象
            selector = Selector.open();
            // 连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            // 设置非阻塞
            socketChannel.configureBlocking(false);
            // 将socketChannel注册到选择器上
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 获取username
            username = socketChannel.getLocalAddress().toString();
            System.out.println(username + " is OK ……");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 向服务器发送消息
    public void send(String msg) {
        msg = username + "说：" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取从服务器端恢复得消息
    public void read() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) { // 有刻意的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 启动客户端
        ChatClient chatClient = new ChatClient();
        // 启动一个线程，每隔3秒钟，从服务器读取数据
        new Thread(() -> {
            while (true) {
                chatClient.read();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            chatClient.send(msg);
        }
    }
}