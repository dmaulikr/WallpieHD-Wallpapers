package com.fe.wallpie.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fe.wallpie.R;
import com.fe.wallpie.model.user.RecommendationResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Farmaan-PC on 28-01-2017.
 */

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {

    List<RecommendationResponse> mRecommendationResponses;
    LayoutInflater mInflater;
    OnItemClickListener mOnItemClickListener;

    public RecommendationAdapter(List<RecommendationResponse> recommendationResponses, Context context, OnItemClickListener onItemClickListener) {
        Collections.reverse(recommendationResponses);
        mRecommendationResponses = recommendationResponses ;
        mInflater = LayoutInflater.from(context);
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecommendationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.wallpaper_recomendation, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecommendationViewHolder holder, int position) {
        RecommendationResponse recommendationResponse = mRecommendationResponses.get(position);
        holder.bind(recommendationResponse, mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mRecommendationResponses.size();
    }
    public class RecommendationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.wallpaper_recomdation)
        public ImageView mWallpaperRecommendation;
        public RecommendationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(RecommendationResponse recommendationResponse, OnItemClickListener onItemClickListener) {
            Glide.with(itemView.getContext())
                    .load(recommendationResponse.getUrls().getSmall())
                    .placeholder(R.drawable.wallpaper_placeholder)
                    .thumbnail(0.1f)
                    .into(mWallpaperRecommendation);
            itemView.setOnClickListener(v -> {onItemClickListener.onItemClick(recommendationResponse,RecommendationViewHolder.this);});
        }
    }

    public interface OnItemClickListener{
        void onItemClick(RecommendationResponse recommendationResponse,RecommendationViewHolder recomedationViewHolder);
    }
}
