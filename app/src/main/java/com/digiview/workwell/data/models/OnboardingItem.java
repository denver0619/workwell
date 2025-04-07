package com.digiview.workwell.data.models;

public class OnboardingItem {
    private int imageResId;
    private String description;

    public OnboardingItem(int imageResId, String description) {
        this.imageResId = imageResId;
        this.description = description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getDescription() {
        return description;
    }
}
