package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2018/3/8.
 */
public class DefectTreeBeanJson {
    /**
     *  TowerA_ID: 1910,
         TowerB_ID: 1911,
         TreeDefectPointList:[39],
         TowerDefectList:[37]

     */
    private int TowerA_ID;
    private int TowerB_ID;
    private int TreeDefectPointList[];
    private int TowerDefectList[];
    private String PlanDailyPlanSectionIDs;
    private String sysPatrolExecutionID;


    public String getSysPatrolExecutionID() {
        return sysPatrolExecutionID;
    }

    public void setSysPatrolExecutionID(String sysPatrolExecutionID) {
        this.sysPatrolExecutionID = sysPatrolExecutionID;
    }

    public String getPlanDailyPlanSectionIDs() {
        return PlanDailyPlanSectionIDs;
    }

    public void setPlanDailyPlanSectionIDs(String planDailyPlanSectionIDs) {
        PlanDailyPlanSectionIDs = planDailyPlanSectionIDs;
    }

    public int getTowerA_ID() {
        return TowerA_ID;
    }

    public void setTowerA_ID(int towerA_ID) {
        TowerA_ID = towerA_ID;
    }

    public int getTowerB_ID() {
        return TowerB_ID;
    }

    public void setTowerB_ID(int towerB_ID) {
        TowerB_ID = towerB_ID;
    }

    public int[] getTreeDefectPointList() {
        return TreeDefectPointList;
    }

    public void setTreeDefectPointList(int[] treeDefectPointList) {
        TreeDefectPointList = treeDefectPointList;
    }

    public int[] getTowerDefectList() {
        return TowerDefectList;
    }

    public void setTowerDefectList(int[] towerDefectList) {
        TowerDefectList = towerDefectList;
    }
}
