package com.uflycn.uoperation.ui.splash.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;

import com.uflycn.uoperation.bean.RegistrationInfo;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.http.RegisterFactory;
import com.uflycn.uoperation.ui.register.widget.RegisterActivity;
import com.uflycn.uoperation.ui.splash.model.SplashModel;
import com.uflycn.uoperation.ui.splash.model.SplashModelImpl;
import com.uflycn.uoperation.ui.splash.view.SplashView;
import com.uflycn.uoperation.util.DeviceUtils;
import com.uflycn.uoperation.util.HttpUtils;
import com.uflycn.uoperation.util.Network;
import com.uflycn.uoperation.util.SharedPreferencesUtil;
import com.uflycn.uoperation.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ryan on 2016/7/13.
 */
public class SplashPresenterImpl implements SplashPresenter {

    private static final String TAG = "SplashPresenterImpl";

    private Context mContext;

    private SplashModel mSplashModel;

    private SplashView mSplashView;

    private int day;


    public SplashPresenterImpl(Context context, SplashView splashView) {
        mContext = context;
        mSplashModel = new SplashModelImpl(mContext);
        mSplashView = splashView;
    }

    /**
     * 判断设备应该采取哪种执行方法
     */
    @Override
    public void judge() {
        // 有网络时
        if (Network.checkNetWork(mContext)) {
            //联网查询
            final String deviceNo = DeviceUtils.getFixedIMEI(mContext);
            System.out.println(deviceNo);
            if (deviceNo.equalsIgnoreCase("55A0-96EB-CFC1-9C1A-0E4A-7623-6B87-5389")) {//这个是开发机
                mSplashView.setFlag(RegisterActivity.FLAG_SHOW_LOGIN);
                return;
            } else if (deviceNo.equalsIgnoreCase("7D9A-A505-2CD3-ECB7-73CA-EFC9-D7D4-A10B")) {//这个是模拟器
                mSplashView.setFlag(RegisterActivity.FLAG_SHOW_LOGIN);
                return;
            }
            new HttpUtils<String>(RegisterFactory.getRegisterService().checkStatus(deviceNo, AppConstant.PRODUCT_NAME)).handleResponse(new HttpUtils.ResponseListener<String>() {
                @Override
                public void onSuccess(String s) {
                    System.out.println("zqzc:成功" + s);
                    SharedPreferencesUtil.saveDataToSharedPreferences(mContext, AppConstant.SP_ALREADY_SUBMIT, false, SharedPreferencesUtil.MAP_CONFIG);
                    switch (s) {
                        case AppConstant.REGISTER_STATUS_UNREGISTER:
                            if (mSplashModel.isFileExist()) {
                                mSplashModel.deleteRegisterFile();
                            }
                            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_REGISTER);
                            // 未注册
                            break;
                        case AppConstant.REGISTER_STATUS_WAITING:
                            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_WAITING);
                            // 等待审核
                            SharedPreferencesUtil.saveDataToSharedPreferences(mContext, AppConstant.SP_ALREADY_SUBMIT, true, SharedPreferencesUtil.MAP_CONFIG);
                            break;

                        case AppConstant.REGISTER_STATUS_OVERDUE:
                            // 已过期
                            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_OVERDUE);
                            if (mSplashModel.isFileExist()) {
                                mSplashModel.deleteRegisterFile();
                            }
                            break;

                        case AppConstant.REGISTER_STATUS_REJECT:
                            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_REGISTER);
                            // 被拒绝
                            break;
                        case AppConstant.REGISTER_STATUS_REGISTERED:
                            // 已注册
                            if (!mSplashModel.isFileExist()) {
                                // 不存在
                                downloadRegisterCode();
                            } else {
                                if (!checkCodeLegal()) {
                                    mSplashModel.deleteRegisterFile();
                                    downloadRegisterCode();
                                    break;
                                }
                                //快过期7天提醒
                                if (checkWillOutDate()) {
                                    tellUser();
                                    break;
                                }
                            }
                            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_LOGIN);
                            break;

                        case AppConstant.REGISTER_STATUS_RENEW:
                            if (mSplashModel.isFileExist()) {
                                mSplashModel.deleteRegisterFile();
                            }

                            downloadRegisterCode();
                            break;

                        case AppConstant.REGISTER_STATUS_DISABLE:
                            if (mSplashModel.isFileExist()) {
                                mSplashModel.deleteRegisterFile();
                            }
                            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_DISABLE);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFail(int code, String message) {
                    System.out.println("zqzc:失败");
                    checkInLocal();
                }
            });
        } else {

            checkInLocal();
        }
    }

    private void checkInLocal() {
        boolean waiting = SharedPreferencesUtil.getDataToSharedPreferences(mContext, AppConstant.SP_ALREADY_SUBMIT, false, SharedPreferencesUtil.MAP_CONFIG);
        if (waiting) {
            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_WAITING);
            return;
        }

        // 没网络,查看文件是否存在
        if (mSplashModel.isFileExist()) {

            //判断注册码是否合法
            if (checkCodeLegal()) {
                // 注册码一致

                // 未过期
                if (checkDate()) {
                    //快过期7天提醒
                    if (checkWillOutDate()) {
                        tellUser();
                    }
                } else {
                    mSplashView.setFlag(RegisterActivity.FLAG_SHOW_OVERDUE);
                    return;
                }
                mSplashView.setFlag(RegisterActivity.FLAG_SHOW_LOGIN);
            } else {
                // 注册码不一致
                // 删除文件，重新注册
                mSplashModel.deleteRegisterFile();
                mSplashView.setFlag(RegisterActivity.FLAG_SHOW_REGISTER);

            }

        } else {
            // 文件不存在
            //未注册
            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_REGISTER);
        }
    }

    private boolean checkWillOutDate() {
        try {
            String code = mSplashModel.getRegisterFileCode();
            if (!TextUtils.isEmpty(code)) {
                String result[] = code.split(" ");
                String date = result[2];
                long outDateTime = 0;
                try {
                    outDateTime = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long now = System.currentTimeMillis();
                long dayTime = 24 * 60 * 60;
                long days = (outDateTime - now) / 1000 / dayTime;
                if (days < 7) {
                    //即将过期,每天提醒1次
                    day = (int) days;
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void tellUser() {
        mSplashView.removeCallback();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("到期提示");
        builder.setMessage("你的软件还有" + day + "天到期，注意及时续期！")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mSplashView.moveToRegister(RegisterActivity.FLAG_SHOW_LOGIN);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkCodeLegal() {
        try {
            String wholeCode = mSplashModel.getRegisterFileCode();
            String oldCode = "";
            String productName = "";
            if (!TextUtils.isEmpty(wholeCode)) {
                String[] resultCode = wholeCode.split(" ");
                productName = resultCode[0];
                oldCode = resultCode[1];
            }
            return oldCode.equals(DeviceUtils.getFixedIMEI(mContext)) && productName.equalsIgnoreCase(AppConstant.PRODUCT_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkDate() {
        try {
            String code = mSplashModel.getRegisterFileCode();
            if (!TextUtils.isEmpty(code)) {
                String result[] = code.split(" ");
                System.out.println("code" + code);
                String date = result[2];
                long overdueTime = 0;
                try {
                    overdueTime = new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long now = System.currentTimeMillis();

                if (now < overdueTime) {
                    // 未过期
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 下载注册文件
     */
    public void downloadRegisterCode() {
        new HttpUtils<List<RegistrationInfo>>(RegisterFactory.getRegisterService().
                getRegisterCode(DeviceUtils.getFixedIMEI(mContext), AppConstant.PRODUCT_NAME)).
                handleResponse(new HttpUtils.ResponseListener<List<RegistrationInfo>>() {
                    @Override
                    public void onSuccess(List<RegistrationInfo> list) {
                        Log.d(TAG, "onSuccess: 下载注册文件成功");
                        if (list != null && list.size() > 0) {
                            // 保存code
                            RegistrationInfo registrationInfo = list.get(0);
                            if (!StringUtils.isEmptyOrNull(registrationInfo.getRegSerialNo())) {
                                mSplashModel.saveRegisterFile(registrationInfo.getRegSerialNo());
                            }
                            mSplashModel.saveRegInfoFile(registrationInfo);
                            //快过期7天提醒
                            if (checkWillOutDate()) {
                                tellUser();
                                return;
                            }
                            mSplashView.setFlag(RegisterActivity.FLAG_SHOW_LOGIN);
                        }
                    }

                    @Override
                    public void onFail(int code, String message) {
                        Log.d(TAG, "onFail: 下载注册文件失败");
                    }
                });
    }

    @Override
    public RegistrationInfo getRegInfo() {
        return mSplashModel.getRegInfo();
    }

}
