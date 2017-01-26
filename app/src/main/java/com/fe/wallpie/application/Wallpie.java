package com.fe.wallpie.application;

import android.app.Application;
import android.util.Log;

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

    @Override
    public void onCreate() {
        super.onCreate();
        sWallpie = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);;

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


}
