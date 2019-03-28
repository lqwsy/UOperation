package com.uflycn.uoperation.ui.activity.workManage.model;

import com.uflycn.uoperation.ui.fragment.myMission.MyMissionListen;

public interface AddWorkManageModel {

    void getPlanPatrolExecutionRecordInfo(String id,MyMissionListen.getPlanPatrolExecutionRecordInfoListener listListener);

    void getPlanPatrolExecutionWorkRecordList(MyMissionListen.getPlanPatrolExecutionWorkRecordListListener listener);

    void cancel();

}
