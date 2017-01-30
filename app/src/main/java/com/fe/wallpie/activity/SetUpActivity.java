package com.fe.wallpie.activity;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.fe.wallpie.BuildConfig;
import com.fe.wallpie.R;
import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.collection.CollectionImages;
import com.fe.wallpie.model.photos.WallpapersResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetUpActivity extends AppCompatActivity {

    @BindView(R.id.loading_wait)
    ImageView mImageViewLoad;
    BroadcastReceiver mCompleteBroadcastReceiver;
    WallpaperManager mWallpaperManager;


    private static final String PARCEL_EXTRA = "wallpaper_parcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mWallpaperManager = WallpaperManager.getInstance(this);
        GlideDrawableImageViewTarget glideDrawableImageViewTarget = new GlideDrawableImageViewTarget(mImageViewLoad);
        Glide.with(this).load(R.raw.loading_pallete).into(glideDrawableImageViewTarget);


    }

    @Override
    protected void onStart() {
        super.onStart();

        initializeBroadcastReceiver();

    }

    private void initializeBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mCompleteBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == Wallpie.getDownloadRef()) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    Cursor cursor = downloadManager.query(query);
                    cursor.moveToFirst();
                    Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                    File file = new File(uri.getPath());
                    Log.d(SetUpActivity.class.getName(), "onReceive: " + file.toString());

                    Uri capturedImage = null;

                    capturedImage = FileProvider.getUriForFile(SetUpActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            file);
//
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent setWallpaper = mWallpaperManager.getCropAndSetWallpaperIntent(capturedImage);
                        startActivity(setWallpaper);
                        finish();
                    } else {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), capturedImage);
                            mWallpaperManager.setBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }


            }
        };
        registerReceiver(mCompleteBroadcastReceiver, intentFilter);
    }


    public static Intent creatIntent(Context context, WallpapersResponse wallpapersResponse) {
        Intent intent = new Intent(context, SetUpActivity.class);
        intent.putExtra(PARCEL_EXTRA, wallpapersResponse);
        return intent;
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mCompleteBroadcastReceiver);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
