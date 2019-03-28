package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.AddDayPlanBean;
import com.uflycn.uoperation.bean.AddDayPlanDefectBean;
import com.uflycn.uoperation.bean.AddDayPlanFronLineBean;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DailyPlanDefect;
import com.uflycn.uoperation.bean.DailyPlanJobContent;
import com.uflycn.uoperation.bean.DailyPlanOfficeHolder;
import com.uflycn.uoperation.bean.DailyPlanTower;
import com.uflycn.uoperation.bean.DayPlanFromWeekPlanBean;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.JobContent;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.UploadDayPlanBean;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.AddDayPlanAdapter;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.AddDayPlanDefectAdapter;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.AddDayPlanSafeAdapter;
import com.uflycn.uoperation.ui.fragment.dayplan.presenter.DayPlanPresenterImp;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.SimpleDlg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 新增任务
 */
public class AddDayPlanActivity extends Activity implements DayPlanView.AddDayPlanView {
    public static final String DAY_PLAN = "day_plan";
    private static final int ADD_PLAN_LINE_REQUEST_CODE = 0x0015;
    private static final int ADD_DEFECT_REQUEST_CODE = 0x0011;
    public static final int RESULT_CODE = 0x0014;
    private static final int FROM_WEEK_REQUEST_CODE = 0x0016;
    private DayPlanPresenterImp mPresenter;
    private Unbinder unbinder;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.et_remark)
    EditText mEtRemark;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private List<AddDayPlanBean> mDatas;
    private AddDayPlanAdapter mAdapter;//新增任务-线路列表
    @BindView(R.id.safe_recycler)
    RecyclerView mSafeRecycler;//安全措施
    private List<AddDayPlanBean> mSafeDatas;
    private AddDayPlanSafeAdapter mSafeAdapter;//安全措施和注意事项
    @BindView(R.id.defect_recycler)
    RecyclerView mDefectRecycler;
    private ProgressDialog mProgressDialog;
    List<AddDayPlanDefectBean> mDefectList;//缺陷
    private AddDayPlanDefectAdapter mDefectAdapter;//缺陷列表
    private Date mCurrentDate;
    private SimpleDlg mSimpleDlg;
    private EditText edit;

    /**
     * 实例化AddDayPlanActivity
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AddDayPlanActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_day_plan);
//        EventBus.getDefault().register(this);
        mPresenter = new DayPlanPresenterImp(this);
        unbinder = ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        //处理日期
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mCurrentDate = calendar.getTime();
        final String formatDate = sf.format(mCurrentDate);
        mTvTime.setText(formatDate);
        //点击后选中日期
        mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime();
            }
        });
        //设置recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mDatas = new ArrayList<>();
        mAdapter = new AddDayPlanAdapter(R.layout.item_add_day_plan_recycler, mDatas);
        View headView = View.inflate(this, R.layout.item_add_day_plan_head, null);//新增任务中线路列表
        mAdapter.addHeaderView(headView);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycler.setAdapter(mAdapter);
        //解决滑动冲突
//        mRecycler.setNestedScrollingEnabled(false);
        //设置线路条目的按钮的点击事件（选择和移除）
        mAdapter.setOnMulteChildClickListener(new AddDayPlanAdapter.OnMulteChildClickListener() {
            @Override
            public void onSelectNums(AddDayPlanBean bean) {
                handleSelectNums(bean);
            }

            @Override
            public void onSelectPeoples(AddDayPlanBean bean) {
                handleSelectPeoples(bean);
            }

            @Override
            public void remove(AddDayPlanBean bean) {
                mDatas.remove(bean);
                mSafeDatas.remove(bean);
                mAdapter.notifyDataSetChanged();
                mSafeAdapter.notifyDataSetChanged();
//                AddDayPlanDefectBean addDayPlanDefectBean = new AddDayPlanDefectBean();
//                for (int i = 0; i < mDefectList.size(); i++) {
//                    int getLineid = mDefectList.get(i).getSysGridLineId();
//                    if (bean.getSysGridLineId()==getLineid) {
//                        mDefectList.remove(mDefectList.get(i));
//                    }
//                }
//                mDefectAdapter.notifyDataSetChanged();
                removeSafeAndDefectByLineId(bean.getSysGridLineId());
                mDefectAdapter.notifyDataSetChanged();
            }
        });

        //<editor-fold desc="处理安全措施和注意事">
        //处理安全措施和注意事项的recycler
        mSafeDatas = new ArrayList<>();
        mSafeRecycler.setLayoutManager(new LinearLayoutManager(this));
        mSafeAdapter = new AddDayPlanSafeAdapter(R.layout.item_add_day_plan_safe_recycler, mSafeDatas);//安全措施和注意事项数据
        View safeHeadView = View.inflate(this, R.layout.item_add_day_plan_safe_head, null);//标题
        mSafeAdapter.addHeaderView(safeHeadView);
        mSafeRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSafeRecycler.setAdapter(mSafeAdapter);
//        mSafeRecycler.setNestedScrollingEnabled(false);
        //处理安全和注意的点击事件
        mSafeAdapter.setOnSafeAndNoticeClickListener(new AddDayPlanSafeAdapter.OnSafeAndNoticeClickListener() {

            //<editor-fold desc="安全措施">
            @Override
            public void onSafe(final AddDayPlanBean bean) {

//                if (mSimpleDlg == null) {
                SimpleDlg.Builder builder = new SimpleDlg.Builder();
//                    View view1 = View.inflate(AddDayPlanActivity.this, R.layout.general_dialog, null);
//                    final TextView textView = (TextView) view1.findViewById(R.id.dlg_content_tv);
//                    textView.setVisibility(View.GONE);
//                    edit.setVisibility(View.VISIBLE);
                List<JobContent> jobContents = bean.getJobContents();
                StringBuilder safeSb = new StringBuilder();
                for (int i = 0; i < jobContents.size(); i++) {
                    safeSb.append(jobContents.get(i).getSafetyMeasure());
                    if (i != jobContents.size() - 1) {
                        safeSb.append(",");
                    }
                }
                mSimpleDlg = builder.create(AddDayPlanActivity.this);

//                    edit = (EditText) view1.findViewById(R.id.edt_content);
                edit = builder.getEdit();
                edit.setText(safeSb.toString());
                builder.setETVisible();
                builder.setTvGone();
                builder.setTitle("修改安全措施");
                builder.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.dlg_btn_left:
                                mSimpleDlg.dismiss();

                                break;
                            case R.id.dlg_btn_right:
                                String content = edit.getText().toString();
                                if (StringUtils.isEmptyOrNull(content)) {
                                    ToastUtil.show("安全措施不能为空!");
                                    return;
                                }
//                                bean.getJobContent().setSafetyMeasure(edit.getText().toString());
                                String[] split = content.split(",");
                                List<JobContent> jobContents1 = bean.getJobContents();
                                for (int i = 0; i < split.length; i++) {
                                    jobContents1.get(i).setSafetyMeasure(split[i]);
                                }
                                if (split.length < jobContents1.size()) {
                                    for (int j = split.length; j < jobContents1.size(); j++) {
                                        jobContents1.get(j).setSafetyMeasure("");
                                    }
                                }
                                mSafeAdapter.notifyDataSetChanged();
                                mSimpleDlg.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
//                                    mSimpleDlg.setContentView(view1);
//                }

                mSimpleDlg.show();
//                mSimpleDlg=null;

//                AlertDialog dialog = new AlertDialog.Builder(AddDayPlanActivity.this)
//                        .setTitle("修改安全措施")
//                        .setView(view1)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int position) {
//                                String content = edit.getText().toString();
//                                if (StringUtils.isEmptyOrNull(content)) {
//                                    ToastUtil.show("安全措施不能为空!");
//                                    return;
//                                }
////                                bean.getJobContent().setSafetyMeasure(edit.getText().toString());
//                                String[] split = content.split(",");
//                                List<JobContent> jobContents1 = bean.getJobContents();
//                                for (int i = 0; i < split.length; i++) {
//                                    jobContents1.get(i).setSafetyMeasure(split[i]);
//                                }
//                                if (split.length < jobContents1.size()) {
//                                    for (int j = split.length; j < jobContents1.size(); j++) {
//                                        jobContents1.get(j).setSafetyMeasure("");
//                                    }
//                                }
//                                mSafeAdapter.notifyDataSetChanged();
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).create();
//                dialog.show();
            }
            //</editor-fold>

            //<editor-fold desc="注意事项">
            @Override
            public void onNotice(final AddDayPlanBean bean) {
//                View view1 = View.inflate(AddDayPlanActivity.this, R.layout.dialog_edt, null);
//                final EditText edit = (EditText) view1.findViewById(R.id.edt_content);
//                if (mSimpleDlg == null) {
                SimpleDlg.Builder builder = new SimpleDlg.Builder();
                List<JobContent> jobContents = bean.getJobContents();
                StringBuilder safeSb = new StringBuilder();
                for (int i = 0; i < jobContents.size(); i++) {
                    safeSb.append(jobContents.get(i).getSafetyPrecaution());
                    if (i != jobContents.size() - 1) {
                        safeSb.append(",");
                    }
                }
                mSimpleDlg = builder.create(AddDayPlanActivity.this);
                edit = builder.getEdit();
                edit.setText(safeSb.toString());
                builder.setETVisible();
                builder.setTvGone();
                builder.setTitle("修改注意事项");
                builder.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.dlg_btn_left:
                                mSimpleDlg.dismiss();
                                break;
                            case R.id.dlg_btn_right:
                                String content = edit.getText().toString();
                                if (StringUtils.isEmptyOrNull(content)) {
                                    ToastUtil.show("注意事项不能为空!");
                                    return;
                                }
//                                bean.getJobContent().setSafetyMeasure(edit.getText().toString());
                                String[] split = content.split(",");
                                List<JobContent> jobContents1 = bean.getJobContents();
                                for (int i = 0; i < split.length; i++) {
                                    jobContents1.get(i).setSafetyPrecaution(split[i]);
                                }
                                if (split.length < jobContents1.size()) {
                                    for (int j = split.length; j < jobContents1.size(); j++) {
                                        jobContents1.get(j).setSafetyPrecaution("");
                                    }
                                }
                                mSafeAdapter.notifyDataSetChanged();
                                mSimpleDlg.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
//                }
                mSimpleDlg.show();
//                mSimpleDlg=null;

//                edit.setText(safeSb.toString());
//                AlertDialog dialog = new AlertDialog.Builder(AddDayPlanActivity.this)
//                        .setTitle("修改安全措施")
//                        .setView(view1)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int position) {
//                                String content = edit.getText().toString();
//                                if (StringUtils.isEmptyOrNull(content)) {
//                                    ToastUtil.show("安全措施不能为空!");
//                                    return;
//                                }
////                                bean.getJobContent().setSafetyMeasure(edit.getText().toString());
//                                String[] split = content.split(",");
//                                List<JobContent> jobContents1 = bean.getJobContents();
//                                for (int i = 0; i < split.length; i++) {
//                                    jobContents1.get(i).setSafetyPrecaution(split[i]);
//                                }
//                                if (split.length < jobContents1.size()) {
//                                    for (int j = split.length; j < jobContents1.size(); j++) {
//                                        jobContents1.get(j).setSafetyPrecaution("");
//                                    }
//                                }
//                                mSafeAdapter.notifyDataSetChanged();
//                                dialogInterface.dismiss();
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).create();
//                dialog.show();
            }
            //</editor-fold>
        });
        //</editor-fold>

        //<editor-fold desc="处理 缺陷的adpter">
        mDefectList = new ArrayList<>();
        mDefectAdapter = new AddDayPlanDefectAdapter(R.layout.item_add_day_plan_defect, mDefectList);
        View defectHeadView = View.inflate(this, R.layout.item_select_defects_day_plan_head, null);//标题
        mDefectAdapter.addHeaderView(defectHeadView);
        mDefectRecycler.setLayoutManager(new LinearLayoutManager(this));
        mDefectRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mDefectRecycler.setAdapter(mDefectAdapter);
//        mDefectRecycler.setNestedScrollingEnabled(false);
        mDefectAdapter.setOnRemoveListener(new AddDayPlanDefectAdapter.OnRemoveListener() {
            @Override
            public void onRemove(AddDayPlanDefectBean bean) {
                mDefectList.remove(bean);
                mDefectAdapter.notifyDataSetChanged();
            }
        });
        //</editor-fold>
    }

    private void removeSafeAndDefectByLineId(int lineId) {
//        Iterator<AddDayPlanBean> siterator = mSafeDatas.iterator();
//        while(siterator.hasNext()){
//            AddDayPlanBean addDayPlanBean = siterator.next();
//            if(addDayPlanBean.getSysGridLineId() == lineId){
//                siterator.remove();
//            }
//        }
        Iterator<AddDayPlanDefectBean> diterator = mDefectList.iterator();
        while(diterator.hasNext()){
            AddDayPlanDefectBean addDayPlanDefectBean = diterator.next();
            if(addDayPlanDefectBean.getSysGridLineId() == lineId){
                diterator.remove();
            }
        }
    }

    //<editor-fold desc="刷新安全措施适配器">
    //刷新安全措施适配器
    public void RefushAdapter() {
        if (mDefectAdapter == null) {
            mDefectAdapter = new AddDayPlanDefectAdapter(R.layout.item_add_day_plan_defect, mDefectList);
        }
        mDefectAdapter.notifyDataSetChanged();
    }
    //</editor-fold>

    //<editor-fold desc="处理选择的人">

    /**
     * 处理选择的人
     */
    private void handleSelectPeoples(final AddDayPlanBean bean) {
        final SelectPeopleDialog dialog = new SelectPeopleDialog(this);
        dialog.setData(bean);
        dialog.setOnCloseListener(new SelectPeopleDialog.OnCloseListener() {
            @Override
            public void close(AddDayPlanBean resultBean) {
                bean.setClassId(resultBean.getClassId());
                bean.setClassName(resultBean.getClassName());
                bean.setResponseName(resultBean.getResponseName());
                bean.setResponseId(resultBean.getResponseId());
                bean.setWorkPeopleIds(resultBean.getWorkPeopleIds());
                bean.setWorkPeopleNames(resultBean.getWorkPeopleNames());
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    //</editor-fold>

    //<editor-fold desc="处理选中塔号的点击事件">

    /**
     * 处理选中塔号的点击事件
     */
    private void handleSelectNums(final AddDayPlanBean bean) {
        //所有塔tower，里面有为选中的和选中的
        final List<Tower> resultTowers = new ArrayList<>();
        //数据库中所有的tower，都是未选中的
        final List<Tower> dbTowers = TowerDBHelper.getInstance().getLineTowers(bean.getSysGridLineId());
        //bean中被选中的tower
        final List<Tower> selectTower = bean.getTowers();
        //代表着第一次线路中返回过来
        if (selectTower == null || selectTower.size() == 0) {
            Observable.from(dbTowers)
                    .subscribe(new Action1<Tower>() {
                        @Override
                        public void call(Tower tower) {
                            resultTowers.add(tower);
                        }
                    });
        } else {
            //如果选中的塔中有数据
            Observable.from(dbTowers)
                    .map(new Func1<Tower, Tower>() {
                        @Override
                        public Tower call(Tower tower) {
                            if (isContain(selectTower, tower)) {
                                tower.setChecked(true);
                            } else {
                                tower.setChecked(false);
                            }
                            return tower;
                        }
                    }).subscribe(new Action1<Tower>() {
                @Override
                public void call(Tower tower) {
                    resultTowers.add(tower);
                }
            });
        }
        final FromLineTowerNumsDialog dialog = new FromLineTowerNumsDialog(this);
        dialog.setData(resultTowers, bean.getLineName());
        dialog.show();
        //监听对话框的确定事件
        dialog.setOnSureTowerListener(new FromLineTowerNumsDialog.OnSureTowerNumsListener() {
            @Override
            public void onSureTowerNums(List<Tower> selectDatas) {
                //获取选中的塔
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < selectDatas.size(); i++) {
                    Tower tower = selectDatas.get(i);
                    sb.append(tower.getTowerNo());
                    if (i == selectDatas.size() - 1) {
                    } else {
                        sb.append(",");
                    }
                }
                //更新显示的towernos
                bean.setTourNums(sb.toString());
                //更新选中的towers
                bean.setTowers(selectDatas);
                //刷新页面
                mAdapter.notifyDataSetChanged();
                //消失对话框
                dialog.dismiss();
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="是否被包含">

    /**
     * 是否被包含
     */
    private boolean isContain(List<Tower> selectTower, Tower tower) {
        boolean isContain = false;
        for (Tower tower1 : selectTower) {
            if (tower1.getTowerNo().equals(tower.getTowerNo())) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }
    //</editor-fold>

    //<editor-fold desc="点击时间显示时间">
    //点击时间显示时间
    private void showTime() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mCurrentDate = date;
                mTvTime.setText(DateUtil.format(date, DateUtil.PATTERN_CLASSICAL_SIMPLE));
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
    //</editor-fold>

    @Override
    public void onShowMessage(String msg) {

    }

    //<editor-fold desc="点击事件">
    @OnClick({R.id.iv_open_close_drawer, R.id.btn_from_line,
            R.id.btn_from_week, R.id.btn_add_defects,
            R.id.btn_sure, R.id.btn_cancel})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                finish();
                break;
            case R.id.btn_from_line:
                Intent intent = AddDayPlanFromLineActivity.newInstance(this);
                startActivityForResult(intent, ADD_PLAN_LINE_REQUEST_CODE);
                break;
            case R.id.btn_from_week:
                handleFromWeek();
                break;
            case R.id.btn_add_defects:
                handleAddDefects();
                break;
            case R.id.btn_sure:
                submitAddDayPlan();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="从周计划中获取">

    /**
     * 从周计划中获取
     */
    private void handleFromWeek() {
        Intent intent = AddDayPlanFromWeekActivity.newInstance(this);
        startActivityForResult(intent, FROM_WEEK_REQUEST_CODE);
    }
    //</editor-fold>

    //<editor-fold desc="提交新增日计划">

    /**
     * 提交新增日计划
     */
    private void submitAddDayPlan() {
        if (mDatas.size() == 0) {
            ToastUtil.show("请先添加线路！");
            return;
        }
        //日计划中的除了缺陷的数据都在这个类里面，注意筛选出要提交的数据
        List<UploadDayPlanBean> uploadBeans = new ArrayList<>();
        for (AddDayPlanBean addDayPlanBean : mDatas) {
            if (addDayPlanBean.getTowers().size() == 0) {
                ToastUtil.show("请选择杆塔!");
                return;
            }
            if (StringUtils.isEmptyOrNull(addDayPlanBean.getResponseName()) ||
                    StringUtils.isEmptyOrNull(addDayPlanBean.getWorkPeopleNames())
                    || StringUtils.isEmptyOrNull(addDayPlanBean.getClassName())) {
                ToastUtil.show("请选择负责人或工作人员或班组!");
                return;
            }
            UploadDayPlanBean uploadDayPlanBean = createUploadDayPlanBean(addDayPlanBean);
            uploadBeans.add(uploadDayPlanBean);
        }
        //联网进行提交数据
        post2Net(uploadBeans);
    }
    //</editor-fold>

    //<editor-fold desc="提交">
    //提交
    private void post2Net(List<UploadDayPlanBean> uploadBeans) {
        showDialog("加载中..");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(uploadBeans);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class)
                .postAddDayPlan(requestBody);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                missDialog();
                if (response != null || response.body() != null || response.body().getCode() == 1) {
                    ToastUtil.show("提交成功!");
                    Intent intent = new Intent();
                    setResult(RESULT_CODE, intent);
//                    EventBus.getDefault().post(new UpdateDayPlan());
                    finish();
                } else {
                    ToastUtil.show("提交失败!");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                missDialog();
                ToastUtil.show("网络连接失败!");
            }
        });
    }
    //</editor-fold>

    private UploadDayPlanBean createUploadDayPlanBean(AddDayPlanBean addDayPlanBean) {
        UploadDayPlanBean bean = new UploadDayPlanBean();
        if (addDayPlanBean.isFromWeekPlan()) {
            bean.setSysWeeklyPlanSectionID(addDayPlanBean.getSysWeeklyPlanSectionID());
        }
        bean.setSysGridLineID(addDayPlanBean.getSysGridLineId());
        bean.setTypeOfWork(addDayPlanBean.getTypeOfWork().getItemDetailsId());
        bean.setClassID(addDayPlanBean.getClassId());
        bean.setResponsiblePersonID(addDayPlanBean.getResponseId());
        StringBuilder safeSb = new StringBuilder();
        StringBuilder precautionSb = new StringBuilder();
        for (JobContent jobContent : addDayPlanBean.getJobContents()) {
            safeSb.append(jobContent.getSafetyMeasure());
            precautionSb.append(jobContent.getSafetyPrecaution());
            safeSb.append(",");
            precautionSb.append(",");
        }
        if (safeSb.length() > 0) {
            safeSb.deleteCharAt(safeSb.length() - 1);
            precautionSb.deleteCharAt(precautionSb.length() - 1);
        }
        bean.setSafetyMeasure(safeSb.toString());
        bean.setSafetyPrecaution(precautionSb.toString());
        bean.setStartDate(mCurrentDate);
        bean.setRemark(mEtRemark.getText().toString());
        // 塔的列表
        List<DailyPlanTower> towers = new ArrayList<>();
        for (Tower tower : addDayPlanBean.getTowers()) {
            DailyPlanTower dailyPlanTower = new DailyPlanTower();
            dailyPlanTower.setSysTowerID(tower.getSysTowerID().intValue());
            towers.add(dailyPlanTower);
        }
        bean.setPlanDailyPlanTowerList(towers);
        //工作人员表
        List<DailyPlanOfficeHolder> officeHolders = new ArrayList<>();
        for (String id : addDayPlanBean.getWorkPeopleIds()) {
            DailyPlanOfficeHolder holder = new DailyPlanOfficeHolder();
            holder.setOfficeHolderID(id);
            officeHolders.add(holder);
        }
        bean.setPlanDailyPlanOfficeHolderList(officeHolders);
        //工作内容表
        List<DailyPlanJobContent> jobContents = new ArrayList<>();
        for (JobContent jobContent : addDayPlanBean.getJobContents()) {
            DailyPlanJobContent content = new DailyPlanJobContent();
            content.setSysWorkContentID(jobContent.getSysWorkContentID());
            jobContents.add(content);
        }
        bean.setPlanDailyPlanJobContentList(jobContents);
        //日计划缺陷表
        List<DailyPlanDefect> defects = new ArrayList<>();
        for (AddDayPlanDefectBean addDayPlanDefectBean : mDefectList) {
            //判断缺陷中是否是当前线路的缺陷
            if (addDayPlanDefectBean.getSysGridLineId() == addDayPlanBean.getSysGridLineId()) {
                DailyPlanDefect defect = new DailyPlanDefect();
                defect.setDefectID(addDayPlanDefectBean.getDefectID().intValue());
                defect.setDefectType(addDayPlanDefectBean.getDefectType());
                defects.add(defect);
            }
        }
        bean.setPlanDailyPlanDefectList(defects);
        return bean;
    }

    //<editor-fold desc="添加缺陷">

    /**
     * 添加缺陷
     */
    private void handleAddDefects() {
        if (mDatas.size() == 0) {
            ToastUtil.show("请添加线路!");
            return;
        }

        //所有线路的id
        StringBuilder sb = new StringBuilder();
        for (AddDayPlanBean data : mDatas) {
            sb.append(data.getSysGridLineId());
            sb.append(",");
        }
        //去掉最后一个,
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.lastIndexOf(","));
        }
        //跳转到选择缺陷页面
        Intent intent = DayPlanSelectDefectsActivity.newInstance(this, sb.toString());
        startActivityForResult(intent, ADD_DEFECT_REQUEST_CODE);
    }
    //</editor-fold>


    //<editor-fold desc="获取数据">
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //从线路中获取到的数据
        if (requestCode == ADD_PLAN_LINE_REQUEST_CODE && resultCode == AddDayPlanFromLineActivity.RESULT_CODE) {
            ArrayList<AddDayPlanFronLineBean> beans = intent.getParcelableArrayListExtra(AddDayPlanFromLineActivity.KEY_RESULT);
            //将数据封装到bean中
            for (AddDayPlanFronLineBean bean : beans) {
                AddDayPlanBean dayPlanBean = new AddDayPlanBean();
                //最基础的11个
                dayPlanBean.setLineName(bean.getLineName());
                dayPlanBean.setVoltageClass(bean.getVoltageClass());
                dayPlanBean.setSysGridLineId(bean.getSysGridLineId());
                dayPlanBean.setMaintainClass(bean.getMaintainClass());
                dayPlanBean.setInspectTowerIds(bean.getInspectTowerIds());
                dayPlanBean.setInspectTowerNos(bean.getInspectTowerNos());
                dayPlanBean.setSelectMonth(bean.getSelectMonth());
                dayPlanBean.setMaintainClassName(bean.getMaintainClassName());
                dayPlanBean.setDangerDefectCount(bean.getDangerDefectCount());
                dayPlanBean.setSeriousDefectCount(bean.getSeriousDefectCount());
                dayPlanBean.setNormalDefectCount(bean.getNormalDefectCount());
                //from lin的四个
                dayPlanBean.setTourNums(bean.getTourNums());
                dayPlanBean.setTypeOfWork(bean.getTypeOfWork());
                dayPlanBean.setJobContents(bean.getJobContents());
                dayPlanBean.setTowers(bean.getTowers());
                //设置是否来自周计划
                dayPlanBean.setFromWeekPlan(false);
                //添加到头部的recycler中
                mDatas.add(dayPlanBean);//添加到数据中
                //添加到安全措施和注意事项的数据。
                mSafeDatas.add(dayPlanBean);
            }
            mAdapter.notifyDataSetChanged();
            //同时修改的还有注意事项和安全措施中的recycler数据
            mSafeAdapter.notifyDataSetChanged();
        } else if (requestCode == ADD_DEFECT_REQUEST_CODE && resultCode == DayPlanSelectDefectsActivity.RESULT_CODE) {//获取缺陷数据
            List<AddDayPlanDefectBean> defectBeanList = intent.getParcelableArrayListExtra(DayPlanSelectDefectsActivity.KEY_DEFECT);
            //将数据设置到列表中
            mDefectList.addAll(defectBeanList);
            mDefectAdapter.notifyDataSetChanged();
        } else if (requestCode == FROM_WEEK_REQUEST_CODE && resultCode == AddDayPlanFromWeekActivity.RESULT_CODE) {
            ArrayList<DayPlanFromWeekPlanBean> datas = intent.getParcelableArrayListExtra(AddDayPlanFromWeekActivity.KEY_SELECT_PLANS);
            //将数据转换成 AddDayPlanFronLineBean
            for (DayPlanFromWeekPlanBean bean : datas) {
                AddDayPlanBean dayPlanBean = new AddDayPlanBean();
                //最基础的11个
                dayPlanBean.setSysWeeklyPlanSectionID(bean.getSysWeeklyPlanSectionID());
                dayPlanBean.setLineName(bean.getLineName());
                dayPlanBean.setVoltageClass(bean.getVoltageClass());
                dayPlanBean.setSysGridLineId(bean.getSysGridLineID());
//                dayPlanBean.setMaintainClass(bean.());
//                dayPlanBean.setInspectTowerIds(bean.getInspectTowerIds());
//                dayPlanBean.setInspectTowerNos(bean.getInspectTowerNos());
//                dayPlanBean.setSelectMonth(bean.getSelectMonth());
//                dayPlanBean.setMaintainClassName(bean.getMaintainClassName());
                dayPlanBean.setDangerDefectCount(bean.getDangerDefectCount() + "");
                dayPlanBean.setSeriousDefectCount(bean.getSeriousDefectCount() + "");
                dayPlanBean.setNormalDefectCount(bean.getNormalDefectCount() + "");
                //from lin的四个
                dayPlanBean.setTourNums(bean.getTowerNos());
                //构建Type
                ItemDetail itemDetail = new ItemDetail();
                itemDetail.setItemDetailsId(bean.getTypeOfWork());
                itemDetail.setItemsName(bean.getTypeOfWorkString());
                dayPlanBean.setTypeOfWork(itemDetail);
                //工作内容
                List<JobContent> jobContents = new ArrayList<>();
                String[] jobContentIds = bean.getJobContent().split(",");
                String[] jobContentStrs = bean.getJobContentString().split(",");
                String[] safeMeasures = bean.getSafetyMeasure().split(",");
                String[] safePrecautions = bean.getSafetyPrecaution().split(",");
                for (int i = 0; i < jobContentIds.length; i++) {
                    JobContent jobContent = new JobContent();
                    jobContent.setSysWorkContentID(jobContentIds[i]);
                    jobContent.setSafetyPrecaution(safePrecautions[i]);
                    jobContent.setWorkContent(jobContentStrs[i]);
                    jobContent.setSafetyMeasure(safeMeasures[i]);
                    jobContents.add(jobContent);
                }
                dayPlanBean.setJobContents(jobContents);
                //设置塔
                List<Tower> towers = new ArrayList<>();
                String[] towerNos = bean.getTowerNos().split(",");
                String[] towerIds = bean.getTowerIds().split(",");
                for (int i = 0; i < towerIds.length; i++) {
                    Tower tower = new Tower();
                    tower.setSysTowerID(Long.parseLong(towerIds[i]));
                    tower.setTowerNo(towerNos[i]);
                    towers.add(tower);
                }
                dayPlanBean.setTowers(towers);
                //设置班组，工作人员
                dayPlanBean.setClassName(bean.getClassName());
                dayPlanBean.setClassId(bean.getClassID());
                dayPlanBean.setResponseName(bean.getResponsiblePersonName());
                dayPlanBean.setResponseId(bean.getResponsiblePersonID());
                dayPlanBean.setWorkPeopleNames(bean.getOfficeHolderNames());
                String[] ids = bean.getOfficeHolderIDs().split(",");
                List<String> idsList = new ArrayList<>();
                for (String id : ids) {
                    idsList.add(id);
                }
                dayPlanBean.setWorkPeopleIds(idsList);
                //设置是否来自周计划
                dayPlanBean.setFromWeekPlan(true);
                //添加到头部的recycler中
                mDatas.add(dayPlanBean);//添加到数据中
                //添加到安全措施和注意事项的数据。
                mSafeDatas.add(dayPlanBean);
            }
            mAdapter.notifyDataSetChanged();
            //同时修改的还有注意事项和安全措施中的recycler数据
            mSafeAdapter.notifyDataSetChanged();
        }
    }
    //</editor-fold>

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
//        EventBus.getDefault().unregister(this);
    }

    //<editor-fold desc="显示弹窗">
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
    //</editor-fold>

    //<editor-fold desc="隐藏弹窗">
    public void missDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
    //</editor-fold>
}
