package com.uflycn.uoperation.bean;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Administrator on 2017/9/7.
 */
@Entity(nameInDb = "operation_PatrolTrack")
public class PatrolTrack {
    @Id(autoincrement = true)
    @Property(nameInDb = "sysPatrolTrackId")
    private Long Id;//
    private String sysUserId;//登录用户ID
    private double Longitude;
    private double Latitude;
    private double Altitude;
    private String CreateDate;
    private int UploadFlag;
    private String PatrolTypeId;
    private int sysTowerId;//到位登记杆塔号
    private int NearTowerId;//区段第二级杆塔
    private String PatrolType;//巡视种类
    @SerializedName("PlanDailyPlanSectionIDs")
    private String sysDailyPlanSectionID;
    private String sysPatrolExecutionID;
    private String DailyPlanTimeSpanIDs;
    public String getDailyPlanTimeSpanIDs() {
        return this.DailyPlanTimeSpanIDs;
    }
    public void setDailyPlanTimeSpanIDs(String DailyPlanTimeSpanIDs) {
        this.DailyPlanTimeSpanIDs = DailyPlanTimeSpanIDs;
    }
    public String getSysPatrolExecutionID() {
        return this.sysPatrolExecutionID;
    }
    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }
    public String getSysDailyPlanSectionID() {
        return this.sysDailyPlanSectionID;
    }
    public void setSysDailyPlanSectionID(String sysDailyPlanSectionID) {
        this.sysDailyPlanSectionID = sysDailyPlanSectionID;
    }
    public String getPatrolType() {
        return this.PatrolType;
    }
    public void setPatrolType(String PatrolType) {
        this.PatrolType = PatrolType;
    }
    public int getNearTowerId() {
        return this.NearTowerId;
    }
    public void setNearTowerId(int NearTowerId) {
        this.NearTowerId = NearTowerId;
    }
    public int getSysTowerId() {
        return this.sysTowerId;
    }
    public void setSysTowerId(int sysTowerId) {
        this.sysTowerId = sysTowerId;
    }
    public String getPatrolTypeId() {
        return this.PatrolTypeId;
    }
    public void setPatrolTypeId(String PatrolTypeId) {
        this.PatrolTypeId = PatrolTypeId;
    }
    public int getUploadFlag() {
        return this.UploadFlag;
    }
    public void setUploadFlag(int UploadFlag) {
        this.UploadFlag = UploadFlag;
    }
    public String getCreateDate() {
        return this.CreateDate;
    }
    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }
    public double getAltitude() {
        return this.Altitude;
    }
    public void setAltitude(double Altitude) {
        this.Altitude = Altitude;
    }
    public double getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }
    public double getLongitude() {
        return this.Longitude;
    }
    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }
    public String getSysUserId() {
        return this.sysUserId;
    }
    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    @Generated(hash = 197856851)
    public PatrolTrack(Long Id, String sysUserId, double Longitude,
            double Latitude, double Altitude, String CreateDate, int UploadFlag,
            String PatrolTypeId, int sysTowerId, int NearTowerId,
            String PatrolType, String sysDailyPlanSectionID,
            String sysPatrolExecutionID, String DailyPlanTimeSpanIDs) {
        this.Id = Id;
        this.sysUserId = sysUserId;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.Altitude = Altitude;
        this.CreateDate = CreateDate;
        this.UploadFlag = UploadFlag;
        this.PatrolTypeId = PatrolTypeId;
        this.sysTowerId = sysTowerId;
        this.NearTowerId = NearTowerId;
        this.PatrolType = PatrolType;
        this.sysDailyPlanSectionID = sysDailyPlanSectionID;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
        this.DailyPlanTimeSpanIDs = DailyPlanTimeSpanIDs;
    }
    @Generated(hash = 1269289269)
    public PatrolTrack() {
    }


   
}
