package com.rohan.earthquaketracker.misc;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class SpeedyLinearLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 25;
    private int mDistance = 0;

    public SpeedyLinearLayoutManager(Context context) {
        super(context);
    }

    public void smoothScrollToPositionVariableSpeed(RecyclerView recyclerView, int currentPosition, int finalPosition) {
        mDistance = Math.abs(finalPosition - currentPosition);
        if (mDistance > 50) {
            recyclerView.scrollToPosition(0);
        } else {
            float difference = 200 - mDistance;
            if (difference <= 0) { // So factor will never be 0. Hence 0 is never returned. Returning non-positive gives error
                difference = 0.0001f;
            }
            float factor = difference / 200;
            recyclerView.smoothScrollToPosition(finalPosition);
        }
        recyclerView.smoothScrollToPosition(finalPosition);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return super.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                float difference = 100 - mDistance;
                if (difference <= 0) {
                    return Math.abs(Float.MIN_VALUE);
                }
                float factor = difference / 100;
                float answer = factor * MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                return answer; // Returning <= 0 gives an error
            }
        };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
