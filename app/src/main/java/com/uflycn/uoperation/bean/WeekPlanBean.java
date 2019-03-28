package com.uflycn.uoperation.bean;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/25
 * Describe  : 周计划bean
 */
public class WeekPlanBean extends DayPlanBaseBean {

    /**
     * sysWeeklyPlanSectionID : d5e577ef-ba67-40d2-b6bd-0326593bc89e
     * ClassName : 运维一班
     * TypeOfWork : 外勤工作
     * JobContent : 无人机通道,树障测试,无人机精细化
     * LineName : 庄海线
     * VoltageClass : 110kV
     * TowerIds : 10317,10318,10319,10320,10322,10323
     * PatrolTowerIds :
     * TowerNos : 1,2,3,4,6,7
     * StartDateString : 2019-03-01
     * EndDateString : 2019-03-20
     * ResponsiblePersonName : 郭宝明
     * OfficeHolderNames : 王五,郭宝明,CSQ
     */

    private String sysWeeklyPlanSectionID;
    private String ClassName;
    private String TypeOfWork;
    private String JobContent;
    private String LineName;
    private String VoltageClass;
    private String TowerIds;
    private String PatrolTowerIds;
    private String TowerNos;
    private String StartDateString;
    private String EndDateString;
    private String ResponsiblePersonName;
    private String OfficeHolderNames;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSysWeeklyPlanSectionID() {
        return sysWeeklyPlanSectionID;
    }

    public void setSysWeeklyPlanSectionID(String sysWeeklyPlanSectionID) {
        this.sysWeeklyPlanSectionID = sysWeeklyPlanSectionID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String TypeOfWork) {
        this.TypeOfWork = TypeOfWork;
    }

    public String getJobContent() {
        return JobContent;
    }

    public void setJobContent(String JobContent) {
        this.JobContent = JobContent;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String LineName) {
        this.LineName = LineName;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String VoltageClass) {
        this.VoltageClass = VoltageClass;
    }

    public String getTowerIds() {
        return TowerIds;
    }

    public void setTowerIds(String TowerIds) {
        this.TowerIds = TowerIds;
    }

    public String getPatrolTowerIds() {
        return PatrolTowerIds;
    }

    public void setPatrolTowerIds(String PatrolTowerIds) {
        this.PatrolTowerIds = PatrolTowerIds;
    }

    public String getTowerNos() {
        return TowerNos;
    }

    public void setTowerNos(String TowerNos) {
        this.TowerNos = TowerNos;
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

    public String getResponsiblePersonName() {
        return ResponsiblePersonName;
    }

    public void setResponsiblePersonName(String ResponsiblePersonName) {
        this.ResponsiblePersonName = ResponsiblePersonName;
    }

    public String getOfficeHolderNames() {
        return OfficeHolderNames;
    }

    public void setOfficeHolderNames(String OfficeHolderNames) {
        this.OfficeHolderNames = OfficeHolderNames;
    }
}
