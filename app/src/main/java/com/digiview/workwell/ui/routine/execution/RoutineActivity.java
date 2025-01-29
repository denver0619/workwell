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

    private String videoId;
    private String routineId;
    private String routineName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        routineId = getIntent().getStringExtra("ROUTINE_ID");
        routineName = getIntent().getStringExtra("ROUTINE_NAME");
        ArrayList<RoutineExercise> exercises = (ArrayList<RoutineExercise>) getIntent().getSerializableExtra("EXERCISES");

        if (savedInstanceState == null) {
            RoutineLooperFragment routineLooperFragment = RoutineLooperFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putSerializable("EXERCISES", exercises);
            routineLooperFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, routineLooperFragment)
                    .commitNow();
        }
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
        Log.d("RoutineActivity", "Received videoId: " + videoId);
    }

    @Override
    public void onRoutineFinished(List<RoutineExercise> exercises) {
        Log.d("RoutineActivity", "onRoutineFinished called with videoId: " + videoId);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("VIDEO_ID", videoId); // Pass videoId to MainActivity
        resultIntent.putExtra("ROUTINE_ID", routineId);
        resultIntent.putExtra("ROUTINE_NAME", routineName);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


}
