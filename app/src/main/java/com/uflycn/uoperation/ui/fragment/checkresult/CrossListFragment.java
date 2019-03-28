package com.uflycn.uoperation.ui.fragment.checkresult;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.UpdateLineCrossList;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.CrossRegisterActivity;
import com.uflycn.uoperation.ui.adapter.LineCrossAdapter;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.ViewPagerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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
 * A simple {@link Fragment} subclass.
 * 交跨记录列表
 */
public class CrossListFragment extends ViewPagerFragment {

    @BindView(R.id.list_cross)
    ListView mCrossListView;
    private LineCrossAdapter mLineCrossAdapter;
    private Call<BaseCallBack<List<LineCrossEntity>>> mLineCrossRequestCall;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Reference<Context> mContextReference;

    //    @Override
    //    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    //                             Bundle savedInstanceState) {
    //        View contentView = inflater.inflate(R.layout.fragment_cross_list, container, false);
    //        ButterKnife.bind(this, contentView);
    //        mContextReference = new WeakReference<Context>(getActivity());
    //        mLineCrossAdapter = new LineCrossAdapter(new ArrayList<LineCrossEntity>(), getActivity(), R.layout.item_linecross);
    //        mCrossListView.setAdapter(mLineCrossAdapter);
    //        initSwipRefreshLis();
    //        return contentView;
    //    }

    @Override
    public View initView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_cross_list, null);
    }

    @Override
    public void initData() {
        mContextReference = new WeakReference<Context>(getActivity());
        mLineCrossAdapter = new LineCrossAdapter(new ArrayList<LineCrossEntity>(),MyApplication.getContext(), R.layout.item_linecross);
        mCrossListView.setAdapter(mLineCrossAdapter);
        initSwipRefreshLis();
        getLineCrossList();

    }

    @Override
    public void onDismissDialog() {

    }

    //    @Override
    //    public void onResume() {
    //        super.onResume();
    //    }

    private boolean planTowerNotSpace() {
        return MyApplication.mAllTowersInMap != null && MyApplication.mAllTowersInMap.size() > 0;
    }

    private boolean tempTowerNotSpace() {
        return MyApplication.mTempTowerMap != null && MyApplication.mTempTowerMap.size() > 0;
    }

    //获取交跨登记列表
    private void getLineCrossList() {
        final StringBuilder sb = new StringBuilder();
        final List<String> lineNames = new ArrayList<>();
        if (planTowerNotSpace() || tempTowerNotSpace()) {
            for (Map.Entry entry : MyApplication.mLineIdNamePairs.entrySet()) {
                sb.append(entry.getKey());
                sb.append(",");
                lineNames.add((String) entry.getValue());
            }
            for (Map.Entry entry : MyApplication.mTempLineMap.entrySet()) {
                if (MyApplication.mLineIdNamePairs.containsKey(entry.getKey().toString())) {
                    continue;
                }
                sb.append(entry.getKey());
                sb.append(",");
                lineNames.add(((Gridline) entry.getValue()).getLineName());
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
        } else {
            ToastUtil.show("请先开启线路");
            return;
        }
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
        //判断是否有网 没网 直接数据库获取 有网 先网络请求
        if (!AppConstant.NET_WORK_AVAILABLE) {
            mSwipeRefreshLayout.setRefreshing(false);
            getDateFromDb(lineNames);
        } else {
            if (MyApplication.registeredTower == null) {
                mLineCrossRequestCall = RetrofitManager.getInstance().getService(ApiService.class).getLineCrossList(sb.toString(),
                        null, null, sysPatrolExecutionID, planDailyPlanSectionIDs);
            } else if (MyApplication.crossSecondTower == null) {
                mLineCrossRequestCall = RetrofitManager.getInstance().getService(ApiService.class).getLineCrossList(null,
                        MyApplication.registeredTower.getSysTowerID() + "",
                        null, sysPatrolExecutionID, planDailyPlanSectionIDs);
            } else {
                mLineCrossRequestCall = RetrofitManager.getInstance().getService(ApiService.class).getLineCrossList(null,
                        MyApplication.registeredTower.getSysTowerID() + "",
                        MyApplication.crossSecondTower.getSysTowerID() + "", sysPatrolExecutionID, planDailyPlanSectionIDs);
            }

            if (!mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(true);
            }

            mLineCrossRequestCall.enqueue(new Callback<BaseCallBack<List<LineCrossEntity>>>() {
                @Override
                public void onResponse(Call<BaseCallBack<List<LineCrossEntity>>> call, Response<BaseCallBack<List<LineCrossEntity>>> response) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (response != null && response.body() != null && response.body().getCode() == 1) {
                        setCrpssType(response.body().getData());
                        LineCrossDBHelper.getInstance().updateLineCross(lineNames, response.body().getData());
                    } else {
                        getDateFromDb(lineNames);
                    }
                }

                @Override
                public void onFailure(Call<BaseCallBack<List<LineCrossEntity>>> call, Throwable t) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    //从本地数据库获取
                    getDateFromDb(lineNames);
                }
            });


        }
    }

    private void setCrpssType(final List<LineCrossEntity> lineCrossEntities) {
        Observable.create(new Observable.OnSubscribe<List<LineCrossEntity>>() {
            @Override
            public void call(Subscriber<? super List<LineCrossEntity>> subscriber) {
                for (LineCrossEntity lineCrossEntity : lineCrossEntities) {
                    DefectType defectType = DefectTypeDBHelper.getInstance().getParentDefect(lineCrossEntity.getCrossType());
                    lineCrossEntity.setCrossTypeFirst(defectType.getDefectName());
                }
                subscriber.onNext(lineCrossEntities);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LineCrossEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<LineCrossEntity> crossEntityList) {
                        mLineCrossAdapter.onDataChange(lineCrossEntities);
                    }
                });

    }

    private void getDateFromDb(List<String> lineNames) {
        if (MyApplication.registeredTower == null || MyApplication.crossSecondTower == null) {
            setCrpssType(LineCrossDBHelper.getInstance().getCacheByLine(lineNames));
        } else {
            setCrpssType(LineCrossDBHelper.getInstance().getLineCrossByTowerId(MyApplication.registeredTower.getSysTowerID().intValue(),
                    MyApplication.crossSecondTower.getSysTowerID().intValue()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLineCrossRequestCall != null && !mLineCrossRequestCall.isCanceled()) {
            mLineCrossRequestCall.cancel();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getLineCrossList();
        }
    }

    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (ProjectManageUtil.isShanDong()) {
                    if (MyApplication.registeredTower==null){
                        ToastUtil.show("请先到位登记");
                        return;
                    }
                    startActivity(CrossRegisterActivity.newInstance(getContext()));
                } else {
                    EventBus.getDefault().post(new ChangePageEvent(3));
                }
                break;
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
    public void onEventMainThread(BaseMainThreadEvent event) {
        if (event instanceof UpdateLineCrossList) {
            getLineCrossList();
        }
    }

    private void initSwipRefreshLis() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (MyApplication.currentNearestTower != null) {
                    getLineCrossList();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
