package com.digiview.workwell.ui.routine;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digiview.workwell.R;
import com.digiview.workwell.ui.routine.adapter.RoutineAdapter;
import com.digiview.workwell.ui.routine.viewmodel.RoutineViewModel;

import java.util.ArrayList;

public class RoutineFragment extends Fragment implements RoutineAdapter.OnRoutineClickListener{

    private RecyclerView rvRoutine;
    private RoutineAdapter routineAdapter;
    private RoutineViewModel mViewModel;

    public static RoutineFragment newInstance() {
        return new RoutineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_routine, container, false);

        rvRoutine = view.findViewById(R.id.rvRoutine);
        rvRoutine.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);

        routineAdapter = new RoutineAdapter(new ArrayList<>(), this);
        rvRoutine.setAdapter(routineAdapter);

        mViewModel.getDataList().observe(getViewLifecycleOwner(), routineList -> {
            routineAdapter.updateDataList(routineList);
        });
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRoutineClicked(String routineTitle) {
        // Pass the title to another fragment
        RoutineDetailFragment detailFragment = new RoutineDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ROUTINE_TITLE", routineTitle);
        detailFragment.setArguments(bundle);

        // Navigate to the detail fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragmentContainer, detailFragment); // Replace with the correct container ID
        transaction.addToBackStack(null);
        transaction.commit();
    }
}