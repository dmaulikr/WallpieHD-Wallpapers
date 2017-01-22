package com.fe.wallpie.api;

import android.content.Context;
import android.util.Log;

import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.model.photo.WallpaperResponse;
import com.fe.wallpie.utility.AndroidUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import timber.log.Timber;

/**
 * Created by Farmaan-PC on 21-01-2017.
 */

public class WallpaperProvider {
    private static final String UNSPLASH_API_KEY = "9a6788942f576c62329be1a2124c997cc9a409889400fd6f98ef2d62ccd69ed8";
    private static final String UNSPLASH_POPULAR = "popular";
    private static final String UNSPLASH_LATEST = "latest";
    private static final String CACHE_CONTROL = "Cache-Control";
    OkHttpClient mOkHttpClient;
    Retrofit mRetrofit;
    UnsplashService unsplashService;
    int mHeight;
    int mWidth;


    private interface UnsplashService {
        @GET("photos/curated")
        Observable<List<WallpapersResponse>> getImages(
                @Query("order_by") String orderBY,
                @Query("page") String page,
                @Query("client_id") String clientID,
                @Query("per_page") String perPage
        );

        @GET("photos/{id}")
        Observable<WallpaperResponse> getImage(
                @Path("id") String photoId,
                @Query("client_id") String clientId,
                @Query("h") int height,
                @Query("w") int width
        );

    }

    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build();
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(Wallpie.getInstance().getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Timber.e(e, "Could not create Cache!");
        }
        return cache;
    }

    public static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    public static Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!AndroidUtils.isNetworkAvailable()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    public WallpaperProvider(int height, int width) {

        Retrofit retrofit = new Retrofit.Builder()
                .client(provideOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.unsplash.com/")
                .build();
        unsplashService = retrofit.create(UnsplashService.class);
        mHeight = height;
        mWidth = width;
    }

    public void getImage(String id) {
        Observable<WallpaperResponse> observable = unsplashService.getImage(id, UNSPLASH_API_KEY, mHeight, mWidth);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wallpaperResponse -> Log.d("Farmaan", wallpaperResponse.getUrls().getCustom()));
    }

    public Observable<List<WallpapersResponse>> getPopularImages(int page) {
        Observable<List<WallpapersResponse>> observable = unsplashService.getImages(UNSPLASH_POPULAR, String.valueOf(page), UNSPLASH_API_KEY, "20");
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }


    public Observable<List<WallpapersResponse>> getLatestImages(int page) {
        Observable<List<WallpapersResponse>> observable = unsplashService.getImages(UNSPLASH_LATEST, String.valueOf(page), UNSPLASH_API_KEY, "20");
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
