package com.digiview.workwell.ui.fitnesslog;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineLogs;
import com.digiview.workwell.ui.fitnesslog.adapter.FitnessLogAdapter;
import com.digiview.workwell.ui.fitnesslog.viewmodel.FitnessLogViewModel;

public class FitnessLogFragment extends Fragment {

    private FitnessLogViewModel fitnessLogViewModel;
    private FitnessLogAdapter fitnessLogAdapter;
    private RecyclerView rvFitnessLog;
    private ImageView progressBar;

    public static FitnessLogFragment newInstance() {
        return new FitnessLogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness_log, container, false);

        rvFitnessLog = view.findViewById(R.id.rvFitnessLog);
        progressBar = view.findViewById(R.id.progressBar);
        // Load GIF into ImageView using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.ic_loading) // Replace with your actual GIF in drawable
                .into(progressBar);

        rvFitnessLog.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fitnessLogViewModel = new ViewModelProvider(this).get(FitnessLogViewModel.class);

        // Show progress bar before loading data
        progressBar.setVisibility(View.VISIBLE);
        rvFitnessLog.setVisibility(View.GONE);

        fitnessLogViewModel.getRoutineLogsLiveData().observe(getViewLifecycleOwner(), routineLogs -> {
            // Hide progress bar when data is loaded
            progressBar.setVisibility(View.GONE);
            rvFitnessLog.setVisibility(View.VISIBLE);

            // Handle "View Details" click (e.g., navigate to next fragment)
            fitnessLogAdapter = new FitnessLogAdapter(requireContext(), routineLogs, this::navigateToRoutineLogDetails);
            rvFitnessLog.setAdapter(fitnessLogAdapter);
        });
    }

    private void navigateToRoutineLogDetails(RoutineLogs routineLogs) {
        // Logic to navigate to details fragment and pass the selected routineLogs object
        FitnessLogDetailFragment detailFragment = FitnessLogDetailFragment.newInstance();

        // Pass the selected RoutineLogs object to the next fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedRoutineLog", routineLogs);
        detailFragment.setArguments(bundle);

        // Perform the fragment transaction
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragmentContainer, detailFragment) // Replace with your container ID
                .addToBackStack(null) // Add to back stack to allow "Back" navigation
                .commit();
    }
}
