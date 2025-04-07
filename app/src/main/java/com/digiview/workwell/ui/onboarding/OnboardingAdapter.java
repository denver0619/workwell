package com.digiview.workwell.ui.onboarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.digiview.workwell.R;
import com.digiview.workwell.data.models.OnboardingItem;
import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.ViewHolder> {

    private List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingAdapter.ViewHolder holder, int position) {
        OnboardingItem item = onboardingItems.get(position);
        holder.imageView.setImageResource(item.getImageResId());

        // Only show the title on the first page
        if (position == 0 && item.getTitle() != null && !item.getTitle().isEmpty()) {
            holder.titleView.setVisibility(View.VISIBLE);
            holder.titleView.setText(item.getTitle());
        } else {
            holder.titleView.setVisibility(View.GONE);
        }
        holder.descriptionView.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageOnboarding);
            titleView = itemView.findViewById(R.id.textTitle);
            descriptionView = itemView.findViewById(R.id.textDescription);
        }
    }
}
