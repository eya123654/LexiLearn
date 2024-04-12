package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class CourseDetailsController {

    @FXML
    private Label lblCourseTitle;
    @FXML
    private Label lblCourseDescription;
    @FXML
    private ListView<Lecon> listViewLessons;
    @FXML
    private Button backButton;
    @FXML
    private VBox courseDetailsBox;
    private Cours currentCourse;

    public ListView<Lecon> getListViewLessons() {
        return listViewLessons;
    }
    @FXML
    private void initialize() {

    }
    // Method to populate course details
    public void populateCourseDetails(Cours course) {
        currentCourse = course;
        lblCourseTitle.setText(course.getNomCours());
        lblCourseDescription.setText(course.getDescription());

        // Here is where you set the custom cell factory for the ListView
        listViewLessons.setCellFactory(listView -> new ListCell<Lecon>() {
            @Override
            protected void updateItem(Lecon lecon, boolean empty) {
                super.updateItem(lecon, empty);
                if (empty || lecon == null) {
                    setText(null);
                } else {
                    setText(lecon.getTitre() + " - " + lecon.getDescription());
                    // You can further customize the cell here, e.g., by adding graphics
                }
            }
        });

        // Assuming you have a method getLessons() that returns a list of lessons
        listViewLessons.getItems().setAll(course.getLessons());
    }

    @FXML
    private void onBackButtonClicked() {
        // Logic to go back to the course list view
        // This might involve telling the main UI controller to swap views
    }
}
