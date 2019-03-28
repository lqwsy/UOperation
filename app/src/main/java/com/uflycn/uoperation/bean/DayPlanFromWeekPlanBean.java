package com.uflycn.uoperation.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/3/6
 * Describe  : 新增日计划从周计划中获取的数据
 */
public class DayPlanFromWeekPlanBean implements Parcelable {

    /**
     * sysWeeklyPlanSectionID : 2bcbbf00-862f-4a5e-aba7-78f0c0a80959
     * ClassID : 86aaad38-7660-4117-a183-fc47d0a5c574
     * ClassName : 测试一组1
     * TypeOfWork : 3187abd9-6693-4c34-918e-63b5d6071a22
     * TypeOfWorkString : 外勤工作
     * JobContent : 0b205803-e2d1-4693-8a0c-549278aba328
     * JobContentString : 无人机精细化
     * sysGridLineID : 31
     * LineName : 周良线
     * VoltageClass : 110kV
     * TowerIds : 1593,1594,1595,1596,1597,1598,1599,1600,1601,1602
     * TowerNos : 1,2,3,4,5,6,7,8,9,10
     * PatrolTowerIds : null
     * PatrolTowerNos : null
     * StartDateString : 2019-03-01
     * ResponsiblePersonID : 10edb317-c162-430a-a53a-2777ff608e14
     * ResponsiblePersonName : hyl1
     * OfficeHolderIDs : 10edb317-c162-430a-a53a-2777ff608e14,4acf0d72-c1f8-47fa-ba2c-02d3dc7a5e20
     * OfficeHolderNames : hyl1,满河
     * DangerDefectCount : 8
     * SeriousDefectCount : 12
     * NormalDefectCount : 8
     * SafetyMeasure : 测试1
     * SafetyPrecaution : 测试1
     */

    private String sysWeeklyPlanSectionID;
    private String ClassID;
    private String ClassName;
    private String TypeOfWork;
    private String TypeOfWorkString;
    private String JobContent;
    private String JobContentString;
    private Integer sysGridLineID;
    private String LineName;
    private String VoltageClass;
    private String TowerIds;
    private String TowerNos;
    private String PatrolTowerIds;
    private String PatrolTowerNos;
    private String StartDateString;
    private String ResponsiblePersonID;
    private String ResponsiblePersonName;
    private String OfficeHolderIDs;
    private String OfficeHolderNames;
    private Integer DangerDefectCount;
    private Integer SeriousDefectCount;
    private Integer NormalDefectCount;
    private String SafetyMeasure;
    private String SafetyPrecaution;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getSysWeeklyPlanSectionID() {
        return sysWeeklyPlanSectionID;
    }

