package com.uflycn.uoperation.ui.fragment.myMission.presenter;

import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.bean.PlanPatrolExecutionTowerList;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.ui.fragment.myMission.MyMissionListen;
import com.uflycn.uoperation.ui.fragment.myMission.model.MyMissionModelImp;
import com.uflycn.uoperation.ui.fragment.myMission.view.MyMissionView;

import java.util.List;

public class MyMissionPresenterImp implements MyMissionPresenter {

    private MyMissionModelImp mModel;

    private MyMissionView.PlanPatrolExecutionView mView;
    private MyMissionView.PlanPatrolExecutionRecordInfoView mView1;

    public MyMissionPresenterImp(MyMissionView.PlanPatrolExecutionView view) {
        mView = view;
        mModel = new MyMissionModelImp();
    }

    public MyMissionPresenterImp(MyMissionView.PlanPatrolExecutionRecordInfoView view) {
        mView1 = view;
        mModel = new MyMissionModelImp();
    }


    @Override
    public void getPlanPatrolExecutionList() {

        mModel.getPlanPatrolExecutionList(new MyMissionListen.getPlanPatrolExecutionListListener() {
            @Override
            public void onSuccess(List<PlanPatrolExecution> list) {
                mView.onSuccess(list);
            }

            @Override
            public void onFailed(String msg) {
                mView.onFailed(msg);
            }
        });
    }

    @Override
    public void cancel() {
        mModel.cancel();
    }

    @Override
    public void getPlanPatrolExecutionRecordInfo(String id) {
        mModel.getPlanPatrolExecutionRecordInfo(id, new MyMissionListen.getPlanPatrolExecutionRecordInfoListener() {
            @Override
            public void onSuccess(List<PlanPatrolExecutionWorkRecordInfo> list) {
                mView1.onSuccess(list);
            }

            @Override
            public void onFailed(String msg) {
                mView1.onFailed(msg);
            }
        });
    }

    @Override
    public void getPlanPatrolExecutionLine(final PlanPatrolExecution execution) {
        mModel.getPlanPatrolExecutionLine(execution.getSysPatrolExecutionID(), new MyMissionListen.getPlanPatrolExecutionLineListListener() {
            @Override
            public void onSuccess(List<PlanPatrolExecutionTowerList> list) {
                mView.onSuccessGetTower(execution,list);
            }

            @Override
            public void onFailed(String msg) {
                mView.onFailed(msg);
            }
        });
    }
}
