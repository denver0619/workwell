package com.digiview.workwell.data.models;

import java.util.List;

public class Constraint {
    private String ConstraintId;
    private int AlignedThreshold;
    private int RestingThreshold;
    private String RestingComparator;
    private List<String> Keypoints;

    public String getConstraintId() {
        return ConstraintId;
    }

    public void setConstraintId(String constraintId) {
        ConstraintId = constraintId;
    }

    public int getAlignedThreshold() {
        return AlignedThreshold;
    }

    public void setAlignedThreshold(int alignedThreshold) {
        AlignedThreshold = alignedThreshold;
    }

    public int getRestingThreshold() {
        return RestingThreshold;
    }

    public void setRestingThreshold(int restingThreshold) {
        RestingThreshold = restingThreshold;
    }

    public String getRestingComparator() {
        return RestingComparator;
    }

    public void setRestingComparator(String restingComparator) {
        RestingComparator = restingComparator;
    }

    public List<String> getKeypoints() {
        return Keypoints;
    }

    public void setKeypoints(List<String> keypoints) {
        Keypoints = keypoints;
    }
}
