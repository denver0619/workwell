package com.digiview.workwell.data.repository;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.android.gms.tasks.Task;

public class AuthRepository {

    private final FirebaseAuth firebaseAuth;

    public AuthRepository() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Login user with email and password.
     */
    public Task<AuthResult> login(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

    /**
     * Get the current logged-in user.
     */
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    /**
     * Get the ID token for the current user.
     */
    public Task<GetTokenResult> getIdToken(boolean forceRefresh) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            return user.getIdToken(forceRefresh);
        } else {
            throw new IllegalStateException("No user is logged in.");
        }
    }

    public Task<Void> sendPasswordResetEmail(String email) {
        return firebaseAuth.sendPasswordResetEmail(email);
    }
}
