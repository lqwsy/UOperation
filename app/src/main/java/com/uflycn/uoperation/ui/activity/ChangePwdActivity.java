package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.http.LoginService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by UF_PC on 2017/10/16.
 * 修改密码页面
 */
public class ChangePwdActivity extends Activity {


    @BindView(R.id.et_old_pwd)
    EditText mEtOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText mEtNewPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText mEtConfirmPwd;
    @BindView(R.id.btn_change_pwd)
    Button mBtnChangePwd;

    private Call<BaseCallBack<String>> mSubmitCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btn_change_pwd)
    public void doClick(View view) {
        String oldPwd = mEtOldPwd.getText().toString().trim();
        final String newPwd = mEtNewPwd.getText().toString().trim();
        String confirmPwd = mEtConfirmPwd.getText().toString().trim();
        if (!newPwd.equals(confirmPwd)) {
            ToastUtil.show("两次输入的密码不相同");
            return;
        }

        mSubmitCall = RetrofitManager.getInstance().getService(LoginService.class).changePwd(oldPwd, newPwd, confirmPwd);
        mSubmitCall.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response.body() == null) {
                    return;
                }
                int code = response.body().getCode();
                Log.i("upload", "code=" + code + "message=" + response.body().getMessage());
                if (code == 0) {
                    ToastUtil.show(response.body().getMessage());

                } else if (code == 1) {
                    ToastUtil.show("密码修改成功");
                    ShareUtil.setString(UIUtils.getContext(), "password", newPwd);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                ToastUtil.show(t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubmitCall != null && !mSubmitCall.isCanceled()) {
            mSubmitCall.cancel();
        }
    }
}
