package com.uflycn.uoperation.ui.fragment.plantask.contract;

import com.uflycn.uoperation.base.BaseModel;
import com.uflycn.uoperation.base.BasePresenter;
import com.uflycn.uoperation.base.BaseView;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.ui.fragment.plantask.model.GridlineCallBack;

import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public interface PlanTaskContract {

    interface Model extends BaseModel {
        void getPlantaskByVol(List<ItemDetail> voltageClass, GridlineCallBack callBack, int page);
        void getPlantask(String lineName, GridlineCallBack callBack,int page);
        void onDettach();
        void startInspect(int lineID,GridlineCallBack callBack);
    }

    interface PlanTaskView extends BaseView {
        void showDialog();
        void dismisDialog();
        void updateGridlines(List<Gridline> gridlines);//更新线路
        void startInspect(int lineId);//开启巡视
        void startInspectCallback(int lineid);
    }

    abstract class Presenter extends BasePresenter<PlanTaskView, Model> {
        protected abstract void getPlanGridLinesByVol(List<ItemDetail> voltageClass,int page);
        protected abstract void getPlanGirdlines(String lineName,int page);
        protected abstract void updateGridLine(List<Gridline> gridlines);
        protected abstract void startInspect(int lineid);
    }
}
