package com.digiview.workwell.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.workwell.R;
import com.digiview.workwell.data.service.AuthService;
import com.digiview.workwell.ui.main.MainActivity;
import com.digiview.workwell.ui.profile.ProfileTermsConditionActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.digiview.workwell.data.util.Constants;
import com.google.firebase.auth.FirebaseUser;

public class AuthLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText inputEmail, inputPassword;
    private Button emailLogin;
    private TextView tvForgotPassword;
    private AuthService authService; // Service for authentication workflows

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_ACCEPTED_TERMS = "AcceptedTerms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth_login);

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize AuthService
        authService = new AuthService();
        // Initialize UI components
        inputEmail = findViewById(R.id.etLoginEmail);
        inputPassword = findViewById(R.id.etLoginPassword);
        emailLogin = findViewById(R.id.btnLoginEmail);
        emailLogin.setOnClickListener(this);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean hasAcceptedTerms = sharedPreferences.getBoolean(KEY_ACCEPTED_TERMS, false);

        if (!hasAcceptedTerms) {
            // Redirect to Terms and Conditions Activity
            Intent intent = new Intent(this, ProfileTermsConditionActivity.class);
            startActivity(intent);
            finish(); // Prevent returning to login until accepted
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLoginEmail) {
            handleEmailLogin();
        }
    }

    private void handleEmailLogin() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (email.isEmpty()) {
            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return;
        }

        // Delegate login logic to AuthService
        authService.login(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = authService.getCurrentUser();

                        // ✅ Check if email is verified
                        if (user != null && user.isEmailVerified()) {
                            Log.d("FirebaseAuthTest", "Login successful and email verified!");
                            fetchAndCheckUserClaims();
                        } else {
                            Log.e("FirebaseAuthTest", "Email not verified");
                            Toast.makeText(AuthLoginActivity.this,
                                    "Please verify your email before logging in.", Toast.LENGTH_LONG).show();

                            // Optional: Send a new verification email
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnSuccessListener(aVoid -> Toast.makeText(AuthLoginActivity.this,
                                                "A new verification email has been sent.", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> Log.e("FirebaseAuthTest", "Failed to send email verification: " + e.getMessage()));
                            }
                        }
                    } else {
                        handleLoginError(task.getException());
                    }
                });
    }

    private void handleForgotPassword() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty()) {
            inputEmail.setError("Please enter your email to reset password");
            inputEmail.requestFocus();
            return;
        }
        authService.requestPasswordReset(email)
                .addOnSuccessListener(aVoid -> Toast.makeText(AuthLoginActivity.this,
                        "Password reset email sent. Check your inbox.", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(AuthLoginActivity.this,
                        "Failed to send reset email: " + e.getMessage(), Toast.LENGTH_LONG).show());

    }

    private void fetchAndCheckUserClaims() {
        authService.getIdToken(true)
                .addOnCompleteListener(tokenTask -> {
                    if (tokenTask.isSuccessful()) {
                        String role = authService.extractUserRoleAsString(tokenTask.getResult().getClaims());
                        handleUserRole(role);
                    } else {
                        Log.e("AuthDebug", "Error fetching token: " + tokenTask.getException().getMessage());
                        denyAccess("Failed to verify user role.");
                    }
                });
    }

    private void handleUserRole(String role) {
        if (Constants.UserRole.User.equals(role)) {
            Log.d("AuthDebug", "User role: Standard User");
            proceedToApp();
        } else if (Constants.UserRole.Admin.equals(role) || Constants.UserRole.SuperAdmin.equals(role)) {
            Log.d("AuthDebug", "User role: Admin or SuperAdmin");
            denyAccess("Admins are not allowed to access this app.");
        } else {
            denyAccess("Invalid role detected.");
        }
    }

    private void handleLoginError(Exception exception) {
        try {
            throw exception;
        } catch (FirebaseAuthInvalidCredentialsException e) {
            Log.e("FirebaseAuthTest", "Invalid credentials");
            Toast.makeText(AuthLoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("FirebaseAuthTest", "Login failed: " + e.getMessage());
            Toast.makeText(AuthLoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void proceedToApp() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Check if the user has seen onboarding. Default is true (first time).
        boolean isFirstTimeOnboarding = sharedPreferences.getBoolean("isFirstTimeOnboarding", true);
        if (isFirstTimeOnboarding) {
            Intent intent = new Intent(AuthLoginActivity.this, com.digiview.workwell.ui.onboarding.OnboardingActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }


    private void denyAccess(String message) {
        Log.d("FirebaseAuthTest", "Access denied: " + message);
        Toast.makeText(AuthLoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }



}
