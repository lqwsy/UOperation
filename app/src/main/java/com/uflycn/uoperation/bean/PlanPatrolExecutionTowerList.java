package com.uflycn.uoperation.bean;

import java.util.List;

public class PlanPatrolExecutionTowerList {
    /**
     * sysPatrolExecutionID : aa85ce2f-d155-4fe1-8000-dcd519563ee2
     * sysGridLineID : 1214
     * TowerList : [{"sysTowerID":10846,"TypeOfWork":[{"TypeOfWork":"cb0506c0-cc24-4bad-b489-20584d614473","TypeOfWorkString":"人工巡视"}]},{"sysTowerID":10847,"TypeOfWork":[{"TypeOfWork":"cb0506c0-cc24-4bad-b489-20584d614473","TypeOfWorkString":"人工巡视"}]},{"sysTowerID":10848,"TypeOfWork":[{"TypeOfWork":"cb0506c0-cc24-4bad-b489-20584d614473","TypeOfWorkString":"人工巡视"}]},{"sysTowerID":10849,"TypeOfWork":[{"TypeOfWork":"cb0506c0-cc24-4bad-b489-20584d614473","TypeOfWorkString":"人工巡视"}]},{"sysTowerID":10850,"TypeOfWork":[{"TypeOfWork":"cb0506c0-cc24-4bad-b489-20584d614473","TypeOfWorkString":"人工巡视"}]}]
     */

    private String sysPatrolExecutionID;
    private int sysGridLineID;
    private List<TowerList> TowerList;

    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public int getSysGridLineID() {
        return sysGridLineID;
    }

    public void setSysGridLineID(int sysGridLineID) {
        this.sysGridLineID = sysGridLineID;
    }

    public List<TowerList> getTowerList() {
        return TowerList;
    }

    public void setTowerList(List<TowerList> TowerList) {
        this.TowerList = TowerList;
    }


    /**
     * sysPatrolExecutionID : aa85ce2f-d155-4fe1-8000-dcd519563ee2
     * sysGridLineID : 1214
     * TowerList : [10846,10847,10848,10849,10850]
     */

//    private String sysPatrolExecutionID;
//    private int sysGridLineID;
//    private List<Integer> TowerList;
//
//    public String getSysPatrolExecutionID() {
//        return sysPatrolExecutionID;
//    }
//
//    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
//        this.sysPatrolExecutionID = sysPatrolExecutionID;
//    }
//
//    public int getSysGridLineID() {
//        return sysGridLineID;
//    }
//
//    public void setSysGridLineID(int sysGridLineID) {
//        this.sysGridLineID = sysGridLineID;
//    }
//
//    public List<Integer> getTowerList() {
//        return TowerList;
//    }
//
//    public void setTowerList(List<Integer> TowerList) {
//        this.TowerList = TowerList;
//    }


}
