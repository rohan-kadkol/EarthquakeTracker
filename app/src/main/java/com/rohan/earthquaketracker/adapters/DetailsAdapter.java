package com.rohan.earthquaketracker.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.rohan.earthquaketracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsAdapter extends ListAdapter<Pair<String, String>, DetailsAdapter.MainViewHolder> {
    private static final int VIEW_BODY = 0;
    private static final int VIEW_DETAILS = 1;

    private static final DiffUtil.ItemCallback<Pair<String, String>> DIFF_CALLBACK = new DiffUtil.ItemCallback<Pair<String, String>>() {
        @Override
        public boolean areItemsTheSame(@NonNull Pair<String, String> oldItem, @NonNull Pair<String, String> newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Pair<String, String> oldItem, @NonNull Pair<String, String> newItem) {
            assert oldItem.first != null;
            assert oldItem.second != null;
            return oldItem.first.equals(newItem.first) && oldItem.second.equals(newItem.second);
        }
    };

    public DetailsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_BODY) {
            View view = inflater.inflate(R.layout.item_body, parent, false);
            return new BodyViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_detail, parent,false);
            return new DetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (holder instanceof BodyViewHolder) {
            ((BodyViewHolder) holder).bind(position);
        } else if (holder instanceof DetailViewHolder){
            ((DetailViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_BODY;
        } else {
            return VIEW_DETAILS;
        }
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class BodyViewHolder extends MainViewHolder {
        @BindView(R.id.tv_body)
        TextView tvBody;

        public BodyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            Pair<String, String> pair = getItem(position);
            if (TextUtils.isEmpty(pair.second)) {
                tvBody.setVisibility(View.GONE);
            } else {
                tvBody.setVisibility(View.VISIBLE);
                tvBody.setText(pair.second);
            }
        }
    }

    class DetailViewHolder extends MainViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            Pair<String, String> pair = getItem(position);
            tvTitle.setText(pair.first);
            tvContent.setText(pair.second);
        }
    }
}
