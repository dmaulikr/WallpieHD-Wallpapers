
package com.fe.wallpie.model.photos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Links implements Parcelable {

    @SerializedName("download")
    private String mDownload;
    @SerializedName("download_location")
    private String mDownloadLocation;
    @SerializedName("followers")
    private String mFollowers;
    @SerializedName("following")
    private String mFollowing;
    @SerializedName("html")
    private String mHtml;
    @SerializedName("likes")
    private String mLikes;
    @SerializedName("photos")
    private String mPhotos;
    @SerializedName("portfolio")
    private String mPortfolio;
    @SerializedName("self")
    private String mSelf;

    public Links() {
    }

    public String getDownload() {
        return mDownload;
    }

    public void setDownload(String download) {
        mDownload = download;
    }

    public String getDownloadLocation() {
        return mDownloadLocation;
    }

    public void setDownloadLocation(String download_location) {
        mDownloadLocation = download_location;
    }

    public String getFollowers() {
        return mFollowers;
    }

    public void setFollowers(String followers) {
        mFollowers = followers;
    }

    public String getFollowing() {
        return mFollowing;
    }

    public void setFollowing(String following) {
        mFollowing = following;
    }

    public String getHtml() {
        return mHtml;
    }

    public void setHtml(String html) {
        mHtml = html;
    }

    public String getLikes() {
        return mLikes;
    }

    public void setLikes(String likes) {
        mLikes = likes;
    }

    public String getPhotos() {
        return mPhotos;
    }

    public void setPhotos(String photos) {
        mPhotos = photos;
    }

    public String getPortfolio() {
        return mPortfolio;
    }

    public void setPortfolio(String portfolio) {
        mPortfolio = portfolio;
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        mSelf = self;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDownload);
        dest.writeString(this.mDownloadLocation);
        dest.writeString(this.mFollowers);
        dest.writeString(this.mFollowing);
        dest.writeString(this.mHtml);
        dest.writeString(this.mLikes);
        dest.writeString(this.mPhotos);
        dest.writeString(this.mPortfolio);
        dest.writeString(this.mSelf);
    }

    protected Links(Parcel in) {
        this.mDownload = in.readString();
        this.mDownloadLocation = in.readString();
        this.mFollowers = in.readString();
        this.mFollowing = in.readString();
        this.mHtml = in.readString();
        this.mLikes = in.readString();
        this.mPhotos = in.readString();
        this.mPortfolio = in.readString();
        this.mSelf = in.readString();
    }

    public static final Parcelable.Creator<Links> CREATOR = new Parcelable.Creator<Links>() {
        @Override
        public Links createFromParcel(Parcel source) {
            return new Links(source);
        }

        @Override
        public Links[] newArray(int size) {
            return new Links[size];
        }
    };
}
