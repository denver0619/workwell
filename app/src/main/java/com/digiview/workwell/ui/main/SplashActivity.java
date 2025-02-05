package com.digiview.workwell.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.digiview.workwell.R;
import com.digiview.workwell.ui.auth.AuthLoginActivity;
import com.digiview.workwell.ui.debug.DebugActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // Duration in milliseconds (3 seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_splash);

        
        // Button to continue immediately
        Button continueButton = findViewById(R.id.btnContinue);
        continueButton.setOnClickListener(view -> transitionToMainActivity());

        // Auto-transition after SPLASH_DELAY
//        new Handler().postDelayed(this::transitionToMainActivity, SPLASH_DELAY);
    }

    private void transitionToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, AuthLoginActivity.class);
//        Intent intent = new Intent(SplashActivity.this, DebugActivity.class);
        startActivity(intent);
        finish(); // Prevent going back to splash screen
    }
}
