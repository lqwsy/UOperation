package com.uflycn.uoperation.bean;

import java.util.Date;
import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/3/5
 * Describe  : 提交的日计划
 */
public class UploadDayPlanBean {
    //从周计划中获取的，如果从线路中获取可以为空
    private String sysWeeklyPlanSectionID;
    private Integer sysGridLineID;
    private String TypeOfWork;//工作类型id
    private String ClassID;//负责班组id
    private String ResponsiblePersonID;//负责人id
    private String SafetyMeasure;//安全措施 每个工作内容的安全措施以，隔开 可以为空
    private String SafetyPrecaution;//注意事项  可以为空
    private Date StartDate;//开始日期
    private String Remark;//备注 可以为空
    private List<DailyPlanTower> planDailyPlanTowerList;//巡视杆塔列表
    private List<DailyPlanOfficeHolder> planDailyPlanOfficeHolderList;//工作人员表
    private List<DailyPlanJobContent> planDailyPlanJobContentList;//工作内容表
    private List<DailyPlanDefect> planDailyPlanDefectList;//日计划缺陷表

    public String getSysWeeklyPlanSectionID() {
        return sysWeeklyPlanSectionID;
    }

    public void setSysWeeklyPlanSectionID(String sysWeeklyPlanSectionID) {
        this.sysWeeklyPlanSectionID = sysWeeklyPlanSectionID;
    }

    public Integer getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(Integer sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        TypeOfWork = typeOfWork;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getResponsiblePersonID() {
        return ResponsiblePersonID;
    }

    public void setResponsiblePersonID(String responsiblePersonID) {
        ResponsiblePersonID = responsiblePersonID;
    }

    public String getSafetyMeasure() {
        return SafetyMeasure;
    }

    public void setSafetyMeasure(String safetyMeasure) {
        SafetyMeasure = safetyMeasure;
    }

    public String getSafetyPrecaution() {
        return SafetyPrecaution;
    }

    public void setSafetyPrecaution(String safetyPrecaution) {
        SafetyPrecaution = safetyPrecaution;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public List<DailyPlanTower> getPlanDailyPlanTowerList() {
        return planDailyPlanTowerList;
    }

    public void setPlanDailyPlanTowerList(List<DailyPlanTower> planDailyPlanTowerList) {
        this.planDailyPlanTowerList = planDailyPlanTowerList;
    }

    public List<DailyPlanOfficeHolder> getPlanDailyPlanOfficeHolderList() {
        return planDailyPlanOfficeHolderList;
    }

    public void setPlanDailyPlanOfficeHolderList(List<DailyPlanOfficeHolder> planDailyPlanOfficeHolderList) {
        this.planDailyPlanOfficeHolderList = planDailyPlanOfficeHolderList;
    }

    public List<DailyPlanJobContent> getPlanDailyPlanJobContentList() {
        return planDailyPlanJobContentList;
    }

    public void setPlanDailyPlanJobContentList(List<DailyPlanJobContent> planDailyPlanJobContentList) {
        this.planDailyPlanJobContentList = planDailyPlanJobContentList;
    }

    public List<DailyPlanDefect> getPlanDailyPlanDefectList() {
        return planDailyPlanDefectList;
    }

    public void setPlanDailyPlanDefectList(List<DailyPlanDefect> planDailyPlanDefectList) {
        this.planDailyPlanDefectList = planDailyPlanDefectList;
    }
}
