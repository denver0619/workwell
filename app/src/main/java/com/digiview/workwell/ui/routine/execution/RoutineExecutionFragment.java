package com.digiview.workwell.ui.routine.execution;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.digiview.workwell.databinding.FragmentRoutineExecutionBinding;
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult;

public class RoutineExecutionFragment extends Fragment {

    private RoutineExecutionViewModel routineExecutionViewModel;
    private RoutineLooperViewModel routineViewModel;
    private CameraViewModel cameraViewModel;
    private FragmentRoutineExecutionBinding routineExecutionBinding;

    public static RoutineExecutionFragment newInstance() {
        return new RoutineExecutionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        routineExecutionBinding = FragmentRoutineExecutionBinding.inflate(inflater, container, false);
        return routineExecutionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // instantiate view models
        routineExecutionViewModel = new ViewModelProvider(requireActivity()).get(RoutineExecutionViewModel.class);
        cameraViewModel = new ViewModelProvider(requireActivity()).get(CameraViewModel.class);
        routineViewModel = new ViewModelProvider(requireActivity()).get(RoutineLooperViewModel.class);

        //initialize some members
        routineExecutionViewModel.setTtsHelper(getContext());
        routineExecutionViewModel.setMediaPlayer(new MediaPlayer());
        routineExecutionViewModel.setContext(requireContext());
        routineExecutionViewModel.init();

        // perform functions
        cameraViewModel.getLandmarkerResult().observe(getViewLifecycleOwner(), new Observer<PoseLandmarkerResult>() {
            @Override
            public void onChanged(PoseLandmarkerResult poseLandmarkerResult) {
                RoutineExecutionFragment.this.routineExecutionViewModel.setPoseLandmarkerResult(poseLandmarkerResult);
            }
        });

        routineExecutionViewModel.getPoseLandmarkerResult().observe(getViewLifecycleOwner(), new Observer<PoseLandmarkerResult>() {
            @Override
            public void onChanged(PoseLandmarkerResult poseLandmarkerResult) {
                RoutineExecutionFragment.this
                        .routineExecutionViewModel
                        .processLandmarkerResult(
                                poseLandmarkerResult
                        );
            }
        });

        //TODO: check for current status of exercise
//        routineExecutionViewModel.getExecutionState().observe(getViewLifecycleOwner(), new Observer<RoutineConstants.EXECUTION_STATE>() {
//            @Override
//            public void onChanged(RoutineConstants.EXECUTION_STATE executionState) {
//                switch (executionState) {
//                    case PAUSED:
//
//                }
//            }
//        });


        // TODO: Use the ViewModel
        //
        routineExecutionViewModel.getStatusText().observe(getViewLifecycleOwner(), text ->
                routineExecutionBinding.status.setText(text)
        );

        routineExecutionViewModel.getStatusColor().observe(getViewLifecycleOwner(), color ->
                routineExecutionBinding.status.setTextColor(color)
        );

        routineExecutionViewModel.getTimeLeft().observe(getViewLifecycleOwner(), timeLeft ->
                routineExecutionBinding.timeLeft.setText("Time Left: " + String.format("%.2f", (double)(timeLeft/1000)))
        );

        routineExecutionViewModel.getAngles().observe(getViewLifecycleOwner(), new Observer<double[]>() {
            @Override
            public void onChanged(double[] angles) {
                routineExecutionBinding.angle1.setText("ANGLE1: " + String.format("%.2f", angles[0]));
                routineExecutionBinding.angle2.setText("ANGLE1: " + String.format("%.2f", angles[1]));
//                routineExecutionBinding.angle3.setText("ANGLE1: " + String.format("%.2f", angles[2]));

            }
        });
        routineExecutionViewModel.getCounter().observe(getViewLifecycleOwner(), count ->
                routineExecutionBinding.counter.setText("Counter: " + count)
        );

        routineExecutionViewModel.getExecutionState().observe(getViewLifecycleOwner(), new Observer<RoutineConstants.EXECUTION_STATE>() {
            @Override
            public void onChanged(RoutineConstants.EXECUTION_STATE executionState) {
                routineViewModel.setExecutionState(executionState);
            }
        });
    }

}