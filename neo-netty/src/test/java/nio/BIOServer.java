package nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(8000);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream()) {

                        byte[] bytes = new byte[1024];
                        while (inputStream.read(bytes) != -1) {
                            outputStream.write(bytes);
                            bytes = new byte[1024];
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
