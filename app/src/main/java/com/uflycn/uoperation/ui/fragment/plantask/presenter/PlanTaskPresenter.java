package com.uflycn.uoperation.ui.fragment.plantask.presenter;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.ui.fragment.plantask.contract.PlanTaskContract;
import com.uflycn.uoperation.ui.fragment.plantask.model.GridlineCallBack;
import com.uflycn.uoperation.ui.fragment.plantask.model.PlanModel;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class PlanTaskPresenter extends PlanTaskContract.Presenter implements GridlineCallBack {

    @Override
    public PlanTaskContract.Model getModel() {
        return new PlanModel();
    }


    @Override
    public void getPlanGridLinesByVol(List<ItemDetail> voltageClass, int page) {
        model.getPlantaskByVol(voltageClass,this,page);
    }

    @Override
    public void getPlanGirdlines(String lineName,int page) {
        model.getPlantask(lineName,this,page);
    }

    @Override
    protected void updateGridLine(List<Gridline> gridlines) {
        getView().updateGridlines(gridlines);
    }

    @Override
    public void startInspect(int lineid) {
        model.startInspect(lineid,this);
    }

    @Override
    public void deAttch() {
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
        if (model != null) {
            model.onDettach();
        }
    }

    @Override
    public void onGridLineCallBack(List<Gridline> gridlines) {
        if (gridlines.isEmpty()){
            ToastUtil.show("无符合条件的线路存在");
        }
        getView().dismisDialog();
        updateGridLine(gridlines);
    }

    @Override
    public void onStartInspectCallBack(String mesage,int lineId) {
      /*  ToastUtil.show(mesage);
        if(mesage.equalsIgnoreCase("开启巡视成功")){
            getPlanGirdlines("");
        }*/
        if(mesage != null){
            getView().startInspectCallback(lineId);
        }else{
            ToastUtil.show(MyApplication.getContext(),"开启失败");
        }


    }
}
