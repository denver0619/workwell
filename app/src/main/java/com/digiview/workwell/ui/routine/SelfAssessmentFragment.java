package com.digiview.workwell.ui.routine;

import android.os.Bundle;
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
import com.digiview.workwell.ui.routine.viewmodel.SelfAssessmentViewModel;

public class SelfAssessmentFragment extends Fragment {

    private SelfAssessmentViewModel viewModel;
    private SeekBar seekBarStiffness, seekBarPain, seekBarDifficulty, seekBarAwareness;
    private TextView tvStiffnessValue, tvPainValue, tvDifficultyValue, tvAwarenessValue;
    private Button btnSubmit;

    public static SelfAssessmentFragment newInstance() {
        return new SelfAssessmentFragment();
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

        // Observe ViewModel
        setupObservers();

        // SeekBar Listeners
        setupSeekBarListeners();

        // Submit Button
        btnSubmit.setOnClickListener(v -> {
            // Prepare data for submission
            viewModel.submitData();
            Toast.makeText(requireContext(), "Data submitted", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void setupSeekBarListeners() {
        seekBarStiffness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStiffnessValue.setText("Value: " + progress);
                viewModel.setStiffness(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarPain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPainValue.setText("Value: " + progress);
                viewModel.setPain(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDifficultyValue.setText("Value: " + progress);
                viewModel.setDifficulty(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarAwareness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAwarenessValue.setText("Value: " + progress);
                viewModel.setAwareness(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
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
}
