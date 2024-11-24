package com.digiview.workwell.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digiview.workwell.R;
import com.digiview.workwell.models.Routine;
import com.digiview.workwell.models.RoutineExercise;
import com.digiview.workwell.services.ui.RoutineService;

import java.text.MessageFormat;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private TextView tvUsername;
    private ProgressBar progressBar;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize views
        tvUsername = view.findViewById(R.id.tvUsername);
        progressBar = view.findViewById(R.id.progressBar);  // Initialize ProgressBar

        // Initialize the ViewModel
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observe the displayName LiveData
        mViewModel.getDisplayName().observe(getViewLifecycleOwner(), fullName -> {
            // Set the full name when it's available
            tvUsername.setText(fullName);
        });

        // Observe the isLoading LiveData
        mViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                progressBar.setVisibility(View.VISIBLE);  // Show loading indicator
                tvUsername.setVisibility(View.GONE);  // Hide username until data is loaded
            } else {
                progressBar.setVisibility(View.GONE);  // Hide loading indicator
                tvUsername.setVisibility(View.VISIBLE);  // Show username
            }
        });



        // Fetch user data from the ViewModel
        mViewModel.fetchUserData();
    }
}