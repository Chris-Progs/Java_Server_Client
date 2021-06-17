
package servera;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

/**
 * Portfolio Question 4
 * This program sets up the Server side for the client to connect and
 * it also handles the user login by validating the username and password 
 */
public class ServerA {

    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;
    
    public static void main(String[] args) {
        startServer();
    }
    // Connect to the client via sockets
    public static void startServer() {

        try (ServerSocket serverSocket = new ServerSocket(2021)) {
            System.out.println("Server is listening on port #" + serverSocket.getLocalPort()); 
            try (Socket clientSocket = serverSocket.accept()) {
                String clientHostName = clientSocket.getInetAddress().getHostName(); 
                int clientPortNum = clientSocket.getLocalPort(); 
		System.out.println("Connected from " + clientHostName + " on #" + clientPortNum);
                openStreams(clientSocket);
                createUser();
                closeStreams();
            }
        } catch (IOException | NoSuchAlgorithmException e) { 
            System.out.println("Error occured, closing program.");
            System.exit(1);
        }
    }
    // Opens data output and input streams
    public static void openStreams(Socket clientSocket) throws IOException {
        outputStream = new DataOutputStream(clientSocket.getOutputStream());
        outputStream.flush();
        inputStream = new DataInputStream(clientSocket.getInputStream());
    }
    // Closes data input and output streams
    public static void closeStreams() throws IOException {
        inputStream.close();
        outputStream.close();
    }
    // Creates a new user to be validated 
    public static void createUser() throws IOException, NoSuchAlgorithmException {
        outputStream.writeUTF("To create a user please enter a username: ");
        String userName = inputStream.readUTF();
        outputStream.flush();        
        outputStream.writeUTF("To create a user please enter a password: ");
        String password = inputStream.readUTF();
        outputStream.flush();
        User.newUser(userName, password);
        login();
        }
    // Validate user login info
    public static void login() throws IOException, NoSuchAlgorithmException {
        outputStream.writeUTF("To login please enter your username: ");
        String userName = inputStream.readUTF();
        outputStream.flush();
        if (User.checkUsername(userName)) {
            outputStream.writeUTF("To login please enter your password: ");
            String password = inputStream.readUTF();
            outputStream.flush();
            if (User.checkUser(userName, password)) {
                outputStream.writeUTF("Login Success - Type exit to close program.");
                outputStream.flush();
                System.out.println("Program closing.");
            } else {
                outputStream.writeUTF("Incorrect username or password, please restart program.");
                outputStream.flush();
                System.exit(0);
            }
        } else {
            login();
        }
    }
}
