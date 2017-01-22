
package com.fe.wallpie.model.photo;


import com.google.gson.annotations.SerializedName;

public class Exif {

    @SerializedName("aperture")
    private String mAperture;
    @SerializedName("exposure_time")
    private String mExposureTime;
    @SerializedName("focal_length")
    private String mFocalLength;
    @SerializedName("iso")
    private Long mIso;
    @SerializedName("make")
    private String mMake;
    @SerializedName("model")
    private String mModel;

    public String getAperture() {
        return mAperture;
    }

    public void setAperture(String aperture) {
        mAperture = aperture;
    }

    public String getExposureTime() {
        return mExposureTime;
    }

    public void setExposureTime(String exposure_time) {
        mExposureTime = exposure_time;
    }

    public String getFocalLength() {
        return mFocalLength;
    }

    public void setFocalLength(String focal_length) {
        mFocalLength = focal_length;
    }

    public Long getIso() {
        return mIso;
    }

    public void setIso(Long iso) {
        mIso = iso;
    }

    public String getMake() {
        return mMake;
    }

    public void setMake(String make) {
        mMake = make;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

}
