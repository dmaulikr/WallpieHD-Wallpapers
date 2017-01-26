package com.fe.wallpie.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
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
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public PhotosAdapter(List<WallpapersResponse> wallpapersResponses, Context context) {
        mWallpapersResponses = wallpapersResponses;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addItems(List<WallpapersResponse> wallpapersResponses) {
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.wallpaper_item_detailed, parent, false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, int position) {
        WallpapersResponse wallpapersResponse = mWallpapersResponses.get(position);
        holder.mPhotographerName.setText(wallpapersResponse.getUser().getName());
        holder.mPhotogtapherUserName.setText(wallpapersResponse.getUser().getUsername());
        Glide.with(mLayoutInflater.getContext())
                .load(wallpapersResponse.getUrls().getRegular())
                .placeholder(R.drawable.wallpaper_placeholder)
                .thumbnail(0.1f)
                .into(holder.mWallpaper);
        Log.d("Farmaan", "Completed" + position);

        Glide.with(mLayoutInflater.getContext())
                .load(wallpapersResponse.getUser().getProfileImage().getMedium())
                .thumbnail(0.1f)
                .into(holder.mProfilePic);

        if (Wallpie.getFavRef() != null) {
            Wallpie.getFavRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(wallpapersResponse.getId())) {
                        holder.mWallapaperLike.setChecked(true);
                    } else {
                        holder.mWallapaperLike.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        holder.mWallapaperLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Wallpie.getFavRef() != null){
                        Wallpie.getFavRef().child(wallpapersResponse.getId()).setValue(wallpapersResponse);
                    }else {
                        Snackbar.make(holder.itemView, mLayoutInflater.getContext().getString(R.string.login_to_fav), Snackbar.LENGTH_SHORT).show();
                        holder.mWallapaperLike.setChecked(false);
                    }

                } else {
                    if (Wallpie.getFavRef() != null) {
                        Wallpie.getFavRef().child(wallpapersResponse.getId()).removeValue();
                    }
                }
            }
        });


        holder.mWallapaperShare.setImageResource(R.drawable.ic_share_grey);

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
        ImageView mWallpaper;
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
    }
}
