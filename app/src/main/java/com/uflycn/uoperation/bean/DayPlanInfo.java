package com.uflycn.uoperation.bean;

public class DayPlanInfo {

    /**
     * StartDateString : 2019-02-21
     * ClassName : 优飞测试
     * ResponsiblePersonName : admin
     * OfficeHolderNames : 李多雄,潘泽君
     * VoltageClass : 220kV
     * LineName : 丰金线
     * TowerNos : 90,91
     * PatrolTowerNos :
     * TypeOfWorkString : null
     * JobContent : 无人机精细化
     * SafetyMeasure : 测试1
     * SafetyPrecaution : 测试1
     * Remark : nulldayPlanInfo.getPatrolTowerNos()
     */

    private String StartDateString;
    private String ClassName;
    private String ResponsiblePersonName;
    private String OfficeHolderNames;
    private String VoltageClass;
    private String LineName;
    private String TowerNos;
    private String PatrolTowerNos;
    private String TypeOfWorkString;
    private String JobContent;
    private String SafetyMeasure;
    private String SafetyPrecaution;
    private String Remark;
    private String sysDailyPlanSectionID;
    private Long sysGridLineID;

    public Long getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(Long sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public String getSysDailyPlanSectionID() {
        return sysDailyPlanSectionID;

    }

    public void setSysDailyPlanSectionID(String sysDailyPlanSectionID) {
        this.sysDailyPlanSectionID = sysDailyPlanSectionID;
    }

    public String getStartDateString() {
        return StartDateString;
    }

    public void setTypeOfWorkString(String typeOfWorkString) {
        TypeOfWorkString = typeOfWorkString;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public void setStartDateString(String StartDateString) {
        this.StartDateString = StartDateString;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
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

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String VoltageClass) {
        this.VoltageClass = VoltageClass;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String LineName) {
        this.LineName = LineName;
    }

    public String getTowerNos() {
        return TowerNos;
    }

    public void setTowerNos(String TowerNos) {
        this.TowerNos = TowerNos;
    }

    public String getPatrolTowerNos() {
        return PatrolTowerNos;
    }

    public void setPatrolTowerNos(String PatrolTowerNos) {
        this.PatrolTowerNos = PatrolTowerNos;
    }

    public String getTypeOfWorkString() {
        return TypeOfWorkString;
    }


    public String getJobContent() {
        return JobContent;
    }

    public void setJobContent(String JobContent) {
        this.JobContent = JobContent;
    }

    public String getSafetyMeasure() {
        return SafetyMeasure;
    }

    public void setSafetyMeasure(String SafetyMeasure) {
        this.SafetyMeasure = SafetyMeasure;
    }

    public String getSafetyPrecaution() {
        return SafetyPrecaution;
    }

    public void setSafetyPrecaution(String SafetyPrecaution) {
        this.SafetyPrecaution = SafetyPrecaution;
    }

    public String getRemark() {
        return Remark;
    }

}
