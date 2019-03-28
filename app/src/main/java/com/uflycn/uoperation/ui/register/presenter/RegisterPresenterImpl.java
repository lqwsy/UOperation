package  com.uflycn.uoperation.ui.register.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.uflycn.uoperation.bean.RegisterInfo;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.http.RegisterFactory;
import com.uflycn.uoperation.ui.register.model.RegisterModel;
import com.uflycn.uoperation.ui.register.model.RegisterModelImpl;
import com.uflycn.uoperation.ui.register.view.RegisterView;
import com.uflycn.uoperation.util.DeviceUtils;
import com.uflycn.uoperation.util.HttpUtils;
import com.uflycn.uoperation.util.Network;
import com.uflycn.uoperation.util.SharedPreferencesUtil;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ryan on 2016/7/13.
 */
public class RegisterPresenterImpl implements RegisterPresenter {

    private Context mContext;

    private RegisterView mRegisterView;

    private RegisterModel mRegisterModel;

    public RegisterPresenterImpl(Context context, RegisterView registerView) {
        mContext = context;
        mRegisterView = registerView;
        mRegisterModel = new RegisterModelImpl(mContext);
    }

    @Override
    public void submit(String username, String email, String tel, String company) {
        if(!Network.checkNetWork(mContext)){
            mRegisterView.showToast("请先联网再重试");
            return ;
        }
        if (!isLegal(username,email,tel,company)){
            return;
        }
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setUsername(username);
        registerInfo.setEmail(email);
        registerInfo.setTel(tel);
        registerInfo.setCompany(company);
        registerInfo.setDeviceName(DeviceUtils.getDeviceName());
        registerInfo.setAppVersion(DeviceUtils.getVersionName(mContext));
        registerInfo.setDeviceNo(DeviceUtils.getFixedIMEI(mContext));
        registerInfo.setApplyDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis())));
        Gson gson = new Gson();
        String json = gson.toJson(registerInfo);
        System.out.println(json);

        // 注册
        new HttpUtils(RegisterFactory.getRegisterService().register(json)).handleResponse(new HttpUtils.ResponseListener<String>() {
            @Override
            public void onSuccess(String s) {
                mRegisterView.showWaitingView();
                SharedPreferencesUtil.saveDataToSharedPreferences(mContext, AppConstant.SP_ALREADY_SUBMIT,true, SharedPreferencesUtil.MAP_CONFIG);
            }

            @Override
            public void onFail(int code,String message) {
                mRegisterView.showToast("网络异常,请检查网络");
                Log.e("RegisterFactory",message+"");
            }
        });

    }

    @Override
    public void renew() {
        new HttpUtils<String>(RegisterFactory.getRegisterService().renew(DeviceUtils.getFixedIMEI(mContext),AppConstant.PRODUCT_NAME)).handleResponse(new HttpUtils.ResponseListener<String>() {
            @Override
            public void onSuccess(String s) {
                mRegisterView.showWaitingView();
                SharedPreferencesUtil.saveDataToSharedPreferences(mContext, AppConstant.SP_ALREADY_SUBMIT,true, SharedPreferencesUtil.MAP_CONFIG);

            }

            @Override
            public void onFail(int code,String message) {
                mRegisterView.showToast("网络异常,请检查网络");
            }
        });
    }


    private boolean isLegal(String username, String email,String tel,String company) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||TextUtils.isEmpty(tel) || TextUtils.isEmpty(company)){
            mRegisterView.showToast("用户信息不能为空");
            return false;
        }
        Pattern namePattern = Pattern.compile("^([a-zA-Z0-9\\u4e00-\\u9fa5\\·]{1,10})$");
        Matcher nameMatcher = namePattern.matcher(username);
        if (!nameMatcher.matches()){
            mRegisterView.showToast("请输入正确的姓名");
            return false;
        }
        Pattern telPattern = Pattern.compile("^1\\d{10}$|^(0\\d{2,3}-?|\\(0\\d{2,3}\\))?[1-9]\\d{4,7}(-\\d{1,8})?$");
        Matcher telMatcher = telPattern.matcher(tel);
        if (!telMatcher.matches()){
            mRegisterView.showToast("请输入正确的电话");
            return false;
        }
        Pattern emailPattern = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$");
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()){
            mRegisterView.showToast("请输入正确的邮箱");
            return false;
        }
        return true;
    }



}
