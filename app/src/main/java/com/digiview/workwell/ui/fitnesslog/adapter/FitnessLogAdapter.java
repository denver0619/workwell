package com.digiview.workwell.ui.fitnesslog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineLogs;

import com.digiview.workwell.data.util.DateUtil;

import java.util.List;

public class FitnessLogAdapter extends RecyclerView.Adapter<FitnessLogAdapter.FitnessLogViewHolder> {

    private final List<RoutineLogs> routineLogsList;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onViewDetailsClick(RoutineLogs routineLogs);
    }

    public FitnessLogAdapter(Context context, List<RoutineLogs> routineLogsList, OnItemClickListener listener) {
        this.context = context;
        this.routineLogsList = routineLogsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FitnessLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fitness_log, parent, false);
        return new FitnessLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FitnessLogViewHolder holder, int position) {
        RoutineLogs routineLogs = routineLogsList.get(position);
        holder.bind(routineLogs, listener);
    }

    @Override
    public int getItemCount() {
        return routineLogsList.size();
    }

    static class FitnessLogViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRoutineName;
        private final Button btnViewDetails;
        private final TextView tvItemDate;

        public FitnessLogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoutineName = itemView.findViewById(R.id.tvItemTitle);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);

        }

        public void bind(RoutineLogs routineLogs, OnItemClickListener listener) {
            tvRoutineName.setText(routineLogs.getRoutineLogName());
            String formattedDate = DateUtil.formatDate(routineLogs.getCreatedAt());
            tvItemDate.setText(formattedDate);
            btnViewDetails.setOnClickListener(v -> listener.onViewDetailsClick(routineLogs));
        }
    }
}
