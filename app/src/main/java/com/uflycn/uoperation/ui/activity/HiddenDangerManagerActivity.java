package com.uflycn.uoperation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.ui.fragment.hiddendanger.adapter.MyFragmentStatePagerAdapter;
import com.uflycn.uoperation.ui.fragment.hiddendanger.view.GreenHouseFilmFragment;
import com.uflycn.uoperation.ui.fragment.hiddendanger.view.HiddenDangerManagerFragment;
import com.uflycn.uoperation.ui.fragment.hiddendanger.view.TreeBarrierFragment;
import com.uflycn.uoperation.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 隐患管理
 */
public class HiddenDangerManagerActivity extends FragmentActivity {


    @BindView(R.id.tl_hidden_danger_tab)
    TabLayout mTabLayout;

    @BindView(R.id.vp_hidden_danger)
    ViewPager mViewPager;

    private String[] mTitle;
    private List<Fragment> fragmentList;

    public static Intent newInstance(Context context) {
        return new Intent(context, HiddenDangerManagerActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_hidden_danger);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        mTitle = UIUtils.getStringArray(R.array.hidden_danger);

        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new TreeBarrierFragment());//树障
        fragmentList.add(new HiddenDangerManagerFragment());//隐患
        fragmentList.add(new GreenHouseFilmFragment());//棚膜管理
        MyFragmentStatePagerAdapter fragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentList, mTitle);
        mViewPager.setAdapter(fragmentStatePagerAdapter);
    }

    @OnClick(R.id.iv_hidden_danger_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_hidden_danger_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

}
