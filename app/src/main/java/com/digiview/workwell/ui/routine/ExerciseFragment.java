package com.digiview.workwell.ui.routine;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExercise;
import com.digiview.workwell.data.models.Video;
import com.digiview.workwell.data.service.VideoService;

public class ExerciseFragment extends Fragment {

    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private ImageView progressBar;
    private static final String TAG = "ExerciseFragment";

    public static ExerciseFragment newInstance() {
        return new ExerciseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Views
        playerView = view.findViewById(R.id.playerView);
        progressBar = view.findViewById(R.id.progressBar);
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        TextView tvExerciseTitle = view.findViewById(R.id.tvExerciseTitle);
        TextView tvExerciseDetail = view.findViewById(R.id.tvExerciseDetail);
        TextView tvExerciseDeviceSetup = view.findViewById(R.id.tvExerciseDeviceSetup);

        // Load GIF into ImageView using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.ic_loading) // Replace with your actual GIF in drawable
                .into(progressBar);

        // Retrieve RoutineExercise from arguments
        RoutineExercise exercise = null;
        if (getArguments() != null) {
            exercise = (RoutineExercise) getArguments().getSerializable("EXERCISE");
        }

        if (exercise != null) {
            tvExerciseTitle.setText(exercise.getExerciseName());
            tvExerciseDetail.setText(exercise.getExerciseDescription());
            tvExerciseDeviceSetup.setText(exercise.getExerciseDeviceSetup());

            // Load video if VideoId exists
            if (exercise.getVideoId() != null && !exercise.getVideoId().isEmpty()) {
                fetchVideoUrl(exercise.getVideoId());
            } else {
                Log.w(TAG, "No VideoId provided.");
            }
        }

        // Back button
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void fetchVideoUrl(String videoId) {
        progressBar.setVisibility(View.VISIBLE);

        VideoService videoService = new VideoService();
        videoService.getVideo(videoId).thenAccept(video -> {
            if (video != null && video.getVideoUrl() != null) {
                initializePlayer(video.getVideoUrl());
            } else {
                Log.e(TAG, "Video not found for ID: " + videoId);
                progressBar.setVisibility(View.GONE);
            }
        }).exceptionally(e -> {
            Log.e(TAG, "Failed to load video: " + e.getMessage(), e);
            progressBar.setVisibility(View.GONE);
            return null;
        });
    }

    private void initializePlayer(String videoUrl) {
        exoPlayer = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        // Hide loading when ready
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == ExoPlayer.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                } else if (state == ExoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
