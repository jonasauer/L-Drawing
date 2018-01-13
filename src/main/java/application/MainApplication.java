package main.java.application;

import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication  extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/fxml/new.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1365, 768);
        scene.getStylesheets().add("/main/resources/css/GraphControl.css");

        stage.setOnShown((windowEvent) -> fxmlLoader.<GUIController>getController().onLoaded());
        stage.setMinWidth(200);
        stage.setMinHeight(200);
        stage.setTitle("L-Drawings");
        stage.getIcons().add(new Image("/main/resources/icons/applicationIcon.png"));
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
