package com.fe.wallpie.model.collections;


import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    private Long mId;
    @SerializedName("links")
    private Links mLinks;
    @SerializedName("photo_count")
    private Long mPhotoCount;
    @SerializedName("title")
    private String mTitle;

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

    public Long getPhotoCount() {
        return mPhotoCount;
    }

    public void setPhotoCount(Long photo_count) {
        mPhotoCount = photo_count;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
