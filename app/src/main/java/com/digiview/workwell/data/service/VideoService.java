package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.Video;
import com.digiview.workwell.data.repository.VideoRepository;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.CompletableFuture;

public class VideoService {

    private final VideoRepository repository;

    public VideoService() {
        this.repository = new VideoRepository();
    }

    /**
     * Uploads a video file to Cloudinary and stores metadata in Firestore.
     *
     * @param videoFilePath The path of the video file.
     * @return CompletableFuture with Video object containing ID and URL.
     */
    public CompletableFuture<Video> uploadVideo(String videoFilePath) {
        return repository.uploadVideoToCloudinary(videoFilePath)
                .thenCompose(video -> repository.saveVideoMetadata(video));
    }
}
