package Comuniactor;

import DataBase.User;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class EchoClient extends Application {
    public User loggedUser;
    public ArrayList<User> registeredUsers;
    private TextField password, login;
    PrintWriter out;
    BufferedReader in;
    Socket echoSocket;
    public static void main(String[] args) throws IOException {
        launch(args);
       /* BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in));
        String userInput;
        System.out.println("Type a message: ");
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            System.out.println("echo: " + in.readLine()); }
        out.close();
        in.close();
        stdIn.close();
        echoSocket.close();*/
    }

    @Override
    public void start(Stage stage) throws Exception {
        echoSocket = null;
        out = null;
        in = null;
        try {
            echoSocket = new Socket("localhost", 6666);
            out = new PrintWriter(echoSocket.getOutputStream(), true); in = new BufferedReader(new InputStreamReader( echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost."); System.exit(1);
        } catch (IOException e) { System.err.println("Couldn't get I/O for " + "the connection to: localhost."); System.exit(1);
        }
        loggedUser = new User();
        GridPane gridPane = new GridPane();
        gridPane.add(new Text("Login"), 0, 0);
        login = new TextField();
        gridPane.add(login, 1,0);
        gridPane.add(new Text("Password"), 0,1);
        password = new TextField();
        gridPane.add(password, 1,1);
        Button apply = login(stage);
        GridPane.setHalignment(apply, HPos.RIGHT);
        gridPane.add(apply, 1,2);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefWidth(1000);
        gridPane.setStyle("-fx-background-color: #D4CDCD");
        Scene scene = new Scene(gridPane,1000,600);
        stage.setTitle("Data Base");
        stage.setScene(scene);
        stage.show();
    }

    private Button login(Stage stage){
        Button button = new Button("Go!");
        button.setOnAction(value -> {
            out.println(login.getText());
            out.println(password.getText());
            try {
                int id = Integer.parseInt(in.readLine());
                if(id != 0) {
                    loggedUser = new User(id, login.getText(), password.getText());
                } else {
                    loggedUser = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (loggedUser == null) {
                wrongUser(stage);
            } else {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(echoSocket.getInputStream());
                    registeredUsers = (ArrayList<User>) objectInputStream.readObject();
                    loggedIn(stage);
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        return button;
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
        stage.setTitle("Data Base");
        stage.setScene(scene);
        stage.show();
    }

    private void loggedIn(Stage stage){
        BorderPane manePage = new BorderPane();
        BorderPane upperMenu = new BorderPane();
        ListView listView = new ListView();
        for(User us: registeredUsers){
            if(us.id != loggedUser.id) {
                listView.getItems().add(us.login);
            }
        }
        upperMenu.setPadding(new Insets(10, 10, 10, 10));
        upperMenu.setTop(new Text("Who do you want to talk to?"));
        upperMenu.setCenter(listView);
        Button talk = new Button("Chose");
        BorderPane.setAlignment(talk,Pos.CENTER_RIGHT);
        BorderPane.setMargin(talk,new Insets(10,10,10,10));
        upperMenu.setRight(talk);
        listView.setMaxHeight(100);
        manePage.setTop(upperMenu);
        manePage.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(manePage,1000,600);
        stage.setTitle("Data Base");
        stage.setScene(scene);
        stage.show();
    }
}