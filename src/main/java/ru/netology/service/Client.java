package ru.netology.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8989;

    public static void main(String[] args) {
        try {
            InetAddress inetAddress = Inet4Address.getByName(SERVER_ADDRESS);
            System.out.println("Client running on port " + SERVER_PORT);
            try (Socket socket = new Socket(inetAddress, SERVER_PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                Scanner scanner = new Scanner(System.in);

                System.out.println("Enter the request in JSON format: ");
                String jsonRequest = scanner.nextLine();


                // Отправляем запрос на сервер
                out.println(jsonRequest);

                // Читаем ответ от сервера
                String response;
                while ((response = in.readLine()) != null && !response.isEmpty()) {
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
