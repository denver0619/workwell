package com.digiview.workwell.services.ui;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.Task;

public class UserService {

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public UserService() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // Fetch user data based on the current user's UID
    public Task<DocumentSnapshot> getUserData() {
        if (currentUser != null) {
            return db.collection("users")  // Assuming "users" is your collection name
                    .document(currentUser.getUid())
                    .get();
        } else {
            throw new IllegalStateException("User not logged in");
        }
    }
}