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
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SelfAssessmentFragment extends Fragment {

    private SelfAssessmentViewModel viewModel;
    private SeekBar seekBarStiffness, seekBarPain, seekBarDifficulty, seekBarAwareness;
    private TextView tvStiffnessValue, tvPainValue, tvDifficultyValue, tvAwarenessValue;
    private Button btnSubmit;

    private String videoId;
    private String routineId;
    private String routineName;

    public static SelfAssessmentFragment newInstance(String videoId, String routineId, String routineName) {
        SelfAssessmentFragment fragment = new SelfAssessmentFragment();
        Bundle args = new Bundle();
        args.putString("VIDEO_ID", videoId);
        args.putString("ROUTINE_ID", routineId);
        args.putString("ROUTINE_NAME", routineName);
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

        if (getArguments() != null) {
            videoId = getArguments().getString("VIDEO_ID"); // Get Video ID
            routineId = getArguments().getString("ROUTINE_ID");
            routineName = getArguments().getString("ROUTINE_NAME");

            Log.d("SelfAssessmentFragment", "Received Video ID: " + videoId);
            Log.d("SelfAssessmentFragment", "Routine ID: " + routineId);
            Log.d("SelfAssessmentFragment", "Routine Name: " + routineName);
        } else {
            Log.e("SelfAssessmentFragment", "Error: VIDEO_ID not found in arguments");
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
        if (videoId.isEmpty() || routineId.isEmpty() || routineName.isEmpty()) {
            Toast.makeText(requireContext(), "Error: Missing required data.", Toast.LENGTH_SHORT).show();
            Log.e("SelfAssessmentFragment", "Error: Missing data: Video ID: " + videoId + ", Routine ID: " + routineId + ", Routine Name: " + routineName);
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // Call ViewModel to submit SelfAssessment and create RoutineLog
        viewModel.submitData(videoId, routineId, routineName, uid);

        Toast.makeText(requireContext(), "Submitting SelfAssessment and creating RoutineLog...", Toast.LENGTH_SHORT).show();

        // Navigate back to previous activity or show confirmation
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
