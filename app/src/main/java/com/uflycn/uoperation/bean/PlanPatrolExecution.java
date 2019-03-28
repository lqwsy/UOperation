package com.uflycn.uoperation.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 任务列表
 */
public class PlanPatrolExecution implements Serializable {


    /**
     * sysPatrolExecutionID : aa85ce2f-d155-4fe1-8000-dcd519563ee2
     * StartDateString : 2019-02-28
     * EndDateString : 2019-03-01
     * TaskName : 测试23333
     * TypeString : 巡视
     * ResponsibleClassName : 运维一班
     * ResponsiblePersonID : 8ef4ba3a-04c6-4eaf-b08b-24d4f2a02bff
     * ResponsiblePersonName : 王五
     * sysGridLineIDs : 2319,1214
     * TypeOfWork : 人工巡视
     * OfficeHolderNames : 毛宁
     * OfficeHolderIDs : c5434175-e065-4162-8d4d-6bfed3764500
     * LineNameSection : 110kVh测试线路2-3人工巡视,220kVgbm101-5人工巡视
     * WorkContent : null
     */

    private String sysPatrolExecutionID = "";
    private String StartDateString;
    private String EndDateString;
    private String TaskName;
    private String TypeString;
    private String ResponsibleClassName;
    private String ResponsiblePersonID;
    private String ResponsiblePersonName;
    private String sysGridLineIDs;
    @SerializedName("TypeOfWorkStrings")
    private String TypeOfWork;
    private String TypeOfWorks;
    private String OfficeHolderNames;
    private String OfficeHolderIDs;
    private String LineNameSection;
    private String WorkContent;

    public String getTypeOfWorks() {
        return TypeOfWorks;
    }

    public void setTypeOfWorks(String typeOfWorks) {
        TypeOfWorks = typeOfWorks;
    }

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public String getStartDateString() {
        return StartDateString;
    }

    public void setStartDateString(String StartDateString) {
        this.StartDateString = StartDateString;
    }

    public String getEndDateString() {
        return EndDateString;
    }

    public void setEndDateString(String EndDateString) {
        this.EndDateString = EndDateString;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String TaskName) {
        this.TaskName = TaskName;
    }

    public String getTypeString() {
        return TypeString;
    }

    public void setTypeString(String TypeString) {
        this.TypeString = TypeString;
    }

    public String getResponsibleClassName() {
        return ResponsibleClassName;
    }

    public void setResponsibleClassName(String ResponsibleClassName) {
        this.ResponsibleClassName = ResponsibleClassName;
    }

    public String getResponsiblePersonID() {
        return ResponsiblePersonID;
    }

    public void setResponsiblePersonID(String ResponsiblePersonID) {
        this.ResponsiblePersonID = ResponsiblePersonID;
    }

    public String getResponsiblePersonName() {
        return ResponsiblePersonName;
    }

    public void setResponsiblePersonName(String ResponsiblePersonName) {
        this.ResponsiblePersonName = ResponsiblePersonName;
    }

    public String getSysGridLineIDs() {
        return sysGridLineIDs;
    }

    public void setSysGridLineIDs(String sysGridLineIDs) {
        this.sysGridLineIDs = sysGridLineIDs;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String TypeOfWork) {
        this.TypeOfWork = TypeOfWork;
    }

    public String getOfficeHolderNames() {
        return OfficeHolderNames;
    }

    public void setOfficeHolderNames(String OfficeHolderNames) {
        this.OfficeHolderNames = OfficeHolderNames;
    }

    public String getOfficeHolderIDs() {
        return OfficeHolderIDs;
    }

    public void setOfficeHolderIDs(String OfficeHolderIDs) {
        this.OfficeHolderIDs = OfficeHolderIDs;
    }

    public String getLineNameSection() {
        return LineNameSection;
    }

    public void setLineNameSection(String LineNameSection) {
        this.LineNameSection = LineNameSection;
    }

    public String getWorkContent() {
        return WorkContent;
    }

    public void setWorkContent(String WorkContent) {
        this.WorkContent = WorkContent;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        else {
            if (obj instanceof PlanPatrolExecution) {
                PlanPatrolExecution planPatrolExecution = (PlanPatrolExecution) obj;
                if (planPatrolExecution.getSysPatrolExecutionID().equals(this.sysPatrolExecutionID)) {
                    return true;
                }
            }
        }
        return false;
    }
}
