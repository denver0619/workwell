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
import com.digiview.workwell.services.ui.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


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

                        // Now, check the user's role in Firestore
                        checkUserRole(user);
                    } else {
                        // Login failed, handle errors
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Log.e("FirebaseAuthTest", "Invalid credentials");
                            Toast.makeText(AuthLoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        } catch (FirebaseAuthUserCollisionException e) {
                            Log.e("FirebaseAuthTest", "User already exists");
                            Toast.makeText(AuthLoginActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("FirebaseAuthTest", "Login failed: " + e.getMessage());
                            Toast.makeText(AuthLoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkUserRole(FirebaseUser user) {
        if (user != null) {
            // Get the user ID
            String userId = user.getUid();

            // Reference to the users collection in Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Get the user's role
                                Long role = document.getLong("Role");
                                if (role != null && role == 1) {
                                    // Role is 1, allow user to proceed to the app
                                    proceedToApp();
                                } else {
                                    // Role is not 1 (admin), deny access
                                    denyAccess();
                                }
                            } else {
                                // No document found, deny access
                                denyAccess();
                            }
                        } else {
                            // Error fetching user data
                            Log.e("FirebaseAuthTest", "Error fetching user role: " + task.getException());
                            denyAccess();
                        }
                    });
        }
    }

    private void proceedToApp() {
        // Navigate to the main app (MainActivity)
        Intent intent = new Intent(AuthLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void denyAccess() {
        // Inform the user they cannot access the app
        Log.d("FirebaseAuthTest", "User is not authorized to access the app.");
        Toast.makeText(AuthLoginActivity.this, "You are not authorized to use this app.", Toast.LENGTH_SHORT).show();
    }
}
