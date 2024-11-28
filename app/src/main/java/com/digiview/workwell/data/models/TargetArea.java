package com.digiview.workwell.data.models;

public enum TargetArea {
    NECK(0),
    SHOULDER(1),
    LOWER_BACK(2),
    THIGH(23);

    private final int value;

    TargetArea(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TargetArea fromValue(long value) {
        for (TargetArea area : TargetArea.values()) {
            if (area.value == value) {
                return area;
            }
        }
        throw new IllegalArgumentException("Invalid value for TargetArea: " + value);
    }
}
