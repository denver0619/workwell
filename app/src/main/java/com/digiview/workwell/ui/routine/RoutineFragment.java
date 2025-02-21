package com.digiview.workwell.ui.routine;

import androidx.core.view.ViewCompat;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.ui.routine.adapter.RoutineAdapter;
import com.digiview.workwell.ui.routine.viewmodel.RoutineViewModel;

import java.util.ArrayList;

public class RoutineFragment extends Fragment implements RoutineAdapter.OnRoutineClickListener {

    private RecyclerView rvActiveRoutine, rvInactiveRoutine;
    private RoutineAdapter activeAdapter, inactiveAdapter;
    private RoutineViewModel routineViewModel;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        // Initialize RecyclerViews
        rvActiveRoutine = view.findViewById(R.id.rvActiveRoutine);
        rvInactiveRoutine = view.findViewById(R.id.rvInactiveRoutine);
        progressBar = view.findViewById(R.id.progressBar);

        rvActiveRoutine.setLayoutManager(new LinearLayoutManager(getContext()));
        rvInactiveRoutine.setLayoutManager(new LinearLayoutManager(getContext()));

        activeAdapter = new RoutineAdapter(new ArrayList<>(), this, true);  // true = show button
        inactiveAdapter = new RoutineAdapter(new ArrayList<>(), this, false); // false = hide button

        rvActiveRoutine.setAdapter(activeAdapter);
        rvInactiveRoutine.setAdapter(inactiveAdapter);

        // Initialize ViewModel
        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);

        // Initialize empty message TextViews
        TextView tvNoActiveRoutines = view.findViewById(R.id.tvNoActiveRoutines);
        TextView tvNoInactiveRoutines = view.findViewById(R.id.tvNoInactiveRoutines);

        // Observe Active Routines
        routineViewModel.getActiveRoutines().observe(getViewLifecycleOwner(), activeList -> {
            Log.d("RoutineFragment", "Active Routines Count: " + activeList.size());
            activeAdapter.updateDataList(activeList);

            // Show message if empty
            if (activeList.isEmpty()) {
                rvActiveRoutine.setVisibility(View.GONE);
                tvNoActiveRoutines.setVisibility(View.VISIBLE);
            } else {
                rvActiveRoutine.setVisibility(View.VISIBLE);
                tvNoActiveRoutines.setVisibility(View.GONE);
            }

            // Ensure proper height adjustment
            rvActiveRoutine.post(() -> setRecyclerViewHeightBasedOnChildren(rvActiveRoutine));
        });

        // Observe Inactive Routines
        routineViewModel.getInactiveRoutines().observe(getViewLifecycleOwner(), inactiveList -> {
            Log.d("RoutineFragment", "Inactive Routines Count: " + inactiveList.size());
            inactiveAdapter.updateDataList(inactiveList);

            // Show message if empty
            if (inactiveList.isEmpty()) {
                rvInactiveRoutine.setVisibility(View.GONE);
                tvNoInactiveRoutines.setVisibility(View.VISIBLE);
            } else {
                rvInactiveRoutine.setVisibility(View.VISIBLE);
                tvNoInactiveRoutines.setVisibility(View.GONE);
            }

            rvActiveRoutine.post(() -> setRecyclerViewHeightBasedOnChildren(rvInactiveRoutine));

        });



        // Observe Loading State
        routineViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            rvActiveRoutine.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            rvInactiveRoutine.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Fetch Data
        routineViewModel.fetchRoutines();

        return view;
    }

    @Override
    public void onRoutineClicked(Routine routine) {
        RoutineDetailFragment detailFragment = new RoutineDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ROUTINE", routine);
        detailFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    // Method to set RecyclerView height based on content
    private void setRecyclerViewHeightBasedOnChildren(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) return;

        int totalHeight = 0;
        for (int i = 0; i < adapter.getItemCount(); i++) {
            View itemView = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i)).itemView;
            itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            totalHeight += itemView.getMeasuredHeight();
        }

        // Set the height of RecyclerView based on children
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight + (recyclerView.getItemDecorationCount() * 10);  // Add padding if needed
        recyclerView.setLayoutParams(params);
        recyclerView.requestLayout();
    }

}

