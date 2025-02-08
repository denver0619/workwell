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

    public HomeViewModel() {
        userService = new UserService(); // Initialize UserService
    }

    // Fetch complete user data
    public void fetchUserData() {
        isLoading.setValue(true); // Start loading

        userService.getCompleteUserData().thenAccept(userDTO -> {
            if (userDTO != null) {
                // Manually create Full Name by appending FirstName and LastName
                String fullName = userDTO.getName();

                displayName.postValue(fullName.trim().isEmpty() ? "Unknown User" : fullName);

                // Convert Double height/weight to String with formatting
                height.postValue(userDTO.getHeight() != 0 ? String.format("%.2f cm", userDTO.getHeight()) : "N/A");
                weight.postValue(userDTO.getWeight() != 0 ? String.format("%.2f kg", userDTO.getWeight()) : "N/A");


                // Set Assigned Professional Name
                assignedProfessional.postValue(userDTO.getAssignedProfessionalName() != null ?
                        userDTO.getAssignedProfessionalName() : "Unassigned");
            }
            isLoading.postValue(false); // Stop loading
        }).exceptionally(e -> {
            displayName.postValue("Error fetching data");
            height.postValue("N/A");
            weight.postValue("N/A");
            assignedProfessional.postValue("Unassigned");
            isLoading.postValue(false);
            return null;
        });
    }

    // Expose LiveData to the fragment
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
