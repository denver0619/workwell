package com.digiview.workwell.ui.main;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.digiview.workwell.R;
import com.digiview.workwell.ui.home.HomeFragment;
import com.digiview.workwell.ui.profile.ProfileFragment;
import com.digiview.workwell.ui.routine.RoutineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView navigationBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }


        navigationBarView = findViewById(R.id.bottomNav);

        navigationBarView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                Log.d(TAG, "home");
                replaceFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_routine) {
                Log.d(TAG, "routine");
                replaceFragment(new RoutineFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_activity_log) {
                Log.d(TAG, "activity log");
                return true;
            } else if (item.getItemId() == R.id.menu_profile) {
                Log.d(TAG, "profile");
                replaceFragment(new ProfileFragment());
                return true;
            } else {
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}