package com.fe.wallpie.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fe.wallpie.R;
import com.fe.wallpie.fragment.CollectionsFragment;
import com.fe.wallpie.fragment.FavoriteFragmanet;
import com.fe.wallpie.fragment.LatestPhotoFragment;
import com.fe.wallpie.fragment.PopularPhotosFragment;
import com.fe.wallpie.adapters.ViewPagerAdpater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        CollectionsFragment.OnCollectionsFragmentInteractionListener,
        PopularPhotosFragment.OnPopularPhotosFragmentInteractionListener,
        LatestPhotoFragment.OnLatestPhotoFragmentInteractionListener,
        FavoriteFragmanet.OnFavoriteFragmentInteractionListener {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        setUpToolbar();
        setUpViewPager();
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void setUpViewPager() {

        ViewPagerAdpater viewPagerAdpater = new ViewPagerAdpater(getSupportFragmentManager());
        viewPagerAdpater.addFragment(new CollectionsFragment(), getString(R.string.collections));
        viewPagerAdpater.addFragment(new PopularPhotosFragment(), getString(R.string.popular));
        viewPagerAdpater.addFragment(new LatestPhotoFragment(), getString(R.string.latest));
        viewPagerAdpater.addFragment(new FavoriteFragmanet(), getString(R.string.favorite));
        mViewPager.setAdapter(viewPagerAdpater);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
