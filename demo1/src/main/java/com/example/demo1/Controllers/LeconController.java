package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import com.example.demo1.Services.LeconService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class LeconController {
    @FXML
    private ComboBox<Cours> courseComboBox;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextArea contentTextField;
    @FXML
    private CheckBox completedCheckBox;
    @FXML
    private Button deleteButton;
    private final CoursService coursService;
    private final LeconService leconService;
    @FXML
    private TableView<Lecon> lessonsTable;
    @FXML
    private TableColumn<Lecon, String> titleColumn;
    @FXML
    private TableColumn<Lecon, String> descriptionColumn;
    @FXML
    private TableColumn<Lecon, String> contentColumn;
    @FXML
    private TableColumn<Lecon, Boolean> completedColumn;
    @FXML
    private TableColumn<Lecon, String> courseName;
    private ObservableList<Lecon> LessonList = FXCollections.observableArrayList();

    public LeconController() throws SQLException {
        leconService = new LeconService();
        coursService = new CoursService();
    }

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        completedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        courseName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCourse().getNomCours()));
        loadLessons();

        loadCourses();

        lessonsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Lecon selectedLesson = lessonsTable.getSelectionModel().getSelectedItem();
                populateTextFields(selectedLesson);
            }
        });
        refreshCourseTable();
    }

    private void loadLessons() {
        ObservableList<Lecon> lessonsList = leconService.readAll();
        // Adjust as necessary to return ObservableList
        lessonsTable.setItems(lessonsList);

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        completedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        courseName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCourse().getNomCours()));
    }

    private void loadCourses() {
        ObservableList<Cours> coursList = coursService.readAll();

        courseComboBox.setItems(coursList);
        courseComboBox.setConverter(new StringConverter<Cours>() {
            @Override
            public String toString(Cours cours) {
                return cours.getNomCours();
            }

            @Override
            public Cours fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    public void addLecon(ActionEvent event) {
        if (validateInput()) {
            Cours selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
            if (selectedCourse == null) {
                 showAlert(Alert.AlertType.WARNING, "no selected  course", "please selesct a course");;
            }

            String title = titleTextField.getText();
            String description = descriptionTextArea.getText();
            String content = contentTextField.getText();
            boolean isCompleted = completedCheckBox.isSelected();
            boolean isUnique = leconService.checkCourseNameUnique(titleTextField.getText());
            if (!isUnique) {
                showAlert(Alert.AlertType.WARNING, "Duplicate Entry", "Cette leçon existe déjà.");
                return;
            }
            Lecon newLecon = new Lecon(selectedCourse, title, description, content, isCompleted);


            try {
                leconService.ajouter(newLecon);

                LessonList.add(newLecon);
                clearForm();
                refreshCourseTable();
                showAlert(Alert.AlertType.INFORMATION, "lesson Added", "The lesson was successfully added.");

                // Optionally, show confirmation message
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Error parsing avancement to integer.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error creating course: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "This Field is Required", "please fill the field");
        }
    }

    private void clearForm() {
        courseComboBox.getSelectionModel().clearSelection();
        titleTextField.clear();
        descriptionTextArea.clear();
        contentTextField.clear();
        completedCheckBox.setSelected(false);
    }

    @FXML
    public void handleSaveChanges() {
        if (validateInput()) {
            try {

                Lecon leconToUpdate = lessonsTable.getSelectionModel().getSelectedItem();

                if (leconToUpdate != null) {
                    leconToUpdate.setTitre(titleTextField.getText());
                    leconToUpdate.setDescription(descriptionTextArea.getText());
                    leconToUpdate.setContenu(contentTextField.getText());
                    leconToUpdate.setCompleted(completedCheckBox.isSelected());
                    boolean isUnique = leconService.checkCourseNameUnique(titleTextField.getText());
                    if (!isUnique) {
                        showAlert(Alert.AlertType.WARNING, "Duplicate Entry", "Ce nom existe déjà.");
                        return;
                    }
                    Cours selectedCours = courseComboBox.getSelectionModel().getSelectedItem();
                    leconToUpdate.setCourse(selectedCours);

                    leconService.update(leconToUpdate);
                    refreshCourseTable();
                    showAlert(Alert.AlertType.INFORMATION, "lesson Updated", "The lesson was successfully updated.");

                } else {
                    showAlert(Alert.AlertType.WARNING, "No Selection", "No lesson Selected. Please select a lesson in the table.");
                }


            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Error parsing avancement to integer.");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating lesson: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "This Field is Required", "please fill the field");
        }
    }

    @FXML
    private void deleteLesson(ActionEvent event) {
        Lecon selectedLesson = lessonsTable.getSelectionModel().getSelectedItem();
        if (selectedLesson != null) {
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this lesson?", ButtonType.YES, ButtonType.NO);
            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        leconService.supprimer(selectedLesson.getId());
                        LessonList.remove(selectedLesson);
                        refreshCourseTable();

                        showAlert(Alert.AlertType.INFORMATION, "Course Deleted", "The course was successfully deleted.");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the lesson: " + e.getMessage());
                        System.out.println(e.getMessage());
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a course from the table.");
        }
    }

    @FXML
    private void refreshCourseTable() {
        LessonList.clear();
        LessonList.addAll(leconService.readAll());
        lessonsTable.setItems(LessonList);
    }

    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateInput() {
        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        String content = contentTextField.getText();
        Cours selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();

        boolean isValid = true;

        if (title.isEmpty()) {
            isValid = false;
            titleTextField.setStyle("-fx-border-color: red;");
        } else {
            titleTextField.setStyle("");
        }
        if (description.isEmpty()) {
            isValid = false;
            descriptionTextArea.setStyle("-fx-border-color: red;");
        } else {
            descriptionTextArea.setStyle("");
        }
        if (content.isEmpty()) {
            isValid = false;
            contentTextField.setStyle("-fx-border-color: red;");
        } else {
            contentTextField.setStyle("");
        }

        if (selectedCourse == null) {
            isValid = false;
            courseComboBox.setStyle("-fx-border-color: red;");
        } else {
            courseComboBox.setStyle("");
        }
        return isValid;
    }

    private void populateTextFields(Lecon lesson) {
        titleTextField.setText(lesson.getTitre());
        descriptionTextArea.setText(lesson.getDescription());
        contentTextField.setText(lesson.getContenu());
        completedCheckBox.setSelected(lesson.isCompleted());


        Cours course = lesson.getCourse();
        if (course != null) {
            courseComboBox.getSelectionModel().select(course);
        }
    }

}
