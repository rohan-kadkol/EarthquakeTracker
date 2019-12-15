package com.rohan.earthquaketracker.utils;

import android.net.Uri;

public final class UriUtils {
    private static final String HTTPS = "https";
    private static final String AUTHORITY = "maps.googleapis.com";
    private static final String PATH = "maps/api/staticmap";

    private static final String SIZE = "size";
    private static final String MARKERS = "markers";
    private static final String COLOR = "color";
    private static final String API_KEY = "key";
    private static final String LABEL = "label";

    private UriUtils() {
    }

    public static String getMapUrl(double lat, double lng, Double userLat, Double userLng) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(HTTPS)
                .encodedAuthority(AUTHORITY)
                .appendEncodedPath(PATH)
                .appendQueryParameter(SIZE, "620x620")
                .appendQueryParameter(MARKERS, COLOR + ":red|" + LABEL + ":E|" + lat + "," + lng);
        if (userLat != null || userLng != null) {
            builder.appendQueryParameter(MARKERS, COLOR + ":red|" + userLat + "," + userLng);
        }
        builder.appendQueryParameter(API_KEY, "AIzaSyBfsYcofQkfV4yHpeMpB2XJihJDBhM-Mjw");
        return builder.toString();
    }
}
