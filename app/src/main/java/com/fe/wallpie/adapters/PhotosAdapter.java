package com.fe.wallpie.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.fe.wallpie.R;
import com.fe.wallpie.activity.DetailActivity;
import com.fe.wallpie.activity.PhotographerActivity;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.parcellable.WallpaperParcel;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.IDN;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Farmaan-PC on 22-01-2017.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    List<WallpapersResponse> mWallpapersResponses;
    LayoutInflater mLayoutInflater;
    OnItemClickListener mItemClickListener;

    public PhotosAdapter(List<WallpapersResponse> wallpapersResponses, Activity activity,OnItemClickListener onItemClickListener) {
        mWallpapersResponses = wallpapersResponses;
        mLayoutInflater = LayoutInflater.from(activity);
        mItemClickListener = onItemClickListener;
    }

    public void addItems(List<WallpapersResponse> wallpapersResponses) {
        mWallpapersResponses.addAll(wallpapersResponses);
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.wallpaper_item_detailed, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, int position) {
        WallpapersResponse wallpapersResponse = mWallpapersResponses.get(position);
        try {
            holder.onBind(wallpapersResponse, mItemClickListener);
        } catch (Exception e) {
            Log.d("PhotosAdapter", e.getMessage());
        }

    }



    @Override
    public int getItemCount() {
        return mWallpapersResponses.size();
    }

    static public class PhotosViewHolder extends RecyclerView.ViewHolder {
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

        public PhotosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void onBind(final WallpapersResponse wallpapersResponse,final OnItemClickListener onItemClickListener) {
            mPhotographerName.setText(wallpapersResponse.getUser().getName());
            mPhotographerName.setOnClickListener(v -> {
                Intent intent=PhotographerActivity.createIntent(itemView.getContext(),
                        wallpapersResponse.getUser().getUsername(),
                        wallpapersResponse.getUser().getProfileImage().getMedium(),
                        wallpapersResponse.getUser().getName());
                itemView.getContext().startActivity(intent);

            });
            mPhotogtapherUserName.setText(wallpapersResponse.getUser().getUsername());
            Glide.with(itemView.getContext())
                    .load(wallpapersResponse.getUrls().getRegular())
                    .placeholder(R.drawable.wallpaper_placeholder)
                    .thumbnail(0.1f)
                    .into(mWallpaper);

            Glide.with(itemView.getContext())
                    .load(wallpapersResponse.getUser().getProfileImage().getMedium())
                    .into(mProfilePic);



            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (FirebaseDatabase.getInstance().getReference("favorite").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                    FirebaseDatabase.getInstance().getReference("favorite").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            addListenerForSingleValueEvent(new ValueEventListener() {
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
                            if (FirebaseDatabase.getInstance().getReference("favorite").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                                FirebaseDatabase.getInstance().getReference("favorite").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                        child(wallpapersResponse.getId()).setValue(wallpapersResponse);
                            } else {
                                Snackbar.make(itemView, itemView.getContext().getString(R.string.login_to_fav), Snackbar.LENGTH_SHORT).show();
                                mWallapaperLike.setChecked(false);
                            }

                        } else {
                            if (FirebaseDatabase.getInstance().getReference("favorite").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                                FirebaseDatabase.getInstance().getReference("favorite").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                        child(wallpapersResponse.getId()).removeValue();
                            }
                        }
                    }
                });
            } else {
                mWallapaperLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mWallapaperLike.setChecked(false);
                        }
                        Snackbar.make(itemView,
                                itemView.getContext().getString(R.string.login_to_fav),
                                Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            mWallapaperShare.setImageResource(R.drawable.ic_share_grey);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(wallpapersResponse, PhotosViewHolder.this);
                }
            });
            mWallapaperShare.setOnClickListener(v -> {shareImage(wallpapersResponse);});

        }

        private void shareImage(WallpapersResponse wallpapersResponse) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome wallpapper : "+wallpapersResponse.getLinks().getHtml());
            sendIntent.setType("text/plain");
            itemView.getContext().startActivity(sendIntent);
        }
    }

    public interface  OnItemClickListener{
        void onItemClick(WallpapersResponse wallpapersResponse,PhotosViewHolder photosViewHolder);
    }
}
