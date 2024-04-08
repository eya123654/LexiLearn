package com.example.demo1.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button allCoursesCheckBox;

    @FXML
    private Button addCourseCheckBox;

    @FXML
    private Button deleteCourseCheckBox;

    @FXML
    private Button updateCourseCheckBox;

    @FXML private Button coursesButton;

    @FXML
    private Accordion coursesAccordion;

    @FXML
    private VBox coursesPane;

    @FXML
    private void initialize() {

    }
    @FXML
    private void toggleCoursesPane(ActionEvent event) {
        coursesPane.setVisible(!coursesPane.isVisible());
        coursesPane.setManaged(!coursesPane.isManaged());
    }
    @FXML
    public void loadView(ActionEvent event) {
        try {
            Node view = null;
            if (event.getSource() == allCoursesCheckBox) {
                view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo1/views/Cours/ShowCourses.fxml")));
            } else if (event.getSource() == addCourseCheckBox) {
                view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo1/views/Cours/AddCours.fxml")));
            } else if (event.getSource() == updateCourseCheckBox) {
                view =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/demo1/views/Cours/UpdateCourse.fxml")));

            }

            if (view != null) {
                contentArea.getChildren().setAll(view);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            showAlert("Load Error", "The FXML file was not found.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Load Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
