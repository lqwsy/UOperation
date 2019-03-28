package com.uflycn.uoperation.ui.fragment.worksheet.presenter;

import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.ui.fragment.worksheet.WorkSheetListener;
import com.uflycn.uoperation.ui.fragment.worksheet.model.WorkSheetModelImpl;
import com.uflycn.uoperation.ui.fragment.worksheet.view.WorkSheetFragment;
import com.uflycn.uoperation.util.CheckTimeUtil;
import com.uflycn.uoperation.util.ToastUtil;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2018/1/15.
 */
public class WorkSheetPresenterImpl implements WorkSheetPresenter{

    private WorkSheetFragment mWorkSheetFragment;
    private WorkSheetModelImpl mWorkSheetModelImpl;

    public WorkSheetPresenterImpl(WorkSheetFragment mWorkSheetFragment) {
        this.mWorkSheetFragment = mWorkSheetFragment;
        mWorkSheetModelImpl = new WorkSheetModelImpl();
    }

    @Override
    public void getWorkSheetList() {
        mWorkSheetModelImpl.getWeekSheet(new WorkSheetListener.loadWorkSheetListener() {
            @Override
            public void onSuccess(List<WorkSheetTask> workSheetTask) {
                mWorkSheetFragment.refreshData(sort(workSheetTask),"workSheetTasks");
            }

            @Override
            public void onFailed(String message) {
                if (message != null){
                    ToastUtil.show(message);
                }
            }
        });
    }

    private List<WorkSheetTask> sort(List<WorkSheetTask> workSheetTask){
        Collections.sort(workSheetTask, new Comparator<WorkSheetTask>() {
            @Override
            public int compare(WorkSheetTask o1, WorkSheetTask o2) {
                //降序
                return o2.getTaskNo().compareTo(o1.getTaskNo());
            }
        });
        return workSheetTask;
    }

}
