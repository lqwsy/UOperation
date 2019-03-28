package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.HiddenDangerBean;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.ui.fragment.hiddendanger.adapter.HdmListViewAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 隐患列表fragment
 * 2019.3.21 lqw
 */
public class HiddenDangerManagerListFragment extends DemoBaseFragment {

    @BindView(R.id.lv_hdm_list)
    ListView tbListView;
    @BindView(R.id.srl_hdm_refresh)
    SwipeRefreshLayout tbSwipeRefresh;
    private HdmListViewAdapter myListViewAdapter;
    //    private OnFragmentOpenListener onFragmentOpenListener;
    private List<HiddenDangerBean> testBeanList;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hd_hdm_list;
    }

    @Override
    protected void initView() {
        testBeanList = HiddenDangerBean.initTestData();
        myListViewAdapter = new HdmListViewAdapter(getActivity(), 2, testBeanList, new HdmListViewAdapter.MyListViewOnItemClickListener() {
            @Override
            public void onLeftClick() {
//                onFragmentOpenListener.onFragmentChange();
                startUpdateActivity();
            }

            @Override
            public void onRightClick() {

            }
        });
        tbListView.setAdapter(myListViewAdapter);

        tbSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }


    @Override
    protected void initData() {

    }

    private void startUpdateActivity(){

    }
}
