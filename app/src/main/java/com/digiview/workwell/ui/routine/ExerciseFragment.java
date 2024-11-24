package com.digiview.workwell.ui.routine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExercise;

public class ExerciseFragment extends Fragment {

    public static ExerciseFragment newInstance() {
        return new ExerciseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the RoutineExercise object from arguments
        RoutineExercise exercise = null;
        if (getArguments() != null) {
            exercise = (RoutineExercise) getArguments().getSerializable("EXERCISE");
        }

        // Set up the back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Navigate back to the previous fragment
            getParentFragmentManager().popBackStack();
        });

        // Populate the UI with the exercise details
        TextView tvExerciseTitle = view.findViewById(R.id.tvExerciseTitle);
        TextView tvExerciseDetail = view.findViewById(R.id.tvExerciseDetail);

        if (exercise != null) {
            tvExerciseTitle.setText(exercise.getExerciseName()); // Set exercise name
            tvExerciseDetail.setText(exercise.getExerciseDescription()); // Set exercise description
        }
    }
}
