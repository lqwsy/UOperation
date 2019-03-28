package com.uflycn.uoperation.ui.fragment.temptask.contract;

import com.uflycn.uoperation.base.BaseModel;
import com.uflycn.uoperation.base.BasePresenter;
import com.uflycn.uoperation.base.BaseView;
import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.ui.fragment.temptask.model.TempTaskCallBack;

import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public interface TempTaskContract {

    interface Model extends BaseModel {
        void getTempTaskList(String strLineName,TempTaskCallBack callBack);
        void updateReadStatus(int iSysMessageInfoId);
    }

    interface TempTaskView extends BaseView {
        void returnTempTaskList(List<TempTask> tempTaskInfoList);//加载数据
        void viewDetail(TempTask tempTask);//
    }

    abstract class Presenter extends BasePresenter<TempTaskView, Model> {
        protected abstract void getTempTaskList(String lineName);
        protected abstract void updateReadStatus(int iSysMessageInfoId);
    }
}
