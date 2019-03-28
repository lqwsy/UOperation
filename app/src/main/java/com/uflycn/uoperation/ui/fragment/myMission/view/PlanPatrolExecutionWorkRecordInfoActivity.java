package com.uflycn.uoperation.ui.fragment.myMission.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.ui.adapter.PlanPatrolExecutionInfoAdapter;
import com.uflycn.uoperation.ui.fragment.myMission.presenter.MyMissionPresenterImp;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 任务详情
 */
public class PlanPatrolExecutionWorkRecordInfoActivity extends Activity implements MyMissionView.PlanPatrolExecutionRecordInfoView {

    @BindView(R.id.tv_mission_name)
    TextView mTvMissionName;
    @BindView(R.id.tv_mission_time)
    TextView mTvMissionTime;
    @BindView(R.id.tv_mission_type)
    TextView mTvMissionType;
    @BindView(R.id.tv_mission_content)
    TextView mTvMissionContent;
    @BindView(R.id.lv_mission_line)
    ListView mLvMissionLine;//工作路线
    private PlanPatrolExecution mExecution;
    private PlanPatrolExecutionInfoAdapter mAdapter;
    private MyMissionPresenterImp mPresenter;


    public static Intent newInstance(Context context, PlanPatrolExecution planPatrolExecution) {
        Intent intent = new Intent(context, PlanPatrolExecutionWorkRecordInfoActivity.class);
        intent.putExtra("planPatrolExecution", planPatrolExecution);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_plan_patrol_execution_record_info);

        ButterKnife.bind(this);
        mPresenter = new MyMissionPresenterImp(this);
        initView();
        initData();
    }

    private void initData() {
        mPresenter.getPlanPatrolExecutionRecordInfo(mExecution.getSysPatrolExecutionID());
    }

    private void initView() {
        mExecution = (PlanPatrolExecution) getIntent().getSerializableExtra("planPatrolExecution");
        mTvMissionName.setText(mExecution.getTaskName());
        mTvMissionTime.setText(mExecution.getStartDateString() + " 至  " + mExecution.getEndDateString());
        mTvMissionType.setText(mExecution.getTypeOfWork());
        mTvMissionContent.setText(mExecution.getWorkContent());

        mAdapter = new PlanPatrolExecutionInfoAdapter(new ArrayList<PlanPatrolExecutionWorkRecordInfo>(), this, R.layout.item_plan_patrol_excution_info);
        mLvMissionLine.setAdapter(mAdapter);

    }

    @Override
    public void onSuccess(List<PlanPatrolExecutionWorkRecordInfo> list) {
        mAdapter.onDataChange(list);//工作线路数据插入
    }

    @Override
    public void onFailed(String msg) {
        ToastUtil.show(msg);
    }

    @OnClick(R.id.iv_back)
    public void doClick(View view) {
        finish();
    }
}
