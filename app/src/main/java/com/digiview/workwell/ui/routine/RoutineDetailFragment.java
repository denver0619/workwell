package com.digiview.workwell.ui.routine;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import com.digiview.workwell.data.models.Exercise;
import com.digiview.workwell.data.models.Keypoint;
import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.data.models.RoutineExerciseDetailDTO;
import com.digiview.workwell.data.service.RoutineExerciseService;
import com.digiview.workwell.ui.main.MainActivity;
import com.digiview.workwell.ui.routine.adapter.RoutineDetailAdapter;
import com.digiview.workwell.ui.routine.execution.RoutineActivity;
import com.digiview.workwell.ui.routine.viewmodel.RoutineDetailViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<RoutineExercise> exercises = mViewModel.getRoutineExercises();

        if (exercises.isEmpty()) {
            Toast.makeText(getContext(), "No exercises found in the routine.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the service
        RoutineExerciseService routineExerciseService = new RoutineExerciseService();

        // We can fetch the detail DTOs for each routine exercise
        List<Task<RoutineExerciseDetailDTO>> detailTasks = new ArrayList<>();
        for (RoutineExercise re : exercises) {
            Task<RoutineExerciseDetailDTO> detailTask = routineExerciseService.getRoutineExerciseDetail(re);
            detailTasks.add(detailTask);
        }

        // Wait until all the detail objects are ready
        Tasks.whenAllSuccess(detailTasks)
                .addOnSuccessListener(results -> {
                    @SuppressWarnings("unchecked")
                    List<RoutineExerciseDetailDTO> detailDTOs = (List<RoutineExerciseDetailDTO>) (List<?>) results;

                    // ────────────────────────────────────────────────────────────────
                    Log.d("RoutineDetailFragment", "====== Routine Exercise Details (START) ======");
                    for (RoutineExerciseDetailDTO dto : detailDTOs) {

                        Log.d("RoutineDetailFragment", "Exercise ID: " + dto.getExerciseId());
                        Log.d("RoutineDetailFragment", "Reps: " + dto.getReps());
                        Log.d("RoutineDetailFragment", "Duration: " + dto.getDuration());
                        Log.d("RoutineDetailFragment", "Exercise Name: " + dto.getExerciseName());
                        Log.d("RoutineDetailFragment", "Exercise Description: " + dto.getExerciseDescription());

                        // Log the embedded Exercise document details
                        Exercise e = dto.getExercise();
                        if (e != null) {
                            Log.d("RoutineDetailFragment", "  [Exercise Doc]");
                            Log.d("RoutineDetailFragment", "    ExerciseId: " + e.getExerciseId());
                            Log.d("RoutineDetailFragment", "    Name: " + e.getName());
                            Log.d("RoutineDetailFragment", "    Description: " + e.getDescription());
                            Log.d("RoutineDetailFragment", "    TargetArea: " + e.getTargetArea());
                            Log.d("RoutineDetailFragment", "    OrganizationId: " + e.getOrganizationId());
                        }
                        // Log the constraints and their keypoints
                        if (dto.getConstraints() != null) {
                            for (int i = 0; i < dto.getConstraints().size(); i++) {
                                RoutineExerciseDetailDTO.ConstraintDetailDTO constraint = dto.getConstraints().get(i);
                                Log.d("RoutineDetailFragment", "  [Constraint " + (i + 1) + "]");
                                Log.d("RoutineDetailFragment", "    ConstraintId: " + constraint.getConstraintId());
                                Log.d("RoutineDetailFragment", "    AlignedThreshold: " + constraint.getAlignedThreshold());
                                Log.d("RoutineDetailFragment", "    RestingThreshold: " + constraint.getRestingThreshold());
                                Log.d("RoutineDetailFragment", "    RestingComparator: " + constraint.getRestingComparator());
                                Log.d("RoutineDetailFragment", "    AlignedComparator: " + constraint.getAlignedComparator());

                                // Keypoints
                                List<Keypoint> keypoints = constraint.getKeypoints();
                                if (keypoints != null && !keypoints.isEmpty()) {
                                    for (int k = 0; k < keypoints.size(); k++) {
                                        Keypoint kp = keypoints.get(k);
                                        Log.d("RoutineDetailFragment", "      [Keypoint " + (k + 1) + "]");
                                        Log.d("RoutineDetailFragment", "        KeypointId: " + kp.getKeypointId());
                                        Log.d("RoutineDetailFragment", "        Keypoint: " + kp.getKeypoint());
                                        Log.d("RoutineDetailFragment", "        SecondaryKeypoint: " + kp.getSecondaryKeypoint());
                                        Log.d("RoutineDetailFragment", "        isMidpoint: " + kp.isMidpoint());
                                    }
                                } else {
                                    Log.d("RoutineDetailFragment", "      No keypoints found.");
                                }
                            }
                        } else {
                            Log.d("RoutineDetailFragment", "  No constraints found.");
                        }
                        Log.d("RoutineDetailFragment", "────────────────────────────────────────────");
                    }
                    Log.d("RoutineDetailFragment", "====== Routine Exercise Details (END) ======");

                    // ────────────────────────────────────────────────────────────────


                    // Now you have a list of fully hydrated DTOs
                    // Pass them to your next activity, or do whatever you need:
                    String routineId = mViewModel.getRoutine().getRoutineId();
                    String routineName = mViewModel.getRoutine().getName();

                    ((MainActivity) requireActivity())
                            .startRoutineActivity(new ArrayList<>(detailDTOs), routineId, routineName);

                })
                .addOnFailureListener(e -> {
                    Log.e("RoutineDetailFragment", "Failed to load routine details: ", e);
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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