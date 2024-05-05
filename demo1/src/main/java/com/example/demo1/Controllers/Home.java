package com.example.demo1.Controllers;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class Home extends Application {

    @Override

    public void start(Stage primaryStage) {

        try {
            Parent rootNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo1/main.fxml")));

            primaryStage.setTitle("Educational Platform");
            primaryStage.setScene(new Scene(rootNode, 993, 616));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Failed to load the FXML file", "Could not load the user interface. Please check the console for more information.");
        }

    }



    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
