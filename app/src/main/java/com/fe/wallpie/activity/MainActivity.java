package com.fe.wallpie.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fe.wallpie.R;
import com.fe.wallpie.adapters.ViewPagerAdpater;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.fragment.CollectionsFragment;
import com.fe.wallpie.fragment.FavoriteFragmanet;
import com.fe.wallpie.fragment.LatestPhotoFragment;
import com.fe.wallpie.fragment.PopularPhotosFragment;
import com.fe.wallpie.utility.AndroidUtils;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements
        CollectionsFragment.OnCollectionsFragmentInteractionListener,
        PopularPhotosFragment.OnPopularPhotosFragmentInteractionListener,
        LatestPhotoFragment.OnLatestPhotoFragmentInteractionListener,
        FavoriteFragmanet.OnFavoriteFragmentInteractionListener {

    private static final int RC_SIGN_IN = 100;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    CircleImageView mProfileDp;
    ImageView mProfileBg;
    TextView mUserName;
    TextView mEmail;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.app_name_tv)
    TextView mAppName;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mStateListener;
    ActionBarDrawerToggle mActionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        ButterKnife.bind(this);
        setUpToolbar();
        setUpViewPager();
        setUpDrawer();
        setUpSeachView();
    }

    private void setUpSeachView() {
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchViewListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);


            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                showSnakcBar("Welcome " + user.getDisplayName());
                updateUserInfo(user);

                Wallpie.setFavRef(FirebaseDatabase.getInstance().getReference("favorite"));
                return;
            } else if (resultCode==RESULT_CANCELED){
                if (response!=null &&response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                } else if (response!=null && response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                }
            }
        }
    }

    private void updateUserInfo(FirebaseUser user) {
        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(mProfileDp);
        mUserName.setText(user.getDisplayName());
        mEmail.setText(user.getEmail());

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


    public void login(View view) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
        }else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),
                    RC_SIGN_IN);
        }

    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


    private void setUpDrawer() {

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_closed);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        View header=mNavigationView.getHeaderView(0);
        mProfileDp = (CircleImageView) header.findViewById(R.id.profile_dp);
        mProfileBg = (ImageView) header.findViewById(R.id.poster_bg);
        mUserName = (TextView) header.findViewById(R.id.tv_name);
        mEmail = (TextView) header.findViewById(R.id.tv_email);
        Glide.with(this)
                .load(getString(R.string.photographer_bg))
                .into(mProfileBg);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateUserInfo(FirebaseAuth.getInstance().getCurrentUser());
        } else {
            removeUserInfo();
        }

       mNavigationView.setNavigationItemSelectedListener(item -> {
           if (item.getItemId() == R.id.logout) {
               FirebaseAuth.getInstance().signOut();
               removeUserInfo();
               return true;
           }
           return false;

       });

    }

    private void removeUserInfo() {
        mUserName.setText(R.string.login);
        mUserName.setOnClickListener(this::login);
        mEmail.setText("");
    }

    @Override
    protected void setUpToolbar() {
        super.setUpToolbar();
        getSupportActionBar().setTitle("");
        mAppName.setTypeface(AndroidUtils.getPacificoTypeface(this));
    }
}
