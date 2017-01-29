package com.fe.wallpie.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.fe.wallpie.R;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.fragment.CollectionsFragment;
import com.fe.wallpie.fragment.FavoriteFragmanet;
import com.fe.wallpie.fragment.LatestPhotoFragment;
import com.fe.wallpie.fragment.PopularPhotosFragment;
import com.fe.wallpie.adapters.ViewPagerAdpater;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        CollectionsFragment.OnCollectionsFragmentInteractionListener,
        PopularPhotosFragment.OnPopularPhotosFragmentInteractionListener,
        LatestPhotoFragment.OnLatestPhotoFragmentInteractionListener,
        FavoriteFragmanet.OnFavoriteFragmentInteractionListener {

    private static final int RC_SIGN_IN = 100;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mStateListener;


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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
               showSnackbar(R.string.logged_in);
                Wallpie.setFavRef(FirebaseDatabase.getInstance().getReference("favorite"));
                return;
            } else if (resultCode==RESULT_CANCELED){
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackbar(R.string.unknown_error);
                }
            }
        }
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
    private void showSnackbar(int id){
        Snackbar.make(mCoordinatorLayout, getString(id), Snackbar.LENGTH_SHORT).show();
    }

    public void login(View view) {
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

}
