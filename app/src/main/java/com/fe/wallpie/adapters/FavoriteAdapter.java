package com.fe.wallpie.adapters;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.fe.wallpie.R;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Farmaan-PC on 26-01-2017.
 */

public class FavoriteAdapter extends FirebaseRecyclerAdapter<WallpapersResponse, FavoriteAdapter.FavoriteViewHolder> {


    /**
     * @param modelClass      Firebase will marshall the data at a location into
     * an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     * You will be responsible for populating an instance of the corresponding
     * view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     * using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */

    FavoriteAdapter.OnItemClickListener mItemClickListener;

    public FavoriteAdapter(Class<WallpapersResponse> modelClass, int modelLayout, Class<FavoriteViewHolder> viewHolderClass, Query ref, OnItemClickListener onItemClickListener) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mItemClickListener = onItemClickListener;
    }

    @Override
    protected void populateViewHolder(FavoriteViewHolder holder, WallpapersResponse wallpapersResponse, int position) {
        try {
            holder.bind(wallpapersResponse, mItemClickListener);
        } catch (Throwable throwable) {

            Log.d(FavoriteAdapter.class.getName(), throwable.getMessage());
        }


    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photograpger_name)
        TextView mPhotographerName;
        @BindView(R.id.photograpger_username)
        TextView mPhotogtapherUserName;
        @BindView(R.id.wallpaper)
        public ImageView mWallpaper;
        @BindView(R.id.photographer_prof_pic)
        CircleImageView mProfilePic;
        @BindView(R.id.iv_like)
        ToggleButton mWallapaperLike;
        @BindView(R.id.iv_share)
        ImageView mWallapaperShare;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(WallpapersResponse wallpapersResponse, OnItemClickListener itemClickListener) {

            mPhotographerName.setText(wallpapersResponse.getUser().getName());
            mPhotogtapherUserName.setText(wallpapersResponse.getUser().getUsername());
            Glide.with(Wallpie.getInstance())
                    .load(wallpapersResponse.getUrls().getRegular())
                    .placeholder(R.drawable.wallpaper_placeholder)
                    .thumbnail(0.1f)
                    .into(mWallpaper);

            Glide.with(Wallpie.getInstance())
                    .load(wallpapersResponse.getUser().getProfileImage().getMedium())
                    .into(mProfilePic);

            if (FirebaseDatabase.getInstance().getReference("favorite").child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                FirebaseDatabase.getInstance().getReference("favorite").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(wallpapersResponse.getId())) {
                            mWallapaperLike.setChecked(true);
                        } else {
                            mWallapaperLike.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            mWallapaperLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (FirebaseDatabase.getInstance().getReference("favorite").child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                            FirebaseDatabase.getInstance().getReference("favorite").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(wallpapersResponse.getId()).setValue(wallpapersResponse);
                        } else {
                            Snackbar.make(itemView, Wallpie.getInstance().getString(R.string.login_to_fav), Snackbar.LENGTH_SHORT).show();
                            mWallapaperLike.setChecked(false);
                        }

                    } else {
                        if (FirebaseDatabase.getInstance().getReference("favorite").child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                            FirebaseDatabase.getInstance().getReference("favorite").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(wallpapersResponse.getId()).removeValue();
                        }
                    }
                }
            });


            mWallapaperShare.setImageResource(R.drawable.ic_share_grey);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(wallpapersResponse, FavoriteViewHolder.this);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(WallpapersResponse wallpapersResponse, FavoriteViewHolder holder);
    }
}
