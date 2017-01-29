package com.fe.wallpie.application;

import android.app.Application;
import android.app.WallpaperManager;
import android.util.Log;

import com.fe.wallpie.R;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.BuildConfig;
import timber.log.Timber;

/**
 * Created by Farmaan-PC on 21-01-2017.
 */

public class Wallpie extends Application {
    private static Wallpie sWallpie;
    private static DatabaseReference sDatabaseReference;
    private static WallpaperManager mWallpaperManager;
    private static long sDownloadRef;

    @Override
    public void onCreate() {
        super.onCreate();
        sWallpie = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);;
        mWallpaperManager = WallpaperManager.getInstance(this);
        MobileAds.initialize(this,getString(R.string.app_id));


    }

    public static Wallpie getInstance() {
        return sWallpie;
    }


    public static void setFavRef(DatabaseReference favorite) {
        sDatabaseReference = favorite;
    }

    public static DatabaseReference getFavRef() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null && sDatabaseReference!=null) {
            return sDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        return null;

    }
    public static int getDesiredMinimumHeight() {
        return mWallpaperManager.getDesiredMinimumHeight();
    }
    public static int getDesiredMinimumWidth() {
        return mWallpaperManager.getDesiredMinimumWidth();
    }
    public static void setDownloadRef(long id) {
        sDownloadRef = id;
    }

    public static long getDownloadRef() {
        return sDownloadRef;
    }


}
