package com.uflycn.uoperation.ui.fragment.worksheet.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.adapter.WorkSheetListAdapter;
import com.uflycn.uoperation.ui.fragment.worksheet.presenter.WorkSheetPresenterImpl;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ToastUtil;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/12.
 */
public class WorkSheetFragment extends Fragment {

    @BindView(R.id.rg_check_type)
    RadioGroup mrgCheckType;

    @BindView(R.id.lv_work_order)
    ListView mLvWorkSheet;

    @BindView(R.id.sw_lv_work_order)
    SwipeRefreshLayout mSwWorkSheet;

    @BindView(R.id.rb_class_task)
    RadioButton rbClassTask;


    private Reference<Activity> mRef;
    private List<WorkSheetTask> mWorkSheetTasks;
    private WorkSheetListAdapter mWorkSheetListAdapter;
    private WorkSheetPresenterImpl mWorkSheetPresenterImpl;

    private IntentFilter intentFilter;
    private ReceiveMsgBroadcast receiveMsgBroadcast;
    private LocalBroadcastManager localBroadcastManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_order, container, false);
        ButterKnife.bind(this, view);
        mRef = new WeakReference<Activity>(getActivity());

        initView();
        initData();

        localBroadcastManager = LocalBroadcastManager.getInstance(this.getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.WORK_SHEET_NUM_DEL);
        intentFilter.addAction(AppConstant.WORK_SHEET_NUM_ADD);
        receiveMsgBroadcast = new ReceiveMsgBroadcast();
        localBroadcastManager.registerReceiver(receiveMsgBroadcast, intentFilter);
        //LoadListData();
        mSwWorkSheet.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //加载数据
                LoadListData();
            }
        });

        return view;
    }

    private void initView(){
//        mProgressDialog = new ProgressDialog(mRef.get());
//        mProgressDialog.setMessage("正在加载工单列表...");
//        mProgressDialog.setCanceledOnTouchOutside(false);
        rbClassTask.setChecked(true);
        mrgCheckType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_class_task:
                            chooseData(1);
                        break;
                    case R.id.rb_self_task:
                            chooseData(2);
                        break;
                }
            }
        });
    }

    private void initData() {

        mWorkSheetPresenterImpl =new WorkSheetPresenterImpl(this);
        mWorkSheetTasks = new ArrayList<>();
        //mWorkSheetTasks = WorkSheetTaskDBHelper.getInstance().getUploadList();
        mWorkSheetListAdapter = new WorkSheetListAdapter(sort(mWorkSheetTasks),mRef.get(),R.layout.item_work_order);
        mLvWorkSheet.setAdapter(mWorkSheetListAdapter);
        LoadListData();

    }

    private List<WorkSheetTask> sort(List<WorkSheetTask> workSheetTask){
        Collections.sort(workSheetTask, new Comparator<WorkSheetTask>() {
            @Override
            public int compare(WorkSheetTask o1, WorkSheetTask o2) {
                //降序
                return o2.getTaskNo().compareTo(o1.getTaskNo());
            }
        });
        return workSheetTask;
    }

    private void LoadListData() {
        if (mSwWorkSheet == null && !mSwWorkSheet.isRefreshing()){
            mSwWorkSheet.setRefreshing(true);
        }
        mWorkSheetPresenterImpl.getWorkSheetList();
    }

    public void chooseData(int msg){

        List<WorkSheetTask> query_WorkSheetTask = new ArrayList<>();
        if (msg == 1){
            for (WorkSheetTask workSheetTask : mWorkSheetTasks){
                if (workSheetTask.getReceiverId() == null){
                    query_WorkSheetTask.add(workSheetTask);
                    continue;
                }
                if (AppConstant.currentUser == null){
                    ToastUtil.show("用户信息过期,请重新登陆");
                    return;
                }
                if (!workSheetTask.getReceiverId().equals(AppConstant.currentUser.getUserId())){
                    query_WorkSheetTask.add(workSheetTask);
                }
            }
        }else{
            for (WorkSheetTask workSheetTask : mWorkSheetTasks){
                if (workSheetTask.getReceiverId() == null){
                    continue;
                }
                if (workSheetTask.getReceiverId().equals(AppConstant.currentUser.getUserId())){
                    query_WorkSheetTask.add(workSheetTask);
                }
            }
        }
        if (mSwWorkSheet != null && mSwWorkSheet.isRefreshing()){
            mSwWorkSheet.setRefreshing(false);
        }
        if (mWorkSheetListAdapter != null){
            mWorkSheetListAdapter.onDataChange(sort(query_WorkSheetTask));
        }
        mWorkSheetListAdapter.notifyDataSetChanged();
    }

    public void refreshData(List<WorkSheetTask> workSheetTasks,String msg){
        List<WorkSheetTask> query_WorkSheetTask = new ArrayList<>();
        if (!msg.equals("workSheetTasks")){
            //查询
            for (WorkSheetTask workSheetTask : mWorkSheetTasks){
                if (workSheetTask.getLineName().contains(msg)){
                    query_WorkSheetTask.add(workSheetTask);
                }
            }
            workSheetTasks = sort(query_WorkSheetTask);
        }else{
            mWorkSheetTasks = sort(workSheetTasks);
        }
        if (rbClassTask.isChecked()){
            chooseData(1);
        }else{
            chooseData(2);
        }
//        if (mSwWorkSheet != null && mSwWorkSheet.isRefreshing()){
//            mSwWorkSheet.setRefreshing(false);
//        }
//        if (mWorkSheetListAdapter != null){
//            mWorkSheetListAdapter.onDataChange(workSheetTasks);
//        }
//        mWorkSheetListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_open_close_drawer})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadListData();
    }

    class ReceiveMsgBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstant.WORK_SHEET_NUM_ADD)){
                LoadListData();
            }
        }
    }
}
