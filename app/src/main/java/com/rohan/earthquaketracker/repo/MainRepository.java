package com.rohan.earthquaketracker.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.rohan.earthquaketracker.R;
import com.rohan.earthquaketracker.pojos.ApiResponse;
import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.retrofit.ApiMethods;
import com.rohan.earthquaketracker.retrofit.RetrofitClientInstance;
import com.rohan.earthquaketracker.utils.ColorUtils;
import com.rohan.earthquaketracker.utils.SortingUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainRepository {
    private static final String TAG = MainRepository.class.getSimpleName();

    private static MutableLiveData<Earthquake[]> sEarthquakes;
    private static MutableLiveData<Boolean> sDownloading;

    private static MutableLiveData<Integer> sLimit;
    private static MutableLiveData<Integer> sCurrentDownloadSort;

    static {
        sEarthquakes = new MutableLiveData<>();
        sEarthquakes.setValue(new Earthquake[] {});
        sDownloading = new MutableLiveData<>();
        sDownloading.setValue(false);
        sLimit = new MutableLiveData<>();
        sLimit.setValue(100);
        sCurrentDownloadSort = new MutableLiveData<>();
        sCurrentDownloadSort.setValue(SortingUtils.RECENT_FIRST); //TODO: Use SharedPreferences
    }

    public static void downloadEarthquakes(Context context) {
        sDownloading.postValue(true);

        ApiMethods apiMethods = RetrofitClientInstance.getRetrofitInstance().create(ApiMethods.class);

        // Cancel all previous requests
        String[] limits = context.getResources().getStringArray(R.array.limit_string_array);
        for (String limitString : limits) {
            int limitTemp = Integer.parseInt(limitString);
            apiMethods.getEarthquakes(limitTemp).cancel();
        }

        String orderBy = SortingUtils.getOrderBy(sCurrentDownloadSort.getValue(), context);
        if (orderBy == null) {
            Log.d(TAG, "downloadEarthquakes: ");
            return;
        }

        apiMethods.getEarthquakes(sLimit.getValue(), orderBy).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                sDownloading.postValue(false);
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }
                Earthquake[] earthquakes = response.body().getEarthquakes();
                ColorUtils.setBackgroundAndScrim(earthquakes);
                earthquakes = SortingUtils.sort(earthquakes);
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

    public static MutableLiveData<Integer> getCurrentDownloadSort() {
        return sCurrentDownloadSort;
    }

    public static void changeLimit(int limit) {
        sLimit.postValue(limit);
    }

    public static void changeCurrentDownloadSort(int currentDownloadSort) {
        sCurrentDownloadSort.postValue(currentDownloadSort);
    }

    // TODO: Prevent memory leak. Eg. When the call is in progress and the app is closed.
    //  Will the repository automatically be destroyed and call cancelled?
//    public static void stopDownloading() {
//        ApiMethods apiMethods = RetrofitClientInstance.getRetrofitInstance().create(ApiMethods.class);
//        apiMethods.getEarthquakes(20).cancel();
//    }
}
