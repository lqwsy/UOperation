package com.uflycn.uoperation.ui.splash.presenter;


import com.uflycn.uoperation.bean.RegistrationInfo;

/**
 * Created by Ryan on 2016/7/13.
 */
public interface SplashPresenter {
    void judge();
    RegistrationInfo getRegInfo();
    void downloadRegisterCode();
}
