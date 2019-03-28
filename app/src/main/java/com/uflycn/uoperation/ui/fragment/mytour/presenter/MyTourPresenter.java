package com.uflycn.uoperation.ui.fragment.mytour.presenter;

import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TowerDefects;
import com.uflycn.uoperation.http.RequestCallback;
import com.uflycn.uoperation.ui.fragment.mytour.contract.MyTourContract;
import com.uflycn.uoperation.ui.fragment.mytour.model.MyTourModel;

import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class MyTourPresenter extends MyTourContract.Presenter implements RequestCallback{
    @Override
    public MyTourContract.Model getModel() {
        return new MyTourModel();
    }

    @Override
    public void addTowerByid(int lineid) {
        model.addTowersById(lineid,this);
    }

  public void addTowersBewteenTowerById(String firstTowerId, String endTowerId, int lineid) {
        model.addTowersBewteenTowerById(firstTowerId,endTowerId,lineid,this);
    }


    @Override
    public void showTowers(List<Tower> towers) {
        mWeakReference.get().showTowers(towers);
    }

    @Override
    public void showDefectsByLineId(DefectInfo defectInfo) {
        mWeakReference.get().showDefectsByLineId(defectInfo);
    }

//    @Override
//    public void getDefectsByTowerId(String towerid) {
//        model.getAllDefectByTowerId(towerid,this);
//    }

    @Override
    public void showDefectsByTowerId(TowerDefects towerDefects) {
        mWeakReference.get().showDefectsByTowerId(towerDefects);
    }

    @Override
    public void getLineCrossList(String lindids) {
        model.getCrossList(lindids,this);
    }

    @Override
    public void showLineCrossList(List<LineCrossEntity> crossEntityList) {
        mWeakReference.get().showCrossList(crossEntityList);
    }

//    @Override
//    public void getDefectsByLineid(String lineIds) {
////        model.getAllDefectByLineId(lineIds,this);
//    }

    @Override
    public void deAttch() {
        super.deAttch();
        model.dettach();
    }
    @Override
    public void onDataCallBack(Object data) {
        if(data == null ){
            return;
        }
        if(data instanceof DefectInfo){
            showDefectsByLineId((DefectInfo) data);
        }else if(data instanceof  TowerDefects){
            showDefectsByTowerId((TowerDefects) data);
        }else if( data instanceof List){
            if(((List) data).size() >0){
                if(((List) data).get(0) instanceof Tower){
                    showTowers((List<Tower>) data);
                }else if(((List) data).get(0) instanceof LineCrossEntity){
                    showLineCrossList((List<LineCrossEntity>) data);
                }
            }
        }
    }




}
