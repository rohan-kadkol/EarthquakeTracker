package com.rohan.earthquaketracker.pojos;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class Earthquake implements Parcelable {
    @SerializedName("properties")
    private EProperties mProperties;

    @SerializedName("geometry")
    private EGeometry mGeometry;

    @SerializedName("id")
    private String mId;

    protected Earthquake(Parcel in) {
        mProperties = in.readParcelable(EProperties.class.getClassLoader());
        mGeometry = in.readParcelable(EGeometry.class.getClassLoader());
        mId = in.readString();
    }

    public static final Creator<Earthquake> CREATOR = new Creator<Earthquake>() {
        @Override
        public Earthquake createFromParcel(Parcel in) {
            return new Earthquake(in);
        }

        @Override
        public Earthquake[] newArray(int size) {
            return new Earthquake[size];
        }
    };

    public EProperties getProperties() {
        return mProperties;
    }

    public EGeometry getGeometry() {
        return mGeometry;
    }

    public String getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mProperties, i);
        parcel.writeParcelable(mGeometry, i);
        parcel.writeString(mId);
    }
}

// TODO: Remove the scrim field if you're not going to use the scrim in the item root background.
