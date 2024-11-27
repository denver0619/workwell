package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.SelfAssessment;
import com.digiview.workwell.data.repository.SelfAssessmentRepository;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.CompletableFuture;

public class SelfAssessmentService {

    private final SelfAssessmentRepository repository;

    public SelfAssessmentService() {
        repository = new SelfAssessmentRepository();
    }

    // Add a self-assessment using the repository
    public Task<Void> addSelfAssessment(SelfAssessment selfAssessment) {
        // Perform any additional logic here if needed
        return repository.addSelfAssessment(selfAssessment);
    }

    public CompletableFuture<SelfAssessment> getSelfAssessment(String selfAssessmentId) {
        return repository.getSelfAssessment(selfAssessmentId);
    }


    // Update a self-assessment
    public Task<Void> updateSelfAssessment(SelfAssessment selfAssessment) {
        return repository.updateSelfAssessment(selfAssessment);
    }

    // Delete a self-assessment by ID
    public Task<Void> deleteSelfAssessment(String selfAssessmentId) {
        return repository.deleteSelfAssessment(selfAssessmentId);
    }
}
