package com.uflycn.uoperation.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public abstract class BaseActivity<T extends BasePresenter> extends Activity {

    public T mPresenter;
    private Unbinder mUnBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutID());
        mUnBinder = ButterKnife.bind(this);

        mPresenter = getPresenter();
        mPresenter.attach((BaseView) this);

        initView();

        initData();
    }

    public void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.deAttch();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    protected abstract int getLayoutID();

    public abstract T getPresenter();

    protected abstract void initView();

}
