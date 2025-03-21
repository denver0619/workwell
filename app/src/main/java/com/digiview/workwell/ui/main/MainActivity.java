package com.digiview.workwell.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.data.models.RoutineExerciseDetailDTO;
import com.digiview.workwell.ui.diagnosis.DiagnosisFragment;
import com.digiview.workwell.ui.fitnesslog.FitnessLogFragment;
import com.digiview.workwell.ui.home.HomeFragment;
import com.digiview.workwell.ui.profile.ProfileFragment;
import com.digiview.workwell.ui.routine.RoutineFragment;
import com.digiview.workwell.ui.routine.SelfAssessmentFragment;
import com.digiview.workwell.ui.routine.execution.RoutineActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ROUTINE_ACTIVITY = 100;
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
                replaceFragment(new FitnessLogFragment());
                return true;
            } else if (item.getItemId() == R.id.menu_diagnosis) {
                Log.d(TAG, "diagnosis");
                replaceFragment(new DiagnosisFragment());
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

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    public void startRoutineActivity(ArrayList<RoutineExerciseDetailDTO> exercises, String routineId, String routineName) {
        Intent intent = new Intent(this, RoutineActivity.class);
        intent.putExtra("EXERCISES", exercises);
        intent.putExtra("ROUTINE_ID", routineId);
        intent.putExtra("ROUTINE_NAME", routineName);

        startActivityForResult(intent, REQUEST_ROUTINE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ROUTINE_ACTIVITY && resultCode == RESULT_OK && data != null) {
            String videoId = data.getStringExtra("VIDEO_ID"); // Get Video ID
            String routineId = data.getStringExtra("ROUTINE_ID");
            String routineName = data.getStringExtra("ROUTINE_NAME");

            if (videoId != null && routineId != null && routineName != null) {
                Log.d("MainActivity", "Video ID received: " + videoId);
                Log.d("MainActivity", routineId);
                Log.d("MainActivity", routineName);

                // Open SelfAssessmentFragment with video ID
                openSelfAssessmentFragment(videoId, routineId, routineName);
            } else {
                Log.e("MainActivity", "Error: Video ID not received.");
            }
        }
    }


    private void openSelfAssessmentFragment(String videoId, String routineId, String routineName) {
        SelfAssessmentFragment selfAssessmentFragment = new SelfAssessmentFragment();

        Bundle bundle = new Bundle();
        bundle.putString("VIDEO_ID", videoId);
        bundle.putString("ROUTINE_ID", routineId);
        bundle.putString("ROUTINE_NAME", routineName);
        selfAssessmentFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragmentContainer, selfAssessmentFragment)
                .addToBackStack(null)
                .commit();
    }
}
