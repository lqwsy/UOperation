package com.uflycn.uoperation.ui.fragment.breakdocument;


import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.UpdateBrokenListEvent;
import com.uflycn.uoperation.ui.activity.TourResultActivity;
import com.uflycn.uoperation.ui.adapter.TourResultAdapter;
import com.uflycn.uoperation.util.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 外破专档
 * Created by xiaoyehai on 2017/9/8.
 */
public class BreakDocumentFragment extends DemoBaseFragment {

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private String[] mTitle;

    private List<Fragment> mFragmentList;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_break_document;
    }

    @Override
    protected void initView() {
        mTitle = UIUtils.getStringArray(R.array.break_document);

        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new BreaklistFragment());
        mFragmentList.add(new BreakCreateFragment());

        TourResultAdapter adapter = new TourResultAdapter(getChildFragmentManager(), mFragmentList, mTitle);
        mViewPager.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ((TourResultActivity) mWeakReference.get()).finish();
                break;
        }
    }

    public void changeToFragment(int position){
        mViewPager.setCurrentItem(position);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThread(BaseMainThreadEvent event){
        if(event instanceof UpdateBrokenListEvent){
            changeToFragment(0);
        }
    }

}
