package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] mainArray = new String[1000];
        String address = "127.0.0.1";
        int port = 33333;

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");
            while (true) {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    String clientMessage = input.readUTF().trim();
                    String[] parts = clientMessage.split("\\s+");

                    if ("exit".equals(parts[0])) {
                        output.writeUTF("OK");
                        return;
                    }

                    String result;
                    switch (parts[0]) {
                        case "set": {
                            StringBuilder message = new StringBuilder();
                            for (int i = 2; i < parts.length; i++) {
                                if (i > 2) message.append(" ");
                                message.append(parts[i]);
                            }
                            result = set(Integer.parseInt(parts[1]), message.toString(), mainArray);
                            break;
                        }
                        case "get":
                            result = get(Integer.parseInt(parts[1]), mainArray);
                            break;
                        case "delete":
                            result = delete(Integer.parseInt(parts[1]), mainArray);
                            break;
                        default:
                            result = "ERROR";
                    }

                    output.writeUTF(result);
                }
            }
        }
    }

    public static String set(int index, String text, String[] array) {
        if (index < 1 || index > 1000) return "ERROR";
        array[index] = text;
        return "OK";
    }

    public static String get(int index, String[] array) {
        if (index < 1 || index > 1000) return "ERROR";
        if (array[index] == null || array[index].isEmpty()) return "ERROR";
        return array[index];
    }

    public static String delete(int index, String[] array) {
        if (index < 1 || index > 1000) return "ERROR";
        array[index] = "";
        return "OK";
    }
}
