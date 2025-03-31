package com.digiview.workwell.data.service;

import android.annotation.SuppressLint;

import com.digiview.workwell.data.models.User;
import com.digiview.workwell.data.models.UserDTO;
import com.digiview.workwell.data.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    /**
     * Update the user data.
     */
    public Task<Void> updateUserData(Map<String, Object> updates) {
        return userRepository.updateUserData(updates);
    }

    @SuppressLint("NewApi")
    public CompletableFuture<UserDTO> getCompleteUserData() {
        return userRepository.getCompleteUserData()
                .thenCompose(document -> {
                    if (document == null || !document.exists()) {
                        return CompletableFuture.failedFuture(new Exception("User not found"));
                    }

                    User user = document.toObject(User.class);
                    if (user == null) {
                        return CompletableFuture.failedFuture(new Exception("Invalid user data"));
                    }

                    UserDTO userDTO = new UserDTO(
                            user.getUid(),
                            user.getEmail(),
                            user.getFirstName() + " " + user.getLastName(),
                            user.getBirthDate(),
                            user.getContact(),
                            user.getHeight(),
                            user.getWeight(),
                            user.getAddress(),
                            user.getAssignedProfessional(),
                            null // Assigned professional name will be set later
                    );

                    if (user.getAssignedProfessional() == null || user.getAssignedProfessional().isEmpty()) {
                        return CompletableFuture.completedFuture(userDTO);
                    }

                    // Fetch assigned professional's name
                    return userRepository.getUserById(user.getAssignedProfessional())
                            .thenApply(professionalDoc -> {
                                if (professionalDoc != null && professionalDoc.exists()) {
                                    String professionalFirstName = professionalDoc.getString("FirstName");
                                    String professionalLastName = professionalDoc.getString("LastName");
                                    String professionalName = professionalFirstName + " " + professionalLastName;
                                    userDTO.setAssignedProfessionalName(professionalName);
                                } else {
                                    userDTO.setAssignedProfessionalName("Unknown Professional");
                                }
                                return userDTO;
                            });
                });
    }
    // Additional user-related business logic can be added here
}
