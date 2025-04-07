package com.digiview.workwell.ui.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.OnboardingItem;
import com.digiview.workwell.ui.main.MainActivity;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button btnSkip, btnNext;
    private OnboardingAdapter onboardingAdapter;
    private List<OnboardingItem> onboardingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if onboarding has already been completed.
        if (!isFirstTimeLaunch()) {
            launchMainActivity();
            finish();
            return;
        }

        setContentView(R.layout.activity_onboarding);

        // Initialize views
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        btnSkip = findViewById(R.id.btnSkip);
        btnNext = findViewById(R.id.btnNext);

        // Prepare onboarding items
        onboardingItems = new ArrayList<>();
        // First page with a welcome title
        onboardingItems.add(new OnboardingItem(
                R.drawable.img_onboarding_0,
                "WELCOME TO\nWORKWELL!",
                "Ready to dive in? Here’s a quick guide to help you get started."
        ));
        // Subsequent pages (title hidden in adapter)
        onboardingItems.add(new OnboardingItem(
                R.drawable.img_onboarding_1,
                "",
                "This Home Tab gives you a quick overview of your personal health details."
        ));
        onboardingItems.add(new OnboardingItem(
                R.drawable.img_onboarding_2,
                "",
                "This Routine Tab shows your list of Active and Inactive routines. Click \"Start Routine\" to begin."
        ));
        onboardingItems.add(new OnboardingItem(
                R.drawable.img_onboarding_3,
                "",
                "This Progress Log Tab displays a list of your previously completed routines."
        ));
        onboardingItems.add(new OnboardingItem(
                R.drawable.img_onboarding_4,
                "",
                "This Care Plan Tab shows a list of diagnoses provided by your healthcare professional."
        ));
        onboardingItems.add(new OnboardingItem(
                R.drawable.img_onboarding_5,
                "",
                "This Profile Tab allows you to manage your settings and sign out of your account."
        ));

        // Set up the adapter and ViewPager2
        onboardingAdapter = new OnboardingAdapter(onboardingItems);
        viewPager.setAdapter(onboardingAdapter);

        // Attach TabLayoutMediator to show bullet dots.
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText("•");
        }).attach();

        // Skip button listener
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFirstTimeLaunch(false);
                launchMainActivity();
                finish();
            }
        });

        // Next button listener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem < onboardingItems.size() - 1) {
                    viewPager.setCurrentItem(currentItem + 1);
                } else {
                    setFirstTimeLaunch(false);
                    launchMainActivity();
                    finish();
                }
            }
        });
    }

    private boolean isFirstTimeLaunch() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getBoolean("isFirstTime", true);
    }

    private void setFirstTimeLaunch(boolean isFirstTime) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isFirstTime", isFirstTime);
        editor.apply();
    }

    private void launchMainActivity() {
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
