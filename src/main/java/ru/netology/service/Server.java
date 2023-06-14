package ru.netology.service;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8989;
    static BuyProcessor processor;

    public void start() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server running on port " + PORT);
            while (true) {
                try (Socket client = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                    StringBuilder requestBody = new StringBuilder();

                    // Читаем тело запроса
                    while (in.ready()) {
                        requestBody.append((char) in.read());
                    }

                    if (requestBody.length() > 0) {
                        if (processor == null) {
                            processor = new BuyProcessor();  // Создание экземпляра BuyProcessor
                        }
                        String response = processor.process(requestBody);
                        out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Connection processing error");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Can't start server");
            e.printStackTrace();
        }
    }
}
