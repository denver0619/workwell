package com.digiview.workwell.ui.routine.execution;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.digiview.workwell.R;
import com.digiview.workwell.data.service.RoutineLogService;
import com.digiview.workwell.data.service.VideoService;
import com.digiview.workwell.databinding.FragmentVideoConvertBinding;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class VideoConvertFragment extends Fragment {

    private VideoConvertViewModel mViewModel;
    private RoutineLooperViewModel routineViewModel;
    private CameraViewModel cameraViewModel;
    private FragmentVideoConvertBinding fragmentVideoConvertBinding;

    public static VideoConvertFragment newInstance() {
        return new VideoConvertFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentVideoConvertBinding = FragmentVideoConvertBinding.inflate(inflater, container, false);
        return fragmentVideoConvertBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VideoConvertViewModel.class);
        routineViewModel = new ViewModelProvider(requireActivity()).get(RoutineLooperViewModel.class);
        cameraViewModel = new ViewModelProvider(requireActivity()).get(CameraViewModel.class);

        File saveDirectory = cameraViewModel.getSaveDirectory();
        String fitnessLogID = cameraViewModel.getFitnessLogID();

        // Observe conversion success
        mViewModel.getIsConvertSuccess().observe(getViewLifecycleOwner(), isConvertSuccess -> {
            if (isConvertSuccess) {
                Toast.makeText(requireContext(), "Conversion Successful", Toast.LENGTH_SHORT).show();

                File convertedVideo = new File(saveDirectory, fitnessLogID + ".mp4");

                if (convertedVideo.exists()) {
                    Log.d("Video Path", "Converted video path: " + convertedVideo.getAbsolutePath());
                    uploadVideoAndStoreMetadata(convertedVideo, fitnessLogID);
//                    clearCache(saveDirectory);
                } else {
                    Log.e("VideoUpload", "Converted video file does not exist.");
                }


                routineViewModel.setIsRoutineFinished(true);
            } else {
                Toast.makeText(requireContext(), "Conversion Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Start the video conversion
        convertToVideo(saveDirectory, fitnessLogID);
    }



    private void convertToVideo(File rootDir, String fitnessLogID) {
        String inputPath = new File(rootDir, sortImagesAndSaveToText(rootDir)).getAbsolutePath();
        File outputFile = new File(rootDir, fitnessLogID + ".mp4");
        String outputPath = outputFile.getAbsolutePath();

        // FFmpeg command to convert images to video
        //with rotation
//        String command = "-f concat -safe 0 -i \"" + inputPath + "\" -vf \"fps=15,format=yuv420p,transpose=1\" \"" + outputPath + "\"";
        //without rotation
        String command = "-f concat -safe 0 -i \"" + inputPath + "\" -vf \"fps=15,format=yuv420p\" \"" + outputPath + "\"";
        //-f concat -safe 0 -i "images.txt" -vf "fps=15,format=yuv420p" "aaaaa.mp4"


        // Execute the FFmpeg command asynchronously
        FFmpegKit.executeAsync(command, session -> {
            if (session.getReturnCode().isSuccess()) {
                Log.d("FFmpeg", "Video created successfully!");
                mViewModel.setIsConvertSuccess(true);
                mViewModel.setOutputVideoFile(outputFile); // Store the output file in ViewModel
            } else {
                Log.e("FFmpeg", "Failed to create video: " + session.getOutput());
                mViewModel.setIsConvertSuccess(false);
            }
        });
//        FFmpegKit.execute(command);
//        mViewModel.setIsConvertSuccess(true);
    }
//    private void checkIfFileExist(File outputFile) {
//        if (outputFile.exists()) {
//            mViewModel.setIsConvertSuccess(true);
//        } else {
//            mViewModel.setIsConvertSuccess(false);
//        }
//
//    }

    private String sortImagesAndSaveToText(File directory) {
        String outputFileName = "images.txt";
        File[] imageFiles = directory.listFiles((dir, name) -> name.startsWith("image_") && name.endsWith(".jpeg"));

        if (imageFiles != null) {
            // Sort the image files based on the numeric value in the filename (timestamp)
            Arrays.sort(imageFiles, Comparator.comparingLong(file -> Long.parseLong(file.getName().substring(6, file.getName().length() - 5))));

            // Create a list file (images.txt)
            File listFile = new File(directory, outputFileName);
            try (FileWriter writer = new FileWriter(listFile)) {
                for (File imageFile : imageFiles) {
                    writer.write("file '" + imageFile.getAbsolutePath() + "'\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputFileName;
    }

    private void uploadVideoAndStoreMetadata(File videoFile, String routineLogId) {
        VideoService videoService = new VideoService();

        videoService.uploadVideo(videoFile.getAbsolutePath()).thenAccept(video -> {
            // Update RoutineLog with VideoId
            RoutineLogService routineLogService = new RoutineLogService();
            routineLogService.updateRoutineLogVideoId(routineLogId, video.getVideoId())
                    .addOnSuccessListener(unused -> {
                        Log.d("VideoUpload", "VideoId successfully updated in RoutineLog!");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("VideoUpload", "Failed to update VideoId in RoutineLog.", e);
                    });
        }).exceptionally(e -> {
            Log.e("VideoUpload", "Failed to upload video or save metadata.", e);
            return null;
        });
    }


    private void clearCache(File cacheDir) {
        if (cacheDir != null && cacheDir.isDirectory()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            cacheDir.delete();
        }
    }

}