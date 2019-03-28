package com.uflycn.uoperation.ui.activity.workManage.view;

import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordList;

import java.util.List;

public interface AddWorkManageView {

    interface PlanPatrolExecutionRecordInfoView{
        void onSuccess(List<PlanPatrolExecutionWorkRecordInfo> list);

        void onFailed(String msg);
    }

    interface PlanPatrolExecutionRecordListView{
        void onSuccess(List<PlanPatrolExecutionWorkRecordList> list);

        void onFailed(String msg);
    }
}
