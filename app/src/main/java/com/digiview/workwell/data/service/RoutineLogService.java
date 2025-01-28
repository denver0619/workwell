package com.digiview.workwell.data.service;

import android.util.Log;

import com.digiview.workwell.data.models.RoutineLogs;
import com.digiview.workwell.data.repository.RoutineLogRepository;
import com.digiview.workwell.data.util.AuthHelper;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoutineLogService {

    private final RoutineLogRepository repository;
    private final SelfAssessmentService selfAssessmentService;
    private final VideoService videoService;
    private final JournalService journalService;

    public RoutineLogService() {
        this.repository = new RoutineLogRepository();
        selfAssessmentService = new SelfAssessmentService();
        videoService = new VideoService();
        journalService = new JournalService();
    }

    /**
     * Create a RoutineLog in Firestore.
     *
     * @param routineId        The ID of the routine being logged.
     * @param routineLogName   The name of the routine log.
     * @param uid              The UID of the user creating the log.
     * @return CompletableFuture containing the generated RoutineLogId.
     */
    public CompletableFuture<String> createRoutineLog(String routineId, String routineLogName, String uid) {
        return AuthHelper.getOrganizationIdFromToken()
                .thenCompose(organizationId -> {
                    // Create a RoutineLog with the retrieved OrganizationId
                    RoutineLogs routineLog = new RoutineLogs();
                    routineLog.setRoutineId(routineId);
                    routineLog.setRoutineLogName(routineLogName);
                    routineLog.setUid(uid);
                    routineLog.setOrganizationId(organizationId); // Set OrganizationId
                    routineLog.setCreatedAt(null);
                    routineLog.setSelfAssessmentId(null);
                    routineLog.setVideoId(null);
                    routineLog.setJournalId(null);

                    return repository.createRoutineLog(routineLog);
                });
    }

    public Task<Void> updateRoutineLogSelfAssessment(String routineLogId, String selfAssessmentId) {
        return repository.updateRoutineLogField(routineLogId, "SelfAssessmentId", selfAssessmentId);
    }
    public Task<Void> updateRoutineLogVideoId(String routineLogId, String videoId) {
        return repository.updateVideoIdField(routineLogId, videoId);
    }
    public Task<Void> updateRoutineLogJournalId(String routineLogId, String journalId) {
        return repository.updateJournalIdField(routineLogId, journalId);
    }

    public CompletableFuture<List<RoutineLogs>> fetchRoutineLogsWithDetails(String currentUserId) {
        return repository.fetchRoutineLogsForCurrentUser(currentUserId)
                .thenCompose(routineLogs -> {
                    CompletableFuture<Void> allDetailsFetched = CompletableFuture.allOf(
                            routineLogs.stream().map(log -> {
                                CompletableFuture<Void> selfAssessmentFuture = log.getSelfAssessmentId() != null
                                        ? selfAssessmentService.getSelfAssessment(log.getSelfAssessmentId())
                                        .thenAccept(log::setSelfAssessment)
                                        .exceptionally(e -> {
                                            Log.e("RoutineLogService", "Error fetching SelfAssessment: " + log.getSelfAssessmentId(), e);
                                            return null;
                                        })
                                        : CompletableFuture.completedFuture(null);

                                CompletableFuture<Void> videoFuture = log.getVideoId() != null
                                        ? videoService.getVideo(log.getVideoId())
                                        .thenAccept(log::setVideo)
                                        .exceptionally(e -> {
                                            Log.e("RoutineLogService", "Error fetching Video: " + log.getVideoId(), e);
                                            return null;
                                        })
                                        : CompletableFuture.completedFuture(null);

                                CompletableFuture<Void> journalFuture = log.getJournalId() != null
                                        ? journalService.getJournal(log.getJournalId())
                                        .thenAccept(log::setJournal)
                                        .exceptionally(e -> {
                                            Log.e("RoutineLogService", "Error fetching Journal: " + log.getJournalId(), e);
                                            return null;
                                        })
                                        : CompletableFuture.completedFuture(null);

                                return CompletableFuture.allOf(selfAssessmentFuture, videoFuture, journalFuture);
                            }).toArray(CompletableFuture[]::new)
                    );

                    return allDetailsFetched.thenApply(ignored -> routineLogs);
                });
    }





}
