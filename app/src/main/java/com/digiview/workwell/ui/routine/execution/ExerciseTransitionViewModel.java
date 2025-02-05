package com.digiview.workwell.ui.routine.execution;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.os.CountDownTimer;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.services.TTSHelper;
import com.digiview.workwell.services.mediapipe.TTSInitializationListener;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class ExerciseTransitionViewModel extends ViewModel {
    private MediaPlayer mediaPlayer;
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private Context context;
    public void setContext(Context context) {
        this.context = context;
    }

    private CountDownTimer countDownTimer;


    private final MutableLiveData<RoutineConstants.TRANSITION_STATE> transitionState = new MutableLiveData<>();
    public LiveData<RoutineConstants.TRANSITION_STATE> getTransitionState() {
        return transitionState;
    }
    public void setTransitionState(RoutineConstants.TRANSITION_STATE transitionState) {
        this.transitionState.setValue(transitionState);
    }

    private final  MutableLiveData<String> exerciseName = new MutableLiveData<>();
    public void setExerciseName(String exerciseName) {
        this.exerciseName.setValue(exerciseName);
    }
    public LiveData<String> getExerciseName() {return  exerciseName;}


    private final MutableLiveData<TTSHelper> ttsHelper = new MutableLiveData<>();
    public void setTtsHelper(Context context, TTSInitializationListener listener) {
        ttsHelper.setValue(new TTSHelper(context, listener));
    }
    public LiveData<TTSHelper> getTtsHelper() {
        return ttsHelper;
    }

    private final MutableLiveData<Long> timeLeft = new MutableLiveData<>();
    public LiveData<Long> getTimeLeft() {
        return  timeLeft;
    }

    public void startTransition() {

        Objects.requireNonNull(ttsHelper.getValue()).speak("Prepare for" + exerciseName.getValue());

        // Initialize the CountDownTimer and play the exercise name
        try (AssetFileDescriptor afd = context.getAssets().openFd("sounds/timer.wav")) {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd. getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Cancel any existing timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }


        countDownTimer= new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setValue(millisUntilFinished); // Update time left
                transitionState.setValue(RoutineConstants.TRANSITION_STATE.ONGOING); // Update transition state

                mediaPlayer.start();
            }

            @Override
            public void onFinish() {
                transitionState.setValue(RoutineConstants.TRANSITION_STATE.FINISHED); // Update state when finished
            }
        };

        // Reset the state and time left
        timeLeft.setValue(6000L);
        transitionState.setValue(RoutineConstants.TRANSITION_STATE.PENDING);

        // Start the timer
        countDownTimer.start();
    }
}