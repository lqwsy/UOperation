package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "operation_defect_delete")
public class DefectDeleteRecord {
    @Id(autoincrement = true)
    private Long Id;

    private String LocalDefectId;
    private String PirctruePath;//照片路径
    private String CreateDate;//创建日期
    private String sysDefectId;//缺陷id
    private String Remark;//消缺描述
    private int UploadFlag;//上传标记
    private int DefectCategory;//缺陷类型 1 表示树障

    private Integer TreeCutCount;
    private String PlanDailyPlanSectionIDs;

    @Transient
    private int sysTaskId;


    public Integer getTreeCutCount() {
        return TreeCutCount;
    }

    public void setTreeCutCount(Integer treeCutCount) {
        TreeCutCount = treeCutCount;
    }

    public int getDefectCategory() {
        return this.DefectCategory;
    }

    public void setDefectCategory(int DefectCategory) {
        this.DefectCategory = DefectCategory;
    }

    public int getUploadFlag() {
        return this.UploadFlag;
    }

    public void setUploadFlag(int UploadFlag) {
        this.UploadFlag = UploadFlag;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getSysDefectId() {
        return this.sysDefectId;
    }

    public void setSysDefectId(String sysDefectId) {
        this.sysDefectId = sysDefectId;
    }

    public String getCreateDate() {
        return this.CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public String getPirctruePath() {
        return this.PirctruePath;
    }

    public void setPirctruePath(String PirctruePath) {
        this.PirctruePath = PirctruePath;
    }

    public String getLocalDefectId() {
        return this.LocalDefectId;
    }

    public void setLocalDefectId(String LocalDefectId) {
        this.LocalDefectId = LocalDefectId;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public int getSysTaskId() {
        return sysTaskId;
    }

    public void setSysTaskId(int sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 1092836115)
    public DefectDeleteRecord(Long Id, String LocalDefectId, String PirctruePath,
            String CreateDate, String sysDefectId, String Remark, int UploadFlag,
            int DefectCategory, Integer TreeCutCount, String PlanDailyPlanSectionIDs) {
        this.Id = Id;
        this.LocalDefectId = LocalDefectId;
        this.PirctruePath = PirctruePath;
        this.CreateDate = CreateDate;
        this.sysDefectId = sysDefectId;
        this.Remark = Remark;
        this.UploadFlag = UploadFlag;
        this.DefectCategory = DefectCategory;
        this.TreeCutCount = TreeCutCount;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 951829851)
    public DefectDeleteRecord() {
    }
    
}
