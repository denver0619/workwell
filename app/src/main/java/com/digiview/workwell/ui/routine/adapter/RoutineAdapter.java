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

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    private List<String> routineList;
    private OnRoutineClickListener onRoutineClickListener;

    public RoutineAdapter(List<String> routineList, OnRoutineClickListener onRoutineClickListener){
        this.routineList = routineList;
        this.onRoutineClickListener = onRoutineClickListener;
    }

    public void updateDataList(List<String> newDataList){
        this.routineList = newDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoutineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineAdapter.ViewHolder holder, int position) {
        String routineTitle = routineList.get(position);
        holder.itemTitle.setText(routineTitle);
        holder.button.setOnClickListener(v -> {
            onRoutineClickListener.onRoutineClicked(routineTitle);
        });
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
        public Button button;
        public ViewHolder(View view){
            super(view);
            itemTitle = view.findViewById(R.id.tvItemTitle);
            button = view.findViewById(R.id.btnStartRoutine);
        }
    }

    public interface OnRoutineClickListener {
        void onRoutineClicked(String routineTitle);
    }
}
