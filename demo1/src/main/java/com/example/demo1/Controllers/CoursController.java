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
    @FXML
    private TableView<Cours> courseTable;
    @FXML
    private TableColumn<Cours, Integer> idColumn;
    @FXML
    private TableColumn<Cours, String> nameColumn;
    @FXML
    private TableColumn<Cours, String> descriptionColumn;
    @FXML
    private TableColumn<Cours, Integer> progressColumn;
    @FXML
    private TableColumn<Cours, String> imageColumn;
    @FXML
    private TableColumn<Cours, String> priceColumn;
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

    public CoursController() throws SQLException {
        coursService = new CoursService();
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomCours"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("avancement"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        courseTable.setItems(courseList);
        refreshCourseTable();
        courseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Cours selectedCourse = courseTable.getSelectionModel().getSelectedItem();
                populateTextFields(selectedCourse);
            }
        });
    }

    private void populateTextFields(Cours course) {
        idColumn.setText(String.valueOf(course.getId()));
        nomCoursField.setText(course.getNomCours());
        descriptionField.setText(course.getDescription());
        avancementField.setText(String.valueOf(course.getAvancement()));
        imageField.setText(course.getImage());
        priceField.setText(course.getPrice());


    }

    @FXML
    private void addCours(ActionEvent event) {
        if (validateInput()) {
            try {

                int avancement = Integer.parseInt(avancementField.getText().trim());
                String nomCours = nomCoursField.getText().trim();
                String description = descriptionField.getText().trim();
                String image = imageField.getText().trim();
                String price = priceField.getText().trim();

                boolean isUnique = coursService.checkCourseNameUnique(nomCours);
                if (!isUnique) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Entry", "Ce cours existe déjà.");
                    return;
                }


                Cours cours = new Cours(nomCours, description, avancement, image, price);
                coursService.ajouter(cours);
                courseList.add(cours);
                clearForm();
                refreshCourseTable();
                System.out.println(cours);

                showAlert(Alert.AlertType.INFORMATION, "Course Added", "The course was successfully added.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Error parsing avancement to integer.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error creating course: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "This Field is Required", "please fill the field");
        }
    }

    @FXML
    private void refreshCourseTable() {
        courseList.clear();
        courseList.addAll(coursService.readAll());
    }

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

    private boolean validateInput() {
        String courseName = nomCoursField.getText();
        String description = descriptionField.getText();
        String progress = avancementField.getText();
        String imageUrl = imageField.getText();
        String price = priceField.getText();


        boolean isValid = true;

        if (courseName.isEmpty()) {
            isValid = false;
            nomCoursField.setStyle("-fx-border-color: red;");
        } else {
            nomCoursField.setStyle("");
        }
        if (description.isEmpty()) {
            isValid = false;
            descriptionField.setStyle("-fx-border-color: red;");
        } else {
            descriptionField.setStyle("");
        }
        if (imageUrl.isEmpty()) {
            isValid = false;
            imageField.setStyle("-fx-border-color: red;");
        } else {
            imageField.setStyle("");
        }
        if (price.isEmpty()) {
            isValid = false;
            priceField.setStyle("-fx-border-color: red;");
        } else {
            priceField.setStyle("");
        }

        try {
            int progressValue = Integer.parseInt(progress);
            if (progressValue < 0 || progressValue > 100) {
                isValid = false;
                avancementField.setStyle("-fx-border-color: red;");
            } else {
                avancementField.setStyle("");
            }
        } catch (NumberFormatException e) {
            isValid = false;
            avancementField.setStyle("-fx-border-color: red;");
        }


        return isValid;
    }

    private void clearForm() {
        descriptionField.clear();
        nomCoursField.clear();
        priceField.clear();
        imageField.clear();
        priceField.clear();
    }
}
