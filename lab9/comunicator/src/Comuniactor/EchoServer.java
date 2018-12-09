package Comuniactor;

import DataBase.DataBase;
import DataBase.User;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
        final List<Socket> sockets = new ArrayList<>();
        int cons = 0;
        while(true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Accept failed: 6666");
                System.exit(-1);
            }
            sockets.add(clientSocket);
            new Thread(() -> {
                try {
                    int threadId = sockets.size()-1;
                    ObjectOutputStream out = new ObjectOutputStream(sockets.get(threadId).getOutputStream());
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(sockets.get(threadId).getInputStream()));
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
                            out.writeInt(logged.id);
                            id = logged.id;
                        }
                    }
                    ArrayList<User> users = dataBase.getUsers();
                    System.out.println(users);
                    out.writeObject(users);
                    while ((inputLine = in.readLine()) != null) {
                        out.writeChars(inputLine);
                    }
                    out.close();
                    in.close();
                    sockets.get(threadId).close();
                } catch (Error | IOException error) {
                    error.printStackTrace();
                }
            }).start();
        }
    }
}