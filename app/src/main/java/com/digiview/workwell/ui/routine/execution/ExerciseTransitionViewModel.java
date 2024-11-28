package com.digiview.workwell.ui.routine.execution;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.CountDownTimer;
import android.media.MediaPlayer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

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


    private final MutableLiveData<Long> timeLeft = new MutableLiveData<>();
    public LiveData<Long> getTimeLeft() {
        return  timeLeft;
    }

    public void startTransition() {// Initialize the CountDownTimer
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