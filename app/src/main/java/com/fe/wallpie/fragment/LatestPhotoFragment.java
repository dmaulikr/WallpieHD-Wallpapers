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
import com.fe.wallpie.adapters.PhotosAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.model.photos.WallpapersResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    WallpaperProvider mWallpaperProvider;

    public LatestPhotoFragment() {
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
        return inflater.inflate(R.layout.fragment_latest_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mWallpaperProvider.getLatestImages(1).
                subscribe(wallpapersResponses -> populatePhotos(wallpapersResponses, this.getActivity()));

    }

    private void populatePhotos(List<WallpapersResponse> wallpapersResponses, Context context) {
        PhotosAdapter photosAdapter = new PhotosAdapter(wallpapersResponses, context);
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
    }


    public interface OnLatestPhotoFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
