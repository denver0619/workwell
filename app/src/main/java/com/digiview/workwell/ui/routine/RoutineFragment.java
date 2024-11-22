package com.digiview.workwell.ui.routine;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.workwell.R;
import com.digiview.workwell.models.Routine;
import com.digiview.workwell.ui.routine.adapter.RoutineAdapter;
import com.digiview.workwell.ui.routine.viewmodel.RoutineViewModel;

import java.util.ArrayList;

public class RoutineFragment extends Fragment implements RoutineAdapter.OnRoutineClickListener {

    private RecyclerView rvRoutine;
    private RoutineAdapter routineAdapter;
    private RoutineViewModel routineViewModel;

    public static RoutineFragment newInstance() {
        return new RoutineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        // Initialize RecyclerView
        rvRoutine = view.findViewById(R.id.rvRoutine);
        rvRoutine.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize ViewModel
        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);

        // Set up the adapter
        routineAdapter = new RoutineAdapter(new ArrayList<>(), this);
        rvRoutine.setAdapter(routineAdapter);

        // Observe LiveData for routine list
        routineViewModel.getRoutineList().observe(getViewLifecycleOwner(), routineList -> {
            if (routineList != null) {
                routineAdapter.updateDataList(routineList);
            }
        });

        // Observe loading state (optional)
        routineViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                Log.d("RoutineFragment", "Loading routines...");
                // Show a loading indicator if necessary
            } else {
                Log.d("RoutineFragment", "Finished loading routines.");
                // Hide the loading indicator
            }
        });

        // Trigger data fetch
        routineViewModel.fetchRoutines();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRoutineClicked(Routine routine) {
        // Pass the entire Routine object to another fragment
        RoutineDetailFragment detailFragment = new RoutineDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ROUTINE", routine); // Pass the Routine object (it implements Serializable)
        detailFragment.setArguments(bundle);

        // Navigate to the detail fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragmentContainer, detailFragment); // Replace with the correct container ID
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
