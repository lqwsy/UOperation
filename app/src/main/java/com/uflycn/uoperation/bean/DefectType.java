package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "t_DefectType")
public class DefectType {
    @Id(autoincrement = false)
    @Property(nameInDb = "sysDefectTypeID")
    private Long sysDefectTypeID;//"sysDefectTypeID": 8,

    private int DefectParentId;//
    private int DefectCategory;//DefectCategory": 5,
    private int DefectLevel;//"DefectLevel": 2,
    private String DefectName;//"DefectName": "测试线路_其他问题",
    private String DefectNo;//"Other No.01",
    private int SeverityLevel;// "SeverityLevel": 1,
    private int Standard;// "Standard": 0,
    private boolean Deleted;// "Deleted": true

    @Generated(hash = 1585498403)
    public DefectType(Long sysDefectTypeID, int DefectParentId, int DefectCategory,
            int DefectLevel, String DefectName, String DefectNo, int SeverityLevel,
            int Standard, boolean Deleted) {
        this.sysDefectTypeID = sysDefectTypeID;
        this.DefectParentId = DefectParentId;
        this.DefectCategory = DefectCategory;
        this.DefectLevel = DefectLevel;
        this.DefectName = DefectName;
        this.DefectNo = DefectNo;
        this.SeverityLevel = SeverityLevel;
        this.Standard = Standard;
        this.Deleted = Deleted;
    }

    @Generated(hash = 1750798429)
    public DefectType() {
    }

    public Long getSysDefectTypeID() {
        return sysDefectTypeID;
    }

    public void setSysDefectTypeID(Long sysDefectTypeID) {
        this.sysDefectTypeID = sysDefectTypeID;
    }

    public int getDefectParentId() {
        return DefectParentId;
    }

    public void setDefectParentId(int defectParentId) {
        DefectParentId = defectParentId;
    }

    public int getDefectCategory() {
        return DefectCategory;
    }

    public void setDefectCategory(int defectCategory) {
        DefectCategory = defectCategory;
    }

    public int getDefectLevel() {
        return DefectLevel;
    }

    public void setDefectLevel(int defectLevel) {
        DefectLevel = defectLevel;
    }

    public String getDefectName() {
        return DefectName;
    }

    public void setDefectName(String defectName) {
        DefectName = defectName;
    }

    public String getDefectNo() {
        return DefectNo;
    }

    public void setDefectNo(String defectNo) {
        DefectNo = defectNo;
    }

    public int getSeverityLevel() {
        return SeverityLevel;
    }

    public void setSeverityLevel(int severityLevel) {
        SeverityLevel = severityLevel;
    }

    public int getStandard() {
        return Standard;
    }

    public void setStandard(int standard) {
        Standard = standard;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public boolean getDeleted() {
        return this.Deleted;
    }
}


