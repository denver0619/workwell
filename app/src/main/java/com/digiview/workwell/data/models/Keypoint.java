package com.digiview.workwell.data.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Keypoint implements Serializable {
    private String KeypointId;
    private String Keypoint;
    private String SecondaryKeypoint;
    private boolean isMidpoint;

    public String getKeypointId() {
        return KeypointId;
    }

    public void setKeypointId(String keypointId) {
        KeypointId = keypointId;
    }

    public String getKeypoint() {
        return Keypoint;
    }

    public void setKeypoint(String keypoint) {
        Keypoint = keypoint;
    }

    public String getSecondaryKeypoint() {
        return SecondaryKeypoint;
    }

    public void setSecondaryKeypoint(String secondaryKeypoint) {
        SecondaryKeypoint = secondaryKeypoint;
    }

    @PropertyName("IsMidpoint")
    public boolean isMidpoint() {
        return isMidpoint;
    }

    @PropertyName("IsMidpoint")
    public void setMidpoint(boolean midpoint) {
        isMidpoint = midpoint;
    }
}
