package com.uflycn.uoperation.ui.fragment.temptask.view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.BaseFragment;
import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.adapter.TempTaskAdapter;
import com.uflycn.uoperation.ui.fragment.temptask.contract.TempTaskContract;
import com.uflycn.uoperation.ui.fragment.temptask.presenter.TempTaskPresenter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.widget.SimpleDlg;
import com.uflycn.uoperation.widget.ViewTempTaskDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 临时通知
 * Created by xiaoyehai on 2017/9/4.
 */
public class TempTaskFragment extends BaseFragment<TempTaskPresenter> implements TempTaskContract.TempTaskView {

    @BindView(R.id.et_search_line)
    EditText etSearchLine;

    @BindView(R.id.btn_search_line)
    Button btnSearchLine;

    @BindView(R.id.listview)
    ListView mListView;

    private TempTaskAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private SimpleDlg mSimpleDlg;
    private IntentFilter intentFilter;
    private ReceiveMsgBroadcast receiveMsgBroadcast;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_temp_task;
    }

    @Override
    public TempTaskPresenter getPresenter() {
        return new TempTaskPresenter();
    }

    @Override
    protected void initView() {
        mAdapter = new TempTaskAdapter(new ArrayList<TempTask>(), mRef.get(), R.layout.item_temp_task);
        mAdapter.setTaskView(this);
        mListView.setAdapter(mAdapter);

        mProgressDialog = new ProgressDialog(this.getContext());
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        initSwipRefreshLis();


    }

    @Override
    protected void initData() {
        mProgressDialog.show();
        mPresenter.getTempTaskList("");
        mProgressDialog.dismiss();
    }

    @OnClick({R.id.iv_open_close_drawer})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
                break;
        }
    }

    @OnClick({R.id.btn_search_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_line:
                String lineName = etSearchLine.getText().toString();
                mPresenter.getTempTaskList(lineName);
                break;
        }
    }

    @Override
    public void returnTempTaskList(List<TempTask> tempTaskInfoList) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (tempTaskInfoList == null) {
            return;
        }
        Collections.sort(tempTaskInfoList, new Comparator<TempTask>(){
            public int compare(TempTask o1, TempTask o2) {

                if(o1.getSysMessageInfoId() < o2.getSysMessageInfoId()){
                    return 1;
                }
                if(o1.getSysMessageInfoId() == o2.getSysMessageInfoId()){
                    return 0;
                }
                return -1;
            }
        });
        mAdapter.onDataChange(tempTaskInfoList);
    }

    @Override
    public void viewDetail(final TempTask tempTaskInfo) {
        //打开新的窗体显示任务详情
        if (tempTaskInfo.getReadState() != "2") {
            getPresenter().updateReadStatus(tempTaskInfo.getSysMessageInfoId());
        }
        final ViewTempTaskDialog dialog = new ViewTempTaskDialog(mRef.get());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setData(tempTaskInfo);
        mAdapter.notifyDataSetChanged();
        dialog.show();

    }


    private void initSwipRefreshLis() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getTempTaskList("");
            }
        });
    }

    class ReceiveMsgBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstant.TEMP_TASK_NUM_ADD)){
                mPresenter.getTempTaskList("");
            }
        }
    }
}
