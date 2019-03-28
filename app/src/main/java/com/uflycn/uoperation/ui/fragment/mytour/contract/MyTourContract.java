package com.uflycn.uoperation.ui.fragment.mytour.contract;

import com.uflycn.uoperation.base.BaseModel;
import com.uflycn.uoperation.base.BasePresenter;
import com.uflycn.uoperation.base.BaseView;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TowerDefects;
import com.uflycn.uoperation.http.RequestCallback;

import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public interface MyTourContract {

    interface Model extends BaseModel {
        void addTowersById(int lineid, RequestCallback<List<Tower>> callback);
        void addTowersBewteenTowerById(String firstTowerId, String endTowerId,int lineid, RequestCallback<List<Tower>> callback);
//        void getAllDefectByLineId(String lineIds, RequestCallback<DefectInfo> callback);
//        void getAllDefectByTowerId(String towerid, RequestCallback<TowerDefects> callback);
        void getCrossList(String lineIds,RequestCallback<List<LineCrossEntity>> callback);
        void dettach();
    }

    interface View extends BaseView {
        void addTowerByid(int lineid);
        void showTowers(List<Tower> towers);
        void showDefectsByLineId(DefectInfo defectInfo);
        void showDefectsByTowerId(TowerDefects towerDefects);
        void showCrossList(List<LineCrossEntity> lineCrossEntities);
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        //加塔
        public abstract void addTowerByid(int lineid);
        public abstract void showTowers(List<Tower> towers);
        //整条线路缺陷
//        public abstract void getDefectsByLineid(String lineIds);
        public abstract void showDefectsByLineId(DefectInfo defectInfo);
        //到位登记塔的缺陷
//        public abstract void getDefectsByTowerId(String towerid);
        public abstract void showDefectsByTowerId(TowerDefects towerDefects);
        //整条线路的交跨
        public abstract void getLineCrossList(String lindids);
        public abstract void showLineCrossList(List<LineCrossEntity> crossEntityList);
    }
}
