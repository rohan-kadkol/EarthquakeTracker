package com.rohan.earthquaketracker.utils;

import com.rohan.earthquaketracker.pojos.Earthquake;

import java.util.List;

public final class StatePersistenceUtils {
    private static List<Earthquake> sEarthquakes;

    private StatePersistenceUtils() {}

    public static List<Earthquake> getEarthquakes() {
        return sEarthquakes;
    }

    public static void setEarthquakes(List<Earthquake> earthquakes) {
        sEarthquakes = earthquakes;
    }
}
