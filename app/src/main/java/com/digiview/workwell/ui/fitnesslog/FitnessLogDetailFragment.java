package com.digiview.workwell.ui.fitnesslog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.Journal;
import com.digiview.workwell.data.models.RoutineLogs;
import com.digiview.workwell.data.models.SelfAssessment;
import com.digiview.workwell.data.service.JournalService;
import com.digiview.workwell.data.service.RoutineLogService;
import com.digiview.workwell.ui.fitnesslog.adapter.FitnessLogDetailAdapter;
import com.digiview.workwell.ui.fitnesslog.viewmodel.FitnessLogDetailViewModel;

public class FitnessLogDetailFragment extends Fragment {

    private RoutineLogs routineLog;
    private JournalService journalService;
    private RoutineLogService routineLogService;
    private FitnessLogDetailViewModel viewModel;
    private FitnessLogDetailAdapter adapter;

    private TextView tvExerciseTitle;
    private TextView tvReflectionContent;
    private EditText etReflectionContentInput;
    private ImageButton btnBack;
    private Button btnSubmit;
    private RecyclerView rvExerciseList;

//    private TextView tvStiffnessLogValue;
//    private TextView tvPainLogValue;
//    private TextView tvDifficultyLogValue;
//    private TextView tvAwarenessLogValue;

    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private ProgressBar loadingIndicator;
    private TextView tvDoctorComment, tvDoctorCommentTitle;

    private static final String TAG = "FitnessLogDetailFragment";

