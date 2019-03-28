package com.uflycn.uoperation.ui.main.view;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.LoginService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.service.CheckService;
import com.uflycn.uoperation.service.OperationService;
import com.uflycn.uoperation.service.UpdateService;
import com.uflycn.uoperation.service.UploadRecordService;
import com.uflycn.uoperation.ui.fragment.dayplan.view.DayPlanFragment;
import com.uflycn.uoperation.ui.fragment.document.view.DocumentFragment;
import com.uflycn.uoperation.ui.fragment.minetask.MineTourFragment;
import com.uflycn.uoperation.ui.fragment.myMission.view.MyMissionFragment;
import com.uflycn.uoperation.ui.fragment.mytour.view.MyTourFragment;
import com.uflycn.uoperation.ui.fragment.plantask.view.PlanTaskFragment;
import com.uflycn.uoperation.ui.fragment.setting.view.SettingFragment;
import com.uflycn.uoperation.ui.fragment.temptask.view.TempTaskFragment;
import com.uflycn.uoperation.ui.fragment.worksheet.view.WorkSheetFragment;
import com.uflycn.uoperation.util.AndroidWorkaround;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.CustomRadioButton;
import com.uflycn.uoperation.widget.SimpleDlg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;

    @BindView(R.id.radiogroup)
    RadioGroup mRadioGroup;

    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.rb_my_tour)
    public RadioButton mRbtnInspection;
    @BindView(R.id.rb_temp_task)
    CustomRadioButton mRbtnTaskplan;
    @BindView(R.id.rb_work_order)
    CustomRadioButton mRbtnWorkOrder;
    @BindView(R.id.rb_day_plan)
    CustomRadioButton mRbDayPlan;
    @BindView(R.id.rb_line_list)
    CustomRadioButton mRbLineList;
    @BindView(R.id.rb_tour)
    CustomRadioButton mRbTour;
    @BindView(R.id.rb_plan_task)
    CustomRadioButton mRbPlanTask;
    private List<Fragment> mFragmentList;
    private Fragment previousFragment;
    private int position;
    private ServiceConnection mServiceConnection;
    public OperationService mOperationService;
    private SimpleDlg mSimpleDlg;

    private QBadgeView mBadgeView;
    private QBadgeView mBadgeViewWorkOrder;
    private Integer mCountTempTask = 0;
    private Integer mCountWorkSheetTask = 0;
    private Handler handler = new Handler();

    private IntentFilter intentFilter;
    private ReceiveMsgBroadcast receiveMsgBroadcast;
    private LocalBroadcastManager localBroadcastManager;

    private List<Integer> mIdList = new ArrayList<>();

    private boolean isBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        AppConstant.ScreenHeight = dm.heightPixels;
        AppConstant.ScreenWidth = dm.widthPixels;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initContent();
        bindService();
        CountAsycTask countAsycTask = new CountAsycTask();
        countAsycTask.execute();
        if (AppConstant.currentUser != null) {
            tvUserName.setText(AppConstant.currentUser.getRealName());
        }
        //解决虚拟键盘屏幕适配
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.AssistActivity(findViewById(android.R.id.content));
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.TEMP_TASK_NUM_DEL);
        intentFilter.addAction(AppConstant.TEMP_TASK_NUM_ADD);
        intentFilter.addAction(AppConstant.TEMP_TASK_NET_CHANGE);
        intentFilter.addAction(AppConstant.WORK_SHEET_NUM_ADD);
        intentFilter.addAction(AppConstant.WORK_SHEET_NUM_DEL);
        receiveMsgBroadcast = new ReceiveMsgBroadcast();
        localBroadcastManager.registerReceiver(receiveMsgBroadcast, intentFilter);

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    //<editor-fold desc="退出登陆">
    /**
     * 退出登陆
     */
    public void logout() {
        reset();//清空
        if (isBind && mServiceConnection != null) {
            isBind = false;
            unbindService(mServiceConnection);
        }
        stopService(new Intent(MainActivity.this, OperationService.class));

        RetrofitManager.getInstance().getService(LoginService.class).doLogout("").enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response == null || response.body() == null) {
                    return;
                }
                BaseCallBack<String> body = response.body();

                finish();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                finish();
            }
        });
    }
    //</editor-fold>

    private void reset() {
        AppConstant.CURRENT_INPLACE = null;
        MyApplication.registeredTower = null;
        MyApplication.mCurrentDayPlan = null;
        MyApplication.currentNearestTower = null;
        MyApplication.nearSecondTower = null;//当前第二近的塔，如果为空表示 在塔20米以内
        MyApplication.crossSecondTower = null;
        MyApplication.nearestDistance = 0;//当前最近杆塔的最近距离
        MyApplication.nearSecondDistance = 0;//第二近杆塔的距离
        MyApplication.mAllTowersInMap.clear();
        MyApplication.mLineIdNamePairs.clear();
        MyApplication.gridlines.clear();
        MyApplication.mTempTowerMap.clear();
        MyApplication.mTempLineMap.clear();
        MyApplication.mDayPlanLineMap.clear();
        MyApplication.mOpenDayPlanLineMap.clear();
        MyApplication.mWorkTypeMap.clear();

        stopService(new Intent(this, CheckService.class));
    }


    @OnClick(R.id.btn_logout)
    public void clickLogout(View view) {
        closeleftMenu();
        if (mSimpleDlg == null) {
            SimpleDlg.Builder builder = new SimpleDlg.Builder();
            mSimpleDlg = builder.create(this);
            builder.setContentText("确定退出登录?");
            builder.setOnclickListener(this);
        }
        mSimpleDlg.show();
    }

    /**
     * 初始化主页面fragment
     */
    private void initContent() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new MyTourFragment());
        mIdList.add(R.id.rb_my_tour);
        mFragmentList.add(new PlanTaskFragment());
        mIdList.add(R.id.rb_plan_task);
        mFragmentList.add(new TempTaskFragment());
        mIdList.add(R.id.rb_temp_task);
        mFragmentList.add(new MineTourFragment());
        mIdList.add(R.id.rb_tour);
        mFragmentList.add(new SettingFragment());
        mIdList.add(R.id.rb_setting);
        mFragmentList.add(new DocumentFragment());
        mIdList.add(R.id.rb_document);
        mFragmentList.add(new WorkSheetFragment());
        mIdList.add(R.id.rb_work_order);
        mFragmentList.add(new DayPlanFragment());
        mIdList.add(R.id.rb_day_plan);
        mFragmentList.add(new MyMissionFragment());
        mIdList.add(R.id.rb_line_list);

        mBadgeView = new QBadgeView(this);
        mBadgeView.bindTarget(mRbtnTaskplan)
                .setBadgeNumber(mCountTempTask)
                .setBadgeGravity(Gravity.END | Gravity.TOP)
                .setBadgePadding(5, true)
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                    }
                });
        if (!UpdateService.APP_NAME.contains("山东")) {
            mBadgeViewWorkOrder = new QBadgeView(this);
            mBadgeViewWorkOrder.bindTarget(mRbtnWorkOrder)
                    .setBadgeNumber(mCountTempTask)
                    .setBadgeGravity(Gravity.END | Gravity.TOP)
                    .setBadgePadding(5, true)
                    .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                        @Override
                        public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        }
                    });
            mBadgeViewWorkOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRadioGroup.check(R.id.rb_work_order);
                }
            });
        }
        mBadgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroup.check(R.id.rb_temp_task);
                //设置为0既不可见
                //mBadgeView.setBadgeNumber(0);
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //计划任务 工单任务 我得巡视
                if (MyApplication.gridlineTaskStatus == 2 && (checkedId == R.id.rb_plan_task || checkedId == R.id.rb_tour || checkedId == R.id.rb_work_order)) {
                    ToastUtil.show("请先关闭每日计划");
                    return;
                }
                if (ProjectManageUtil.isShanDong()) {
                    if (MyApplication.gridlineTaskStatus == 3 && (checkedId == R.id.rb_line_list || checkedId == R.id.rb_plan_task)) {
                        ToastUtil.show("请先关闭我的任务");
                        return;
                    }
                }

                //日计划
                if (MyApplication.gridlineTaskStatus == 1 && checkedId == R.id.rb_day_plan) {
                    ToastUtil.show("请先关闭计划线路或附近杆塔");
                    return;
                }

                if (ProjectManageUtil.isShanDong()) {
                    if (MyApplication.gridlineTaskStatus == 1 && checkedId == R.id.rb_tour) {
                        ToastUtil.show("请先关闭计划线路或附近杆塔");
                        return;
                    }
                }


                if (UpdateService.APP_NAME.contains("山东")) {
                    mRbPlanTask.setText("线路巡视");
                    mRbTour.setText("我的任务");
                    mRbLineList.setVisibility(View.VISIBLE);
                    mRbtnWorkOrder.setVisibility(View.GONE);
                    mRbDayPlan.setVisibility(View.GONE);
                    if (checkedId == R.id.rb_line_list) {
                        position = 1;
                    } else if (checkedId == R.id.rb_plan_task) {
                        position = 3;
                    } else if (checkedId == R.id.rb_tour) {
                        position = 8;
                    } else {
                        position = mIdList.indexOf(checkedId);
                    }
                } else {
                    position = mIdList.indexOf(checkedId);
                }
                closeleftMenu();
                Fragment currentFragment = mFragmentList.get(position);
                switchFragment(previousFragment, currentFragment);
            }
        });

        mRadioGroup.check(R.id.rb_my_tour);
        if (UpdateService.APP_NAME.contains("赣州")) {
            mRbDayPlan.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 切换fragment
     *
     * @param from
     * @param to
     */
    private void switchFragment(Fragment from, Fragment to) {
        if (from != to) {
            previousFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    //这里不用commit的原因是 ：当在其他页面点击notification时会
                    //出现：Can not perform this action after onSaveInstanceState
                    transaction.add(R.id.fl_content, to).commitAllowingStateLoss();
                }
            } else {
                if (from != null) {
                    transaction.hide(from);
                }
                if (to != null) {
                    transaction.show(to).commitAllowingStateLoss();
                }
            }
        }
    }

    /**
     * 关闭侧滑
     */
    public void closeleftMenu() {
        mDrawerLayout.closeDrawer(rlMenu);
    }

    /**
     * 打开侧滑
     */
    public void openLeftMenu() {
        mDrawerLayout.openDrawer(rlMenu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //点击通知栏进去是执行这个方法
        getNotify(intent);
        setIntent(intent);
    }

    private void getNotify(Intent intent) {
        String value = intent.getStringExtra("toValue");
        if (!TextUtils.isEmpty(value)) {
            switch (value) {
                case AppConstant.TEMP_TASK:
                    mRadioGroup.check(R.id.rb_temp_task);
                    break;
                case AppConstant.WORK_SHEET:
                    mRadioGroup.check(R.id.rb_work_order);
                    break;
            }
        }
        String Line = intent.getStringExtra("LineID");
        if (!TextUtils.isEmpty(Line)) {
            Gridline gridline = GridLineDBHelper.getInstance().getLine(Integer.valueOf(Line));
            if (gridline == null) {
                ToastUtil.show("该线路不存在");
            } else {
                if (!MyApplication.mLineIdNamePairs.containsKey(Line + "") && !MyApplication.gridlines.containsKey(Line)) {
                    MyApplication.mLineIdNamePairs.put(Line + "", gridline.getLineName());
                    MyApplication.gridlines.put(Integer.valueOf(Line), gridline);
                    mRbtnInspection.performClick();
                    addTowersById(Integer.valueOf(Line));
                } else {
                    ToastUtil.show("线路已经打开");
                    mRadioGroup.check(R.id.rb_my_tour);
                }
            }
        }
        intent.putExtra("LineID", "");
        intent.putExtra("toValue", "");
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        getNotify(getIntent());
        super.onResume();
    }

    /**
     * 添加线路到地图
     */
    public void addTowersById(int lineid) {
        ((MyTourFragment) mFragmentList.get(0)).addTowerByid(lineid);
    }

    private void bindService() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mOperationService = ((OperationService.OperationBinder) service).getOprationService();
                mOperationService.setDistanceCallBack((MyTourFragment) mFragmentList.get(0));
                //                mOperationService.stopAllTask();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        isBind = bindService(new Intent(this, OperationService.class), mServiceConnection, BIND_AUTO_CREATE);

        startService(new Intent(this, UploadRecordService.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mOperationService.stopAllTask();
        if (isBind) {
            isBind = false;
            unbindService(mServiceConnection);
        }
    }

    public void removeLineByID(int lineid) {
        ((MyTourFragment) mFragmentList.get(0)).removeGridLine(lineid);
    }


    public void getSecondNearTower() {
        if (MyApplication.isRegisterAuto == false) {
            mOperationService.getSecondNearTower(MyApplication.registeredTower);
        } else {
            mOperationService.getSecondNearTower(MyApplication.currentNearestTower);
        }

    }

    public void changeToMainFragment(String lineId) {
        mRadioGroup.check(R.id.rb_my_tour);
        ((MyTourFragment) mFragmentList.get(0)).locateAtTower(lineId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRadioGroup.getCheckedRadioButtonId()!=R.id.rb_my_tour){
                mRadioGroup.check(R.id.rb_my_tour);
                return true;
            }else {
                if (mSimpleDlg == null) {
                    SimpleDlg.Builder builder = new SimpleDlg.Builder();
                    mSimpleDlg = builder.create(this);
                    builder.setContentText("确定退出登录?");
                    builder.setOnclickListener(this);
                }
                mSimpleDlg.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DayPlanFragment fragment = (DayPlanFragment) mFragmentList.get(7);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlg_btn_left:
                mSimpleDlg.dismiss();
                break;
            case R.id.dlg_btn_right:
                logout();
                finish();
                mSimpleDlg.dismiss();
                break;
        }
    }

    class CountAsycTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Call<BaseCallBack<List<TempTask>>> tempTaskCall = RetrofitManager.getInstance().getService(ApiService.class).getTempTaskList("");
            tempTaskCall.enqueue(new Callback<BaseCallBack<List<TempTask>>>() {
                @Override
                public void onResponse(Call<BaseCallBack<List<TempTask>>> call, Response<BaseCallBack<List<TempTask>>> response) {
                    mCountTempTask = 0;
                    if (response == null || response.body() == null) {
                        return;
                    }
                    if (response.body().getCode() == 0) {
                    } else {
                        List<TempTask> tempTasks = response.body().getData();
                        for (int i = 0; i < tempTasks.size(); i++) {
                            if (tempTasks.get(i).getReadState().equals("未接收"))
                                mCountTempTask += 1;
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseCallBack<List<TempTask>>> call, Throwable t) {
                    //ToastUtil.show(MyApplication.getContext(), "网络错误！");
                }
            });
            Call<BaseCallBack<List<WorkSheetTask>>> mWorkSheetTask = RetrofitManager.getInstance().getService(ApiService.class).getWorksheetTask();
            mWorkSheetTask.enqueue(new Callback<BaseCallBack<List<WorkSheetTask>>>() {
                @Override
                public void onResponse(Call<BaseCallBack<List<WorkSheetTask>>> call, Response<BaseCallBack<List<WorkSheetTask>>> response) {
                    if (response == null || response.body() == null) {
                        return;
                    }
                    if (response.body().getCode() == 1 && response.body().getData() != null) {
                        List<WorkSheetTask> workSheetTasks = response.body().getData();
                        mCountWorkSheetTask = workSheetTasks.size();
                    }
                }

                @Override
                public void onFailure(Call<BaseCallBack<List<WorkSheetTask>>> call, Throwable t) {
                }
            });

            //有无人机时测试失败  网络慢
            //18/1/29 没无人机的时候也会失败，但是大几率会成功 失败是偶尔现象
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBadgeView.setBadgeNumber(mCountTempTask);
                    if (0 == mCountTempTask) {
                        mBadgeView.hide(true);
                    }
                    if (!UpdateService.APP_NAME.contains("山东")) {
                        mBadgeViewWorkOrder.setBadgeNumber(mCountWorkSheetTask);
                        if (0 == mCountWorkSheetTask) {
                            mBadgeViewWorkOrder.hide(true);
                        }
                    }
                }
            }, 3000);//3秒后执行Runnable中的run方法
            return null;
        }
    }

    class ReceiveMsgBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, final Intent intent) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 不睡的话有几率收不到广播
                        Thread.sleep(500);
                        if (intent.getAction().equals(AppConstant.TEMP_TASK_NUM_DEL)) {
                            mCountTempTask -= 1;
                        } else if (intent.getAction().equals(AppConstant.TEMP_TASK_NUM_ADD)) {
                            mCountTempTask += 1;
                        } else if (intent.getAction().equals(AppConstant.WORK_SHEET_NUM_ADD)) {
                            mCountWorkSheetTask++;
                        } else if (intent.getAction().equals(AppConstant.WORK_SHEET_NUM_DEL)) {
                            mCountWorkSheetTask--;
                        } else if (intent.getAction().equals(AppConstant.TEMP_TASK_NET_CHANGE)) {
                            CountAsycTask countAsycTask = new CountAsycTask();
                            countAsycTask.execute();
                        }


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mBadgeView.setBadgeNumber(mCountTempTask);
                                if (0 == mCountTempTask) {
                                    mBadgeView.hide(false);
                                }
                                if (!UpdateService.APP_NAME.contains("山东")) {
                                    mBadgeViewWorkOrder.setBadgeNumber(mCountWorkSheetTask);
                                    if (0 == mCountWorkSheetTask) {
                                        mBadgeViewWorkOrder.hide(false);
                                    }
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

}



