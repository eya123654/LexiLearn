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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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
    private TableColumn<Cours, byte[]> imageColumn;
    @FXML
    private TableColumn<Cours, String> priceColumn;
    @FXML
    private StackPane contentArea;
    @FXML
    private ImageView imagePreview;
    @FXML
    private Button imageButton;
    @FXML
    private Button deleteButton;
    private CoursService coursService;
    private ObservableList<Cours> courseList = FXCollections.observableArrayList();
    private byte[] imageData;
    public CoursController() throws SQLException {
        coursService = new CoursService();
    }
    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePreview.setImage(new Image(file.toURI().toString()));
            imageData = readFileToByteArray(file);
        }
    }

    private byte[] readFileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            return buffer;
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "File Reading Error", "Failed to read the image file.");
            return null;
        }
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomCours"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("avancement"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        imageColumn.setCellFactory(param -> new TableCell<Cours, byte[]>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(50); // set the image view size
                imageView.setFitWidth(50);}

            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        ByteArrayInputStream bis = new ByteArrayInputStream(item);
                        Image image = new Image(bis);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle any exceptions
                    }
                }
            }});


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

        priceField.setText(course.getPrice());
        byte[] imageData = course.getImage();
        if (imageData != null && imageData.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                Image image = new Image(bis);
                imagePreview.setImage(image);  // Assuming `imagePreview` is your ImageView
                bis.close();
            } catch (IOException e) {
                System.err.println("Error loading image: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Optionally set a default or placeholder image if no image is available
            imagePreview.setImage(new Image("@../../Images/public.jpg"));
        }
    }



    @FXML
    private void addCours(ActionEvent event) {
        if (validateInput()) {
            try {

                int avancement = Integer.parseInt(avancementField.getText().trim());
                String nomCours = nomCoursField.getText().trim();
                String description = descriptionField.getText().trim();
             //   String image = imageField.getText().trim();
                String price = priceField.getText().trim();

                boolean isUnique = coursService.checkCourseNameUnique(nomCours);
                if (!isUnique) {
                    showAlert(Alert.AlertType.WARNING, "Duplicate Entry", "Ce cours existe déjà.");
                    return;
                }


                Cours cours = new Cours(nomCours, description, avancement, imageData, price);
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
                System.out.println(e.getMessage());
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
//String image = imageField.getText().trim();
                String price = priceField.getText().trim();

                Cours updatedCours = new Cours(selectedCours.getId(), nomCours, description, avancement, imageData, price);

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

        priceField.clear();
    }
}
