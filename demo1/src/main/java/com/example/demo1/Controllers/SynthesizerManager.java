package com.example.demo1.Controllers;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.synthesis.*;
import java.util.Locale;
public class SynthesizerManager {
    private static Synthesizer synthesizer = null;


    public static synchronized Synthesizer getSynthesizer() throws EngineException {
        if (synthesizer == null) {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
            if (synthesizer == null) {
                throw new IllegalStateException("Failed to create a synthesizer");
            }
            synthesizer.allocate();
            System.out.println("Synthesizer allocated successfully.");
        }
        return synthesizer;
    }


    public static void cleanup() {
            if (synthesizer != null) {
                try {
                    synthesizer.deallocate(); // Deallocate the synthesizer
                } catch (EngineException e) {
                    System.err.println("Error deallocating synthesizer: " + e.getMessage());
                } finally {
                    synthesizer = null; // Clear the reference
                }
            }
        }
    }





