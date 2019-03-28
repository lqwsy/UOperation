package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/9.
 */
@Entity(nameInDb = "operation_Project")
public class ProjectEntity implements Serializable {
    /**
     * "rowNum": 1,
     * "sysProjectId": 1006,
     * "VoltageClass": "220kV",
     * "GridLineName": "大仙Ⅰ线",
     * "StartTowerId": 9713,
     * "EndTowerId": 9789,
     * "StartTowerNo": "6",
     * "EndTowerNo": "82",
     * "TowerSection": "6-82",
     * "ProjectNo": "20170010",
     * "ProjectName": "大仙测试15",
     * "ProjectDescription": "233666",
     * "ProjectStatus": 0,
     * "ProjectStatusString": "新项目",
     * "CreatedBy": "admin",
     * "Deleted": true,
     * "ProjectImage": "ba631e1c-e335-4935-8740-80718077d60c.png",
     * "CreatedTime": "2017-10-11T16:10:32.323",
     * "CreatedTimeString": "2017-10-11 16:10",
     * "InspectTime": null,
     * "ListTime": nulls
     */
    @Id(autoincrement = true)
    private Long sysBrokenDocumentId;
    private int PlatformId; //平台id
    //    private int StartTowerId;
    //    private int EndTowerId;
    private int sysTaskId;//工单id
    private String ProjectNo;
    private String ProjectName;
    private String ProjectDescription;
    private String ProjectImage;
    private int ProjectStatus;
    private boolean Deleted;
    private String CreatedBy;
    private String CreatedTime;
    private String UpdatedBy;
    private String UpdatedTime;
    private int UploadFlag;

    private String ProjectManager;//项目负责人
    private String GridLineName;//线路名称

    private String VoltageClass;
    @Transient
    private String VClass;
    private String StartTowerNo;
    private String EndTowerNo;
    @Transient
    private String TowerSection;

    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;

    @Generated(hash = 79169854)
    public ProjectEntity(Long sysBrokenDocumentId, int PlatformId, int sysTaskId,
            String ProjectNo, String ProjectName, String ProjectDescription,
            String ProjectImage, int ProjectStatus, boolean Deleted, String CreatedBy,
            String CreatedTime, String UpdatedBy, String UpdatedTime, int UploadFlag,
            String ProjectManager, String GridLineName, String VoltageClass,
            String StartTowerNo, String EndTowerNo, String PlanDailyPlanSectionIDs,
            String sysPatrolExecutionID) {
        this.sysBrokenDocumentId = sysBrokenDocumentId;
        this.PlatformId = PlatformId;
        this.sysTaskId = sysTaskId;
        this.ProjectNo = ProjectNo;
        this.ProjectName = ProjectName;
        this.ProjectDescription = ProjectDescription;
        this.ProjectImage = ProjectImage;
        this.ProjectStatus = ProjectStatus;
        this.Deleted = Deleted;
        this.CreatedBy = CreatedBy;
        this.CreatedTime = CreatedTime;
        this.UpdatedBy = UpdatedBy;
        this.UpdatedTime = UpdatedTime;
        this.UploadFlag = UploadFlag;
        this.ProjectManager = ProjectManager;
        this.GridLineName = GridLineName;
        this.VoltageClass = VoltageClass;
        this.StartTowerNo = StartTowerNo;
        this.EndTowerNo = EndTowerNo;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    @Generated(hash = 939074542)
    public ProjectEntity() {
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public String getVClass() {
        return VClass;
    }

    public void setVClass(String VClass) {
        this.VClass = VClass;
    }

    public Long getSysBrokenDocumentId() {
        return sysBrokenDocumentId;
    }

    public void setSysBrokenDocumentId(Long sysBrokenDocumentId) {
        this.sysBrokenDocumentId = sysBrokenDocumentId;
    }

    public int getPlatformId() {
        return PlatformId;
    }

    public void setPlatformId(int platformId) {
        PlatformId = platformId;
    }


    public String getProjectNo() {
        return ProjectNo;
    }

    public void setProjectNo(String projectNo) {
        ProjectNo = projectNo;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProjectDescription() {
        return ProjectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        ProjectDescription = projectDescription;
    }

    public String getProjectImage() {
        return ProjectImage;
    }

    public void setProjectImage(String projectImage) {
        ProjectImage = projectImage;
    }

    public int getProjectStatus() {
        return ProjectStatus;
    }

    public void setProjectStatus(int projectStatus) {
        ProjectStatus = projectStatus;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public String getUpdatedBy() {
        return UpdatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        UpdatedBy = updatedBy;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        UpdatedTime = updatedTime;
    }

    public int getUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public String getGridLineName() {
        return GridLineName;
    }

    public void setGridLineName(String gridLineName) {
        GridLineName = gridLineName;
    }

    public String getStartTowerNo() {
        return StartTowerNo;
    }

    public void setStartTowerNo(String startTowerNo) {
        StartTowerNo = startTowerNo;
    }

    public String getEndTowerNo() {
        return EndTowerNo;
    }

    public void setEndTowerNo(String endTowerNo) {
        EndTowerNo = endTowerNo;
    }

    public String getTowerSection() {
        return TowerSection;
    }

    public void setTowerSection(String towerSection) {
        TowerSection = towerSection;
    }

    public String getProjectManager() {
        return ProjectManager;
    }

    public void setProjectManager(String projectManager) {
        ProjectManager = projectManager;
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "sysBrokenDocumentId=" + sysBrokenDocumentId +
                ", PlatformId=" + PlatformId +
                ", ProjectNo='" + ProjectNo + '\'' +
                ", ProjectName='" + ProjectName + '\'' +
                ", ProjectDescription='" + ProjectDescription + '\'' +
                ", ProjectImage='" + ProjectImage + '\'' +
                ", ProjectStatus=" + ProjectStatus +
                ", Deleted=" + Deleted +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", CreatedTime='" + CreatedTime + '\'' +
                ", UpdatedBy='" + UpdatedBy + '\'' +
                ", UpdatedTime='" + UpdatedTime + '\'' +
                ", UploadFlag=" + UploadFlag +
                ", ProjectManager='" + ProjectManager + '\'' +
                ", GridLineName='" + GridLineName + '\'' +
                ", VoltageClass='" + VoltageClass + '\'' +
                ", StartTowerNo='" + StartTowerNo + '\'' +
                ", EndTowerNo='" + EndTowerNo + '\'' +
                ", TowerSection='" + TowerSection + '\'' +
                '}';
    }

    public boolean getDeleted() {
        return this.Deleted;
    }

    public int getSysTaskId() {
        return this.sysTaskId;
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
}
