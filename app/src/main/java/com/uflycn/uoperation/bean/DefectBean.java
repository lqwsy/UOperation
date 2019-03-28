package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

@Entity(nameInDb = "t_TowerDefect")
public class DefectBean implements Serializable {
    /**
     * "sysTowerDefectId": 18329,
     "HandleAdvise": "",
     "Remark": "110kV珠坂线 1号杆塔,杆塔,角钢塔,塔身,倾斜,倾斜度10‰～15‰",
     "FoundedTime": "2019-01-03 10:17:53",
     "Latitude": 28.36432797,
     "Longitude": 115.113923,
     "LatitudeString": 28.36432797,
     "LongitudeString": 115.113923,
     "LineName": "珠坂线",
     "VoltageClassCode": "15",
     "VoltageClass": "110kV",
     "TowerNo": "1",
     "sysTowerID": 17948,
     "NearTowerNo": "",
     "NearTowerID": null,
     "ServerityLevel": 3,
     "ServerityLevelString": "一般",
     "DefectStateString": "已核实",
     "DefectCategory": 1,
     "DefectCategoryString": "精细化",
     "ImageCategory": "TowerDefect"
     */
    @Id(autoincrement = true)
    private Long id;//本地数据库Id

    @Property(nameInDb = "PlatformId")//平台Id
    private int sysTowerDefectId;

    private int sysPhotoInTaskID;//照片 当数据来源为人巡时，此处为空

    private int sysTaskId;//工单id

    private String TowerNo;
    private int sysTowerID;
    private String NearTowerNo;
    private int NearTowerID;

    private String HandleAdvise;
    private String Remark;
    private String FoundedTime;
    private double Latitude;
    private double Longitude;
    private String LineName;

    private int ServerityLevel;
    private String ServerityLevelString;
    private String DefectStateString;
    private String DefectCategory;
    private String ImageCategory;

    private int UploadFlag = 1;

    private String PicturePath;

    private String DefectTypeId;
    private String DefectCategoryString;

    private Integer sysApanageTaskId;

    @Transient
    private Integer sysGridLineID;//线路id

    @Transient
    private String sysDailyPlanSectionID;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;
    public String DefectPosition;
    public String Phase ;

    @Transient
    private boolean isChecked;

    @Transient
    private String VoltageClass;

    public String getDefectPosition() {
        return DefectPosition;
    }

    public void setDefectPosition(String defectPosition) {
        DefectPosition = defectPosition;
    }

    public String getPhase() {
        return Phase;
    }

    public void setPhase(String phase) {
        Phase = phase;
    }

    public Integer getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(Integer sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 2055471703)
    public DefectBean(Long id, int sysTowerDefectId, int sysPhotoInTaskID,
            int sysTaskId, String TowerNo, int sysTowerID, String NearTowerNo,
            int NearTowerID, String HandleAdvise, String Remark,
            String FoundedTime, double Latitude, double Longitude, String LineName,
            int ServerityLevel, String ServerityLevelString,
            String DefectStateString, String DefectCategory, String ImageCategory,
            int UploadFlag, String PicturePath, String DefectTypeId,
            String DefectCategoryString, Integer sysApanageTaskId,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID,
            String DefectPosition, String Phase) {
        this.id = id;
        this.sysTowerDefectId = sysTowerDefectId;
        this.sysPhotoInTaskID = sysPhotoInTaskID;
        this.sysTaskId = sysTaskId;
        this.TowerNo = TowerNo;
        this.sysTowerID = sysTowerID;
        this.NearTowerNo = NearTowerNo;
        this.NearTowerID = NearTowerID;
        this.HandleAdvise = HandleAdvise;
        this.Remark = Remark;
        this.FoundedTime = FoundedTime;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.LineName = LineName;
        this.ServerityLevel = ServerityLevel;
        this.ServerityLevelString = ServerityLevelString;
        this.DefectStateString = DefectStateString;
        this.DefectCategory = DefectCategory;
        this.ImageCategory = ImageCategory;
        this.UploadFlag = UploadFlag;
        this.PicturePath = PicturePath;
        this.DefectTypeId = DefectTypeId;
        this.DefectCategoryString = DefectCategoryString;
        this.sysApanageTaskId = sysApanageTaskId;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
        this.DefectPosition = DefectPosition;
        this.Phase = Phase;
    }

