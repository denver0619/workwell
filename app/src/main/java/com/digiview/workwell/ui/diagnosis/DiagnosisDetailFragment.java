package com.digiview.workwell.ui.diagnosis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.Diagnosis;

public class DiagnosisDetailFragment extends Fragment {

    private TextView tvSymptoms, tvDiagnosis, tvSeverity;
    private TextView tvRecommendedErgonomicAdjustment, tvPhysicalTherapyRecommendation, tvMedicationPrescription;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnosis_detail, container, false);

        // Initialize UI components
        tvSymptoms = view.findViewById(R.id.tvSymptoms);
        tvDiagnosis = view.findViewById(R.id.tvDiagnosis);
        tvSeverity = view.findViewById(R.id.tvSeverity);
        tvRecommendedErgonomicAdjustment = view.findViewById(R.id.tvRecommendedErgonomicAdjustment);
        tvPhysicalTherapyRecommendation = view.findViewById(R.id.tvPhysicalTherapyRecommendation);
        tvMedicationPrescription = view.findViewById(R.id.tvMedicationPrescription);

        // Get the passed Diagnosis object
        if (getArguments() != null && getArguments().getSerializable("DIAGNOSIS") != null) {
            Diagnosis diagnosis = (Diagnosis) getArguments().getSerializable("DIAGNOSIS");

            // Populate the UI
            tvSymptoms.setText(diagnosis.getSymptoms());
            tvDiagnosis.setText(diagnosis.getDiagnosisResult());
            tvSeverity.setText(diagnosis.getSeverityLevel());
            tvRecommendedErgonomicAdjustment.setText(diagnosis.getRecommendedErgonomicAdjustments());
            tvPhysicalTherapyRecommendation.setText(diagnosis.getPhysicalTherapyRecommendations());
            tvMedicationPrescription.setText(diagnosis.getMedicationPrescriptions());
        }

        return view;
    }
}
