package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.LineCrossAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 交跨列表页面
 */
public class CrossListActivity extends Activity {

    @BindView(R.id.list_cross)
    ListView mCrossListView;
    private LineCrossAdapter mLineCrossAdapter;
    private Call<BaseCallBack<List<LineCrossEntity>>> mLineCrossRequestCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cross_list);
        ButterKnife.bind(this);
        mLineCrossAdapter = new LineCrossAdapter(new ArrayList<LineCrossEntity>(), this, R.layout.item_linecross);
        mCrossListView.setAdapter(mLineCrossAdapter);
    }

    @OnClick({R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initDatas() {
        getLineCrossList();
    }

    //获取交跨登记列表
    private void getLineCrossList() {
        StringBuilder sb = new StringBuilder();
        if (MyApplication.registeredTower == null) {
            if (MyApplication.mLineIdNamePairs.size() > 0) {
                for (Map.Entry entry : MyApplication.mLineIdNamePairs.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(",");
                }
            }
        } else {
            sb.append(MyApplication.registeredTower.getSysGridLineId()).append(",");
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
        mLineCrossRequestCall = RetrofitManager.getInstance().getService(ApiService.class).getLineCrossList(sb.toString(), null,
                null, sysPatrolExecutionID, planDailyPlanSectionIDs);
        mLineCrossRequestCall.enqueue(new Callback<BaseCallBack<List<LineCrossEntity>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<LineCrossEntity>>> call, Response<BaseCallBack<List<LineCrossEntity>>> response) {
                if (response == null || response.body() == null) {
                    Log.e("RequestErr", "交跨登记response == null || response.body() == null");
                    return;
                }
                if (response.body().getCode() == 0) {
                    Log.e("RequestErr", response.body().getMessage());
                } else {
                    List<LineCrossEntity> lineCrossEntities = response.body().getData();
                    for (LineCrossEntity lineCrossEntity : lineCrossEntities) {
                        DefectType defectType = DefectTypeDBHelper.getInstance().getParentDefect(lineCrossEntity.getCrossType());
                        lineCrossEntity.setCrossTypeFirst(defectType.getDefectName());
                    }
                    mLineCrossAdapter.onDataChange(lineCrossEntities);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<LineCrossEntity>>> call, Throwable t) {
                if (t != null) {
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLineCrossRequestCall != null && !mLineCrossRequestCall.isCanceled()) {
            mLineCrossRequestCall.cancel();
        }
    }
}
