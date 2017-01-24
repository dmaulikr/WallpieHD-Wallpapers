package com.fe.wallpie.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Farmaan-PC on 24-01-2017.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    List<CollectionResponse> mCollectionResponses;
    LayoutInflater mInflater;

    public CollectionAdapter(List<CollectionResponse> collectionResponses, Context context) {
        mCollectionResponses = collectionResponses;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.collections_item, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CollectionViewHolder holder, int position) {
        CollectionResponse collectionResponse = mCollectionResponses.get(position);
        Glide.with(mInflater.getContext())
                .load(collectionResponse.getCoverPhoto().getUrls().getRegular())
                .placeholder(R.drawable.wallpaper_placeholder)
                .thumbnail(0.1f)
                .into(holder.mCollectionCoverImage);

        Glide.with(mInflater.getContext())
                .load(collectionResponse.getUser().getProfileImage().getMedium())
                .placeholder(R.drawable.wallpaper_placeholder)
                .thumbnail(0.1f)
                .into(holder.mCollectionCreaterImageView);
        holder.mCollectionCreaterName.setText(collectionResponse.getUser().getName());
        holder.mCollectionName.setText(collectionResponse.getTitle());

    }

    @Override
    public int getItemCount() {
        return mCollectionResponses.size();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.collection_cover_img)
        ImageView mCollectionCoverImage;
        @BindView(R.id.collection_creater_img)
        CircleImageView mCollectionCreaterImageView;
        @BindView(R.id.collection_name)
        TextView mCollectionName;
        @BindView(R.id.collection_creater_name)
        TextView mCollectionCreaterName;
        public CollectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
