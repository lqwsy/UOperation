package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.DayPlanSection;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.eventbus.StopLineFromDayPlanEvent;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.ui.adapter.DayPlanInfoAdapter;
import com.uflycn.uoperation.ui.fragment.dayplan.presenter.DayPlanPresenterImp;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.SimpleDlg;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 日计划详情页面
 */
public class DayPlanDetailActivity extends Activity implements DayPlanView.DayPlanDetailView {

    @BindView(R.id.iv_open_close_drawer)
    ImageView mIvOpenCloseDrawer;

    @BindView(R.id.tv_plan_date)
    TextView tvPlanDate;
    @BindView(R.id.tv_response_class)
    TextView tvResponseClass;
    @BindView(R.id.tv_response_name)
    TextView tvResponseName;
    @BindView(R.id.tv_work_peoples)
    TextView tvWorkPeoples;
    @BindView(R.id.tv_line_name)
    TextView tvLineName;
    @BindView(R.id.tv_tour_towers)
    TextView tvTourTowers;
    @BindView(R.id.tv_work_type)
    TextView tvWorkType;
    @BindView(R.id.tv_work_content)
    TextView tvWorkContent;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.btn_check_day_plan)
    Button btnCheckDayPlan;
    @BindView(R.id.btn_close_day_plan)
    Button btnCloseDayPlan;
    @BindView(R.id.tv_have_tour_towers)
    TextView tvHaveTourTowers;


    private DayPlan mDayPlan;
    private DayPlanInfoAdapter mAdapter;
    private DayPlanPresenterImp mPresenter;
    //
    @BindView(R.id.rg_time_type)
    RadioGroup mRgTimeType;
    private SimpleDlg mSimpleDlg;
    //    private View mEmptyView;
    private ProgressDialog mProgressDialog;
    private SimpleDlg simpleDlg;
    private Unbinder unbinder;

    private DayPlanInfo dayPlanInfo;
    private DayPlanCloseLineDialog dayPlanCloseLineDialog;

    public static Intent newInstance(Context context, DayPlan dayPlan) {
        Intent intent = new Intent(context, DayPlanDetailActivity.class);
        intent.putExtra("dayPlan", dayPlan);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_day_plan);
        unbinder = ButterKnife.bind(this);
        mDayPlan = (DayPlan) getIntent().getSerializableExtra("dayPlan");
        mPresenter = new DayPlanPresenterImp(this);
        initView();
        initData();
    }

    private void initData() {
        showDialog("加载中..");
        mPresenter.getDailyTaskInfo(mDayPlan.getSysDailyPlanSectionID());
    }

    private void initView() {

        //修改按钮的状态
        initButtonStatus();
        //。。。
        int topType = mDayPlan.getIsFromTopType();
        switch (topType) {
            case DayPlanFragment.TOP_TYPE_DAY:
                mRgTimeType.check(R.id.rb_day);
                break;
            case DayPlanFragment.TOP_TYPE_WEEK:
                mRgTimeType.check(R.id.rb_week);
                break;
            case DayPlanFragment.TOP_TYPE_MONTH:
                mRgTimeType.check(R.id.rb_month);
                break;
            case DayPlanFragment.TOP_TYPE_YEAR:
                mRgTimeType.check(R.id.rb_year);
                break;
        }
        //让所有rb都不可点
        for (int i = 0; i < mRgTimeType.getChildCount(); i++) {
            mRgTimeType.getChildAt(i).setEnabled(false);
        }
        //设置一些基本信息
      /*  mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mPresenter.cancelAll();
            }
        });*/
    }

    private void initButtonStatus() {
        if (MyApplication.gridlineTaskStatus == 2 && MyApplication.mDayPlanLineMap.size() > 0) {
            btnCheckDayPlan.setBackgroundColor(getResources().getColor(R.color.stop_patrol_1));
        } else {
            btnCheckDayPlan.setBackgroundColor(getResources().getColor(R.color.tab_bg));
        }
    }

    @OnClick(value = {R.id.btn_check_day_plan, R.id.btn_close_day_plan})
    public void openOrClosePlan(View view) {
        switch (view.getId()) {
            case R.id.btn_check_day_plan://开启计划
               /* if (MyApplication.mLineIdNamePairs.size() >= 5) {
                    ToastUtil.show("线路最多开启5个");
                    return;
                }*/
                if (MyApplication.mLineIdNamePairs.containsKey(dayPlanInfo.getSysGridLineID() + "")) {
                    ToastUtil.show("线路已开启，无需再次开启");
                    return;
                }
                showCloseLineInfoDialog(dayPlanInfo, true);
                break;
            case R.id.btn_close_day_plan://关闭计划
                showCloseLineInfoDialog(dayPlanInfo, false);
                break;
        }
    }

    private void startLine(DayPlanInfo dayPlanInfo) {
        mProgressDialog.setMessage("正在开启线路...");
        mProgressDialog.show();
        mPresenter.openPlanDailyPlanSection(dayPlanInfo);
    }

    private void showCloseLineInfoDialog(final DayPlanInfo dayPlanInfo, final boolean isStart) {
     /*   String info = "";
        if (MyApplication.currentNearestTower == null && !isStart) {
            info = "当前最近塔暂未获取到,是否继续操作?";
        } else if (AppConstant.CURRENT_LOCATION == null || AppConstant.CURRENT_LOCATION.longitude == 0) {
            info = "当前位置经纬度暂未获取到,是否继续操作?";
        } else if (MyApplication.nearestDistance == 0 && !isStart) {
            info = "当前位置到最近塔距离为0米,是否继续操作?";
        }
        if (MyApplication.currentNearestTower != null) {
            Log.d("nate", "showCloseLineInfoDialog: " + MyApplication.currentNearestTower.getTowerNo());
        }
        if (StringUtils.isEmptyOrNull(info)) {

        } else {
            SimpleDlg.Builder builder = new SimpleDlg.Builder();
            simpleDlg = builder.create(this);
            builder.setTitle("提示");
            builder.setContentText(info);
            builder.setOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.dlg_btn_right) {
                        if (isStart) {
                            startLine(dayPlanInfo);
                        } else {
                            showCloseLineDialog(dayPlanInfo);
                        }
                    }
                    simpleDlg.dismiss();
                }
            });
            simpleDlg.show();
        }*/
        if (isStart) {
            startLine(dayPlanInfo);
        } else {
            showCloseLineDialog(dayPlanInfo);
        }
    }

    /**
     * 显示关闭线路对话框
     */
    private void showCloseLineDialog(final DayPlanInfo dayPlanInfo) {
        if (MyApplication.gridlineTaskStatus != 2 && MyApplication.mDayPlanLineMap.get(dayPlanInfo.getSysGridLineID().intValue()) == null) {
            ToastUtil.show("请开启巡视!");
            return;
        }
        //联网获取关闭的数据
        showDialog("加载中..");
        String sysDailyPlanTimeSpanID = MyApplication.mOpenDayPlanLineMap.get(dayPlanInfo.getSysDailyPlanSectionID() + "");
        mPresenter.getSelectTowers(sysDailyPlanTimeSpanID);
    }


    @OnClick({R.id.iv_open_close_drawer})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                finish();
                break;
        }

    }

    @Override
    public void onRefreshData(List<DayPlanInfo> planList) {
        missDialog();
        updateDayPlanInfo(planList.get(0));
    }

    /**
     * 更新信息
     */
    private void updateDayPlanInfo(DayPlanInfo dayPlanInfo) {
        this.dayPlanInfo = dayPlanInfo;
        tvPlanDate.setText(dayPlanInfo.getStartDateString());
        tvResponseClass.setText(dayPlanInfo.getClassName());
        tvResponseName.setText(dayPlanInfo.getResponsiblePersonName());
        tvWorkPeoples.setText(dayPlanInfo.getOfficeHolderNames());
        tvLineName.setText(dayPlanInfo.getVoltageClass() + "  " + dayPlanInfo.getLineName());
        tvTourTowers.setText(dayPlanInfo.getTowerNos());
        tvHaveTourTowers.setText(dayPlanInfo.getPatrolTowerNos());
        tvWorkType.setText(dayPlanInfo.getTypeOfWorkString());
        tvWorkContent.setText(dayPlanInfo.getJobContent());
        tvRemark.setText(dayPlanInfo.getRemark());
    }

    @Override
    public void onShowMessage(String msg) {
        missDialog();
        ToastUtil.show(msg);
    }

    public void showDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    public void missDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
        if (dayPlanCloseLineDialog != null && dayPlanCloseLineDialog.isShowing()) {
            dayPlanCloseLineDialog.dismiss();
        }
    }

    @Override
    public void onOpenDayPlanSectionSuccess(DayPlanSection dayPlanSection, DayPlanInfo dayPlanInfo) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        MyApplication.gridlineTaskStatus = 2;
        MyApplication.mLineIdNamePairs.put(dayPlanInfo.getSysGridLineID() + "", dayPlanInfo.getLineName());
        if (!MyApplication.gridlines.containsKey(dayPlanInfo.getSysGridLineID())) {
            Gridline gridline = GridLineDBHelper.getInstance().getLine(dayPlanInfo.getSysGridLineID());
            MyApplication.gridlines.put(dayPlanInfo.getSysGridLineID().intValue(), gridline);
        }
        MyApplication.mDayPlanLineMap.put(dayPlanInfo.getSysGridLineID().intValue(), dayPlanSection.getSysDailyPlanSectionID());
        MyApplication.mOpenDayPlanLineMap.put(dayPlanInfo.getSysDailyPlanSectionID() + "", dayPlanSection.getSysDailyPlanTimeSpanID());
        EventBus.getDefault().post(dayPlanInfo);
        Intent intent = new Intent(DayPlanDetailActivity.this, MainActivity.class);
        MyApplication.mDayPlanLineSb = new StringBuffer();
        MyApplication.mOpenDayPlanLineSb = new StringBuffer();

        for (String values : MyApplication.mDayPlanLineMap.values()) {
            MyApplication.mDayPlanLineSb.append(values).append(",");
        }
        for (String values : MyApplication.mOpenDayPlanLineMap.values()) {
            MyApplication.mOpenDayPlanLineSb.append(values).append(",");
        }
        if (MyApplication.mDayPlanLineSb.length() > 0) {
            MyApplication.mDayPlanLineSb.deleteCharAt(MyApplication.mDayPlanLineSb.length() - 1);
        }
        if (MyApplication.mOpenDayPlanLineSb.length() > 0) {
            MyApplication.mOpenDayPlanLineSb.deleteCharAt(MyApplication.mOpenDayPlanLineSb.length() - 1);
        }
        startActivity(intent);
    }

    @Override
    public void onCloseDayPlanSectionSuccess(DayPlanInfo dayPlanInfo) {
        mProgressDialog.dismiss();
        if (dayPlanCloseLineDialog != null && dayPlanCloseLineDialog.isShowing()) {
            dayPlanCloseLineDialog.dismiss();
        }
        MyApplication.mDayPlanLineMap.remove(dayPlanInfo.getSysGridLineID().intValue());
        MyApplication.mOpenDayPlanLineMap.remove(dayPlanInfo.getSysDailyPlanSectionID() + "");
        if (MyApplication.mDayPlanLineMap.size() == 0) {
            MyApplication.gridlineTaskStatus = 0;
        }
        //        MyApplication.mLineIdNamePairs.remove(dayPlanInfo.getSysGridLineID() + "");
        EventBus.getDefault().post(new StopLineFromDayPlanEvent(dayPlanInfo.getSysGridLineID().intValue()));
        MyApplication.gridlines.remove(dayPlanInfo.getSysGridLineID());
        //重新加载数据
        initData();
        //改变button颜色
        initButtonStatus();
        ToastUtil.show("结束巡视成功!");
    }

    //获取到额关闭日计划中的数据
    @Override
    public void onRefreshSelectData(List<SelectTower> towers) {
        missDialog();
        SelectTower selectTower = towers.get(0);
        dayPlanCloseLineDialog = new DayPlanCloseLineDialog(this);
        //设置数据
        dayPlanCloseLineDialog.setData(dayPlanInfo.getStartDateString(), selectTower);
        //设置点击监听
        dayPlanCloseLineDialog.setListener(new DayPlanCloseLineDialog.onCloseLineListener() {
            @Override
            public void onClose(List<SelectTower.TowerListBean> datas) {
                mProgressDialog.setMessage("正在关闭线路...");
                mProgressDialog.show();
                String sysDailyPlanTimeSpanID = MyApplication.mOpenDayPlanLineMap.get(dayPlanInfo.getSysDailyPlanSectionID() + "");
                mPresenter.closePlanDailyPlanSection(sysDailyPlanTimeSpanID, dayPlanInfo, datas);
            }
        });
        dayPlanCloseLineDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MyApplication.mDayPlanLineMap.size() == 0) {
            MyApplication.mCurrentDayPlan = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelAll();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
