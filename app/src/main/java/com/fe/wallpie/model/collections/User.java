package com.fe.wallpie.model.collections;

import com.google.gson.annotations.SerializedName;


public class User {

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
    private Object mPortfolioUrl;
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

    public String getLocation() {
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

    public Object getPortfolioUrl() {
        return mPortfolioUrl;
    }

    public void setPortfolioUrl(Object portfolio_url) {
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

}
