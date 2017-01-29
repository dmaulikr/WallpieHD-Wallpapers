package com.fe.wallpie.activity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.fe.wallpie.BuildConfig;
import com.fe.wallpie.R;
import com.fe.wallpie.adapters.RecommendationAdapter;
import com.fe.wallpie.api.WallpaperProvider;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.model.user.RecommendationResponse;
import com.fe.wallpie.utility.PermissionManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class DetailActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 100;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 101;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.backdrop_iv)
    ImageView mWallpaperImage;
    @BindView(R.id.btn_download)
    Button mButtonDownload;
    @BindView(R.id.btn_set_wallpaper)
    Button mButtonSetWallpaper;
    @BindView(R.id.iv_like)
    ToggleButton mLikeToggle;
    @BindView(R.id.iv_like_info)
    TextView mLikeText;
    @BindView(R.id.photograpger_name_tv)
    TextView mPhotographerName;
    @BindView(R.id.photograpger_username)
    TextView mPhotographerUserName;
    @BindView(R.id.prof_pic_iv)
    CircleImageView mProfPic;
    @BindView(R.id.photograpger_bio)
    TextView mPhotographerBio;
    @BindView(R.id.more_recommendation)
    TextView mMoreRecommendation;
    @BindView(R.id.recommendation_rv)
    RecyclerView mRecommendation;
    @BindView(R.id.more_from_photographer)
    TextView mMoreFromPhotgrapher;
    RecommendationAdapter mRecommendationAdapter;
    RecommendationAdapter.OnItemClickListener mOnItemClickListener;
    Disposable mDisposable;
    WallpaperProvider mWallpaperProvider;
    WallpapersResponse mWallpapersResponse;
    @BindView(R.id.detail_ac_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.native_ad)
    NativeExpressAdView mExpressAdView;
    @BindView(R.id.ads_layout)
    LinearLayout mAdsLayout;
    DownloadManager mDownloadManager;
    WallpaperManager mWallpaperManager;
    private static final String mWallpaperParcel = "wallpaper_parcel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setUpToolbar();
        initializeUI();
        setUpManagers();
    }

    private void setUpManagers() {
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mWallpaperManager = WallpaperManager.getInstance(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Observable<List<RecommendationResponse>> recommendation = mWallpaperProvider.getRecommendation(mWallpapersResponse.getUser().getUsername(), "4");
        mDisposable = recommendation.subscribe(
                recommendationResponses -> {
                    mRecommendationAdapter = new RecommendationAdapter(recommendationResponses, DetailActivity.this, mOnItemClickListener);
                    mRecommendation.setAdapter(mRecommendationAdapter);
                    mRecommendationAdapter.notifyDataSetChanged();

                },
                throwable -> {
                    Log.d(DetailActivity.this.getClass().getName(), throwable.getMessage());
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mDisposable.dispose();
    }

    private void setUpToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static Intent createIntent(Context context, WallpapersResponse parcelable) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(mWallpaperParcel, parcelable);
        return intent;
    }

    private void initializeUI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mRecommendation.setLayoutManager(new GridLayoutManager(this, 2));
        mWallpaperProvider = new WallpaperProvider(Wallpie.getDesiredMinimumHeight(), Wallpie.getDesiredMinimumWidth());
        mWallpapersResponse = getIntent().getParcelableExtra(mWallpaperParcel);
        mOnItemClickListener = new RecommendationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecommendationResponse recommendationResponse, RecommendationAdapter.RecommendationViewHolder recomedationViewHolder) {
                Toast.makeText(DetailActivity.this, "Working item click", Toast.LENGTH_SHORT).show();
            }
        };
        Glide.with(this)
                .load(mWallpapersResponse.getUrls().getRegular())
                .placeholder(R.drawable.wallpaper_placeholder)
                .thumbnail(0.1f)
                .into(mWallpaperImage);
        Glide.with(this)
                .load(mWallpapersResponse.getUser().getProfileImage().getMedium())
                .thumbnail(0.1f)
                .into(mProfPic);
        mPhotographerName.setText(mWallpapersResponse.getUser().getName());
        mPhotographerUserName.setText(mWallpapersResponse.getUser().getUsername());
        if (mWallpapersResponse.getUser().getBio() != null) {
            mPhotographerBio.setText(mWallpapersResponse.getUser().getBio());
        } else {
            mPhotographerBio.setVisibility(View.GONE);
        }
        mMoreFromPhotgrapher.setText("More from " + mWallpapersResponse.getUser().getFirstName());
        setLikeButton();
        mExpressAdView.loadAd( new AdRequest.Builder()
                .build());
        mExpressAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                mAdsLayout.setVisibility(View.GONE);
            }
        });
        mButtonDownload.setOnClickListener(v -> downloadOnly());
        mButtonSetWallpaper.setOnClickListener(v ->{
            downloadWallpaper();
        });
    }
    private void setLikeButton() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseDatabase.getInstance().getReference("favorite").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                FirebaseDatabase.getInstance().getReference("favorite").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(mWallpapersResponse.getId())) {
                                    mLikeToggle.setChecked(true);
                                    mLikeText.setText(R.string.liked_it);
                                } else {
                                    mLikeToggle.setChecked(false);
                                    mLikeText.setText(R.string.havent_liked_it);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
            mLikeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (FirebaseDatabase.getInstance().getReference("favorite").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                            FirebaseDatabase.getInstance().getReference("favorite").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    child(mWallpapersResponse.getId()).setValue(mWallpapersResponse);
                            mLikeText.setText(R.string.liked_it);
                        } else {
                            Snackbar.make(mCoordinatorLayout, getString(R.string.login_to_fav), Snackbar.LENGTH_SHORT).show();
                            mLikeToggle.setChecked(false);
                        }

                    } else {
                        if (FirebaseDatabase.getInstance().getReference("favorite").
                                child(FirebaseAuth.getInstance().getCurrentUser().getUid()) != null) {
                            FirebaseDatabase.getInstance().getReference("favorite").
                                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    child(mWallpapersResponse.getId()).removeValue();
                        }
                    }
                }
            });
        } else {
            mLikeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mLikeToggle.setChecked(false);
                    }
                    Snackbar.make(mCoordinatorLayout,
                            getString(R.string.login_to_fav),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void downloadOnly() {
        if (!PermissionManager.checkWriteStoragePermission(this)) {
            PermissionManager.requestWriteStoragePermission(this);

        } else {
            initializeWallPaperDownLoad();
        }
    }
    public void downloadWallpaper(){
        if (!PermissionManager.checkWriteStoragePermission(this)) {
            PermissionManager.requestWriteStoragePermission(this);

        } else if ((!PermissionManager.checkReadStoragePermission(this))) {
            PermissionManager.requestReadStoragePermission(this);
        } else {
            setWallpaper();
        }
    }

    private void setWallpaper() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File fullPathToFile = new File(file, "wallpie/" + mWallpapersResponse.getId() + ".jpg");
        if (fullPathToFile.exists()) {
            Uri capturedImage = FileProvider.getUriForFile(DetailActivity.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent setWallpaper = mWallpaperManager.getCropAndSetWallpaperIntent(capturedImage);
                startActivity(setWallpaper);
            } else {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), capturedImage);
                    mWallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            initializeWallPaperDownLoad();
            startActivity(SetUpActivity.creatIntent(this, mWallpapersResponse));
        }
    }



    private void initializeWallPaperDownLoad() {
        String url = mWallpapersResponse.getUrls().getRegular();
        String url1 = url.substring(0, url.indexOf("?ixlib"));
        String customUrl = url1 + WallpaperProvider.CUSTOM_PARAMS;
        Log.d(DetailActivity.class.getName(), "initializeWallPaperDownLoad: "+customUrl);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(customUrl));
        request.setTitle(getString(R.string.app_name)).setDescription("Wallpaper by" + mWallpapersResponse.getUser().getFirstName());
        request.setDestinationInExternalPublicDir( Environment.DIRECTORY_PICTURES,"wallpie/"+mWallpapersResponse.getId() + ".jpg");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        long downloadRef=mDownloadManager.enqueue(request);
        Wallpie.setDownloadRef(downloadRef);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(mCoordinatorLayout, getString(R.string.permission_required), Snackbar.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
