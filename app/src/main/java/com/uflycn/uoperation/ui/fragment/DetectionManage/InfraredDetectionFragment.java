package com.uflycn.uoperation.ui.fragment.DetectionManage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.InfraredTemperature;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.OptTensilePointTemperature;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.InfraredTemperatureDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.TowerDistanceUtils;
import com.uflycn.uoperation.util.ViewPagerFragment;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 检测管理——红外检测
 */
public class InfraredDetectionFragment extends ViewPagerFragment {


    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.tv_tower_no)
    TextView mTvTowerNo;
    @BindView(R.id.et_guide_line)
    EditText mEtGuideLine;
    @BindView(R.id.et_environment_temperature)
    EditText mEtEnvironmentTemperature;


    @BindView(R.id.rg_part_selector)
    RadioGroup mRgPartSelector;

    @BindView(R.id.rg_joint)
    RadioGroup mRgJoint;
    @BindView(R.id.et_big_size_slide_a)
    EditText mEtBigSizeSlideA;
    @BindView(R.id.et_small_size_slide_a)
    EditText mEtSmallSizeSlideA;
    @BindView(R.id.et_big_size_slide_b)
    EditText mEtBigSizeSlideB;
    @BindView(R.id.et_small_size_slide_b)
    EditText mEtSmallSizeSlideB;
    @BindView(R.id.et_big_size_slide_c)
    EditText mEtBigSizeSlideC;
    @BindView(R.id.et_small_size_slide_c)
    EditText mEtSmallSizeSlideC;
    @BindView(R.id.layout_single_line)
    LinearLayout mLayoutSingleLine;
    @BindView(R.id.et_double_big_size_slide_up_a)
    EditText mEtDoubleBigSizeSlideUpA;
    @BindView(R.id.et_double_big_size_slide_down_a)
    EditText mEtDoubleBigSizeSlideDownA;
    @BindView(R.id.et_double_small_size_slide_up_a)
    EditText mEtDoubleSmallSizeSlideUpA;
    @BindView(R.id.et_double_small_size_slide_down_a)
    EditText mEtDoubleSmallSizeSlideDownA;
    @BindView(R.id.et_double_big_size_slide_up_b)
    EditText mEtDoubleBigSizeSlideUpB;
    @BindView(R.id.et_double_big_size_slide_down_b)
    EditText mEtDoubleBigSizeSlideDownB;
    @BindView(R.id.et_double_small_size_slide_up_b)
    EditText mEtDoubleSmallSizeSlideUpB;
    @BindView(R.id.et_double_small_size_slide_down_b)
    EditText mEtDoubleSmallSizeSlideDownB;
    @BindView(R.id.et_double_big_size_slide_up_c)
    EditText mEtDoubleBigSizeSlideUpC;
    @BindView(R.id.et_double_big_size_slide_down_c)
    EditText mEtDoubleBigSizeSlideDownC;
    @BindView(R.id.et_double_small_size_slide_up_c)
    EditText mEtDoubleSmallSizeSlideUpC;
    @BindView(R.id.et_double_small_size_slide_down_c)
    EditText mEtDoubleSmallSizeSlideDownC;
    @BindView(R.id.layout_double_line)
    LinearLayout mLayoutDoubleLine;
    @BindView(R.id.layout_naizhang)
    LinearLayout mLayoutNaizhang;
    @BindView(R.id.layout_middle)
    LinearLayout mLayoutMiddle;

    @BindView(R.id.rb_middle_single_line)
    RadioButton mRbMiddleSingleLine;
    @BindView(R.id.rb_middle_double_line)
    RadioButton mRbMiddleDoubleLine;
    @BindView(R.id.rg_middle_joint)
    RadioGroup mRgMiddleJoint;
    @BindView(R.id.et_middle_single_a)
    EditText mEtMiddleSingleA;
    @BindView(R.id.et_middle_single_b)
    EditText mEtMiddleSingleB;
    @BindView(R.id.et_middle_single_c)
    EditText mEtMiddleSingleC;
    @BindView(R.id.layout_middle_single_line)
    LinearLayout mLayoutMiddleSingleLine;
    @BindView(R.id.et_middle_double_up_a)
    EditText mEtMiddleDoubleUpA;
    @BindView(R.id.et_middle_double_down_a)
    EditText mEtMiddleDoubleDownA;
    @BindView(R.id.et_middle_double_up_b)
    EditText mEtMiddleDoubleUpB;
    @BindView(R.id.et_middle_double_down_b)
    EditText mEtMiddleDoubleDownB;
    @BindView(R.id.et_middle_double_up_c)
    EditText mEtMiddleDoubleUpC;
    @BindView(R.id.et_middle_double_down_c)
    EditText mEtMiddleDoubleDownC;
    @BindView(R.id.layout_middle_double_line)
    LinearLayout mLayoutMiddleDoubleLine;
    @BindView(R.id.et_center_a)
    EditText mEtCenterA;
    @BindView(R.id.et_center_b)
    EditText mEtCenterB;
    @BindView(R.id.et_center_c)
    EditText mEtCenterC;
    @BindView(R.id.layout_middle_temp)
    LinearLayout mLayoutMiddleTemp;
    @BindView(R.id.layout_infrared_temperature)
    LinearLayout mLayoutInfraredTemperature;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.rb_naizhang)
    RadioButton rbNaizhang;
    @BindView(R.id.rb_middle)
    RadioButton rbMiddle;
    @BindView(R.id.tv_wire_type)
    TextView tvWireType;
    @BindView(R.id.rb_single_line)
    RadioButton rbSingleLine;
    @BindView(R.id.rb_double_line)
    RadioButton rbDoubleLine;
    @BindView(R.id.tv_line_first)
    TextView tvLineFirst;
    @BindView(R.id.tv_line_second)
    TextView tvLineSecond;
    @BindView(R.id.tv_line_third)
    TextView tvLineThird;
    Unbinder unbinder;
    @BindView(R.id.tv_ver_first)
    TextView tvVerFirst;
    @BindView(R.id.tv_ver_second)
    TextView tvVerSecond;
    @BindView(R.id.tv_ver_third)
    TextView tvVerThird;
    @BindView(R.id.tv_middle_first)
    TextView tvMiddleFirst;
    @BindView(R.id.tv_middle_second)
    TextView tvMiddleSecond;
    @BindView(R.id.tv_middle_third)
    TextView tvMiddleThird;
    @BindView(R.id.tv_middle_double_first)
    TextView tvMiddleDoubleFirst;
    @BindView(R.id.tv_middle_double_second)
    TextView tvMiddleDoubleSecond;
    @BindView(R.id.tv_middle_double_third)
    TextView tvMiddleDoubleThird;
    @BindView(R.id.tv_wire_type_line)
    TextView tvWireTypeLine;


    //耐张为0：中间接头为1；
    private int mJointType = 1;
    private String SINGLE_LOOP_TYPE; // 单导线
    private String DOUBLE_LOOP_TYPE; // 双导线

    private String currentLoopType;// 耐张接头导线类型

    private int SINGLE_WIRE_TYPE = 0;// 单导线
    private int DOUBLE_WIRE_TYPE = 1;//双导线
    private int mCurrentWirewayType = SINGLE_WIRE_TYPE;

    private String mCurrentLoopTypeForMiddle; // 中间接头导线类型
    Call<BaseCallBack<List<ListCallBack<String>>>> call0;

    @Override
    public View initView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_infrare_detection, null);

    }

    @Override
    public void initData() {
        if (MyApplication.registeredTower != null) {
            mTvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
            Gridline gridline = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
            mTvLineName.setText(gridline.getLineName());
        }
        //如果是山东则修改选中按钮及下方的名称
        if (ProjectManageUtil.isShanDong()) {
            rbNaizhang.setText("耐张塔");
            rbMiddle.setText("直线塔");
            tvWireType.setText("线路排列形式");
            tvWireTypeLine.setText("线路排列形式");
            rbSingleLine.setText("水平");
            rbDoubleLine.setText("垂直");
            tvLineFirst.setText("左相");
            tvLineSecond.setText("中相");
            tvLineThird.setText("右相");
            tvVerFirst.setText("左相");
            tvVerSecond.setText("中相");
            tvVerThird.setText("右相");
            tvMiddleFirst.setText("左相");
            tvMiddleSecond.setText("中相");
            tvMiddleThird.setText("右相");
            tvMiddleDoubleFirst.setText("左相");
            tvMiddleDoubleSecond.setText("中相");
            tvMiddleDoubleThird.setText("右相");
            mRbMiddleSingleLine.setText("水平");
            mRbMiddleDoubleLine.setText("垂直");
        }
        initTemperature();

    }

    @Override
    public void onDismissDialog() {
        if (call0 != null && !call0.isCanceled()) {
            call0.cancel();
        }
    }

    /**
     * 初始化温度参数
     */
    private void initTemperature() {
        mRgPartSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    // 耐张接头
                    case R.id.rb_naizhang:
                        if (MyApplication.registeredTower != null) {
                            mTvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
                        }
                        mLayoutNaizhang.setVisibility(View.VISIBLE);
                        mLayoutMiddle.setVisibility(View.GONE);

                        mJointType = 0;
                        break;
                    // 中间接头
                    case R.id.rb_middle:
                        if (MyApplication.registeredTower != null && MyApplication.nearSecondTower != null) {
                            mTvTowerNo.setText(MyApplication.registeredTower.getTowerNo() + " - " + MyApplication.nearSecondTower.getTowerNo());
                        }
                        mLayoutNaizhang.setVisibility(View.GONE);
                        mLayoutMiddle.setVisibility(View.VISIBLE);
                        // 清空输入框里面的数据
                        mJointType = 1;
                        break;
                    default:
                        break;
                }

            }
        });
        mRgPartSelector.check(R.id.rb_naizhang);//默认选中耐张塔

        // 耐张接头添加选择监听
        mRgJoint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_single_line:
                        mLayoutSingleLine.setVisibility(View.VISIBLE);
                        mLayoutDoubleLine.setVisibility(View.GONE);

                        currentLoopType = SINGLE_LOOP_TYPE;
                        mCurrentWirewayType = SINGLE_WIRE_TYPE;
                        break;
                    case R.id.rb_double_line:
                        mLayoutSingleLine.setVisibility(View.GONE);
                        mLayoutDoubleLine.setVisibility(View.VISIBLE);

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
        mRgMiddleJoint.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_middle_single_line:
                        mLayoutMiddleSingleLine.setVisibility(View.VISIBLE);
                        mLayoutMiddleDoubleLine.setVisibility(View.GONE);

                        mCurrentLoopTypeForMiddle = SINGLE_LOOP_TYPE;
                        break;
                    case R.id.rb_middle_double_line:
                        mLayoutMiddleSingleLine.setVisibility(View.GONE);
                        mLayoutMiddleDoubleLine.setVisibility(View.VISIBLE);

                        mCurrentLoopTypeForMiddle = DOUBLE_LOOP_TYPE;
                        break;
                    default:
                        break;
                }

            }
        });
        mRgMiddleJoint.check(R.id.rb_middle_single_line);

        mEtGuideLine.addTextChangedListener(new TextWatcher() {
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
        mEtEnvironmentTemperature.addTextChangedListener(new TextWatcher() {
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

        checkQualified(mEtBigSizeSlideA);
        checkQualified(mEtBigSizeSlideB);
        checkQualified(mEtBigSizeSlideC);
        checkQualified(mEtSmallSizeSlideA);
        checkQualified(mEtSmallSizeSlideB);
        checkQualified(mEtSmallSizeSlideC);
        checkQualified(mEtCenterA);
        checkQualified(mEtCenterB);
        checkQualified(mEtCenterC);
        checkQualified(mEtDoubleBigSizeSlideUpA);
        checkQualified(mEtDoubleBigSizeSlideDownA);
        checkQualified(mEtDoubleSmallSizeSlideUpA);
        checkQualified(mEtDoubleSmallSizeSlideDownA);
        checkQualified(mEtDoubleBigSizeSlideUpB);
        checkQualified(mEtDoubleBigSizeSlideDownB);
        checkQualified(mEtDoubleSmallSizeSlideUpB);
        checkQualified(mEtDoubleSmallSizeSlideDownB);
        checkQualified(mEtDoubleBigSizeSlideUpC);
        checkQualified(mEtDoubleBigSizeSlideDownC);
        checkQualified(mEtDoubleSmallSizeSlideUpC);
        checkQualified(mEtDoubleSmallSizeSlideDownC);

        // 中间接头，单双导线输入值监听
        checkQualified(mEtMiddleSingleA);
        checkQualified(mEtMiddleSingleB);
        checkQualified(mEtMiddleSingleC);
        checkQualified(mEtMiddleDoubleUpA);
        checkQualified(mEtMiddleDoubleDownA);
        checkQualified(mEtMiddleDoubleUpB);
        checkQualified(mEtMiddleDoubleDownB);
        checkQualified(mEtMiddleDoubleUpC);
        checkQualified(mEtMiddleDoubleDownC);
    }

    private void setCheck() {
        calculate(mEtBigSizeSlideA);
        calculate(mEtBigSizeSlideB);
        calculate(mEtBigSizeSlideC);
        calculate(mEtSmallSizeSlideA);
        calculate(mEtSmallSizeSlideB);
        calculate(mEtSmallSizeSlideC);
        calculate(mEtCenterA);
        calculate(mEtCenterB);
        calculate(mEtCenterC);
        calculate(mEtDoubleBigSizeSlideUpA);
        calculate(mEtDoubleBigSizeSlideDownA);
        calculate(mEtDoubleSmallSizeSlideUpA);
        calculate(mEtDoubleSmallSizeSlideDownA);
        calculate(mEtDoubleBigSizeSlideUpB);
        calculate(mEtDoubleBigSizeSlideDownB);
        calculate(mEtDoubleSmallSizeSlideUpB);
        calculate(mEtDoubleSmallSizeSlideDownB);
        calculate(mEtDoubleBigSizeSlideUpC);
        calculate(mEtDoubleBigSizeSlideDownC);
        calculate(mEtDoubleSmallSizeSlideUpC);
        calculate(mEtDoubleSmallSizeSlideDownC);
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
                || StringUtils.isEmptyOrNull(mEtGuideLine.getText().toString())
                || StringUtils.isEmptyOrNull(mEtEnvironmentTemperature.getText().toString())) {
            edText.setBackgroundResource(R.drawable.shape_et_bg);
            return;
        }
        if (checkPoint(mEtGuideLine) || checkPoint(mEtEnvironmentTemperature) || checkPoint(edText)) {
            edText.setBackgroundResource(R.drawable.shape_et_bg);
            return;
        }

        double guideLineTemp = Double.valueOf(mEtGuideLine.getText().toString());
        double environmentTemp = Double.valueOf(mEtEnvironmentTemperature.getText().toString());
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


    public boolean checkPoint(EditText editText) {
        if (editText.getText().toString().equals(".") || editText.getText().toString().equals("-") || editText.getText().toString().equals("-.")) {
            return true;
        }
        return false;
    }

    @OnClick({R.id.btn_submit})
    public void doClick(View v) {
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(getContext().getString(R.string.tip_far_awayfrom_tower));
            return;
        }
        submitTemperature();
    }

    private void submitTemperature() {
        List<OptTensilePointTemperature> list = new ArrayList<>();
        InfraredTemperature infraredTemperature = new InfraredTemperature();
        if (MyApplication.registeredTower != null) {
            //温度
            if (StringUtils.isEmptyOrNull(mEtGuideLine.getText().toString()) || checkPoint(mEtGuideLine)) {
                ToastUtil.show("请输入导线温度");
                return;
            } else {
                infraredTemperature.setWireway(StringUtils.getValueFromEditText(mEtGuideLine));
            }
            //环境温度
            if (StringUtils.isEmptyOrNull(mEtEnvironmentTemperature.getText().toString()) || checkPoint(mEtEnvironmentTemperature)) {
                ToastUtil.show("请输入环境温度");
                return;
            } else {
                infraredTemperature.setEnvTemperature(StringUtils.getValueFromEditText(mEtEnvironmentTemperature));
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
            if (mRgPartSelector.getCheckedRadioButtonId() == R.id.rb_naizhang) { // 耐张接头
                if (mRgJoint.getCheckedRadioButtonId() == R.id.rb_single_line) { // 单导线
                    if (StringUtils.isEmptyOrNull(mEtBigSizeSlideA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtSmallSizeSlideA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtBigSizeSlideB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtSmallSizeSlideB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtBigSizeSlideC.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtSmallSizeSlideC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(mEtBigSizeSlideA) || checkPoint(mEtSmallSizeSlideA) || checkPoint(mEtBigSizeSlideB) ||
                            checkPoint(mEtSmallSizeSlideB) || checkPoint(mEtBigSizeSlideC) || checkPoint(mEtSmallSizeSlideC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {//tensileABig1
                        //耐张 单导线 输入框值
                        Double[] values = new Double[]{StringUtils.getValueFromEText(mEtBigSizeSlideA), StringUtils.getValueFromEText(mEtSmallSizeSlideA)
                                , StringUtils.getValueFromEText(mEtBigSizeSlideB), StringUtils.getValueFromEText(mEtSmallSizeSlideB)
                                , StringUtils.getValueFromEText(mEtBigSizeSlideC), StringUtils.getValueFromEText(mEtSmallSizeSlideC)};
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
                    if (StringUtils.isEmptyOrNull(mEtDoubleBigSizeSlideDownA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleBigSizeSlideUpA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleSmallSizeSlideUpA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleSmallSizeSlideDownA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleSmallSizeSlideUpB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleBigSizeSlideDownB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleBigSizeSlideUpB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleSmallSizeSlideDownB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleBigSizeSlideUpC.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleBigSizeSlideDownC.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleSmallSizeSlideUpC.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtDoubleSmallSizeSlideDownC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(mEtDoubleBigSizeSlideDownA) || checkPoint(mEtDoubleBigSizeSlideUpA) || checkPoint(mEtDoubleSmallSizeSlideUpA) ||
                            checkPoint(mEtDoubleSmallSizeSlideDownA) || checkPoint(mEtDoubleSmallSizeSlideUpB) || checkPoint(mEtDoubleBigSizeSlideDownB) ||
                            checkPoint(mEtDoubleBigSizeSlideUpB) || checkPoint(mEtDoubleSmallSizeSlideDownB) || checkPoint(mEtDoubleBigSizeSlideUpC) ||
                            checkPoint(mEtDoubleBigSizeSlideDownC) || checkPoint(mEtDoubleSmallSizeSlideUpC) || checkPoint(mEtDoubleSmallSizeSlideDownC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {
                        //耐张 双导线 输入框值
                        Double[] values = new Double[]{
                                StringUtils.getValueFromEText(mEtDoubleBigSizeSlideDownA),
                                StringUtils.getValueFromEText(mEtDoubleBigSizeSlideUpA),
                                StringUtils.getValueFromEText(mEtDoubleSmallSizeSlideUpA),
                                StringUtils.getValueFromEText(mEtDoubleSmallSizeSlideDownA),
                                StringUtils.getValueFromEText(mEtDoubleSmallSizeSlideUpB),
                                StringUtils.getValueFromEText(mEtDoubleBigSizeSlideDownB),
                                StringUtils.getValueFromEText(mEtDoubleBigSizeSlideUpB),
                                StringUtils.getValueFromEText(mEtDoubleSmallSizeSlideDownB),
                                StringUtils.getValueFromEText(mEtDoubleBigSizeSlideUpC),
                                StringUtils.getValueFromEText(mEtDoubleBigSizeSlideDownC),
                                StringUtils.getValueFromEText(mEtDoubleSmallSizeSlideUpC),
                                StringUtils.getValueFromEText(mEtDoubleSmallSizeSlideDownC)};
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
                if (mRgMiddleJoint.getCheckedRadioButtonId() == R.id.rb_middle_single_line) { // 单导线
                    if (StringUtils.isEmptyOrNull(mEtMiddleSingleA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtMiddleSingleB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtMiddleSingleC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(mEtMiddleSingleA) || checkPoint(mEtMiddleSingleB) || checkPoint(mEtMiddleSingleC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {
                        //中间接头 单导线 输入框值
                        Double[] values = new Double[]{
                                StringUtils.getValueFromEText(mEtMiddleSingleA),
                                StringUtils.getValueFromEText(mEtMiddleSingleB)
                                , StringUtils.getValueFromEText(mEtMiddleSingleC)};
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
                    if (StringUtils.isEmptyOrNull(mEtMiddleDoubleUpA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtMiddleDoubleDownA.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtMiddleDoubleUpB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtMiddleDoubleDownB.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtMiddleDoubleUpC.getText().toString()) &
                            StringUtils.isEmptyOrNull(mEtMiddleDoubleDownC.getText().toString())) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else if (checkPoint(mEtMiddleDoubleUpA) || checkPoint(mEtMiddleDoubleDownA) || checkPoint(mEtMiddleDoubleUpB) ||
                            checkPoint(mEtMiddleDoubleDownB) || checkPoint(mEtMiddleDoubleUpC) || checkPoint(mEtMiddleDoubleDownC)) {
                        ToastUtil.show("最少需要一个数据");
                        return;
                    } else {
                        //中间接头 双导线 输入框值
                        Double[] values = new Double[]{
                                StringUtils.getValueFromEText(mEtMiddleDoubleUpA),
                                StringUtils.getValueFromEText(mEtMiddleDoubleDownA),
                                StringUtils.getValueFromEText(mEtMiddleDoubleUpB),
                                StringUtils.getValueFromEText(mEtMiddleDoubleDownB),
                                StringUtils.getValueFromEText(mEtMiddleDoubleUpC),
                                StringUtils.getValueFromEText(mEtMiddleDoubleDownC)};
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

    @Override
    public void onDestroyView() {
        if (call0 != null && !call0.isCanceled()) {
            call0.cancel();
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    private void resetInfraredTemperature() {
        mEtGuideLine.setText("");
        mEtEnvironmentTemperature.setText("");
        mEtBigSizeSlideA.setText("");
        mEtBigSizeSlideB.setText("");
        mEtBigSizeSlideC.setText("");
        mEtSmallSizeSlideA.setText("");
        mEtSmallSizeSlideB.setText("");
        mEtSmallSizeSlideC.setText("");
        mEtCenterA.setText("");
        mEtCenterB.setText("");
        mEtCenterC.setText("");
        mEtDoubleBigSizeSlideUpA.setText("");
        mEtDoubleBigSizeSlideDownA.setText("");
        mEtDoubleSmallSizeSlideUpA.setText("");
        mEtDoubleSmallSizeSlideDownA.setText("");
        mEtDoubleBigSizeSlideUpB.setText("");
        mEtDoubleBigSizeSlideDownB.setText("");
        mEtDoubleSmallSizeSlideUpB.setText("");
        mEtDoubleSmallSizeSlideDownB.setText("");
        mEtDoubleBigSizeSlideUpC.setText("");
        mEtDoubleBigSizeSlideDownC.setText("");
        mEtDoubleSmallSizeSlideUpC.setText("");
        mEtDoubleSmallSizeSlideDownC.setText("");
        mEtMiddleSingleA.setText("");
        mEtMiddleSingleB.setText("");
        mEtMiddleSingleC.setText("");
        mEtMiddleDoubleUpA.setText("");
        mEtMiddleDoubleDownA.setText("");
        mEtMiddleDoubleUpB.setText("");
        mEtMiddleDoubleDownB.setText("");
        mEtMiddleDoubleUpC.setText("");
        mEtMiddleDoubleDownC.setText("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
