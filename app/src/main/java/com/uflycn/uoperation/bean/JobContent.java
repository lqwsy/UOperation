package com.uflycn.uoperation.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/26
 * Describe  :工作内容
 */
public class JobContent implements Parcelable {

    /**
     *  sysWorkContentID  :  f7cf59b0-24ed-4fcc-bac9-bd7152aa1661
     *  WorkContent  : 123
     *  SafetyMeasure  :  f7cf59b0-24ed-4fcc-bac9-bd7152aa1661
     *  SafetyPrecaution  : 123
     */

    private String sysWorkContentID;
    private String WorkContent;
    private String SafetyMeasure;
    private String SafetyPrecaution;

    public String getSysWorkContentID() {
        return sysWorkContentID;
    }

    public void setSysWorkContentID(String sysWorkContentID) {
        this.sysWorkContentID = sysWorkContentID;
    }

    public String getWorkContent() {
        return WorkContent;
    }

    public void setWorkContent(String workContent) {
        WorkContent = workContent;
    }

    public String getSafetyMeasure() {
        return SafetyMeasure;
    }

    public void setSafetyMeasure(String safetyMeasure) {
        SafetyMeasure = safetyMeasure;
    }

    public String getSafetyPrecaution() {
        return SafetyPrecaution;
    }

    public void setSafetyPrecaution(String safetyPrecaution) {
        SafetyPrecaution = safetyPrecaution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sysWorkContentID);
        dest.writeString(this.WorkContent);
        dest.writeString(this.SafetyMeasure);
        dest.writeString(this.SafetyPrecaution);
    }

    public JobContent() {
    }

    protected JobContent(Parcel in) {
        this.sysWorkContentID = in.readString();
        this.WorkContent = in.readString();
        this.SafetyMeasure = in.readString();
        this.SafetyPrecaution = in.readString();
    }

    public static final Parcelable.Creator<JobContent> CREATOR = new Parcelable.Creator<JobContent>() {
        @Override
        public JobContent createFromParcel(Parcel source) {
            return new JobContent(source);
        }

        @Override
        public JobContent[] newArray(int size) {
            return new JobContent[size];
        }
    };
}
