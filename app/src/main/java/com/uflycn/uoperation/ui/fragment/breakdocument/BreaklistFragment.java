package com.uflycn.uoperation.ui.fragment.breakdocument;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.UpdateBrokenListEvent;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.main.adapter.BreakListAdapter;
import com.uflycn.uoperation.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 专档列表
 * A simple {@link Fragment} subclass.
 */
public class BreaklistFragment extends DemoBaseFragment {


    @BindView(R.id.break_list)
    ListView mBreakListView;
    private Call<BaseCallBack<List<BrokenDocument>>> mGetBrokenListCall;
    private CallbackHandler mHandler;
    private BreakListAdapter mAdapter;
    private boolean isThisFragment = false;
    @BindView(R.id.et_search_line)
    EditText mSearchLineEdit;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_breaklist;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutID(), container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        initData();
        return mRootView;
    }

    @Override
    protected void initView() {
        ArrayList<BrokenDocument> arrayList = new ArrayList<>();
        mAdapter = new BreakListAdapter(arrayList, mWeakReference.get(), R.layout.item_break_list);
        mBreakListView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mHandler = new CallbackHandler(this);
        initSwipRefreshLis();
        if (AppConstant.NET_WORK_AVAILABLE) {
            if (MyApplication.registeredTower == null) {
                getBrokenList(getLineBrokenListIds(), null, null);
            } else {
                getBrokenList(null, null, MyApplication.registeredTower.getSysTowerID() + "");
            }
        } else {
            getBrokenList("暂无数据");
        }

    }

    public void updateList(List<BrokenDocument> list) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        if (mAdapter == null) {
            mAdapter = new BreakListAdapter(list, mWeakReference.get(), R.layout.item_break_list);
            mBreakListView.setAdapter(mAdapter);
        } else {
            mAdapter.onDataChange(list);
        }
    }

    private void getBrokenList(String lineids, String lineName, String towerid) {
        //没有开启线路
        //        if (MyApplication.mLineIdNamePairs.size() == 0) {
        //            ToastUtil.show(mWeakReference.get().getResources().getString(R.string.tip_start_line_first));
        //            return;
        //        }
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
        String planDailyPlanSectionIDs = "";
        String sysPatrolExecutionID = "";
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null) {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId());
            } else {
                planDailyPlanSectionIDs = MyApplication.mDayPlanLineSb.toString();
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            sysPatrolExecutionID = MyApplication.mPlanPatrolExecutionId;
        }
        //根据 ids 获取已开启的线路的  外破 如果请求失败 或者服务器返回 错误 从数据库获取
        mGetBrokenListCall = RetrofitManager.getInstance().getService(ApiService.class).getBrokenDoucumentList(lineids, towerid, lineName, 1,
                1000, null, sysPatrolExecutionID, planDailyPlanSectionIDs);
        mGetBrokenListCall.enqueue(new Callback<BaseCallBack<List<BrokenDocument>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<BrokenDocument>>> call, Response<BaseCallBack<List<BrokenDocument>>> response) {
                if (mSwipeRefreshLayout != null)
                    mSwipeRefreshLayout.setRefreshing(false);
                Message msg = Message.obtain();
                if (response.body() == null) {
                    toastNetWorkErr(null);
                    return;
                }
                if (response.body().getCode() == 0) {
                    msg.what = 0;
                    msg.obj = response.body().getMessage();
                } else {
                    msg.what = 1;
                    msg.obj = response.body().getData();
                }
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<BrokenDocument>>> call, Throwable t) {//如果网络获取失败 就从数据库拿
                Message msg = Message.obtain();
                msg.what = 3;
                msg.obj = t.getMessage();
                mHandler.sendMessageDelayed(msg, 100);
            }
        });
    }

    public void toastNetWorkErr(String message) {
        //        ToastUtil.show(mWeakReference.get(), "巡视已关闭,请重新开启巡视");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick({R.id.btn_search_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_line:
                String lineName = mSearchLineEdit.getText().toString();
                getBrokenList(getLineBrokenListIds(), lineName, null);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetBrokenListCall != null && !mGetBrokenListCall.isCanceled()) {
            mGetBrokenListCall.cancel();
        }
        mHandler.removeCallbacksAndMessages(null);
    }


    private static class CallbackHandler extends Handler {
        private Reference<BreaklistFragment> mReference;

        public CallbackHandler(BreaklistFragment fragment) {
            mReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mReference.get().toastNetWorkErr((String) msg.obj);
                    break;
                case 1:
                    mReference.get().updateList((List<BrokenDocument>) msg.obj);
                    break;
                case 3:
                    mReference.get().getBrokenList((String) msg.obj);
                    break;

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.registeredTower == null) {
            getBrokenList(getLineBrokenListIds(), null, null);
        } else {
            getBrokenList(null, null, MyApplication.registeredTower.getSysTowerID() + "");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (MyApplication.registeredTower == null) {
                getBrokenList(getLineBrokenListIds(), null, null);
            } else {
                getBrokenList(null, null, MyApplication.registeredTower.getSysTowerID() + "");
            }
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter && !isThisFragment) {
            isThisFragment = true;
            if (MyApplication.registeredTower == null) {
                getBrokenList(getLineBrokenListIds(), null, null);
            } else {
                getBrokenList(null, null, MyApplication.registeredTower.getSysTowerID() + "");
            }
        } else {
            isThisFragment = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
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

    @Subscribe
    public void onMainThread(BaseMainThreadEvent event) {
        if (event instanceof UpdateBrokenListEvent) {
            if (MyApplication.registeredTower == null) {
                getBrokenList(getLineBrokenListIds(), null, null);
            } else {
                getBrokenList(null, null, MyApplication.registeredTower.getSysTowerID() + "");
            }
        }
    }

    private String getLineBrokenListIds() {
        StringBuilder sb = new StringBuilder();
        if (MyApplication.registeredTower == null) {
            if (MyApplication.gridlines.size() > 0) {
                for (Map.Entry entry : MyApplication.gridlines.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
        }
        return sb.toString();
    }


    private List<String> getLineNames() {
        List<String> lineNames = new ArrayList<>();
        if (MyApplication.mLineIdNamePairs.size() > 0) {
            for (Map.Entry entry : MyApplication.mLineIdNamePairs.entrySet()) {
                lineNames.add((String) entry.getValue());
            }
        }
        return lineNames;
    }

    private void initSwipRefreshLis() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //                if (MyApplication.currentNearestTower != null) {
                if (MyApplication.registeredTower == null) {
                    getBrokenList(getLineBrokenListIds(), null, null);
                } else {
                    getBrokenList(null, null, MyApplication.registeredTower.getSysTowerID() + "");
                }
                //                } else {
                //                    getBrokenList(null);
                //                    mSwipeRefreshLayout.setRefreshing(false);
                //                }
            }
        });
    }


    private void getBrokenList(String message) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        List<BrokenDocument> list = null;
        if (MyApplication.registeredTower == null) {
            list = BrokenDocumentDBHelper.getInstance().getAllByLineNames(getLineNames());
        } else {
            list = BrokenDocumentDBHelper.getInstance().getAllByTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
        }
        if (list == null || list.size() == 0) {
            ToastUtil.show("暂无数据");
        } else {
            Collections.reverse(list);
            updateList(list);
        }
    }
}
