package com.uflycn.uoperation.ui.fragment.temptask.model;

import com.uflycn.uoperation.bean.TempTask;

import java.util.List;

/**
 * Created by Xiong on 2017/9/27.
 */
public interface TempTaskCallBack {

    void onGetTempTaskCallBack(List<TempTask> tempTaskInfoList);
}
