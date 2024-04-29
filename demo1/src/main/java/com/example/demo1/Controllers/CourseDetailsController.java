package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import com.example.demo1.Services.LeconService;
import com.example.demo1.Utils.DataSource;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.File;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class CourseDetailsController {
    private Connection connection;

    public CourseDetailsController() {
        connection = DataSource.getInstance().getCon();
    }

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
    private LeconService leconService = new LeconService();
    private Lecon lecon;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button audioButton;

    public ListView<Lecon> getListViewLessons() {
        return listViewLessons;
    }

    @FXML
    private void initialize() {
        listAllVoices();
        listViewLessons.setCellFactory(lv -> new ListCell<Lecon>() {
            @Override
            protected void updateItem(Lecon lecon, boolean empty) {
                super.updateItem(lecon, empty);
                if (empty || lecon == null) {

                    setText(null);
                    setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setSelected(lecon.isCompleted());
                    checkBox.setOnAction(event -> {
                        lecon.setCompleted(checkBox.isSelected());
                        try {
                            leconService.updateCompleted(lecon);
                            updateCourseProgress();// Implémentez cette méthode pour mettre à jour votre base de données
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        updateProgressBar();
                    });
                    VBox vbox = new VBox(5);
                    Label titleLabel = new Label(lecon.getTitre(), checkBox);
                    Label descriptionLabel = new Label(lecon.getDescription());
                    Label contentLabel = new Label(lecon.getContenu());
                    Label completedLabel = new Label(lecon.isCompleted() ? "Completed" : "Not Completed");

                    Button downloadButton = new Button("Download as PDF");
                    downloadButton.setOnAction(event -> downloadAsPDF(lecon));


                    Button passQuizButton = new Button("Pass The Quiz");
                    passQuizButton.setOnAction(event -> {
                    });


                    titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                    descriptionLabel.setStyle("-fx-font-size: 12px;");
                    contentLabel.setStyle("-fx-font-size: 12px; -fx-padding: 10px;");
                    downloadButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                    passQuizButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

                    vbox.getChildren().addAll(titleLabel, descriptionLabel, contentLabel, completedLabel, downloadButton, passQuizButton);
                    setGraphic(vbox);


                }


            }
        });
    }

    @FXML
    private void handleGenerateAudio() {
        if (lblCourseDescription != null) {
            String text = lblCourseDescription.getText(); // Get the text from your label or text source
            synthesizeAndPlayText(text);
        }
    }

    private void synthesizeAndPlayText(String text) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                VoiceManager voiceManager = VoiceManager.getInstance();
                Voice voice = voiceManager.getVoice("kevin16");
                if (voice != null) {
                    voice.allocate();
                    try {
                        voice.speak(text); // Directly speak the text
                    } finally {
                        voice.deallocate(); // Make sure to deallocate the voice
                    }
                } else {
                    System.err.println("Voice 'kevin16' not available");
                }
                return null;
            }
        };
        new Thread(task).start(); // Run the speech synthesis in a background thread
    }
    public void listAllVoices() {
        System.out.println("Listing all available voices:");
        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice[] voices = voiceManager.getVoices();
        if (voices.length == 0) {
            System.out.println("No voices found. Check your FreeTTS configuration.");
        } else {
        for (Voice voice : voices) {
            System.out.println("Voice name: " + voice.getName());
        }
    }}

    private void updateProgressBar() {
        List<Lecon> lecons = listViewLessons.getItems();
        long completedCount = lecons.stream().filter(Lecon::isCompleted).count();
        progressBar.setProgress((double) completedCount / lecons.size());
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
                    // Draw the image/logo
                    PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/resources/com/example/demo1/Images/logo.jpg", document);
                    contentStream.drawImage(pdImage, 100, 700, 50, 50);

                    // Draw the title
                    contentStream.beginText();
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                    contentStream.newLineAtOffset(160, 720); // Adjust x to align with your image
                    contentStream.showText(lecon.getTitre());
                    contentStream.endText();

                    // Draw the description with text wrapping
                    addWrappedText(contentStream, lecon.getDescription(), PDType1Font.HELVETICA, 12, 100, 650, page.getMediaBox().getWidth() - 200);

                    // Draw the content with text wrapping
                    addWrappedText(contentStream, lecon.getContenu(), PDType1Font.HELVETICA, 12, 100, 600, page.getMediaBox().getWidth() - 200);
                }

                document.save(file);
                System.out.println("PDF saved to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addWrappedText(PDPageContentStream contentStream, String text, PDFont font, float fontSize, float xStart, float yStart, float width) throws IOException {
        String[] lines = text.split("\n");
        contentStream.setFont(font, fontSize);
        contentStream.setLeading(fontSize * 1.2f);

        float nextY = yStart;
        for (String line : lines) {
            List<String> words = Arrays.asList(line.split(" "));
            int lastBlanks = 0;
            while (!words.isEmpty()) {
                StringBuilder lineBuilder = new StringBuilder();
                float contentWidth = 0;
                while (!words.isEmpty() && contentWidth < width) {
                    String word = words.get(0);
                    float wordWidth = font.getStringWidth(word + " ") / 1000 * fontSize;
                    if (contentWidth + wordWidth < width) {
                        lineBuilder.append(word).append(" ");
                        contentWidth += wordWidth;
                        words = words.subList(1, words.size());
                        lastBlanks++;
                    } else {
                        break;
                    }
                }
                contentStream.beginText();
                contentStream.newLineAtOffset(xStart, nextY);
                contentStream.showText(lineBuilder.toString());
                contentStream.endText();
                nextY -= fontSize * 1.2f + lastBlanks * 0.2f;
            }
        }
    }

    public void populateCourseDetails(Cours course) {
        currentCourse = course;
        lblCourseTitle.setText(course.getNomCours());
        lblCourseDescription.setText(course.getDescription());

        System.out.println("Lessons loaded: " + coursService.fetchLessonsForCourse(course).size());


        listViewLessons.getItems().setAll(coursService.fetchLessonsForCourse(course));
    }

    private void updateCourseProgress() throws SQLException {
        List<Lecon> lecons = listViewLessons.getItems();
        long completedCount = lecons.stream().filter(Lecon::isCompleted).count();
        double progress = (double) completedCount / lecons.size();

        progressBar.setProgress(progress);

        // Mise à jour de l'avancement dans la base de données pour le cours
        String query = "UPDATE cours SET avancement = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDouble(1, progress * 100); // Conversion en pourcentage
            preparedStatement.setInt(2, currentCourse.getId());
            preparedStatement.executeUpdate();
        }
    }

    @FXML
    private void onBackButtonClicked() {

    }
}