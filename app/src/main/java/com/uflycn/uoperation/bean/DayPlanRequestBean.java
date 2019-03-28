package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/21
 * Describe  :
 */
public class DayPlanRequestBean {
    private String sysDailyPlanTimeSpanID;
    private int Status;
    List<SelectTower.TowerListBean> PlanDailyPlanPatrolTowerList;
    private String EndLongitude;
    private String EndLatitude;

    public String getSysDailyPlanTimeSpanID() {
        return sysDailyPlanTimeSpanID;
    }

    public void setSysDailyPlanTimeSpanID(String sysDailyPlanTimeSpanID) {
        this.sysDailyPlanTimeSpanID = sysDailyPlanTimeSpanID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public List<SelectTower.TowerListBean> getPlanDailyPlanPatrolTowerList() {
        return PlanDailyPlanPatrolTowerList;
    }

    public void setPlanDailyPlanPatrolTowerList(List<SelectTower.TowerListBean> planDailyPlanPatrolTowerList) {
        PlanDailyPlanPatrolTowerList = planDailyPlanPatrolTowerList;
    }

    public String getEndLongitude() {
        return EndLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        EndLongitude = endLongitude;
    }

    public String getEndLatitude() {
        return EndLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        EndLatitude = endLatitude;
    }
}
