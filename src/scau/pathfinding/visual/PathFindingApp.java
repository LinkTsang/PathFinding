package scau.pathfinding.visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Link
 */
public class PathFindingApp extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private boolean initUILayout(Stage primaryStage) {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (root == null) return false;
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setTitle("Path Finding Demo");
        return true;
    }

    @Override
    public void start(Stage primaryStage) {
        if (!initUILayout(primaryStage)) {
            return;
        }
        primaryStage.show();
    }

}
