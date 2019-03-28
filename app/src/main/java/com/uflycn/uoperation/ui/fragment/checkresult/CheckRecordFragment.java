package com.uflycn.uoperation.ui.fragment.checkresult;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.EarthingResistance;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.IceCover;
import com.uflycn.uoperation.bean.InfraredTemperature;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.OptTensilePointTemperature;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.ZeroDetection;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.SelectTowerEvent;
import com.uflycn.uoperation.greendao.EarthingResistanceDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.IceCoverDBHelper;
import com.uflycn.uoperation.greendao.InfraredTemperatureDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.ZeroDetectionDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.SelectTowerActivity;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.TowerDistanceUtils;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;

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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 检测记录
 * Created by xiaoyehai on 2017/9/8.
 */
public class CheckRecordFragment extends DemoBaseFragment {

    @BindView(R.id.rg_check_type)
    RadioGroup mrgCheckType;

    @BindView(R.id.layout_grounding_resistance)
    LinearLayout mResistanceLayout;

    @BindView(R.id.layout_infrared_temperature)
    LinearLayout mTemperature;

    @BindView(R.id.layout_zero_check)
    LinearLayout mZeroCheck;


    @BindView(R.id.layout_ice_cover)
    LinearLayout mIceCover;


    @BindView(R.id.rg_tower_type)
    RadioGroup mTowerType;

    @BindView(R.id.rg_loop_type)
    RadioGroup mTowerSubType;

    @BindView(R.id.rg_joint)
    RadioGroup mRgJoint;

    @BindView(R.id.rg_part_selector)
    RadioGroup mPartSelector;

    @BindView(R.id.layout_naizhang)
    LinearLayout layoutNaizhangTempture;

    @BindView(R.id.layout_middle)
    LinearLayout layoutMiddleTempture;

    @BindView(R.id.layout_single_line)
    LinearLayout mSingleLineLayout;

    @BindView(R.id.layout_double_line)
    LinearLayout mDoubleLineLayout;

    @BindView(R.id.et_resistance_a)
    EditText mResistanceA;

    @BindView(R.id.et_resistance_b)
    EditText mResistanceB;

    @BindView(R.id.et_resistance_c)
    EditText mResistanceC;

    @BindView(R.id.et_resistance_d)
    EditText mResistanceD;

    @BindView(R.id.et_guide_line)
    EditText guideLine;

    @BindView(R.id.et_environment_temperature)
    EditText envTemperature;

    @BindView(R.id.et_center_a)
    EditText centerA;

    @BindView(R.id.et_center_b)
    EditText centerB;

    @BindView(R.id.et_center_c)
    EditText centerC;

    @BindView(R.id.et_big_size_slide_a)
    EditText etBigA;

    @BindView(R.id.et_small_size_slide_a)
    EditText etSmallA;

    @BindView(R.id.et_big_size_slide_b)
    EditText etBigB;

    @BindView(R.id.et_small_size_slide_b)
    EditText etSmallB;

    @BindView(R.id.et_big_size_slide_c)
    EditText etBigC;

    @BindView(R.id.et_small_size_slide_c)
    EditText etSmallC;

    @BindView(R.id.et_double_big_size_slide_up_a)
    EditText etDoubleBigUpA;

    @BindView(R.id.et_double_big_size_slide_down_a)
    EditText etDoubleBigDownA;

    @BindView(R.id.et_double_big_size_slide_up_b)
    EditText etDoubleBigUpB;

    @BindView(R.id.et_double_big_size_slide_down_b)
    EditText etDoubleBigDownbB;

    @BindView(R.id.et_double_big_size_slide_up_c)
    EditText etDoubleBigUpC;

    @BindView(R.id.et_double_big_size_slide_down_c)
    EditText etDoubleBigDownbC;

    @BindView(R.id.et_double_small_size_slide_up_a)
    EditText etDoubleSmallUpA;

    @BindView(R.id.et_double_small_size_slide_down_a)
    EditText etDoubleSmallDownA;

    @BindView(R.id.et_double_small_size_slide_up_b)
    EditText etDoubleSmallUpB;

    @BindView(R.id.et_double_small_size_slide_down_b)
    EditText etDoubleSmallDownbB;

    @BindView(R.id.et_double_small_size_slide_up_c)
    EditText etDoubleSmallUpC;

    @BindView(R.id.et_double_small_size_slide_down_c)
    EditText etDoubleSmallDownbC;


    @BindView(R.id.et_ice_cover)
    EditText mEtIceCover;

    @BindView(R.id.spinner_ice_type)
    Spinner mSpIceType;

    @BindView(R.id.et_ice_temp)
    EditText mEtIceTemp;

    @BindView(R.id.et_ice_humidity)
    EditText mEtIceHumidity;

    @BindView(R.id.tv_line_name)
    TextView tvLineName;

    @BindView(R.id.tv_tower_no)
    TextView tvTowerNo;

    @BindView(R.id.img_tower_overview)
    ImageView imgSelectTower;

    //后期增加的零值的几个字段
    @BindView(R.id.tv_tiaoxian_count)
    TextView tvTiaoXianCount;
    @BindView(R.id.et_tiaoxian_count)
    EditText etTiaoXianCount;
    @BindView(R.id.et_jueyuan_count)
    EditText etJueYuanCount;
    @BindView((R.id.rg_tiaoxian_type))
    RadioGroup rgTiaoXianType;

    // 后期对红外测温进行修改
    @BindView(R.id.rg_middle_joint)
    RadioGroup rgMiddleJoint;

    @BindView(R.id.layout_middle_single_line)
    LinearLayout layoutMiddleSingleLine;

    @BindView(R.id.layout_middle_double_line)
    LinearLayout layoutMiddleDoubleLine;

