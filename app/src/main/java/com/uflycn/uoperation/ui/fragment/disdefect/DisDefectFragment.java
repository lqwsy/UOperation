package com.uflycn.uoperation.ui.fragment.disdefect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.TowerDefects;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.UpdateDefectList;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.TreeDefectDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.NewDefectListActivity;
import com.uflycn.uoperation.ui.activity.TourResultActivity;
import com.uflycn.uoperation.ui.adapter.DefectAdapter;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 缺陷列表
 * Created by xiaoyehai on 2017/9/8.
 */
public class DisDefectFragment extends DemoBaseFragment {

    @BindView(R.id.btn_defect_list)
    Button mBtnDefectList;
    private List<Object> dataList;
    @BindView(R.id.listview)
    ListView mListView;//缺陷列表

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private DefectAdapter mAdapter;
    private Call<DefectInfo> mReqeustGetLineDefectCall;
    private Call<TowerDefects> mRequestGetTowerDefects;
    private String currentTowerId;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_dis_defect;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    @Override
    protected void initView() {
        dataList = new ArrayList<>();
        initSwipRefreshLis();
        if (ProjectManageUtil.isShanDong()) {
            mBtnDefectList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWeakReference.get() instanceof TourResultActivity && ((TourResultActivity) mWeakReference.get()).getTowerId() != -1) {
            currentTowerId = ((TourResultActivity) mWeakReference.get()).getTowerId() + "";
        } else {
            initData();
        }
    }

    @Override
    protected void initData() {
        dataList.clear();
        //getAllDefectsByDb();
        cancelPreRequest();
        if (planTowerNotSpace() || tempTowerNotSpace()) {
            if (mSwipeRefreshLayout != null && !mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
            final StringBuilder sb = new StringBuilder();
            //            String lineIds = "";
            for (String strLineIds : MyApplication.mAllTowersInMap.keySet()) {
                sb.append(strLineIds);
                sb.append(",");
            }
            if (MyApplication.registeredTower != null) {
                for (int strLineIds : MyApplication.mTempLineMap.keySet()) {
                    if (!MyApplication.mAllTowersInMap.containsKey(strLineIds + "")) {
                        sb.append(strLineIds);
                        sb.append(",");
                    }
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }

            if (MyApplication.registeredTower == null) {
                getLineDfaultDefects(sb.toString(), "", "", "", "");
            } else {
                if (MyApplication.specialRegisteredStartTower != null) {
                    getLineDfaultDefects(sb.toString(), "", "", MyApplication.specialRegisteredStartTower.getSysTowerID() + "", MyApplication.specialRegisteredEndTower.getSysTowerID() + "");
                } else {
                    getLineDfaultDefects(sb.toString(), "", "", MyApplication.registeredTower.getSysTowerID() + "", "");
                }

            }
        } else {
            ToastUtil.show(mWeakReference.get().getResources().getString(R.string.tip_start_line_first));
        }
    }

    private boolean planTowerNotSpace() {
        return MyApplication.mAllTowersInMap != null && MyApplication.mAllTowersInMap.size() > 0;
    }

    private boolean tempTowerNotSpace() {
        return MyApplication.mTempTowerMap != null && MyApplication.mTempTowerMap.size() > 0;
    }


    private void getLineDfaultDefects(String lineIds, String voltageClass, String lineName, final String towerid, String nearTowerid) {
        String planDailyPlanSectionIDs = "";
        String sysPatrolExecutionID = "";
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null) {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId());
            } else {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineSb.toString();
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            sysPatrolExecutionID = MyApplication.mPlanPatrolExecutionId;
        }
        mReqeustGetLineDefectCall = RetrofitManager.getInstance().getService(ApiService.class).getLineDefectList(lineIds, "0", "0",
                voltageClass, lineName, towerid, nearTowerid, sysPatrolExecutionID, planDailyPlanSectionIDs);
        mReqeustGetLineDefectCall.enqueue(new Callback<DefectInfo>() {
            @Override
            public void onResponse(Call<DefectInfo> call, Response<DefectInfo> response) {
                try {
                    DefectInfo defectInfo = response.body();
                    if (defectInfo == null) {
                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        return;
                    }
                    dataList.clear();
                    if (defectInfo.getCode() == 1) {
                        List<DefectBean> channelDefect = defectInfo.getChannelDefect();
                        dataList.addAll(channelDefect);
                        //sortList(dataList,1);

                        List<TreeDefectPointBean> treeDefectPoint = defectInfo.getTreeDefectPoint();
                        dataList.addAll(treeDefectPoint);
                        //sortList(dataList,1);

                        List<DefectBean> propertyDefect = defectInfo.getPropertyDefect();
                        dataList.addAll(propertyDefect);
                        //sortList(dataList,1);

                        List<DefectBean> personalDefect = defectInfo.getPersonalDefect();
                        dataList.addAll(personalDefect);
                        //sortList(dataList,1);

                        List<DefectBean> lineCrossList = defectInfo.getLineCrossList();
                        dataList.addAll(lineCrossList);
                        sortList(dataList, 1);

                    }
                } catch (Exception e) {
                    getAllDefectsByDb();
                }
            }

            @Override
            public void onFailure(Call<DefectInfo> call, Throwable t) {
                dataList.clear();
                getAllDefectsByDb();
            }
        });
    }

