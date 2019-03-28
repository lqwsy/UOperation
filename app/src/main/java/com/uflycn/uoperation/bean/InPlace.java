package com.uflycn.uoperation.bean;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "operation_InPlace")
public class InPlace {

    @Id(autoincrement = true)
    private Long sysInPlaceId;

    private String sysUserId;

    private double Longitude;

    private double Latitude;

    private double Altitude;

    @SerializedName("TowerId")
    private int sysTowerId;

    @SerializedName("NearTowerId")
    private int nearTowerId;


    private String PatrolType;

    private String CreateDate;
    private int UploadFlag;
    private String PlanDailyPlanSectionIDs="";
    private String sysPatrolExecutionID="";


    @Generated(hash = 1724476924)
    public InPlace(Long sysInPlaceId, String sysUserId, double Longitude,
            double Latitude, double Altitude, int sysTowerId, int nearTowerId,
            String PatrolType, String CreateDate, int UploadFlag,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.sysInPlaceId = sysInPlaceId;
        this.sysUserId = sysUserId;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.Altitude = Altitude;
        this.sysTowerId = sysTowerId;
        this.nearTowerId = nearTowerId;
        this.PatrolType = PatrolType;
        this.CreateDate = CreateDate;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 933612458)
    public InPlace() {
    }


    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public Long getSysInPlaceId() {
        return sysInPlaceId;
    }

    public void setSysInPlaceId(Long sysInPlaceId) {
        this.sysInPlaceId = sysInPlaceId;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getAltitude() {
        return Altitude;
    }

    public void setAltitude(double altitude) {
        Altitude = altitude;
    }

    public int getSysTowerId() {
        return sysTowerId;
    }

    public void setSysTowerId(int sysTowerId) {
        this.sysTowerId = sysTowerId;
    }

    public int getNearTowerId() {
        return nearTowerId;
    }

    public void setNearTowerId(int nearTowerId) {
        this.nearTowerId = nearTowerId;
    }

    public String getPatrolType() {
        return PatrolType;
    }

    public void setPatrolType(String patrolType) {
        PatrolType = patrolType;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
