package com.digiview.workwell.ui.routine.execution;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExercise;

import java.util.ArrayList;
import java.util.List;

public class RoutineActivity extends AppCompatActivity implements RoutineLooperFragment.OnRoutineFinishedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        String routineLogId = getIntent().getStringExtra("ROUTINE_LOG_ID");
        ArrayList<RoutineExercise> exercises = (ArrayList<RoutineExercise>) getIntent().getSerializableExtra("EXERCISES");

        if (savedInstanceState == null) {
            RoutineLooperFragment routineLooperFragment = RoutineLooperFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString("ROUTINE_LOG_ID", routineLogId);
            bundle.putSerializable("EXERCISES", exercises);
            routineLooperFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, routineLooperFragment)
                    .commitNow();
        }
    }

    @Override
    public void onRoutineFinished(String routineLogId, List<RoutineExercise> exercises) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("ROUTINE_LOG_ID", routineLogId);
        resultIntent.putExtra("EXERCISES", new ArrayList<>(exercises));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
