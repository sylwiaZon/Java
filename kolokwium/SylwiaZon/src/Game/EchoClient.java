package Game;

import DataBase.Game;
import DataBase.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EchoClient extends Application {
    public User loggedUser;
    public ArrayList<User> registeredUsers;
    private TextField password, login;
    ObjectOutputStream out;
    ObjectInputStream in;
    User selectedUser;
    TextField messageToSend;
    ArrayList<Game> games;
    ScheduledExecutorService scheduledExecutorService;
    public static void main(String[] args) throws IOException { launch(args);    }

    @Override
    public void start(Stage stage) throws Exception {
        selectedUser = null;
        Socket echoSocket = null;
        out = null;
        in = null;
        try {
            echoSocket = new Socket("localhost", 6666);
            out = new ObjectOutputStream(echoSocket.getOutputStream());
            in = new ObjectInputStream(echoSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost."); System.exit(1);
        } catch (IOException e) { System.err.println("Couldn't get I/O for " + "the connection to: localhost."); System.exit(1);
        }
        loggedUser = new User();
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Login"), 0, 0);
        login = new TextField();
        gridPane.add(login, 1,0,2,1);
        gridPane.add(new Text("Password"), 0,1);
        password = new TextField();
        gridPane.add(password, 1,1,2,1);
        Button apply = login(stage);
        GridPane.setHalignment(apply,HPos.RIGHT);
        gridPane.add(apply, 2,2);
        Button register = register(stage);
        gridPane.add(register, 1,2);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefWidth(1000);
        gridPane.setStyle("-fx-background-color: #D4CDCD");
        Scene scene = new Scene(gridPane,1000,600);
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.show();
    }

    private Button register(Stage stage){
        Button button = new Button("Register!");
        button.setOnAction(value -> {
            try {
                out.writeObject("/register");
                out.writeObject(login.getText());
                out.writeObject(password.getText());
                int id = (int) in.readObject();
                System.out.println(id);
                if(id != 0) {
                    loggedUser = new User(id, login.getText(), password.getText());
                } else {
                    loggedUser = null;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (loggedUser == null) {
                wrongLogin(stage);
            } else {
                logging(stage);
            }
        });
        return button;
    }

    private void logging(Stage stage) {
        try {
            registeredUsers = getRegisteredUsers();
            System.out.println(registeredUsers);
            out.writeObject("/getGames");
            games = (ArrayList<Game>) in.readObject();
            System.out.println(games);
            loggedIn(stage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Button login(Stage stage){
        Button button = new Button("Log in!");
        button.setOnAction(value -> {
            try {
                out.writeObject("/login");
                out.writeObject(login.getText());
                out.writeObject(password.getText());
                int id = (int) in.readObject();
                if(id != 0) {
                    loggedUser = new User(id, login.getText(), password.getText());
                } else {
                    loggedUser = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (loggedUser == null) {
                wrongUser(stage);
            } else {
                logging(stage);

            }
        });
        return button;
    }

    private ArrayList<User> getRegisteredUsers() throws IOException, ClassNotFoundException {
        out.writeObject("/getUsers");
        ArrayList<User> users = (ArrayList<User>) in.readObject();
        int id = 0;
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).id == loggedUser.id){
                id = i;
            }
        }
        users.remove(id);
        return users;
    }

    private void wrongUser(Stage stage){
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Wrong login or password!"),0,0,2,1);
        gridPane.add(new Text("Login"), 1, 1);
        login = new TextField();
        gridPane.add(login, 2,1);
        gridPane.add(new Text("Password"), 1,2);
        password = new TextField();
        gridPane.add(password, 2,2);
        Button apply = login(stage);
        GridPane.setHalignment(apply, HPos.RIGHT);
        gridPane.add(apply, 2,3);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefWidth(1000);
        gridPane.setStyle("-fx-background-color: #D4CDCD");
        Scene scene = new Scene(gridPane,1000,600);
        stage.setScene(scene);
        stage.show();
    }

    private void wrongLogin(Stage stage){
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Login already taken!"),0,0,2,1);
        gridPane.add(new Text("Login"), 1, 1);
        login = new TextField();
        gridPane.add(login, 2,1);
        gridPane.add(new Text("Password"), 1,2);
        password = new TextField();
        gridPane.add(password, 2,2);
        Button register = register(stage);
        GridPane.setHalignment(register, HPos.RIGHT);
        gridPane.add(register, 2,3);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefWidth(1000);
        gridPane.setStyle("-fx-background-color: #D4CDCD");
        Scene scene = new Scene(gridPane,1000,600);
        stage.setScene(scene);
        stage.show();
    }

    private void loggedIn(Stage stage){
        Scene scene = new Scene(loggedStage(stage),1000,600);
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane loggedStage(Stage stage){
        BorderPane manePage = new BorderPane();
        BorderPane upperMenu = new BorderPane();
        ListView listView = new ListView();
        for(User us: registeredUsers){
            listView.getItems().add(us.login);
        }
        listView.setPrefHeight(200);
        upperMenu.setPadding(new Insets(10, 10, 10, 10));
        upperMenu.setTop(new Text("Who do you want to talk to?"));
        upperMenu.setCenter(listView);
        Button talk = new Button();
        //Button talk = selectUser(listView, stage);
        BorderPane.setAlignment(talk,Pos.CENTER_RIGHT);
        BorderPane.setMargin(listView, new Insets(10,10,10,10));
        BorderPane.setMargin(talk,new Insets(10,10,10,10));
        upperMenu.setRight(talk);
        listView.setMaxHeight(100);
        manePage.setTop(upperMenu);
        manePage.setPadding(new Insets(10, 10, 10, 10));
        return  manePage;
    }
/*
    private Button selectUser(ListView listView, Stage stage){
        Button button = new Button("Chose");
        button.setOnAction(value -> {
            ObservableList selected = listView.getSelectionModel().getSelectedIndices();
            selectedUser = registeredUsers.get((int)selected.get(0));
            /*try {
                out.writeObject("/getMessages");
                messages = (ArrayList<Message>) in.readObject();
                conversation(stage);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            Runnable runnable = () -> {
                try {
                    out.writeObject("/getMessages");
                    messages = (ArrayList<Message>) in.readObject();
                    Platform.runLater(() -> conversation(stage));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            };
            scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);

        });
        return button;
    }

    private void conversation(Stage stage){
        BorderPane manePage = loggedStage(stage);
        BorderPane conversation = new BorderPane();
        BorderPane message = new BorderPane();
        messageToSend = new TextField();
        BorderPane.setAlignment(messageToSend, Pos.CENTER);
        messageToSend.setMinWidth(800);
        message.setRight(messageToSend);
        Button send = sendMessage(stage);
        BorderPane.setAlignment(send, Pos.CENTER_RIGHT);
        conversation.setLeft(messageToSend);
        conversation.setRight(send);
        manePage.setBottom(conversation);
        VBox vBox = messages();
        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setVvalue(1.0);
        scrollPane.setStyle("-fx-fit-to-height: true; -fx-fit-to-width: true");
        BorderPane.setAlignment(scrollPane,Pos.CENTER);
        BorderPane.setMargin(conversation, new Insets(20,10,10,10));
        manePage.setCenter(scrollPane);
        Scene scene = new Scene(manePage,1000,600);
        stage.setScene(scene);
        stage.show();
    }

    private VBox messages(){
        VBox vBox = new VBox();
        GridPane gridPane;
        Text message;
        for(Message mes: messages){
            if((mes.toId == selectedUser.id && mes.fromId == loggedUser.id) || (mes.toId == loggedUser.id && mes.fromId == selectedUser.id)) {
                gridPane = new GridPane();
                gridPane.setPadding(new Insets(10, 10, 10, 10));
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                gridPane.setAlignment(Pos.CENTER);
                if(mes.fromId == loggedUser.id){
                    gridPane.setStyle("-fx-background-color: #b9bcd4");
                } else {
                    gridPane.setStyle("-fx-background-color: #d4b4b5");
                }
                gridPane.add(new Text("From: "), 0, 0);
                gridPane.add(new Text(searchUser(mes.fromId).login), 1, 0);
                gridPane.add(new Text("To: "), 0, 1);
                gridPane.add(new Text(searchUser(mes.toId).login), 1, 1);
                message = new Text(mes.message);
                GridPane.setValignment(message, VPos.CENTER);
                gridPane.add(message, 0, 2, 1, 2);
                GridPane.setMargin(gridPane, new Insets(10,10,10,10));
                VBox.setMargin(gridPane, new Insets(10));
                vBox.getChildren().add(gridPane);
            }
        }
        vBox.maxHeight(400);
        return vBox;
    }

    private User searchUser(int id){
        if(id == loggedUser.id){
            return loggedUser;
        }
        for(User us: registeredUsers){
            if(us.id == id){
                return us;
            }
        }
        return null;
    }

    private Button sendMessage(Stage stage){
        Button button = new Button("Send");
        button.setOnAction(value -> {
            try {
                Message message = new Message();
                message.fromId = loggedUser.id;
                message.toId = selectedUser.id;
                message.message = messageToSend.getText();
                out.writeObject("/sendMessage");
                out.writeObject(message);
                out.writeObject("/getMessages");
                messages = (ArrayList<Message>) in.readObject();
                conversation(stage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return button;
    }
*/
}