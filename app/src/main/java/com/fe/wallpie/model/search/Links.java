
package com.fe.wallpie.model.search;

import com.google.gson.annotations.SerializedName;
public class Links {

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

}
