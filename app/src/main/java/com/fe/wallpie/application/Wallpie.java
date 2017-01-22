package com.fe.wallpie.application;

import android.app.Application;

import timber.log.BuildConfig;
import timber.log.Timber;

/**
 * Created by Farmaan-PC on 21-01-2017.
 */

public class Wallpie extends Application {
    private static Wallpie sWallpie;

    @Override
    public void onCreate() {
        super.onCreate();
        sWallpie = this;
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static Wallpie getInstance() {
        return sWallpie;
    }
}