    // 单导线的3相输入值
    @BindView(R.id.et_middle_single_a)
    EditText etMiddleSingleA;

    @BindView(R.id.et_middle_single_b)
    EditText etMiddleSingleB;

    @BindView(R.id.et_middle_single_c)
    EditText etMiddleSingleC;

    // 双导线的3相输入值
    @BindView(R.id.et_middle_double_up_a)
    EditText etMiddleDoubleUpA;

    @BindView(R.id.et_middle_double_down_a)
    EditText etMiddleDoubleDownA;

    @BindView(R.id.et_middle_double_up_b)
    EditText etMiddleDoubleUpB;

    @BindView(R.id.et_middle_double_down_b)
    EditText etMiddleDoubleDownB;

    @BindView(R.id.et_middle_double_up_c)
    EditText etMiddleDoubleUpC;

    @BindView(R.id.et_middle_double_down_c)
    EditText etMiddleDoubleDownC;


    //导线类型,这2个既然读数据库，则
    //        public static final String SINGLE_LOOP_TYPE = "0cf4d742-932b-4ab9-b507-26537c8b4f2a";
    //        public static final String DOUBLE_LOOP_TYPE = "d4f35a29-55f6-4f05-87e8-30f30c0a7ed2";

    private String SINGLE_LOOP_TYPE; // 单导线
    private String DOUBLE_LOOP_TYPE; // 双导线

    private String currentLoopType;// 耐张接头导线类型

    private int SINGLE_WIRE_TYPE = 0;// 单导线
    private int DOUBLE_WIRE_TYPE = 1;//双导线
    private int mCurrentWirewayType = SINGLE_WIRE_TYPE;

    private String mCurrentLoopTypeForMiddle; // 中间接头导线类型

    private InfraredTemperature infraredTemperature;

    //耐张为0：中间接头为1；
    private int mJointType = 1;

    // 弹出的等待框
    private ProgressDialog mProgressDialog;

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

