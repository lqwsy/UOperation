package com.uflycn.uoperation.ui.register.view;

/**
 * Created by Ryan on 2016/7/13.
 */
public interface RegisterView {
    void showRegisterView();

    void showWaitingView();

    void showOverdueView();

    void showDisableView();

    void moveToLoginActivity();

    void showToast(String toast);
}
