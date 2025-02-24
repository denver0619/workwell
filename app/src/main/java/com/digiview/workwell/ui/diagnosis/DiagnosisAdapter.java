package com.digiview.workwell.ui.diagnosis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.Diagnosis;
import java.util.List;

public class DiagnosisAdapter extends RecyclerView.Adapter<DiagnosisAdapter.DiagnosisViewHolder> {

    private List<Diagnosis> diagnosisList;
    private final OnViewClickListener onViewClickListener;

    public interface OnViewClickListener {
        void onViewClicked(Diagnosis diagnosis);
    }

    public DiagnosisAdapter(List<Diagnosis> diagnosisList, OnViewClickListener listener) {
        this.diagnosisList = diagnosisList;
        this.onViewClickListener = listener;
    }

    public void setDiagnosisList(List<Diagnosis> diagnosisList) {
        this.diagnosisList = diagnosisList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiagnosisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diagnosis, parent, false);
        return new DiagnosisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosisViewHolder holder, int position) {
        Diagnosis diagnosis = diagnosisList.get(position);
        holder.tvDiagnosisDate.setText(diagnosis.getFormattedDiagnosisDate());

        holder.btnViewDiagnosis.setOnClickListener(v -> onViewClickListener.onViewClicked(diagnosis));
    }

    @Override
    public int getItemCount() {
        return diagnosisList != null ? diagnosisList.size() : 0;
    }

    public static class DiagnosisViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiagnosisDate;
        Button btnViewDiagnosis;

        public DiagnosisViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiagnosisDate = itemView.findViewById(R.id.tvDiagnosisDate);
            btnViewDiagnosis = itemView.findViewById(R.id.btnViewDiagnosis);
        }
    }
}
