package com.rohan.earthquaketracker.adapters;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.rohan.earthquaketracker.R;
import com.rohan.earthquaketracker.pojos.Earthquake;
import com.rohan.earthquaketracker.utils.StringUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarthquakesAdapter extends ListAdapter<Earthquake, EarthquakesAdapter.ViewHolder> implements SectionTitleProvider {
    private EarthquakeClickInterface mClickInterface;
    private Context mContext;

    private int mCurrentPosition = 0;

    @Override
    public String getSectionTitle(int position) {
        double magnitude = getItem(position).getProperties().getMagnitude();
        return mContext.getString(R.string.magnitude_value, magnitude);
    }

    public interface EarthquakeClickInterface {
        void onClick(Earthquake earthquake, TextView tvMagnitude, TextView tvLocation1, TextView tvLocation2,
                     TextView tvTime, ImageView ivMagBackground);
    }

    private static final DiffUtil.ItemCallback<Earthquake> DIFF_CALLBACK = new DiffUtil.ItemCallback<Earthquake>() {
        @Override
        public boolean areItemsTheSame(@NonNull Earthquake oldItem, @NonNull Earthquake newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Earthquake oldItem, @NonNull Earthquake newItem) {
            return Arrays.equals(oldItem.getGeometry().getCoordinates(), newItem.getGeometry().getCoordinates())
                    && oldItem.getProperties().getMagnitude() == newItem.getProperties().getMagnitude()
                    && oldItem.getProperties().getPlace().equals(newItem.getProperties().getPlace())
                    && oldItem.getProperties().getSignificance() == newItem.getProperties().getSignificance()
                    && oldItem.getProperties().getTime() == newItem.getProperties().getTime();
        }
    };

    public EarthquakesAdapter(Context context) {
        super(DIFF_CALLBACK);
        mClickInterface = (EarthquakeClickInterface) context;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_earthquake, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCurrentPosition = position;
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_item_earthquake)
        View root;
        @BindView(R.id.tv_magnitude)
        TextView tvMagnitude;
        @BindView(R.id.tv_location_1)
        TextView tvLocation1;
        @BindView(R.id.tv_location_2)
        TextView tvLocation2;
        @BindView(R.id.tv_body)
        TextView tvBody;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_mag_background)
        ImageView ivMagBackground;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(
                    view -> mClickInterface.onClick(getItem(getAdapterPosition()), tvMagnitude,
                            tvLocation1, tvLocation2, tvTime, ivMagBackground));
            tvLocation2.setSelected(true);
        }

        void bind(int position) {
            Earthquake earthquake = getItem(position);
            if (earthquake == null) return;

            String magnitude = StringUtils.getFormattedMagnitude(earthquake.getProperties().getMagnitude());
            String[] location = StringUtils.getFormattedLocation(earthquake.getProperties().getPlace());
            int magBackgroundColorId = earthquake.getProperties().getMagnitudeBackground();
            int rootBackgroundColorId = earthquake.getProperties().getRootBackground();
            String time = StringUtils.getFormattedDate(earthquake.getProperties().getTime());

            tvMagnitude.setText(magnitude);
            tvLocation1.setText(location[0]);
            tvLocation2.setText(location[1]);
            root.setBackgroundColor(ContextCompat.getColor(ivMagBackground.getContext(), rootBackgroundColorId));
            ivMagBackground.setColorFilter(ContextCompat.getColor(ivMagBackground.getContext(), magBackgroundColorId));
            tvTime.setText(time);

            String body = StringUtils.getFormattedSubBody(earthquake);
            if (TextUtils.isEmpty(body)) {
                tvBody.setVisibility(View.GONE);
            } else {
                tvBody.setVisibility(View.VISIBLE);
                tvBody.setText(body);
            }
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }
}

// TODO (COMPLETED): Remove unused scrim code