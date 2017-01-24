package com.fe.wallpie.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fe.wallpie.R;
import com.fe.wallpie.adapters.CollectionAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.model.collections.CollectionResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public CollectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWallpaperProvider = new WallpaperProvider(1920, 1080);
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWallpaperProvider.getCollections(1).subscribe(collectionResponses -> populateCollection(collectionResponses));
    }

    private void populateCollection(List<CollectionResponse> collectionResponses) {
        mCollectionAdapter = new CollectionAdapter(collectionResponses, getActivity());
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
    }

    public interface OnCollectionsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
