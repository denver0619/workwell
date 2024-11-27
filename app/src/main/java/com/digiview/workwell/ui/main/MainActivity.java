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

    public void startRoutineActivity(String routineLogId, ArrayList<RoutineExercise> exercises) {
        Intent intent = new Intent(this, RoutineActivity.class);
        intent.putExtra("ROUTINE_LOG_ID", routineLogId);
        intent.putExtra("EXERCISES", exercises);
        startActivityForResult(intent, REQUEST_ROUTINE_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ROUTINE_ACTIVITY && resultCode == RESULT_OK && data != null) {
            String routineLogId = data.getStringExtra("ROUTINE_LOG_ID");
            ArrayList<RoutineExercise> exercises =
                    (ArrayList<RoutineExercise>) data.getSerializableExtra("EXERCISES");

            openSelfAssessmentFragment(routineLogId, exercises);
        }
    }

    private void openSelfAssessmentFragment(String routineLogId, ArrayList<RoutineExercise> exercises) {
        SelfAssessmentFragment selfAssessmentFragment = new SelfAssessmentFragment();

        Bundle bundle = new Bundle();
        bundle.putString("ROUTINE_LOG_ID", routineLogId);
        bundle.putSerializable("EXERCISES", exercises);
        selfAssessmentFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragmentContainer, selfAssessmentFragment)
                .addToBackStack(null)
                .commit();
    }
}
