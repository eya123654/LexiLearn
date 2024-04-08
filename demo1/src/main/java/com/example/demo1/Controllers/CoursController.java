package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class CoursController {
    @FXML
    private TextField nomCoursField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField avancementField;

    @FXML
    private TextField imageField;

    @FXML
    private TextField priceField;
    @FXML private TableView<Cours> courseTable;
    @FXML private TableColumn<Cours, Integer> idColumn; // Assuming the ID is of type Integer
    @FXML private TableColumn<Cours, String> nameColumn;
    @FXML private TableColumn<Cours, String> descriptionColumn;
    @FXML private TableColumn<Cours, Integer> progressColumn; // Assuming the progress is of type Integer
    @FXML private TableColumn<Cours, String> imageColumn;
    @FXML private TableColumn<Cours, String> priceColumn; // Assuming price is a String, adjust if it's another type
    @FXML
    private StackPane contentArea;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    private CoursService coursService;
    private ObservableList<Cours> courseList = FXCollections.observableArrayList();

    public  CoursController() throws SQLException{
        coursService =new CoursService();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Ensure property name matches
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomCours")); // Ensure property name matches
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description")); // Ensure property name matches
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("avancement")); // Ensure property name matches
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image")); // Ensure property name matches
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price")); // Ensure property name matches

        courseTable.setItems(courseList); // Bind the table's items to the observable list once
        refreshCourseTable(); // Load initial data
    }
    @FXML
    private void addCours(ActionEvent event) {
        try {
            // Assuming avancement is an integer.
            int avancement = Integer.parseInt(avancementField.getText().trim());
            String nomCours = nomCoursField.getText().trim();
            String description = descriptionField.getText().trim();
            String image = imageField.getText().trim();
            String price = priceField.getText().trim();

            // Validation can be added here for each field

            // Create a new Cours object with the collected information
            Cours cours = new Cours( nomCours, description, avancement, image, price); // Assuming ID is auto-generated
            coursService.ajouter(cours);
            courseList.add(cours);
            refreshCourseTable();
            System.out.println(cours);

         showAlert(Alert.AlertType.INFORMATION, "Course Added", "The course was successfully added.");
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Input Error", "Error parsing avancement to integer.");
    } catch (Exception e) {
        showAlert(Alert.AlertType.ERROR, "Database Error", "Error creating course: " + e.getMessage());
    }
    }
    @FXML
    private void refreshCourseTable() {
        courseList.clear();
        courseList.addAll(coursService.readAll());    }
    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void updateCours(ActionEvent event) throws IOException {
        Cours selectedCours = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {

            try {

                int avancement = Integer.parseInt(avancementField.getText().trim());
                String nomCours = nomCoursField.getText().trim();
                String description = descriptionField.getText().trim();
                String image = imageField.getText().trim();
                String price = priceField.getText().trim();

                Cours updatedCours = new Cours(selectedCours.getId(), nomCours, description, avancement, image, price);

                coursService.update(updatedCours);
                refreshCourseTable();

                showAlert(Alert.AlertType.INFORMATION, "Course Updated", "The course was successfully updated.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Error parsing avancement to integer.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating course: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "No Course Selected. Please select a course in the table.");
        }
    }
    @FXML
    private void deleteCours(ActionEvent event) {
        Cours selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this course?", ButtonType.YES, ButtonType.NO);
            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        coursService.supprimer(selectedCourse.getId());
                        courseList.remove(selectedCourse);
                        refreshCourseTable();

                        showAlert(Alert.AlertType.INFORMATION, "Course Deleted", "The course was successfully deleted.");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the course: " + e.getMessage());
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a course from the table.");
        }
    }



}
