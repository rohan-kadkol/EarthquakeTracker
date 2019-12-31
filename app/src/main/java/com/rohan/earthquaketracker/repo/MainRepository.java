package com.rohan.earthquaketracker.repo;

import androidx.lifecycle.MutableLiveData;

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
    private static MutableLiveData<List<Earthquake>> sEarthquakes;
    private static MutableLiveData<Boolean> sDownloading;

    static {
        sEarthquakes = new MutableLiveData<>();
        sEarthquakes.setValue(new ArrayList<>());
        sDownloading = new MutableLiveData<>();
        sDownloading.setValue(false);
    }

    public static void downloadEarthquakes() {
        sDownloading.setValue(true);

        ApiMethods apiMethods = RetrofitClientInstance.getRetrofitInstance().create(ApiMethods.class);
        apiMethods.getEarthquakes(200).cancel();
        apiMethods.getEarthquakes(200).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                sDownloading.setValue(false);
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                List<Earthquake> earthquakes = response.body().getEarthquakes();
                ColorUtils.setBackgroundAndScrim(earthquakes);
                sEarthquakes.setValue(earthquakes);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                sDownloading.setValue(false);
            }
        });
    }

    public static MutableLiveData<List<Earthquake>> getEarthquakes() {
        return sEarthquakes;
    }

    public static MutableLiveData<Boolean> getDownloading() {
        return sDownloading;
    }

    // TODO: Prevent memory leak. Eg. When the call is in progress and the app is closed.
    //  Will the repository automatically be destroyed and call cancelled?
//    public static void stopDownloading() {
//        ApiMethods apiMethods = RetrofitClientInstance.getRetrofitInstance().create(ApiMethods.class);
//        apiMethods.getEarthquakes(20).cancel();
//    }
}
