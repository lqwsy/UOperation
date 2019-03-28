package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.MyBaseActivity;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DayPlanFromWeekPlanBean;
import com.uflycn.uoperation.bean.Organizition;
import com.uflycn.uoperation.bean.User;
import com.uflycn.uoperation.greendao.OrganizitionDbHelper;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.DayPlanFromWeekAdapter;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 周计划列表
 */
public class AddDayPlanFromWeekActivity extends MyBaseActivity {


    public static final String KEY_SELECT_PLANS = "key_select_plans";
    public static final int RESULT_CODE = 0x0019;
    @BindView(R.id.sp_voltage_class)
    Spinner mSpResponseClass;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.btn_filter)
    Button btnFilter;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    //    @BindView(R.id.swipe_refresh)
//    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    //筛选条件
    private String mCurrentOrganizitionId;//当前的班组
    private String mStartTime;//开始日期
    private String mEndTime;//结束日期
    //数据列表
    private List<DayPlanFromWeekPlanBean> mDatas;
    private DayPlanFromWeekAdapter mAdapter;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AddDayPlanFromWeekActivity.class);
        return intent;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_day_plan_from_week;
    }

    @Override
    public void initView() {
        super.initView();
        //处理班组
        handleClass();
        //默认的开始结束时间
//        mStartTime = "";
//        mEndTime = "";
//        tvStartDate.setText("");
//        tvEndDate.setText("");
        //设置recycler
        mDatas = new ArrayList<>();
        mAdapter = new DayPlanFromWeekAdapter(R.layout.item_day_plan_from_week_recycler, mDatas);
        View headView = View.inflate(this, R.layout.item_day_plan_from_week_head, null);
        mAdapter.addHeaderView(headView);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        //swip
       /* swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });*/
        //处理时间
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date endDate = calendar.getTime();
        String starDateStr = DateUtil.format(startDate, DateUtil.PATTERN_CLASSICAL_SIMPLE);
        String endDateStr = DateUtil.format(endDate, DateUtil.PATTERN_CLASSICAL_SIMPLE);
        mStartTime = starDateStr;
        mEndTime = endDateStr;
        tvStartDate.setText(starDateStr);
        tvEndDate.setText(endDateStr);
    }

    @Override
    public void initData() {
        super.initData();
        showDialog("加载中..");
        post2Net();
    }

    /**
     * 联网请求数据
     */
    private void post2Net() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ClassID", mCurrentOrganizitionId);
        jsonObject.addProperty("StartDate", mStartTime);
        jsonObject.addProperty("EndDate", mEndTime);
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(jsonObject));
        RetrofitManager.getInstance()
                .getService(ApiService.class)
                .getDayPlanFromWeekList(requestBody)
                .enqueue(new Callback<BaseCallBack<List<DayPlanFromWeekPlanBean>>>() {
                    @Override
                    public void onResponse(Call<BaseCallBack<List<DayPlanFromWeekPlanBean>>> call, Response<BaseCallBack<List<DayPlanFromWeekPlanBean>>> response) {
                        missDialog();
                        if (response != null && response.body() != null && response.body().getCode() == 1) {
                            refreshList(response.body().getData());
                        } else if (response.body() != null && response.body().getCode() == 401) {
                            ToastUtil.show("登录失效");
                        } else {
                            ToastUtil.show("获取列表失败!");
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseCallBack<List<DayPlanFromWeekPlanBean>>> call, Throwable t) {
                        missDialog();
                        ToastUtil.show("网络连接失败!");
                    }
                });
    }

    /**
     * 刷新列表
     */
    private void refreshList(List<DayPlanFromWeekPlanBean> data) {
        mDatas.clear();
        mDatas.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 处理班组
     */
    private void handleClass() {
        //获取所有的班组，并过滤掉没有人员的班组
        final List<Organizition> teamList = OrganizitionDbHelper.getInstance().findAllTeam();
        Iterator<Organizition> iterator = teamList.iterator();
        while (iterator.hasNext()) {
            Organizition organizition = iterator.next();
            //各个部门的人的集合
            List<User> users = new ArrayList<>();
            //通过部门名称获取到部门中的人
            users.addAll(UserDBHellper.getInstance().getUserByDepartment(organizition.getOrganizationId()));
            if (users.size() == 0) {
                iterator.remove();
                continue;
            }
        }
        //设置Spinner
        //负责班组
        final List<String> items = new ArrayList<>();
        items.add("所有班组");
        for (Organizition organizition : teamList) {
            items.add(organizition.getFullName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        mSpResponseClass.setAdapter(adapter);
        mSpResponseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //更新当前班组id
                if (position == 0) {
                    mCurrentOrganizitionId = "";
                } else {
                    String organizationId = teamList.get(position - 1).getOrganizationId();
                    mCurrentOrganizitionId = organizationId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mCurrentOrganizitionId = "";
    }

    //点击时间显示时间
    private void showStartTime() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String formDateStr = DateUtil.format(date, DateUtil.PATTERN_CLASSICAL_SIMPLE);
                mStartTime = formDateStr;
                tvStartDate.setText(formDateStr);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(20)
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择日计划时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setRangDate(startDate, endDate)
                .setDate(Calendar.getInstance())
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    //点击时间显示时间
    private void showEndTime() {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);
        Calendar currentCal = Calendar.getInstance();
        currentCal.add(Calendar.DAY_OF_MONTH, 7);
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                String formDateStr = DateUtil.format(date, DateUtil.PATTERN_CLASSICAL_SIMPLE);
                mEndTime = formDateStr;
                tvEndDate.setText(formDateStr);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(20)
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择日计划时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setRangDate(startDate, endDate)
                .setDate(currentCal)
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    @OnClick({R.id.iv_open_close_drawer, R.id.tv_start_date
            , R.id.tv_end_date, R.id.btn_filter
            , R.id.btn_save, R.id.btn_cancel})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                finish();
                break;
            case R.id.tv_start_date:
                showStartTime();
                break;
            case R.id.tv_end_date:
                showEndTime();
                break;
            case R.id.btn_filter:
                initData();//从新联网请求数据
                break;
            case R.id.btn_save:
                handleSave();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 保存
     */
    private void handleSave() {
        //获取集合中所有的
        final ArrayList<DayPlanFromWeekPlanBean> selectWeekPlans = new ArrayList<>();
        Observable.from(mDatas)
                .filter(new Func1<DayPlanFromWeekPlanBean, Boolean>() {
                    @Override
                    public Boolean call(DayPlanFromWeekPlanBean dayPlanFromWeekPlanBean) {
                        return dayPlanFromWeekPlanBean.isCheck();
                    }
                }).subscribe(new Action1<DayPlanFromWeekPlanBean>() {
            @Override
            public void call(DayPlanFromWeekPlanBean dayPlanFromWeekPlanBean) {
                selectWeekPlans.add(dayPlanFromWeekPlanBean);
            }
        });
        //将集合返回
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(KEY_SELECT_PLANS, selectWeekPlans);
        setResult(RESULT_CODE, intent);
        finish();
    }
}
