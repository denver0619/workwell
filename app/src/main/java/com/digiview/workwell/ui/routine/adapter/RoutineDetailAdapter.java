package com.digiview.workwell.ui.routine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.workwell.R;
import com.digiview.workwell.models.RoutineExercise;

import java.util.List;

public class RoutineDetailAdapter extends RecyclerView.Adapter<RoutineDetailAdapter.ViewHolder> {
    private List<RoutineExercise> routineDetailList;
    private final OnExerciseClickListener onExerciseClickListener;

    public RoutineDetailAdapter(List<RoutineExercise> routineDetailList, OnExerciseClickListener onRoutineClickListener) {
        this.routineDetailList = routineDetailList;
        this.onExerciseClickListener = onRoutineClickListener;
    }

    public void updateDataList(List<RoutineExercise> newDataList) {
        this.routineDetailList = newDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoutineDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routine_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineDetailAdapter.ViewHolder holder, int position) {
        RoutineExercise exercise = routineDetailList.get(position);
        holder.itemTitle.setText(exercise.getExerciseName());
        holder.button.setOnClickListener(v -> onExerciseClickListener.onExerciseClicked(exercise));
    }

    @Override
    public int getItemCount() {
        return routineDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
        public Button button;

        public ViewHolder(View view) {
            super(view);
            itemTitle = view.findViewById(R.id.tvItemTitle);
            button = view.findViewById(R.id.btnViewExercise);
        }
    }

    public interface OnExerciseClickListener {
        void onExerciseClicked(RoutineExercise exercise);
    }
}
