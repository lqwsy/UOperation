package com.uflycn.uoperation.ui.fragment.cleardefectlist;

import com.uflycn.uoperation.bean.WorksheetApanageTask;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */
public interface ClearDefectListListener {
    void onSeccess(List<WorksheetApanageTask> clearDefLists);
    void onFailed(String message);
}
