package com.fe.wallpie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


public class PhotosActivity extends AppCompatActivity {

    private static final String COLLECTION_ID = "collection_id";
    private static final int MAX_ITEMS_PER_REQUEST = 30;
    CollectionImagesAdapter mCollectionImagesAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collection_images)
    RecyclerView mCollectionImagesRecylerView;
    WallpaperProvider mWallpaperProvider;
    Disposable mDisposableCollectionImagesInitial;
    Disposable mDisposableCollectionImagesFollowing;
    String mCollectionId;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mCollectionId = getIntent().getStringExtra(COLLECTION_ID);
        mWallpaperProvider = new WallpaperProvider(Wallpie.getDesiredMinimumHeight(), Wallpie.getDesiredMinimumWidth());
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

    private void populateImages(List<CollectionImages> collectionImages) {
        mCollectionImagesAdapter = new CollectionImagesAdapter(this, collectionImages, new CollectionImagesAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(CollectionImages collectionImages, CollectionImagesAdapter.ColectionImagesViewholder colectionImagesViewholder) {
                startActivity(DetailActivity.createIntent(PhotosActivity.this, collectionToWallpaperResponse(collectionImages)));
            }
        });
        mCollectionImagesRecylerView.setLayoutManager(new LinearLayoutManager(this));
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
                                    Log.d(PhotosActivity.class.getName(), "onLoadMore: " + throwable.getMessage());
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

    public static Intent ceateIntent(Context context, String id) {
        Intent intent = new Intent(context, PhotosActivity.class);
        intent.putExtra(COLLECTION_ID, id);
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
}
