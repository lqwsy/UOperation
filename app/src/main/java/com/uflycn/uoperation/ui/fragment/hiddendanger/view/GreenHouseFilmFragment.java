package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.ui.fragment.hiddendanger.adapter.MyFragmentStatePagerAdapter;
import com.uflycn.uoperation.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * 棚膜管理fragment
 * 2019.3.21 lqw
 *
 */
public class GreenHouseFilmFragment extends DemoBaseFragment {

    @BindView(R.id.tl_gf)
    TabLayout mTabLayout;

    @BindView(R.id.vp_gf)
    ViewPager mViewPager;


    private String[] mTitle;
    private List<Fragment> fragmentList;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hd_greenhouse_film;
    }

    @Override
    protected void initView() {
        mTitle = UIUtils.getStringArray(R.array.greenhouse_film);
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initViewPager(){
        fragmentList = new ArrayList<>();
        fragmentList.add(new GreenHouseFilmListFragment());//棚膜列表
        fragmentList.add(new GreenHouseFilmAddFragment());//新建棚膜
        MyFragmentStatePagerAdapter fragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(),fragmentList,mTitle);
        mViewPager.setAdapter(fragmentStatePagerAdapter);
    }

    @Override
    protected void initData() {

    }
}
