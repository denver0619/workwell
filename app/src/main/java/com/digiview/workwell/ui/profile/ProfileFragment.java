package com.digiview.workwell.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.digiview.workwell.ui.auth.AuthLoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import com.digiview.workwell.R;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Find the logout button
        Button btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnLogout){
            logout();
        }
    }

    // Method to handle the logout
    private void logout() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast for confirmation
        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Navigate the user to the login screen
        Intent intent = new Intent(getActivity(), AuthLoginActivity.class);
        startActivity(intent);
        getActivity().finish();  // Optionally finish the current activity to prevent user from navigating back
    }
}