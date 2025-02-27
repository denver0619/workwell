package com.digiview.workwell.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.digiview.workwell.R;
import com.digiview.workwell.ui.auth.AuthLoginActivity;

public class ProfileTermsConditionActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_ACCEPTED_TERMS = "AcceptedTerms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_terms_condition);

        Button btnAccept = findViewById(R.id.btnAccept);

        btnAccept.setOnClickListener(v -> {
            // Save acceptance to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_ACCEPTED_TERMS, true);
            editor.apply();

            // Redirect to Login or Main Activity
            Intent intent = new Intent(ProfileTermsConditionActivity.this, AuthLoginActivity.class);
            startActivity(intent);
            finish(); // Close this activity
        });
    }
}
