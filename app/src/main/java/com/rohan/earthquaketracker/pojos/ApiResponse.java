package com.rohan.earthquaketracker.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("features")
    private List<Earthquake> mEarthquakes;

    public List<Earthquake> getEarthquakes() {
        return mEarthquakes;
    }
}
