package com.digiview.workwell.ui.routine.execution;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.digiview.workwell.R;

import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.databinding.FragmentRoutineLooperBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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

    private String routineLogId;
    private List<RoutineExercise> exercises;
    private OnRoutineFinishedListener onRoutineFinishedListener;

    // SAMPLE DATABASE MOCK DATA
//    private List<RoutineExerciseEntity> routine = new ArrayList<>(
//            Arrays.asList(
//                    new RoutineExerciseEntity(
//                            "",
//                            "Longus Colli Stretch",
//                            "",
//                            2,
//                            3000L
//                    )
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
//            )
//    );

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRoutineFinishedListener) {
            onRoutineFinishedListener = (OnRoutineFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRoutineFinishedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRoutineFinishedListener = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve data from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            routineLogId = arguments.getString("ROUTINE_LOG_ID");
            exercises = (ArrayList<RoutineExercise>) arguments.getSerializable("EXERCISES");
            Log.d("RoutineLooperFragment", "RoutineLogId: " + routineLogId);

            if (exercises != null) {
                for (RoutineExercise exercise : exercises) {
                    Log.d("RoutineLooperFragment", "Exercise: " + exercise.getExerciseName());
                }
            }
        } else {
            Log.e("RoutineLooperFragment", "No arguments provided to the fragment.");
        }
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
        //initialize view model
        routineViewModel = new ViewModelProvider(requireActivity()).get(RoutineLooperViewModel.class);
        routineExecutionViewModel = new ViewModelProvider(requireActivity()).get(RoutineExecutionViewModel.class);
        exerciseTransitionViewModel = new ViewModelProvider(requireActivity()).get(ExerciseTransitionViewModel.class);
        cameraViewModel = new ViewModelProvider(requireActivity()).get(CameraViewModel.class);

        // Generate a unique temporary ID
        String tempRoutineLogId = UUID.randomUUID().toString();

// Initialize values with the temporary ID
        cameraViewModel.setFitnessLogID(tempRoutineLogId);
        cameraViewModel.setSaveDirectory(requireContext(), tempRoutineLogId);

        //perform things
//        routineViewModel.setTtsHelper(getContext());
        routineViewModel.setRoutine(exercises);
        // Observe destination changes
        routineViewModel.getDestination().observe(getViewLifecycleOwner(), destinationFragment -> {
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
        });

        // Observe current exercise
        routineViewModel.getCurrentExercise().observe(getViewLifecycleOwner(), exercise -> {
            routineExecutionViewModel.setExercise(exercise);
        });

        // Observe execution state
        routineExecutionViewModel.getExecutionState().observe(getViewLifecycleOwner(), executionState -> {
            routineViewModel.setExecutionState(executionState);
            routineViewModel.setExecutionState(RoutineConstants.EXECUTION_STATE.NONE);
        });

        // Execute the routine
        routineViewModel.executeRoutine();

        // Observe transition state
        exerciseTransitionViewModel.getTransitionState().observe(getViewLifecycleOwner(), routineViewModel::setTransitionState);

        // Debugging: Show Toast messages
        routineViewModel.getToastMsg().observe(getViewLifecycleOwner(), s -> Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show());

        // Self-assessment entry point
        routineViewModel.getIsRoutineFinished().observe(getViewLifecycleOwner(), isRoutineFinished -> {
            if (isRoutineFinished) {
                // Pass data to the parent activity using the callback
                if (onRoutineFinishedListener != null) {
                    onRoutineFinishedListener.onRoutineFinished(exercises);
                }

                // Close the RoutineLooperFragment
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }

    public interface OnRoutineFinishedListener {
        void onRoutineFinished(List<RoutineExercise> exercises);
    }



}