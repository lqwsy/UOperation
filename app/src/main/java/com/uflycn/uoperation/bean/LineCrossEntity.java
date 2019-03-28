package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

@Entity(nameInDb = "operation_LineCross")
public class LineCrossEntity implements Serializable {
  /**
   * "sysLineCrossId": 2,
   "LineName": "下花II线",
   "VoltageClass": "35kV",
   "StartTower": "5",
   "StartTowerId": 654,
   "EndTower": "7",
   "EndTowerId": 656,
   "DtoSmartTower": 23,
   "Clearance": 5,
   "CrossAngle": null,
   "CrossImage": "LineCross/20170912/03ed447b4ebc49d99816d7e492a14522.jpg",
   "Remark": "",
   "CreateDate": "2017-09-06T16:03:54.19",
   "CrossLatitude": null,
   "CrossLongitude": null,
   "CrossType": 3944,
   "CrossTypeName": "电力线",
   "CrossStatus": "存在"
   * */

  @Id(autoincrement = true)
    private Long id;
    //private String sysUserId;

    private int PlatformId;
    private int StartTowerId;
    private int EndTowerId;

    private int CrossType;
    private double DtoSmartTower;
    private double Clearance;
    private double CrossAngle;
    private String CrossImage;
    private String CrossLatitude;
    private String CrossLongitude;
    private String Remark;
    private String CreatedTime;
    private String UpdatedTime;
    private int UploadFlag = 1;

    //下面数据库里面没有
    private String LineName;

    private String VoltageClass;

    private String StartTower;

    private String EndTower;

    private String CrossStatus;

    private String CrossTypeName;
    private String CreatedBy;

    @Transient
    private String CrossTypeFirst;

    @Transient
    private int sysTaskId;//工单id

    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;


    @Generated(hash = 341270113)
    public LineCrossEntity(Long id, int PlatformId, int StartTowerId,
            int EndTowerId, int CrossType, double DtoSmartTower, double Clearance,
            double CrossAngle, String CrossImage, String CrossLatitude,
            String CrossLongitude, String Remark, String CreatedTime,
            String UpdatedTime, int UploadFlag, String LineName,
            String VoltageClass, String StartTower, String EndTower,
            String CrossStatus, String CrossTypeName, String CreatedBy,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.id = id;
        this.PlatformId = PlatformId;
        this.StartTowerId = StartTowerId;
        this.EndTowerId = EndTowerId;
        this.CrossType = CrossType;
        this.DtoSmartTower = DtoSmartTower;
        this.Clearance = Clearance;
        this.CrossAngle = CrossAngle;
        this.CrossImage = CrossImage;
        this.CrossLatitude = CrossLatitude;
        this.CrossLongitude = CrossLongitude;
        this.Remark = Remark;
        this.CreatedTime = CreatedTime;
        this.UpdatedTime = UpdatedTime;
        this.UploadFlag = UploadFlag;
        this.LineName = LineName;
        this.VoltageClass = VoltageClass;
        this.StartTower = StartTower;
        this.EndTower = EndTower;
        this.CrossStatus = CrossStatus;
        this.CrossTypeName = CrossTypeName;
        this.CreatedBy = CreatedBy;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 1496399049)
    public LineCrossEntity() {
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPlatformId() {
        return PlatformId;
    }

    public void setPlatformId(int platformId) {
        PlatformId = platformId;
    }

    public int getStartTowerId() {
        return StartTowerId;
    }

    public void setStartTowerId(int startTowerId) {
        StartTowerId = startTowerId;
    }

    public int getEndTowerId() {
        return EndTowerId;
    }

    public void setEndTowerId(int endTowerId) {
        EndTowerId = endTowerId;
    }

    public int getCrossType() {
        return CrossType;
    }

    public void setCrossType(int crossType) {
        CrossType = crossType;
    }

    public double getDtoSmartTower() {
        return DtoSmartTower;
    }

    public String getCrossTypeFirst() {
        return CrossTypeFirst;
    }

    public void setCrossTypeFirst(String crossTypeFirst) {
        CrossTypeFirst = crossTypeFirst;
    }

    public void setDtoSmartTower(double dtoSmartTower) {
        DtoSmartTower = dtoSmartTower;
    }

    public double getClearance() {
        return Clearance;
    }

    public void setClearance(double clearance) {
        Clearance = clearance;
    }

    public double getCrossAngle() {
        return CrossAngle;
    }

    public void setCrossAngle(double crossAngle) {
        CrossAngle = crossAngle;
    }

    public String getCrossImage() {
        return CrossImage;
    }

    public void setCrossImage(String crossImage) {
        CrossImage = crossImage;
    }

    public String getCrossLatitude() {
        return CrossLatitude;
    }

    public void setCrossLatitude(String crossLatitude) {
        CrossLatitude = crossLatitude;
    }

    public String getCrossLongitude() {
        return CrossLongitude;
    }

    public void setCrossLongitude(String crossLongitude) {
        CrossLongitude = crossLongitude;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
    

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public String getStartTower() {
        return StartTower;
    }

    public void setStartTower(String startTower) {
        StartTower = startTower;
    }

    public String getEndTower() {
        return EndTower;
    }

    public void setEndTower(String endTower) {
        EndTower = endTower;
    }

    public String getCrossStatus() {
        return CrossStatus;
    }

    public void setCrossStatus(String crossStatus) {
        CrossStatus = crossStatus;
    }

    public String getCrossTypeName() {
        return CrossTypeName;
    }

    public void setCrossTypeName(String crossTypeName) {
        CrossTypeName = crossTypeName;
    }

    public int getSysTaskId() {
        return this.sysTaskId;
    }

    public void setSysTaskId(int sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public String getCreatedBy() {
        return this.CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public String getUpdatedTime() {
        return this.UpdatedTime;
    }

    public void setUpdatedTime(String UpdatedTime) {
        this.UpdatedTime = UpdatedTime;
    }

    public String getCreatedTime() {
        return this.CreatedTime;
    }

    public void setCreatedTime(String CreatedTime) {
        this.CreatedTime = CreatedTime;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
