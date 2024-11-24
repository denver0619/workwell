package com.digiview.workwell.data.service;

import com.digiview.workwell.data.repository.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GetTokenResult;

import java.util.Map;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService() {
        this.authRepository = new AuthRepository();
    }

    /**
     * Delegate login to AuthRepository.
     */
    public Task<AuthResult> login(String email, String password) {
        return authRepository.login(email, password);
    }

    /**
     * Get the current user's ID token.
     */
    public Task<GetTokenResult> getIdToken(boolean forceRefresh) {
        return authRepository.getIdToken(forceRefresh);
    }

    /**
     * Extract the user's role from claims.
     */
    public long extractUserRole(Map<String, Object> claims) {
        if (claims.containsKey("Role")) {
            Object roleObj = claims.get("Role");
            if (roleObj instanceof Integer) {
                return ((Integer) roleObj).longValue();
            } else if (roleObj instanceof Long) {
                return (Long) roleObj;
            }
        }
        return -1; // Default to invalid role
    }
}
