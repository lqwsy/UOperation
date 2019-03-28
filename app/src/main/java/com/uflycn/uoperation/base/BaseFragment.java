package com.uflycn.uoperation.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment {
    public View mRootView;
    public T mPresenter;
    protected Reference<Context> mRef;
    private Unbinder mUnBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRef = new WeakReference<Context>(getActivity());
        mPresenter = getPresenter();
        mPresenter.attach((BaseView) this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutID(), container, false);
        mUnBinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.deAttch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    public abstract int getLayoutID();

    public abstract T getPresenter();

    protected abstract void initView();


    protected void initData() {

    }

}
