package main.java.application;

import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.decomposition.graph.Graph;
import main.java.decomposition.hyperGraph.Vertex;
import main.java.decomposition.spqrTree.TCTree;

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

        Graph g = new Graph();
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addEdge(v1, v4);
        g.addEdge(v1, v2);
        g.addEdge(v1, v3);
        g.addEdge(v2, v3);
        g.addEdge(v2, v4);
        g.addEdge(v3, v4);

        System.out.println(g.getVertices().size());

        TCTree spqrTree = new TCTree(g);

        System.out.println(spqrTree.getTCTreeNodes().size());
        System.out.println(g.toDOT());
        System.out.println(spqrTree.getGraph().toDOT());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
