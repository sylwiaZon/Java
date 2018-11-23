package Gallery;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Gallery extends Application {
    ArrayList<File> photos;
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(5);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(chooseDirectory(stage));
        root.getChildren().addAll(borderPane);
        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("JavaFX DirectoryChooser");
        stage.setScene(scene);
        stage.show();
    }

    private Button chooseDirectory(Stage stage){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);

        Button button = new Button("Select a directory");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                File directory = directoryChooser.showDialog(stage);
                if (directory != null) {
                    File[] fList = directory.listFiles();
                    photos = new ArrayList<File>();
                    String name;
                    for( File file: fList) {
                        name = file.getName();
                        if (name.matches(".*\\.JPG") || name.matches(".*\\.PNG") || name.matches(".*\\.BMP") || name.matches(".*\\.GIF")) {
                            photos.add(file);
                        }
                    }
                    try {
                        showPictures();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    stage.close();
                }
            }
        });

        return button;
    }

    private void showPictures() throws FileNotFoundException {
        ScrollPane scrollPane = new ScrollPane();
        Group root = new Group();
        double totalHeight = 0;
        for(int i = 1; i <= photos.size(); i++){
            Image image = new Image(new FileInputStream(photos.get(i-1).getAbsolutePath()));
            ImageView imageView = new ImageView(image);
            imageView.setY(totalHeight + 10 );
            imageView.setFitWidth(300);
            totalHeight += 300;
            imageView.setPreserveRatio(true);
            root.getChildren().add(imageView);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    ImageView bigImageView = new ImageView(image);
                    bigImageView.setPreserveRatio(true);
                    bigImageView.setFitWidth(720);
                    BorderPane borderPane = new BorderPane();
                    borderPane.setCenter(bigImageView);
                    Scene bigImageScene = new Scene(borderPane,720, 400);
                    Stage bigImageStage = new Stage();
                    bigImageStage.setScene(bigImageScene);
                    bigImageStage.show();
                }
            });
        }
        scrollPane.setContent(root);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Scene secondScene = new Scene(scrollPane, 300, 500);

        Stage newWindow = new Stage();
        newWindow.setTitle("Gallery");
        newWindow.setScene(secondScene);

        newWindow.setX(200);
        newWindow.setY(100);

        newWindow.show();
    }


    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Select directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}