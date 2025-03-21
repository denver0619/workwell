package com.digiview.workwell.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.digiview.workwell.ui.main.MainActivity;
import com.digiview.workwell.R;
import com.google.android.material.textfield.TextInputEditText;

public class AuthSignupActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText inputEmail, inputPassword;
    Button emailLogin, googleLogin, signupRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inputEmail = findViewById(R.id.etSignupEmail);
        inputPassword = findViewById(R.id.etSignupPassword);
        emailLogin = findViewById(R.id.btnSignupEmail);
        googleLogin = findViewById(R.id.btnSignupGoogle);
        signupRedirect = findViewById(R.id.btnLoginRedirect);

        googleLogin.setOnClickListener(this);
        emailLogin.setOnClickListener(this);
        signupRedirect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLoginRedirect) {
            Intent intent = new Intent(AuthSignupActivity.this, AuthLoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnSignupEmail) {
            Intent intent = new Intent(AuthSignupActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}