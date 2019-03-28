package com.uflycn.uoperation.ui.fragment.myMission;

import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.bean.PlanPatrolExecutionTowerList;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordList;

import java.util.List;

public interface MyMissionListen {

    interface getPlanPatrolExecutionListListener {

        void onSuccess(List<PlanPatrolExecution> list);

        void onFailed(String msg);
    }

    interface getPlanPatrolExecutionRecordInfoListener {

        void onSuccess(List<PlanPatrolExecutionWorkRecordInfo> list);

        void onFailed(String msg);
    }

    interface getPlanPatrolExecutionWorkRecordListListener {

        void onSuccess(List<PlanPatrolExecutionWorkRecordList> list);

        void onFailed(String msg);
    }

    interface getPlanPatrolExecutionLineListListener {

        void onSuccess(List<PlanPatrolExecutionTowerList> list);

        void onFailed(String msg);
    }
}
