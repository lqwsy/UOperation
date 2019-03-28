package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "operation_crossDelete")
public class LineCrossDelete {
    @Id
    private Long Id;
    private String PictruePath;//图片
    private int sysLineCrossId;
    private String Remark;//描述
    private int UploadFlag;// 上传
    private String CheckerId;
    private String CheckedTime;
    @Transient
    private int sysTaskId;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;

    @Generated(hash = 847991230)
    public LineCrossDelete(Long Id, String PictruePath, int sysLineCrossId,
            String Remark, int UploadFlag, String CheckerId, String CheckedTime,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.Id = Id;
        this.PictruePath = PictruePath;
        this.sysLineCrossId = sysLineCrossId;
        this.Remark = Remark;
        this.UploadFlag = UploadFlag;
        this.CheckerId = CheckerId;
        this.CheckedTime = CheckedTime;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 748763755)
    public LineCrossDelete() {
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public int getSysTaskId() {
        return sysTaskId;
    }

    public void setSysTaskId(int sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getPictruePath() {
        return PictruePath;
    }

    public void setPictruePath(String pictruePath) {
        PictruePath = pictruePath;
    }

    public int getSysLineCrossId() {
        return sysLineCrossId;
    }

    public void setSysLineCrossId(int sysLineCrossId) {
        this.sysLineCrossId = sysLineCrossId;
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

    public String getCheckerId() {
        return CheckerId;
    }

    public void setCheckerId(String checkerId) {
        CheckerId = checkerId;
    }

    public String getCheckedTime() {
        return CheckedTime;
    }

    public void setCheckedTime(String checkedTime) {
        CheckedTime = checkedTime;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
