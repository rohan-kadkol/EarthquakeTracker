package com.rohan.earthquaketracker.aac;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.repo.MainRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Earthquake>> mEarthquakes;
    private LiveData<Boolean> mDownloading;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mEarthquakes = MainRepository.getEarthquakes();
        mDownloading = MainRepository.getDownloading();
    }

    public LiveData<List<Earthquake>> getEarthquakes() {
        return mEarthquakes;
    }

    public LiveData<Boolean> getDownloading() {
        return mDownloading;
    }
}
