package com.fe.wallpie.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fe.wallpie.R;
import com.fe.wallpie.activity.DetailActivity;
import com.fe.wallpie.adapters.FavoriteAdapter;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoriteFragmanet extends Fragment {

    @BindView(R.id.favorite_recycler_view)
    RecyclerView mFavRecyclerView;
    @BindView(R.id.login_instruction)
    TextView mLoginInstruction;

    FavoriteAdapter mFavoriteAdapter;
    private OnFavoriteFragmentInteractionListener mListener;
    private DatabaseReference mReference;


    public FavoriteFragmanet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mFavRecyclerView.setVisibility(View.VISIBLE);
            mReference = FirebaseDatabase.getInstance().getReference("favorite").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mFavoriteAdapter = new FavoriteAdapter(WallpapersResponse.class,
                    R.layout.wallpaper_item_detailed,
                    FavoriteAdapter.FavoriteViewHolder.class,
                    mReference,
                    new FavoriteAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(WallpapersResponse wallpapersResponse, FavoriteAdapter.FavoriteViewHolder holder) {
                            Intent intent = DetailActivity.createIntent(getActivity(), wallpapersResponse);
                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), holder.mWallpaper, getString(R.string.shared_element_transition_wallpaper));
                            startActivity(intent, optionsCompat.toBundle());
                        }
                    });
            mFavRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mFavRecyclerView.setAdapter(mFavoriteAdapter);
        } else {
            mLoginInstruction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteFragmentInteractionListener) {
            mListener = (OnFavoriteFragmentInteractionListener) context;
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


    public interface OnFavoriteFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
