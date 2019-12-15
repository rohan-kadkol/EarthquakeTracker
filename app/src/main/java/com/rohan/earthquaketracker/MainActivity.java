package com.rohan.earthquaketracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.rohan.earthquaketracker.aac.MainViewModel;
import com.rohan.earthquaketracker.adapters.EarthquakesAdapter;
import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.repo.LocationRepository;
import com.rohan.earthquaketracker.repo.MainRepository;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EarthquakesAdapter.EarthquakeClickInterface {
    EarthquakesAdapter mAdapter;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupList();

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getEarthquakes().observe(this, earthquakes -> mAdapter.submitList(earthquakes));
        viewModel.getDownloading().observe(this, downloading -> mSwipeRefreshLayout.setRefreshing(downloading));

        LocationRepository.initializeLocation(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LocationRepository.LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    LocationRepository.setLastKnownLocation(lastLocation);
                }
            }
        }
    }

    private void setupList() {
        RecyclerView rvEarthquakes = findViewById(R.id.rv_earthquakes);
        mAdapter = new EarthquakesAdapter(this);
        rvEarthquakes.setAdapter(mAdapter);
        rvEarthquakes.setLayoutManager(new LinearLayoutManager(this));
        rvEarthquakes.setHasFixedSize(true);
        rvEarthquakes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mSwipeRefreshLayout.setOnRefreshListener(MainRepository::downloadEarthquakes);
    }

    @Override
    public void onClick(Earthquake earthquake, TextView tvMagnitude, TextView tvLocation1, TextView tvLocation2,
                        TextView tvTime, ImageView ivMagBackground) {
        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this,
                    new Pair<>(tvMagnitude, tvMagnitude.getTransitionName()),
                    new Pair<>(tvLocation1, tvLocation1.getTransitionName()),
                    new Pair<>(tvLocation2, tvLocation2.getTransitionName()),
                    new Pair<>(tvTime, tvTime.getTransitionName()),
                    new Pair<>(ivMagBackground, ivMagBackground.getTransitionName())).toBundle();
        }
        Intent intent = new Intent(this, EarthquakeDetail.class);
        intent.putExtra(getString(R.string.earthquake), earthquake);
        startActivity(intent, bundle);
    }
}