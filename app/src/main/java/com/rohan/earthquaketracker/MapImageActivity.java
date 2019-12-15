package com.rohan.earthquaketracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.utils.StringUtils;
import com.rohan.earthquaketracker.utils.UriUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapImageActivity extends AppCompatActivity {
    private static final String TAG = MapImageActivity.class.getSimpleName();

    Earthquake mEarthquake;
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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_image);

        ButterKnife.bind(this);

        mMapUrl = getIntent().getStringExtra(getString(R.string.map_image_url));
        Picasso.get().load(mMapUrl).into(ivMap);

        mEarthquake = getIntent().getParcelableExtra(getString(R.string.earthquake));
        if (mEarthquake == null) {
            Log.d(TAG, "onCreate: Earthquake passed in the intent was null");
            return;
        }

        loadToolbar();
    }

    private void loadToolbar() {
        String magnitude = StringUtils.getFormattedMagnitude(mEarthquake.getProperties().getMagnitude());
        String[] location = StringUtils.getFormattedLocation(mEarthquake.getProperties().getPlace());
        int magBackground = mEarthquake.getProperties().getMagnitudeBackground();
        String time = StringUtils.getFormattedDate(mEarthquake.getProperties().getTime() * 1000L);

        tvMagnitude.setText(magnitude);
        tvLocation1.setText(location[0]);
        tvLocation2.setText(location[1]);
        ivMagBackground.setColorFilter(ContextCompat.getColor(ivMagBackground.getContext(), magBackground));
        tvTime.setText(time);

        toolbar.setNavigationOnClickListener(view -> finish());
    }
}
