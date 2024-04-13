package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

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
    private CoursService coursService = new CoursService();

    public ListView<Lecon> getListViewLessons() {
        return listViewLessons;
    }

    @FXML
    private void initialize() {
        listViewLessons.setCellFactory(lv -> new ListCell<Lecon>() {
            @Override
            protected void updateItem(Lecon lecon, boolean empty) {
                super.updateItem(lecon, empty);
                if (empty || lecon == null) {

                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(5); // Use a VBox to stack elements vertically
                    Label titleLabel = new Label(lecon.getTitre());
                    Label descriptionLabel = new Label(lecon.getDescription());
                    Label contentLabel = new Label(lecon.getContenu());
                    Button downloadButton = new Button("Download as PDF");
                    Button passQuizButton = new Button("Pass The Quiz");
                    titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                    descriptionLabel.setStyle("-fx-font-size: 12px;");
                    contentLabel.setStyle("-fx-font-size: 12px; -fx-padding: 10px;");
                    downloadButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                    passQuizButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

                    vbox.getChildren().addAll(titleLabel, descriptionLabel, contentLabel, downloadButton, passQuizButton);
                    setGraphic(vbox);
                }

            }


        });

    }

    public void populateCourseDetails(Cours course) {
        currentCourse = course;
        lblCourseTitle.setText(course.getNomCours());
        lblCourseDescription.setText(course.getDescription());
        System.out.println("Lessons loaded: " + coursService.fetchLessonsForCourse(course).size()); // Check the size of lessons


        // Assuming you have a method getLessons() that returns a list of lessons
        listViewLessons.getItems().setAll(coursService.fetchLessonsForCourse(course));
    }

    @FXML
    private void onBackButtonClicked() {
        // Logic to go back to the course list view
        // This might involve telling the main UI controller to swap views
    }
}
