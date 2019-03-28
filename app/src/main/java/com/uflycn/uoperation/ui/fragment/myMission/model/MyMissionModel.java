package com.uflycn.uoperation.ui.fragment.myMission.model;

import com.uflycn.uoperation.ui.fragment.myMission.MyMissionListen;

public interface MyMissionModel {

    void getPlanPatrolExecutionList(MyMissionListen.getPlanPatrolExecutionListListener listListener);

    void getPlanPatrolExecutionRecordInfo(String id,MyMissionListen.getPlanPatrolExecutionRecordInfoListener listListener);

    void cancel();

    void getPlanPatrolExecutionLine(String id,MyMissionListen.getPlanPatrolExecutionLineListListener listener);

}
