package com.fe.wallpie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fe.wallpie.R;
import com.fe.wallpie.adapters.CollectionImagesAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.listener.EndlessRecyclerViewScrollListener;
import com.fe.wallpie.model.collection.CollectionImages;
import com.fe.wallpie.model.photos.ProfileImage;
import com.fe.wallpie.model.photos.Urls;
import com.fe.wallpie.model.photos.User;
import com.fe.wallpie.model.photos.WallpapersResponse;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


public class PhotosActivity extends BaseActivity {

    private static final String COLLECTION_ID = "collection_id";
    private static final int MAX_ITEMS_PER_REQUEST = 30;
    private static final String COLLECTION_NAME = "collection_name";
    private static final String BUNDLE_RECYLER_VIEW = "bundle_rv";
    CollectionImagesAdapter mCollectionImagesAdapter;


    @BindView(R.id.collection_images)
    RecyclerView mCollectionImagesRecylerView;
    WallpaperProvider mWallpaperProvider;
    Disposable mDisposableCollectionImagesInitial;
    Disposable mDisposableCollectionImagesFollowing;
    String mCollectionId;
    int page;
    @BindView(R.id.progress_bar_loading)
    ProgressBar mProgressBar;
    private String mCOllectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);
        mCollectionId = getIntent().getStringExtra(COLLECTION_ID);
        mCOllectionName = getIntent().getStringExtra(COLLECTION_NAME);
        setUpToolbar();
        setUpSeachView();
        mWallpaperProvider = new WallpaperProvider(Wallpie.getDesiredMinimumHeight(), Wallpie.getDesiredMinimumWidth());
        mCollectionImagesRecylerView.setLayoutManager(new LinearLayoutManager(this));
        page = 1;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDisposableCollectionImagesInitial = mWallpaperProvider.getCollectionImage(mCollectionId, MAX_ITEMS_PER_REQUEST, 1)
                .subscribe(collectionImages -> {
                            populateImages(collectionImages);
                        },
                        throwable -> {
                            Log.d(PhotosActivity.class.getName(), "onStart: " + throwable.getMessage());
                        });


    }

    private void setUpSeachView() {
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchViewListener(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCollectionImagesRecylerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(BUNDLE_RECYLER_VIEW));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYLER_VIEW, mCollectionImagesRecylerView.getLayoutManager().onSaveInstanceState());
    }

    private void populateImages(List<CollectionImages> collectionImages) {
        mProgressBar.setVisibility(View.GONE);
        mCollectionImagesRecylerView.setVisibility(View.VISIBLE);
        mCollectionImagesAdapter = new CollectionImagesAdapter(this, collectionImages, new CollectionImagesAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(CollectionImages collectionImages, CollectionImagesAdapter.ColectionImagesViewholder colectionImagesViewholder) {
                startActivity(DetailActivity.createIntent(PhotosActivity.this, collectionToWallpaperResponse(collectionImages)));
            }
        });

        mCollectionImagesRecylerView.setAdapter(mCollectionImagesAdapter);
        mCollectionImagesRecylerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mCollectionImagesRecylerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mDisposableCollectionImagesFollowing = mWallpaperProvider.getCollectionImage(mCollectionId, MAX_ITEMS_PER_REQUEST, page)
                        .subscribe(collectionImages1 -> {
                                    mCollectionImagesAdapter.addCollectionImages(collectionImages1);
                                    mCollectionImagesAdapter.notifyItemRangeInserted((page - 1) * MAX_ITEMS_PER_REQUEST, MAX_ITEMS_PER_REQUEST);

                                },
                                throwable -> {
                                    handleError(throwable);
                                });
            }
        });

    }

    private WallpapersResponse collectionToWallpaperResponse(CollectionImages collectionImages) {
        WallpapersResponse wallpapersResponse = new WallpapersResponse();
        Urls urls = new Urls();
        User user = new User();
        user.setUsername(collectionImages.getUser().getUsername());
        user.setName(collectionImages.getUser().getName());
        user.setUsername(collectionImages.getUser().getUsername());
        user.setFirstName(collectionImages.getUser().getFirstName());
        user.setBio(collectionImages.getUser().getBio());
        urls.setRegular(collectionImages.getUrls().getRegular());
        ProfileImage profileImage = new ProfileImage();
        profileImage.setMedium(collectionImages.getUser().getProfileImage().getMedium());
        user.setProfileImage(profileImage);
        wallpapersResponse.setUrls(urls);
        wallpapersResponse.setUser(user);
        wallpapersResponse.setId(collectionImages.getId());
        return wallpapersResponse;
    }

    public static Intent ceateIntent(Context context, String id, String name) {
        Intent intent = new Intent(context, PhotosActivity.class);
        intent.putExtra(COLLECTION_ID, id);
        intent.putExtra(COLLECTION_NAME, name);
        return intent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDisposableCollectionImagesInitial != null && !mDisposableCollectionImagesInitial.isDisposed()) {
            mDisposableCollectionImagesInitial.dispose();
        }
        if (mDisposableCollectionImagesFollowing != null && !mDisposableCollectionImagesFollowing.isDisposed()) {
            mDisposableCollectionImagesFollowing.dispose();
        }
    }

    public void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            snackBarResult(getString(R.string.no_internet_connection));
        } else if (throwable instanceof IllegalStateException) {
            snackBarResult(getString(R.string.conversion_error));
        } else {

            snackBarResult(String.valueOf(throwable.getLocalizedMessage()));
        }
    }

    private void snackBarResult(String msg) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mCollectionImagesRecylerView, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void setUpToolbar() {
        super.setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(mCOllectionName);
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
