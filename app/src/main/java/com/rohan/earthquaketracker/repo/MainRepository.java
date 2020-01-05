package com.rohan.earthquaketracker.repo;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.rohan.earthquaketracker.R;
import com.rohan.earthquaketracker.pojos.ApiResponse;
import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.retrofit.ApiMethods;
import com.rohan.earthquaketracker.retrofit.RetrofitClientInstance;
import com.rohan.earthquaketracker.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    private static MutableLiveData<Earthquake[]> sEarthquakes;
    private static MutableLiveData<Boolean> sDownloading;

    private static MutableLiveData<Integer> sLimit;

    static {
        sEarthquakes = new MutableLiveData<>();
        sEarthquakes.postValue(new Earthquake[] {});
        sDownloading = new MutableLiveData<>();
        sDownloading.postValue(false);
        sLimit = new MutableLiveData<>();
    }

    public static void downloadEarthquakes(Context context, int limit) {
        sDownloading.postValue(true);

        if (sLimit.getValue() == null || limit != sLimit.getValue()) {
            sLimit.postValue(limit);
        }

        ApiMethods apiMethods = RetrofitClientInstance.getRetrofitInstance().create(ApiMethods.class);

        // Cancel all previous requests
        String[] limits = context.getResources().getStringArray(R.array.limit_string_array);
        for (String limitString : limits) {
            int limitTemp = Integer.parseInt(limitString);
            apiMethods.getEarthquakes(limitTemp).cancel();
        }

        apiMethods.getEarthquakes(limit).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                sDownloading.postValue(false);
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
//                List<Earthquake> earthquakes = response.body().getEarthquakes();
                Earthquake[] earthquakes = response.body().getEarthquakes();
                ColorUtils.setBackgroundAndScrim(earthquakes);
                sEarthquakes.postValue(earthquakes);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                sDownloading.postValue(false);
            }
        });
    }

    public static MutableLiveData<Earthquake[]> getEarthquakes() {
        return sEarthquakes;
    }

    public static MutableLiveData<Boolean> getDownloading() {
        return sDownloading;
    }

    public static MutableLiveData<Integer> getLimit() {
        return sLimit;
    }

    public static void changeLimit(int limit) {
        sLimit.postValue(limit);
    }

    // TODO: Prevent memory leak. Eg. When the call is in progress and the app is closed.
    //  Will the repository automatically be destroyed and call cancelled?
//    public static void stopDownloading() {
//        ApiMethods apiMethods = RetrofitClientInstance.getRetrofitInstance().create(ApiMethods.class);
//        apiMethods.getEarthquakes(20).cancel();
//    }
}
