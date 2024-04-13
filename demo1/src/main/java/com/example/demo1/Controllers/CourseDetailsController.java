package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CourseDetailsController {

    @FXML
    private Label lblCourseTitle;
    @FXML
    private Label lblCourseDescription;
    @FXML
    private ListView<Lecon> listViewLessons;
    @FXML
    private Button backButton;
    @FXML
    private VBox courseDetailsBox;
    private Cours currentCourse;
    private CoursService coursService = new CoursService();

    public ListView<Lecon> getListViewLessons() {
        return listViewLessons;
    }

    @FXML
    private void initialize() {
        listViewLessons.setCellFactory(lv -> new ListCell<Lecon>() {
            @Override
            protected void updateItem(Lecon lecon, boolean empty) {
                super.updateItem(lecon, empty);
                if (empty || lecon == null) {

                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(5); // Use a VBox to stack elements vertically
                    Label titleLabel = new Label(lecon.getTitre());
                    Label descriptionLabel = new Label(lecon.getDescription());
                    Label contentLabel = new Label(lecon.getContenu());
                    Button downloadButton = new Button("Download as PDF");
                    downloadButton.setOnAction(event -> downloadAsPDF(lecon));

                    Button passQuizButton = new Button("Pass The Quiz");
                    titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                    descriptionLabel.setStyle("-fx-font-size: 12px;");
                    contentLabel.setStyle("-fx-font-size: 12px; -fx-padding: 10px;");
                    downloadButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                    passQuizButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

                    vbox.getChildren().addAll(titleLabel, descriptionLabel, contentLabel, downloadButton, passQuizButton);
                    setGraphic(vbox);
                }

            }


        });

    }
    private void downloadAsPDF(Lecon lecon) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(listViewLessons.getScene().getWindow());

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.newLineAtOffset(100, 700);
                    contentStream.showText(lecon.getTitre()); // Title
                    contentStream.endText();

                    // Handle multiline description
                    String[] descriptionLines = lecon.getDescription().split("\n");
                    int currentHeight = 685;
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(100, currentHeight);
                    for (String line : descriptionLines) {
                        contentStream.showText(line);
                        contentStream.newLine();
                    }
                    contentStream.endText();

                    // Handle multiline content
                    String[] contentLines = lecon.getContenu().split("\n");
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, currentHeight - 15 * descriptionLines.length); // Adjust the height
                    for (String line : contentLines) {
                        contentStream.showText(line);
                        contentStream.newLine();
                    }
                    contentStream.endText();
                }

                document.save(file);
                System.out.println("PDF saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                // Here you can show an error message to the user if needed.
            }
        }
    }

    public void populateCourseDetails(Cours course) {
        currentCourse = course;
        lblCourseTitle.setText(course.getNomCours());
        lblCourseDescription.setText(course.getDescription());
        System.out.println("Lessons loaded: " + coursService.fetchLessonsForCourse(course).size()); // Check the size of lessons


        // Assuming you have a method getLessons() that returns a list of lessons
        listViewLessons.getItems().setAll(coursService.fetchLessonsForCourse(course));
    }

    @FXML
    private void onBackButtonClicked() {
        // Logic to go back to the course list view
        // This might involve telling the main UI controller to swap views
    }
}
