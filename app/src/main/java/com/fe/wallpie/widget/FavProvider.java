package com.fe.wallpie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.fe.wallpie.R;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

class FavProvider implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    DatabaseReference mReference;
    int mWidgetId;
    List<WallpapersResponse> mWallpapersResponses;

    public FavProvider(Context ctx, Intent intent) {
        mContext = ctx;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        mWallpapersResponses = new ArrayList<>();

    }

    @Override
    public void onCreate() {

    }

    private void populateData() {
        Log.d(FavProvider.class.getName(), "populateData: ");
        Semaphore semaphore = new Semaphore(0);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Log.d(FavProvider.class.getName(), "populateData: check user");
            mReference = FirebaseDatabase.getInstance().getReference("favorite").child(firebaseAuth.getCurrentUser().getUid());
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot data :
                            dataSnapshot.getChildren()) {
                        WallpapersResponse wallpapersResponse = data.getValue(WallpapersResponse.class);
                        mWallpapersResponses.add(wallpapersResponse);
                        Log.d(FavProvider.class.getName(), "populateData:datasna" + wallpapersResponse.getUser());
                    }
                    semaphore.release();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDataSetChanged() {
        populateData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d(FavProvider.class.getName(), "populateData:size" + mWallpapersResponses.size());
        return mWallpapersResponses.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        WallpapersResponse wallpapersResponse = mWallpapersResponses.get(position);
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_layout);
        BitmapRequestBuilder bitmapRequestBuilder = Glide.with(mContext)
                .load(wallpapersResponse.getUrls().getSmall())
                .asBitmap();
        FutureTarget futureTarget = bitmapRequestBuilder.into(200, 200);
        try {
            remoteViews.setImageViewBitmap(R.id.fav_image, (Bitmap) futureTarget.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return remoteViews;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}