package com.digiview.workwell;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.digiview.workwell.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;
    private final MainViewModel viewModel = new MainViewModel(); // Adjust if using ViewModelProvider

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the binding
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        // Set up Navigation
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(activityMainBinding.navigation, navController);

        // Handle navigation item reselection
        activityMainBinding.navigation.setOnNavigationItemReselectedListener(item -> {
            // Ignore reselection
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Finish activity on back pressed
        finish();
    }
}