package com.digiview.workwell.ui.routine.execution;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.workwell.R;
import com.digiview.workwell.databinding.FragmentExerciseTransitionBinding;


public class ExerciseTransitionFragment extends Fragment {

    private ExerciseTransitionViewModel exerciseTransitionViewModel;
    private FragmentExerciseTransitionBinding fragmentExerciseTransitionBinding;

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
        // TODO: Use the ViewModel
        exerciseTransitionViewModel.getTimeLeft().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long timeLeft) {
                fragmentExerciseTransitionBinding
                        .transitionCountdown
                        .setText(
                                String.valueOf(
                                        (int) (timeLeft/1000)
                                )
                        );
            }
        });
        exerciseTransitionViewModel.startTransition();
    }

}