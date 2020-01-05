package com.rohan.earthquaketracker.utils;

import android.content.Context;
import android.content.res.Resources;

import com.rohan.earthquaketracker.R;
import com.rohan.earthquaketracker.pojos.Earthquake;

public final class SortingUtils {
    private SortingUtils() {
    }

    public static Earthquake[] quickSort(Earthquake[] earthquakes, String sort, Context context) {
        Earthquake[] newEarthquakes = earthquakes.clone();
        quickSort(newEarthquakes, 0, earthquakes.length - 1, sort, context);
        return newEarthquakes;
    }

    private static void quickSort(Earthquake[] earthquakes, int low, int high, String sort, Context context) {
        if (high - low <= 0) {
            return;
        }

        int pivot = partition(earthquakes, low, high, sort, context);
        quickSort(earthquakes, low, pivot - 1, sort, context);
        quickSort(earthquakes, pivot + 1, high, sort, context);
    }

    private static int partition(Earthquake[] arr, int low, int high, String sort, Context context) {
        Resources res = context.getResources();
        int pivot = high;
        int i = low - 1;
        int j = low;

        while (j <= high - 1) {
            if (sort.equals(res.getString(R.string.lowest_magnitude_first)) ||
                    sort.equals(res.getString(R.string.highest_magnitude_first))) {
                if (arr[j].getProperties().getMagnitude() < arr[pivot].getProperties().getMagnitude()) {
                    swap(++i, j, arr);
                }
            } else if (sort.equals(res.getString(R.string.oldest_first)) ||
                    sort.equals(res.getString(R.string.recent_first))) {
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

    public static <E> E[] reverseArray(E[] arr) {
        E[] newArr = arr.clone();
        int start = 0;
        int end = arr.length - 1;
        while (start < end) {
            swap(start++, end--, newArr);
        }
        return newArr;
    }
}
