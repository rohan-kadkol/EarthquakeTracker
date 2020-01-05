package com.rohan.earthquaketracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rohan.earthquaketracker.adapters.DetailsAdapter;
import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.repo.LocationRepository;
import com.rohan.earthquaketracker.utils.StringUtils;
import com.rohan.earthquaketracker.utils.UriUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EarthquakeDetail extends AppCompatActivity {
    private static final String TAG = EarthquakeDetail.class.getSimpleName();

    Earthquake mEarthquake;
    DetailsAdapter mAdapter;
    String mMapUrl;

    @BindView(R.id.tv_magnitude)
    TextView tvMagnitude;
    @BindView(R.id.tv_location_1)
    TextView tvLocation1;
    @BindView(R.id.tv_location_2)
    TextView tvLocation2;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_mag_background)
    ImageView ivMagBackground;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.v_map_background)
    View vMapBackground;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);

        ButterKnife.bind(this);

        mEarthquake = getIntent().getParcelableExtra(getString(R.string.earthquake));
        if (mEarthquake == null) {
            Log.d(TAG, "onCreate: Earthquake passed in the intent was null");
            return;
        }

        loadToolbar();
        loadDetails();
        loadMapImage();
    }

    private void loadMapImage() {
        double lat = mEarthquake.getGeometry().getCoordinates()[1];
        double lng = mEarthquake.getGeometry().getCoordinates()[0];
        Double userLat = LocationRepository.getLastKnownLat();
        Double userLng = LocationRepository.getLastKnownLng();

        mMapUrl = UriUtils.getMapUrl(lat, lng, userLat, userLng);
        Picasso.get().load(mMapUrl).into(ivMap);
    }

    private void loadDetails() {
        mAdapter = new DetailsAdapter();
        RecyclerView rvDetails = findViewById(R.id.rv_details);
        rvDetails.setAdapter(mAdapter);
        rvDetails.setLayoutManager(new LinearLayoutManager(this));
        rvDetails.setHasFixedSize(true);
        rvDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter.submitList(StringUtils.getDetailsList(this, mEarthquake));
    }

    private void loadToolbar() {
        String magnitude = StringUtils.getFormattedMagnitude(mEarthquake.getProperties().getMagnitude());
        String[] location = StringUtils.getFormattedLocation(mEarthquake.getProperties().getPlace());
        int magBackground = mEarthquake.getProperties().getMagnitudeBackground();
        int rootBackground = mEarthquake.getProperties().getRootBackground();
        String time = StringUtils.getFormattedDate(mEarthquake.getProperties().getTime() * 1000L);

        tvMagnitude.setText(magnitude);
        tvLocation1.setText(location[0]);
        tvLocation2.setText(location[1]);
        ivMagBackground.setColorFilter(ContextCompat.getColor(ivMagBackground.getContext(), magBackground));
        appBarLayout.setBackgroundColor(ContextCompat.getColor(ivMagBackground.getContext(), rootBackground));
        tvTime.setText(time);

        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @OnClick({R.id.app_bar_layout, R.id.collapsing_toolbar, R.id.iv_map, R.id.toolbar, R.id.layout_mag_loc})
    void onMapClicked() {
        Bundle bundle = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this,
                    new Pair<>(tvMagnitude, tvMagnitude.getTransitionName()),
                    new Pair<>(tvLocation1, tvLocation1.getTransitionName()),
                    new Pair<>(tvLocation2, tvLocation2.getTransitionName()),
                    new Pair<>(tvTime, tvTime.getTransitionName()),
                    new Pair<>(ivMagBackground, ivMagBackground.getTransitionName()),
                    new Pair<>(ivMap, ivMap.getTransitionName()),
                    new Pair<>(vMapBackground, vMapBackground.getTransitionName())).toBundle();
        }

        Intent intent = new Intent(this, MapImageActivity.class);
        intent.putExtra(getString(R.string.earthquake), mEarthquake);
        intent.putExtra(getString(R.string.map_image_url), mMapUrl);
        startActivity(intent, bundle);
    }
}

// TODO: Add a progress bar for the map image download