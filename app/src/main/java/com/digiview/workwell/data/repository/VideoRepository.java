package com.digiview.workwell.data.repository;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.digiview.workwell.data.models.Video;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VideoRepository {

    private final FirebaseFirestore firestore;
    private final Cloudinary cloudinary;

    public VideoRepository() {
        this.firestore = FirebaseFirestore.getInstance();
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmf3my3no",
                "api_key", "699114612782245",
                "api_secret", "y7HnXTn2M9hEzosFDzPpzy1ChWY"
        ));
    }

    /**
     * Uploads a video to Cloudinary.
     *
     * @param videoFilePath The path to the video file.
     * @return CompletableFuture with a Video object containing the Cloudinary ID and URL.
     */
    public CompletableFuture<Video> uploadVideoToCloudinary(String videoFilePath) {
        CompletableFuture<Video> future = new CompletableFuture<>();

        new Thread(() -> {
            try {
                // Use the upload preset
                Map<String, Object> options = ObjectUtils.asMap(
                        "upload_preset", "android_upload", // Replace with your actual preset name
                        "resource_type", "video" // Specify that this is a video upload
                );

                // Upload the video file
                Map uploadResult = cloudinary.uploader().upload(new File(videoFilePath), options);

                // Extract the Cloudinary ID and URL from the response
                String cloudinaryId = (String) uploadResult.get("public_id");
                String videoUrl = (String) uploadResult.get("url");

                // Create a Video object with the Cloudinary details
                Video video = new Video();
                video.setCloudinaryId(cloudinaryId); // Set the Cloudinary public_id
                video.setVideoUrl(videoUrl);

                future.complete(video); // Complete the future with the video details
            } catch (Exception e) {
                future.completeExceptionally(e); // Handle errors
            }
        }).start();

        return future;
    }


    /**
     * Saves video metadata to Firestore.
     *
     * @param video The Video object containing metadata.
     * @return CompletableFuture with the saved Video object.
     */
    public CompletableFuture<Video> saveVideoMetadata(Video video) {
        CompletableFuture<Video> future = new CompletableFuture<>();

        // Create a new Firestore document
        DocumentReference documentReference = firestore.collection("videos").document();
        video.setVideoId(documentReference.getId()); // Set the Firebase document ID

        // Save the video metadata to Firestore
        documentReference.set(video)
                .addOnSuccessListener(unused -> future.complete(video))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }


    /**
     * Updates the VideoId in a routine log.
     *
     * @param routineLogId The ID of the routine log to update.
     * @param videoId      The ID of the video to associate with the routine log.
     * @return Task representing the Firestore operation.
     */
    public Task<Void> updateRoutineLogVideoId(String routineLogId, String videoId) {
        return firestore.collection("routinelogs")
                .document(routineLogId)
                .update("VideoId", videoId);
    }
}
