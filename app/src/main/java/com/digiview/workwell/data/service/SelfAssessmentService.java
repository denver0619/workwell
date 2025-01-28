package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.SelfAssessment;
import com.digiview.workwell.data.repository.SelfAssessmentRepository;
import com.digiview.workwell.data.util.AuthHelper;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.CompletableFuture;

public class SelfAssessmentService {

    private final SelfAssessmentRepository repository;

    public SelfAssessmentService() {
        repository = new SelfAssessmentRepository();
    }

    /**
     * Add a self-assessment, ensuring OrganizationId is set dynamically.
     *
     * @param selfAssessment The SelfAssessment object to add.
     * @return A Task representing the operation.
     */
    public CompletableFuture<Void> addSelfAssessment(SelfAssessment selfAssessment) {
        return AuthHelper.getOrganizationIdFromToken()
                .thenCompose(organizationId -> {
                    // Set OrganizationId in the SelfAssessment object
                    selfAssessment.setOrganizationId(organizationId);

                    // Add SelfAssessment using the repository
                    return CompletableFuture.supplyAsync(() -> {
                        repository.addSelfAssessment(selfAssessment);
                        return null;
                    });
                });
    }

    public CompletableFuture<SelfAssessment> getSelfAssessment(String selfAssessmentId) {
        return repository.getSelfAssessment(selfAssessmentId);
    }

    /**
     * Update a self-assessment, ensuring OrganizationId is included.
     *
     * @param selfAssessment The SelfAssessment object to update.
     * @return A Task representing the operation.
     */
    public CompletableFuture<Void> updateSelfAssessment(SelfAssessment selfAssessment) {
        return AuthHelper.getOrganizationIdFromToken()
                .thenCompose(organizationId -> {
                    // Ensure the OrganizationId is set before updating
                    selfAssessment.setOrganizationId(organizationId);

                    return CompletableFuture.supplyAsync(() -> {
                        repository.updateSelfAssessment(selfAssessment);
                        return null;
                    });
                });
    }

    /**
     * Delete a self-assessment by ID.
     *
     * @param selfAssessmentId The ID of the SelfAssessment to delete.
     * @return A Task representing the operation.
     */
    public Task<Void> deleteSelfAssessment(String selfAssessmentId) {
        return repository.deleteSelfAssessment(selfAssessmentId);
    }
}
