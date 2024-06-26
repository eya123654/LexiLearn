package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CourseCardController {
    @FXML
    private VBox courseCard;
    @FXML
    private Label courseName;
    @FXML
    private Label instructorName;
    @FXML
    private Label courseDescription;
    @FXML
    private ImageView courseImage;

    @FXML private Button viewCourseButton;

    private FrontCoursController frontCoursController;

    private Cours cours;


    public interface CourseDetailsCallback {
        void onCourseSelected(Cours cours);
    }

    private CourseDetailsCallback callback;

    public void setCourseDetailsCallback(CourseDetailsCallback callback) {
        this.callback = callback;
    }




    public void initialize() {
    }




    public void initData(Cours cours) {
        System.out.println("initData called with course: " + cours);

        this.cours = cours;
        courseName.setText(cours.getNomCours());
        courseDescription.setText(cours.getDescription());
        byte[] imagePath = cours.getImage();
        if (imagePath != null && imagePath.length >0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(imagePath);
                Image image = new Image(bis);
                courseImage.setImage(image);
                bis.close();  // Always good to close streams
            } catch (Exception e) {
                System.err.println("Error loading image from byte array.");
                e.printStackTrace();
            }
        } else {
            String defaultImagePath = "C:/xampp/htdocs/pidev/demo1/src/main/resources/com/example/demo1/Images/public.jpg";
                    Image image = new Image(defaultImagePath, true); // true = load in background

                courseImage.setImage(image);
            }
        }





    @FXML
    private void showCourseDetails(ActionEvent event) {
        if (callback == null) {
            System.out.println("Callback is null.");
        }
        if (cours == null) {
            System.out.println("Course is null.");
        }
        if (callback != null && cours != null) {
            callback.onCourseSelected(cours);
        } else {
            System.out.println("Either callback or course is null");
        }
    }



}

