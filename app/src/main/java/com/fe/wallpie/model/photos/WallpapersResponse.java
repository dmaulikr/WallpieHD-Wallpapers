
package com.fe.wallpie.model.photos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;


public class WallpapersResponse implements Parcelable {

    @SerializedName("categories")
    private List<Category> mCategories;
    @SerializedName("color")
    private String mColor;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("current_user_collections")
    private List<Object> mCurrentUserCollections;
    @SerializedName("height")
    private long mHeight;
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
    private long mWidth;

    public WallpapersResponse() {
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
        dest.writeLong(this.mHeight);
        dest.writeString(this.mId);
        dest.writeValue(this.mLikedByUser);
        dest.writeValue(this.mLikes);
        dest.writeParcelable(this.mLinks, flags);
        dest.writeParcelable(this.mUrls, flags);
        dest.writeParcelable(this.mUser, flags);
        dest.writeLong(this.mWidth);
    }

    protected WallpapersResponse(Parcel in) {
        this.mCategories = new ArrayList<Category>();
        in.readList(this.mCategories, Category.class.getClassLoader());
        this.mColor = in.readString();
        this.mCreatedAt = in.readString();
        this.mCurrentUserCollections = new ArrayList<Object>();
        in.readList(this.mCurrentUserCollections, Object.class.getClassLoader());
        this.mHeight = in.readLong();
        this.mId = in.readString();
        this.mLikedByUser = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mLikes = (Long) in.readValue(Long.class.getClassLoader());
        this.mLinks = in.readParcelable(Links.class.getClassLoader());
        this.mUrls = in.readParcelable(Urls.class.getClassLoader());
        this.mUser = in.readParcelable(User.class.getClassLoader());
        this.mWidth = in.readLong();
    }

    public static final Parcelable.Creator<WallpapersResponse> CREATOR = new Parcelable.Creator<WallpapersResponse>() {
        @Override
        public WallpapersResponse createFromParcel(Parcel source) {
            return new WallpapersResponse(source);
        }

        @Override
        public WallpapersResponse[] newArray(int size) {
            return new WallpapersResponse[size];
        }
    };
}
