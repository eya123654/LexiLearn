package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Services.CoursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

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

}
