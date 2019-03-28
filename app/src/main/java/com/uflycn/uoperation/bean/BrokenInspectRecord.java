package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "operation_broken_inspect")
public class BrokenInspectRecord {
    @Id(autoincrement = true)
    @Property(nameInDb = "sysBrokenInspectRecordId")
    private Long sysBrokenInspectRecordId;
    @Property(nameInDb = "PlatformId")
    private int sysBrokenPatrolDetailId;//平台ID

    private int BrokenDocumentId;//本地的 BrokenDocument Id
    private int DocumentPlatformId;//平台 的 BrokenDocument Id 用来上传记录

    private String PatrolImage;//照片路径
    private String Remark;//描述
    private int BrokenStatus;//状态

    private String CreateDate;//创建时间
    private int UploadFlag = 1;//是否已经上传  0 未上传  1 表示 已上传 默认是1

    @Transient
    private String BrokenStatusName;

    @Transient
    private int sysTaskId;

    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;


    @Generated(hash = 2023294429)
    public BrokenInspectRecord(Long sysBrokenInspectRecordId,
            int sysBrokenPatrolDetailId, int BrokenDocumentId,
            int DocumentPlatformId, String PatrolImage, String Remark,
            int BrokenStatus, String CreateDate, int UploadFlag,
            String PlanDailyPlanSectionIDs, String sysPatrolExecutionID) {
        this.sysBrokenInspectRecordId = sysBrokenInspectRecordId;
        this.sysBrokenPatrolDetailId = sysBrokenPatrolDetailId;
        this.BrokenDocumentId = BrokenDocumentId;
        this.DocumentPlatformId = DocumentPlatformId;
        this.PatrolImage = PatrolImage;
        this.Remark = Remark;
        this.BrokenStatus = BrokenStatus;
        this.CreateDate = CreateDate;
        this.UploadFlag = UploadFlag;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 687003680)
    public BrokenInspectRecord() {
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

    public Long getSysBrokenInspectRecordId() {
        return sysBrokenInspectRecordId;
    }

    public void setSysBrokenInspectRecordId(Long sysBrokenInspectRecordId) {
        this.sysBrokenInspectRecordId = sysBrokenInspectRecordId;
    }

    public int getSysBrokenPatrolDetailId() {
        return sysBrokenPatrolDetailId;
    }

    public void setSysBrokenPatrolDetailId(int sysBrokenPatrolDetailId) {
        this.sysBrokenPatrolDetailId = sysBrokenPatrolDetailId;
    }

    public int getBrokenDocumentId() {
        return BrokenDocumentId;
    }

    public void setBrokenDocumentId(int brokenDocumentId) {
        BrokenDocumentId = brokenDocumentId;
    }

    public int getDocumentPlatformId() {
        return DocumentPlatformId;
    }

    public void setDocumentPlatformId(int documentPlatformId) {
        DocumentPlatformId = documentPlatformId;
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

    public int getBrokenStatus() {
        return BrokenStatus;
    }

    public void setBrokenStatus(int brokenStatus) {
        BrokenStatus = brokenStatus;
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
    public String getBrokenStatusName() {
        if(BrokenStatus == 0){
            BrokenStatusName = "未消除";
        }else{
            BrokenStatusName = "已消除";
        }
        return BrokenStatusName;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
