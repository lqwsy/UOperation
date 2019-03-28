package com.uflycn.uoperation.ui.splash.model;


import com.uflycn.uoperation.bean.RegistrationInfo;

/**
 * Created by Ryan on 2016/7/13.
 */
public interface SplashModel {
    boolean isFileExist();

    String getRegisterFileCode();

    /**
     * 保存注册码信息
     * @param message
     */
    void saveRegisterFile(String message);

    /**
     * 保存注册信息
     */
    void saveRegInfoFile(RegistrationInfo info);


    RegistrationInfo getRegInfo();

    /**
     * 计算本机的注册码
     * @return
     */
    String createCode(String imei);

    /**
     * 删除注册文件
     */
    void deleteRegisterFile();
}
