package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;
import com.example.demo1.Entities.Lecon;
import com.example.demo1.Services.CoursService;
import com.example.demo1.Services.LeconService;
import com.example.demo1.Utils.DataSource;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.sun.speech.freetts.audio.AudioPlayer;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;
import javax.sound.sampled.AudioFileFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.File;
import java.util.Locale;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import static com.mysql.cj.util.StringUtils.getBytes;
import static java.awt.SystemColor.text;

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
    private ImageView imageView;
    @FXML
    private Label title;
    @FXML
    private Label desc;
    @FXML
    private Label number;
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


    public ListView<Lecon> getListViewLessons() {
        return listViewLessons;
    }

    @FXML
    private void initialize() {
        listViewLessons.setCellFactory(lv -> new ListCell<Lecon>() {
            private final HBox hbox = new HBox(10); // spacing between elements
            private final Label label = new Label();
             Button audioButton = new Button("🔊");
            Button downloadaudio = new Button("Download", new FontAwesomeIconView(FontAwesomeIcon.DOWNLOAD));
            CheckBox checkBox = new CheckBox();
            private final VBox vbox = new VBox();
            {
                vbox.getChildren().addAll(label,checkBox, audioButton,downloadaudio);
                vbox.setVgrow(label, Priority.ALWAYS); // Make label grow horizontally


            }
            @Override
            protected void updateItem(Lecon lecon, boolean empty) {
                super.updateItem(lecon, empty);
                if (empty || lecon == null) {

                    setText(null);
                    setGraphic(null);
                } else {
                    Label titleLabel = new Label(lecon.getTitre(), checkBox);
                    Label descriptionLabel = new Label(lecon.getDescription());
                    Label contentLabel = new Label(lecon.getContenu());
                    Label completedLabel = new Label(lecon.isCompleted() ? "Completed" : "Not Completed");
                    audioButton.setOnAction(event -> handleGenerateAudio(lecon)); // Set action for audio button
                    downloadaudio.setOnAction(event -> generateAndSaveAudio(lecon));
                  //  setGraphic(hbox);
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

                    vbox.getChildren().addAll(titleLabel, descriptionLabel, contentLabel,audioButton,downloadaudio, completedLabel, downloadButton, passQuizButton);
                    setGraphic(vbox);
                    //setGraphic(hbox);

                }


            }
        });
    }

    @FXML
    private void handleGenerateAudio(Lecon lecon) {
        if (lecon == null) {
            System.err.println("No lesson provided for text-to-speech.");
            return; // Early exit if the lesson data is null
        }

        String text = lecon.getContenu(); // Retrieve the content of the lesson
        if (text == null || text.isEmpty()) {
            System.err.println("No content to speak.");
            return; // Early exit if there is no text to synthesize
        }

        try {
            // Setting the system properties to use Kevin's dictionary of FreeTTS
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            // Registering the speech engine
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");

            // Create a Synthesizer that generates voice
            Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));

            // Allocates a synthesizer
            synthesizer.allocate();

            // Resume a Synthesizer
            synthesizer.resume();

            // Speak the specified text until the QUEUE become empty
            synthesizer.speakPlainText(text, null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);

            // Deallocating the Synthesizer
            synthesizer.deallocate();
        } catch (Exception e) {
            System.err.println("Error during speech synthesis: " + e.getMessage());
            e.printStackTrace();
        }
        generateAndSaveAudio(lecon);

   }
   @FXML
    private void generateAndSaveAudio(Lecon lecon) {
        if (lecon == null || lecon.getContenu().isEmpty()) {
            System.err.println("No content to generate audio.");
            return;
        }

        try {
            // Set the properties to use the desired voice
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

            VoiceManager voiceManager = VoiceManager.getInstance();
            Voice voice = voiceManager.getVoice("kevin16"); // Ensure this voice is available in your setup

            if (voice == null) {
                System.err.println("Voice not available.");
                return;
            }

            // Set up the audio player to write to a file
            String basePath = "C:\\xampp\\htdocs\\pidev\\demo1\\src\\main\\java\\com\\example\\demo1\\audio\\";
            String sanitizedTitle = sanitizeFilename(lecon.getTitre());

            String filename = basePath + "Lecon_" +sanitizedTitle; // Custom filename within the audio folder
            AudioPlayer audioPlayer = new SingleFileAudioPlayer(filename, AudioFileFormat.Type.WAVE);
            voice.setAudioPlayer(audioPlayer);
            voice.allocate();
            voice.speak(lecon.getContenu());
            voice.deallocate();

            // Close the audio player to finalize and save the audio file
            audioPlayer.close();
            System.out.println("Audio file saved as: " + filename + ".wav");
        } catch (Exception e) {
            System.err.println("Error in generating audio file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private String sanitizeFilename(String name) {
        // Remove any characters that are not letters, digits, or common punctuation.
        name = name.replaceAll("[^a-zA-Z0-9_\\-\\s]", "");

        // Replace spaces with underscores to avoid issues in URLs or command lines.
        name = name.replace(" ", "_");

        // Truncate the filename to 100 characters if it's excessively long.
        if (name.length() > 100) {
            name = name.substring(0, 100);
        }

        return name;
    }

    private void updateProgressBar() {
        List<Lecon> lecons = listViewLessons.getItems();
        long completedCount = lecons.stream().filter(Lecon::isCompleted).count();
        progressBar.setProgress((double) completedCount / lecons.size());
    }

    private void downloadAsPDF(Lecon lecon) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));

        // Set the initial directory (optional, could be configured or static)
        fileChooser.setInitialDirectory(new File("C:\\Users\\asus\\Desktop\\lessons"));

        // Suggest a file name based on the lesson title
        String safeTitle = lecon.getTitre().replaceAll("[^a-zA-Z0-9_\\-]", "_");  // Sanitize to ensure it's a valid filename
        fileChooser.setInitialFileName(safeTitle + ".pdf");  // Set default filename in the save dialog

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
                    contentStream.newLineAtOffset(160, 720);
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
        title.setText("Course:"+course.getNomCours());
        desc.setText("Description:"+course.getDescription());
        number.setText("Total Lessons"+course.getLessons().size());
        // Assuming getImage() returns a byte[] that is your image data
        byte[] imgBytes = course.getImage();
        if (imgBytes != null) {
            Image img = new Image(new ByteArrayInputStream(imgBytes));
            imageView.setImage(img); // Assuming imageView is your ImageView control
        }

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