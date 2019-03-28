package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */
@Entity(nameInDb = "operation_WorksheetApanageTask")
public class WorksheetApanageTask implements Serializable {

    @org.greenrobot.greendao.annotation.Id(autoincrement = false)
    private int sysApanageTaskId;

    private String TaskNo;   //工单编号

    private String TaskContent; //工单内容

    private int TowerA_ID;

    private int TowerB_ID;

    private int Status; //工单状态

    private String Operator;//班组

    private String OperationTime;//时间

    private String Remark;

    private boolean Deleted;

    private String CreatedBy;

    private String CreatedTime;

    private String UpdatedBy;

    private String UpdatedTime;

    private boolean UploadFlag;

    private String StatusString;

    private String Manager;

    private String LineName;

    private String TowerA_No;

    private String TowerB_No;

    private String PlanDailyPlanSectionIDs;


    @Transient
    private List<TreeDefectPointBean> TreeDefectPointList;

    @Transient
    private List<DefectBean> TowerDefectList;

    public boolean isDeleted() {
        return Deleted;
    }

    public boolean isUploadFlag() {
        return UploadFlag;
    }

    public List<TreeDefectPointBean> getTreeDefectPointBeen() {
        return TreeDefectPointList;
    }

    public void setTreeDefectPointBeen(List<TreeDefectPointBean> treeDefectPointBeen) {
        this.TreeDefectPointList = treeDefectPointBeen;
    }

    public List<DefectBean> getTowerDefectList() {
        return TowerDefectList;
    }

    public void setTowerDefectList(List<DefectBean> towerDefectList) {
        TowerDefectList = towerDefectList;
    }

    public String getTowerB_No() {
        return this.TowerB_No;
    }

    public void setTowerB_No(String TowerB_No) {
        this.TowerB_No = TowerB_No;
    }

    public String getTowerA_No() {
        return this.TowerA_No;
    }

    public void setTowerA_No(String TowerA_No) {
        this.TowerA_No = TowerA_No;
    }

    public String getLineName() {
        return this.LineName;
    }

    public void setLineName(String LineName) {
        this.LineName = LineName;
    }

    public String getManager() {
        return this.Manager;
    }

    public void setManager(String Manager) {
        this.Manager = Manager;
    }

    public String getStatusString() {
        return this.StatusString;
    }

    public void setStatusString(String StatusString) {
        this.StatusString = StatusString;
    }

    public boolean getUploadFlag() {
        return this.UploadFlag;
    }

    public void setUploadFlag(boolean UploadFlag) {
        this.UploadFlag = UploadFlag;
    }

    public String getUpdatedTime() {
        return this.UpdatedTime;
    }

    public void setUpdatedTime(String UpdatedTime) {
        this.UpdatedTime = UpdatedTime;
    }

    public String getUpdatedBy() {
        return this.UpdatedBy;
    }

    public void setUpdatedBy(String UpdatedBy) {
        this.UpdatedBy = UpdatedBy;
    }

    public String getCreatedTime() {
        return this.CreatedTime;
    }

    public void setCreatedTime(String CreatedTime) {
        this.CreatedTime = CreatedTime;
    }

    public String getCreatedBy() {
        return this.CreatedBy;
    }

    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    public boolean getDeleted() {
        return this.Deleted;
    }

    public void setDeleted(boolean Deleted) {
        this.Deleted = Deleted;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getOperationTime() {
        return this.OperationTime;
    }

    public void setOperationTime(String OperationTime) {
        this.OperationTime = OperationTime;
    }

    public String getOperator() {
        return this.Operator;
    }

    public void setOperator(String Operator) {
        this.Operator = Operator;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getTowerB_ID() {
        return this.TowerB_ID;
    }

    public void setTowerB_ID(int TowerB_ID) {
        this.TowerB_ID = TowerB_ID;
    }

    public int getTowerA_ID() {
        return this.TowerA_ID;
    }

    public void setTowerA_ID(int TowerA_ID) {
        this.TowerA_ID = TowerA_ID;
    }

    public String getTaskContent() {
        return this.TaskContent;
    }

    public void setTaskContent(String TaskContent) {
        this.TaskContent = TaskContent;
    }

    public String getTaskNo() {
        return this.TaskNo;
    }

    public void setTaskNo(String TaskNo) {
        this.TaskNo = TaskNo;
    }

    public int getSysApanageTaskId() {
        return this.sysApanageTaskId;
    }

    public void setSysApanageTaskId(int sysApanageTaskId) {
        this.sysApanageTaskId = sysApanageTaskId;
    }

    public String getPlanDailyPlanSectionIDs() {
        return this.PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String PlanDailyPlanSectionIDs) {
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 606671019)
    public WorksheetApanageTask(int sysApanageTaskId, String TaskNo, String TaskContent,
            int TowerA_ID, int TowerB_ID, int Status, String Operator, String OperationTime,
            String Remark, boolean Deleted, String CreatedBy, String CreatedTime,
            String UpdatedBy, String UpdatedTime, boolean UploadFlag, String StatusString,
            String Manager, String LineName, String TowerA_No, String TowerB_No,
            String PlanDailyPlanSectionIDs) {
        this.sysApanageTaskId = sysApanageTaskId;
        this.TaskNo = TaskNo;
        this.TaskContent = TaskContent;
        this.TowerA_ID = TowerA_ID;
        this.TowerB_ID = TowerB_ID;
        this.Status = Status;
        this.Operator = Operator;
        this.OperationTime = OperationTime;
        this.Remark = Remark;
        this.Deleted = Deleted;
        this.CreatedBy = CreatedBy;
        this.CreatedTime = CreatedTime;
        this.UpdatedBy = UpdatedBy;
        this.UpdatedTime = UpdatedTime;
        this.UploadFlag = UploadFlag;
        this.StatusString = StatusString;
        this.Manager = Manager;
        this.LineName = LineName;
        this.TowerA_No = TowerA_No;
        this.TowerB_No = TowerB_No;
        this.PlanDailyPlanSectionIDs = PlanDailyPlanSectionIDs;
    }

    @Generated(hash = 917588490)
    public WorksheetApanageTask() {
    }



}
