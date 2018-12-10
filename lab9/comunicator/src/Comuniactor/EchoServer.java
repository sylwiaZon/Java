package Comuniactor;

import DataBase.DataBase;
import DataBase.User;
import DataBase.Message;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EchoServer extends Application {
    public DataBase dataBase;
    User user;

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
        final ArrayList<Socket> sockets = new ArrayList<>();
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
                    ObjectOutputStream out = new ObjectOutputStream(sockets.get((threadId)).getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(sockets.get(threadId).getInputStream());
                    user =  new User();
                    String command;
                    while(true){
                        command = (String) in.readObject();
                        switch (command) {
                            case "/login":
                                login(out, in);
                                break;
                            case "/register":
                                register(out, in);
                                break;
                            case "/getUsers":
                                getUsers(out, in);
                                break;
                            case "/getMessages":
                                getMessages(out,in);
                                break;
                            case "/sendMessage":
                                sendMessage(out,in);
                                break;
                        }
                    }
                } catch (Error | IOException | SQLException | ClassNotFoundException error) {
                    error.printStackTrace();
                }
            }).start();
        }
    }
    private void login(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        user.setLogin((String) in.readObject());
        user.setPassword((String) in.readObject());
        User logged = dataBase.login(user);
        if(logged == null){
            out.writeObject(0);
        }
        else {
            user = logged;
            out.writeObject(logged.id);
        }
    }
    private void register(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException, SQLException {
        user.setLogin((String) in.readObject());
        user.setPassword((String) in.readObject());
        User logged = dataBase.register(user);
        if(logged == null){
            out.writeObject(0);
        }
        else {
            user = logged;
            out.writeObject(logged.id);
        }
    }
    private void getUsers(ObjectOutputStream out, ObjectInputStream in) throws IOException {
        ArrayList<User> users = dataBase.getUsers();
        out.writeObject(users);
    }
    private void getMessages(ObjectOutputStream out, ObjectInputStream in) throws IOException {
        ArrayList<Message> messages = dataBase.getMessages(user.id);
        out.writeObject(messages);
    }
    private void sendMessage(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        Message message = (Message)in.readObject();
        dataBase.sendMessage(message);
    }
}