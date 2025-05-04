package com.digiview.workwell.ui.routine.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digiview.workwell.R;
import com.digiview.workwell.data.models.Routine;

import org.w3c.dom.Text;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    private List<Routine> routineList;
    private final OnRoutineClickListener onRoutineClickListener;
    private final boolean isActive;

    public RoutineAdapter(List<Routine> routineList, OnRoutineClickListener onRoutineClickListener, boolean isActive) {
        this.routineList = routineList;
        this.onRoutineClickListener = onRoutineClickListener;
        this.isActive = isActive;
    }

    public void updateDataList(List<Routine> newDataList) {
        this.routineList.clear();
        this.routineList.addAll(newDataList);
        notifyDataSetChanged(); // Ensure UI updates
    }


    @NonNull
    @Override
    public RoutineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineAdapter.ViewHolder holder, int position) {
        Routine routine = routineList.get(position);
        Log.d("AdapterDebug", "Binding Routine: " + routine.getName() + " at position " + position);
        holder.itemTitle.setText(routine.getName());
//        holder.routineDate.setText(String.format("%s - %s", routine.getFormattedStartDate(), routine.getFormattedEndDate()));
        // Format frequency using the string resource
        String formattedFrequency = String.format(holder.itemView.getContext().getString(R.string.placeholder_frequency), routine.getFrequency());
        holder.frequency.setText(formattedFrequency);
        // Show button only if it's an active routine
        holder.button.setVisibility(isActive ? View.VISIBLE : View.GONE);
        holder.button.setOnClickListener(v -> onRoutineClickListener.onRoutineClicked(routine));

        int imageResId;
        switch (routine.getTargetArea()) {
            case "NeckCervicalSpine":
                imageResId = R.drawable.img_neck;
                break;
            case "Shoulders":
                imageResId = R.drawable.img_shoulder;
                break;
            case "Thighs":
                imageResId = R.drawable.img_knees;
                break;
            case "LowerBackLumbarSpine":
                imageResId = R.drawable.img_lower_back;
                break;
            default:
                imageResId = R.drawable.img_lower_back;
        }
        holder.heroImage.setImageResource(imageResId);
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTitle, routineDate, frequency;
        public Button button;
        public ImageView heroImage;

        public ViewHolder(View view) {
            super(view);
            itemTitle = view.findViewById(R.id.tvItemTitle);
            frequency = view.findViewById(R.id.tvFrequency);
//            routineDate = view.findViewById(R.id.tvRoutineDate);
            button = view.findViewById(R.id.btnStartRoutine);
            heroImage = view.findViewById(R.id.ivHero);
        }
    }

    public interface OnRoutineClickListener {
        void onRoutineClicked(Routine routine);
    }
}

