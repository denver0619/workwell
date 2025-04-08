package com.digiview.workwell.ui.profile;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.UserDTO;
import com.digiview.workwell.data.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileAccountFragment extends Fragment {

    private UserService userService;
    private TextView tvEmail, tvName, tvBirthdate, tvContact, tvHeight, tvWeight, tvSex, tvAddress, tvAssignedProfessional;
    private Button btnEditProfile;
    // Hold the fetched user data for reuse when passing to the edit fragment.
    private UserDTO currentUserData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userService = new UserService();

        // Initialize UI elements
        tvEmail = view.findViewById(R.id.tvEmail);
        tvName = view.findViewById(R.id.tvName);
        tvBirthdate = view.findViewById(R.id.tvBirthdate);
        tvContact = view.findViewById(R.id.tvContact);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvSex = view.findViewById(R.id.tvSex);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvAssignedProfessional = view.findViewById(R.id.tvAssignedDoctor);
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Back button functionality
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Edit button now uses the currentUserData that was fetched
        btnEditProfile.setOnClickListener(v -> {
            if (currentUserData != null) {
                Bundle bundle = new Bundle();
                // Use raw values from the user data
                bundle.putString("contact", currentUserData.getContact());
                bundle.putString("height", String.valueOf(currentUserData.getHeight()));
                bundle.putString("weight", String.valueOf(currentUserData.getWeight()));
                bundle.putString("sex", String.valueOf(currentUserData.getSex()));
                bundle.putString("birthDate", currentUserData.getFormattedBirthDate());
                bundle.putString("address", currentUserData.getAddress());

                ProfileEditFragment editFragment = new ProfileEditFragment();
                editFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragmentContainer, editFragment) // ensure this is your fragment container ID
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "User data not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });

        // Load user data initially
        loadUserData();
    }

    // Reload data whenever the fragment resumes (for example, after coming back from editing)
    @Override
    public void onResume() {
        super.onResume();
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
                currentUserData = user; // store fetched user data for later use
                Log.d("ProfileFragment", "User fetched: " + user.getEmail());

                // Apply dynamic bold styling for labels
                tvEmail.setText(getBoldFormattedText("Email:", user.getEmail()));
                tvName.setText(getBoldFormattedText("Name:", user.getName()));
                tvBirthdate.setText(getBoldFormattedText("Birth Date:", String.valueOf(user.getFormattedBirthDate())));
                tvContact.setText(getBoldFormattedText("Contact:", user.getContact()));
                tvHeight.setText(getBoldFormattedText("Height:", user.getHeight() + " cm"));
                tvWeight.setText(getBoldFormattedText("Weight:", user.getWeight() + " kg"));
                tvSex.setText(getBoldFormattedText("Sex:", user.getSex()));
                tvAddress.setText(getBoldFormattedText("Address:", user.getAddress()));
                tvAssignedProfessional.setText(getBoldFormattedText("Assigned Healthcare Professional:\n", user.getAssignedProfessionalName()));
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
