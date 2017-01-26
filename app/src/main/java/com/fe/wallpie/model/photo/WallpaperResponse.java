
package com.fe.wallpie.model.photo;

import java.util.List;


import com.google.gson.annotations.SerializedName;

public class WallpaperResponse {

    @SerializedName("categories")
    private List<Category> mCategories;
    @SerializedName("color")
    private String mColor;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("current_user_collections")
    private List<Object> mCurrentUserCollections;
    @SerializedName("downloads")
    private Long mDownloads;
    @SerializedName("exif")
    private Exif mExif;
    @SerializedName("height")
    private Long mHeight;
    @SerializedName("id")
    private String mId;
    @SerializedName("liked_by_user")
    private Boolean mLikedByUser;
    @SerializedName("likes")
    private Long mLikes;
    @SerializedName("links")
    private Links mLinks;
    @SerializedName("urls")
    private Urls mUrls;
    @SerializedName("user")
    private User mUser;
    @SerializedName("width")
    private Long mWidth;

    public WallpaperResponse() {
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String created_at) {
        mCreatedAt = created_at;
    }

    public List<Object> getCurrentUserCollections() {
        return mCurrentUserCollections;
    }

    public void setCurrentUserCollections(List<Object> current_user_collections) {
        mCurrentUserCollections = current_user_collections;
    }

    public Long getDownloads() {
        return mDownloads;
    }

    public void setDownloads(Long downloads) {
        mDownloads = downloads;
    }

    public Exif getExif() {
        return mExif;
    }

    public void setExif(Exif exif) {
        mExif = exif;
    }

    public Long getHeight() {
        return mHeight;
    }

    public void setHeight(Long height) {
        mHeight = height;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Boolean getLikedByUser() {
        return mLikedByUser;
    }

    public void setLikedByUser(Boolean liked_by_user) {
        mLikedByUser = liked_by_user;
    }

    public Long getLikes() {
        return mLikes;
    }

    public void setLikes(Long likes) {
        mLikes = likes;
    }

    public Links getLinks() {
        return mLinks;
    }

    public void setLinks(Links links) {
        mLinks = links;
    }

    public Urls getUrls() {
        return mUrls;
    }

    public void setUrls(Urls urls) {
        mUrls = urls;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public Long getWidth() {
        return mWidth;
    }

    public void setWidth(Long width) {
        mWidth = width;
    }

}
