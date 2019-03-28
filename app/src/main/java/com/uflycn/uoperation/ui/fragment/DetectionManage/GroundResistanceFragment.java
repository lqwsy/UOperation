package com.uflycn.uoperation.ui.fragment.DetectionManage;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.EarthingResistance;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.EarthingResistanceDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.ViewPagerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 接地电阻
 */
public class GroundResistanceFragment extends ViewPagerFragment {

    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_tower_no)
    TextView mTvTowerNo;
    @BindView(R.id.et_resistance_a)
    EditText mEtResistanceA;
    @BindView(R.id.et_resistance_b)
    EditText mEtResistanceB;
    @BindView(R.id.et_resistance_c)
    EditText mEtResistanceC;
    @BindView(R.id.et_resistance_d)
    EditText mEtResistanceD;
    @BindView(R.id.layout_grounding_resistance)
    LinearLayout mLayoutGroundingResistance;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    Call<BaseCallBack<String>> call1;

    @Override
    public View initView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_ground_resistance, null);
    }

    @Override
    public void initData() {
        mLayoutGroundingResistance.setVisibility(View.VISIBLE);
        if (MyApplication.registeredTower != null) {
            mTvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
            Gridline gridline = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
            mTvLineName.setText(gridline.getLineName());
        }


    }

    @OnClick({R.id.btn_submit})
    public void doClick(View v) {
        //        if (MyApplication.registeredTower == null || MyApplication.currentNearestTower.getSysTowerID() != MyApplication.registeredTower.getSysTowerID()) {
        //            ToastUtil.show(mWeakReference.get().getString(R.string.tip_far_awayfrom_tower));
        //            return;
        //        }
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(getString(R.string.tip_far_awayfrom_tower));
            return;
        }
        submitResistance();

    }

    /**
     * 提交接地电阻数据
     */
    private void submitResistance() {
        if (MyApplication.registeredTower != null) {
            if ((TextUtils.isEmpty(mEtResistanceA.getText()) && TextUtils.isEmpty(mEtResistanceB.getText()) &&
                    TextUtils.isEmpty(mEtResistanceC.getText()) && TextUtils.isEmpty(mEtResistanceD.getText())) ||
                    (checkPoint(mEtResistanceA) || checkPoint(mEtResistanceB) || checkPoint(mEtResistanceC) || checkPoint(mEtResistanceD))
                    ) {
                ToastUtil.show("至少填入一个数据！");
                return;
            }
            final EarthingResistance earthingResistance = createResistance();
            EarthingResistanceDBHelper.getInstance().insert(earthingResistance);
            postEarthNet(earthingResistance);
        }
    }

    private EarthingResistance createResistance() {
        EarthingResistance earthingResistance = new EarthingResistance();
        earthingResistance.setTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
        earthingResistance.setSysUserId(AppConstant.currentUser.getUserId());
        earthingResistance.setUploadFlag(0);
        earthingResistance.setCreateDate(DateUtil.format(new Date()));
        earthingResistance.setSysUserId(AppConstant.currentUser.getUserId());
        if (!(TextUtils.isEmpty(mEtResistanceA.getText()) || checkPoint(mEtResistanceA))) {
            earthingResistance.setResistanceA(Double.parseDouble(mEtResistanceA.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mEtResistanceB.getText()) || checkPoint(mEtResistanceB))) {
            earthingResistance.setResistanceB(Double.parseDouble(mEtResistanceB.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mEtResistanceC.getText()) || checkPoint(mEtResistanceC))) {
            earthingResistance.setResistanceC(Double.parseDouble(mEtResistanceC.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mEtResistanceD.getText()) || checkPoint(mEtResistanceD))) {
            earthingResistance.setResistanceD(Double.parseDouble(mEtResistanceD.getText().toString()));
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                earthingResistance.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                earthingResistance.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            earthingResistance.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        return earthingResistance;
    }

    private void postEarthNet(final EarthingResistance earthingResistance) {
        mProgressDialog.show();
        Gson gson = new Gson();
        List<EarthingResistance> list = new ArrayList<>();
        list.add(earthingResistance);
        String jsonStr = gson.toJson(list);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        call1 = RetrofitManager.getInstance().getService(ApiService.class)
                .postEarthingResistance(body);
        call1.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mProgressDialog.dismiss();
                BaseCallBack<String> body = response.body();
                if (body != null && body.getCode() == 1) {
                    earthingResistance.setUploadFlag(1);
                    EarthingResistanceDBHelper.getInstance().updateById(earthingResistance);
                    ToastUtil.show("提交成功");
                } else {
                    DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postEarthNet(earthingResistance);
                        }
                    });
                }
                resetEarth();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                //                ToastUtil.show("提交成功");
                mProgressDialog.dismiss();
                DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postEarthNet(earthingResistance);
                    }
                });
                resetEarth();
            }
        });
    }

    private void resetEarth() {
        mEtResistanceA.setText("");
        mEtResistanceB.setText("");
        mEtResistanceC.setText("");
        mEtResistanceD.setText("");
    }


    public boolean checkPoint(EditText editText) {
        if (editText.getText().toString().equals(".") || editText.getText().toString().equals("-") || editText.getText().toString().equals("-.")) {
            return true;
        }
        return false;
    }

    @Override
    public void onDismissDialog() {
        if (call1 != null && !call1.isCanceled()) {
            call1.cancel();
        }
    }

    @Override
    public void onDestroy() {
        if (call1 != null && !call1.isCanceled()) {
            call1.cancel();
        }
        super.onDestroy();
    }
}
