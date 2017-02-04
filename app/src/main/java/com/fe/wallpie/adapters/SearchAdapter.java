package com.fe.wallpie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.fe.wallpie.R;
import com.fe.wallpie.activity.PhotographerActivity;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.model.search.Result;
import com.fe.wallpie.model.search.SearchResponse;
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
 * Created by Farmaan-PC on 04-02-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    List<Result> mSearchResponses;
    LayoutInflater mInflater;
    OnItemClickListener mOnItemClickListener;

    public SearchAdapter(Context context, List<Result> searchResponses, OnItemClickListener onItemClickListener) {
        mSearchResponses = searchResponses;
        mInflater = LayoutInflater.from(context);
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.wallpaper_item_detailed, parent, false);
        return new SearchViewHolder(view);
    }


    public void addItems(List<Result> searchResponses) {
        mSearchResponses.addAll(searchResponses);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Result searchResponse = mSearchResponses.get(position);
        try {
            holder.bind(searchResponse, mOnItemClickListener);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mSearchResponses.size();
    }

    public void clear() {
        mSearchResponses.clear();
        notifyDataSetChanged();
    }

    static public class SearchViewHolder extends RecyclerView.ViewHolder {

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

        public SearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Result searchResponse, OnItemClickListener onItemClickListener) {

            mPhotographerName.setText(searchResponse.getUser().getName());
            mPhotographerName.setOnClickListener(v -> {
                Intent intent = PhotographerActivity.createIntent(itemView.getContext(),
                        searchResponse.getUser().getUsername(),
                        searchResponse.getUser().getProfileImage().getMedium(),
                        searchResponse.getUser().getName());
                itemView.getContext().startActivity(intent);

            });
            mPhotogtapherUserName.setText(searchResponse.getUser().getUsername());
            Glide.with(itemView.getContext())
                    .load(searchResponse.getUrls().getRegular())
                    .placeholder(R.drawable.wallpaper_placeholder)
                    .thumbnail(0.1f)
                    .into(mWallpaper);

            Glide.with(itemView.getContext())
                    .load(searchResponse.getUser().getProfileImage().getMedium())
                    .into(mProfilePic);


            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                if (FirebaseDatabase.getInstance().getReference("favorite").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                    FirebaseDatabase.getInstance().getReference("favorite").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                            addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(searchResponse.getId())) {
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
                                        child(searchResponse.getId()).setValue(searchResponse);
                            } else {
                                Snackbar.make(itemView, itemView.getContext().getString(R.string.login_to_fav), Snackbar.LENGTH_SHORT).show();
                                mWallapaperLike.setChecked(false);
                            }

                        } else {
                            if (FirebaseDatabase.getInstance().getReference("favorite").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                                FirebaseDatabase.getInstance().getReference("favorite").
                                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                        child(searchResponse.getId()).removeValue();
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
                    onItemClickListener.onItemClick(searchResponse, SearchViewHolder.this);
                }
            });
            mWallapaperShare.setOnClickListener(v -> {shareImage(searchResponse);});

        }
        private void shareImage(Result wallpapersResponse) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome wallpapper : "+wallpapersResponse.getLinks().getHtml());
            sendIntent.setType("text/plain");
            itemView.getContext().startActivity(sendIntent);
        }

    }



    public interface OnItemClickListener {
        void onItemClick(Result searchResponse, SearchAdapter.SearchViewHolder searchViewHolder);
    }
}