    public void setSysWeeklyPlanSectionID(String sysWeeklyPlanSectionID) {
        this.sysWeeklyPlanSectionID = sysWeeklyPlanSectionID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
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

    public String getTypeOfWorkString() {
        return TypeOfWorkString;
    }

    public void setTypeOfWorkString(String typeOfWorkString) {
        TypeOfWorkString = typeOfWorkString;
    }

    public String getJobContent() {
        return JobContent;
    }

    public void setJobContent(String jobContent) {
        JobContent = jobContent;
    }

    public String getJobContentString() {
        return JobContentString;
    }

    public void setJobContentString(String jobContentString) {
        JobContentString = jobContentString;
    }

    public Integer getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(Integer sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
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

    public String getTowerIds() {
        return TowerIds;
    }

    public void setTowerIds(String towerIds) {
        TowerIds = towerIds;
    }

    public String getTowerNos() {
        return TowerNos;
    }

    public void setTowerNos(String towerNos) {
        TowerNos = towerNos;
    }

    public String getPatrolTowerIds() {
        return PatrolTowerIds;
    }

    public void setPatrolTowerIds(String patrolTowerIds) {
        PatrolTowerIds = patrolTowerIds;
    }

    public String getPatrolTowerNos() {
        return PatrolTowerNos;
    }

    public void setPatrolTowerNos(String patrolTowerNos) {
        PatrolTowerNos = patrolTowerNos;
    }

    public String getStartDateString() {
        return StartDateString;
    }

    public void setStartDateString(String startDateString) {
        StartDateString = startDateString;
    }

    public String getResponsiblePersonID() {
        return ResponsiblePersonID;
    }

    public void setResponsiblePersonID(String responsiblePersonID) {
        ResponsiblePersonID = responsiblePersonID;
    }

    public String getResponsiblePersonName() {
        return ResponsiblePersonName;
    }

    public void setResponsiblePersonName(String responsiblePersonName) {
        ResponsiblePersonName = responsiblePersonName;
    }

    public String getOfficeHolderIDs() {
        return OfficeHolderIDs;
    }

    public void setOfficeHolderIDs(String officeHolderIDs) {
        OfficeHolderIDs = officeHolderIDs;
    }

    public String getOfficeHolderNames() {
        return OfficeHolderNames;
    }

    public void setOfficeHolderNames(String officeHolderNames) {
        OfficeHolderNames = officeHolderNames;
    }

    public Integer getDangerDefectCount() {
        return DangerDefectCount;
    }

    public void setDangerDefectCount(Integer dangerDefectCount) {
        DangerDefectCount = dangerDefectCount;
    }

    public Integer getSeriousDefectCount() {
        return SeriousDefectCount;
    }

    public void setSeriousDefectCount(Integer seriousDefectCount) {
        SeriousDefectCount = seriousDefectCount;
    }

    public Integer getNormalDefectCount() {
        return NormalDefectCount;
    }

    public void setNormalDefectCount(Integer normalDefectCount) {
        NormalDefectCount = normalDefectCount;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sysWeeklyPlanSectionID);
        dest.writeString(this.ClassID);
        dest.writeString(this.ClassName);
        dest.writeString(this.TypeOfWork);
        dest.writeString(this.TypeOfWorkString);
        dest.writeString(this.JobContent);
        dest.writeString(this.JobContentString);
        dest.writeValue(this.sysGridLineID);
        dest.writeString(this.LineName);
        dest.writeString(this.VoltageClass);
        dest.writeString(this.TowerIds);
        dest.writeString(this.TowerNos);
        dest.writeString(this.PatrolTowerIds);
        dest.writeString(this.PatrolTowerNos);
        dest.writeString(this.StartDateString);
        dest.writeString(this.ResponsiblePersonID);
        dest.writeString(this.ResponsiblePersonName);
        dest.writeString(this.OfficeHolderIDs);
        dest.writeString(this.OfficeHolderNames);
        dest.writeValue(this.DangerDefectCount);
        dest.writeValue(this.SeriousDefectCount);
        dest.writeValue(this.NormalDefectCount);
        dest.writeString(this.SafetyMeasure);
        dest.writeString(this.SafetyPrecaution);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
    }

    public DayPlanFromWeekPlanBean() {
    }

    protected DayPlanFromWeekPlanBean(Parcel in) {
        this.sysWeeklyPlanSectionID = in.readString();
        this.ClassID = in.readString();
        this.ClassName = in.readString();
        this.TypeOfWork = in.readString();
        this.TypeOfWorkString = in.readString();
        this.JobContent = in.readString();
        this.JobContentString = in.readString();
        this.sysGridLineID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.LineName = in.readString();
        this.VoltageClass = in.readString();
        this.TowerIds = in.readString();
        this.TowerNos = in.readString();
        this.PatrolTowerIds = in.readString();
        this.PatrolTowerNos = in.readString();
        this.StartDateString = in.readString();
        this.ResponsiblePersonID = in.readString();
        this.ResponsiblePersonName = in.readString();
        this.OfficeHolderIDs = in.readString();
        this.OfficeHolderNames = in.readString();
        this.DangerDefectCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SeriousDefectCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.NormalDefectCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.SafetyMeasure = in.readString();
        this.SafetyPrecaution = in.readString();
        this.isCheck = in.readByte() != 0;
    }

    public static final Parcelable.Creator<DayPlanFromWeekPlanBean> CREATOR = new Parcelable.Creator<DayPlanFromWeekPlanBean>() {
        @Override
        public DayPlanFromWeekPlanBean createFromParcel(Parcel source) {
            return new DayPlanFromWeekPlanBean(source);
        }

        @Override
        public DayPlanFromWeekPlanBean[] newArray(int size) {
            return new DayPlanFromWeekPlanBean[size];
        }
    };
}
