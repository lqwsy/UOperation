package com.uflycn.uoperation.ui.fragment.myMission.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.bean.PlanPatrolExecutionTowerList;
import com.uflycn.uoperation.eventbus.LoadLineFromPlanPatrolEvent;
import com.uflycn.uoperation.eventbus.StopLineFromPlanPatrilExecutionEvent;
import com.uflycn.uoperation.ui.adapter.PlanPatrolExecutionAdapter;
import com.uflycn.uoperation.ui.fragment.myMission.presenter.MyMissionPresenterImp;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyMissionFragment extends Fragment implements MyMissionView.PlanPatrolExecutionView {

    @BindView(R.id.lv_work_manage)
    ListView mLvWorkManage;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private WeakReference<Activity> mRef;
    private PlanPatrolExecutionAdapter mAdapter;
    private MyMissionPresenterImp mPresenter;
    private ProgressDialog mProgressDialog;
    private PlanPatrolExecution mPlanPatrolExecution;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_mission, container, false);
        ButterKnife.bind(this, view);
        mRef = new WeakReference<Activity>(getActivity());
        mPresenter = new MyMissionPresenterImp(this);
        initView();
        getData();
        return view;
    }

    private void initView() {
        mAdapter = new PlanPatrolExecutionAdapter(new ArrayList<PlanPatrolExecution>(), mRef.get(), R.layout.item_plan_patrol_execution);
        mLvWorkManage.setAdapter(mAdapter);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPlanPatrolExecution = mAdapter.getPlanPatrolExecution();
                getData();
            }
        });
        mAdapter.setListener(new PlanPatrolExecutionAdapter.onMissionAdapterListener() {
            @Override
            public void onClickMissionInfo(PlanPatrolExecution execution) {
                startActivity(PlanPatrolExecutionWorkRecordInfoActivity.newInstance(mRef.get(), execution));
            }

            @Override
            public void onClickOpenMission(PlanPatrolExecution execution) {
                if (!mAdapter.isCurrenMission(execution)) {
                    ToastUtil.show("请先关闭其他的任务");
                    return;
                }
                if (mAdapter.isPlanPatrol(execution)) {
                    //关闭
                    MyApplication.gridlineTaskStatus = 0;
                    EventBus.getDefault().post(new StopLineFromPlanPatrilExecutionEvent(execution.getSysGridLineIDs()));
                    MyApplication.mPlanPatrolExecutionId = "";
                    MyApplication.mTypeString = "";
                } else {
                    //开启
                    getPlanPatrolLine(execution);

                }
                mAdapter.updatePlanPatrolState(execution);
            }
        });
        mProgressDialog = new ProgressDialog(mRef.get());
        mProgressDialog.setMessage("正在获取线路数据...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPresenter.cancel();
            }
        });

    }

    private void getPlanPatrolLine(PlanPatrolExecution execution) {
        mProgressDialog.show();
        mPresenter.getPlanPatrolExecutionLine(execution);

    }

    @Override
    public void onDestroyView() {
        mPresenter.cancel();
        super.onDestroyView();
    }

    private void getData() {
        mPresenter.getPlanPatrolExecutionList();
    }

    @OnClick({R.id.iv_open_close_drawer})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
                break;
        }
    }

    @Override
    public void onSuccess(List<PlanPatrolExecution> list) {
        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mAdapter.onDataChange(list);
    }

    @Override
    public void onSuccessGetTower(PlanPatrolExecution execution, List<PlanPatrolExecutionTowerList> list) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        MyApplication.mTypeString = execution.getTypeString();
        MyApplication.gridlineTaskStatus = 3;
        EventBus.getDefault().post(new LoadLineFromPlanPatrolEvent(list));
        MyApplication.mPlanPatrolExecutionId = execution.getSysPatrolExecutionID();
    }

    @Override
    public void onFailed(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        ToastUtil.show(msg);
    }
}
