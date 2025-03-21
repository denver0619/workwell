package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.Diagnosis;
import com.digiview.workwell.data.repository.DiagnosisRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiagnosisService {

    private final DiagnosisRepository repository;

    public DiagnosisService() {
        this.repository = new DiagnosisRepository();
    }

    /**
     * Fetch a diagnosis by its ID using CompletableFuture.
     *
     * @param diagnosisId The ID of the diagnosis to fetch.
     * @return A CompletableFuture containing the fetched diagnosis.
     */
    public CompletableFuture<Diagnosis> getDiagnosis(String diagnosisId) {
        return repository.getDiagnosis(diagnosisId);
    }

    public CompletableFuture<List<Diagnosis>> getAllDiagnosis() {
        return repository.getAllDiagnosis();
    }
}
