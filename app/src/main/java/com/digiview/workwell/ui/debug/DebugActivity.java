package com.digiview.workwell.ui.debug;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.ui.routine.execution.RoutineActivity;
import com.google.common.hash.Hashing;

import java.util.ArrayList;

public class DebugActivity extends AppCompatActivity {
    private static final int REQUEST_ROUTINE_ACTIVITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debug);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        RoutineExercise exercise1 = new RoutineExercise();
        exercise1.setExerciseName("Longus Colli Stretch");
        exercise1.setReps(2);
        exercise1.setDuration(5000);
        RoutineExercise exercise2 = new RoutineExercise();
        exercise2.setExerciseName("Quadriceps Stretch Right");
        exercise2.setReps(2);
        exercise2.setDuration(5000);
        RoutineExercise exercise3 = new RoutineExercise();
        exercise3.setExerciseName("Quadriceps Stretch Left");
        exercise3.setReps(2);
        exercise3.setDuration(5000);


        Button startRoutine = findViewById(R.id.startRoutineButton);

        ArrayList<RoutineExercise> exercises = new ArrayList<>();
        exercises.add(exercise1);
        exercises.add(exercise2);
        exercises.add(exercise3);

        startRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RoutineActivity.class);
                intent.putExtra("ROUTINE_LOG_ID", Hashing.adler32().toString());
                intent.putExtra("EXERCISES", exercises);
                startActivityForResult(intent, REQUEST_ROUTINE_ACTIVITY);
            }
        });
    }

}