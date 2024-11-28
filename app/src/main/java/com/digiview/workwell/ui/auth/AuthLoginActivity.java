package com.digiview.workwell.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.workwell.R;
import com.digiview.workwell.data.service.AuthService;
import com.digiview.workwell.ui.main.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class AuthLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText inputEmail, inputPassword;
    private Button emailLogin, googleLogin, signupRedirect;

    private AuthService authService; // Service for authentication workflows

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
//        googleLogin = findViewById(R.id.btnLoginGoogle);
//        signupRedirect = findViewById(R.id.btnSignupRedirect);

//        googleLogin.setOnClickListener(this);
        emailLogin.setOnClickListener(this);
//        signupRedirect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.btnSignupRedirect) {
//            redirectToSignup();
//        } else
        if (v.getId() == R.id.btnLoginEmail) {
            handleEmailLogin();
        }
    }

//    private void redirectToSignup() {
//        Intent intent = new Intent(AuthLoginActivity.this, AuthSignupActivity.class);
//        startActivity(intent);
//    }

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
                        Log.d("FirebaseAuthTest", "Login successful!");
                        fetchAndCheckUserClaims();
                    } else {
                        handleLoginError(task.getException());
                    }
                });
    }

    private void fetchAndCheckUserClaims() {
        authService.getIdToken(true)
                .addOnCompleteListener(tokenTask -> {
                    if (tokenTask.isSuccessful()) {
                        long role = authService.extractUserRole(tokenTask.getResult().getClaims());
                        handleUserRole(role);
                    } else {
                        Log.e("AuthDebug", "Error fetching token: " + tokenTask.getException().getMessage());
                        denyAccess("Failed to verify user role.");
                    }
                });
    }

    private void handleUserRole(long role) {
        if (role == 1) {
            Log.d("AuthDebug", "User role: Standard User");
            proceedToApp();
        } else if (role == 0) {
            Log.d("AuthDebug", "User role: Admin");
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
        Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void denyAccess(String message) {
        Log.d("FirebaseAuthTest", "Access denied: " + message);
        Toast.makeText(AuthLoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
