package com.digiview.workwell.ui.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.User;
import com.digiview.workwell.data.models.UserDTO;
import com.digiview.workwell.data.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private UserService userService;
    private TextView tvEmail, tvName, tvAge, tvContact, tvHeight, tvWeight, tvAddress, tvAssignedProfessional;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userService = new UserService();

        // Initialize UI elements
        tvEmail = view.findViewById(R.id.tvEmail);
        tvName = view.findViewById(R.id.tvName);
        tvAge = view.findViewById(R.id.tvAge);
        tvContact = view.findViewById(R.id.tvContact);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvAssignedProfessional = view.findViewById(R.id.tvAssignedProfessional);

        // Load user data
        loadUserData();
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        userService.getCompleteUserData().thenAccept(user -> {
            if (user != null) {
                Log.d("ProfileFragment", "User fetched: " + user.getEmail());

                // Apply dynamic bold styling for labels
                tvEmail.setText(getBoldFormattedText("Email:", user.getEmail()));
                tvName.setText(getBoldFormattedText("Name:", user.getName()));
                tvAge.setText(getBoldFormattedText("Age:", String.valueOf(user.getAge())));
                tvContact.setText(getBoldFormattedText("Contact:", user.getContact()));
                tvHeight.setText(getBoldFormattedText("Height:", user.getHeight() + " cm"));
                tvWeight.setText(getBoldFormattedText("Weight:", user.getWeight() + " kg"));
                tvAddress.setText(getBoldFormattedText("Address:", user.getAddress()));
                tvAssignedProfessional.setText(getBoldFormattedText("Assigned Professional:", user.getAssignedProfessionalName()));
            }
        }).exceptionally(ex -> {
            Log.e("ProfileFragment", "Error fetching user data", ex);
            Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            return null;
        });
    }

    private SpannableStringBuilder getBoldFormattedText(String label, String value) {
        SpannableStringBuilder builder = new SpannableStringBuilder(label + " " + value);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

}
