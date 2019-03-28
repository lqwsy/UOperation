package com.uflycn.uoperation.ui.fragment.hiddendanger.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 隐患管理适配器
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private String[] mTitle;


    public MyFragmentStatePagerAdapter(FragmentManager fm,List<Fragment> fragmentList,String[] mTitle) {
        super(fm);
        this.fragmentList = fragmentList;
        this.mTitle = mTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
