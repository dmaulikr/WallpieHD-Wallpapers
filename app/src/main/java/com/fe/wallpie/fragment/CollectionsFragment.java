package com.fe.wallpie.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fe.wallpie.R;
import com.fe.wallpie.activity.PhotosActivity;
import com.fe.wallpie.adapters.CollectionAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.listener.EndlessRecyclerViewScrollListener;
import com.fe.wallpie.model.collections.CollectionResponse;
import com.fe.wallpie.utility.AndroidUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCollectionsFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CollectionsFragment extends Fragment {

    private OnCollectionsFragmentInteractionListener mListener;
    @BindView(R.id.collection_recyclerview)
    RecyclerView mRecyclerView;
    WallpaperProvider mWallpaperProvider;
    CollectionAdapter mCollectionAdapter;
    private static final int MAX_ITEMS_PER_REQUEST = 30;

    Disposable mCollectionInitialSubscription;
    Disposable mCollectionFollowingSubscription;
    private int page;

    public CollectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWallpaperProvider = new WallpaperProvider(1920, 1080);
        page = 1;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collections, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCollectionInitialSubscription=mWallpaperProvider.getCollections(page,MAX_ITEMS_PER_REQUEST).
                subscribe(collectionResponses -> populateCollection(collectionResponses),
                throwable -> {
                    Snackbar.make(getView(),throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
                });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mCollectionFollowingSubscription=mWallpaperProvider.getCollections(page, MAX_ITEMS_PER_REQUEST)
                        .subscribe(
                                collectionResponses -> {

                                    mCollectionAdapter.addItems(collectionResponses);
                                    mCollectionAdapter.notifyItemRangeInserted((page-1)*MAX_ITEMS_PER_REQUEST,MAX_ITEMS_PER_REQUEST);
                                },
                                throwable -> {
                                    Log.d(CollectionsFragment.class.getName(), throwable.getMessage());
                                });
            }
        });
    }

    private void populateCollection(List<CollectionResponse> collectionResponses) {
        mCollectionAdapter = new CollectionAdapter(collectionResponses, getActivity(), new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CollectionResponse collectionResponse, CollectionAdapter.CollectionViewHolder collectionViewHolder) {
                startActivity(PhotosActivity.ceateIntent(getActivity(),collectionResponse.getId().toString()));
            }
        });
        mRecyclerView.setAdapter(mCollectionAdapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCollectionsFragmentInteractionListener) {
            mListener = (OnCollectionsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCollectionsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mCollectionInitialSubscription != null && !mCollectionInitialSubscription.isDisposed()) {
            mCollectionInitialSubscription.dispose();
        }
        if (mCollectionFollowingSubscription != null && !mCollectionFollowingSubscription.isDisposed()) {
            mCollectionFollowingSubscription.dispose();
        }
    }

    public interface OnCollectionsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
