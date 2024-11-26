package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.RoutineLogs;
import com.digiview.workwell.data.repository.RoutineLogRepository;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.CompletableFuture;

public class RoutineLogService {

    private final RoutineLogRepository repository;

    public RoutineLogService() {
        this.repository = new RoutineLogRepository();
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
        RoutineLogs routineLog = new RoutineLogs();
        routineLog.setRoutineId(routineId);
        routineLog.setRoutineLogName(routineLogName);
        routineLog.setUid(uid);
        routineLog.setCreatedAt(null); // Firestore will auto-set this
        routineLog.setSelfAssessmentId(null); // Placeholder
        routineLog.setVideoId(null);          // Placeholder
        routineLog.setJournalId(null);

        return repository.createRoutineLog(routineLog);
    }

    public Task<Void> updateRoutineLogSelfAssessment(String routineLogId, String selfAssessmentId) {
        return repository.updateRoutineLogField(routineLogId, "SelfAssessmentId", selfAssessmentId);
    }
    public Task<Void> updateRoutineLogVideoId(String routineLogId, String videoId) {
        return repository.updateVideoIdField(routineLogId, videoId);
    }



}
