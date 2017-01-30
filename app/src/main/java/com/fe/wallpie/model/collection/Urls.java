
package com.fe.wallpie.model.collection;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Urls implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFull);
        dest.writeString(this.mRaw);
        dest.writeString(this.mRegular);
        dest.writeString(this.mSmall);
        dest.writeString(this.mThumb);
    }

    public Urls() {
    }

    protected Urls(Parcel in) {
        this.mFull = in.readString();
        this.mRaw = in.readString();
        this.mRegular = in.readString();
        this.mSmall = in.readString();
        this.mThumb = in.readString();
    }

    public static final Parcelable.Creator<Urls> CREATOR = new Parcelable.Creator<Urls>() {
        @Override
        public Urls createFromParcel(Parcel source) {
            return new Urls(source);
        }

        @Override
        public Urls[] newArray(int size) {
            return new Urls[size];
        }
    };
}
