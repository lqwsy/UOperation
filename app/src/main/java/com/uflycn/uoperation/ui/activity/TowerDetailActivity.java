package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.TreeDefectDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.TowerDtailAdapter;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 线路塔详情页面
 */
public class TowerDetailActivity extends Activity {

    @BindView(R.id.list_view_tower_detail)
    ListView lv_tower_detail;

    @BindView(R.id.activity_title)
    TextView tv_activity_title;

    private String lineId;
    private long towerId;
    private String lineName;
    private String towerNum;
    private List<Object> dataList;

    private TowerDtailAdapter towerDtailAdapter;
    private Call<DefectInfo> mTowerDefectsCall;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_tower_detail);

        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        Intent intent = this.getIntent();
        lineName = intent.getStringExtra("lineName");
        towerNum = intent.getStringExtra("towerNum");
        towerId = intent.getLongExtra("towerId", -1);
        lineId = intent.getStringExtra("lineId");
        tv_activity_title.setText(towerNum + "号塔详情");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载" + towerNum + "号塔详情，请稍等");
        progressDialog.show();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mTowerDefectsCall != null && !mTowerDefectsCall.isCanceled()) {
                    mTowerDefectsCall.cancel();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initDatas() {
        dataList = new ArrayList<>();
        getTowerDetailList();
    }

    private void getTowerDetailList() {
        //        mTowerDefectsCall = RetrofitManager.getInstance().getService(ApiService.class).getLineDefectList(lineId, "0", "0", "0", lineName, towerId + "", "");
        String planDailyPlanSectionIDs = "";
        String sysPatrolExecutionID
                = "";
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null) {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId());
            } else {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineSb.toString();
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            sysPatrolExecutionID = MyApplication.mPlanPatrolExecutionId;
        }

        mTowerDefectsCall = RetrofitManager.getInstance().getService(ApiService.class).getLineDefectList(lineId,
                "0", "0", "", lineName, towerId + "",
                "", sysPatrolExecutionID, planDailyPlanSectionIDs);
        mTowerDefectsCall.enqueue(new Callback<DefectInfo>() {
            @Override
            public void onResponse(Call<DefectInfo> call, Response<DefectInfo> response) {
                if (response == null || response.body() == null) {
                    progressDialog.dismiss();
                    ToastUtil.show("暂无本条线路的缺陷信息");
                    return;
                }
                try {
                    if (response.body().getCode() == 0) {
                        Log.e("RequestErr", response.body().getMessage());
                    } else {
                        DefectInfo defectInfo = response.body();
                        if (defectInfo == null) {
                            return;
                        }
                        dataList.clear();
                        if (defectInfo.getCode() == 1) {
                            List<DefectBean> channelDefect = defectInfo.getChannelDefect();
                            dataList.addAll(channelDefect);
                            List<TreeDefectPointBean> treeDefectPoint = defectInfo.getTreeDefectPoint();
                            dataList.addAll(treeDefectPoint);
                            List<DefectBean> propertyDefect = defectInfo.getPropertyDefect();
                            dataList.addAll(propertyDefect);
                            List<DefectBean> personalDefect = defectInfo.getPersonalDefect();
                            dataList.addAll(personalDefect);
                            List<DefectBean> lineCrossList = defectInfo.getLineCrossList();
                            dataList.addAll(lineCrossList);

                            if (towerDtailAdapter == null) {
                                towerDtailAdapter = new TowerDtailAdapter(dataList, UIUtils.getContext(), R.layout.item_tower_detail);
                                lv_tower_detail.setAdapter(towerDtailAdapter);
                                progressDialog.dismiss();
                            }

                        }
                    }

                } catch (Exception e) {
                    getAllDefectsByDb();
                }
            }

            @Override
            public void onFailure(Call<DefectInfo> call, Throwable t) {
                if (t != null) {
                    getAllDefectsByDb();
                }
            }
        });
    }

    private void getAllDefectsByDb() {
        if (MyApplication.mLineIdNamePairs != null && MyApplication.mLineIdNamePairs.size() > 0) {
            for (String lineName : MyApplication.mLineIdNamePairs.values()) {
                dataList.addAll(DefectBeanDBHelper.getInstance().getDefectByTowerNum(lineName, towerNum));
                dataList.addAll(TreeDefectDBHelper.getInstance().getDefectByTowerNum(lineName, towerNum));
            }
        }
        if (towerDtailAdapter == null) {
            towerDtailAdapter = new TowerDtailAdapter(dataList, UIUtils.getContext(), R.layout.item_tower_detail);
            if (lv_tower_detail != null) {
                lv_tower_detail.setAdapter(towerDtailAdapter);
                progressDialog.dismiss();
            }
        }
    }


    @OnClick({R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTowerDefectsCall != null && !mTowerDefectsCall.isCanceled()) {
            mTowerDefectsCall.cancel();
        }
    }
}
