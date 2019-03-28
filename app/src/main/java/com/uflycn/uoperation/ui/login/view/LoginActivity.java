package com.uflycn.uoperation.ui.login.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.BaseActivity;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.UpdateTableEvent;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.service.CheckService;
import com.uflycn.uoperation.service.UpdateService;
import com.uflycn.uoperation.ui.login.contract.LoginContract;
import com.uflycn.uoperation.ui.login.presenter.LoginPresenter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.uflycn.uoperation.widget.SimpleDlg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View, View.OnClickListener {

    @BindView(R.id.et_username)
    EditText etUsername;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.btn_server_setting)
    Button btnServerSetting;

    private ProgressDialog mProgressDialog;
    private View mDialogView;
    private AlertDialog mDialog;
    private EditText mEtSerVerIp;
    private EditText mEtSerVerPort;
    private EditText mEtPushPort;
    private SimpleDlg mSimpleDlg;
    private Spinner mSpinnerIp;
    private LinearLayout mLlChangeIp;
    //    private ProgressDialog mLoadingDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public LoginPresenter getPresenter() {
        return new LoginPresenter();
    }

    private void saveIp() {
        if (StringUtils.isEmptyOrNull(ShareUtil.getString(LoginActivity.this, "MoveIP", "")) &&
                StringUtils.isEmptyOrNull(ShareUtil.getString(LoginActivity.this, "WireIP", ""))) {
            if (ProjectManageUtil.isGanXi()) {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "223.84.234.157");
                ShareUtil.setString(LoginActivity.this, "WireIP", "218.87.108.184");
                ShareUtil.setString(LoginActivity.this, "port", "7002");
                ShareUtil.setString(LoginActivity.this, "pushPort", "7003");
            } else if (ProjectManageUtil.isPingXiang()) {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "117.40.230.100");
                ShareUtil.setString(LoginActivity.this, "WireIP", "");
                ShareUtil.setString(LoginActivity.this, "port", "7002");
                ShareUtil.setString(LoginActivity.this, "pushPort", "7003");
            } else if (ProjectManageUtil.isJiuJiang()) {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "113.108.203.26");
                ShareUtil.setString(LoginActivity.this, "WireIP", "");
                ShareUtil.setString(LoginActivity.this, "port", "9592");
                ShareUtil.setString(LoginActivity.this, "pushPort", "9593");
            } else if (ProjectManageUtil.isNanChang()) {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "113.108.203.26");
                ShareUtil.setString(LoginActivity.this, "WireIP", "");
                ShareUtil.setString(LoginActivity.this, "port", "9552");
                ShareUtil.setString(LoginActivity.this, "pushPort", "9553");
            } else if (ProjectManageUtil.isShanDong()) {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "221.214.61.30");
                ShareUtil.setString(LoginActivity.this, "WireIP", "");
                ShareUtil.setString(LoginActivity.this, "port", "38082");
                ShareUtil.setString(LoginActivity.this, "pushPort", "38083");
            } else if (ProjectManageUtil.isYiChun()) {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "1175.54.561.");
                ShareUtil.setString(LoginActivity.this, "WireIP", "");
                ShareUtil.setString(LoginActivity.this, "port", "7002");
                ShareUtil.setString(LoginActivity.this, "pushPort", "7003");
            }else if (ProjectManageUtil.isGanZhou()) {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "117.41.200.55");
                ShareUtil.setString(LoginActivity.this, "WireIP", "");
                ShareUtil.setString(LoginActivity.this, "port", "7002");
                ShareUtil.setString(LoginActivity.this, "pushPort", "7003");
            } else if (ProjectManageUtil.isYongChuan()){
                ShareUtil.setString(LoginActivity.this, "MoveIP", "113.108.203.26");
                ShareUtil.setString(LoginActivity.this, "port", "9732");
                ShareUtil.setString(LoginActivity.this, "pushPort", "9733");
            }else {
                ShareUtil.setString(LoginActivity.this, "MoveIP", "113.108.203.26");
                ShareUtil.setString(LoginActivity.this, "port", "6303");
                ShareUtil.setString(LoginActivity.this, "pushPort", "6313");
            }

        }

    }

    @Override
    protected void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在登录...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mDialogView = View.inflate(this, R.layout.dialog_system_setting, null);
        mSpinnerIp = (Spinner) mDialogView.findViewById(R.id.sp_server_choose);
        mEtSerVerIp = (EditText) mDialogView.findViewById(R.id.et_server_ip);
        mEtSerVerPort = (EditText) mDialogView.findViewById(R.id.et_server_port);
        mEtPushPort = (EditText) mDialogView.findViewById(R.id.et_push_port);
        mLlChangeIp = (LinearLayout) mDialogView.findViewById(R.id.ll_change_ip);
        Button btnSubmit = (Button) mDialogView.findViewById(R.id.btn_submit);
        initSpinnerIp();
        setIp(ShareUtil.getString(LoginActivity.this, "MoveIP", ""));

        btnLogin.setOnClickListener(this);
        btnServerSetting.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        mPresenter.initLogin();
        mPresenter.initIpPort();
        int is_auto_login = getIntent().getIntExtra("is_auto_login", -1);
        if (is_auto_login == 100) {
            mPresenter.login();
        }
        if (!UpdateService.APP_NAME.contains("赣西")) {
            mLlChangeIp.setVisibility(View.GONE);
            setIp(ShareUtil.getString(LoginActivity.this, "MoveIP", ""));
        }
        EventBus.getDefault().register(this);

    }


    private void initSpinnerIp() {
        List<SpinnerOption> mSpIpNo = new ArrayList<>();
        saveIp();

        mSpIpNo.add(new SpinnerOption("0", "移动IP"));
        mSpIpNo.add(new SpinnerOption("1", "电信IP"));
        ArrayAdapter<SpinnerOption> adapter = new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, mSpIpNo);
        mSpinnerIp.setAdapter(adapter);

        mSpinnerIp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setIp(ShareUtil.getString(LoginActivity.this, "MoveIP", ""));
                        break;
                    case 1:
                        setIp(ShareUtil.getString(LoginActivity.this, "WireIP", ""));
                        break;
                }
                ShareUtil.setInt(LoginActivity.this, "ChooseIP", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinnerIp.setSelection(ShareUtil.getInt(this, "ChooseIP", 0));
        //        setIp(ShareUtil.getString(LoginActivity.this, "",""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //jumpActivity();
                mPresenter.login();
                break;
            case R.id.btn_server_setting:
                showSettingDialog();
                break;
            case R.id.btn_submit:
                if (!StringUtils.IPCheck(mEtSerVerIp.getText().toString())) {
                    ToastUtil.show("请在服务器设置中填写正确的ip");
                    return;
                }
                mPresenter.submitIpPort();
                mDialog.dismiss();
                break;
            case R.id.dlg_btn_left:
                mSimpleDlg.dismiss();
                break;
            case R.id.dlg_btn_right:
                mSimpleDlg.dismiss();
                System.exit(0);
                break;
        }
    }

    private void showSettingDialog() {

        ViewGroup parent = (ViewGroup) mDialogView.getParent();
        if (parent != null) {
            parent.removeView(mDialogView);
        }
        mDialog = new AlertDialog.Builder(this)
                .setView(mDialogView)
                .show();

    }

    @Override
    public String getIpType() {
        return mSpinnerIp.getSelectedItemPosition() == 0 ? "MoveIP" : "WireIP";
    }

    @Override
    public void setUserName(String userName) {
        etUsername.setText(userName);
    }

    @Override
    public void setPassword(String password) {
        etPassword.setText(password);
    }

    @Override
    public void setIp(String ip) {
        mEtSerVerIp.setText(ip);
    }

    @Override
    public void setPort(String port) {
        mEtSerVerPort.setText(port);
    }

    @Override
    public void setPushPort(String pushPort) {
        mEtPushPort.setText(pushPort);
    }

    @Override
    public String getUsername() {
        return etUsername.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public String getPushPort() {
        return mEtPushPort.getText().toString().trim();
    }

    @Override
    public String getServerIP() {
        return mEtSerVerIp.getText().toString().trim();
    }

    @Override
    public String getServerPort() {
        return mEtSerVerPort.getText().toString().trim();
    }

    @Override
    public void jumpActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void showLoading() {
        mProgressDialog.setMessage("获取电网资源，请勿断网或退出程序");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSimpleDlg == null) {
                SimpleDlg.Builder builder = new SimpleDlg.Builder();
                mSimpleDlg = builder.create(this);
                builder.setContentText("确定退出系统?");
                builder.setOnclickListener(this);
            }
            mSimpleDlg.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishUpdate(UpdateTableEvent event) {

        if (event.getSuccess() == 1) {

            mProgressDialog.dismiss();
            startService(new Intent(this, CheckService.class));
            AppConstant.currentUser = UserDBHellper.getInstance().getUser(AppConstant.currentUser.getUserId());
            ShareUtil.setString(UIUtils.getContext(), "LoginToken", AppConstant.TOKEN);
            jumpActivity();
        } else {
            ToastUtil.show("网络连接异常，更新本地资源失败");
            mProgressDialog.dismiss();
        }
    }
}
