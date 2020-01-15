package com.rohan.earthquaketracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rohan.earthquaketracker.aac.MainViewModel;
import com.rohan.earthquaketracker.adapters.EarthquakesAdapter;
import com.rohan.earthquaketracker.misc.SpeedyLinearLayoutManager;
import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.repo.LocationRepository;
import com.rohan.earthquaketracker.repo.MainRepository;
import com.rohan.earthquaketracker.utils.SortingUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EarthquakesAdapter.EarthquakeClickInterface,
        AdapterView.OnItemSelectedListener {
    EarthquakesAdapter mAdapter;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private AnimatedVectorDrawable upToDown;
    private AnimatedVectorDrawable downToUp;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupList();

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getEarthquakes().observe(this, earthquakes -> mAdapter.submitList(Arrays.asList(earthquakes)));
        viewModel.getDownloading().observe(this, downloading -> mSwipeRefreshLayout.setRefreshing(downloading));
        viewModel.getLimit().observe(this, limit -> MainRepository.downloadEarthquakes(getApplicationContext()));
        viewModel.getCurrentDownloadSort().observe(this, currentDownloadSort -> MainRepository.downloadEarthquakes(getApplicationContext()));

        LocationRepository.initializeLocation(this);

        setupBottomSheet();

        if (savedInstanceState == null) {
            MainRepository.downloadEarthquakes(this);
        }
    }

    private void setupBottomSheet() {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        setupSpinner(R.id.spinner_limit, R.array.limit_string_array);
        setupSpinner(R.id.spinner_download_sort, R.array.sort_string_array);
        setupSpinner(R.id.spinner_local_sort, R.array.sort_string_array);

        setupUpDownButton();

        // TODO: Be default, it should use the last limit value and not the first every time.
        //  Eg. If the user previously choose 300 as the limit, the next time the app loads, it must use 300 and not 100
    }

    private void setupUpDownButton() {
        ImageView ivUpDown = findViewById(R.id.iv_up_down);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            upToDown = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_up_to_down);
            downToUp = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_down_to_up);

            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    AnimatedVectorDrawable drawable = null;
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        drawable = upToDown;
                    } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        drawable = downToUp;
                    }
                    if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        ivUpDown.setImageDrawable(drawable);
                        drawable.start();
                    }
                }

                @Override
                public void onSlide(@NonNull View view, float v) {
                }
            });
        } else {
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        ivUpDown.setImageResource(R.drawable.ic_arrow_up);
                    } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        ivUpDown.setImageResource(R.drawable.ic_arrow_down);
                    }
                }

                @Override
                public void onSlide(@NonNull View view, float v) {
                }
            });
        }

        ivUpDown.setOnClickListener(v -> {
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private void setupSpinner(int spinnerResId, int spinnerArrayResId) {
        Spinner spinner = findViewById(spinnerResId);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, spinnerArrayResId, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
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
        rvEarthquakes.setLayoutManager(new SpeedyLinearLayoutManager(this));
        rvEarthquakes.setHasFixedSize(true);
        rvEarthquakes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        FastScroller fastScroller = findViewById(R.id.fastscroll);
        fastScroller.setRecyclerView(rvEarthquakes);

        ImageButton scrollToTop = findViewById(R.id.btn_scroll_to_top);
        scrollToTop.setOnClickListener(v -> {
            SpeedyLinearLayoutManager layoutManager = (SpeedyLinearLayoutManager) rvEarthquakes.getLayoutManager();
            layoutManager.smoothScrollToPositionVariableSpeed(rvEarthquakes, mAdapter.getCurrentPosition(), 0);
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> MainRepository.downloadEarthquakes(getApplicationContext()));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.primary);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_limit:
                String limitString = parent.getItemAtPosition(position).toString();
                int limit = Integer.parseInt(limitString);
                MainRepository.changeLimit(limit);
                break;

            case R.id.spinner_local_sort:
                SortingUtils.setCurrentLocalSort(position);
                Earthquake[] earthquakes = SortingUtils.sort(MainRepository.getEarthquakes().getValue());
                MainRepository.getEarthquakes().postValue(earthquakes);
                break;

            case R.id.spinner_download_sort:
                MainRepository.changeCurrentDownloadSort(position);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}