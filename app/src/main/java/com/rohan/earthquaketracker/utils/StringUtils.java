package com.rohan.earthquaketracker.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.util.Pair;

import com.rohan.earthquaketracker.R;
import com.rohan.earthquaketracker.pojos.Earthquake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class StringUtils {
    private StringUtils() {
    }

    public static String getFormattedMagnitude(double magnitude) {
        return String.format(Locale.getDefault(), "%.1f", magnitude);
    }

    public static String[] getFormattedLocation(String location) { // TODO: Multilingual Support
        int firstCharAscii = location.charAt(0);
        // First char is a number
        if (firstCharAscii >= 48 && firstCharAscii <= 57) {
            //First of is divider
            String[] locations = location.split(" of ", 2);
            locations[0] += " of";
            return locations;
        }
        // If the location is in the form of "Near Island of Phuket
        else if (location.substring(0, 5).equalsIgnoreCase("near ")) {
            return new String[]{"Near ", location.substring(5)};
        }
        // If the location only contains the place, eg. "Island of Phuket";
        else {
            return new String[]{"Near ", location};
        }
    }

    public static String getFormattedSubBody(Earthquake earthquake, Context context) {
        double latitude = earthquake.getGeometry().getCoordinates()[1];
        double longitude = earthquake.getGeometry().getCoordinates()[0];
        int distance = LocationUtils.findDistanceToUser(latitude, longitude); // TODO: Create helper class that returns distance from given coordinates to user's location
        int felt = earthquake.getProperties().getFelt();
        int tsunami = earthquake.getProperties().getTsunami();

        boolean firstLinePrinted = false;
        StringBuilder builder = new StringBuilder();
        if (distance != -1) {
            firstLinePrinted = true;
            String unit = context.getString(R.string.unit_mi); //TODO: Use the user's preferred unit
            builder.append(distance).append(context.getString(R.string.distance, distance, unit));
        }
        if (felt > 0) {
            if (firstLinePrinted) {
                builder.append("\n");
            }
            firstLinePrinted = true;
            String feltString = context.getResources().getQuantityString(R.plurals.content_felt_string, felt, felt);
            builder.append(feltString);
        }
        if (tsunami > 0) {
            if (firstLinePrinted) {
                builder.append("\n");
            }
            builder.append(context.getString(R.string.tsunami_reported));
        }

        return builder.toString();
    }

    public static String getFormattedDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy - hh:mm aa", Locale.getDefault());
        formatter.setTimeZone(Calendar.getInstance().getTimeZone());
        return formatter.format(date);
    }

    public static String getFormattedMainBody(Earthquake earthquake, Context context) {
        double latitude = earthquake.getGeometry().getCoordinates()[1];
        double longitude = earthquake.getGeometry().getCoordinates()[0];
        int distance = LocationUtils.findDistanceToUser(latitude, longitude);
        int tsunami = earthquake.getProperties().getTsunami();
        String status = earthquake.getProperties().getStatus();
        long updated = earthquake.getProperties().getUpdated();

        boolean firstLinePrinted = false;
        StringBuilder builder = new StringBuilder();
        if (distance != -1) {
            firstLinePrinted = true;
            String unit = context.getString(R.string.unit_mi); //TODO: Use the user's preferred unit
            builder.append(distance).append(context.getString(R.string.distance, distance, unit));
        }
        if (tsunami == 1) {
            if (firstLinePrinted) {
                builder.append("\n");
            }
            firstLinePrinted = true;
            builder.append(context.getString(R.string.tsunami_reported));
        }

        if (firstLinePrinted) {
            builder.append("\n\n");
        }

        if (!TextUtils.isEmpty(status)) {
            if (firstLinePrinted) {
                builder.append("\n");
            }
            firstLinePrinted = true;
            if (status.equals("automatic")) {
                builder.append(context.getString(R.string.reviewed_by_system));
            } else {
                builder.append(context.getString(R.string.reviewed_by_human));
            }
        }

        if (firstLinePrinted) {
            builder.append("\n");
        }
        builder.append("Last updated: ").append(getFormattedDate(updated));

        return builder.toString();
    }

    public static List<Pair<String, String>> getDetailsList(Context context, Earthquake earthquake) {
        ArrayList<Pair<String, String>> details = new ArrayList<>();
        String title;
        String content;

        title = null;
        content = getFormattedMainBody(earthquake, context);
        details.add(new Pair<>(title, content));

        // Alert Color
        // TODO: Change textColor of the text to match the color it shows
        // TODO: Add a drop down menu to show additional information of the alert level. eg: damage, etc. (https://earthquake.usgs.gov/data/pager/background.php)
        // TODO: Proper capitalization of color. eg. "Green" and not "green"
        title = context.getString(R.string.title_alert);
        content = getFormattedAlert(context, earthquake.getProperties().getAlert());
        details.add(new Pair<>(title, content));

        // Significance
        title = context.getString(R.string.title_significance);
        content = getFormattedSignificance(context, earthquake.getProperties().getSignificance());
        details.add(new Pair<>(title, content));

        // Felt
        title = context.getString(R.string.title_felt);
        content = getFormattedFelt(context, earthquake.getProperties().getFelt());
        details.add(new Pair<>(title, content));

        // Maximum Reported Intensity (cdi)
        title = context.getString(R.string.title_cdi_reported);
        content = getFormattedCdiMmi(context, earthquake.getProperties().getCdiReported());
        details.add(new Pair<>(title, content));

        // Depth (in km)
        title = context.getString(R.string.title_depth);
        content = getFormattedDepth(context, earthquake.getGeometry().getCoordinates()[2]);
        details.add(new Pair<>(title, content));

        // Latitude [-90, 90]
        title = context.getString(R.string.title_latitude);
        content = String.valueOf(earthquake.getGeometry().getCoordinates()[1]);
        details.add(new Pair<>(title, content));

        // Longitude [-180, 180]
        title = context.getString(R.string.title_longitude);
        content = String.valueOf(earthquake.getGeometry().getCoordinates()[0]);
        details.add(new Pair<>(title, content));

        // Maximum estimated intensity (mmi) [0.0, 10.0]
        title = context.getString(R.string.title_mmi_estimated);
        content = getFormattedCdiMmi(context, earthquake.getProperties().getMmiEstimated());
        details.add(new Pair<>(title, content));

        // Number of seismic stations used to calculate location (nst)
        title = context.getString(R.string.title_nst);
        content = getFormattedNstStations(context, earthquake.getProperties().getNstStationsForLoc());
        details.add(new Pair<>(title, content));

        return details;
    }

    public static String getFormattedSignificance(Context context, int significance) {
        if (significance == 0) {
            return context.getString(R.string.content_null);
        }
        return context.getString(R.string.content_significance, significance / 10.0);
    }

    public static String getFormattedCdiMmi(Context context, double cdi) {
        if (cdi == 0) {
            return context.getString(R.string.content_null);
        }
        return context.getString(R.string.content_cdi_reported, cdi);
    }

    public static String getFormattedDepth(Context context, double depth) {
        // TODO: Units support
        if (depth == 0) {
            return context.getString(R.string.content_null);
        }
        return context.getString(R.string.content_depth, (int) depth);
    }

    public static String getFormattedAlert(Context context, String alert) {
        if (TextUtils.isEmpty(alert)) {
            return context.getString(R.string.content_null);
        }
        return alert;
        //TODO: Correct case. ie. "Green" instead of "green"
    }

    public static String getFormattedFelt(Context context, int felt) {
        if (felt == 0) {
            return context.getString(R.string.content_null);
        }
        return context.getResources().getQuantityString(R.plurals.content_felt, felt, felt);
    }

    public static String getFormattedNstStations(Context context, int nst) {
        if (nst == 0) {
            return context.getString(R.string.content_null);
        }
        return context.getResources().getQuantityString(R.plurals.content_nst_stations, nst, nst);
    }

    // TODO: Make a small view to the right of location 1 on the right of the screen that shows the significance
    // TODO: Change the above hard-coded strings to make it multi-lingual
}
