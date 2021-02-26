package com.li.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    public static void main(String[] args) throws IOException {

        // 思路：使用线程池机制改善
        // 1. 创建一个线程池
        ExecutorService pool = Executors.newCachedThreadPool();

        // 2. 创建一个 ServerSocket 等待客户端的连接
        ServerSocket serverSocket = new ServerSocket(2648);
        System.out.println("服务端已启动");

        while (true) {

            // 3. 如果有客户端连接，就创建一个线程，与之通讯
            final Socket socket = serverSocket.accept() ;
            System.out.println("连接到客户端");
            pool.execute(() -> {

                // 4. 处理客户端发送的数据
                handler(socket);
            });
        }

    }

    // 接收客户端发送的消息
    public static void handler(Socket socket) {

        // 获取输入流
        try {
            System.out.println("当前线程：" + Thread.currentThread().getId() + ", " + Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();
            byte[] b = new byte[1024];
            int len;
            // 循环读取客户端发送的数据
            while ((len = inputStream.read(b)) != -1) {
                System.out.println("当前线程：" + Thread.currentThread().getId() + ", " + Thread.currentThread().getName());
                System.out.println(new String(b, 0, len));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
