package com.uflycn.uoperation.ui.fragment.dayplan.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanBaseBean;
import com.uflycn.uoperation.bean.MonthPlanBean;
import com.uflycn.uoperation.bean.WeekPlanBean;
import com.uflycn.uoperation.bean.YearPlanBean;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.adapter.DayPlanAdapter;
import com.uflycn.uoperation.ui.fragment.dayplan.presenter.DayPlanPresenterImp;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.SimpleDlg;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DayPlanFragment extends Fragment implements DayPlanView.DayPlanListView, SwipeRefreshLayout.OnRefreshListener {
    private static final int ADD_DAY_PLAN_REQUEST_CODE = 0x0015;
    //上面的四种时间类型
    @BindView(R.id.rg_time_type)
    RadioGroup rgTimeType;
    @BindView(R.id.rb_day)
    RadioButton rbDay;
    @BindView(R.id.rb_week)
    RadioButton rbWeek;
    @BindView(R.id.rb_month)
    RadioButton rbMonth;
    @BindView(R.id.rb_year)
    RadioButton rbYear;
    //中间的下拉刷新河recycler
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    //下面的三种计划按钮
    @BindView(R.id.rb_me_plan)
    RadioButton rbMePlan;
    @BindView(R.id.rb_all_plan)
    RadioButton rbAllPlan;
    @BindView(R.id.rb_add_plan)
    RadioButton rbAddPlan;
    @BindView(R.id.rg_type_plan)
    RadioGroup rgTypePlan;

    private Reference<Activity> mRef;

    private DayPlanPresenterImp mPresenter;
    private DayPlanAdapter mAdapter;
    private SimpleDlg mSimpleDlg;
    private Unbinder unbinder;

    public static final int TOP_TYPE_DAY = 0;
    public static final int TOP_TYPE_WEEK = 1;
    public static final int TOP_TYPE_MONTH = 2;
    public static final int TOP_TYPE_YEAR = 3;

    private int currentTopType = TOP_TYPE_DAY;

    private final int BOTTOM_TYPE_ME = 6;
    private final int BOTTOM_TYPE_ALL = 7;
    private final int BOTTOM_TYPE_ADD = 8;
    private int currentBottomType = BOTTOM_TYPE_ME;

    public static final int TYPE_ALL = 1;
    public static final int TYPE_ME = 0;

    private boolean isFromNet = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_plan, container, false);
        mRef = new WeakReference<Activity>(getActivity());
        unbinder = ButterKnife.bind(this, view);
        mPresenter = new DayPlanPresenterImp(this);
        initView();
        return view;
    }

    private void initView() {
        mAdapter = new DayPlanAdapter(new ArrayList<DayPlanBaseBean>());
        swipeRefresh.setOnRefreshListener(this);

        recycler.setLayoutManager(new LinearLayoutManager(mRef.get()));
        recycler.setAdapter(mAdapter);
//        recycler.addItemDecoration(new DividerItemDecoration(mRef.get(), DividerItemDecoration.VERTICAL));

        mAdapter.setListener(new DayPlanAdapter.DayPlanClickListener() {
            @Override
            public void onCheckDayPlan(DayPlan dayPlan) {
                if (MyApplication.mCurrentDayPlan != null && !MyApplication.mCurrentDayPlan.getSysDailyPlanSectionID().equals(dayPlan.getSysDailyPlanSectionID())) {
                    ToastUtil.show("请先关闭其他计划!");
                    return;
                }
              /*  String date = DateUtil.format(new Date(), DateUtil.PATTERN_CLASSICAL_SIMPLE);
                if (!date.equals(dayPlan.getStartDateString())) {
                    ToastUtil.show("当前日期与任务日期不匹配!");
                    return;
                }*/
                dayPlan.setIsFromTopType(currentTopType);//设置类型
                checkDayPlan(dayPlan);
                MyApplication.mCurrentDayPlan = dayPlan;
            }

            @Override
            public void onCloseDayPlan(DayPlan dayPlan) {
                showCloseDayPlanDialog(dayPlan);
            }
        });
        //点击类型
        rgTimeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                //防止重复调用
                switch (checkId) {
                    case R.id.rb_day:
                        currentTopType = TOP_TYPE_DAY;
                        //默认选中第一个
//                        rgTypePlan.check(R.id.rb_me_plan);
                        updateList();
                        break;
                    case R.id.rb_week:
                        currentTopType = TOP_TYPE_WEEK;
                        updateList();
                        break;
                    case R.id.rb_month:
                        currentTopType = TOP_TYPE_MONTH;
                        updateList();
                        break;
                    case R.id.rb_year:
                        currentTopType = TOP_TYPE_YEAR;
                        updateList();
                        break;
                    default:
                        break;
                }
                initBottom();
            }
        });
        rgTypePlan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                //防止重复调用
                switch (checkId) {
                    case R.id.rb_me_plan:
                        currentBottomType = BOTTOM_TYPE_ME;
                        updateList();
                        break;
                    case R.id.rb_all_plan:
                        currentBottomType = BOTTOM_TYPE_ALL;
                        updateList();
                        break;
                    default:
                        break;
                }
            }
        });
        rgTimeType.check(R.id.rb_day);
        rgTypePlan.check(R.id.rb_me_plan);
        //新增日计划
        rbAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转道新增日计划页面
                Intent intent = AddDayPlanActivity.newInstance(getContext());
                startActivityForResult(intent, ADD_DAY_PLAN_REQUEST_CODE);
            }
        });
