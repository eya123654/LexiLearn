package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class CourseCardController {
    @FXML
    private VBox courseCard;
    @FXML
    private Label courseName;
    @FXML
    private Label instructorName; // Assuming you have an instructor name in your course data.
    @FXML
    private Label courseDescription;
    @FXML
    private ImageView courseImage; // Make sure this fx:id matches the one in your FXML.

    // Call this method somewhere in your controller's initialization process

    private Cours cours;
    public void initialize() {
    }




    public void initData(Cours cours) {
        this.cours = cours;
        courseName.setText(cours.getNomCours());
        courseDescription.setText(cours.getDescription());
        String imagePath = cours.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image;
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    // Load image from URL
                    image = new Image(imagePath, true); // true = load in background
                } else {
                    // Load image from file system
                    imagePath = "@../../Images/public.jpg";
                    image = new Image(imagePath, true); // true = load in background
                }
                courseImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Error loading image: " + imagePath);
                // Optionally, set a default or placeholder image
            }
        } else {
            // Handle cases where no image path is provided
            // Optionally, set a default or placeholder image
        }
    }
    }

