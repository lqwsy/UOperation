package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by UF_PC on 2017/10/22.
 */
@Entity(nameInDb = "operation_ProjectInspection")
public class ProjectInspection {

    @Id(autoincrement = true)
    private Long sysProjectInspectionId;//

    private int sysProjectId; //平台项目Id

    private int localProjectId;//项目在本地数据库的Id

    private String sysUserId;//登录用户ID

    private int State;//隐患状态    0:未消除；1：已消除

    private String PatrolImage;//特巡照片

    private String Remark;//备注

    private String CreateDate;//创建时间

    private String CheckedTime;//检查日期

    private int UploadFlag = 1;//上传标记

    @Transient
    private int sysTaskId;
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;


    @Generated(hash = 314409446)
    public ProjectInspection(Long sysProjectInspectionId, int sysProjectId,
            int localProjectId, String sysUserId, int State, String PatrolImage,
            String Remark, String CreateDate, String CheckedTime, int UploadFlag,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.sysProjectInspectionId = sysProjectInspectionId;
        this.sysProjectId = sysProjectId;
        this.localProjectId = localProjectId;
        this.sysUserId = sysUserId;
        this.State = State;
        this.PatrolImage = PatrolImage;
        this.Remark = Remark;
        this.CreateDate = CreateDate;
        this.CheckedTime = CheckedTime;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 1694920012)
    public ProjectInspection() {
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

    public Long getSysProjectInspectionId() {
        return sysProjectInspectionId;
    }

    public void setSysProjectInspectionId(Long sysProjectInspectionId) {
        this.sysProjectInspectionId = sysProjectInspectionId;
    }

    public int getSysProjectId() {
        return sysProjectId;
    }

    public void setSysProjectId(int sysProjectId) {
        this.sysProjectId = sysProjectId;
    }

    public int getLocalProjectId() {
        return localProjectId;
    }

    public void setLocalProjectId(int localProjectId) {
        this.localProjectId = localProjectId;
    }

    public String getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(String sysUserId) {
        this.sysUserId = sysUserId;
    }

    public int getProjectStatus() {
        return State;
    }

    public void setProjectStatus(int projectStatus) {
        State = projectStatus;
    }

    public String getPatrolImage() {
        return PatrolImage;
    }

    public void setPatrolImage(String patrolImage) {
        PatrolImage = patrolImage;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getCheckedTime() {
        return CheckedTime;
    }

    public void setCheckedTime(String checkedTime) {
        CheckedTime = checkedTime;
    }

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public int getState() {
        return this.State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
