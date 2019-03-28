package com.uflycn.uoperation.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

 /**
 * Created by UF_PC on 2017/9/8.
 */
public class TourResultAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private String[] title;

    public TourResultAdapter(FragmentManager supportFragmentManager, List<Fragment> fragmentList, String[] title) {
        super(supportFragmentManager);
        this.fragmentList = fragmentList;
        this.title = title;
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
        return title[position];
    }
}
