package com.digiview.workwell.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.models.UserDTO;
import com.digiview.workwell.data.service.UserService;

public class HomeViewModel extends ViewModel {

    private final UserService userService;
    private final MutableLiveData<String> displayName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> height = new MutableLiveData<>();
    private final MutableLiveData<String> weight = new MutableLiveData<>();
    private final MutableLiveData<String> assignedProfessional = new MutableLiveData<>();
    private boolean isDataLoaded = false; // Flag to track if data is already fetched

    public HomeViewModel() {
        userService = new UserService();
    }

    public void fetchUserData() {
        if (isDataLoaded) return; // Prevent redundant fetching
        isLoading.setValue(true);

        // If cached values exist, use them immediately
        if (displayName.getValue() != null) {
            isLoading.setValue(false);
            return;
        }

        userService.getCompleteUserData().thenAccept(userDTO -> {
            if (userDTO != null) {
                displayName.postValue(userDTO.getName().trim().isEmpty() ? "Unknown User" : userDTO.getName());
                height.postValue(userDTO.getHeight() != 0 ? String.format("%.2f cm", userDTO.getHeight()) : "N/A");
                weight.postValue(userDTO.getWeight() != 0 ? String.format("%.2f kg", userDTO.getWeight()) : "N/A");
                assignedProfessional.postValue(userDTO.getAssignedProfessionalName() != null ?
                        userDTO.getAssignedProfessionalName() : "Unassigned");

                isDataLoaded = true;
            }
            isLoading.postValue(false);
        }).exceptionally(e -> {
            isLoading.postValue(false);
            return null;
        });
    }


    public LiveData<String> getDisplayName() {
        return displayName;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getHeight() {
        return height;
    }

    public LiveData<String> getWeight() {
        return weight;
    }

    public LiveData<String> getAssignedProfessional() {
        return assignedProfessional;
    }
}

