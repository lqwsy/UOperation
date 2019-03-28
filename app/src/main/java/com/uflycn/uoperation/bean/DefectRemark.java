package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "operation_defect_remark")
public class DefectRemark {
    /**
     * "CreatedTime": "2017-09-21T22:46:47.46",
     * "Remark": "测试"
     */
    @Id(autoincrement = true)
    private Long Id;
    private int defectId;//平台缺陷Id
    private int localDefectId;// 本地缺陷Id
    private String CreatedTime;
    private String Remark;
    private int UploadFlag = 1;//上传标记
    private String DefectType;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;

    @Generated(hash = 1458728878)
    public DefectRemark(Long Id, int defectId, int localDefectId,
            String CreatedTime, String Remark, int UploadFlag, String DefectType,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.Id = Id;
        this.defectId = defectId;
        this.localDefectId = localDefectId;
        this.CreatedTime = CreatedTime;
        this.Remark = Remark;
        this.UploadFlag = UploadFlag;
        this.DefectType = DefectType;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 1305376165)
    public DefectRemark() {
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public int getDefectId() {
        return defectId;
    }

    public void setDefectId(int defectId) {
        this.defectId = defectId;
    }

    public int getLocalDefectId() {
        return localDefectId;
    }

    public void setLocalDefectId(int localDefectId) {
        this.localDefectId = localDefectId;
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

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public String getDefectType() {
        return DefectType;
    }

    public void setDefectType(String defectType) {
        DefectType = defectType;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
