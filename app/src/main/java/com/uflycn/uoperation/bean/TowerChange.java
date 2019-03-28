package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "operation_TowerChanged")
public class TowerChange {
    @Id(autoincrement = true)
    private Long sysTowerChangedId;
    private String sysUserId;
    private int sysTowerId;
    private double Longitude;
    private double Latitude;
    private double Altitude;
    private String CreateDate;
    private int UploadFlag;
    private int Status;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
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

    public int getSysTowerId() {
        return this.sysTowerId;
    }

    public void setSysTowerId(int sysTowerId) {
        this.sysTowerId = sysTowerId;
    }

    public String getSysUserId() {
        return this.sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getSysTowerChangedId() {
        return this.sysTowerChangedId;
    }

    public void setSysTowerChangedId(Long sysTowerChangedId) {
        this.sysTowerChangedId = sysTowerChangedId;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 687276413)
    public TowerChange(Long sysTowerChangedId, String sysUserId, int sysTowerId,
            double Longitude, double Latitude, double Altitude, String CreateDate,
            int UploadFlag, int Status, String PlanDailyPlanSectionIDs,
            String sysPatrolExecutionID) {
        this.sysTowerChangedId = sysTowerChangedId;
        this.sysUserId = sysUserId;
        this.sysTowerId = sysTowerId;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.Altitude = Altitude;
        this.CreateDate = CreateDate;
        this.UploadFlag = UploadFlag;
        this.Status = Status;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 1139297639)
    public TowerChange() {
    }
}
