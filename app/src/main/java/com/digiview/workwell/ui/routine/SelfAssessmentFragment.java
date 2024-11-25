package com.digiview.workwell.ui.routine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.ui.routine.viewmodel.SelfAssessmentViewModel;

import java.util.List;

public class SelfAssessmentFragment extends Fragment {

    private SelfAssessmentViewModel viewModel;
    private SeekBar seekBarStiffness, seekBarPain, seekBarDifficulty, seekBarAwareness;
    private TextView tvStiffnessValue, tvPainValue, tvDifficultyValue, tvAwarenessValue;
    private Button btnSubmit;

    private String routineLogId;
    private List<RoutineExercise> exercises;

    public static SelfAssessmentFragment newInstance(String routineLogId, List<RoutineExercise> exercises) {
        SelfAssessmentFragment fragment = new SelfAssessmentFragment();
        Bundle args = new Bundle();
        args.putString("ROUTINE_LOG_ID", routineLogId);
        args.putSerializable("EXERCISES", (java.io.Serializable) exercises);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_assessment, container, false);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(SelfAssessmentViewModel.class);

        // Initialize Views
        seekBarStiffness = view.findViewById(R.id.seekBarStiffness);
        seekBarPain = view.findViewById(R.id.seekBarPain);
        seekBarDifficulty = view.findViewById(R.id.seekBarDifficulty);
        seekBarAwareness = view.findViewById(R.id.seekBarAwareness);

        tvStiffnessValue = view.findViewById(R.id.tvStiffnessValue);
        tvPainValue = view.findViewById(R.id.tvPainValue);
        tvDifficultyValue = view.findViewById(R.id.tvDifficultyValue);
        tvAwarenessValue = view.findViewById(R.id.tvAwarenessValue);

        btnSubmit = view.findViewById(R.id.btnSubmit);

        // Setup listeners and observers
        setupSeekBarListeners();
        setupObservers();

        // Submit Button
        btnSubmit.setOnClickListener(v -> handleSubmit());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve arguments
        if (getArguments() != null) {
            routineLogId = getArguments().getString("ROUTINE_LOG_ID");
            exercises = (List<RoutineExercise>) getArguments().getSerializable("EXERCISES");

            Log.d("SelfAssessmentFragment", "RoutineLogId: " + routineLogId);
            if (exercises != null) {
                for (RoutineExercise exercise : exercises) {
                    Log.d("SelfAssessmentFragment", "Exercise: " + exercise.getExerciseName());
                }
            }
        }
    }

    private void setupSeekBarListeners() {
        seekBarStiffness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStiffnessValue.setText("Value: " + progress);
                viewModel.setStiffness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarPain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPainValue.setText("Value: " + progress);
                viewModel.setPain(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDifficultyValue.setText("Value: " + progress);
                viewModel.setDifficulty(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarAwareness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAwarenessValue.setText("Value: " + progress);
                viewModel.setAwareness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupObservers() {
        viewModel.getStiffness().observe(getViewLifecycleOwner(), value ->
                tvStiffnessValue.setText("Value: " + value));

        viewModel.getPain().observe(getViewLifecycleOwner(), value ->
                tvPainValue.setText("Value: " + value));

        viewModel.getDifficulty().observe(getViewLifecycleOwner(), value ->
                tvDifficultyValue.setText("Value: " + value));

        viewModel.getAwareness().observe(getViewLifecycleOwner(), value ->
                tvAwarenessValue.setText("Value: " + value));
    }

    private void handleSubmit() {
        
        viewModel.submitData(routineLogId);

        Toast.makeText(requireContext(), "Data submitted successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back to previous activity or show confirmation
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
