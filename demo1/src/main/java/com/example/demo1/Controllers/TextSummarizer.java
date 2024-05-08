package com.example.demo1.Controllers;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import java.io.InputStream;

public class TextSummarizer {
    private SentenceDetectorME sentenceDetector;

    public TextSummarizer() {
        try (InputStream modelIn = getClass().getResourceAsStream("/com/example/demo1/models/en-sent.bin")) {
            if (modelIn == null) {
                System.err.println("Model file not found. Make sure the path is correct.");
                return; // Exit constructor if model file is not found
            }
            SentenceModel model = new SentenceModel(modelIn);
            sentenceDetector = new SentenceDetectorME(model);
        } catch (Exception e) {
            System.err.println("Failed to load the sentence detection model.");
            e.printStackTrace();
        }
    }

    public String summarize(String content) {
        if (sentenceDetector == null) {
            return "Sentence detector was not initialized."; // Return error message if initialization failed
        }
        String[] sentences = sentenceDetector.sentDetect(content);
        if (sentences.length > 1) {
            return sentences[0] + " " + sentences[sentences.length - 1];
        }
        return content;
    }
}
