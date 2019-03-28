package com.uflycn.uoperation.ui.activity.workManage.presenter;

import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordList;
import com.uflycn.uoperation.ui.activity.workManage.model.AddWorkManageModelImp;
import com.uflycn.uoperation.ui.activity.workManage.view.AddWorkManageView;
import com.uflycn.uoperation.ui.fragment.myMission.MyMissionListen;

import java.util.List;

public class AddWorkManagePresenterImp implements AddWorkManagePresenter {

    private AddWorkManageModelImp mModel;

    private AddWorkManageView.PlanPatrolExecutionRecordInfoView mView;
    private AddWorkManageView.PlanPatrolExecutionRecordListView mView1;

    public AddWorkManagePresenterImp(AddWorkManageView.PlanPatrolExecutionRecordInfoView view) {
        mView = view;
        mModel = new AddWorkManageModelImp();
    }

    public AddWorkManagePresenterImp(AddWorkManageView.PlanPatrolExecutionRecordListView view) {
        mView1 = view;
        mModel = new AddWorkManageModelImp();
    }


    @Override
    public void getPlanPatrolExecutionRecordInfo(String id) {
        mModel.getPlanPatrolExecutionRecordInfo(id, new MyMissionListen.getPlanPatrolExecutionRecordInfoListener() {
            @Override
            public void onSuccess(List<PlanPatrolExecutionWorkRecordInfo> list) {
                mView.onSuccess(list);
            }

            @Override
            public void onFailed(String msg) {
                mView.onFailed(msg);
            }
        });
    }

    @Override
    public void getPlanPatrolExecutionWorkRecordList() {
        mModel.getPlanPatrolExecutionWorkRecordList(new MyMissionListen.getPlanPatrolExecutionWorkRecordListListener() {
            @Override
            public void onSuccess(List<PlanPatrolExecutionWorkRecordList> list) {
                mView1.onSuccess(list);
            }

            @Override
            public void onFailed(String msg) {
                mView1.onFailed(msg);
            }
        });
    }

    @Override
    public void cancel() {
        mModel.cancel();
    }
}
