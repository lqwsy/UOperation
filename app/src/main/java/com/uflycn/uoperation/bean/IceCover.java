package com.uflycn.uoperation.bean;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by UF_PC on 2018/1/26.
 */
@Entity(nameInDb = "operation_IceCover")
public class IceCover {
    @org.greenrobot.greendao.annotation.Id(autoincrement = true)
    private Long Id;
    private String sysUserId;
    @SerializedName("sysTowerId")
    private int TowerId;

    private Double IceCoverHeight; //厚度
    private String IceType; //类型
    private Double Temperature;//温度
    private Double Wetness;//湿度
    private String CreateDate;
    private int UploadFlag;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;


    @Generated(hash = 867907489)
    public IceCover(Long Id, String sysUserId, int TowerId, Double IceCoverHeight,
            String IceType, Double Temperature, Double Wetness, String CreateDate,
            int UploadFlag, String PlanDailyPlanSectionIDs,
            String sysPatrolExecutionID) {
        this.Id = Id;
        this.sysUserId = sysUserId;
        this.TowerId = TowerId;
        this.IceCoverHeight = IceCoverHeight;
        this.IceType = IceType;
        this.Temperature = Temperature;
        this.Wetness = Wetness;
        this.CreateDate = CreateDate;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 1794718496)
    public IceCover() {
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public String getIceType() {
        return IceType;
    }

    public void setIceType(String iceType) {
        IceType = iceType;
    }

    public Double getTemperature() {
        return Temperature;
    }

    public void setTemperature(Double temperature) {
        Temperature = temperature;
    }

    public Double getWetness() {
        return Wetness;
    }

    public void setWetness(Double wetness) {
        Wetness = wetness;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public int getTowerId() {
        return TowerId;
    }

    public void setTowerId(int towerId) {
        TowerId = towerId;
    }

    public Double getIceCoverHeight() {
        return IceCoverHeight;
    }

    public void setIceCoverHeight(Double iceCoverHeight) {
        IceCoverHeight = iceCoverHeight;
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
