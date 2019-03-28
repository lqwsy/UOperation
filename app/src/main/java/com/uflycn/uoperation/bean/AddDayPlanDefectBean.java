package com.uflycn.uoperation.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/3/5
 * Describe  : 新增日计划中的缺陷封装
 */
public class AddDayPlanDefectBean implements Parcelable {
    private Long DefectID;
    private Integer DefectType;
    private String lineName;
    private String towerNo;
    private String defectDesc;
    private String defectStatus;
    private Integer sysGridLineId;//线路id


    public Integer getSysGridLineId() {
        return sysGridLineId;
    }

    public void setSysGridLineId(Integer sysGridLineId) {
        this.sysGridLineId = sysGridLineId;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getTowerNo() {
        return towerNo;
    }

    public void setTowerNo(String towerNo) {
        this.towerNo = towerNo;
    }

    public String getDefectDesc() {
        return defectDesc;
    }

    public void setDefectDesc(String defectDesc) {
        this.defectDesc = defectDesc;
    }

    public String getDefectStatus() {
        return defectStatus;
    }

    public void setDefectStatus(String defectStatus) {
        this.defectStatus = defectStatus;
    }

    public Long getDefectID() {
        return DefectID;
    }

    public void setDefectID(Long defectID) {
        DefectID = defectID;
    }

    public int getDefectType() {
        return DefectType;
    }

    public void setDefectType(int defectType) {
        DefectType = defectType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.DefectID);
        dest.writeValue(this.DefectType);
        dest.writeString(this.lineName);
        dest.writeString(this.towerNo);
        dest.writeString(this.defectDesc);
        dest.writeString(this.defectStatus);
        dest.writeValue(this.sysGridLineId);
    }

    public AddDayPlanDefectBean() {
    }

    protected AddDayPlanDefectBean(Parcel in) {
        this.DefectID = (Long) in.readValue(Long.class.getClassLoader());
        this.DefectType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lineName = in.readString();
        this.towerNo = in.readString();
        this.defectDesc = in.readString();
        this.defectStatus = in.readString();
        this.sysGridLineId = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<AddDayPlanDefectBean> CREATOR = new Parcelable.Creator<AddDayPlanDefectBean>() {
        @Override
        public AddDayPlanDefectBean createFromParcel(Parcel source) {
            return new AddDayPlanDefectBean(source);
        }

        @Override
        public AddDayPlanDefectBean[] newArray(int size) {
            return new AddDayPlanDefectBean[size];
        }
    };
}
