package com.uflycn.uoperation.ui.fragment.myMission.presenter;

import com.uflycn.uoperation.bean.PlanPatrolExecution;

public interface MyMissionPresenter {


    void getPlanPatrolExecutionList();

    void cancel();

    void getPlanPatrolExecutionRecordInfo(String id);

    void getPlanPatrolExecutionLine(PlanPatrolExecution planPatrolExecution);
}
