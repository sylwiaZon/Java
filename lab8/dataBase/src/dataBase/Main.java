package dataBase;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    private static DataBase dataBase;
    private TextField searchAuthor;
    private TextField searchISBN;
    private TextField newBook[];
    private ArrayList<Book> books;
    private GridPane menuPane;
    @Override
    public void start(Stage stage) throws Exception {
        books = new ArrayList<>();
        boolean connected = false;
        try {
            dataBase = new DataBase("books");
            int i = 0;
            while(i <  3 && !connected) {
                dataBase.connect();
                connected = dataBase.connected();
                TimeUnit.SECONDS.sleep(3);
                i++;
            }
            if(connected){
                createStage(stage);
            } else {
                unconnectedStage(stage);
            }
        } catch (Exception e) { e.printStackTrace(); }

    }

    private void unconnectedStage(Stage stage){
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(new Text("You were unable to connect with the data base"));
        Scene scene = new Scene(borderPane,1000,600);
        stage.setTitle("Data Base");
        stage.setScene(scene);
        stage.show();
    }

    private void createStage(Stage stage){
        menuPane = createScene(stage);
        Scene scene = new Scene(menuPane,1000,600);
        stage.setTitle("Data Base");
        stage.setScene(scene);
        stage.show();
    }

    private void refreshPage(Stage stage, GridPane records){
        VBox vBox = new VBox();
        vBox.getChildren().add(menuPane);
        vBox.getChildren().add(records);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        Scene scene = new Scene(scrollPane,1000,600);
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createScene(Stage stage){
        GridPane menu = new GridPane();
        menu.add(selectAll(stage),0,0,2,2);
        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);
        menu.add(separator1,2,0, 1,3);
        menu.add(new Text("Search by author"), 3, 0);
        searchAuthor = new TextField();
        menu.add(searchAuthor, 4, 0);
        menu.add(selectByAuthor(stage), 5,0);
        menu.add(new Text("Search by ISBN"), 3, 1);
        searchISBN = new TextField();
        menu.add(searchISBN, 4,1);
        menu.add(selectByISBN(stage),5,1);
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        menu.add(separator2,6,0, 1,3);
        newBook = new TextField[4];
        for(int i = 0; i < 4; i++){
            newBook[i] = new TextField();
        }
        menu.add(new Text("ISBN"), 7, 0);
        menu.add(newBook[0], 8,0);
        menu.add(new Text("Title"), 7, 1);
        menu.add(newBook[1], 8,1);
        menu.add(new Text("Author"), 9, 0);
        menu.add(newBook[2], 10,0);
        menu.add(new Text("Year"), 9, 1);
        menu.add(newBook[3], 10,1);
        Button addBook = addBook(stage);
        GridPane.setHalignment(addBook, HPos.RIGHT);
        menu.add(addBook, 10,2);
        menu.setPadding(new Insets(10, 10, 10, 10));
        menu.setVgap(10);
        menu.setHgap(10);
        menu.setAlignment(Pos.CENTER);
        menu.setPrefWidth(1000);
        menu.setStyle("-fx-background-color: #D4CDCD");
        return menu;
    }

    private void showBooks(Stage stage){
        GridPane records = new GridPane();
        records.setPadding(new Insets(10, 10, 10, 10));
        records.setVgap(10);
        records.setHgap(10);
        records.setAlignment(Pos.CENTER);
        records.setPrefWidth(1000);
        int j = 0;
        for(int i = 0; i < books.size(); i ++){
            GridPane book = book(books.get(i),i);
            book.setStyle("-fx-background-color: #D4CDCD; -fx-padding: 10");
            records.add(book,0,i);
            j = j + 4;
        }
        refreshPage(stage, records);
    }

    private GridPane book(Book book, int i){
        GridPane record = new GridPane();
        record.setVgap(15);
        record.add(new Text("ISBN: "),i,0);
        record.add(new Text(book.ISBN),i + 1,0);
        record.add(new Text("Author: "), i,1);
        record.add(new Text(book.author),i + 1,1);
        record.add(new Text("Title: "),i,2);
        record.add(new Text(book.title),i + 1,2);
        record.add(new Text("Year: "),i, 3);
        record.add(new Text(Integer.toString(book.year)),i + 1,3);
        return record;
    }

    private Button selectAll(Stage stage) {
        Button button = new Button("Select all");
        button.setPrefSize(100,50);
        button.setOnAction(value -> {
            books = dataBase.listData();
            showBooks(stage);
        });

        return button;
    }

    private Button selectByAuthor(Stage stage) {
        Button button = new Button("Search");
        button.setOnAction(value -> {
            books = dataBase.searchViaAuthor(searchAuthor.getText());
            showBooks(stage);
        });

        return button;
    }

    private Button selectByISBN(Stage stage) {
        Button button = new Button("Search");
        button.setOnAction(value -> {
            books = dataBase.searchViaISBN(searchISBN.getText());
            showBooks(stage);
        });

        return button;
    }

    private Button addBook(Stage stage) {
        Button button = new Button("Add a book");
        button.setOnAction(value -> {
            try {
                dataBase.addBook(new Book(newBook[0].getText(),newBook[1].getText(),newBook[2].getText(),Integer.parseInt(newBook[3].getText())));
                books = dataBase.listData();
                showBooks(stage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
