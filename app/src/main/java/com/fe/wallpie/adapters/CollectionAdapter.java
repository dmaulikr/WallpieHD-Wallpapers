package com.fe.wallpie.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fe.wallpie.R;
import com.fe.wallpie.model.collections.CollectionResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Farmaan-PC on 24-01-2017.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    List<CollectionResponse> mCollectionResponses;
    LayoutInflater mInflater;
    OnItemClickListener mItemClickListener;

    public CollectionAdapter(List<CollectionResponse> collectionResponses, Context context, OnItemClickListener itemClickListener) {
        mCollectionResponses = collectionResponses;
        mInflater = LayoutInflater.from(context);
        mItemClickListener = itemClickListener;
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.collections_item, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        CollectionResponse collectionResponse = mCollectionResponses.get(position);
        holder.bindItems(collectionResponse, mItemClickListener);

    }

    @Override
    public int getItemCount() {
        return mCollectionResponses.size();
    }

    public void addItems(List<CollectionResponse> collectionResponses) {
        mCollectionResponses.addAll(collectionResponses);
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.collection_cover_img)
        ImageView mCollectionCoverImage;
        @BindView(R.id.collection_name)
        TextView mCollectionName;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItems(CollectionResponse collectionResponse, OnItemClickListener itemClickListener) {
            Glide.with(itemView.getContext())
                    .load(collectionResponse.getCoverPhoto().getUrls().getRegular())
                    .placeholder(R.drawable.wallpaper_placeholder)
                    .thumbnail(0.1f)
                    .into(mCollectionCoverImage);


            mCollectionCoverImage.setContentDescription(collectionResponse.getTitle());
            mCollectionName.setText(collectionResponse.getTitle());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(collectionResponse, CollectionViewHolder.this);
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(CollectionResponse collectionResponse, CollectionViewHolder collectionViewHolder);
    }
}
