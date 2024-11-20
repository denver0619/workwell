package com.digiview.workwell.ui.routine;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.digiview.workwell.R;
import com.digiview.workwell.ui.routine.adapter.RoutineDetailAdapter;
import com.digiview.workwell.ui.routine.viewmodel.RoutineDetailViewModel;

import java.util.ArrayList;

public class RoutineDetailFragment extends Fragment implements RoutineDetailAdapter.OnRoutineClickListener {

    private String routineTitle;
    private RecyclerView rvRoutineDetail;
    private RoutineDetailAdapter routineDetailAdapter;
    private RoutineDetailViewModel mViewModel;

    public static RoutineDetailFragment newInstance() {
        return new RoutineDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_detail, container, false);

        // Retrieve the title from arguments
        if (getArguments() != null) {
            routineTitle = getArguments().getString("ROUTINE_TITLE");
        }

        // Find the back button and set up the listener
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Navigate back to the previous fragment
            getParentFragmentManager().popBackStack();
        });

        rvRoutineDetail = view.findViewById(R.id.rvRoutineDetail);
        rvRoutineDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel = new ViewModelProvider(this).get(RoutineDetailViewModel.class);

        routineDetailAdapter = new RoutineDetailAdapter(new ArrayList<>(), this);
        rvRoutineDetail.setAdapter(routineDetailAdapter);

        mViewModel.getDataList().observe(getViewLifecycleOwner(), routineDetailList -> {
            routineDetailAdapter.updateDataList(routineDetailList);
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Use the title for setting the fragment’s title or displaying it in a TextView
        TextView tvRoutineTitle = view.findViewById(R.id.tvRoutineTitle);
        if (routineTitle != null) {
            routineTitle = routineTitle.replace("\n", " ");
            tvRoutineTitle.setText(routineTitle);
        }
    }

    @Override
    public void onExerciseClicked(String exerciseTitle) {

    }
}