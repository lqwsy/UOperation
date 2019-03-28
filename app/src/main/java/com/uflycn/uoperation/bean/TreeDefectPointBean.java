package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * 树障列表
 */
@Entity(nameInDb = "operation_TreeDefect")
public class TreeDefectPointBean implements Serializable {
    /**
     * "sysTreeDefectPointID": 18406,
     "LineName": "珠坂线",
     "VoltageClass": "110kV",
     "TowerA_Id": 5351,
     "TowerA_Name": "4",
     "TowerB_Name": "5",
     "TowerRegion": "4 - 5",
     "DistanceFromTower": 41.2430878,
     "DistanceFromLine": 4.348372,
     "DistanceFromLineH": 2.892663,
     "DistanceFromLineV": 3.246666,
     "Latitude": 30.6828842,
     "Longitude": 113.999123,
     "LatitudeString": 30.6828842,
     "LongitudeString": 113.999123,
     "DefectStateString": "待处理",
     "FoundedTime": "2018-02-05 17:40:27",
     "Type": 1,
     "TreeDefectType": "树障",
     "DefectLevel": 3,
     "TreeDefectPointType": "危急",
     "Remark": "110kV珠坂线4 - 5杆段内，有树距线净空距离为4.35米",
     "ImageCategory": "TreeDefect"
     */
    @Id(autoincrement = false)
    @Property(nameInDb = "sysTreeDefectPointID")
    private Long sysTreeDefectPointID;
    private int sysTaskId;//工单id
    private String LineName;
    private String VoltageClassName;
    private String TreeDefectPointType;
    private String TowerA_Name;
    private String TowerB_Name;
    private String TowerRegion;
    private int TowerA_Id;
    private int TowerB_Id;
    private double DistanceFromTower;
    private double DistanceFromLine;
    private double DistanceFromLineH;
    private double DistanceFromLineV;
    private double Latitude;
    private double Longitude;
    private String LatitudeString;
    private String LongitudeString;
    private String TreeDefectType;
    private float Temperature;
    private String TreeSeed;
    private String TreeSeedNumber;
    @Transient
    private Integer sysGridLineID;

    public int getTowerB_Id() {
        return TowerB_Id;
    }

    public void setTowerB_Id(int towerB_Id) {
        TowerB_Id = towerB_Id;
    }

    public float getTemperature() {
        return Temperature;
    }

    public void setTemperature(float temperature) {
        Temperature = temperature;
    }

    public String getTreeSeed() {
        return TreeSeed;
    }

    public void setTreeSeed(String treeSeed) {
        TreeSeed = treeSeed;
    }

    public String getTreeSeedNumber() {
        return TreeSeedNumber;
    }

    public void setTreeSeedNumber(String treeSeedNumber) {
        TreeSeedNumber = treeSeedNumber;
    }

