package com.fe.wallpie.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fe.wallpie.R;
import com.fe.wallpie.activity.DetailActivity;
import com.fe.wallpie.adapters.PhotosAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.listener.EndlessRecyclerViewScrollListener;
import com.fe.wallpie.model.parcellable.WallpaperParcel;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.utility.AndroidUtils;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLatestPhotoFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LatestPhotoFragment extends Fragment {

    private OnLatestPhotoFragmentInteractionListener mListener;
    @BindView(R.id.photos_recyclerview)
    public RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    WallpaperProvider mWallpaperProvider;
    Disposable mLatestImageInitialDisposable;
    Disposable mLatestImageFollowingDisposable;
    private static final int MAX_ITEMS_PER_REQUEST = 30;
    private int page;
    PhotosAdapter photosAdapter;

    public LatestPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
        mWallpaperProvider = new WallpaperProvider(Wallpie.getDesiredMinimumHeight(), Wallpie.getDesiredMinimumWidth());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_latest_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mLatestImageInitialDisposable = mWallpaperProvider.getLatestImages(page, MAX_ITEMS_PER_REQUEST).
                subscribe(
                        wallpapersResponses -> populatePhotos(wallpapersResponses),
                        throwable -> {
                            Snackbar.make(getView(), getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT).show();
                        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mLatestImageFollowingDisposable=mWallpaperProvider.getLatestImages(page, MAX_ITEMS_PER_REQUEST)
                        .subscribe(
                                wallpapersResponses -> {

                                    photosAdapter.addItems(wallpapersResponses);
                                    photosAdapter.notifyItemRangeInserted((page-1)*MAX_ITEMS_PER_REQUEST,MAX_ITEMS_PER_REQUEST);
                                },
                                throwable -> {
                                    Log.d(LatestPhotoFragment.class.getName(), throwable.getMessage());
                                });
            }
        });
    }


    private void populatePhotos(List<WallpapersResponse> wallpapersResponses) {
        photosAdapter = new PhotosAdapter(wallpapersResponses, getActivity(), new PhotosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WallpapersResponse wallpapersResponse, PhotosAdapter.PhotosViewHolder photosViewHolder) {
                Intent intent = DetailActivity.createIntent(getActivity(), wallpapersResponse);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), photosViewHolder.mWallpaper, getString(R.string.shared_element_transition_wallpaper));
                startActivity(intent, optionsCompat.toBundle());

            }
        });
        mRecyclerView.setAdapter(photosAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLatestPhotoFragmentInteractionListener) {
            mListener = (OnLatestPhotoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLatestPhotoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mLatestImageInitialDisposable!=null && !mLatestImageInitialDisposable.isDisposed()) {
            mLatestImageInitialDisposable.dispose();
        }
        if (mLatestImageFollowingDisposable != null && !mLatestImageFollowingDisposable.isDisposed()) {
            mLatestImageFollowingDisposable.dispose();
        }
    }


    public interface OnLatestPhotoFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
