package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.ShedFilmBean;
import com.uflycn.uoperation.ui.activity.SelectTowerActivity;
import com.uflycn.uoperation.ui.fragment.hiddendanger.adapter.GreenHouseFilmListViewAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 棚膜管理-棚膜列表fragment
 * 2019.3.21 lqw
 *
 */
public class GreenHouseFilmListFragment extends DemoBaseFragment {

    @BindView(R.id.lv_gf_list)
    ListView tbListView;
    @BindView(R.id.srl_gf_refresh)
    SwipeRefreshLayout tbSwipeRefresh;
    private GreenHouseFilmListViewAdapter myListViewAdapter;
    //    private OnFragmentOpenListener onFragmentOpenListener;
    private List<ShedFilmBean> shedFilmBeans;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hd_gf_list;
    }

    @Override
    protected void initView() {
        shedFilmBeans = ShedFilmBean.initTestData();

        myListViewAdapter = new GreenHouseFilmListViewAdapter(getActivity(), 2, shedFilmBeans, new GreenHouseFilmListViewAdapter.MyListViewOnItemClickListener() {
            @Override
            public void onLeftClick() {
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
//        Intent intent = new Intent(getContext(), SpecialTourActivity.class);
//        startActivity(intent);

    }
}
