package com.li.nio.zerocopy;

    import java.io.DataInputStream;
    import java.io.IOException;
    import java.net.ServerSocket;
    import java.net.Socket;

    public class OldIOServer {

        public static void main(String[] args) throws IOException {

            ServerSocket serverSocket = new ServerSocket(2648);
            while (true) {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                byte[] bytes = new byte[4096];
                while (dataInputStream.read(bytes) != -1) {

                }
            }
        }
    }
