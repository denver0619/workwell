package com.digiview.workwell.data.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.digiview.workwell.data.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Fetches user data for the current user.
     *
     * This method delegates the Firestore interaction to the UserRepository
     * and can include additional logic if needed in the future.
     *
     * @return A Task representing the asynchronous operation to fetch user data.
     */
    public Task<DocumentSnapshot> getUserData() {
        return userRepository.getUserData();
    }

    // Additional user-related business logic can be added here
}
