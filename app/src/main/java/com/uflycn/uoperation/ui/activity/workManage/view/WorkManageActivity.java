package com.uflycn.uoperation.ui.activity.workManage.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordList;
import com.uflycn.uoperation.ui.activity.workManage.presenter.AddWorkManagePresenterImp;
import com.uflycn.uoperation.ui.adapter.PlanPatrolExecutionWorkRecordListAdapter;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作业管理
 */
public class WorkManageActivity extends Activity implements AddWorkManageView.PlanPatrolExecutionRecordListView {

    @BindView(R.id.top_layout)
    RelativeLayout mTopLayout;
    @BindView(R.id.lv_work_manage)
    ListView mLvWorkManage;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private PlanPatrolExecutionWorkRecordListAdapter mAdapter;
    private AddWorkManagePresenterImp mPresenter;

    public static Intent newInstance(Context context) {
        return new Intent(context, WorkManageActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_work_manage);

        ButterKnife.bind(this);
        mPresenter = new AddWorkManagePresenterImp(this);
        initView();
    }

    private void initView() {
        mAdapter = new PlanPatrolExecutionWorkRecordListAdapter(new ArrayList<PlanPatrolExecutionWorkRecordList>(),
                this, R.layout.item_plan_patrol_execution_work_record_list);
        mLvWorkManage.setAdapter(mAdapter);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mPresenter.getPlanPatrolExecutionWorkRecordList();
    }

    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                if (MyApplication.mTypeString.equals("巡视") || MyApplication.mTypeString.equals("检测")) {
                    ToastUtil.show("当前模式无法新增作业");
                    return;
                }
                startActivity(AddWorkManageActivity.newInstance(this));
                break;
        }
    }

    @Override
    public void onSuccess(List<PlanPatrolExecutionWorkRecordList> list) {
        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        mAdapter.onDataChange(list);
    }

    @Override
    public void onFailed(String msg) {
        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        ToastUtil.show(msg);
    }
}
