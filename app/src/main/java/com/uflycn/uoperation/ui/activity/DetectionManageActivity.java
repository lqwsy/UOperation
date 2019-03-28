package com.uflycn.uoperation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.ui.fragment.DetectionManage.GroundResistanceFragment;
import com.uflycn.uoperation.ui.fragment.DetectionManage.IceDetectionFragment;
import com.uflycn.uoperation.ui.fragment.DetectionManage.InfraredDetectionFragment;
import com.uflycn.uoperation.ui.fragment.DetectionManage.ZeroDetectionFragment;
import com.uflycn.uoperation.ui.fragment.checkresult.CrossListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 检测管理
 */
public class DetectionManageActivity extends FragmentActivity {

    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();

    public static Intent newInstance(Context context) {
        return new Intent(context, DetectionManageActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_detection_manage);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mFragmentList.add(new InfraredDetectionFragment());
        mTitleList.add("红外检测");
        mFragmentList.add(new GroundResistanceFragment());
        mTitleList.add("接地电阻");
        mFragmentList.add(new CrossListFragment());
        mTitleList.add("交跨检测");
        mFragmentList.add(new ZeroDetectionFragment());
        mTitleList.add("零值检测");
        mFragmentList.add(new IceDetectionFragment());
        mTitleList.add("覆冰检测");
        mViewpager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitleList.get(position);
            }
        });
        mTablayout.setupWithViewPager(mViewpager);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