    private void sortList(final List<Object> dataList, final int flag) { //flag用于判断排列顺序


        Observable.create(new Observable.OnSubscribe<List<Object>>() {
            @Override
            public void call(Subscriber<? super List<Object>> subscriber) {

                Collections.sort(dataList, new Comparator<Object>() {
                    @Override
                    public int compare(Object arg0, Object arg1) {
                        //                        String lineName1, lineName2;
                        //                        if (arg0 instanceof DefectBean) {
                        //                            lineName1 = ((DefectBean) arg0).getLineName();
                        //                        } else {
                        //                            lineName1 = ((TreeDefectPointBean) arg0).getLineName();
                        //                        }
                        //                        int lineId1 = GridLineDBHelper.getInstance().getLine(lineName1).getSysGridLineID().intValue();
                        //
                        //                        if (arg1 instanceof DefectBean) {
                        //                            lineName2 = ((DefectBean) arg1).getLineName();
                        //                        } else {
                        //                            lineName2 = ((TreeDefectPointBean) arg1).getLineName();
                        //                        }
                        //                        int lineId2 = GridLineDBHelper.getInstance().getLine(lineName2).getSysGridLineID().intValue();
                        //
                        //                        if (lineId1 > lineId2) {
                        //                            return 1;
                        //                        } else if (lineId1 < lineId2) {
                        //                            return -1;
                        //                        }

                        int towerId1, towerId2;

                        if (arg0 instanceof DefectBean) {
                            towerId1 = ((DefectBean) arg0).getSysTowerID();
                        } else {
                            towerId1 = ((TreeDefectPointBean) arg0).getTowerA_Id();
                        }

                        if (arg1 instanceof DefectBean) {
                            towerId2 = ((DefectBean) arg1).getSysTowerID();
                        } else {
                            towerId2 = ((TreeDefectPointBean) arg1).getTowerA_Id();
                        }

                        if (towerId1 > towerId2) {
                            return 1;
                        } else if (towerId1 < towerId2) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }

                });

                subscriber.onNext(dataList);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Object>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<Object> objects) {

                        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        if (mAdapter == null) {
                            mAdapter = new DefectAdapter(dataList, UIUtils.getContext(), R.layout.item_list_defect);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mAdapter.refresh(dataList);
                        }
                    }
                });


    }

