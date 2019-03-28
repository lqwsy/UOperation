package com.uflycn.uoperation.bean;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/25
 * Describe  :
 */
public class YearPlanBean extends DayPlanBaseBean {

    /**
     * sysAnnualPlanSectionID : f7cf59b0-24ed-4fcc-bac9-bd7152aa1661
     * ClassName : 运维一班
     * TypeOfWork : 外勤工作
     * JobContent : 测试1
     * LineName : 天河一线
     * VoltageClass : 220kV
     * TowerNos : #1，#2
     * PlanYear : 2019
     * PlanMonth : 3
     */

    private String sysAnnualPlanSectionID;
    private String ClassName;
    private String TypeOfWork;
    private String JobContent;
    private String LineName;
    private String VoltageClass;
    private String TowerNos;
    private String PlanYear;
    private String PlanMonth;

    public String getSysAnnualPlanSectionID() {
        return sysAnnualPlanSectionID;
    }

    public void setSysAnnualPlanSectionID(String sysAnnualPlanSectionID) {
        this.sysAnnualPlanSectionID = sysAnnualPlanSectionID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        TypeOfWork = typeOfWork;
    }

    public String getJobContent() {
        return JobContent;
    }

    public void setJobContent(String jobContent) {
        JobContent = jobContent;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public String getTowerNos() {
        return TowerNos;
    }

    public void setTowerNos(String towerNos) {
        TowerNos = towerNos;
    }

    public String getPlanYear() {
        return PlanYear;
    }

    public void setPlanYear(String planYear) {
        PlanYear = planYear;
    }

    public String getPlanMonth() {
        return PlanMonth;
    }

    public void setPlanMonth(String planMonth) {
        PlanMonth = planMonth;
    }
}
