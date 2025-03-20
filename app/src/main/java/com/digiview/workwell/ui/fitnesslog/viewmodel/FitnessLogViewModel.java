package com.digiview.workwell.ui.fitnesslog.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.RoutineLogs;
import com.digiview.workwell.data.service.RoutineLogService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FitnessLogViewModel extends ViewModel {

    private final MutableLiveData<List<RoutineLogs>> routineLogsLiveData = new MutableLiveData<>();
    private final RoutineLogService routineLogService;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public FitnessLogViewModel() {
        routineLogService = new RoutineLogService();
        fetchRoutineLogs();
    }

    public LiveData<List<RoutineLogs>> getRoutineLogsLiveData() {
        return routineLogsLiveData;
    }

    private void fetchRoutineLogs() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        executor.execute(() -> {
            routineLogService.fetchRoutineLogsWithDetails(currentUserId)
                    .thenAccept(routineLogs -> {
                        if (routineLogs != null) {
                            // Sort by CreatedAt (descending order: newest first)
                            Collections.sort(routineLogs, (log1, log2) -> {
                                Date date1 = log1.getCreatedAt();
                                Date date2 = log2.getCreatedAt();
                                if (date1 == null || date2 == null) {
                                    return 0; // Handle null dates gracefully
                                }
                                return date2.compareTo(date1); // Descending order
                            });
                        }

                        Log.d("FitnessLogViewModel", "Fetched RoutineLogs: " + routineLogs);
                        routineLogsLiveData.postValue(routineLogs);
                    }).exceptionally(e -> {
                        Log.e("FitnessLogViewModel", "Error fetching routine logs: ", e);
                        return null;
                    });
        });
    }

}
