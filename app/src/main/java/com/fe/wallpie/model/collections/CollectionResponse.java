package com.fe.wallpie.model.collections;

import com.google.gson.annotations.SerializedName;

public class CollectionResponse {

    @SerializedName("cover_photo")
    private CoverPhoto mCoverPhoto;
    @SerializedName("curated")
    private Boolean mCurated;
    @SerializedName("description")
    private Object mDescription;
    @SerializedName("featured")
    private Boolean mFeatured;
    @SerializedName("id")
    private Long mId;
    @SerializedName("links")
    private Links mLinks;
    @SerializedName("private")
    private Boolean mPrivate;
    @SerializedName("published_at")
    private String mPublishedAt;
    @SerializedName("share_key")
    private String mShareKey;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("total_photos")
    private Long mTotalPhotos;
    @SerializedName("user")
    private User mUser;

    public CoverPhoto getCoverPhoto() {
        return mCoverPhoto;
    }

    public void setCoverPhoto(CoverPhoto cover_photo) {
        mCoverPhoto = cover_photo;
    }

    public Boolean getCurated() {
        return mCurated;
    }

    public void setCurated(Boolean curated) {
        mCurated = curated;
    }

    public Object getDescription() {
        return mDescription;
    }

    public void setDescription(Object description) {
        mDescription = description;
    }

    public Boolean getFeatured() {
        return mFeatured;
    }

    public void setFeatured(Boolean featured) {
        mFeatured = featured;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Links getLinks() {
        return mLinks;
    }

    public void setLinks(Links links) {
        mLinks = links;
    }

    public Boolean getPrivate() {
        return mPrivate;
    }

    public void setPrivate(Boolean mprivate) {
        this.mPrivate = mprivate;
    }

    public String getPublishedAt() {
        return mPublishedAt;
    }

    public void setPublishedAt(String published_at) {
        mPublishedAt = published_at;
    }

    public String getShareKey() {
        return mShareKey;
    }

    public void setShareKey(String share_key) {
        mShareKey = share_key;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Long getTotalPhotos() {
        return mTotalPhotos;
    }

    public void setTotalPhotos(Long total_photos) {
        mTotalPhotos = total_photos;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

}
