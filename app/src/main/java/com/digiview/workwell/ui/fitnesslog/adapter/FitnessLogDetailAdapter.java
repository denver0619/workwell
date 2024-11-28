package com.digiview.workwell.ui.fitnesslog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.RoutineExercise;

import java.util.List;

public class FitnessLogDetailAdapter extends RecyclerView.Adapter<FitnessLogDetailAdapter.ExerciseViewHolder> {
    private List<RoutineExercise> exercises;

    public FitnessLogDetailAdapter(List<RoutineExercise> exercises) {
        this.exercises = exercises;
    }

    public void updateExercises(List<RoutineExercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        RoutineExercise exercise = exercises.get(position);
        holder.tvExerciseName.setText(exercise.getExerciseName());
        holder.tvReps.setText("Reps: " + exercise.getReps());
        holder.tvDuration.setText("Duration: " + exercise.getDuration() + " milliseconds");
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName, tvReps, tvDuration;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseItemName);
            tvReps = itemView.findViewById(R.id.tvExerciseItemReps);
            tvDuration = itemView.findViewById(R.id.tvExerciseItemDuration);
        }
    }
}

