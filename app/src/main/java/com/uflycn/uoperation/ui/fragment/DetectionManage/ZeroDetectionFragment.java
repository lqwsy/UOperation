package com.uflycn.uoperation.ui.fragment.DetectionManage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.ZeroDetection;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.SelectTowerEvent;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.ZeroDetectionDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.SelectTowerActivity;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.ViewPagerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 零值检测
 */
public class ZeroDetectionFragment extends ViewPagerFragment {


    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_tower_no)
    TextView mTvTowerNo;
    @BindView(R.id.rb_straight_tower)
    RadioButton mRbStraightTower;
    @BindView(R.id.rb_tension_tower)
    RadioButton mRbTensionTower;
    @BindView(R.id.rg_tower_type)
    RadioGroup mRgTowerType;
    @BindView(R.id.et_jueyuan_count)
    EditText mEtJueyuanCount;
    @BindView(R.id.tv_tiaoxian_count)
    TextView mTvTiaoxianCount;
    @BindView(R.id.et_tiaoxian_count)
    EditText mEtTiaoxianCount;
    @BindView(R.id.rb_single_loop)
    RadioButton mRbSingleLoop;
    @BindView(R.id.rb_double_loop)
    RadioButton mRbDoubleLoop;
    @BindView(R.id.rg_loop_type)
    RadioGroup mRgLoopType;
    @BindView(R.id.rb_tx_single_loop)
    RadioButton mRbTxSingleLoop;
    @BindView(R.id.rb_tx_double_loop)
    RadioButton mRbTxDoubleLoop;
    @BindView(R.id.rg_tiaoxian_type)
    RadioGroup mRgTiaoxianType;
    @BindView(R.id.btn_select_tower)
    TextView mBtnSelectTower;
    @BindView(R.id.img_tower_overview)
    ImageView mImgTowerOverview;
    @BindView(R.id.layout_zero_check)
    LinearLayout mLayoutZeroCheck;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    /**
     * 直线塔零值检测数据
     */
    private int[][] mSingleTower;

    /**
     * 耐张塔零值检测数据
     */
    private Map<String, int[]> mMap;
    private int jumpCount;
    private int insulatorCount;
    Call<BaseCallBack<List<ListCallBack<String>>>> call2;

    @Override
    public View initView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_zero_detection, null);
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        mLayoutZeroCheck.setVisibility(View.VISIBLE);
        if (MyApplication.registeredTower != null) {
            mTvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
            Gridline gridline = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
            mTvLineName.setText(gridline.getLineName());
        }
        mRgTowerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_straight_tower:
                        mTvTiaoxianCount.setVisibility(View.GONE);
                        mEtTiaoxianCount.setVisibility(View.GONE);
                        mRgTiaoxianType.setVisibility(View.GONE);
                        break;
                    case R.id.rb_tension_tower:
                        mTvTiaoxianCount.setVisibility(View.VISIBLE);
                        mEtTiaoxianCount.setVisibility(View.VISIBLE);
                        mRgTiaoxianType.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                mSingleTower = null;
                mMap = null;
                mImgTowerOverview.setVisibility(View.GONE);
            }
        });
        mRgLoopType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSingleTower = null;
                mMap = null;
                mImgTowerOverview.setVisibility(View.GONE);
            }
        });
        mTvTiaoxianCount.setVisibility(View.GONE);
        mEtTiaoxianCount.setVisibility(View.GONE);
        mRgTiaoxianType.setVisibility(View.GONE);
        resetZero();

    }


    @OnClick({R.id.btn_submit})
    public void doClick(View v) {
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(getContext().getString(R.string.tip_far_awayfrom_tower));
            return;
        }
        submitZeroCheck();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 标注零值
     * @param view
     */
    @OnClick({R.id.btn_select_tower})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_tower:
                startSelectTower();
                break;
            default:
                break;
        }
    }

    private void startSelectTower() {


        if (mEtJueyuanCount.getText().toString().equals("0")) {
            ToastUtil.show("绝缘子片数不能为0");
            return;
        }
        if (mEtJueyuanCount.getText().toString().contains(".")) {
            ToastUtil.show("绝缘子片数不合法");
            return;
        }
        //数值太大会造成崩溃
        if (mEtJueyuanCount.getText().toString().length() > 2) {
            ToastUtil.show("绝缘子片数最大不超过20");
            return;
        }
        if (StringUtils.isEmptyOrNull(mEtJueyuanCount.getText().toString()) || Integer.valueOf(mEtJueyuanCount.getText().toString()) > 20) {
            ToastUtil.show("绝缘子片数不能为空或者最大不超过20");
            return;
        }
        if (getTowerType() == 1) {
            if (mEtTiaoxianCount.getText().toString().equals("0")) {
                ToastUtil.show("跳线片数不能为0");
                return;
            }
            if (mEtTiaoxianCount.getText().toString().length() > 2) {
                ToastUtil.show("跳线片数最大不超过20");
                return;
            }

            if (StringUtils.isEmptyOrNull(mEtTiaoxianCount.getText().toString()) || Integer.valueOf(mEtTiaoxianCount.getText().toString()) > 20) {
                ToastUtil.show("跳线片数不能为空或者最大不超过20");
                return;
            }
            jumpCount = Integer.valueOf(mEtTiaoxianCount.getText().toString());

        }
        insulatorCount = Integer.valueOf(mEtJueyuanCount.getText().toString());

        Intent intent = new Intent(getContext(), SelectTowerActivity.class);
        intent.putExtra(AppConstant.TOWER_TYPE, getTowerType());
        intent.putExtra(AppConstant.TOWER_SUB_TYPE, getTowerSubType());
        intent.putExtra(AppConstant.INSULATOR_COUNT, insulatorCount);
        if (getTowerType() == 1) {
            intent.putExtra(AppConstant.JUMP_TYPE, getJumpType());
            intent.putExtra(AppConstant.JUMP_COUNT, jumpCount);
            if (mMap != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.SELECT_TOWER, (Serializable) mMap);
                intent.putExtras(bundle);
            }
            //            intent.putExtra(AppConstant.SELECT_TOWER,mMap);
        } else {
            if (mSingleTower != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstant.SELECT_TOWER, mSingleTower);
                intent.putExtras(bundle);
            }
        }

        startActivity(intent);
    }


    private void submitZeroCheck() {
        if (MyApplication.registeredTower != null) {
            if (mSingleTower == null && mMap == null) {
                ToastUtil.show("请标注零值！");
                return;
            }
            mProgressDialog.show();

            final ZeroDetection zeroDetection = initZeroCheck();
            ZeroDetectionDBHelper.getInstance().insertZeroDetection(zeroDetection);
            postZeroNet(zeroDetection);
        }
    }

    private void postZeroNet(final ZeroDetection zeroDetection) {
        Gson gson = new Gson();
        List<ZeroDetection> list = new ArrayList<>();
        list.add(zeroDetection);
        String jsonStr = gson.toJson(list);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        call2 = RetrofitManager.getInstance().getService(ApiService.class).postZeroDetection(body);
        call2.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                mProgressDialog.dismiss();
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    zeroDetection.setUploadFlag(1);
                    ZeroDetectionDBHelper.getInstance().updateZeroDetction(zeroDetection);
                    ToastUtil.show("提交成功");
                } else {
                    DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postZeroNet(zeroDetection);
                        }
                    });
                }
                resetZero();
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                mProgressDialog.dismiss();
              /*  ToastUtil.show("存储到数据库");
                resetZero();
                if (t != null) {
                    Log.e("responseErr", "onFailure: " + t.getMessage());
                }*/
                DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postZeroNet(zeroDetection);
                    }
                });
                resetZero();
            }
        });
    }

    private ZeroDetection initZeroCheck() {
        ZeroDetection zeroDetection = new ZeroDetection();
        switch (mRgTowerType.getCheckedRadioButtonId()) {
            case R.id.rb_straight_tower:
                ArrayList<String> array = StringUtils.intArray2String(mSingleTower);
                if (array.size() <= 3) {
                    zeroDetection.setPhaseALeft1(array.get(0));
                    zeroDetection.setPhaseBLeft1(array.get(1));
                    zeroDetection.setPhaseCLeft1(array.get(2));
                } else {
                    zeroDetection.setPhaseALeft1(array.get(0));
                    zeroDetection.setPhaseALeft2(array.get(1));
                    zeroDetection.setPhaseBLeft1(array.get(2));
                    zeroDetection.setPhaseBLeft2(array.get(3));
                    zeroDetection.setPhaseCLeft1(array.get(4));
                    zeroDetection.setPhaseCLeft2(array.get(5));
                }


                break;
            case R.id.rb_tension_tower:
                zeroDetection.setPhaseALeft1(StringUtils.array2String(mMap.get("PhaseALeft1")));
                zeroDetection.setPhaseALeft2(StringUtils.array2String(mMap.get("PhaseALeft2")));
                zeroDetection.setPhaseBLeft1(StringUtils.array2String(mMap.get("PhaseBLeft1")));
                zeroDetection.setPhaseBLeft2(StringUtils.array2String(mMap.get("PhaseBLeft2")));
                zeroDetection.setPhaseCLeft1(StringUtils.array2String(mMap.get("PhaseCLeft1")));
                zeroDetection.setPhaseCLeft2(StringUtils.array2String(mMap.get("PhaseCLeft2")));

                zeroDetection.setPhaseARight1(StringUtils.array2String(mMap.get("PhaseARight1")));
                zeroDetection.setPhaseARight2(StringUtils.array2String(mMap.get("PhaseARight2")));
                zeroDetection.setPhaseBRight1(StringUtils.array2String(mMap.get("PhaseBRight1")));
                zeroDetection.setPhaseBRight2(StringUtils.array2String(mMap.get("PhaseBRight2")));
                zeroDetection.setPhaseCRight1(StringUtils.array2String(mMap.get("PhaseCRight1")));
                zeroDetection.setPhaseCRight2(StringUtils.array2String(mMap.get("PhaseCRight2")));

                zeroDetection.setJumperA1(StringUtils.array2String(mMap.get("JumperA1")));
                zeroDetection.setJumperA2(StringUtils.array2String(mMap.get("JumperA2")));
                zeroDetection.setJumperB1(StringUtils.array2String(mMap.get("JumperB1")));
                zeroDetection.setJumperB2(StringUtils.array2String(mMap.get("JumperB2")));
                zeroDetection.setJumperC1(StringUtils.array2String(mMap.get("JumperC1")));
                zeroDetection.setJumperC2(StringUtils.array2String(mMap.get("JumperC2")));

                zeroDetection.setJumperStringLength(jumpCount);
                zeroDetection.setJumperType(getJumpType() == 0 ? "1" : "2");
                break;
            default:
                break;
        }

        zeroDetection.setCreateDate(DateUtil.format(new Date()));
        zeroDetection.setTowerType(getTowerType() == 0 ? "1" : "2");
        zeroDetection.setInsulatorType(getTowerSubType() == 0 ? "1" : "2");
        zeroDetection.setInsulatorStringLength(insulatorCount);
        zeroDetection.setSysTowerId(MyApplication.registeredTower.getSysTowerID() + "");
        zeroDetection.setSysUserId(AppConstant.currentUser.getUserId());
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                zeroDetection.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                zeroDetection.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            zeroDetection.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }


        return zeroDetection;
    }

    private int getJumpType() {
        int result = -1;
        switch (mRgTiaoxianType.getCheckedRadioButtonId()) {
            case R.id.rb_tx_single_loop:
                result = 0;
                break;
            case R.id.rb_tx_double_loop:
                result = 1;
                break;
            default:
                break;
        }
        return result;
    }

    private int getTowerSubType() {
        int result = -1;
        switch (mRgLoopType.getCheckedRadioButtonId()) {
            case R.id.rb_single_loop:
                result = 0;
                break;
            case R.id.rb_double_loop:
                result = 1;
                break;
            default:
                break;
        }
        return result;
    }

    private int getTowerType() {
        int result = -1;
        switch (mRgTowerType.getCheckedRadioButtonId()) {
            case R.id.rb_straight_tower:
                result = 0;
                break;
            case R.id.rb_tension_tower:
                result = 1;
                break;
            default:
                break;
        }
        return result;
    }

    public void resetZero() {
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(getContext().getString(R.string.tip_far_awayfrom_tower));
            return;
        }
        Gridline gridline = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
        switch (Integer.valueOf(gridline.getVoltageClass())) {
            case 2:
                jumpCount = insulatorCount = 4;
                break;
            case 15:
                jumpCount = insulatorCount = 7;
                break;
            case 16:
                jumpCount = insulatorCount = 16;
                break;
            default:
                jumpCount = insulatorCount = 18;
                break;
        }

        mEtTiaoxianCount.setText(jumpCount + "");
        mEtJueyuanCount.setText(insulatorCount + "");

        mImgTowerOverview.setVisibility(View.GONE);
        mMap = null;
        mSingleTower = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SelectTowerEvent event) {
        mImgTowerOverview.setVisibility(View.VISIBLE);
        mImgTowerOverview.setImageBitmap(event.getBitmap());
        switch (mRgTowerType.getCheckedRadioButtonId()) {
            case R.id.rb_straight_tower:
                mSingleTower = event.getSingTowers();
                break;
            case R.id.rb_tension_tower:
                mMap = event.getMap();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDismissDialog() {
        if (call2 != null && !call2.isCanceled()) {
            call2.cancel();
        }
    }

    @Override
    public void onDestroy() {
        if (call2 != null && !call2.isCanceled()) {
            call2.cancel();
        }
        super.onDestroy();
    }
}
