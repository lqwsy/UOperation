package com.uflycn.uoperation.ui.login.presenter;


import android.text.TextUtils;

import com.uflycn.uoperation.bean.User;
import com.uflycn.uoperation.service.UpdateService;
import com.uflycn.uoperation.ui.login.callback.LoginListener;
import com.uflycn.uoperation.ui.login.contract.LoginContract;
import com.uflycn.uoperation.ui.login.model.LoginModel;
import com.uflycn.uoperation.util.ToastUtil;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public LoginContract.Model getModel() {
        return new LoginModel();
    }

    @Override
    public void login() {
        final LoginContract.View loginView = getView();

        final String username = loginView.getUsername();
        final String password = loginView.getPassword();

        if (TextUtils.isEmpty(username)) {
            ToastUtil.show("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show("密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(loginView.getServerIP()) || TextUtils.isEmpty(loginView.getServerPort())) {
            ToastUtil.show("请在服务器设置中填写ip和端口号");
            return;
        }



        if (TextUtils.isEmpty(loginView.getPushPort())) {
            ToastUtil.show("请在服务器设置中填写推送端口号");
            return;
        }
        loginView.showProgressDialog();

        model.login(username, password, new LoginListener() {

            @Override
            public void onSuccess(User user) {
                ToastUtil.show(user.getRealName() + "登录成功");
                loginView.hideProgressDialog();
                model.saveAccount(username, password);
                //AppConstant.currentUser = UserDBHellper.getInstance().getUserByAccount(username);

                loginView.jumpActivity();
            }

            @Override
            public void onFailure(String reason) {
                loginView.hideProgressDialog();
                ToastUtil.show(reason);
            }

            @Override
            public void onLoading() {
                model.saveAccount(username, password);

                //弹框
                //                loginView.hideProgressDialog();
                loginView.showLoading();
            }
        });
    }

    @Override
    public void submitIpPort() {
        if (UpdateService.APP_NAME.contains("赣西")){
            model.saveIpPort(getView().getServerIP(), getView().getServerPort(), getView().getPushPort(),getView().getIpType());
        }else {
            model.saveIpPort(getView().getServerIP(), getView().getServerPort(), getView().getPushPort());
        }
    }

    @Override
    public void initIpPort() {
        LoginContract.View loginView = getView();
        loginView.setIp(model.getSavedIp());
        loginView.setPort(model.getSavedPort());
        loginView.setPushPort(model.getSavePushPort());
    }


    @Override
    public void initLogin() {
        LoginContract.View loginView = getView();
        loginView.setUserName(model.getSavedAccount());
        loginView.setPassword(model.getSavedPassword());
    }


}
