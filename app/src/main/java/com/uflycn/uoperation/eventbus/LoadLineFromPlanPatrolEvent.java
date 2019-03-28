package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.PlanPatrolExecutionTowerList;

import java.util.List;

public class LoadLineFromPlanPatrolEvent {

    List<PlanPatrolExecutionTowerList> list;


    public LoadLineFromPlanPatrolEvent(List<PlanPatrolExecutionTowerList> list) {
        this.list = list;
    }

    public List<PlanPatrolExecutionTowerList> getList() {
        return list;
    }
}
