package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by UF_PC on 2018/1/15.
 */
@Entity(nameInDb = "operation_WorksheetTask")
public class WorkSheetTask implements Serializable {
//    "sysTaskId": 7,
//    "TaskNo": "20180116006",
//    "TaskContent": null,
//    "BusinessId": 37,
//    "BusinessType": 5,
//    "CreatedTime": "2018-01-16T18:04:43.847",
//    "LineName": "220kV珠白线",
//    "DeviceId": 4307,
//    "DeviceNo": "29",
//    "DeviceType": 1,
//    "NearDeviceId": 4308,
//    "NearDeviceNo": "30",
//    "NearDeviceType": 1,

    @org.greenrobot.greendao.annotation.Id(autoincrement = false)
    private Long sysTaskId;

    private String TaskNo;

    private int BusinessId;//对应任务id

    private int BusinessType;//对应任务类型

    private Integer sysGridLineID;

    private String TaskContent;

    private int Status;//1、未完成 //2、已完成

    private String Operator;//班组

    private String OperationTime;//时间

    private String Remark;

    private boolean Deleted;

    private String CreatedTime;

    private boolean UploadFlag;

    private int DeviceId;

    private String DeviceNo;

    private int DeviceType;

    private Integer NearDeviceId;

    private String NearDeviceNo;

    private Integer NearDeviceType;

    private String CreatedBy;

    private String CreatedUser;
    private String Phone;

    private String LineName;

    private String ReceiverId;


    @Transient
    private Object WstObject; //工单实体类

//    "BrokenDocument": null,
//    "TowerDefect": null,
//    "TreeDefect": null,
//    "LineCross": null,
    @Transient
    private BrokenDocument BrokenDocument;

    @Transient
    private DefectBean TowerDefect;

    @Transient
    private TreeDefectPointBean TreeDefect;

    @Transient
    private LineCrossEntity LineCross;

    @Transient
    private ProjectEntity OptProject;
    private String PlanDailyPlanSectionIDs;


    @Generated(hash = 2088797301)
    public WorkSheetTask(Long sysTaskId, String TaskNo, int BusinessId, int BusinessType,
            Integer sysGridLineID, String TaskContent, int Status, String Operator,
            String OperationTime, String Remark, boolean Deleted, String CreatedTime,
            boolean UploadFlag, int DeviceId, String DeviceNo, int DeviceType,
            Integer NearDeviceId, String NearDeviceNo, Integer NearDeviceType,
            String CreatedBy, String CreatedUser, String Phone, String LineName,
            String ReceiverId, String PlanDailyPlanSectionIDs) {
        this.sysTaskId = sysTaskId;
        this.TaskNo = TaskNo;
        this.BusinessId = BusinessId;
        this.BusinessType = BusinessType;
        this.sysGridLineID = sysGridLineID;
        this.TaskContent = TaskContent;
        this.Status = Status;
        this.Operator = Operator;
        this.OperationTime = OperationTime;
        this.Remark = Remark;
        this.Deleted = Deleted;
        this.CreatedTime = CreatedTime;
        this.UploadFlag = UploadFlag;
        this.DeviceId = DeviceId;
        this.DeviceNo = DeviceNo;
        this.DeviceType = DeviceType;
        this.NearDeviceId = NearDeviceId;
        this.NearDeviceNo = NearDeviceNo;
        this.NearDeviceType = NearDeviceType;
        this.CreatedBy = CreatedBy;
        this.CreatedUser = CreatedUser;
        this.Phone = Phone;
        this.LineName = LineName;
        this.ReceiverId = ReceiverId;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 1298726659)
    public WorkSheetTask() {
    }


    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }

    public ProjectEntity getOptProject() {
        return OptProject;
    }

    public void setOptProject(ProjectEntity optProject) {
        OptProject = optProject;
    }

    public LineCrossEntity getLineCross() {
        return LineCross;
    }

    public void setLineCross(LineCrossEntity lineCross) {
        LineCross = lineCross;
    }

    public TreeDefectPointBean getTreeDefect() {
        return TreeDefect;
    }

    public void setTreeDefect(TreeDefectPointBean treeDefect) {
        TreeDefect = treeDefect;
    }

    public DefectBean getTowerDefect() {
        return TowerDefect;
    }

    public void setTowerDefect(DefectBean towerDefect) {
        TowerDefect = towerDefect;
    }

    public com.uflycn.uoperation.bean.BrokenDocument getBrokenDocument() {
        return BrokenDocument;
    }

    public void setBrokenDocument(com.uflycn.uoperation.bean.BrokenDocument brokenDocument) {
        BrokenDocument = brokenDocument;
    }



    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public Long getSysTaskId() {
        return sysTaskId;
    }

    public void setSysTaskId(Long sysTaskId) {
        this.sysTaskId = sysTaskId;
    }

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }

    public int getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(int businessId) {
        BusinessId = businessId;
    }

    public int getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(int businessType) {
        BusinessType = businessType;
    }

    public Integer getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(Integer sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public String getTaskContent() {
        return TaskContent;
    }

    public void setTaskContent(String taskContent) {
        TaskContent = taskContent;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public String getOperationTime() {
        return OperationTime;
    }

    public void setOperationTime(String operationTime) {
        OperationTime = operationTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public boolean isUploadFlag() {
        return UploadFlag;
    }

    public void setUploadFlag(boolean uploadFlag) {
        UploadFlag = uploadFlag;
    }

    public int getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(int deviceId) {
        DeviceId = deviceId;
    }

    public Integer getNearDeviceId() {
        return NearDeviceId;
    }

    public void setNearDeviceId(Integer nearDeviceId) {
        NearDeviceId = nearDeviceId;
    }

    public Object getWstObject() {
        return WstObject;
    }

    public void setWstObject(Object wstObject) {
        WstObject = wstObject;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public boolean getUploadFlag() {
        return this.UploadFlag;
    }

    public boolean getDeleted() {
        return this.Deleted;
    }

    public Integer getNearDeviceType() {
        return this.NearDeviceType;
    }

    public void setNearDeviceType(Integer NearDeviceType) {
        this.NearDeviceType = NearDeviceType;
    }

    public String getNearDeviceNo() {
        return this.NearDeviceNo;
    }

    public void setNearDeviceNo(String NearDeviceNo) {
        this.NearDeviceNo = NearDeviceNo;
    }

    public int getDeviceType() {
        return this.DeviceType;
    }

    public void setDeviceType(int DeviceType) {
        this.DeviceType = DeviceType;
    }

    public String getDeviceNo() {
        return this.DeviceNo;
    }

    public void setDeviceNo(String DeviceNo) {
        this.DeviceNo = DeviceNo;
    }

    public String getPhone() {
        return this.Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getCreatedUser() {
        return this.CreatedUser;
    }

    public void setCreatedUser(String CreatedUser) {
        this.CreatedUser = CreatedUser;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }
}
