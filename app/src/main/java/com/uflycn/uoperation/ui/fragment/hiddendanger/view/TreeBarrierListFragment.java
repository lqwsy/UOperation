package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.ui.activity.SolveActivity;
import com.uflycn.uoperation.ui.fragment.hiddendanger.adapter.TreeBarrierListViewAdapter;
import com.uflycn.uoperation.ui.fragment.hiddendanger.presenter.HiddenDangerPresenter;
import com.uflycn.uoperation.ui.fragment.hiddendanger.presenter.HiddenDangerPresenterImp;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 树障列表fragment
 * 2019.3.21 lqw
 */
public class TreeBarrierListFragment extends DemoBaseFragment implements HiddenDangerView.TreeBarrierListView {

    @BindView(R.id.lv_tb_list)
    ListView tbListView;
    @BindView(R.id.srl_tb_refresh)
    SwipeRefreshLayout tbSwipeRefresh;
    @BindView(R.id.et_search_name)
    EditText etSearchName;//输入路线名/杆塔号
    private TreeBarrierListViewAdapter myListViewAdapter;
    private List<TreeDefectPointBean> treeDefectList;//树障列表
    private HiddenDangerPresenter presenter;
    private String lineId;
    private String lineName;
    private String towerId;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_hd_tb_list;
    }

    @Override
    protected void initView() {
        tbSwipeRefresh.setRefreshing(true);
        treeDefectList = new ArrayList<>();
        presenter = new HiddenDangerPresenterImp(this);
        myListViewAdapter = new TreeBarrierListViewAdapter(getActivity(), treeDefectList, new TreeBarrierListViewAdapter.MyListViewOnItemClickListener() {
            @Override
            public void onLeftClick(int position) {
                //点击更新
                startUpdateActivity(position);
            }

            @Override
            public void onRightClick(int position) {
                //点击清除树障
                startSolveActivity(position);
            }
        });
        tbListView.setAdapter(myListViewAdapter);
        tbSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                refreshData();
            }
        });

    }

    @Override
    protected void initData() {
        lineId = "";
        lineName = "";
        towerId = "";
        refreshData();
    }

    //刷新数据
    private void refreshData() {
        //根据任务是否已开启，线路巡视是否已开启，是否已经到位登记来获取数据
        lineId = "all";
        //开启了我的任务或线路巡视
        if (MyApplication.gridlineTaskStatus == 3 || MyApplication.gridlineTaskStatus == 1) {
            if (MyApplication.mLineIdNamePairs != null && MyApplication.mLineIdNamePairs.size() > 0) {
                lineId = "";
                for (String key : MyApplication.mLineIdNamePairs.keySet()) {
                    if (!StringUtils.isEmptyOrNull(key)) {
                        lineId += "," + key;
                    }
                }
                lineId = lineId.substring(lineId.indexOf(",") + 1, lineId.length());
            }
        }
        //已经到位登记，则获取登记塔的ID
        if (MyApplication.registeredTower != null) {
            lineId = "";
            towerId = getTowerId(MyApplication.registeredTower.getSysTowerID() + "");
        }

        presenter.getTreeBarrierDefectList(lineId, "TreeTask", "0", lineName, towerId);
    }


    //跳转到更新树障页面
    private void startUpdateActivity(int position) {
        if (treeDefectList == null || treeDefectList.size() <= 0) {
            return;
        }
        TreeDefectPointBean treeDefectPointBean = treeDefectList.get(position);
        Intent intent = new Intent(getActivity(), TreeBarrierUpdateActivity.class);
        intent.putExtra("treeDefectPointBean", treeDefectPointBean);
        startActivity(intent);
    }

    //跳转到消缺页面
    private void startSolveActivity(int position) {
        if (treeDefectList == null || treeDefectList.size() <= 0) {
            return;
        }
        TreeDefectPointBean channelDefectBean = treeDefectList.get(position);
        Intent intent = new Intent(getActivity(), SolveActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("channelDefectBean", channelDefectBean);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }


    @OnClick(R.id.btn_search_name)
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_name:
                lineName = "";
                towerId = "";
                //点击搜索
                String result = etSearchName.getText().toString().trim();
                if (!StringUtils.isEmptyOrNull(result)) {
                    if (StringUtils.isIntString(result)) {
                        towerId = getTowerId(result);
                    } else {
                        lineName = result;
                    }
                }
                presenter.getTreeBarrierDefectList(lineId, "TreeTask", "0", lineName, towerId);
                tbSwipeRefresh.setRefreshing(true);
                lineName = "";
                towerId = "";
                break;
            default:
                break;
        }
    }

    //获取数据成功回调
    @Override
    public void onPostSuccess(DefectInfo defectInfo) {
        if (tbSwipeRefresh != null) {
            tbSwipeRefresh.setRefreshing(false);
        }
        if (defectInfo == null) {
            return;
        }
        treeDefectList = defectInfo.getTreeDefectPoint();
        if (treeDefectList == null || treeDefectList.size() <= 0) {
            if (!StringUtils.isEmptyOrNull(towerId)) {
                ToastUtil.show("该杆塔无树障数据！");
                return;
            }
            ToastUtil.show("无数据！");
            return;
        }
        myListViewAdapter.onDataChange(treeDefectList);
        ToastUtil.show("刷新成功！");

    }

    //获取数据失败回调
    @Override
    public void onPostFail(String msg) {
        if (tbSwipeRefresh != null) {
            tbSwipeRefresh.setRefreshing(false);
        }
        ToastUtil.show(msg);
    }

    @Override
    public void onStart() {
        refreshData();
        super.onStart();
    }


    @Override
    public void onDestroy() {
        presenter.cancelAll();
        super.onDestroy();
    }

    //获取杆塔前后区段
    private String getTowerId(String towerId) {
        try {
            int value = Integer.parseInt(towerId);
            return (value - 1) + "," + value;
        } catch (Exception e) {
            Log.d("lqwtest", "getTowerId err = ", e);
        }
        return null;
    }
}
