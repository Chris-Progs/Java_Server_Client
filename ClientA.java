package clienta;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
/**
 * Portfolio Question 4
 * This program sets up the Client side for the server to connect
 */
public class ClientA {

    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;
    private static Socket socket;
    private static Scanner scan = new Scanner(System.in);
    private static String serverInput;
    private static String replyToServer;

    public static void main(String[] args) {
        connectServer();
    }
    // Connect to the server
    public static void connectServer() {

        try {
            socket = new Socket("localhost", 2021);
            openStreams(socket);
            chatting();
            closeStreams();
        } catch (IOException e) {
            System.out.println("Input not correct closing program.");
            System.exit(0);
        }
    }
    // Opens data output and input streams
    public static void openStreams(Socket socket) throws IOException {
        outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new DataInputStream(socket.getInputStream());
    }
    // Closes data input and output streams
    public static void closeStreams() throws IOException {
        socket.close();
        inputStream.close();
        outputStream.close();
    }
    // Read and send messages to the server
    public static void chatting() throws IOException {

        while (true) {
            serverInput = inputStream.readUTF();
            System.out.println(serverInput);
            replyToServer = scan.nextLine();
            outputStream.writeUTF(replyToServer);
            outputStream.flush();
            if (replyToServer.equalsIgnoreCase("exit")) {
                System.exit(0);
            } else {
                chatting();
            }
        }
    }
}
