package com.uflycn.uoperation.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/26
 * Describe  : 这是是自己封装的，不参合mvp模式，封装了progressdialog的显示，注意继承的是Activiyy
 * 如果有其他需求最好不要继承这个类，
 */
public abstract class MyBaseActivity extends Activity {

    private ProgressDialog mProgressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getContentViewId());
        unbinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    public abstract int getContentViewId();

    public void initData() {

    }

    public void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void showDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    public void missDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

}
