package com.uflycn.uoperation.ui.fragment.myMission.view;

import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.bean.PlanPatrolExecutionTowerList;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;

import java.util.List;

public interface MyMissionView {

    interface PlanPatrolExecutionView{
        void onSuccess(List<PlanPatrolExecution> list);

        void onSuccessGetTower(PlanPatrolExecution execution,List<PlanPatrolExecutionTowerList> list);

        void onFailed(String msg);
    }


    interface PlanPatrolExecutionRecordInfoView{
        void onSuccess(List<PlanPatrolExecutionWorkRecordInfo> list);

        void onFailed(String msg);
    }


}
