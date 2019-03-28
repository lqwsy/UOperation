package com.uflycn.uoperation.ui.fragment.cleardefectlist.view;

import com.uflycn.uoperation.bean.WorksheetApanageTask;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5.
 */
public interface ClearDefectListView {
    void showDialog();
    void missDialog();
    void refeshListData(List<WorksheetApanageTask> clearDefLists);
}
