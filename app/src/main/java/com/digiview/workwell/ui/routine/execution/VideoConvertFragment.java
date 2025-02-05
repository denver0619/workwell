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
import com.digiview.workwell.services.mediapipe.TTSInitializationListener;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

public class VideoConvertFragment extends Fragment {

    private VideoConvertViewModel mViewModel;
    private RoutineLooperViewModel routineViewModel;
    private CameraViewModel cameraViewModel;
    private FragmentVideoConvertBinding fragmentVideoConvertBinding;
    private String tempVideoId;

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

        // Generate a unique temporary ID for the video
        tempVideoId = UUID.randomUUID().toString();
        Log.d("VideoConvertFragment", "Generated Temporary Video ID: " + tempVideoId);

        File saveDirectory = cameraViewModel.getSaveDirectory();

        // Start the video conversion with the temporary ID
        mViewModel.setTtsHelper(requireContext(),
                new TTSInitializationListener() {
                    @Override
                    public void onTTSInitialized() {
                        Objects.requireNonNull(mViewModel.getTtsHelper().getValue()).speak("Routine is finished, processing the video.");
                        convertToVideo(saveDirectory, tempVideoId);
                    }
                }
        );
    }



    private void convertToVideo(File rootDir, String tempVideoId) {
        String inputPath = new File(rootDir, sortImagesAndSaveToText(rootDir)).getAbsolutePath();
        File outputFile = new File(rootDir, tempVideoId + ".mp4"); // Use temp ID
        String outputPath = outputFile.getAbsolutePath();

        // FFmpeg command to convert images to video
        //with rotation
//        String command = "-f concat -safe 0 -i \"" + inputPath + "\" -vf \"fps=15,format=yuv420p,transpose=1\" \"" + outputPath + "\"";
        //without rotation

        //without duration in images.txt
        //ffmpeg -f concat -safe 0 -i "images.txt" -vf "fps=15,format=yuv420p" "aaaaa.mp4"

//        String command = "-f concat -safe 0 -i \"" + inputPath + "\" -vf \"fps=15,format=yuv420p\" \"" + outputPath + "\"";

        //with durationin images.txt
        //ffmpeg -f concat -safe 0 -i images.txt -vf "format=yuv420p" "output.mp4"

        String command = "-f concat -safe 0 -i \"" + inputPath + "\" -vf \"format=yuv420p\" \"" + outputPath + "\"";

        FFmpegKit.executeAsync(command, session -> {
            if (session.getReturnCode().isSuccess()) {
                Log.d("FFmpeg", "Video created successfully with Temp ID: " + tempVideoId);
                mViewModel.setIsConvertSuccess(true);
                mViewModel.setOutputVideoFile(outputFile);

                // Upload the video
                uploadVideo(outputFile);
            } else {
                Log.e("FFmpeg", "Failed to create video.");
                mViewModel.setIsConvertSuccess(false);
            }
        });
    }


//    private void checkIfFileExist(File outputFile) {
//        if (outputFile.exists()) {
//            mViewModel.setIsConvertSuccess(true);
//        } else {
//            mViewModel.setIsConvertSuccess(false);
//        }
//
//    }

//    private String sortImagesAndSaveToText(File directory) {
//        String outputFileName = "images.txt";
//        File[] imageFiles = directory.listFiles((dir, name) -> name.startsWith("image_") && name.endsWith(".jpeg"));
//
//        if (imageFiles != null) {
//            // Sort the image files based on the numeric value in the filename (timestamp)
//            Arrays.sort(imageFiles, Comparator.comparingLong(file -> Long.parseLong(file.getName().substring(6, file.getName().length() - 5))));
//
//            // Create a list file (images.txt)
//            File listFile = new File(directory, outputFileName);
//            try (FileWriter writer = new FileWriter(listFile)) {
//                for (File imageFile : imageFiles) {
//                    writer.write("file '" + imageFile.getAbsolutePath() + "'\n");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return outputFileName;
//    }

    private String sortImagesAndSaveToText(File directory) {
        String outputFileName = "images.txt";
        File[] imageFiles = directory.listFiles((dir, name) -> name.startsWith("image_") && name.endsWith(".jpeg"));

        if (imageFiles != null) {
            // Sort the image files based on the numeric value in the filename (timestamp)
            Arrays.sort(imageFiles, Comparator.comparingLong(file ->
                    Long.parseLong(file.getName().substring(6, file.getName().length() - 5))
            ));

            // Create the images.txt file
            File listFile = new File(directory, outputFileName);
            try (FileWriter writer = new FileWriter(listFile)) {
                for (int i = 0; i < imageFiles.length; i++) {
                    File imageFile = imageFiles[i];
                    writer.write("file '" + imageFile.getAbsolutePath() + "'\n");

                    // Calculate and write the duration for all except the last file
                    if (i < imageFiles.length - 1) {
                        long currentTimestamp = Long.parseLong(imageFile.getName().substring(6, imageFile.getName().length() - 5));
                        long nextTimestamp = Long.parseLong(imageFiles[i + 1].getName().substring(6, imageFiles[i + 1].getName().length() - 5));
                        double duration = (nextTimestamp - currentTimestamp) / 1000.0; // Convert milliseconds to seconds
                        writer.write(String.format("duration %.3f\n", duration));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return outputFileName;
    }

    private void uploadVideo(File videoFile) {
        VideoService videoService = new VideoService();

        videoService.uploadVideo(videoFile.getAbsolutePath()).thenAccept(video -> {
            String realVideoId = video.getVideoId(); // Get real video ID from upload
            Log.d("VideoConvertFragment", "Video uploaded with ID: " + realVideoId);

            // Notify RoutineActivity with the uploaded video ID
            requireActivity().runOnUiThread(() -> {
                if (getActivity() instanceof RoutineActivity) {
                    ((RoutineActivity) getActivity()).setVideoId(realVideoId);
                } else {
                    Log.e("VideoConvertFragment", "Activity is not RoutineActivity");
                }
            });

            // ✅ Set isRoutineFinished to true to trigger navigation to MainActivity
            routineViewModel.setIsRoutineFinished(true);

        }).exceptionally(e -> {
            Log.e("VideoUpload", "Failed to upload video.", e);
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