package com.digiview.workwell.ui.routine;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.ui.routine.adapter.RoutineDetailAdapter;
import com.digiview.workwell.ui.routine.viewmodel.RoutineDetailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoutineDetailFragment extends Fragment implements RoutineDetailAdapter.OnExerciseClickListener {

    private String routineTitle;
    private RecyclerView rvRoutineDetail;
    private RoutineDetailAdapter routineDetailAdapter;
    private RoutineDetailViewModel mViewModel;
    private FloatingActionButton fabPlay;

    public static RoutineDetailFragment newInstance() {
        return new RoutineDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routine_detail, container, false);

        // Retrieve the Routine object from arguments
        Routine routine = null;
        if (getArguments() != null) {
            routine = (Routine) getArguments().getSerializable("ROUTINE");
        }

        // Set the title
        TextView tvRoutineTitle = view.findViewById(R.id.tvRoutineTitle);
        if (routine != null && routine.getName() != null) {
            tvRoutineTitle.setText(routine.getName());
        }

        // Find the back button and set up the listener
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Navigate back to the previous fragment
            getParentFragmentManager().popBackStack();
        });

        rvRoutineDetail = view.findViewById(R.id.rvRoutineDetail);
        rvRoutineDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewModel = new ViewModelProvider(this).get(RoutineDetailViewModel.class);
        fabPlay = view.findViewById(R.id.fabPlay);

        routineDetailAdapter = new RoutineDetailAdapter(new ArrayList<>(), this);
        rvRoutineDetail.setAdapter(routineDetailAdapter);

        // Pass the Routine to the ViewModel to populate exercises
        if (routine != null) {
            mViewModel.setRoutine(routine);
        }

        mViewModel.getDataList().observe(getViewLifecycleOwner(), routineDetailList -> {
            routineDetailAdapter.updateDataList(routineDetailList);
        });

        fabPlay.setOnClickListener(v -> handleFabClick());

        return view;
    }

    private void handleFabClick() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get user ID
        List<RoutineExercise> exercises = mViewModel.getRoutineExercises();

        if (exercises.isEmpty()) {
            Toast.makeText(getContext(), "No exercises found in the routine.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create RoutineLog and handle success or failure
        mViewModel.createRoutineLog(uid).thenAccept(routineLogId -> {
            Log.d("RoutineDetailFragment", "RoutineLog created with ID: " + routineLogId);

            // TODO: Pass RoutineLogId and RoutineExercises

            Log.d("RoutineDetailFragment", "RoutineLogId: " + routineLogId);
            for (RoutineExercise exercise : exercises) {
                Log.d("RoutineDetailFragment", "Exercise: " + exercise.getExerciseName() +
                        ", Id: " + exercise.getExerciseId() +
                        ", Description: " + exercise.getExerciseDescription() +
                        ", Reps: " + exercise.getReps() +
                        ", Duration: " + exercise.getDuration());
            }

            // Navigate to the SelfAssessmentFragment
            SelfAssessmentFragment assessmentFragment = SelfAssessmentFragment.newInstance();

            // Perform the fragment transaction
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragmentContainer, assessmentFragment) // Replace with the correct container ID
                    .addToBackStack(null)
                    .commit();

        }).exceptionally(e -> {
            Log.e("RoutineDetailFragment", "Failed to create RoutineLog: " + e.getMessage());
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Use the title for setting the fragment’s title or displaying it in a TextView
        TextView tvRoutineTitle = view.findViewById(R.id.tvRoutineTitle);
        if (routineTitle != null) {
            routineTitle = routineTitle.replace("\n", " ");
            tvRoutineTitle.setText(routineTitle);
        }
    }

    @Override
    public void onExerciseClicked(RoutineExercise exercise) {
// Create the ExerciseFragment and pass the RoutineExercise object
        ExerciseFragment exerciseFragment = ExerciseFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("EXERCISE", exercise); // Pass the RoutineExercise object
        exerciseFragment.setArguments(bundle);

        // Navigate to the ExerciseFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragmentContainer, exerciseFragment) // Replace with the correct container ID
                .addToBackStack(null)
                .commit();
    }
}