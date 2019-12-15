package com.rohan.earthquaketracker.repo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

public class LocationRepository {
    private static Location sLastKnownLocation;

    public static final int LOCATION_REQUEST_CODE = 0;

    public static void initializeLocation(Context context) {
        if (sLastKnownLocation != null) {
            return;
        }

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Activity activity = (Activity) context;
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
                return;
            }
        }
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LocationRepository.setLastKnownLocation(lastLocation);
    }

    public static Location getLastKnownLocation() {
        return sLastKnownLocation;
    }

    public static Double getLastKnownLat() {
        if (sLastKnownLocation == null) return null;
        return sLastKnownLocation.getLatitude();
    }

    public static Double getLastKnownLng() {
        if (sLastKnownLocation == null) return null;
        return sLastKnownLocation.getLongitude();
    }

    public static void setLastKnownLocation(Location lastKnownLocation) {
        sLastKnownLocation = lastKnownLocation;
    }
}
