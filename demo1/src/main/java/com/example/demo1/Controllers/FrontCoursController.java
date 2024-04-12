package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FrontCoursController {


    @FXML
    private FlowPane coursesContainer;
    @FXML
    private StackPane mainStackPane;
    public DashboardController dashboardController;

    @FXML
    public void setDashboardController(DashboardController dashboardController) {
        if (dashboardController == null) {
            System.out.println("Attempting to set a null dashboardController!");
        }
        this.dashboardController = dashboardController;
        System.out.println("DashboardController is set successfully.");
        loadCourses();
    }

   /* @FXML
    public void initialize() {
        setDashboardController(dashboardController);

        loadCourses();
    }*/

    @FXML
    private void loadCourses() {
        CoursService coursService = new CoursService();
        ObservableList<Cours> courses = coursService.readAll();
        for (Cours course : courses) {

            coursesContainer.getChildren().add(createCourseCard(course));
        }
    }

    private Node createCourseCard(Cours course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/views/CoursFront/CourseCard.fxml"));
            Node card = loader.load();
            CourseCardController cardController = loader.getController();
            cardController.setCourseDetailsCallback(this.dashboardController);
            System.out.println("Callback set for course card: " + course.getNomCours());


            cardController.initData(course);
            return card;
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return new VBox(new Label("Error loading course card."));
        }
    }
 /*   public Node CourseDetails(Cours cours) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/views/CoursFront/CourseDetails.fxml"));
            Node detailsView = loader.load();
            CourseDetailsController controller = loader.getController();

            controller.populateCourseDetails(cours);
            System.out.println("Is 'this' a CourseDetailsRequestListener? " + (this instanceof CourseDetailsRequestListener));



            return detailsView;
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return new VBox(new Label("Error loading course card."));
        }
    }*/

 /*   @FXML
        public void showCourseDetails(Cours selectedCourse) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/views/CoursFront/CourseDetails.fxml"));
                Parent detailsView = loader.load();
                CourseDetailsController detailsController = loader.getController();
                detailsController.populateCourseDetails(selectedCourse);
                System.out.println("Loaded details view for: " + selectedCourse.getNomCours());

                // This should be the mainStackPane that's part of the main application layout, not within a card.
                mainStackPane.getChildren().setAll(detailsView);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }*/


}
