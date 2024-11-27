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

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineLogs;
import com.digiview.workwell.ui.fitnesslog.adapter.FitnessLogAdapter;
import com.digiview.workwell.ui.fitnesslog.viewmodel.FitnessLogViewModel;

public class FitnessLogFragment extends Fragment {

    private FitnessLogViewModel fitnessLogViewModel;
    private FitnessLogAdapter fitnessLogAdapter;
    private RecyclerView rvFitnessLog;

    public static FitnessLogFragment newInstance() {
        return new FitnessLogFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fitness_log, container, false);

        rvFitnessLog = root.findViewById(R.id.rvFitnessLog);
        rvFitnessLog.setLayoutManager(new LinearLayoutManager(requireContext()));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fitnessLogViewModel = new ViewModelProvider(this).get(FitnessLogViewModel.class);

        fitnessLogViewModel.getRoutineLogsLiveData().observe(getViewLifecycleOwner(), routineLogs -> {
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
