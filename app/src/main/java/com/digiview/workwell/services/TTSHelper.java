package com.digiview.workwell.services;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TTSHelper {
    private TextToSpeech tts;
    private boolean isInitialized = false;

    // Constructor
    public TTSHelper(Context context) {
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Set the language once initialized
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Handle language not supported case here
                } else {
                    isInitialized = true;
                }
            }
        });
    }

    // Method to speak text
    public void speak(String text) {
        if (isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Method to stop speaking
    public void stop() {
        if (tts.isSpeaking()) {
            tts.stop();
        }
    }

    // Method to shut down TTS
    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    // Optional: Check if TTS is initialized
    public boolean isInitialized() {
        return isInitialized;
    }
}

