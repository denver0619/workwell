package com.digiview.workwell.data.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.concurrent.CompletableFuture;

public class AuthHelper {

    /**
     * Retrieves the OrganizationId from the current user's ID token.
     *
     * @return A CompletableFuture containing the OrganizationId as a String.
     */
    public static CompletableFuture<String> getOrganizationIdFromToken() {
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            future.completeExceptionally(new IllegalStateException("No authenticated user found"));
            return future;
        }

        // Retrieve the ID token and extract custom claims
        currentUser.getIdToken(false).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                GetTokenResult tokenResult = task.getResult();
                Object organizationId = tokenResult.getClaims().get("OrganizationId");
                if (organizationId != null) {
                    future.complete(organizationId.toString());
                } else {
                    future.completeExceptionally(new IllegalStateException("OrganizationId claim not found"));
                }
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
}
