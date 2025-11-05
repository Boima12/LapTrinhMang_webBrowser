package client;

import client.controller.ClientController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the JavaFX HTTP Client.
 * Responsible only for initializing the UI and starting the application.
 */
public class HttpFxClient extends Application {

    @Override
    public void start(Stage stage) {
        // Create the main controller (which sets up UI + logic)
        ClientController controller = new ClientController();

        // Create the scene and attach the root UI
        Scene scene = new Scene(controller.getRoot(), 1250, 700);

        // Configure and show the stage
        stage.setTitle("HTTP Client (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
