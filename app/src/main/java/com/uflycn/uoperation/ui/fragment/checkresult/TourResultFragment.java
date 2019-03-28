package com.uflycn.uoperation.ui.fragment.checkresult;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.ui.activity.TourResultActivity;
import com.uflycn.uoperation.ui.adapter.TourResultAdapter;
import com.uflycn.uoperation.ui.fragment.assistRecord.view.AssistRecordFragment;
import com.uflycn.uoperation.ui.fragment.disdefect.DisDefectFragment;
import com.uflycn.uoperation.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 巡视登记
 * Created by xiaoyehai on 2017/9/8.
 */
public class TourResultFragment extends DemoBaseFragment {

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private String[] mTitle;

    private List<Fragment> mFragmentList;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_tour_result;
    }

    @Override
    protected void initView() {
        mTitle = UIUtils.getStringArray(R.array.tour_result);
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }

    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new DisDefectFragment());
        mFragmentList.add(new CrossListFragment());
        mFragmentList.add(new CheckRecordFragment());
        mFragmentList.add(new AssistRecordFragment());

        TourResultAdapter adapter = new TourResultAdapter(getChildFragmentManager(), mFragmentList, mTitle);
        mViewPager.setAdapter(adapter);

        mViewPager.setOffscreenPageLimit(3);
    }

    @OnClick({R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ((TourResultActivity) mWeakReference.get()).finish();
                break;
        }
    }
}
