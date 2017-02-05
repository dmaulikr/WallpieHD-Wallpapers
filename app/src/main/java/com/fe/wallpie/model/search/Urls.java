package com.fe.wallpie.model.search;

import com.google.gson.annotations.SerializedName;

public class Urls {

    @SerializedName("full")
    private String mFull;
    @SerializedName("raw")
    private String mRaw;
    @SerializedName("regular")
    private String mRegular;
    @SerializedName("small")
    private String mSmall;
    @SerializedName("thumb")
    private String mThumb;

    public String getFull() {
        return mFull;
    }

    public void setFull(String full) {
        mFull = full;
    }

    public String getRaw() {
        return mRaw;
    }

    public void setRaw(String raw) {
        mRaw = raw;
    }

    public String getRegular() {
        return mRegular;
    }

    public void setRegular(String regular) {
        mRegular = regular;
    }

    public String getSmall() {
        return mSmall;
    }

    public void setSmall(String small) {
        mSmall = small;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(String thumb) {
        mThumb = thumb;
    }

}
