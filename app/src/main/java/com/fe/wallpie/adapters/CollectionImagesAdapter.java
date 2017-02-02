package com.fe.wallpie.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.fe.wallpie.activity.DetailActivity;
import com.fe.wallpie.activity.PhotographerActivity;
import com.fe.wallpie.model.collection.CollectionImages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Farmaan-PC on 30-01-2017.
 */

public class CollectionImagesAdapter extends RecyclerView.Adapter<CollectionImagesAdapter.ColectionImagesViewholder> {

    List<CollectionImages> mCollectionImages;
    LayoutInflater mInflater;
    OnItemClickListner mOnItemClickListner;

    public CollectionImagesAdapter(Context context, List<CollectionImages> collectionImages, CollectionImagesAdapter.OnItemClickListner onItemClickListner) {
        mCollectionImages = collectionImages;
        mInflater = LayoutInflater.from(context);
        mOnItemClickListner = onItemClickListner;
    }

    public void addCollectionImages(List<CollectionImages> collectionImages) {
        mCollectionImages.addAll(collectionImages);
    }

    @Override
    public CollectionImagesAdapter.ColectionImagesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wallpaper_item_detailed, parent, false);
        return new ColectionImagesViewholder(view);
    }

    @Override
    public void onBindViewHolder(CollectionImagesAdapter.ColectionImagesViewholder holder, int position) {
        CollectionImages collectionImages = mCollectionImages.get(position);
        try {
            holder.bind(collectionImages, mOnItemClickListner);
        } catch (Exception e) {
            Log.d(CollectionImagesAdapter.class.getName(), "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mCollectionImages.size();
    }

    public class ColectionImagesViewholder extends RecyclerView.ViewHolder {

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

        public ColectionImagesViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CollectionImages collectionImages, OnItemClickListner onItemClickListner) {

            mPhotographerName.setText(collectionImages.getUser().getName());
            mPhotographerName.setOnClickListener(v -> {
                Intent intent = PhotographerActivity.createIntent(itemView.getContext(),
                        collectionImages.getUser().getUsername(),
                        collectionImages.getUser().getProfileImage().getMedium(),
                        collectionImages.getUser().getName());
                itemView.getContext().startActivity(intent);
            });
            mPhotogtapherUserName.setText(collectionImages.getUser().getUsername());
            Glide.with(itemView.getContext())
                    .load(collectionImages.getUrls().getRegular())
                    .placeholder(R.drawable.wallpaper_placeholder)
                    .thumbnail(0.1f)
                    .into(mWallpaper);

            Glide.with(itemView.getContext())
                    .load(collectionImages.getUser().getProfileImage().getMedium())
                    .into(mProfilePic);


            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (FirebaseDatabase.getInstance().getReference("favorite").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                    FirebaseDatabase.getInstance().getReference("favorite").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(collectionImages.getId())) {
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
                                        child(collectionImages.getId()).setValue(collectionImages);
                            } else {
                                Snackbar.make(itemView, itemView.getContext().getString(R.string.login_to_fav), Snackbar.LENGTH_SHORT).show();
                                mWallapaperLike.setChecked(false);
                            }

                        } else {
                            if (FirebaseDatabase.getInstance().getReference("favorite").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                                FirebaseDatabase.getInstance().getReference("favorite").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                        child(collectionImages.getId()).removeValue();
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
                    onItemClickListner.onItemClick(collectionImages, CollectionImagesAdapter.ColectionImagesViewholder.this);
                }
            });

        }
    }

    public interface OnItemClickListner {
        void onItemClick(CollectionImages collectionImages, CollectionImagesAdapter.ColectionImagesViewholder colectionImagesViewholder);
    }
}

