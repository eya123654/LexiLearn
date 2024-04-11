package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FrontCoursController {


    @FXML
    private FlowPane coursesContainer;

    public void initialize() {
        loadCourses();
    }


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
            CourseCardController controller = loader.getController();
            controller.initData(course);
            return card;
        } catch (IOException e) {
            // Handle exceptions
            e.printStackTrace();
            return new VBox(new Label("Error loading course card."));
        }
    }

}
