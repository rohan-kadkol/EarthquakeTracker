package com.rohan.earthquaketracker.utils;

import android.content.Context;
import android.util.Log;

import com.rohan.earthquaketracker.R;
import com.rohan.earthquaketracker.pojos.Earthquake;

public final class SortingUtils {
    private static final String TAG = SortingUtils.class.getSimpleName();

    public static final int RECENT_FIRST = 0;
    public static final int OLDEST_FIRST = 1;
    public static final int LOWEST_MAGNITUDE_FIRST = 2;
    public static final int HIGHEST_MAGNITUDE_FIRST = 3;

    public static int sCurrentLocalSort;

    private SortingUtils() {
    }

    private static Earthquake[] quickSort(Earthquake[] earthquakes, int sortCode) {
        Earthquake[] newEarthquakes = earthquakes.clone();
        quickSort(newEarthquakes, 0, earthquakes.length - 1, sortCode);
        return newEarthquakes;
    }

    private static void quickSort(Earthquake[] earthquakes, int low, int high, int sortCode) {
        if (high - low <= 0) {
            return;
        }

        int pivot = partition(earthquakes, low, high, sortCode);
        quickSort(earthquakes, low, pivot - 1, sortCode);
        quickSort(earthquakes, pivot + 1, high, sortCode);
    }

    private static int partition(Earthquake[] arr, int low, int high, int sortCode) {
        int pivot = high;
        int i = low - 1;
        int j = low;

        while (j <= high - 1) {
            if (sortCode == LOWEST_MAGNITUDE_FIRST || sortCode == HIGHEST_MAGNITUDE_FIRST) {
                if (arr[j].getProperties().getMagnitude() < arr[pivot].getProperties().getMagnitude()) {
                    swap(++i, j, arr);
                }
            } else if (sortCode == OLDEST_FIRST || sortCode == RECENT_FIRST) {
                if (arr[j].getProperties().getTime() < arr[pivot].getProperties().getTime()) {
                    swap(++i, j, arr);
                }
            }

            j++;
        }

        swap(i + 1, pivot, arr);
        return i + 1;
    }

    private static <E> void swap(int i, int j, E[] arr) {
        E temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static <E> E[] reverseArray(E[] arr) {
        E[] newArr = arr.clone();
        int start = 0;
        int end = arr.length - 1;
        while (start < end) {
            swap(start++, end--, newArr);
        }
        return newArr;
    }

    public static Earthquake[] sort(Earthquake[] earthquakes) {
        if (earthquakes == null) {
            Log.d(TAG, "sortAndUpdate: earthquakes passed is null");
            return new Earthquake[] {};
        }

        Earthquake[] newEarthquakes = new Earthquake[0];
        if (sCurrentLocalSort == SortingUtils.LOWEST_MAGNITUDE_FIRST || sCurrentLocalSort == SortingUtils.OLDEST_FIRST) {
            newEarthquakes = SortingUtils.quickSort(earthquakes, sCurrentLocalSort);
        } else if (sCurrentLocalSort == SortingUtils.HIGHEST_MAGNITUDE_FIRST || sCurrentLocalSort == SortingUtils.RECENT_FIRST) {
            newEarthquakes = SortingUtils.quickSort(earthquakes, sCurrentLocalSort);
            newEarthquakes = SortingUtils.reverseArray(newEarthquakes);
        }
        return newEarthquakes;
    }

    public static void setCurrentLocalSort(int currentLocalSort) {
        sCurrentLocalSort = currentLocalSort;
    }

    public static String getOrderBy(Integer sortCode, Context context) {
        if (sortCode == RECENT_FIRST) {
            return context.getString(R.string.recent_first_order_by);
        } else if (sortCode == OLDEST_FIRST) {
            return context.getString(R.string.oldest_first_order_by);
        } else if (sortCode == HIGHEST_MAGNITUDE_FIRST) {
            return context.getString(R.string.highest_magnitude_first_order_by);
        } else if (sortCode == LOWEST_MAGNITUDE_FIRST) {
            return context.getString(R.string.lowest_magnitude_first_order_by);
        } else {
            return null;
        }
    }
}
