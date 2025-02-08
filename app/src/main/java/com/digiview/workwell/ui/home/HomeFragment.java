package com.digiview.workwell.ui.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import com.digiview.workwell.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.digiview.workwell.ui.routine.RoutineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private TextView tvUsername, tvHeight, tvWeight, tvProfessional;
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
        progressBar = view.findViewById(R.id.progressBar);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvProfessional = view.findViewById(R.id.tvProfessional);
        Button btnStartRoutine = view.findViewById(R.id.btnStartRoutine);

        btnStartRoutine.setOnClickListener(v -> {
            // Get the BottomNavigationView from the activity
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);

            // Set the selected item to Routine
            bottomNav.setSelectedItemId(R.id.menu_routine);
        });


        // Initialize the ViewModel
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observe and update the username
        mViewModel.getDisplayName().observe(getViewLifecycleOwner(), fullName -> {
            if (fullName != null) {
                String formattedText = getString(R.string.user_name, fullName);
                tvUsername.setText(formattedText);
            }
        });

        // Observe and update loading state
        mViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                tvUsername.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                tvUsername.setVisibility(View.VISIBLE);
            }
        });

        // Observe and update height and weight
        mViewModel.getHeight().observe(getViewLifecycleOwner(), height -> {
            tvHeight.setText(height != null ? height : "N/A");
        });

        mViewModel.getWeight().observe(getViewLifecycleOwner(), weight -> {
            tvWeight.setText(weight != null ? weight : "N/A");
        });

        // Observe and update assigned professional
        mViewModel.getAssignedProfessional().observe(getViewLifecycleOwner(), professional -> {
            tvProfessional.setText(professional != null ? professional : "Unassigned");
        });

        // Fetch user data
        mViewModel.fetchUserData();
    }
}
