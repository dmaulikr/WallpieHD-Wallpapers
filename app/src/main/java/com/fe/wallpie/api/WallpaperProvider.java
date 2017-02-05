package com.fe.wallpie.api;

import com.fe.wallpie.application.Wallpie;
import com.fe.wallpie.model.collection.CollectionImages;
import com.fe.wallpie.model.collections.CollectionResponse;
import com.fe.wallpie.model.photos.WallpapersResponse;
import com.fe.wallpie.model.search.SearchResponse;
import com.fe.wallpie.model.user.RecommendationResponse;
import com.fe.wallpie.utility.AndroidUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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

/**
 * Created by Farmaan-PC on 21-01-2017.
 */

public class WallpaperProvider {
    private static final String UNSPLASH_API_KEY = "9a6788942f576c62329be1a2124c997cc9a409889400fd6f98ef2d62ccd69ed8";
    // private static final String UNSPLASH_API_KEY ="a3d491a681845489d835951746515bea9c0f50fe86d692e2c29325a2c68dd9c3";
    private static final String UNSPLASH_POPULAR = "popular";
    private static final String UNSPLASH_LATEST = "latest";
    private static final String UNSPLASH_COLLECTION_FEATURED = "featured";
    private static final String CACHE_CONTROL = "Cache-Control";
    public static final String CUSTOM_PARAMS = String.format(Locale.ENGLISH, "?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=%d&h=%d&fit=max&s=e7b928aabf0a4d92c6254587def34fd3", (int) (Wallpie.getDesiredMinimumWidth() * 1.5), (int) (Wallpie.getDesiredMinimumHeight() * 1.5));
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


        @GET("collections/featured")
        Observable<List<CollectionResponse>> getCollections(
                @Query("page") String page,
                @Query("client_id") String clientID,
                @Query("per_page") String perPage
        );

        @GET("users/{username}/photos")
        Observable<List<RecommendationResponse>> getRecommedation(
                @Path("username") String username,
                @Query("per_page") String perPage,
                @Query("client_id") String clientID,
                @Query("order_by") String orderBY,
                @Query("page") String page
        );

        @GET("collections/{id}/photos")
        Observable<List<CollectionImages>> getCollectionImages(
                @Path("id") String collectionId,
                @Query("page") String page,
                @Query("per_page") String perPage,
                @Query("client_id") String clientID
        );

        @GET("search/photos")
        Observable<SearchResponse> getSearchResult(
                @Query("query") String query,
                @Query("page") String page,
                @Query("per_page") String perPage,
                @Query("client_id") String clientID
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
                        .maxAge(60, TimeUnit.MINUTES)
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

    public Observable<List<WallpapersResponse>> getPopularImages(int page, int noOfItems) {
        Observable<List<WallpapersResponse>> observable = unsplashService.getImages(UNSPLASH_POPULAR, String.valueOf(page), UNSPLASH_API_KEY, String.valueOf(noOfItems));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }


    public Observable<List<WallpapersResponse>> getLatestImages(int page, int noOfItems) {
        Observable<List<WallpapersResponse>> observable = unsplashService.getImages(UNSPLASH_LATEST, String.valueOf(page), UNSPLASH_API_KEY, String.valueOf(noOfItems));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<CollectionResponse>> getCollections(int page, int noOfItems) {
        Observable<List<CollectionResponse>> observable = unsplashService.getCollections(String.valueOf(page), UNSPLASH_API_KEY, String.valueOf(noOfItems));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<RecommendationResponse>> getRecommendation(String username, int perPage, int page) {
        Observable<List<RecommendationResponse>> observable = unsplashService.getRecommedation(username, String.valueOf(perPage), UNSPLASH_API_KEY, "oldest", String.valueOf(page));
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<CollectionImages>> getCollectionImage(String id, int perPage, int page) {
        Observable<List<CollectionImages>> observable = unsplashService.getCollectionImages(id, String.valueOf(page), String.valueOf(perPage), UNSPLASH_API_KEY);
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SearchResponse> getSearchResult(String query, int perPage, int page) {
        Observable<SearchResponse> listObservable = unsplashService.getSearchResult(query, String.valueOf(page), String.valueOf(perPage), UNSPLASH_API_KEY);
        return listObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
