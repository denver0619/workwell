package com.digiview.workwell.data.service;

import com.digiview.workwell.data.models.Routine;
import com.digiview.workwell.data.repository.RoutineRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RoutineService {
    private final RoutineRepository routineRepository;

    public RoutineService() {
        this.routineRepository = new RoutineRepository();
    }

    // Orchestrate fetching user routines
    public CompletableFuture<List<Routine>> getEnrichedRoutinesForUser() {
        return routineRepository.getUserRoutinesWithDetails();
    }

    public CompletableFuture<Routine> getRoutineById(String routineId) {
        return routineRepository.fetchRoutineById(routineId);
    }
}
