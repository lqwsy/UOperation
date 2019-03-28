package com.uflycn.uoperation.bean;

public class PlanPatrolExecutionWorkRecordSubmit {

    private String sysPatrolExecutionID;

    private int StartTowerID;

    private int EndTowerID;

    private String TypeOfWork;

    private String WorkNote;

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public int getStartTowerID() {
        return StartTowerID;
    }

    public void setStartTowerID(int startTowerID) {
        StartTowerID = startTowerID;
    }

    public int getEndTowerID() {
        return EndTowerID;
    }

    public void setEndTowerID(int endTowerID) {
        EndTowerID = endTowerID;
    }

    public String getTypeOfWork() {
        return TypeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        TypeOfWork = typeOfWork;
    }

    public String getWorkNote() {
        return WorkNote;
    }

    public void setWorkNote(String workNote) {
        WorkNote = workNote;
    }

    @Override
    public String toString() {
        return "PlanPatrolExecutionWorkRecordSubmit{" +
                "sysPatrolExecutionID='" + sysPatrolExecutionID + '\'' +
                ", StartTowerID=" + StartTowerID +
                ", EndTowerID=" + EndTowerID +
                ", TypeOfWork='" + TypeOfWork + '\'' +
                ", WorkNote='" + WorkNote + '\'' +
                '}';
    }
}
