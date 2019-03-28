package com.uflycn.uoperation.ui.fragment.temptask.presenter;

import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.ui.fragment.temptask.contract.TempTaskContract;
import com.uflycn.uoperation.ui.fragment.temptask.model.TempModel;
import com.uflycn.uoperation.ui.fragment.temptask.model.TempTaskCallBack;

import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class TempTaskPresenter extends TempTaskContract.Presenter implements TempTaskCallBack {

    @Override
    public TempTaskContract.Model getModel() {
        return new TempModel();
    }

    @Override
    public void getTempTaskList(String lineName) {
        model.getTempTaskList(lineName, this);
    }

    @Override
    public void updateReadStatus(int iSysMessageInfoId) {
        model.updateReadStatus(iSysMessageInfoId);
    }

    @Override
    public void onGetTempTaskCallBack(List<TempTask> tempTaskInfoList) {
        if (getView() != null)
            getView().returnTempTaskList(tempTaskInfoList);
    }
}
