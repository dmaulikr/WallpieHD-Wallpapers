package com.fe.wallpie.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.fe.wallpie.R;
import com.fe.wallpie.activity.DetailActivity;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.listener.EndlessRecyclerViewScrollListener;
import com.fe.wallpie.model.parcellable.WallpaperParcel;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.adapters.PhotosAdapter;
import com.fe.wallpie.utility.AndroidUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPopularPhotosFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PopularPhotosFragment extends Fragment {

    private OnPopularPhotosFragmentInteractionListener mListener;
    @BindView(R.id.photos_recyclerview)
    public RecyclerView mRecyclerView;
    WallpaperProvider mWallpaperProvider;
    Disposable mPopularImageInitialDisposable;
    Disposable mPopularImageFollowingDisposable;
    PhotosAdapter mPhotosAdapter;
    private static final int MAX_ITEMS_PER_REQUEST = 30;
    private int page;


    public PopularPhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWallpaperProvider = new WallpaperProvider(1920, 1080);
        page=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popular_photos, container, false);
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
        mPopularImageInitialDisposable = mWallpaperProvider.getPopularImages(page,MAX_ITEMS_PER_REQUEST).
                subscribe(
                        this::populatePhotos,
                        throwable -> {
                            Snackbar.make(getView(), getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT).show();
                        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mWallpaperProvider.getPopularImages(page, MAX_ITEMS_PER_REQUEST)
                        .subscribe(
                                wallpapersResponses -> {

                                    mPhotosAdapter.addItems(wallpapersResponses);
                                    mPhotosAdapter.notifyItemRangeInserted((page-1)*MAX_ITEMS_PER_REQUEST,MAX_ITEMS_PER_REQUEST);
                                },
                                throwable -> {
                                    Log.d(PopularPhotosFragment.class.getName(), throwable.getMessage());
                                });
            }
        });
    }

    private void populatePhotos(List<WallpapersResponse> wallpapersResponses) {
        mPhotosAdapter = new PhotosAdapter(wallpapersResponses, getActivity(), new PhotosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WallpapersResponse wallpapersResponse, PhotosAdapter.PhotosViewHolder photosViewHolder) {
                Intent intent = DetailActivity.createIntent(getActivity(), wallpapersResponse);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), photosViewHolder.mWallpaper, getString(R.string.shared_element_transition_wallpaper));
                startActivity(intent, optionsCompat.toBundle());
            }
        });
        mRecyclerView.setAdapter(mPhotosAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPopularPhotosFragmentInteractionListener) {
            mListener = (OnPopularPhotosFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLatestPhotoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mPopularImageInitialDisposable!=null && !mPopularImageInitialDisposable.isDisposed()) {
            mPopularImageInitialDisposable.dispose();
        }
        if (mPopularImageFollowingDisposable != null && !mPopularImageFollowingDisposable.isDisposed()) {
            mPopularImageFollowingDisposable.dispose();
        }
    }


    public interface OnPopularPhotosFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
