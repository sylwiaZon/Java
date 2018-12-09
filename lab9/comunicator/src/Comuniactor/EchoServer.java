package Comuniactor;

import DataBase.DataBase;
import DataBase.User;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EchoServer extends Application {
    public DataBase dataBase;

    public static void main(String[] args) throws IOException { launch(args);    }

    @Override
    public void start(Stage stage) throws Exception {
        boolean connected = false;
        try {
            dataBase = new DataBase();
            int i = 0;
            while(i <  3 && !connected) {
                dataBase.connect();
                connected = dataBase.connected();
                TimeUnit.SECONDS.sleep(3);
                i++;
            }
        } catch (Exception e) { e.printStackTrace(); }
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 6666");
            System.exit(-1);
        }
        while(true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Accept failed: 6666");
                System.exit(-1);
            }
            Socket finalClientSocket = clientSocket;
            System.out.println(finalClientSocket);
            new Thread(() -> {
                try {

                    PrintWriter out = new PrintWriter(finalClientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(finalClientSocket.getInputStream()));
                    String inputLine;
                    User user =  new User();
                    int id = 0;
                    while(id == 0){
                        user.setLogin(in.readLine());
                        user.setPassword(in.readLine());
                        User logged = dataBase.login(user);
                        if(logged == null){
                        }
                        else {
                            out.println(logged.id);
                            id = logged.id;
                        }
                    }
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(finalClientSocket.getOutputStream());
                    ArrayList<User> users = dataBase.getUsers();
                    objectOutputStream.writeObject(users);
                    while ((inputLine = in.readLine()) != null) {
                        out.println(inputLine);
                    }
                    objectOutputStream.close();
                    out.close();
                    in.close();
                    finalClientSocket.close();
                } catch (Error | IOException error) {
                    error.printStackTrace();
                }
            }).start();
        }
    }
}