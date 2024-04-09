package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import com.example.demo1.Services.LeconService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private CoursService coursService;
    private LeconService leconService;
    public  LeconController() throws SQLException {
        leconService =new LeconService();
        coursService = new CoursService();
    }
    @FXML
    private void initialize() {
        loadCourses();
    }
    private void loadCourses() {
        ObservableList<Cours> coursList = coursService.readAll(); // Make sure CoursService has a readAll method
        courseComboBox.setItems(coursList);
        courseComboBox.setConverter(new StringConverter<Cours>() {
            @Override
            public String toString(Cours cours) {
                return cours.getNomCours(); // Assuming Cours has a getNomCours() method
            }

            @Override
            public Cours fromString(String string) {
                return null; // No need to convert from string
            }
        });
    }
    @FXML
    public void addLecon(ActionEvent event) {
        Cours selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            // Alert user that a course must be selected
            return;
        }

        String title = titleTextField.getText();
        String description = descriptionTextArea.getText();
        String content = contentTextField.getText();
        boolean isCompleted = completedCheckBox.isSelected();

        Lecon newLecon = new Lecon(selectedCourse,title,description,content,isCompleted);


        try {
            leconService.ajouter(newLecon);
            clearForm();
            // Optionally, show confirmation message
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally, show error message
        }}
    private void clearForm() {
        courseComboBox.getSelectionModel().clearSelection();
        titleTextField.clear();
        descriptionTextArea.clear();
        contentTextField.clear();
        completedCheckBox.setSelected(false);
    }
    // Read
    public List<Lecon> getCustomers() {
        return leconService.readAll();
    }
}
