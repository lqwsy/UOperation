package com.uflycn.uoperation.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public abstract class DemoBaseFragment extends Fragment {
    public View mRootView;
    protected WeakReference<Context> mWeakReference;
    private Unbinder mUnBinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeakReference = new WeakReference<Context>(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutID(), container, false);
        mUnBinder = ButterKnife.bind(this, mRootView);
        initView();
        initData();
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    public Context getContext() {
        return mWeakReference.get();
    }

    public abstract int getLayoutID();

    protected abstract void initView();

    protected abstract void initData();
}
