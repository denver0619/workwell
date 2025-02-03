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
     * Extract the user's role from claims as a string.
     */
    public String extractUserRoleAsString(Map<String, Object> claims) {
        if (claims.containsKey("Role")) {
            Object roleObj = claims.get("Role");
            if (roleObj instanceof String) {
                return (String) roleObj; // Return role as a string
            }
        }
        return null; // Default to null if role is not present or invalid
    }
}
