package com.rohan.earthquaketracker.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EProperties implements Parcelable {
    @SerializedName("mag")
    private double magnitude;

    transient private Integer magnitudeBackground;
    transient private Integer scrim;

    private String place;

    @SerializedName("sig")
    private int significance;

    private int felt;

    private long time;

    private int tsunami;

    private double magError;

    private String alert;

    @SerializedName("cdi")
    private double cdiReported;

    @SerializedName("mmi")
    private double mmiEstimated;

    @SerializedName("ids")
    private String relatedEventsIds;

    private String status;

    @SerializedName("nst")
    private int nstStationsForLoc;

    private long updated;

    private String url;

    protected EProperties(Parcel in) {
        magnitude = in.readDouble();
        magnitudeBackground = in.readInt();
        scrim = in.readInt();
        place = in.readString();
        significance = in.readInt();
        felt = in.readInt();
        time = in.readLong();
        tsunami = in.readInt();
        magError = in.readDouble();
        alert = in.readString();
        cdiReported = in.readDouble();
        mmiEstimated = in.readDouble();
        relatedEventsIds = in.readString();
        status = in.readString();
        nstStationsForLoc = in.readInt();
        updated = in.readLong();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(magnitude);
        dest.writeInt(magnitudeBackground);
        dest.writeInt(scrim);
        dest.writeString(place);
        dest.writeInt(significance);
        dest.writeInt(felt);
        dest.writeLong(time);
        dest.writeInt(tsunami);
        dest.writeDouble(magError);
        dest.writeString(alert);
        dest.writeDouble(cdiReported);
        dest.writeDouble(mmiEstimated);
        dest.writeString(relatedEventsIds);
        dest.writeString(status);
        dest.writeInt(nstStationsForLoc);
        dest.writeLong(updated);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EProperties> CREATOR = new Creator<EProperties>() {
        @Override
        public EProperties createFromParcel(Parcel in) {
            return new EProperties(in);
        }

        @Override
        public EProperties[] newArray(int size) {
            return new EProperties[size];
        }
    };

    public double getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public int getSignificance() {
        return significance;
    }

    public int getFelt() {
        return felt;
    }

    public long getTime() {
        return time;
    }

    public int getTsunami() {
        return tsunami;
    }

    public double getMagError() {
        return magError;
    }

    public String getRelatedEventsIds() {
        return relatedEventsIds;
    }

    public Integer getMagnitudeBackground() {
        return magnitudeBackground;
    }

    public Integer getScrim() {
        return scrim;
    }

    public void setMagnitudeBackground(Integer magnitudeBackground) {
        this.magnitudeBackground = magnitudeBackground;
    }

    public void setScrim(Integer scrim) {
        this.scrim = scrim;
    }

    public String getAlert() {
        return alert;
    }

    public double getCdiReported() {
        return cdiReported;
    }

    public double getMmiEstimated() {
        return mmiEstimated;
    }

    public String getStatus() {
        return status;
    }

    public int getNstStationsForLoc() {
        return nstStationsForLoc;
    }

    public long getUpdated() {
        return updated;
    }

    public String getUrl() {
        return url;
    }
}