    public Integer getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(Integer sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public String getTreeDefectType() {
        return TreeDefectType;
    }

    public void setTreeDefectType(String treeDefectType) {
        TreeDefectType = treeDefectType;
    }

    /*   private Object sysTreeDefectVerificationID;
           private Object DefectState;*/
    private String DefectStateString;
    private String FoundTime;
    private String CreatedTime;
    private String Remark;
    private String ImageCategory;
    private int UploadFlag = 1;
    private String Category;//区分缺陷类别
    @Transient
    private boolean isSelect = true;

    private Integer sysApanageTaskId;
    private String PlanDailyPlanSectionIDs;
    @Transient
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Generated(hash = 316730949)
    public TreeDefectPointBean(Long sysTreeDefectPointID, int sysTaskId, String LineName,
            String VoltageClassName, String TreeDefectPointType, String TowerA_Name,
            String TowerB_Name, String TowerRegion, int TowerA_Id, int TowerB_Id,
            double DistanceFromTower, double DistanceFromLine, double DistanceFromLineH,
            double DistanceFromLineV, double Latitude, double Longitude, String LatitudeString,
            String LongitudeString, String TreeDefectType, float Temperature, String TreeSeed,
            String TreeSeedNumber, String DefectStateString, String FoundTime, String CreatedTime,
            String Remark, String ImageCategory, int UploadFlag, String Category,
            Integer sysApanageTaskId, String PlanDailyPlanSectionIDs) {
        this.sysTreeDefectPointID = sysTreeDefectPointID;
        this.sysTaskId = sysTaskId;
        this.LineName = LineName;
        this.VoltageClassName = VoltageClassName;
        this.TreeDefectPointType = TreeDefectPointType;
        this.TowerA_Name = TowerA_Name;
        this.TowerB_Name = TowerB_Name;
        this.TowerRegion = TowerRegion;
        this.TowerA_Id = TowerA_Id;
        this.TowerB_Id = TowerB_Id;
        this.DistanceFromTower = DistanceFromTower;
        this.DistanceFromLine = DistanceFromLine;
        this.DistanceFromLineH = DistanceFromLineH;
        this.DistanceFromLineV = DistanceFromLineV;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.LatitudeString = LatitudeString;
        this.LongitudeString = LongitudeString;
        this.TreeDefectType = TreeDefectType;
        this.Temperature = Temperature;
        this.TreeSeed = TreeSeed;
        this.TreeSeedNumber = TreeSeedNumber;
        this.DefectStateString = DefectStateString;
        this.FoundTime = FoundTime;
        this.CreatedTime = CreatedTime;
        this.Remark = Remark;
        this.ImageCategory = ImageCategory;
        this.UploadFlag = UploadFlag;
        this.Category = Category;
        this.sysApanageTaskId = sysApanageTaskId;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 1023596479)
    public TreeDefectPointBean() {
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getTowerA_Id() {
        return TowerA_Id;
    }

    public void setTowerA_Id(int towerA_Id) {
        TowerA_Id = towerA_Id;
    }

    public Long getSysTreeDefectPointID() {
        return sysTreeDefectPointID;
    }

    public void setSysTreeDefectPointID(Long sysTreeDefectPointID) {
        this.sysTreeDefectPointID = sysTreeDefectPointID;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getVoltageClassName() {
        return VoltageClassName;
    }

    public void setVoltageClassName(String voltageClassName) {
        VoltageClassName = voltageClassName;
    }

    public String getTreeDefectPointType() {
        return TreeDefectPointType;
    }

    public void setTreeDefectPointType(String treeDefectPointType) {
        TreeDefectPointType = treeDefectPointType;
    }

    public String getTowerA_Name() {
        return TowerA_Name;
    }

    public void setTowerA_Name(String towerA_Name) {
        TowerA_Name = towerA_Name;
    }

    public String getTowerB_Name() {
        return TowerB_Name;
    }

    public void setTowerB_Name(String towerB_Name) {
        TowerB_Name = towerB_Name;
    }

    public String getTowerRegion() {
        return TowerRegion;
    }

    public void setTowerRegion(String towerRegion) {
        TowerRegion = towerRegion;
    }

    public double getDistanceFromTower() {
        return DistanceFromTower;
    }

    public void setDistanceFromTower(double distanceFromTower) {
        DistanceFromTower = distanceFromTower;
    }

    public double getDistanceFromLine() {
        return DistanceFromLine;
    }

    public void setDistanceFromLine(double distanceFromLine) {
        DistanceFromLine = distanceFromLine;
    }

    public double getDistanceFromLineH() {
        return DistanceFromLineH;
    }

    public void setDistanceFromLineH(double distanceFromLineH) {
        DistanceFromLineH = distanceFromLineH;
    }

    public double getDistanceFromLineV() {
        return DistanceFromLineV;
    }

    public void setDistanceFromLineV(double distanceFromLineV) {
        DistanceFromLineV = distanceFromLineV;
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

    public String getLatitudeString() {
        return LatitudeString;
    }

    public void setLatitudeString(String latitudeString) {
        LatitudeString = latitudeString;
    }

    public String getLongitudeString() {
        return LongitudeString;
    }

    public void setLongitudeString(String longitudeString) {
        LongitudeString = longitudeString;
    }

    public String getDefectStateString() {
        return DefectStateString;
    }

    public void setDefectStateString(String defectStateString) {
        DefectStateString = defectStateString;
    }

    public String getFoundTime() {
        return FoundTime;
    }

    public void setFoundTime(String foundTime) {
        FoundTime = foundTime;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
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
