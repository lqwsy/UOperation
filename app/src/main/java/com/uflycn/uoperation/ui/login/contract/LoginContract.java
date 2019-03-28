package com.uflycn.uoperation.ui.login.contract;

import com.uflycn.uoperation.base.BaseModel;
import com.uflycn.uoperation.base.BasePresenter;
import com.uflycn.uoperation.base.BaseView;
import com.uflycn.uoperation.ui.login.callback.LoginListener;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public interface LoginContract {

    interface Model extends BaseModel {
        String getSavedAccount();

        String getSavedPassword();

        String getSavedIp();

        String getSavedPort();

        String getSavePushPort();

        void login(String username, String password, LoginListener loginListener);

        void saveAccount(String uername, String password);

        void saveIpPort(String ip, String port, String pushPort);

        void saveIpPort(String ip, String port, String pushPort,String ipType);

    }

    interface View extends BaseView {

        String getUsername();

        String getPassword();

        void setUserName(String userName);

        void setPassword(String password);

        void setIp(String ip);

        void setPort(String port);

        void setPushPort(String pushPort);

        void showProgressDialog();

        void hideProgressDialog();

        String getServerIP();

        String getServerPort();

        String getPushPort();

        String getIpType();

        void jumpActivity();

        void showLoading();

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void initLogin();

        public abstract void login();

        public abstract void submitIpPort();

        public abstract void initIpPort();
    }


}
