package com.digiview.workwell.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.digiview.workwell.data.service.UserService;
import com.google.firebase.firestore.DocumentSnapshot;

public class HomeViewModel extends ViewModel {

    private UserService userService;
    private MutableLiveData<String> displayName = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public HomeViewModel() {
        userService = new UserService();  // Initialize the UserService
    }

    // Fetch user data from Firebase
    public void fetchUserData() {
        isLoading.setValue(true);  // Set loading to true when starting to fetch data
        userService.getUserData().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Fetch the user's first and last name from Firestore
                    String firstName = document.getString("FirstName");
                    String lastName = document.getString("LastName");

                    // Combine first name and last name
                    String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");

                    // Update LiveData with the full name
                    displayName.setValue(fullName);
                }
            } else {
                // Handle error in fetching data
                displayName.setValue("Error fetching data");
            }
            isLoading.setValue(false);  // Set loading to false after data fetching is done
        });
    }

    // Expose LiveData for full name to the fragment
    public LiveData<String> getDisplayName() {
        return displayName;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}