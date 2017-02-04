package com.fe.wallpie.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fe.wallpie.R;
import com.fe.wallpie.adapters.PhotosAdapter;
import com.fe.wallpie.adapters.SearchAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.listener.EndlessRecyclerViewScrollListener;
import com.fe.wallpie.model.photos.ProfileImage;
import com.fe.wallpie.model.photos.Urls;
import com.fe.wallpie.model.photos.User;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.model.search.Result;
import com.fe.wallpie.model.search.SearchResponse;
import com.fe.wallpie.model.user.RecommendationResponse;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class SearchActivity extends BaseActivity {

    private static final String SEARCH_KWORD_EXTRA = "search_keywood";
    private static final int MAX_ITEMS_PER_REQUEST = 30;
    private static final String BUDNLE_SEARCH = "bundle_search";
    private static final String BUNDLE_RECYLER_VIEW = "bundle_rv";
    @BindView(R.id.recycler_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar_loading)
    ProgressBar mProgressBar;
    WallpaperProvider mWallpaperProvider;
    String search;
    Disposable mDisposableInitial;
    Disposable mDisposableFollowing;
    SearchAdapter.OnItemClickListener mOnItemClickListener;
    private SearchAdapter searchAdapter;
    EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        page = 1;
        setUpSeachView();
        mWallpaperProvider = new WallpaperProvider(Wallpie.getDesiredMinimumHeight(), Wallpie.getDesiredMinimumHeight());
        search = getIntent().getStringExtra(SEARCH_KWORD_EXTRA);
        setUpToolbar();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOnItemClickListener=(searchResponse, searchViewHolder) -> {
            Intent intent = DetailActivity.createIntent(SearchActivity.this, searchToWallpaperResponse(searchResponse));
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, searchViewHolder.mWallpaper, getString(R.string.shared_element_transition_wallpaper));
            startActivity(intent, optionsCompat.toBundle());
        };
        mEndlessRecyclerViewScrollListener = getListener();
        mRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        search = savedInstanceState.getString(BUDNLE_SEARCH);
        mToolbar.setTitle(search);
        mRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(BUNDLE_RECYLER_VIEW));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUDNLE_SEARCH,search);
        outState.putParcelable(BUNDLE_RECYLER_VIEW, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @NonNull
    private EndlessRecyclerViewScrollListener getListener() {
        return new EndlessRecyclerViewScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                scrollSearch(page);
            }
        };
    }

    private void scrollSearch(int page) {
        mDisposableFollowing=mWallpaperProvider.getSearchResult(search, MAX_ITEMS_PER_REQUEST,page)
                .subscribe(
                        seachResponse -> {

                            searchAdapter.addItems(seachResponse.getResults());
                            searchAdapter.notifyItemRangeInserted((page-1)*MAX_ITEMS_PER_REQUEST,MAX_ITEMS_PER_REQUEST);
                        },
                        throwable -> {
                            handleError(throwable);
                        });
    }

    public void setUpToolbar() {
        super.setUpToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(search);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDisposableInitial = mWallpaperProvider.getSearchResult(search, MAX_ITEMS_PER_REQUEST, page)
                .subscribe(searchResponse -> {
                    populatePhotos(searchResponse.getResults());
                }, this::handleError);
    }


    private void populatePhotos(List<Result> searchResponses) {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        searchAdapter = new SearchAdapter(this,searchResponses,mOnItemClickListener);
        mRecyclerView.setAdapter(searchAdapter);
    }

    public static Intent createIntent(Context context, String searchKeyword) {
        Intent intent = new Intent(context,SearchActivity.class);
        intent.putExtra(SEARCH_KWORD_EXTRA, searchKeyword);
        return intent;
    }
    private void setUpSeachView() {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.clear();
                mToolbar.setTitle(query);
                mSearchView.closeSearch();
                mProgressBar.setVisibility(View.VISIBLE);
                search = query;
                mDisposableInitial=mWallpaperProvider.getSearchResult(search, MAX_ITEMS_PER_REQUEST, page)
                        .subscribe(searchResponse -> {
                            populatePhotos(searchResponse.getResults());
                        },throwable -> handleError(throwable));
                mRecyclerView.removeOnScrollListener(mEndlessRecyclerViewScrollListener);
                mEndlessRecyclerViewScrollListener = getListener();
                mRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(this);
    }


    private WallpapersResponse searchToWallpaperResponse(Result searchResponse) {
        WallpapersResponse wallpapersResponse = new WallpapersResponse();
        Urls urls = new Urls();
        User user = new User();
        user.setUsername(searchResponse.getUser().getUsername());
        user.setName(searchResponse.getUser().getName());
        user.setUsername(searchResponse.getUser().getUsername());
        user.setFirstName(searchResponse.getUser().getFirstName());
        user.setBio(searchResponse.getUser().getBio());
        urls.setRegular(searchResponse.getUrls().getRegular());
        ProfileImage profileImage = new ProfileImage();
        profileImage.setMedium(searchResponse.getUser().getProfileImage().getMedium());
        user.setProfileImage(profileImage);
        wallpapersResponse.setUrls(urls);
        wallpapersResponse.setUser(user);
        wallpapersResponse.setId(searchResponse.getId());
        return wallpapersResponse;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDisposableInitial != null && !mDisposableInitial.isDisposed()) {
            mDisposableInitial.dispose();
        }
        if (mDisposableFollowing != null && !mDisposableFollowing.isDisposed()) {
            mDisposableFollowing.dispose();
        }
    }
    public void handleError(Throwable throwable) {
        if (throwable instanceof IOException) {
            snackBarResult("Timeout");
        }
        else if (throwable instanceof IllegalStateException) {
            snackBarResult("ConversionError");
        } else {

            snackBarResult(String.valueOf(throwable.getLocalizedMessage()));
        }
    }

    private void snackBarResult(String msg) {
        mProgressBar.setVisibility(View.GONE);
        Snackbar.make(mRecyclerView,msg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