    @Generated(hash = 1091086457)
    public DefectBean() {
    }


    public String getSysDailyPlanSectionID() {
        return sysDailyPlanSectionID;
    }

    public void setSysDailyPlanSectionID(String sysDailyPlanSectionID) {
        this.sysDailyPlanSectionID = sysDailyPlanSectionID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSysTowerDefectId() {
        return sysTowerDefectId;
    }

    public void setSysTowerDefectId(int sysTowerDefectId) {
        this.sysTowerDefectId = sysTowerDefectId;
    }

    public int getSysPhotoInTaskID() {
        return sysPhotoInTaskID;
    }

    public void setSysPhotoInTaskID(int sysPhotoInTaskID) {
        this.sysPhotoInTaskID = sysPhotoInTaskID;
    }

    public String getTowerNo() {
        return TowerNo;
    }

    public void setTowerNo(String towerNo) {
        TowerNo = towerNo;
    }

    public int getSysTowerID() {
        return sysTowerID;
    }

    public void setSysTowerID(int sysTowerID) {
        this.sysTowerID = sysTowerID;
    }

    public String getNearTowerNo() {
        return NearTowerNo;
    }

    public void setNearTowerNo(String nearTowerNo) {
        NearTowerNo = nearTowerNo;
    }

    public int getNearTowerID() {
        return NearTowerID;
    }

    public void setNearTowerID(int nearTowerID) {
        NearTowerID = nearTowerID;
    }

    public String getHandleAdvise() {
        return HandleAdvise;
    }

    public void setHandleAdvise(String handleAdvise) {
        HandleAdvise = handleAdvise;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getFoundedTime() {
        return FoundedTime;
    }

    public void setFoundedTime(String foundedTime) {
        FoundedTime = foundedTime;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public int getServerityLevel() {
        return ServerityLevel;
    }

    public void setServerityLevel(int serverityLevel) {
        ServerityLevel = serverityLevel;
    }

    public String getServerityLevelString() {
        return ServerityLevelString;
    }

    public void setServerityLevelString(String serverityLevelString) {
        ServerityLevelString = serverityLevelString;
    }

    public String getDefectStateString() {
        return DefectStateString;
    }

    public void setDefectStateString(String defectStateString) {
        DefectStateString = defectStateString;
    }

    public String getDefectCategory() {
        return DefectCategory;
    }

    public void setDefectCategory(String defectCategory) {
        DefectCategory = defectCategory;
    }

    public String getImageCategory() {
        return ImageCategory;
    }

    public void setImageCategory(String imageCategory) {
        ImageCategory = imageCategory;
    }


    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public String getPicturePath() {
        return PicturePath;
    }

    public void setPicturePath(String picturePath) {
        PicturePath = picturePath;
    }

    public String getDefectTypeId() {
        return DefectTypeId;
    }

    public void setDefectTypeId(String defectTypeId) {
        DefectTypeId = defectTypeId;
    }

    public String getDefectCategoryString() {
        return DefectCategoryString;
    }

    public void setDefectCategoryString(String defectCategoryString) {
        DefectCategoryString = defectCategoryString;
    }

    public int getSysTaskId() {
        return this.sysTaskId;
    }

    public void setSysTaskId(int sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public Integer getSysApanageTaskId() {
        return this.sysApanageTaskId;
    }

    public void setSysApanageTaskId(Integer sysApanageTaskId) {
        this.sysApanageTaskId = sysApanageTaskId;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

}
