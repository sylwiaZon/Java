package Game;

import DataBase.DataBase;
import DataBase.User;
import DataBase.Game;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                                getUsers(out);
                                break;
                            case "/getGames":
                                getGames(out);
                            case "/newGame":
                                newGame(out,in);
                                break;
                            case "/acceptGame":
                                acceptGame(in);
                                break;
                            case "/declineGame":
                                declineGame(in);
                                break;
                            case "/makeMove":
                                makeMove(in);
                                break;
                            case "/getMoves":
                                getMoves(out,in);
                                break;
                        }
                    }
                } catch (Error | IOException | SQLException | ClassNotFoundException error) {
                    error.printStackTrace();
                }
            }).start();
        }
    }

    private void getGames(ObjectOutputStream out) throws IOException {
        ArrayList<Game> games = dataBase.getGames();
        out.writeObject(games);
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
    private void getUsers(ObjectOutputStream out) throws IOException {
        ArrayList<User> users = dataBase.getUsers();
        out.writeObject(users);
    }
    private void newGame(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        int askedUser;
        askedUser = (int) in.readObject();
        int game = dataBase.newGame(user,askedUser);
        out.writeObject(game);
    }
    private void acceptGame(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int gameToAccept = (int) in.readObject();
        dataBase.acceptGame(gameToAccept);
    }
    private void declineGame(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int gameToDecline = (int) in.readObject();
        dataBase.declineGame(gameToDecline);
    }
    private void makeMove(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int moveToMake = (int) in.readObject();
        int currentGame = (int) in.readObject();
        dataBase.makeMove(currentGame,moveToMake,user.id);
    }
    private void getMoves(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        int game = (int) in.readObject();
        int user = (int) in.readObject();
        ArrayList<String> moves = dataBase.getMoves(game,user);
        out.writeObject(moves);
    }
}