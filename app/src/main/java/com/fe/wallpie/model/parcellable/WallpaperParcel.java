package com.fe.wallpie.model.parcellable;

import android.os.Parcel;
import android.os.Parcelable;

import com.fe.wallpie.model.photos.Urls;
import com.fe.wallpie.model.photos.User;
import com.fe.wallpie.model.photos.WallpapersResponse;

/**
 * Created by Farmaan-PC on 27-01-2017.
 */

public class WallpaperParcel implements Parcelable {
    public String mID;
    public String mRegularUrls;
    public String mUserName;
    public String mUserBio;
    public String mUserProfImage;
    public String mFullName;
    public String mUserUrls;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mID);
        dest.writeString(this.mRegularUrls);
        dest.writeString(this.mUserName);
        dest.writeString(this.mUserBio);
        dest.writeString(this.mUserProfImage);
        dest.writeString(this.mFullName);
        dest.writeString(this.mUserUrls);
    }

    public WallpaperParcel() {
    }

    protected WallpaperParcel(Parcel in) {
        this.mID = in.readString();
        this.mRegularUrls = in.readString();
        this.mUserName = in.readString();
        this.mUserBio = in.readString();
        this.mUserProfImage = in.readString();
        this.mFullName = in.readString();
        this.mUserUrls = in.readString();
    }

    public WallpaperParcel(String ID, String regularUrls, String userName, String userBio, String fullName, String userProfImage, String userUrls) {
        mID = ID;
        mRegularUrls = regularUrls;
        mUserName = userName;
        mUserBio = userBio;
        mFullName = fullName;
        mUserProfImage = userProfImage;
        mUserUrls = userUrls;
    }

    public static final Parcelable.Creator<WallpaperParcel> CREATOR = new Parcelable.Creator<WallpaperParcel>() {
        @Override
        public WallpaperParcel createFromParcel(Parcel source) {
            return new WallpaperParcel(source);
        }

        @Override
        public WallpaperParcel[] newArray(int size) {
            return new WallpaperParcel[size];
        }
    };


}
