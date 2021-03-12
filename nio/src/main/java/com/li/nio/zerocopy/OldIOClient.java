package com.li.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;

public class OldIOClient {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 2648);
        String fileName = "C:\\Users\\Li\\Music\\hello.flac";
        FileInputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] bytes = new byte[4096];
        long total = 0;
        long len;
        Instant start = Instant.now();

        while ((len = inputStream.read(bytes)) != -1) {
            total += len;
            dataOutputStream.write(bytes);
        }

        Instant end = Instant.now();
        System.out.println("发送总字节：" + total + "，耗时：" + Duration.between(start, end).toMillis()); // 170
        dataOutputStream.close();
        inputStream.close();
        socket.close();
    }
}
