package com.digiview.workwell.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private final FirebaseFirestore db;
    private final FirebaseUser currentUser;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Fetches user data from Firestore based on the current user's UID.
     *
     * @return A Task representing the asynchronous Firestore operation to fetch the user's document.
     */
    public Task<DocumentSnapshot> getUserData() {
        if (currentUser != null) {
            return db.collection("users") // Assuming "users" is your collection name
                    .document(currentUser.getUid())
                    .get();
        } else {
            throw new IllegalStateException("User not logged in");
        }
    }
}
