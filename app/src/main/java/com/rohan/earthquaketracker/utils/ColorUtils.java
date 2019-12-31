package com.rohan.earthquaketracker.utils;

import com.rohan.earthquaketracker.R;
import com.rohan.earthquaketracker.pojos.Earthquake;

import java.util.List;

public final class ColorUtils {
    private ColorUtils() {}

    public static void setBackgroundAndScrim(List<Earthquake> earthquakes) {
        for (Earthquake earthquake : earthquakes) {
            double magnitude = earthquake.getProperties().getMagnitude();
            earthquake.getProperties().setMagnitudeBackground(getMagBackgroundColor(magnitude));
            earthquake.getProperties().setScrim(getMagScrimColor(magnitude));
            earthquake.getProperties().setRootBackground(getRootBackgroundColor(magnitude));
        }
    }

    public static int getMagBackgroundColor(double magnitude) {
        int colorResId;
        int mag = (int) Math.floor(magnitude);
        switch (mag) {
            case -1:
            case 0:
                colorResId = R.color.magnitude_0;
                break;
            case 1:
                colorResId = R.color.magnitude_1;
                break;
            case 2:
                colorResId = R.color.magnitude_2;
                break;
            case 3:
                colorResId = R.color.magnitude_3;
                break;
            case 4:
                colorResId = R.color.magnitude_4;
                break;
            case 5:
                colorResId = R.color.magnitude_5;
                break;
            case 6:
                colorResId = R.color.magnitude_6;
                break;
            case 7:
                colorResId = R.color.magnitude_7;
                break;
            case 8:
                colorResId = R.color.magnitude_8;
                break;
            case 9:
                colorResId = R.color.magnitude_9;
                break;
            default:
                colorResId = R.color.magnitude_10;
        }
        return colorResId;
    }

    public static int getMagScrimColor(double magnitude) {
        int scrimResId;
        int mag = (int) Math.floor(magnitude);
        switch (mag) {
            case -1:
            case 0:
                scrimResId = R.drawable.scrim_0;
                break;
            case 1:
                scrimResId = R.drawable.scrim_1;
                break;
            case 2:
                scrimResId = R.drawable.scrim_2;
                break;
            case 3:
                scrimResId = R.drawable.scrim_3;
                break;
            case 4:
                scrimResId = R.drawable.scrim_4;
                break;
            case 5:
                scrimResId = R.drawable.scrim_5;
                break;
            case 6:
                scrimResId = R.drawable.scrim_6;
                break;
            case 7:
                scrimResId = R.drawable.scrim_7;
                break;
            case 8:
                scrimResId = R.drawable.scrim_8;
                break;
            case 9:
                scrimResId = R.drawable.scrim_9;
                break;
            default:
                scrimResId = R.drawable.scrim_10;
        }
        return scrimResId;
    }

    public static int getRootBackgroundColor(double magnitude) {
        int colorResId;
        int mag = (int) Math.floor(magnitude);
        switch (mag) {
            case -1:
            case 0:
                colorResId = R.color.magnitude_0_scrim;
                break;
            case 1:
                colorResId = R.color.magnitude_1_scrim;
                break;
            case 2:
                colorResId = R.color.magnitude_2_scrim;
                break;
            case 3:
                colorResId = R.color.magnitude_3_scrim;
                break;
            case 4:
                colorResId = R.color.magnitude_4_scrim;
                break;
            case 5:
                colorResId = R.color.magnitude_5_scrim;
                break;
            case 6:
                colorResId = R.color.magnitude_6_scrim;
                break;
            case 7:
                colorResId = R.color.magnitude_7_scrim;
                break;
            case 8:
                colorResId = R.color.magnitude_8_scrim;
                break;
            case 9:
                colorResId = R.color.magnitude_9_scrim;
                break;
            default:
                colorResId = R.color.magnitude_10_scrim;
        }
        return colorResId;
    }
}