    public static FitnessLogDetailFragment newInstance() {
        return new FitnessLogDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fitness_log_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(FitnessLogDetailViewModel.class);
        // Initialize views
        tvExerciseTitle = view.findViewById(R.id.tvExerciseTitle);
        tvReflectionContent = view.findViewById(R.id.tvReflectionContent);
        etReflectionContentInput = view.findViewById(R.id.etReflectionContentInput);
        btnBack = view.findViewById(R.id.btnBack);
        btnSubmit = view.findViewById(R.id.btnSubmit);
//        tvStiffnessLogValue = view.findViewById(R.id.tvStiffnessLogValue);
//        tvPainLogValue = view.findViewById(R.id.tvPainLogValue);
//        tvDifficultyLogValue = view.findViewById(R.id.tvDifficultyLogValue);
//        tvAwarenessLogValue = view.findViewById(R.id.tvAwarenessLogValue);
        playerView = view.findViewById(R.id.playerView);
        loadingIndicator = view.findViewById(R.id.loadingIndicator);
        tvDoctorComment = view.findViewById(R.id.tvDoctorComment);
        tvDoctorCommentTitle = view.findViewById(R.id.tvDoctorCommentTitle);

        // Initialize RecyclerView
        rvExerciseList = view.findViewById(R.id.rvExerciseList);
        rvExerciseList.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize services
        journalService = new JournalService();
        routineLogService = new RoutineLogService();

        // Get the passed RoutineLog from the bundle
        if (getArguments() != null) {
            routineLog = (RoutineLogs) getArguments().getSerializable("selectedRoutineLog");

            if (routineLog != null) {
                Log.d(TAG, "Selected RoutineLog: " + routineLog.toString());
                populateDetails();
            } else {
                Log.e(TAG, "RoutineLog is null!");
            }
        } else {
            Log.e(TAG, "No arguments provided!");
        }

        viewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
            Log.d("FitnessLogDetailFragment", "Exercises observed: " + exercises.size());
            if (exercises.isEmpty()) {
                Log.w("FitnessLogDetailFragment", "No exercises to display.");
                rvExerciseList.setVisibility(View.GONE);  // Optional
            } else {
                rvExerciseList.setVisibility(View.VISIBLE);
                if (adapter == null) {
                    adapter = new FitnessLogDetailAdapter(exercises);
                    rvExerciseList.setAdapter(adapter);
                } else {
                    adapter.updateExercises(exercises);
                }
            }
        });


        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        Log.d("RoutineId", routineLog.getRoutineId());
        // Fetch exercises
        if (routineLog != null) {
            viewModel.fetchRoutineExercises(routineLog.getRoutineId());
        }


        // Back button click listener
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        // Submit button click listener
        btnSubmit.setOnClickListener(v -> handleJournalSubmission());
    }

    private void populateDetails() {
        // Populate RoutineLog details
        tvExerciseTitle.setText(routineLog.getRoutineLogName());

        // Populate self-assessment values if available
        if (routineLog.getSelfAssessment() != null) {
            populateSelfAssessmentValues(routineLog.getSelfAssessment());
        } else {
            Log.w(TAG, "No SelfAssessment data available.");
        }

        // Handle journal content
        if (routineLog.getJournal() != null) {
            // Display existing journal content
            tvReflectionContent.setText(routineLog.getJournal().getContent());
            tvReflectionContent.setVisibility(View.VISIBLE);
            etReflectionContentInput.setVisibility(View.GONE); // Hide input field
            btnSubmit.setVisibility(View.GONE); // Hide Submit button since journal exists
        } else {
            // Allow user to input new journal
            tvReflectionContent.setVisibility(View.GONE);
            etReflectionContentInput.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE); // Show Submit button
        }

        // ✅ Display doctor's comment if available
        if (routineLog.getComment() != null && !routineLog.getComment().isEmpty()) {
            tvDoctorComment.setText(routineLog.getComment());
            tvDoctorComment.setVisibility(View.VISIBLE);
            tvDoctorCommentTitle.setVisibility(View.VISIBLE);
        } else {
            Log.w(TAG, "No doctor comment available.");
            tvDoctorComment.setVisibility(View.GONE);
            tvDoctorCommentTitle.setVisibility(View.GONE);
        }

        // Initialize ExoPlayer and load video
        if (routineLog.getVideo() != null && routineLog.getVideo().getVideoUrl() != null) {
            initializePlayer(routineLog.getVideo().getVideoUrl());
        } else {
            Log.w(TAG, "No video URL found in RoutineLog.");
        }
    }



    private void populateSelfAssessmentValues(SelfAssessment selfAssessment) {
        // Example for Stiffness
        setRatingIcons(R.id.mcvStiffness, selfAssessment.getStiffness());

        // Similarly, for Pain, Difficulty, and Awareness
        setRatingIcons(R.id.mcvPain, selfAssessment.getPain());
        setRatingIcons(R.id.mcvDifficulty, selfAssessment.getDifficulty());
        setRatingIcons(R.id.mcvAwareness, selfAssessment.getAwareness());
    }

    private void setRatingIcons(int cardViewId, int value) {
        View cardView = getView().findViewById(cardViewId);
        if (cardView != null) {
            // Find the LinearLayout inside the MaterialCardView
            ViewGroup linearLayout = (ViewGroup) ((ViewGroup) cardView).getChildAt(0);

            if (linearLayout != null) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View child = linearLayout.getChildAt(i);
                    if (child instanceof ImageView) {
                        ImageView imageView = (ImageView) child;
                        if (i < value) {
                            imageView.setImageResource(R.drawable.ic_ribs_green);  // Filled icon
                        } else {
                            imageView.setImageResource(R.drawable.ic_ribs_white);  // Empty icon
                        }
                    }
                }
            } else {
                Log.e(TAG, "LinearLayout not found inside the card view with ID: " + cardViewId);
            }
        } else {
            Log.e(TAG, "CardView not found with ID: " + cardViewId);
        }
    }



    private void initializePlayer(String videoUrl) {
        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);

        // Create a MediaItem from the URL and set it to the player
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        exoPlayer.setMediaItem(mediaItem);

        // Prepare and start playback
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void handleJournalSubmission() {
        String journalContent = etReflectionContentInput.getText().toString().trim();
        if (journalContent.isEmpty()) {
            Log.e(TAG, "Journal content cannot be empty");
            return;
        }

        // Create a new Journal
        Journal newJournal = new Journal();
        newJournal.setContent(journalContent);

        journalService.createJournal(newJournal).thenAccept(aVoid -> {
            Log.d(TAG, "Journal created successfully with ID: " + newJournal.getJournalId());

            // Update RoutineLog with the new JournalId
            routineLogService.updateRoutineLogJournalId(routineLog.getRoutineLogId(), newJournal.getJournalId())
                    .addOnSuccessListener(aVoid2 -> {
                        Log.d(TAG, "RoutineLog updated successfully with new JournalId");

                        // Update RoutineLog object locally
                        routineLog.setJournal(newJournal);

                        // Refresh UI
                        getActivity().runOnUiThread(this::populateDetails);
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to update RoutineLog with JournalId", e));
        }).exceptionally(e -> {
            Log.e(TAG, "Failed to create Journal", e);
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release ExoPlayer resources
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}








