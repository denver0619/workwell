package com.digiview.workwell.ui.routine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.workwell.R;

import java.util.List;


public class RoutineDetailAdapter extends RecyclerView.Adapter<RoutineDetailAdapter.ViewHolder> {
    private List<String> routineDetailList;
    private OnRoutineClickListener onRoutineClickListener;

    public RoutineDetailAdapter(List<String> routineDetailList, OnRoutineClickListener onRoutineClickListener){
        this.routineDetailList = routineDetailList;
        this.onRoutineClickListener = onRoutineClickListener;
    }

    public void updateDataList(List<String> newDataList){
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
        String routineDetailTitle = routineDetailList.get(position);
        holder.itemTitle.setText(routineDetailTitle);
//        holder.button.setOnClickListener(v -> {
//            onRoutineClickListener.onExerciseClicked(routineDetailTitle);
//        });
    }


    @Override
    public int getItemCount() {
        return routineDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
        public Button button;
        public ViewHolder(View view){
            super(view);
            itemTitle = view.findViewById(R.id.tvItemTitle);
//            button = view.findViewById(R.id.btnStartExercise);
        }
    }

    public interface OnRoutineClickListener {
        void onExerciseClicked(String exerciseTitle);
    }
}
