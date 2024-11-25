package com.digiview.workwell.ui.routine.execution;

import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.digiview.workwell.R;

import com.digiview.workwell.databinding.FragmentRoutineLooperBinding;
import com.digiview.workwell.services.exercises.Exercise;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;



public class RoutineLooperFragment extends Fragment {

    public static RoutineLooperFragment newInstance() {
        return new RoutineLooperFragment();
    }

    private RoutineLooperViewModel routineViewModel;
    private RoutineExecutionViewModel routineExecutionViewModel;
    private ExerciseTransitionViewModel exerciseTransitionViewModel;
    private CameraViewModel cameraViewModel;
    private FragmentRoutineLooperBinding fragmentRoutineBinding;

    private ProcessCameraProvider cameraProvider;




    // SAMPLE DATABASE MOCK DATA
    private List<RoutineExerciseEntity> routine = new ArrayList<>(
            Arrays.asList(
                    new RoutineExerciseEntity(
                            "",
                            "Longus Colli Stretch",
                            "",
                            2,
                            3000L
                    )
//                    new RoutineExerciseEntity(
//                            "",
//                            "Latissimus Dorsi, Teres Major Stretch Left",
//                            "",
//                            2,
//                            3000L
//                    )
//                    new RoutineExerciseEntity(
//                            "",
//                            "Quadriceps Stretch Left",
//                            "",
//                            1,
//                            10000L
//                    )
            )
    );
    private String logID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_routine, container, false);
        fragmentRoutineBinding = FragmentRoutineLooperBinding.inflate(inflater, container, false);
        return fragmentRoutineBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logID = String.valueOf(System.currentTimeMillis());
        //initialize view model
        routineViewModel = new ViewModelProvider(requireActivity()).get(RoutineLooperViewModel.class);
        routineExecutionViewModel = new ViewModelProvider(requireActivity()).get(RoutineExecutionViewModel.class);
        exerciseTransitionViewModel = new ViewModelProvider(requireActivity()).get(ExerciseTransitionViewModel.class);
        cameraViewModel = new ViewModelProvider(requireActivity()).get(CameraViewModel.class);

        // initialize values
        cameraViewModel.setFitnessLogID(logID);
        cameraViewModel.setSaveDirectory(requireContext(),logID);

        //perform things
//        routineViewModel.setTtsHelper(getContext());
        routineViewModel.setRoutine(routine);
        routineViewModel.getDestination().observe(getViewLifecycleOwner(), new Observer<Class<? extends Fragment>>() {
            @Override
            public void onChanged(Class<? extends Fragment> destinationFragment) {
                try {
                    Fragment destination = destinationFragment.newInstance();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right
                    );
                    transaction.replace(R.id.routine_fragment_container, destination)
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        routineViewModel.getCurrentExercise().observe(getViewLifecycleOwner(), new Observer<Exercise>() {
            @Override
            public void onChanged(Exercise exercise) {
                RoutineLooperFragment.this.routineExecutionViewModel.setExercise(exercise);
            }
        });

        routineExecutionViewModel.getExecutionState().observe(getViewLifecycleOwner(), new Observer<RoutineConstants.EXECUTION_STATE>() {
            @Override
            public void onChanged(RoutineConstants.EXECUTION_STATE executionState) {
                RoutineLooperFragment.this.routineViewModel.setExecutionState(executionState);
                RoutineLooperFragment.this.routineViewModel.setExecutionState(RoutineConstants.EXECUTION_STATE.NONE);
            }
        });


        routineViewModel.executeRoutine();

        exerciseTransitionViewModel.getTransitionState().observe(getViewLifecycleOwner(), new Observer<RoutineConstants.TRANSITION_STATE>() {
            @Override
            public void onChanged(RoutineConstants.TRANSITION_STATE transitionState) {
                routineViewModel.setTransitionState(transitionState);
            }
        });

        // DEBUG CODE
        routineViewModel.getToastMsg().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        // SELF ASSESSMENT ENTRY POINT
        routineViewModel.getIsRoutineFinished().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isRoutineFinished) {
                if(isRoutineFinished) {
                    requireActivity().finish();
//                    Intent intent = new Intent(requireActivity(), SelfAssessmentActivity.class);
//                    requireActivity().startActivity(intent);
                }
            }
        });
    }

}