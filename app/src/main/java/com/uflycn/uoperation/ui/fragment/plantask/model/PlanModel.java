package com.uflycn.uoperation.ui.fragment.plantask.model;

import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.ui.fragment.plantask.contract.PlanTaskContract;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class PlanModel implements PlanTaskContract.Model {

    @Override
    public void getPlantaskByVol(List<ItemDetail> voltageClass, GridlineCallBack callBack, int page) {
        getPlanListByVol2(voltageClass, callBack, page);
    }

    private void getPlanListByVol2(final List<ItemDetail> voltageClass, final GridlineCallBack callBack, final int page) {
        Observable.create(new Observable.OnSubscribe<List<Gridline>>() {
            @Override
            public void call(Subscriber<? super List<Gridline>> subscriber) {
                List<Gridline> gridlines;
                gridlines = GridLineDBHelper.getInstance().getLineListByVols(voltageClass);
                Tower from, to;
                for (Gridline gridline : gridlines) {
                    from = TowerDBHelper.getInstance().getFirstTower(gridline.getSysGridLineID().intValue());
                    if (from != null)
                        gridline.setFirstTower(from.getTowerNo());
                    to = TowerDBHelper.getInstance().getEndTower(gridline.getSysGridLineID().intValue());
                    if (to != null) {
                        gridline.setEndTower(to.getTowerNo());
                    }
                    gridline.setVClass(ItemDetailDBHelper.getInstance().getItem("电压等级", gridline.getVoltageClass()));
            }
                subscriber.onNext(gridlines);

            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<List<Gridline>>() {
                    @Override
                    public void onNext(List<Gridline> gridlines) {
                        callBack.onGridLineCallBack(gridlines);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void getPlanListByVol3(final List<ItemDetail> voltageClass, final GridlineCallBack callBack, final int page) {
        Observable.just(voltageClass)
                .flatMap(new Func1<List<ItemDetail>, Observable<? extends List<Gridline>>>() {
                    @Override
                    public Observable<? extends List<Gridline>> call(List<ItemDetail> itemDetails) {
                        return null;
                    }
                });
    }

    @Override
    public void getPlantask(String lineName, GridlineCallBack callBack, int page) {
        getPlanList(lineName, callBack, page);
    }

    @Override
    public void onDettach() {

    }

    private void getPlanList(final String lineName, final GridlineCallBack callBack, final int page) {

        Observable.create(new Observable.OnSubscribe<List<Gridline>>() {
            @Override
            public void call(Subscriber<? super List<Gridline>> subscriber) {
                List<Gridline> gridlines;
                if (!lineName.equals("")) {
                    gridlines = GridLineDBHelper.getInstance().getLineList(lineName);
                } else {
                    gridlines = GridLineDBHelper.getInstance().getLineList(lineName, page);
                }
                Tower from, to;
                for (Gridline gridline : gridlines) {
                    from = TowerDBHelper.getInstance().getFirstTower(gridline.getSysGridLineID().intValue());
                    if (from != null)
                        gridline.setFirstTower(from.getTowerNo());
                    to = TowerDBHelper.getInstance().getEndTower(gridline.getSysGridLineID().intValue());
                    if (to != null) {
                        gridline.setEndTower(to.getTowerNo());
                    }
                    gridline.setVClass(ItemDetailDBHelper.getInstance().getItem("电压等级", gridline.getVoltageClass()));
                }
                subscriber.onNext(gridlines);

            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<List<Gridline>>() {
                    @Override
                    public void onNext(List<Gridline> gridlines) {
                        callBack.onGridLineCallBack(gridlines);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    @Override
    public void startInspect(int lineID, final GridlineCallBack callBack) {
        callBack.onStartInspectCallBack("开启巡视成功", lineID);
    }
}
