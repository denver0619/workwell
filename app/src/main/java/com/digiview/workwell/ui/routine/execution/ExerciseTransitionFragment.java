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
import android.widget.Toast;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExerciseDetailDTO;
import com.digiview.workwell.databinding.FragmentExerciseTransitionBinding;
import com.digiview.workwell.services.mediapipe.TTSInitializationListener;

import java.util.Objects;


public class ExerciseTransitionFragment extends Fragment {

    private ExerciseTransitionViewModel exerciseTransitionViewModel;
    private FragmentExerciseTransitionBinding fragmentExerciseTransitionBinding;

    /**
     * Sample Exercise class
     * TODO: Implement in models then replace this
     * @return
     */
    private class ExerciseModelObject {
        private String exerciseName;
        private String deviceSetup;

        public ExerciseModelObject() {}

        public ExerciseModelObject(String exerciseName, String deviceSetup) {
            this.exerciseName = exerciseName;
            this.deviceSetup = deviceSetup;
        }

        public String getExerciseName() {
            return exerciseName;
        }

        public String getHowToPosition() {
            return deviceSetup;
        }
    }

    public static ExerciseTransitionFragment newInstance() {
        return new ExerciseTransitionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentExerciseTransitionBinding = FragmentExerciseTransitionBinding.inflate(inflater, container, false);
        return fragmentExerciseTransitionBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        exerciseTransitionViewModel = new ViewModelProvider(requireActivity()).get(ExerciseTransitionViewModel.class);

        if (getArguments() != null) {
            RoutineExerciseDetailDTO routineExerciseDetailDTO = (RoutineExerciseDetailDTO) getArguments().getSerializable("exerciseDTO");
            if (routineExerciseDetailDTO!= null) {
                exerciseTransitionViewModel.setExerciseDetailDTO(routineExerciseDetailDTO);
            }
        }

        fragmentExerciseTransitionBinding
                .transitionSetupContent
                        .setText(
                                Objects.requireNonNull(exerciseTransitionViewModel
                                                .getExerciseDetailDTO()
                                                .getValue())
                                        .getExerciseDeviceSetup()
                        );

        fragmentExerciseTransitionBinding.transitionText.setText("Prepare for " + exerciseTransitionViewModel.getExerciseDetailDTO().getValue().getExerciseName());

//        exerciseTransitionViewModel.setContext(requireContext());
//        exerciseTransitionViewModel.setMediaPlayer(new MediaPlayer());
//        exerciseTransitionViewModel.getTimeLeft().observe(getViewLifecycleOwner(), new Observer<Long>() {
//            @Override
//            public void onChanged(Long timeLeft) {
//                fragmentExerciseTransitionBinding
//                        .transitionCountdown
//                        .setText(
//                                String.valueOf(
//                                        (int) (timeLeft/1000)
//                                )
//                        );
//            }
//        });

        exerciseTransitionViewModel.setTtsHelper(requireContext(),
                new TTSInitializationListener() {
                    @Override
                    public void onTTSInitialized() {
                        exerciseTransitionViewModel.startSpeaking();
                    }
                }
        );
        fragmentExerciseTransitionBinding.proceedTransitionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exerciseTransitionViewModel
                                .setTransitionState(
                                        RoutineConstants.TRANSITION_STATE.FINISHED
                                );
                    }
                }
        );
    }

}