//        rgTypePlan.check(R.id.rb_me_plan);
    }

    private void updateList() {
        showRefresh();
        switch (currentTopType) {
            case TOP_TYPE_DAY:
                //日计划
                if (currentBottomType == BOTTOM_TYPE_ME) {
                    //个人日计划
                    mPresenter.getDayPlanList(isFromNet, TYPE_ME);
                } else if (currentBottomType == BOTTOM_TYPE_ALL) {
                    //所有日计划
                    mPresenter.getDayPlanList(isFromNet, TYPE_ALL);
                }
                break;
            case TOP_TYPE_WEEK:
                //周计划
                mPresenter.getWeekPlanList();
                break;
            case TOP_TYPE_MONTH:
                //月计划
                mPresenter.getMonthPlanList();
                break;
            case TOP_TYPE_YEAR:
                mPresenter.getYearPlanList();
                //年计划
                break;
            default:
                break;

        }
    }

    private void showRefresh() {
        //先将其他的取消
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        //将当前的显示
        if (swipeRefresh != null && !swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
    }

    /**
     * 根据上面的类型,改变相应的button
     */
    private void initBottom() {
//        rgTypePlan 如果不是日计划则将下面的隐藏
        switch (currentTopType) {
            case TOP_TYPE_DAY:
                rgTypePlan.setVisibility(View.VISIBLE);
                rbAddPlan.setVisibility(View.VISIBLE);
                break;
            default:
                rgTypePlan.setVisibility(View.GONE);
                rbAddPlan.setVisibility(View.GONE);
                break;
        }
    }

    private void checkDayPlan(DayPlan dayPlan) {
        startActivity(DayPlanDetailActivity.newInstance(mRef.get(), dayPlan));
    }

    private void showCloseDayPlanDialog(final DayPlan dayPlan) {
        if (!dayPlan.getResponsiblePersonID().equals(AppConstant.currentUser.getUserId())) {
            ToastUtil.show("当前用户无权限关闭任务!");
            return;
        }
        if (dayPlan.getStatusString().equals("已完成")) {
            ToastUtil.show("任务已关闭!");
            return;
        }
        dayPlan.setIsFromTopType(currentTopType);
        startActivityForResult(DayPlanWorkDetailActivity.newInstance(mRef.get(), dayPlan), 0);
    }


    @OnClick({R.id.iv_open_close_drawer})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
                break;
        }

    }


    @Override
    public void onRefreshData(List<DayPlan> planList, int type) {
        dissmissRefresh();
        //设置类型
        if (planList != null && planList.size() > 0) {
            for (DayPlan dayPlan : planList) {
                dayPlan.setTopType(currentTopType);
                dayPlan.setType(type);
            }
        }
        mAdapter.setNewData(planList);
    }

    @Override
    public void onShowMessage(String msg) {
        dissmissRefresh();
        ToastUtil.show(msg);
    }

    //更新年计划列表
    @Override
    public void onRefreshYearPlanList(List<YearPlanBean> planBeans) {
        dissmissRefresh();
        if (planBeans != null && planBeans.size() == 0) {
            mAdapter.setNewData(new ArrayList());
        }
        if (planBeans != null && planBeans.size() > 0) {
            //设置类型
            for (YearPlanBean dayPlan : planBeans) {
                dayPlan.setTopType(currentTopType);
            }
        }
        mAdapter.setNewData(planBeans);
    }

    @Override
    public void onRefreshWeekPlanList(List<WeekPlanBean> planBeans) {
        dissmissRefresh();
        if (planBeans != null && planBeans.size() == 0) {
            mAdapter.setNewData(new ArrayList());
        }
        if (planBeans != null && planBeans.size() > 0) {
            //设置类型
            for (WeekPlanBean dayPlan : planBeans) {
                dayPlan.setTopType(currentTopType);
            }
        }
        mAdapter.setNewData(planBeans);
    }

    @Override
    public void onRefreshMonthPlanList(List<MonthPlanBean> planBeans) {
        dissmissRefresh();
        if (planBeans != null && planBeans.size() == 0) {
            mAdapter.setNewData(new ArrayList());
        }
        if (planBeans != null && planBeans.size() > 0) {
            //设置类型
            for (MonthPlanBean dayPlan : planBeans) {
                dayPlan.setTopType(currentTopType);
            }
        }
        mAdapter.setNewData(planBeans);
    }

    public void dissmissRefresh() {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        isFromNet = true;
        updateList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.cancelAll();
        unbinder.unbind();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            if (data != null && data.getBooleanExtra("IsFinish", false)) {
//                mPresenter.getDayPlanList(false, 1);
                updateList();
            }
        } else if (requestCode == ADD_DAY_PLAN_REQUEST_CODE && resultCode == AddDayPlanActivity.RESULT_CODE) {
            updateList();
        }
    }

    @Override
    public void onStart() {
        //开启巡视后，再次进入更新已开启计划的状态
        updateList();
        super.onStart();
    }
}