    Call<BaseCallBack<List<ListCallBack<String>>>> call0;
    Call<BaseCallBack<String>> call1;
    Call<BaseCallBack<List<ListCallBack<String>>>> call2;
    Call<BaseCallBack<String>> call3;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_check_record;
    }

    @Override
    protected void initView() {
        // 从数据库中，读取单双导线，对应的值
        //        SINGLE_LOOP_TYPE = ItemDetailDBHelper.getInstance().getItemDetailByItemsName("单导线").getItemDetailsId();
        //        DOUBLE_LOOP_TYPE = ItemDetailDBHelper.getInstance().getItemDetailByItemsName("双导线").getItemDetailsId();

        if (MyApplication.registeredTower != null) {
            String towerNo = MyApplication.registeredTower.getTowerNo();
            tvTowerNo.setText(towerNo);
            String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.registeredTower.getSysGridLineId() + "");
            if (StringUtils.isEmptyOrNull(lineName)) {
                lineName = MyApplication.mTempLineMap.get(MyApplication.registeredTower.getSysGridLineId()).getLineName();
            }
            tvLineName.setText(lineName);
        }
        mProgressDialog = new ProgressDialog(mWeakReference.get());
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (call0 != null && !call0.isCanceled()) {
                    call0.cancel();
                }
                if (call1 != null && !call1.isCanceled()) {
                    call1.cancel();
                }
                if (call2 != null && !call2.isCanceled()) {
                    call2.cancel();
                }
                if (call3 != null && !call3.isCanceled()) {
                    call3.cancel();
                }
            }
        });
        mrgCheckType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    // 接地电阻
                    case R.id.rb_grounding_resistance:
                        mResistanceLayout.setVisibility(View.VISIBLE);
                        mTemperature.setVisibility(View.GONE);
                        mZeroCheck.setVisibility(View.GONE);
                        mIceCover.setVisibility(View.GONE);

                        if (MyApplication.registeredTower != null) {
                            tvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
                        }
                        break;
                    // 红外测温
                    case R.id.rb_infrared_temperature:
                        mZeroCheck.setVisibility(View.GONE);
                        mIceCover.setVisibility(View.GONE);


                        initTemperature();//初始化温度参数
                        if (MyApplication.registeredTower != null) {
                            tvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
                        }

                        break;
                    // 零值检测
                    case R.id.rb_zero_check:
                        mResistanceLayout.setVisibility(View.GONE);
                        mTemperature.setVisibility(View.GONE);
                        mIceCover.setVisibility(View.GONE);

                        mZeroCheck.setVisibility(View.VISIBLE);
                        if (MyApplication.registeredTower != null) {
                            tvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
                        }
                        break;
                    // 覆冰检测
                    case R.id.rb_ice_cover:
                        mResistanceLayout.setVisibility(View.GONE);
                        mTemperature.setVisibility(View.GONE);
                        mZeroCheck.setVisibility(View.GONE);
                        mIceCover.setVisibility(View.VISIBLE);

                        if (MyApplication.registeredTower != null) {
                            tvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
                        }
                        List<SpinnerOption> mIceType = new ArrayList<>();
                        List<ItemDetail> items = ItemDetailDBHelper.getInstance().getItem("覆冰种类");
                        for (int i = 0; i < items.size(); i++) {
                            mIceType.add(new SpinnerOption(items.get(i).getItemDetailsId() + "", items.get(i).getItemsName()));
                        }
                        mIceType.add(0, new SpinnerOption("", "请选择覆冰种类"));
                        ArrayAdapter<SpinnerOption> mAdapter = new ArrayAdapter<SpinnerOption>(mWeakReference.get(), android.R.layout.simple_spinner_dropdown_item, mIceType);
                        mSpIceType.setAdapter(mAdapter);
                        break;
                    default:
                        break;
                }
            }
        });
        mrgCheckType.check(R.id.rb_grounding_resistance);

        mTowerType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_straight_tower:
                        tvTiaoXianCount.setVisibility(View.GONE);
                        etTiaoXianCount.setVisibility(View.GONE);
                        rgTiaoXianType.setVisibility(View.GONE);
                        break;
                    case R.id.rb_tension_tower:
                        tvTiaoXianCount.setVisibility(View.VISIBLE);
                        etTiaoXianCount.setVisibility(View.VISIBLE);
                        rgTiaoXianType.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                mSingleTower = null;
                mMap = null;
                imgSelectTower.setVisibility(View.GONE);
            }
        });
        tvTiaoXianCount.setVisibility(View.GONE);
        etTiaoXianCount.setVisibility(View.GONE);
        rgTiaoXianType.setVisibility(View.GONE);

        infraredTemperature = new InfraredTemperature();


        setGps();

        mTowerSubType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSingleTower = null;
                mMap = null;
                imgSelectTower.setVisibility(View.GONE);
            }
        });


        mRgJoint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mMap = null;
            }
        });


        //        if (MyApplication.registeredTower == null || MyApplication.currentNearestTower.getSysTowerID() != MyApplication.registeredTower.getSysTowerID()) {
        //            return;
        //        }
        if (MyApplication.registeredTower == null) {
            return;
        }
        guideLine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        envTemperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        checkQualified(etBigA);
        checkQualified(etBigB);
        checkQualified(etBigC);
        checkQualified(etSmallA);
        checkQualified(etSmallB);
        checkQualified(etSmallC);
        checkQualified(centerA);
        checkQualified(centerB);
        checkQualified(centerC);
        checkQualified(etDoubleBigUpA);
        checkQualified(etDoubleBigDownA);
        checkQualified(etDoubleSmallUpA);
        checkQualified(etDoubleSmallDownA);
        checkQualified(etDoubleBigUpB);
        checkQualified(etDoubleBigDownbB);
        checkQualified(etDoubleSmallUpB);
        checkQualified(etDoubleSmallDownbB);
        checkQualified(etDoubleBigUpC);
        checkQualified(etDoubleBigDownbC);
        checkQualified(etDoubleSmallUpC);
        checkQualified(etDoubleSmallDownbC);

        // 中间接头，单双导线输入值监听
        checkQualified(etMiddleSingleA);
        checkQualified(etMiddleSingleB);
        checkQualified(etMiddleSingleC);
        checkQualified(etMiddleDoubleDownA);
        checkQualified(etMiddleDoubleUpA);
        checkQualified(etMiddleDoubleDownB);
        checkQualified(etMiddleDoubleUpB);
        checkQualified(etMiddleDoubleDownC);
        checkQualified(etMiddleDoubleUpC);

        resetZero();
    }

    private void setCheck() {
        calculate(etBigA);
        calculate(etBigB);
        calculate(etBigC);
        calculate(etSmallA);
        calculate(etSmallB);
        calculate(etSmallC);
        calculate(centerA);
        calculate(centerB);
        calculate(centerC);
        calculate(etDoubleBigUpA);
        calculate(etDoubleBigDownA);
        calculate(etDoubleSmallUpA);
        calculate(etDoubleSmallDownA);
        calculate(etDoubleBigUpB);
        calculate(etDoubleBigDownbB);
        calculate(etDoubleSmallUpB);
        calculate(etDoubleSmallDownbB);
        calculate(etDoubleBigUpC);
        calculate(etDoubleBigDownbC);
        calculate(etDoubleSmallUpC);
        calculate(etDoubleSmallDownbC);
    }

    private void setGps() {
        centerA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppConstant.CURRENT_LOCATION != null) {
                    LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
                    //                    infraredTemperature.setJointALatitude(gps.latitude);
                    //                    infraredTemperature.setJointALongitude(gps.longitude);
                }
            }
        });

        centerB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppConstant.CURRENT_LOCATION != null) {
                    LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
                    //                    infraredTemperature.setJointBLatitude(gps.latitude);
                    //                    infraredTemperature.setJointBLongitude(gps.longitude);
                }
            }
        });

        centerC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppConstant.CURRENT_LOCATION != null) {
                    LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
                    //                    infraredTemperature.setJointCLatitude(gps.latitude);
                    //                    infraredTemperature.setJointCLongitude(gps.longitude);
                }
            }
        });
    }


    @Override
    protected void initData() {
    }


    @OnClick({R.id.btn_submit})
    public void doClick(View v) {
        //        if (MyApplication.registeredTower == null || MyApplication.currentNearestTower.getSysTowerID() != MyApplication.registeredTower.getSysTowerID()) {
        //            ToastUtil.show(mWeakReference.get().getString(R.string.tip_far_awayfrom_tower));
        //            return;
        //        }
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(mWeakReference.get().getString(R.string.tip_far_awayfrom_tower));
            return;
        }
        switch (mrgCheckType.getCheckedRadioButtonId()) {
            case R.id.rb_grounding_resistance:
                // 提交接地电阻数据
                submitResistance();
                break;
            case R.id.rb_infrared_temperature:
                // 提交红外测温数据
                submitTemperature();
                break;
            case R.id.rb_zero_check:
                // 提交零值检测数据
                submitZeroCheck();
                break;
            case R.id.rb_ice_cover:
                // 提交覆冰检测数据
                submitIceCover();
                break;
            default:
                break;
        }
    }


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

    /**
     * 提交红外测温数据
     */
    private void submitTemperature() {
        List<OptTensilePointTemperature> list = new ArrayList<>();
        if (MyApplication.registeredTower != null) {
            //温度
            if (StringUtils.isEmptyOrNull(guideLine.getText().toString()) || checkPoint(guideLine)) {
                ToastUtil.show("请输入导线温度");
                return;
            } else {
                infraredTemperature.setWireway(StringUtils.getValueFromEditText(guideLine));
            }
            //环境温度
            if (StringUtils.isEmptyOrNull(envTemperature.getText().toString()) || checkPoint(envTemperature)) {
                ToastUtil.show("请输入环境温度");
                return;
            } else {
                infraredTemperature.setEnvTemperature(StringUtils.getValueFromEditText(envTemperature));
            }
            //接头类型
            infraredTemperature.setJointType(mJointType); //0-耐张接头，1-中间接头
            //导线类型
            infraredTemperature.setWirewayType(mCurrentWirewayType); //0-单导线，1-双导线
            //位置，没一个输入框都可以添加，中间接口必须添加
            LatLngInfo gps = null;
            //地址
            if (AppConstant.CURRENT_LOCATION != null) {
                gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude,
                        AppConstant.CURRENT_LOCATION.longitude);
            }
            if (mPartSelector.getCheckedRadioButtonId() == R.id.rb_naizhang) { // 耐张接头
                if (mRgJoint.getCheckedRadioButtonId() == R.id.rb_single_line) { // 单导线
                    if (StringUtils.isEmptyOrNull(etBigA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etSmallA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etBigB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etSmallB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etBigC.getText().toString()) &
                            StringUtils.isEmptyOrNull(etSmallC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(etBigA) || checkPoint(etSmallA) || checkPoint(etBigB) ||
                            checkPoint(etSmallB) || checkPoint(etBigC) || checkPoint(etSmallC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {//tensileABig1
                        //耐张 单导线 输入框值
                        Double[] values = new Double[]{StringUtils.getValueFromEText(etBigA), StringUtils.getValueFromEText(etSmallA)
                                , StringUtils.getValueFromEText(etBigB), StringUtils.getValueFromEText(etSmallB)
                                , StringUtils.getValueFromEText(etBigC), StringUtils.getValueFromEText(etSmallC)};
                        //对输入框的封装
                        //                        ArrayList<OptTensilePointTemperature> list = new ArrayList<>();
                        list.clear();
                        for (int i = 0; i < values.length; i++) {
                            if (values[i] == null) {
                                continue;
                            }
                            OptTensilePointTemperature temperature = new OptTensilePointTemperature();
                            //判断 A B C 相  0-A相，1-B相，2-C相
                            if (i > 3) {
                                temperature.setPhase("" + 2);
                            } else if (i > 1) {
                                temperature.setPhase("" + 1);
                            } else {
                                temperature.setPhase("" + 0);
                            }
                            //判断大小号
                            if (i % 2 == 0) {
                                temperature.setSideBar("" + 2);
                            } else {
                                temperature.setSideBar("" + 1);
                            }
                            //单导线全部是0
                            temperature.setBiasPoint(0);
                            //设置值
                            temperature.setTemperature(values[i]);
                            //添加到集合
                            list.add(temperature);
                        }

                    }
                } else { // 双导线
                    if (StringUtils.isEmptyOrNull(etDoubleBigUpA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleBigDownA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleSmallUpA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleSmallDownA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleBigUpB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleBigDownbB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleSmallUpB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleSmallDownbB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleBigUpC.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleBigDownbC.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleSmallUpC.getText().toString()) &
                            StringUtils.isEmptyOrNull(etDoubleSmallDownbC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(etDoubleBigUpA) || checkPoint(etDoubleBigDownA) || checkPoint(etDoubleSmallUpA) ||
                            checkPoint(etDoubleSmallDownA) || checkPoint(etDoubleBigUpB) || checkPoint(etDoubleBigDownbB) ||
                            checkPoint(etDoubleSmallUpB) || checkPoint(etDoubleSmallDownbB) || checkPoint(etDoubleBigUpC) ||
                            checkPoint(etDoubleBigDownbC) || checkPoint(etDoubleSmallUpC) || checkPoint(etDoubleSmallDownbC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {
                        //耐张 双导线 输入框值
                        Double[] values = new Double[]{
                                StringUtils.getValueFromEText(etDoubleBigUpA),
                                StringUtils.getValueFromEText(etDoubleBigDownA),
                                StringUtils.getValueFromEText(etDoubleSmallUpA),
                                StringUtils.getValueFromEText(etDoubleSmallDownA),
                                StringUtils.getValueFromEText(etDoubleBigUpB),
                                StringUtils.getValueFromEText(etDoubleBigDownbB),
                                StringUtils.getValueFromEText(etDoubleSmallUpB),
                                StringUtils.getValueFromEText(etDoubleSmallDownbB),
                                StringUtils.getValueFromEText(etDoubleBigUpC),
                                StringUtils.getValueFromEText(etDoubleBigDownbC),
                                StringUtils.getValueFromEText(etDoubleSmallUpC),
                                StringUtils.getValueFromEText(etDoubleSmallDownbC)};
                        //对输入框的封装
                        list.clear();
                        for (int i = 0; i < values.length; i++) {
                            if (values[i] == null) {
                                continue;
                            }
                            OptTensilePointTemperature temperature = new OptTensilePointTemperature();
                            //判断 A B C 相  0-A相，1-B相，2-C相
                            if (i > 7) {
                                temperature.setPhase("" + 2);
                            } else if (i > 3) {
                                temperature.setPhase("" + 1);
                            } else {
                                temperature.setPhase("" + 0);
                            }
                            //判断大小号 //0-悬垂,1-小号侧，2-大号侧  中间接头都是悬垂
                            if (i == 0 || i == 1 || i == 4 || i == 5 || i == 8 || i == 9) {
                                temperature.setSideBar("" + 2);
                            } else {
                                temperature.setSideBar("" + 1);
                            }
                            //判断上下 0,1,2，3，4。赣西1代表上，2代表下，单导线全部是0
                            if (i % 2 == 0) {
                                temperature.setBiasPoint(1);
                            } else {
                                temperature.setBiasPoint(2);
                            }
                            //地址
                            if (gps == null) {
                                temperature.setLatitude("");
                                temperature.setLongitude("");
                            } else {
                                //中间接头必填 经纬度
                                temperature.setLatitude(gps.latitude + "");
                                temperature.setLongitude(gps.longitude + "");
                            }
                            //设置值
                            temperature.setTemperature(values[i]);
                            //添加到集合
                            list.add(temperature);
                        }
                    }
                }
            } else { // 中间接头
                if (rgMiddleJoint.getCheckedRadioButtonId() == R.id.rb_middle_single_line) { // 单导线
                    if (StringUtils.isEmptyOrNull(etMiddleSingleA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etMiddleSingleB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etMiddleSingleC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(etMiddleSingleA) || checkPoint(etMiddleSingleB) || checkPoint(etMiddleSingleC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {
                        //中间接头 单导线 输入框值
                        Double[] values = new Double[]{
                                StringUtils.getValueFromEText(etMiddleSingleA),
                                StringUtils.getValueFromEText(etMiddleSingleB)
                                , StringUtils.getValueFromEText(etMiddleSingleC)};
                        //对输入框的封装
                        list.clear();
                        for (int i = 0; i < values.length; i++) {
                            if (values[i] == null) {
                                continue;
                            }
                            OptTensilePointTemperature temperature = new OptTensilePointTemperature();
                            //判断 A B C 相  0-A相，1-B相，2-C相
                            if (i > 1) {
                                temperature.setPhase("" + 2);
                            } else if (i > 0) {
                                temperature.setPhase("" + 1);
                            } else {
                                temperature.setPhase("" + 0);
                            }
                            //判断大小号 //0-悬垂,1-小号侧，2-大号侧  中间接头都是悬垂
                            temperature.setSideBar("" + 0);
                            //判断上下 0,1,2，3，4。赣西1代表上，2代表下，单导线全部是0
                            temperature.setBiasPoint(0);
                            if (gps == null) {
                                temperature.setLatitude("");
                                temperature.setLongitude("");
                            } else {
                                //中间接头必填 经纬度
                                temperature.setLatitude(gps.latitude + "");
                                temperature.setLongitude(gps.longitude + "");
                            }
                            //设置值
                            temperature.setTemperature(values[i]);
                            //添加到集合
                            list.add(temperature);
                        }
                    }
                } else { // 双导线
                    if (StringUtils.isEmptyOrNull(etMiddleDoubleUpA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etMiddleDoubleDownA.getText().toString()) &
                            StringUtils.isEmptyOrNull(etMiddleDoubleUpB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etMiddleDoubleDownB.getText().toString()) &
                            StringUtils.isEmptyOrNull(etMiddleDoubleUpC.getText().toString()) &
                            StringUtils.isEmptyOrNull(etMiddleDoubleDownC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(etMiddleDoubleUpA) || checkPoint(etMiddleDoubleDownA) || checkPoint(etMiddleDoubleUpB) ||
                            checkPoint(etMiddleDoubleDownB) || checkPoint(etMiddleDoubleUpC) || checkPoint(etMiddleDoubleDownC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {
                        //中间接头 双导线 输入框值
                        Double[] values = new Double[]{
                                StringUtils.getValueFromEText(etMiddleDoubleUpA),
                                StringUtils.getValueFromEText(etMiddleDoubleDownA),
                                StringUtils.getValueFromEText(etMiddleDoubleUpB),
                                StringUtils.getValueFromEText(etMiddleDoubleDownB),
                                StringUtils.getValueFromEText(etMiddleDoubleUpC),
                                StringUtils.getValueFromEText(etMiddleDoubleDownC)};
                        //对输入框的封装
                        list.clear();
                        for (int i = 0; i < values.length; i++) {
                            if (values[i] == null) {
                                continue;
                            }
                            OptTensilePointTemperature temperature = new OptTensilePointTemperature();
                            //判断 A B C 相  0-A相，1-B相，2-C相
                            if (i > 3) {
                                temperature.setPhase("" + 2);
                            } else if (i > 1) {
                                temperature.setPhase("" + 1);
                            } else {
                                temperature.setPhase("" + 0);
                            }
                            //判断大小号 //0-悬垂,1-小号侧，2-大号侧  中间接头都是悬垂
                            temperature.setSideBar("" + 0);
                            //判断上下 0,1,2，3，4。赣西1代表上，2代表下，单导线全部是0
                            if (i % 2 == 0) {
                                temperature.setBiasPoint(1);
                            } else {
                                temperature.setBiasPoint(2);
                            }
                            //地址
                            if (gps == null) {
                                temperature.setLatitude("");
                                temperature.setLongitude("");
                            } else {
                                //中间接头必填 经纬度
                                temperature.setLatitude(gps.latitude + "");
                                temperature.setLongitude(gps.longitude + "");
                            }
                            //设置值
                            temperature.setTemperature(values[i]);
                            //添加到集合
                            list.add(temperature);
                        }
                    }
                }
            }

            infraredTemperature.setSysUserId(AppConstant.currentUser.getUserId());
            infraredTemperature.setCreateDate(DateUtil.format(new Date()));
            infraredTemperature.setSysTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
            //当接头类型为中间接头是必填
            /*if (mPartSelector.getCheckedRadioButtonId() == R.id.rb_middle) {

            }*/
            TowerDistanceUtils utils = new TowerDistanceUtils();
            Tower secondNearTower = utils.getSecondNearTower(MyApplication.registeredTower);
            if (secondNearTower != null) {
                infraredTemperature.setNearTowerId(secondNearTower.getSysTowerID().intValue());
            }
            //设置数据
            infraredTemperature.setTensilePointTemperatureList(list);
            if (MyApplication.gridlineTaskStatus == 2) {
                if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                    infraredTemperature.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
                } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                    infraredTemperature.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
                }
            } else if (MyApplication.gridlineTaskStatus == 3) {
                infraredTemperature.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
            }
            //1、插入数据到数据库
            InfraredTemperatureDBHelper.getInstance().insert(infraredTemperature, list);
            postToNet(infraredTemperature);
        }
    }

    private void checkQualified(final EditText edText) {
        edText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate(edText);
            }
        });
    }

    private void calculate(EditText edText) {

        if (StringUtils.isEmptyOrNull(edText.getText().toString())
                || StringUtils.isEmptyOrNull(guideLine.getText().toString())
                || StringUtils.isEmptyOrNull(envTemperature.getText().toString())) {
            edText.setBackgroundResource(R.drawable.shape_et_bg);
            return;
        }
        if (checkPoint(guideLine) || checkPoint(envTemperature) || checkPoint(edText)) {
            edText.setBackgroundResource(R.drawable.shape_et_bg);
            return;
        }

        double guideLineTemp = Double.valueOf(guideLine.getText().toString());
        double environmentTemp = Double.valueOf(envTemperature.getText().toString());
        double edTemp = Double.valueOf(edText.getText().toString());

        if (edTemp >= 70.0) {
            //如果超过70  直接不合格 不用进行计算
            edText.setBackgroundResource(R.drawable.et_unqualified);
            return;
        }
        double delteTemp = edTemp - environmentTemp;
        if (delteTemp != 0) {
            delteTemp = 100 * (edTemp - guideLineTemp) / (delteTemp);
        }

        if (delteTemp < 35 || edTemp < 50) {
            edText.setBackgroundResource(R.drawable.et_qualified);
        } else if (delteTemp < 80 && edTemp > 50) {
            edText.setBackgroundResource(R.drawable.et_unqualified);
        } else if (delteTemp < 95 && edTemp > 70) {
            edText.setBackgroundResource(R.drawable.et_unqualified);
        } else if (edTemp > 90) {
            edText.setBackgroundResource(R.drawable.et_unqualified);
        } else {
            edText.setBackgroundResource(R.drawable.et_unqualified);
        }
    }

    private void postToNet(final InfraredTemperature infraredTemperature) {
        mProgressDialog.show();
        Gson gson = new Gson();
        //        List<InfraredTemperature> list = new ArrayList<>();
        //        list.add(infraredTemperature);
        String jsonStr = gson.toJson(infraredTemperature);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        call0 = RetrofitManager.getInstance().getService(ApiService.class).postInfraredTemperature(body);
        call0.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                mProgressDialog.dismiss();
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    infraredTemperature.setUploadFlag(1);
                    InfraredTemperatureDBHelper.getInstance().updateInfraredTemperature(infraredTemperature);
                    ToastUtil.show("提交成功");
                } else {
                    showRePostDialog(infraredTemperature);
                }
                resetInfraredTemperature();
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                mProgressDialog.dismiss();
                showRePostDialog(infraredTemperature);
                resetInfraredTemperature();
            }
        });
    }

    private void showRePostDialog(final InfraredTemperature infraredTemperature) {
        DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postToNet(infraredTemperature);
            }
        });
    }

    public boolean checkPoint(EditText editText) {
        if (editText.getText().toString().equals(".") || editText.getText().toString().equals("-") || editText.getText().toString().equals("-.")) {
            return true;
        }
        return false;
    }

    /**
     * 提交接地电阻数据
     */
    private void submitResistance() {
        if (MyApplication.registeredTower != null) {
            if ((TextUtils.isEmpty(mResistanceA.getText()) && TextUtils.isEmpty(mResistanceB.getText()) &&
                    TextUtils.isEmpty(mResistanceC.getText()) && TextUtils.isEmpty(mResistanceD.getText())) ||
                    (checkPoint(mResistanceA) || checkPoint(mResistanceB) || checkPoint(mResistanceC) || checkPoint(mResistanceD))
                    ) {
                ToastUtil.show("至少填入一个数据！");
                return;
            }
            final EarthingResistance earthingResistance = createResistance();
            EarthingResistanceDBHelper.getInstance().insert(earthingResistance);
            postEarthNet(earthingResistance);
        }
    }

    private void postEarthNet(final EarthingResistance earthingResistance) {
        mProgressDialog.show();
        Gson gson = new Gson();
        List<EarthingResistance> list = new ArrayList<>();
        list.add(earthingResistance);
        String jsonStr = gson.toJson(list);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
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

    private void resetInfraredTemperature() {
        infraredTemperature = new InfraredTemperature();
        guideLine.setText("");
        envTemperature.setText("");
        etBigA.setText("");
        etBigB.setText("");
        etBigC.setText("");
        etSmallA.setText("");
        etSmallB.setText("");
        etSmallC.setText("");
        etDoubleBigUpA.setText("");
        etDoubleBigUpB.setText("");
        etDoubleBigUpC.setText("");
        etDoubleBigDownA.setText("");
        etDoubleBigDownbB.setText("");
        etDoubleBigDownbC.setText("");
        etDoubleSmallUpA.setText("");
        etDoubleSmallUpB.setText("");
        etDoubleSmallUpC.setText("");
        etDoubleSmallDownA.setText("");
        etDoubleSmallDownbB.setText("");
        etDoubleSmallDownbC.setText("");
        centerA.setText("");
        centerB.setText("");
        centerC.setText("");

        // 清空中间接头的控件数据
        // 单导线的3相输入值
        etMiddleSingleA.setText("");
        etMiddleSingleB.setText("");
        etMiddleSingleC.setText("");
        // 双导线的3相输入值
        etMiddleDoubleUpA.setText("");
        etMiddleDoubleDownA.setText("");
        etMiddleDoubleUpB.setText("");
        etMiddleDoubleDownB.setText("");
        etMiddleDoubleUpC.setText("");
        etMiddleDoubleDownC.setText("");
    }

    private void resetEarth() {
        mResistanceA.setText("");
        mResistanceB.setText("");
        mResistanceC.setText("");
        mResistanceD.setText("");
    }

    private void resetIceCover() {
        mEtIceCover.setText("");
        mEtIceTemp.setText("");
        mEtIceHumidity.setText("");
        mSpIceType.setSelection(0);
    }

    private EarthingResistance createResistance() {
        EarthingResistance earthingResistance = new EarthingResistance();
        earthingResistance.setTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
        earthingResistance.setSysUserId(AppConstant.currentUser.getUserId());
        earthingResistance.setUploadFlag(0);
        earthingResistance.setCreateDate(DateUtil.format(new Date()));
        earthingResistance.setSysUserId(AppConstant.currentUser.getUserId());
        if (!(TextUtils.isEmpty(mResistanceA.getText()) || checkPoint(mResistanceA))) {
            earthingResistance.setResistanceA(Double.parseDouble(mResistanceA.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mResistanceB.getText()) || checkPoint(mResistanceB))) {
            earthingResistance.setResistanceB(Double.parseDouble(mResistanceB.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mResistanceC.getText()) || checkPoint(mResistanceC))) {
            earthingResistance.setResistanceC(Double.parseDouble(mResistanceC.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mResistanceD.getText()) || checkPoint(mResistanceD))) {
            earthingResistance.setResistanceD(Double.parseDouble(mResistanceD.getText().toString()));
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                earthingResistance.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                earthingResistance.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            earthingResistance.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        return earthingResistance;
    }

    private IceCover createIceCover() {
        IceCover iceCover = new IceCover();
        iceCover.setTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
        iceCover.setSysUserId(AppConstant.currentUser.getUserId());
        iceCover.setUploadFlag(0);
        iceCover.setCreateDate(DateUtil.format(new Date()));
        iceCover.setSysUserId(AppConstant.currentUser.getUserId());
        if (!(TextUtils.isEmpty(mEtIceCover.getText()) || checkPoint(mEtIceCover))) {
            iceCover.setIceCoverHeight(Double.parseDouble(mEtIceCover.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mEtIceTemp.getText()) || checkPoint(mEtIceTemp))) {
            iceCover.setTemperature(Double.parseDouble(mEtIceTemp.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mEtIceHumidity.getText()) || checkPoint(mEtIceHumidity))) {
            iceCover.setWetness(Double.parseDouble(mEtIceHumidity.getText().toString()));
        }
        iceCover.setIceType(((SpinnerOption) mSpIceType.getAdapter().getItem(mSpIceType.getSelectedItemPosition())).getValue());
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                iceCover.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                iceCover.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            iceCover.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        return iceCover;
    }

    //<editor-fold desc="初始化温度参数">

    /**
     * 初始化温度参数
     */
    private void initTemperature() {
        mResistanceLayout.setVisibility(View.GONE);
        mTemperature.setVisibility(View.VISIBLE);
        //mZeroCheck.setVisibility(View.GONE);

        mPartSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    // 耐张接头
                    case R.id.rb_naizhang:
                        if (MyApplication.registeredTower != null) {
                            tvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
                        }
                        layoutNaizhangTempture.setVisibility(View.VISIBLE);
                        layoutMiddleTempture.setVisibility(View.GONE);

                        mJointType = 0;
                        break;
                    // 中间接头
                    case R.id.rb_middle:
                        if (MyApplication.registeredTower != null && MyApplication.nearSecondTower != null) {
                            tvTowerNo.setText(MyApplication.registeredTower.getTowerNo() + " - " + MyApplication.nearSecondTower.getTowerNo());
                        }
                        layoutNaizhangTempture.setVisibility(View.GONE);
                        layoutMiddleTempture.setVisibility(View.VISIBLE);
                        // 清空输入框里面的数据
                        mJointType = 1;
                        break;
                    default:
                        break;
                }

            }
        });
        mPartSelector.check(R.id.rb_naizhang);//默认选中耐张塔

        // 耐张接头添加选择监听
        mRgJoint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_single_line:
                        mSingleLineLayout.setVisibility(View.VISIBLE);
                        mDoubleLineLayout.setVisibility(View.GONE);

                        currentLoopType = SINGLE_LOOP_TYPE;
                        mCurrentWirewayType = SINGLE_WIRE_TYPE;
                        break;
                    case R.id.rb_double_line:
                        mSingleLineLayout.setVisibility(View.GONE);
                        mDoubleLineLayout.setVisibility(View.VISIBLE);

                        currentLoopType = DOUBLE_LOOP_TYPE;
                        mCurrentWirewayType = DOUBLE_WIRE_TYPE;
                        break;
                    default:
                        break;
                }

            }
        });
        mRgJoint.check(R.id.rb_single_line);

        // 中间接头添加选择监听
        rgMiddleJoint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_middle_single_line:
                        layoutMiddleSingleLine.setVisibility(View.VISIBLE);
                        layoutMiddleDoubleLine.setVisibility(View.GONE);

                        mCurrentLoopTypeForMiddle = SINGLE_LOOP_TYPE;
                        break;
                    case R.id.rb_middle_double_line:
                        layoutMiddleSingleLine.setVisibility(View.GONE);
                        layoutMiddleDoubleLine.setVisibility(View.VISIBLE);

                        mCurrentLoopTypeForMiddle = DOUBLE_LOOP_TYPE;
                        break;
                    default:
                        break;
                }

            }
        });
        rgMiddleJoint.check(R.id.rb_middle_single_line);
    }
    //</editor-fold>

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }


    private void startSelectTower() {
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(mWeakReference.get().getString(R.string.tip_far_awayfrom_tower));
            return;
        }

        if (etJueYuanCount.getText().toString().equals("0")) {
            ToastUtil.show("绝缘子片数不能为0");
            return;
        }
        if (etTiaoXianCount.getText().toString().equals("0")) {
            ToastUtil.show("绝缘子片数不能为0");
            return;
        }

        if (etJueYuanCount.getText().toString().contains(".")) {
            ToastUtil.show("绝缘子片数不合法");
            return;
        }
        if (etJueYuanCount.getText().toString().contains(".")) {
            ToastUtil.show("绝缘子片数不合法");
            return;
        }

        //数值太大会造成崩溃
        if (etJueYuanCount.getText().toString().length() > 2) {
            ToastUtil.show("绝缘子片数最大不超过20");
            return;
        }
        if (etTiaoXianCount.getText().toString().length() > 2) {
            ToastUtil.show("绝缘子片数最大不超过20");
            return;
        }

        if (StringUtils.isEmptyOrNull(etJueYuanCount.getText().toString()) || Integer.valueOf(etJueYuanCount.getText().toString()) > 20) {
            ToastUtil.show("绝缘子片数不能为空或者最大不超过20");
            return;
        }

        if (StringUtils.isEmptyOrNull(etTiaoXianCount.getText().toString()) || Integer.valueOf(etTiaoXianCount.getText().toString()) > 20) {
            ToastUtil.show("跳线片数不能为空或者最大不超过20");
            return;
        }


        jumpCount = Integer.valueOf(etTiaoXianCount.getText().toString());
        insulatorCount = Integer.valueOf(etJueYuanCount.getText().toString());

        Intent intent = new Intent(mWeakReference.get(), SelectTowerActivity.class);
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


    private int getTowerType() {
        int result = -1;
        switch (mTowerType.getCheckedRadioButtonId()) {
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

    private int getTowerSubType() {
        int result = -1;
        switch (mTowerSubType.getCheckedRadioButtonId()) {
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

    private int getJumpType() {
        int result = -1;
        switch (rgTiaoXianType.getCheckedRadioButtonId()) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SelectTowerEvent event) {
        imgSelectTower.setVisibility(View.VISIBLE);
        imgSelectTower.setImageBitmap(event.getBitmap());
        switch (mTowerType.getCheckedRadioButtonId()) {
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

    /**
     * 提交零值检测数据
     */
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
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

    //<editor-fold desc="提交覆冰检测数据">

    /**
     * 提交覆冰检测数据
     */
    private void submitIceCover() {
        if (MyApplication.registeredTower != null) {
            if (TextUtils.isEmpty(mEtIceCover.getText()) || checkPoint(mEtIceCover)) {
                ToastUtil.show("请输入覆冰厚度！");
                return;
            }
            if (TextUtils.isEmpty(mEtIceHumidity.getText()) || checkPoint(mEtIceHumidity)) {
                ToastUtil.show("请输入湿度");
                return;
            }
            if (TextUtils.isEmpty(mEtIceTemp.getText()) || checkPoint(mEtIceTemp)) {
                ToastUtil.show("请输入温度");
                return;
            }
            if (mSpIceType.getSelectedItemPosition() == 0) {
                ToastUtil.show("请选择覆冰种类");
                return;
            }
            final IceCover iceCover = createIceCover();
            IceCoverDBHelper.getInstance().insert(iceCover);
            postIceNet(iceCover);
        }
    }
    //</editor-fold>

    private void postIceNet(final IceCover iceCover) {
        mProgressDialog.show();
        Gson gson = new Gson();
        List<IceCover> list = new ArrayList<>();
        list.add(iceCover);
        String jsonStr = gson.toJson(list);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        call3 = RetrofitManager.getInstance().getService(ApiService.class)
                .postOptIceCover(body);
        call3.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mProgressDialog.dismiss();
                BaseCallBack<String> body = response.body();
                if (body != null && body.getCode() == 1) {
                    iceCover.setUploadFlag(1);
                    IceCoverDBHelper.getInstance().updateById(iceCover);
                    ToastUtil.show("提交成功");
                } else {
                    DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postIceNet(iceCover);
                        }
                    });
                }
                resetIceCover();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                //                ToastUtil.show("提交成功");
                mProgressDialog.dismiss();
                DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postIceNet(iceCover);
                    }
                });
                resetIceCover();
            }
        });
    }


    private ZeroDetection initZeroCheck() {
        ZeroDetection zeroDetection = new ZeroDetection();
        switch (mTowerType.getCheckedRadioButtonId()) {
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
        } else if (MyApplication.gridlineTaskStatus == 3) {
            zeroDetection.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        return zeroDetection;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call0 != null && !call0.isCanceled()) {
            call0.cancel();
        }
        if (call1 != null && !call1.isCanceled()) {
            call1.cancel();
        }
        if (call2 != null && !call2.isCanceled()) {
            call2.cancel();
        }
        if (call3 != null && !call3.isCanceled()) {
            call3.cancel();
        }
    }

    public void resetZero() {
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

        etTiaoXianCount.setText(jumpCount + "");
        etJueYuanCount.setText(insulatorCount + "");

        imgSelectTower.setVisibility(View.GONE);
        mMap = null;
        mSingleTower = null;
    }


}
