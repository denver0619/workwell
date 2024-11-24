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

import com.digiview.workwell.ui.main.MainActivity;
import com.digiview.workwell.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class AuthLoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText inputEmail, inputPassword;
    Button emailLogin, googleLogin, signupRedirect;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.etLoginEmail);
        inputPassword = findViewById(R.id.etLoginPassword);
        emailLogin = findViewById(R.id.btnLoginEmail);
        googleLogin = findViewById(R.id.btnLoginGoogle);
        signupRedirect = findViewById(R.id.btnSignupRedirect);

        googleLogin.setOnClickListener(this);
        emailLogin.setOnClickListener(this);
        signupRedirect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignupRedirect) {
            Intent intent = new Intent(AuthLoginActivity.this, AuthSignupActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnLoginEmail) {
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

            // Perform login using Firebase Authentication
            loginWithEmail(email, password);
        }
    }

    private void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login successful
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("FirebaseAuthTest", "Login successful! UID: " + user.getUid());

                        // Fetch and log token claims
                        fetchAndCheckUserClaims(user);
                    } else {
                        // Login failed, handle errors
                        handleLoginError(task.getException());
                    }
                });
    }

    private void fetchAndCheckUserClaims(FirebaseUser user) {
        if (user != null) {
            user.getIdToken(true) // Force refresh to ensure latest claims
                    .addOnCompleteListener(tokenTask -> {
                        if (tokenTask.isSuccessful()) {
                            Map<String, Object> claims = tokenTask.getResult().getClaims();
                            Log.d("AuthDebug", "Claims: " + claims.toString());

                            // Check the Role claim
                            if (claims.containsKey("Role")) {
                                Object roleObj = claims.get("Role");
                                long role = -1;

                                // Handle Integer and Long cases for the Role claim
                                if (roleObj instanceof Integer) {
                                    role = ((Integer) roleObj).longValue();
                                } else if (roleObj instanceof Long) {
                                    role = (Long) roleObj;
                                }

                                if (role == 1) {
                                    Log.d("AuthDebug", "User role: Standard User");
                                    proceedToApp();
                                } else if (role == 0) {
                                    Log.d("AuthDebug", "User role: Admin");
                                    denyAccess("Admins are not allowed to access this app.");
                                } else {
                                    denyAccess("Invalid role detected.");
                                }
                            } else {
                                denyAccess("Role claim not found.");
                            }
                        } else {
                            Log.e("AuthDebug", "Error fetching token: " + tokenTask.getException().getMessage());
                            denyAccess("Failed to verify user role.");
                        }
                    });
        } else {
            denyAccess("User is not authenticated.");
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
        // Navigate to the main app (MainActivity)
        Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void denyAccess(String message) {
        // Inform the user they cannot access the app
        Log.d("FirebaseAuthTest", "Access denied: " + message);
        Toast.makeText(AuthLoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
