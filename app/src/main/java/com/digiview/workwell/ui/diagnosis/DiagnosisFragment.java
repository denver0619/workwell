package com.digiview.workwell.ui.diagnosis;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.Diagnosis;
import com.digiview.workwell.data.service.DiagnosisService;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisFragment extends Fragment {

    private RecyclerView recyclerView;
    private DiagnosisAdapter adapter;
    private DiagnosisService diagnosisService;
    private ImageView progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        recyclerView = view.findViewById(R.id.rvDiagnosis);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        diagnosisService = new DiagnosisService();
        progressBar = view.findViewById(R.id.progressBar);
        // Load GIF into ImageView using Glide
        Glide.with(this)
                .asGif()
                .load(R.drawable.ic_loading) // Replace with your actual GIF in drawable
                .into(progressBar);

        adapter = new DiagnosisAdapter(new ArrayList<>(), diagnosis -> {
            // Navigate to DiagnosisDetailFragment and pass the Diagnosis object
            DiagnosisDetailFragment detailFragment = new DiagnosisDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("DIAGNOSIS", diagnosis);
            detailFragment.setArguments(bundle);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flFragmentContainer, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        recyclerView.setAdapter(adapter);
        loadDiagnoses();

        return view;
    }

    private void loadDiagnoses() {
        // Show loading GIF and hide RecyclerView initially
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        diagnosisService.getAllDiagnosis().thenAccept(diagnosisList -> {
            if (diagnosisList != null) {
                adapter.setDiagnosisList(diagnosisList);
            }

            // Hide loading GIF and show RecyclerView
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }).exceptionally(e -> {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE); // Hide GIF even if there's an error
            return null;
        });
    }
}
