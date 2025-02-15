package com.digiview.workwell.data.repository;

import android.annotation.SuppressLint;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

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
            return db.collection("users")
                    .document(currentUser.getUid())
                    .get();
        } else {
            throw new IllegalStateException("User not logged in");
        }
    }

    /**
     * Fetch current user data from Firestore.
     * @return CompletableFuture<DocumentSnapshot>
     */
    @SuppressLint("NewApi")
    public CompletableFuture<DocumentSnapshot> getCompleteUserData() {
        if (currentUser == null) {
            return CompletableFuture.failedFuture(new Exception("User not logged in"));
        }

        CompletableFuture<DocumentSnapshot> future = new CompletableFuture<>();
        db.collection("users").document(currentUser.getUid()).get()
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    /**
     * Fetch a user document by ID.
     * @param userId The user ID to fetch.
     * @return CompletableFuture<DocumentSnapshot>
     */
    public CompletableFuture<DocumentSnapshot> getUserById(String userId) {
        CompletableFuture<DocumentSnapshot> future = new CompletableFuture<>();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }
}
