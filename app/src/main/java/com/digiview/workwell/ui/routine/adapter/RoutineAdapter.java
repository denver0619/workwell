package com.digiview.workwell.ui.routine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.workwell.R;
import com.digiview.workwell.models.Routine;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    private List<Routine> routineList;
    private final OnRoutineClickListener onRoutineClickListener;

    public RoutineAdapter(List<Routine> routineList, OnRoutineClickListener onRoutineClickListener) {
        this.routineList = routineList;
        this.onRoutineClickListener = onRoutineClickListener;
    }

    public void updateDataList(List<Routine> newDataList) {
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
        Routine routine = routineList.get(position);
        holder.itemTitle.setText(routine.getName());

        holder.button.setOnClickListener(v -> {
            onRoutineClickListener.onRoutineClicked(routine); // Pass the Routine object
        });
    }


    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle;
//        public TextView itemDescription; // Added a field for description
        public Button button;

        public ViewHolder(View view) {
            super(view);
            itemTitle = view.findViewById(R.id.tvItemTitle);
//            itemDescription = view.findViewById(R.id.tvItemDescription); // Assuming description TextView ID
            button = view.findViewById(R.id.btnStartRoutine);
        }
    }

    public interface OnRoutineClickListener {
        void onRoutineClicked(Routine routine); // Pass Routine object
    }

}
