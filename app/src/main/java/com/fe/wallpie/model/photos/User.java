package com.fe.wallpie.model.photos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class User implements Parcelable {

    @SerializedName("bio")
    private String mBio;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("id")
    private String mId;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("links")
    private Links mLinks;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("name")
    private String mName;
    @SerializedName("portfolio_url")
    private String mPortfolioUrl;
    @SerializedName("profile_image")
    private ProfileImage mProfileImage;
    @SerializedName("total_collections")
    private Long mTotalCollections;
    @SerializedName("total_likes")
    private Long mTotalLikes;
    @SerializedName("total_photos")
    private Long mTotalPhotos;
    @SerializedName("username")
    private String mUsername;

    public User() {
    }

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        mBio = bio;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String first_name) {
        mFirstName = first_name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String last_name) {
        mLastName = last_name;
    }

    public Links getLinks() {
        return mLinks;
    }

    public void setLinks(Links links) {
        mLinks = links;
    }

    public Object getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPortfolioUrl() {
        return mPortfolioUrl;
    }

    public void setPortfolioUrl(String portfolio_url) {
        mPortfolioUrl = portfolio_url;
    }

    public ProfileImage getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(ProfileImage profile_image) {
        mProfileImage = profile_image;
    }

    public Long getTotalCollections() {
        return mTotalCollections;
    }

    public void setTotalCollections(Long total_collections) {
        mTotalCollections = total_collections;
    }

    public Long getTotalLikes() {
        return mTotalLikes;
    }

    public void setTotalLikes(Long total_likes) {
        mTotalLikes = total_likes;
    }

    public Long getTotalPhotos() {
        return mTotalPhotos;
    }

    public void setTotalPhotos(Long total_photos) {
        mTotalPhotos = total_photos;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBio);
        dest.writeString(this.mFirstName);
        dest.writeString(this.mId);
        dest.writeString(this.mLastName);
        dest.writeParcelable(this.mLinks, flags);
        dest.writeString(this.mLocation);
        dest.writeString(this.mName);
        dest.writeString(this.mPortfolioUrl);
        dest.writeParcelable(this.mProfileImage, flags);
        dest.writeValue(this.mTotalCollections);
        dest.writeValue(this.mTotalLikes);
        dest.writeValue(this.mTotalPhotos);
        dest.writeString(this.mUsername);
    }

    protected User(Parcel in) {
        this.mBio = in.readString();
        this.mFirstName = in.readString();
        this.mId = in.readString();
        this.mLastName = in.readString();
        this.mLinks = in.readParcelable(Links.class.getClassLoader());
        this.mLocation = in.readString();
        this.mName = in.readString();
        this.mPortfolioUrl = in.readString();
        this.mProfileImage = in.readParcelable(ProfileImage.class.getClassLoader());
        this.mTotalCollections = (Long) in.readValue(Long.class.getClassLoader());
        this.mTotalLikes = (Long) in.readValue(Long.class.getClassLoader());
        this.mTotalPhotos = (Long) in.readValue(Long.class.getClassLoader());
        this.mUsername = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
