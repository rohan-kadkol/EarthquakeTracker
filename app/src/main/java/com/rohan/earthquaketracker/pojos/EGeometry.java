package com.rohan.earthquaketracker.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EGeometry implements Parcelable {
    @SerializedName("coordinates")
    private double[] mCoordinates;

    protected EGeometry(Parcel in) {
        mCoordinates = in.createDoubleArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(mCoordinates);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EGeometry> CREATOR = new Creator<EGeometry>() {
        @Override
        public EGeometry createFromParcel(Parcel in) {
            return new EGeometry(in);
        }

        @Override
        public EGeometry[] newArray(int size) {
            return new EGeometry[size];
        }
    };

    public double[] getCoordinates() {
        return mCoordinates;
    }
}
