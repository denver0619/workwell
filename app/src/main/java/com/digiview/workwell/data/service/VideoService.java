package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.Video;
import com.digiview.workwell.data.repository.VideoRepository;
import com.digiview.workwell.data.util.AuthHelper;

import java.util.concurrent.CompletableFuture;

public class VideoService {

    private final VideoRepository repository;

    public VideoService() {
        this.repository = new VideoRepository();
    }

    /**
     * Uploads a video file to Cloudinary and stores metadata in Firestore,
     * ensuring OrganizationId is included in the Video object.
     *
     * @param videoFilePath The path of the video file.
     * @return CompletableFuture with Video object containing ID and URL.
     */
    public CompletableFuture<Video> uploadVideo(String videoFilePath) {
        return AuthHelper.getOrganizationIdFromToken()
                .thenCompose(organizationId ->
                        repository.uploadVideoToCloudinary(videoFilePath)
                                .thenCompose(video -> {
                                    // Set OrganizationId in the Video object
                                    video.setOrganizationId(organizationId);
                                    // Save video metadata in Firestore
                                    return repository.saveVideoMetadata(video);
                                })
                );
    }

    /**
     * Fetches a video by its ID from Firestore.
     *
     * @param videoId The ID of the video to retrieve.
     * @return CompletableFuture containing the Video object.
     */
    public CompletableFuture<Video> getVideo(String videoId) {
        return repository.getVideo(videoId);
    }
}
