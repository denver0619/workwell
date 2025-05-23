package com.digiview.workwell.ui.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.digiview.workwell.R;
import com.digiview.workwell.data.service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditFragment extends Fragment {

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup top bar back button
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // Get references to Material TextInputEditText fields
        TextInputEditText etContact = view.findViewById(R.id.etContact);
//        TextInputEditText etFirstName = view.findViewById(R.id.etFirstName);
//        TextInputEditText etLastName = view.findViewById(R.id.etLastName);
        TextInputEditText etHeight = view.findViewById(R.id.etHeight);
        TextInputEditText etWeight = view.findViewById(R.id.etWeight);
        TextInputEditText etBirthDate = view.findViewById(R.id.etBirthDate);
        TextInputEditText etAddress = view.findViewById(R.id.etAddress);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Retrieve current field values passed via Bundle and set as initial text
        Bundle args = getArguments();
        if (args != null) {
            etContact.setText(args.getString("contact", "Contact"));
//            etFirstName.setText(args.getString("firstName", ""));
//            etLastName.setText(args.getString("lastName", ""));
            etHeight.setText(args.getString("height", "Height"));
            etWeight.setText(args.getString("weight", "Weight"));
            etBirthDate.setText(args.getString("birthDate", "Birth Date"));
            etAddress.setText(args.getString("address", "Address"));
        }

        // Save updated data when the Save button is clicked
        btnSave.setOnClickListener(v -> {
            // Use entered text directly
            String updatedContact = etContact.getText().toString().trim();
//            String updatedFirstName = etFirstName.getText().toString().trim();
//            String updatedLastName = etLastName.getText().toString().trim();
            String updatedHeight = etHeight.getText().toString().trim();
            String updatedWeight = etWeight.getText().toString().trim();
            String updatedBirthDate = etBirthDate.getText().toString().trim();
            String updatedAddress = etAddress.getText().toString().trim();

            // Validate contact format: "09xx xxx xxxx"
            if (!updatedContact.matches("^09\\d{2} \\d{3} \\d{4}$")) {
                Toast.makeText(getContext(), "Invalid contact format. Please use 09xx xxx xxxx", Toast.LENGTH_SHORT).show();
                return;
            }

//            // Validate first name: only letters allowed
//            if (!updatedFirstName.matches("^[A-Za-z]+$")) {
//                Toast.makeText(getContext(), "Invalid first name. Only alphabets allowed.", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Validate first name: allow multiple words (only alphabets and spaces allowed)
//            if (!updatedFirstName.matches("^[A-Za-z]+(?: [A-Za-z]+)*$")) {
//                Toast.makeText(getContext(), "Invalid first name. Only alphabets and spaces allowed.", Toast.LENGTH_SHORT).show();
//                return;
//            }


            // Convert the birth date string (in format "yyyy/MM/dd") to a Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date birthDate;
            try {
                birthDate = sdf.parse(updatedBirthDate);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Invalid birth date format. Please use yyyy/MM/dd", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate age
            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);
            Calendar now = Calendar.getInstance();
            int age = now.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            if (age < 18 || age > 24) {
                Toast.makeText(getContext(), "Age must be between 18 and 24 years. Your age: " + age, Toast.LENGTH_SHORT).show();
                return;
            }

            // Prepare the data for Firestore update
            Map<String, Object> updates = new HashMap<>();
            updates.put("Contact", updatedContact);
//            updates.put("FirstName", updatedFirstName);
//            updates.put("LastName", updatedLastName);
            try {
                updates.put("Height", Double.parseDouble(updatedHeight));
            } catch (NumberFormatException e) {
                updates.put("Height", 0.0);
            }
            try {
                updates.put("Weight", Double.parseDouble(updatedWeight));
            } catch (NumberFormatException e) {
                updates.put("Weight", 0.0);
            }
            updates.put("BirthDate", birthDate); // Now storing as a Date, which Firestore will save as a Timestamp.
            updates.put("Address", updatedAddress);

            // Update Firestore using UserService
            UserService userService = new UserService();
            userService.updateUserData(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        // After updating, pop this fragment off the back stack so that ProfileAccountFragment resumes and reloads
                        requireActivity().getSupportFragmentManager().popBackStack();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
