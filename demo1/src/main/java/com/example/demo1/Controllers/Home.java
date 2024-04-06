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
            Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Cours/AddLecon.fxml")));
            Scene scene = new Scene(fxmlLoader, 320, 240);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
