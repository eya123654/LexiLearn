package com.example.demo1.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Home extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Correctly load the FXML file and obtain the root node
            Parent rootNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo1/Cours/AddCours.fxml")));
            Scene scene = new Scene(rootNode, 320, 240); // Use the loaded root node
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // It's good practice to handle potential errors in a more user-friendly way,
            // maybe logging the error or showing an alert dialog, depending on your application's needs.
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