    private void getAllDefectsByDb() {
        if (MyApplication.mLineIdNamePairs != null && MyApplication.mLineIdNamePairs.size() > 0) {
            if (MyApplication.registeredTower != null) {
                for (String lineName : MyApplication.mLineIdNamePairs.values()) {
                    dataList.addAll(DefectBeanDBHelper.getInstance().getDefectByTowerNum(lineName, MyApplication.registeredTower.getSysTowerID() + ""));
                    dataList.addAll(TreeDefectDBHelper.getInstance().getDefectByTowerNum(lineName, MyApplication.registeredTower.getSysTowerID() + ""));
                }

            } else {
                for (String lineName : MyApplication.mLineIdNamePairs.values()) {
                    dataList.addAll(DefectBeanDBHelper.getInstance().getAllDefectByLineId(lineName));
                    dataList.addAll(TreeDefectDBHelper.getInstance().getAllDefectByLineName(lineName));
                }
            }
        } else if (MyApplication.registeredTower != null) {
            Gridline gridline = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
            dataList.addAll(DefectBeanDBHelper.getInstance().getDefectByTowerNum(gridline.getLineName(), MyApplication.registeredTower.getSysTowerID() + ""));
            dataList.addAll(TreeDefectDBHelper.getInstance().getDefectByTowerNum(gridline.getLineName(), MyApplication.registeredTower.getSysTowerID() + ""));
        }
        sortList(dataList, 1);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
        if (currentTowerId != null) {//页面切换清空id
            currentTowerId = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReqeustGetLineDefectCall != null && !mReqeustGetLineDefectCall.isCanceled()) {
            mReqeustGetLineDefectCall.cancel();
        }
        if (mRequestGetTowerDefects != null && !mRequestGetTowerDefects.isCanceled()) {
            mRequestGetTowerDefects.cancel();
        }
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainTread(BaseMainThreadEvent event) {
        if (event instanceof UpdateDefectList) {
            if (((UpdateDefectList) event).getTowerId() == null || ((UpdateDefectList) event).getTowerId().equalsIgnoreCase("")) {
                initData();
                currentTowerId = null;
            } else {
                //                getDefectData(((UpdateDefectList) event).getTowerId());
                currentTowerId = ((UpdateDefectList) event).getTowerId();
            }
        }
    }

    @OnClick({R.id.btn_submit, R.id.btn_defect, R.id.btn_defect_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (MyApplication.specialRegisteredStartTower != null && MyApplication.specialRegisteredEndTower != null) {
                    EventBus.getDefault().post(new ChangePageEvent(5));
                } else if (MyApplication.isRegisterAuto == true) {
                    if (MyApplication.currentNearestTower != null && MyApplication.registeredTower != null) {
                        EventBus.getDefault().post(new ChangePageEvent(5));
                    } else {
                        ToastUtil.show("请先进行到位登记");
                    }
                } else {
                    if (MyApplication.registeredTower != null) {
                        EventBus.getDefault().post(new ChangePageEvent(5));
                    } else {
                        ToastUtil.show("请先进行到位登记");
                    }
                }
                break;
            case R.id.btn_defect:
                //                Intent intent = new Intent(mWeakReference.get(), BatchDefcetActivity.class);
                //                if (MyApplication.specialRegisteredStartTower != null && MyApplication.specialRegisteredEndTower != null){
                //                    startActivity(intent);
                //                }else if (MyApplication.isRegisterAuto == true) {
                //                    if (MyApplication.currentNearestTower != null) {
                //                        startActivity(intent);
                //                    } else {
                //                        ToastUtil.show("请先进行到位登记");
                //                    }
                //                } else {
                //                    if (MyApplication.registeredTower != null) {
                //                         startActivity(intent);
                //                    } else {
                //                        ToastUtil.show("请先进行到位登记");
                //                    }
                //                }
                break;
            case R.id.btn_defect_list:
                Intent intent2 = new Intent(mWeakReference.get(), NewDefectListActivity.class);
                if (MyApplication.specialRegisteredStartTower != null && MyApplication.specialRegisteredEndTower != null) {
                    startActivity(intent2);
                } else if (MyApplication.isRegisterAuto == true) {
                    if (MyApplication.currentNearestTower != null && MyApplication.registeredTower != null) {
                        startActivity(intent2);
                    } else {
                        ToastUtil.show("请先进行到位登记");
                    }
                } else {
                    if (MyApplication.registeredTower != null) {
                        startActivity(intent2);
                    } else {
                        ToastUtil.show("请先进行到位登记");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initSwipRefreshLis() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (MyApplication.currentNearestTower != null) {
                    if (currentTowerId == null) {
                        initData();
                    }
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }


    private void cancelPreRequest() {
        if (mReqeustGetLineDefectCall != null && !mReqeustGetLineDefectCall.isCanceled()) {
            mReqeustGetLineDefectCall.cancel();
        }
        if (mRequestGetTowerDefects != null && !mRequestGetTowerDefects.isCanceled()) {
            mRequestGetTowerDefects.cancel();
        }
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateLocalDb(DefectInfo defectInfo) {
        List<String> list = new ArrayList<>();
        for (String strLineName : MyApplication.mLineIdNamePairs.values()) {
            list.add(strLineName);
        }
        DefectBeanDBHelper.getInstance().deleteAllByLineName(list);//删除数据
        //插入数据

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
