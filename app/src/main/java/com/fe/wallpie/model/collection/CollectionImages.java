package com.fe.wallpie.model.collection;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CollectionImages implements Parcelable {

    @SerializedName("categories")
    private List<Object> mCategories;
    @SerializedName("color")
    private String mColor;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("current_user_collections")
    private List<Object> mCurrentUserCollections;
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

    public List<Object> getCategories() {
        return mCategories;
    }

    public void setCategories(List<Object> categories) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.mCategories);
        dest.writeString(this.mColor);
        dest.writeString(this.mCreatedAt);
        dest.writeList(this.mCurrentUserCollections);
        dest.writeValue(this.mHeight);
        dest.writeString(this.mId);
        dest.writeValue(this.mLikedByUser);
        dest.writeValue(this.mLikes);
        dest.writeParcelable(this.mLinks, flags);
        dest.writeParcelable(this.mUrls, flags);
        dest.writeParcelable(this.mUser, flags);
        dest.writeValue(this.mWidth);
    }

    public CollectionImages() {
    }

    protected CollectionImages(Parcel in) {
        this.mCategories = new ArrayList<Object>();
        in.readList(this.mCategories, Object.class.getClassLoader());
        this.mColor = in.readString();
        this.mCreatedAt = in.readString();
        this.mCurrentUserCollections = new ArrayList<Object>();
        in.readList(this.mCurrentUserCollections, Object.class.getClassLoader());
        this.mHeight = (Long) in.readValue(Long.class.getClassLoader());
        this.mId = in.readString();
        this.mLikedByUser = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mLikes = (Long) in.readValue(Long.class.getClassLoader());
        this.mLinks = in.readParcelable(Links.class.getClassLoader());
        this.mUrls = in.readParcelable(Urls.class.getClassLoader());
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.mWidth = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<CollectionImages> CREATOR = new Parcelable.Creator<CollectionImages>() {
        @Override
        public CollectionImages createFromParcel(Parcel source) {
            return new CollectionImages(source);
        }

        @Override
        public CollectionImages[] newArray(int size) {
            return new CollectionImages[size];
        }
    };
}
