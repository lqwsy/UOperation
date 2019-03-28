package com.uflycn.uoperation.ui.register.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.ui.login.view.LoginActivity;
import com.uflycn.uoperation.ui.register.presenter.RegisterPresenter;
import com.uflycn.uoperation.ui.register.presenter.RegisterPresenterImpl;
import com.uflycn.uoperation.ui.register.view.RegisterView;
import com.uflycn.uoperation.util.DeviceUtils;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 2016/7/13.
 */


public class RegisterActivity extends Activity implements RegisterView, View.OnClickListener, View.OnFocusChangeListener {

    public static final String FLAG = "flag";

    public static final String FLAG_SHOW_REGISTER = "show_register";

    public static final String FLAG_SHOW_OVERDUE = "show_overdue";

    public static final String FLAG_SHOW_WAITING = "show_waiting";

    public static final String FLAG_SHOW_DISABLE = "show_disable";

    public static final String FLAG_SHOW_LOGIN = "show_login";

    private RegisterPresenter mRegisterPresenter;

    private CardView mRegisterCv;

    private CardView mWaitingCv;

    private CardView mOverdueCv;

    private CardView mDisableCv;

    private Button mExitBtn;

    private Button mRenewBtn;

    private Button mSubmitBtn;

    private Button mQuitBtn;

    private EditText mUserNameEt;

    private EditText mEmailEt;

    private EditText mTelNoEt;

    private EditText mCompanyEdt;

    private TextView mNameTv;

    private TextView mEmailTv;

    private TextView mTelTv;

    private TextView mCompanyTv;

    private TextView mCodeTv;

    private List<View> mViewList;

    private String mFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        mRegisterPresenter = new RegisterPresenterImpl(this, this);
        mViewList = new ArrayList<>();
        mViewList.add(mRegisterCv);
        mViewList.add(mWaitingCv);
        mViewList.add(mOverdueCv);
        mViewList.add(mDisableCv);
        mFlag = getIntent().getStringExtra(FLAG);
        showView();
    }

    private void showView() {
        switch (mFlag) {
            case FLAG_SHOW_REGISTER:
                showRegisterView();
                break;
            case FLAG_SHOW_WAITING:
                showWaitingView();
                break;
            case FLAG_SHOW_DISABLE:
                showDisableView();
                break;
            case FLAG_SHOW_LOGIN:
                moveToLoginActivity();
                break;
            case FLAG_SHOW_OVERDUE:
                showOverdueView();
                break;
        }

    }

    private void initView() {
        mRegisterCv = (CardView) findViewById(R.id.cardview_register);
        mWaitingCv = (CardView) findViewById(R.id.cardview_waiting);
        mOverdueCv = (CardView) findViewById(R.id.cardview_overdue);
        mDisableCv = (CardView) findViewById(R.id.cardview_disable);
        mExitBtn = (Button) findViewById(R.id.btn_exit);
        mRenewBtn = (Button) findViewById(R.id.btn_renew);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
        mQuitBtn = (Button) findViewById(R.id.btn_quit);
        mUserNameEt = (EditText) findViewById(R.id.edt_name);
        mEmailEt = (EditText) findViewById(R.id.edt_email);
        mTelNoEt = (EditText) findViewById(R.id.edt_tel);
        mCompanyEdt = (EditText) findViewById(R.id.edt_company);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mEmailTv = (TextView) findViewById(R.id.tv_email);
        mTelTv = (TextView) findViewById(R.id.tv_tel);
        mCompanyTv = (TextView) findViewById(R.id.tv_company);
        mCodeTv = (TextView) findViewById(R.id.tv_code);
        mRenewBtn.setOnClickListener(this);
        mExitBtn.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        mQuitBtn.setOnClickListener(this);
        mUserNameEt.setOnFocusChangeListener(this);
        mEmailEt.setOnFocusChangeListener(this);
        mTelNoEt.setOnFocusChangeListener(this);
        mCompanyEdt.setOnFocusChangeListener(this);
    }

    private void hideAllView() {
        for (View view : mViewList) {
            view.setVisibility(View.GONE);
        }
    }


    @Override
    public void showRegisterView() {
        hideAllView();
        mRegisterCv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showWaitingView() {
        hideAllView();
        mCodeTv.setText(DeviceUtils.getFixedIMEI(this));
        mWaitingCv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOverdueView() {
        hideAllView();
        mOverdueCv.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDisableView() {
        hideAllView();
        mDisableCv.setVisibility(View.VISIBLE);
    }

    @Override
    public void moveToLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showToast(String toast) {
        ToastUtil.show(this, toast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                mRegisterPresenter.submit(mUserNameEt.getText().toString(), mEmailEt.getText().toString(), mTelNoEt.getText().toString(), mCompanyEdt.getText().toString());
                break;
            case R.id.btn_renew:
                mRegisterPresenter.renew();
                break;
            case R.id.btn_quit:
            case R.id.btn_exit:
                finish();
                break;
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String hint;
        if (hasFocus) {
            hideAllTitle();
            switch (v.getId()) {
                case R.id.edt_name:
                    mNameTv.setVisibility(View.VISIBLE);
                    hint = mUserNameEt.getHint().toString();
                    mUserNameEt.setTag(hint);
                    mUserNameEt.setHint("");
                    break;
                case R.id.edt_email:
                    mEmailTv.setVisibility(View.VISIBLE);
                    hint = mEmailEt.getHint().toString();
                    mEmailEt.setTag(hint);
                    mEmailEt.setHint("");
                    break;
                case R.id.edt_tel:
                    mTelTv.setVisibility(View.VISIBLE);
                    hint = mTelNoEt.getHint().toString();
                    mTelNoEt.setTag(hint);
                    mTelNoEt.setHint("");
                    break;
                case R.id.edt_company:
                    mCompanyTv.setVisibility(View.VISIBLE);
                    hint = mCompanyEdt.getHint().toString();
                    mCompanyEdt.setTag(hint);
                    mCompanyEdt.setHint("");
                    break;

            }
        } else {
            switch (v.getId()) {
                case R.id.edt_name:
                    hint = mUserNameEt.getTag().toString();
                    mUserNameEt.setHint(hint);
                    break;
                case R.id.edt_email:
                    hint = mEmailEt.getTag().toString();
                    mEmailEt.setHint(hint);
                    break;
                case R.id.edt_tel:
                    hint = mTelNoEt.getTag().toString();
                    mTelNoEt.setHint(hint);
                    break;
                case R.id.edt_company:
                    hint = mCompanyEdt.getTag().toString();
                    mCompanyEdt.setHint(hint);
                    break;

            }
        }
    }


    private void hideAllTitle() {
        mNameTv.setVisibility(View.INVISIBLE);
        mEmailTv.setVisibility(View.INVISIBLE);
        mTelTv.setVisibility(View.INVISIBLE);
        mCompanyTv.setVisibility(View.INVISIBLE);
    }
}



