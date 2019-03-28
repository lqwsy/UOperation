package com.uflycn.uoperation.ui.fragment.worksheet;

import com.uflycn.uoperation.bean.WorkSheetTask;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15.
 */
public interface WorkSheetListener {
    interface BaseListener{
        void onFailed(String message);
    }


    interface loadWorkSheetListener extends BaseListener{
        void onSuccess(List<WorkSheetTask> workSheetTask);
    }
}
