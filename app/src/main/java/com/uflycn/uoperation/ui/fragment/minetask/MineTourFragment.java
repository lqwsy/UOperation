package com.uflycn.uoperation.ui.fragment.minetask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectCount;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.StartedTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.StopAllTaskEvent;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.StartedTaskAdapter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.uflycn.uoperation.widget.SimpleDlg;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的巡视
 * （山东-线路巡视）
 * Created by xiaoyehai on 2017/9/7.
 */
public class MineTourFragment extends Fragment {

    @BindView(R.id.listview)
    SwipeMenuListView mListView;
    @BindView(R.id.et_search_line)
    EditText mSearchEditText;
    @BindView(R.id.btn_all_stop)
    Button mStopAllBtn;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private StartedTaskAdapter mAdapter;
    private Reference<Activity> mReference;
    private ProgressDialog mProgressDialog;

    private List<StartedTask> mStartedTasks;
    private SimpleDlg mSimpleDlg;

    private Call<BaseCallBack<List<DefectCount>>> mCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_tour, container, false);
        ButterKnife.bind(this, view);
        mReference = new WeakReference<Activity>(getActivity());
        if (ProjectManageUtil.isShanDong()) {
            mTvTitle.setText("线路巡视");
        }
        initListView();
        getAllStartedLines();
        return view;
    }

    private void initListView() {
        mAdapter = new StartedTaskAdapter(new ArrayList<StartedTask>(), mReference.get(), R.layout.item_mine_tour);
        initListViewCreater();
        mListView.setAdapter(mAdapter);
    }

    private void initDefectCount(String s, final List<StartedTask> startedTasks) {
        if (!AppConstant.NET_WORK_AVAILABLE)
            return;
        mCall = RetrofitManager.getInstance().getService(ApiService.class).getLineDefectCount(s, 0, 0, true);
        mCall.enqueue(new Callback<BaseCallBack<List<DefectCount>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<DefectCount>>> call, Response<BaseCallBack<List<DefectCount>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    for (int i = 0; i < startedTasks.size(); i++) {
                        List<DefectCount> list = response.body().getData();
                        for (int j = 0; j < list.size(); j++) {
                            if (startedTasks.get(i).getLineId() == list.get(j).getSysGridLineID()) {
                                startedTasks.get(i).setDefectCount(list.get(j).getDefectCount());
                                break;
                            }
                        }
                    }
                    mAdapter.onDataChange(startedTasks);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<DefectCount>>> call, Throwable t) {

            }
        });
    }

    private void getAllStartedLines() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mReference.get());
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();

        StringBuilder sb = new StringBuilder();

        mStartedTasks = new ArrayList<>();
        for (Map.Entry entry : MyApplication.gridlines.entrySet()) {
            if (((Gridline) entry.getValue()).getSysGridLineID() == -1) {
                continue;
            }
            StartedTask startedTask = new StartedTask();
            startedTask.setFromGridLine((Gridline) entry.getValue());
            startedTask.setVClass(ItemDetailDBHelper.getInstance().getItem("电压等级", startedTask.getVoltageClass()));
            mStartedTasks.add(startedTask);
            sb.append(startedTask.getLineId());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        initDefectCount(sb.toString(), mStartedTasks);

        mAdapter.onDataChange(mStartedTasks);
        mProgressDialog.dismiss();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (mCall != null && mCall.isCanceled()) {
            mCall.cancel();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getAllStartedLines();
        }
    }


    public void stopInspection(final StartedTask task) {
        ((MainActivity) mReference.get()).removeLineByID(task.getLineId());
        mStartedTasks.remove(task);
        MyApplication.gridlines.remove(task.getLineId());
        if (MyApplication.gridlines.size()==0){
            MyApplication.gridlineTaskStatus = 0;
        }
        mAdapter.onDataChange(mStartedTasks);
    }

    @OnClick({R.id.btn_all_stop})
    public void stopAllTask() {
        showStopDialog();
    }

    /**
     * 确认是否全部停止弹窗
     */
    private void showStopDialog() {
        if (mSimpleDlg == null) {
            SimpleDlg.Builder builder = new SimpleDlg.Builder();
            mSimpleDlg = builder.create(getContext());
            builder.setContentText("是否停止全部巡视?");
            builder.setOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.dlg_btn_left:
                            mSimpleDlg.dismiss();
                            break;
                        case R.id.dlg_btn_right:
                            MyApplication.gridlineTaskStatus = 0;
                            MyApplication.gridlines.clear();
                            mAdapter.onDataChange(new ArrayList<StartedTask>());
                            EventBus.getDefault().post(new StopAllTaskEvent());
                            mSimpleDlg.dismiss();
                            break;
                    }

                }
            });
        }
        mSimpleDlg.show();

    }

    @OnClick(R.id.iv_open_close_drawer)
    public void doClick(View v) {
        ((MainActivity) getActivity()).openLeftMenu();
    }


    @OnClick({R.id.btn_search_line})
    public void clickToSearch(View view) {
        seachList();
    }

    private void initListViewCreater() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem uploadItem = new SwipeMenuItem(
                        mReference.get());
                // set item background
                uploadItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0xcb,
                        0x5b)));
                // set item width
                uploadItem.setWidth(UIUtils.dp2px(90));
                // set a icon
                uploadItem.setTitle("继续任务");
                uploadItem.setTitleSize(18);
                uploadItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(uploadItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        mReference.get());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xef,
                        0x32, 0x49)));
                deleteItem.setWidth(UIUtils.dp2px(90));
                deleteItem.setTitle("停止任务");
                deleteItem.setTitleSize(18);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        //    绑定左滑和删除事件
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ((MainActivity) mReference.get()).changeToMainFragment(mAdapter.getItem(position).getLineId() + "");
                        break;
                    case 1:
                        mListView.smoothCloseMenu();
                        stopInspection(mAdapter.getItem(position));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }


    private void seachList() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mReference.get());
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
        String lineName = mSearchEditText.getText().toString();
        if (lineName == null || lineName.equalsIgnoreCase("")) {
            getAllStartedLines();
        } else {
            List<StartedTask> datas = new ArrayList<>();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                StartedTask task = mAdapter.getItem(i);
                if (task.getLineName().contains(lineName)) {
                    datas.add(task);
                }
            }
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mAdapter.onDataChange(datas);
        }
    }

}
