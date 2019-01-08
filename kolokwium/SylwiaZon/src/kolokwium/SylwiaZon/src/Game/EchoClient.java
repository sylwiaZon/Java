package Game;

import DataBase.Game;
import DataBase.User;
import javafx.application.Application;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;

public class EchoClient extends Application {
    public User loggedUser;
    public ArrayList<User> registeredUsers;
    private TextField password, login;
    ObjectOutputStream out;
    ObjectInputStream in;
    User selectedUser;
    ArrayList<Game> currentGames;
    Game selectedGame;
    ArrayList<Integer> circles;
    ArrayList<Integer> crosses;
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
            out.writeObject("/getGames");
            currentGames = (ArrayList<Game>) in.readObject();
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
        GridPane upperMenu = new GridPane();
        GridPane lowerMenu = new GridPane();
        GridPane wholeMenu = new GridPane();
        ListView users = new ListView();
        for(User us: registeredUsers){
            users.getItems().add(us.login);
        }
        ListView games = new ListView();
        for(Game g: currentGames){
            String game = new String();
            game = "User: " + g.user1.login + "with user: " + g.user2.login;
            games.getItems().add(game);
        }
        upperMenu.setPadding(new Insets(10, 10, 10, 10));
        Text title1 = new Text("Start a new game");
        Text title2 = new Text("Continue game");
        title1.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        title2.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        upperMenu.add(title1,0,0);
        upperMenu.add(new Text("Who do you want to play with?"),0,1);
        upperMenu.add(users,0,2);
        lowerMenu.setPadding(new Insets(10, 10, 10, 10));
        lowerMenu.add(title2, 2,0);
        lowerMenu.add(new Text("Which game do you want to play?"),2,1);
        lowerMenu.add(games,2,2);
        Button currentGame = startCurrentGame(stage, games);
        Button newGame = startNewGame(stage, users);
        lowerMenu.add(currentGame,1,2);
        upperMenu.add(newGame,3,2);
        GridPane.setMargin(users, new Insets(10,10,10,10));
        GridPane.setMargin(games,new Insets(10,10,10,10));
        users.setMaxHeight(100);
        games.setMaxHeight(100);
        GridPane.setMargin(lowerMenu,new Insets(10,100,10,10));
        GridPane.setMargin(upperMenu, new Insets(10,100,10,10));
        wholeMenu.add(upperMenu,0,0);
        wholeMenu.add(lowerMenu,1,0);
        manePage.setTop(wholeMenu);
        manePage.setPadding(new Insets(10, 10, 10, 10));
        return  manePage;
    }
    private Button startCurrentGame(Stage stage,ListView listView){
        Button button = new Button("Play current game");
        button.wrapTextProperty().setValue(true);
        button.setAlignment(Pos.CENTER);
        button.setOnAction(value -> {
            ObservableList selected = listView.getSelectionModel().getSelectedIndices();
            selectedGame = currentGames.get((int)selected.get(0));
            try {
                play(stage, "curr");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return button;
    }
    private Button startNewGame(Stage stage, ListView listView){
        Button button = new Button("Start a new game");
        button.wrapTextProperty().setValue(true);
        button.setAlignment(Pos.CENTER);
        button.setOnAction(value -> {});
        return button;
    }
    private GridPane grid(){
        GridPane grid = new GridPane();
        Rectangle rectangle;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                rectangle = new Rectangle();
                rectangle.setHeight(100);
                rectangle.setWidth(100);
                rectangle.setFill(Color.TRANSPARENT);
                GridPane.setMargin(rectangle,new Insets(10));
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(1);
                grid.add(rectangle,i,j);
            }
        }
        return grid;
    }
    private void play(Stage stage, String newOne) throws IOException, ClassNotFoundException {
        BorderPane up = loggedStage(stage);
        GridPane grid;
        if(newOne == "curr"){
            out.writeObject("/getMoves");
            out.writeObject(selectedGame.id);
            out.writeObject(loggedUser.id);
            String circle = (String) in.readObject();
            if(circle == null){
                circles = new ArrayList<>();
            }else {
                circles = translateFigures(circle);
            }
            if(selectedGame.user1.id == loggedUser.id){
                out.writeObject(selectedGame.user2.id);
            }else {
                out.writeObject(selectedGame.user1.id);
            }
            String cross = (String) in.readObject();
            if(cross == null) {
                crosses = new ArrayList<>();
            }else {
                crosses = translateFigures(cross);
            }
            grid = paint(stage);
        }else {
            circles = new ArrayList<>();
            crosses = new ArrayList<>();
            grid = grid();
        }
        BorderPane page = new BorderPane();
        page.setTop(up);
        BorderPane.setAlignment(grid,Pos.CENTER);
        page.setCenter(grid);
        Scene scene = new Scene(page,1000,600);
        stage.setScene(scene);
        stage.show();
    }
    private GridPane paint(Stage stage){
        GridPane gridPane = grid();
        for(int cross: crosses){
            int x = (cross - 1) % 3;
            int y = (cross - 1) / 3;
            Text text = new Text("x");
            text.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
            GridPane.setHalignment(text,HPos.CENTER);
            gridPane.add(text,x,y);
        }
        for(int circle: circles){
            int x = (circle - 1) % 3;
            int y = (circle - 1) / 3;

            Text text = new Text("o");
            text.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
            GridPane.setHalignment(text,HPos.CENTER);
            gridPane.add(text,x,y);
        }
        return gridPane;
    }
    private ArrayList<Integer> translateFigures(String figures){
        ArrayList<Integer> arrayList = new ArrayList<>();
        char[] chars = figures.toCharArray();
        for(char ch: chars){
            if(ch != ','){
                arrayList.add(Character.getNumericValue(ch));
            }
        }
        return arrayList;
    }
}