package com.fe.wallpie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fe.wallpie.R;
import com.fe.wallpie.adapters.RecommendationAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.photos.ProfileImage;
import com.fe.wallpie.model.photos.Urls;
import com.fe.wallpie.model.photos.User;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.model.user.RecommendationResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;

public class PhotographerActivity extends AppCompatActivity {

    private static final String PHOTOGRAPHER_NAME_EXTRA = "photographer_name";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.photographer_bg)
    ImageView mImageView;
    @BindView(R.id.photographer_dp)
    CircleImageView mCircleImageView;
    @BindView(R.id.recycler_view_photographer)
    RecyclerView mRecyclerViewPhotographer;
    private static final String PHOTOGRAPHER_USERNAME_EXTRA = "photographer_username";
    private static final String PHOTOGRAPHER_DP_EXTRA = "photographer_dp";
    String mUserName;
    String mProfilePicUrl;
    WallpaperProvider mWallpaperProvider ;
    Disposable mPhotographerPictureInitial;
    Disposable mPhotographerPictureFollowing;
    private static final int MAX_ITEMS_PER_REQUEST = 30;
    RecommendationAdapter mPhotosAdapter;
    String mPhotographerName;
    @BindView(R.id.collapsingtoolbar_photographer_activty)
    CollapsingToolbarLayout mCollapsingToolbarLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer);

        ButterKnife.bind(this);
        setUpToolbar();
        mUserName = getIntent().getStringExtra(PHOTOGRAPHER_USERNAME_EXTRA);
        mProfilePicUrl = getIntent().getStringExtra(PHOTOGRAPHER_DP_EXTRA);
        mPhotographerName=getIntent().getStringExtra(PHOTOGRAPHER_NAME_EXTRA);
        mCollapsingToolbarLayout.setTitle(mPhotographerName);
        mWallpaperProvider = new WallpaperProvider(Wallpie.getDesiredMinimumHeight(), Wallpie.getDesiredMinimumWidth());

        Glide.with(this).load(mProfilePicUrl).into(mCircleImageView);
        Glide.with(this).load(getString(R.string.photographer_bg)).into(mImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPhotographerPictureInitial = mWallpaperProvider.getRecommendation(mUserName, MAX_ITEMS_PER_REQUEST, 1)
                .subscribe(recommendationResponses -> {
                            populatePhotographerRecyclerView(recommendationResponses);
                        },
                        throwable -> handleError(throwable));

    }

    private void populatePhotographerRecyclerView(List<RecommendationResponse> recommendationResponses) {
        mRecyclerViewPhotographer.setVisibility(View.VISIBLE);
        mPhotosAdapter = new RecommendationAdapter(recommendationResponses, this,
                (recommendationResponse, recomedationViewHolder) -> {
                    Intent intent = DetailActivity.createIntent(PhotographerActivity.this, recommendationToWallpaperResponse(recommendationResponse));
                    startActivity(intent);
                });
        mRecyclerViewPhotographer.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerViewPhotographer.setAdapter(mPhotosAdapter);

    }

    public static Intent createIntent(Context context, String username,String profilePicUrl,String name) {
        Intent intent = new Intent(context, PhotographerActivity.class);
        intent.putExtra(PHOTOGRAPHER_USERNAME_EXTRA, username);
        intent.putExtra(PHOTOGRAPHER_DP_EXTRA, profilePicUrl);
        intent.putExtra(PHOTOGRAPHER_NAME_EXTRA, name);
        return intent;
    }
    private WallpapersResponse recommendationToWallpaperResponse(RecommendationResponse recommendationResponse) {
        WallpapersResponse wallpapersResponse = new WallpapersResponse();
        Urls urls = new Urls();
        User user = new User();
        user.setUsername(recommendationResponse.getUser().getUsername());
        user.setName(recommendationResponse.getUser().getName());
        user.setUsername(recommendationResponse.getUser().getUsername());
        user.setFirstName(recommendationResponse.getUser().getFirstName());
        user.setBio(recommendationResponse.getUser().getBio());
        urls.setRegular(recommendationResponse.getUrls().getRegular());
        ProfileImage profileImage = new ProfileImage();
        profileImage.setMedium(recommendationResponse.getUser().getProfileImage().getMedium());
        user.setProfileImage(profileImage);
        wallpapersResponse.setUrls(urls);
        wallpapersResponse.setUser(user);
        wallpapersResponse.setId(recommendationResponse.getId());
        return wallpapersResponse;
    }
    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private List<WallpapersResponse> convertAllToWallpaperResponse(List<RecommendationResponse> recommendationResponses) {
        List<WallpapersResponse> wallpapersResponses = new ArrayList<>();
        for (RecommendationResponse response :
                recommendationResponses) {
            wallpapersResponses.add(recommendationToWallpaperResponse(response));
        }
        return wallpapersResponses;
    }

    public void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            snackBarResult(getString(R.string.no_internet_connection));
        }
        else if (throwable instanceof IllegalStateException) {
            snackBarResult(getString(R.string.conversion_error));
        } else {

            snackBarResult(String.valueOf(throwable.getLocalizedMessage()));
        }
    }

    private void snackBarResult(String msg) {
        Snackbar.make(mRecyclerViewPhotographer,msg,Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
