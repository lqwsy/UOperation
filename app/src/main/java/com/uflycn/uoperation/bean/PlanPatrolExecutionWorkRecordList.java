package com.uflycn.uoperation.bean;

public class PlanPatrolExecutionWorkRecordList {

    /**
     * VoltageClass : 220kV
     * LineName : 天河一线
     * TowerSection : 1-20
     * TaskType : 维修
     * TypeOfWork : 杆塔螺栓紧固
     * TaskName : 任务名称
     * WorkContent : 杆塔螺栓紧固
     * WorkNote : 测试
     */

    private String VoltageClass;
    private String LineName;
    private String TowerSection;
    private String TaskType;
    private String TypeOfWork;
    private String TaskName;
    private String WorkContent;
    private String WorkNote;

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getTowerSection() {
        return TowerSection;
    }

    public void setTowerSection(String towerSection) {
        TowerSection = towerSection;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        TypeOfWork = typeOfWork;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getWorkContent() {
        return WorkContent;
    }

    public void setWorkContent(String workContent) {
        WorkContent = workContent;
    }

    public String getWorkNote() {
        return WorkNote;
    }

    public void setWorkNote(String workNote) {
        WorkNote = workNote;
    }
}
