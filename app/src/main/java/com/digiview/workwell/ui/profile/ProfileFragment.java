package com.digiview.workwell.ui.profile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.digiview.workwell.R;
import com.digiview.workwell.data.service.UserService;
import com.digiview.workwell.ui.auth.AuthLoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), AuthLoginActivity.class); // Redirect to login screen
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
            getActivity().finish(); // Close current activity
        });

        // Account CardView Click - Launch ProfileAccountFragment
        CardView cardAccount = view.findViewById(R.id.cardAccount);
        CardView cardHelp = view.findViewById(R.id.cardHelp);
        CardView cardAboutUs = view.findViewById(R.id.cardAboutUs);
        cardAccount.setOnClickListener(v -> openFragment(new ProfileAccountFragment()));
        cardHelp.setOnClickListener(v -> openFragment(new ProfileHelpFragment()));
        cardAboutUs.setOnClickListener(v -> openFragment(new ProfileAboutUsFragment()));
    }

    // Function to navigate to the desired fragment
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flFragmentContainer, fragment); // Ensure fragment_container is the host layout in your activity
        transaction.addToBackStack(null);
        transaction.commit();
    }




}
