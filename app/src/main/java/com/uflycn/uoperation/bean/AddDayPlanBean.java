package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/28
 * Describe  :
 * 线路列表
 */
public class AddDayPlanBean extends AddDayPlanFronLineBean {
    //班组id和班组的名称
    private String classId;
    private String className;
    //负责人的id和负责人的名称
    private String responseId;
    private  String responseName;
    //工作人员的id号和名称。
    private List<String> workPeopleIds;
    private String workPeopleNames;
    private String sysWeeklyPlanSectionID;//周计划id
    //是否来自周计划
    private boolean isFromWeekPlan;

    public String getSysWeeklyPlanSectionID() {
        return sysWeeklyPlanSectionID;
    }

    public void setSysWeeklyPlanSectionID(String sysWeeklyPlanSectionID) {
        this.sysWeeklyPlanSectionID = sysWeeklyPlanSectionID;
    }

    public boolean isFromWeekPlan() {
        return isFromWeekPlan;
    }

    public void setFromWeekPlan(boolean fromWeekPlan) {
        isFromWeekPlan = fromWeekPlan;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseName() {
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

    public List<String> getWorkPeopleIds() {
        return workPeopleIds;
    }

    public void setWorkPeopleIds(List<String> workPeopleIds) {
        this.workPeopleIds = workPeopleIds;
    }

    public String getWorkPeopleNames() {
        return workPeopleNames;
    }

    public void setWorkPeopleNames(String workPeopleNames) {
        this.workPeopleNames = workPeopleNames;
    }
}
