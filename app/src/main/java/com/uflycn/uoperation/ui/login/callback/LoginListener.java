package com.uflycn.uoperation.ui.login.callback;

import com.uflycn.uoperation.bean.User;

/**
 * 登陆回调监听
 * Created by UF_PC on 2017/9/1.
 */
public interface LoginListener {

    void onSuccess(User user);

    void onFailure(String reason);

    void onLoading();
}
