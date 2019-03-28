package com.uflycn.uoperation.ui.login.model;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.LoginCallbackEntity;
import com.uflycn.uoperation.bean.RecordSyncInfo;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.constant.ContantUrl;
import com.uflycn.uoperation.greendao.RecordSyncInfoDBHelper;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.http.LoginService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.login.callback.LoginListener;
import com.uflycn.uoperation.ui.login.contract.LoginContract;
import com.uflycn.uoperation.util.Network;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.uflycn.uoperation.util.UpdateTableUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public class LoginModel implements LoginContract.Model {

    @Override
    public String getSavedAccount() {
        return ShareUtil.getString(UIUtils.getContext(), "username", "");
    }

    @Override
    public String getSavedPassword() {
        return ShareUtil.getString(UIUtils.getContext(), "password", "");
    }

    @Override
    public String getSavedIp() {
        if (ProjectManageUtil.isShanDong()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "221.214.61.30");
        } else if (ProjectManageUtil.isGanXi()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "223.84.234.157");
        } else if (ProjectManageUtil.isGanZhou()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "117.41.200.55");
        } else if (ProjectManageUtil.isJiuJiang()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "182.106.220.216");
        } else if (ProjectManageUtil.isNanChang()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "117.21.29.110");
        } else if (ProjectManageUtil.isYiChun()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "111.75.54.56");
        } else if (ProjectManageUtil.isYongChuan()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "113.108.203.26");
        } else if (ProjectManageUtil.isPingXiang()) {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "117.40.230.100 ");
        } else {
            return ShareUtil.getString(UIUtils.getContext(), "ip", "113.108.203.26");
        }
    }

    @Override
    public String getSavedPort() {
        if (ProjectManageUtil.isShanDong()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "38082");
        } else if (ProjectManageUtil.isGanXi()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "7002");
        } else if (ProjectManageUtil.isGanZhou()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "7002");
        } else if (ProjectManageUtil.isJiuJiang()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "7002");
        } else if (ProjectManageUtil.isNanChang()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "7002");
        } else if (ProjectManageUtil.isYiChun()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "7002");
        } else if (ProjectManageUtil.isYongChuan()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "9732");
        }  else if (ProjectManageUtil.isPingXiang()) {
            return ShareUtil.getString(UIUtils.getContext(), "port", "7002");
        } else {
            return ShareUtil.getString(UIUtils.getContext(), "port", "6303");
        }
    }

    @Override
    public String getSavePushPort() {
        if (ProjectManageUtil.isShanDong()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "38083");
        } else if (ProjectManageUtil.isGanXi()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "7003");
        } else if (ProjectManageUtil.isGanZhou()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "7003");
        } else if (ProjectManageUtil.isJiuJiang()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "7003");
        } else if (ProjectManageUtil.isNanChang()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "7003");
        } else if (ProjectManageUtil.isYiChun()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "7003");
        } else if (ProjectManageUtil.isYongChuan()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "9733");
        }else if (ProjectManageUtil.isPingXiang()) {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "7003");
        }  else {
            return ShareUtil.getString(UIUtils.getContext(), "pushPort", "6313");
        }
    }


    /**
     * 上传账号 和密码到 服务器上判断  是否正确
     *
     * @param username
     * @param password
     * @param loginListener
     */
    @Override
    public void login(final String username, final String password, final LoginListener loginListener) {

        String savedIp = getSavedIp();
        String savedPort = getSavedPort();
        String savePushPort = getSavePushPort();
        String fixedVersion = "";
        if (savedIp == null || savedIp == "" || savedPort == null || savedPort == "" || savePushPort == "") {
            return;
        }
        ContantUrl.IP_ADDREDDS = savedIp;
        ContantUrl.BASE_URL = "http://" + savedIp + ":" + savedPort;
        ContantUrl.PUSH_PORT = savePushPort;
        try {
            PackageInfo info = MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
            fixedVersion = "v" + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ContantUrl.BASE_URL)
                .build();

        retrofit.create(LoginService.class).doLogin(username, password, "0", fixedVersion, Network.getIMEI(MyApplication.getContext())).enqueue(new Callback<BaseCallBack<LoginCallbackEntity>>() {
            @Override
            public void onResponse(Call<BaseCallBack<LoginCallbackEntity>> call, Response<BaseCallBack<LoginCallbackEntity>> response) {
                if (loginListener != null && response != null && response.body() != null && response.body().getCode() == 1) {
                    AppConstant.TOKEN = response.body().getData().getTOKEN();
                    AppConstant.currentUser = response.body().getData().getUser();
                    AppConstant.NET_WORK_AVAILABLE = true;
                    loginListener.onLoading();
                    // 第二次进行网络请求，判断是否需要更新本地的数据
                    postNeedUpdate();
                } else {
                    loginListener.onFailure("账号或者密码错误！");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<LoginCallbackEntity>> call, Throwable t) {
                try {
                    AppConstant.currentUser = UserDBHellper.getInstance().getUserByAccount(username);
                    if (password.equalsIgnoreCase(AppConstant.currentUser.getPassword())) {
                        loginListener.onSuccess(AppConstant.currentUser);
                    } else {
                        loginListener.onFailure("账号或者密码错误！");
                    }
                } catch (NullPointerException no) {
                    loginListener.onFailure("账号或者密码错误！");

                } catch (Exception e) {

                    loginListener.onFailure("请先联网同步电网资源");
                }
            }
        });

    }

    @Override
    public void saveAccount(String uername, String password) {
        ShareUtil.setString(UIUtils.getContext(), "username", uername);
        ShareUtil.setString(UIUtils.getContext(), "password", password);
    }

    @Override
    public void saveIpPort(String ip, String port, String pushPort) {
        ShareUtil.setString(UIUtils.getContext(), "ip", ip);
        ShareUtil.setString(UIUtils.getContext(), "port", port);
        ShareUtil.setString(UIUtils.getContext(), "pushPort", pushPort);
        ShareUtil.setString(UIUtils.getContext(), "MoveIP", ip);

        ContantUrl.IP_ADDREDDS = getSavedIp();
        ContantUrl.BASE_URL = "http://" + getSavedIp() + ":" + port;
        ContantUrl.PUSH_PORT = getSavePushPort();
        RetrofitManager.getInstance().resetRetrofit();

    }

    @Override
    public void saveIpPort(String ip, String port, String pushPort, String ipType) {
        ShareUtil.setString(UIUtils.getContext(), "ip", ip);
        ShareUtil.setString(UIUtils.getContext(), "port", port);
        ShareUtil.setString(UIUtils.getContext(), "pushPort", pushPort);
        ShareUtil.setString(UIUtils.getContext(), ipType, ip);

        ContantUrl.IP_ADDREDDS = getSavedIp();
        ContantUrl.BASE_URL = "http://" + getSavedIp() + ":" + port;
        ContantUrl.PUSH_PORT = getSavePushPort();
        RetrofitManager.getInstance().resetRetrofit();
    }

    /**
     * 本地数据库与服务器数据库同步处理
     */
    private void postNeedUpdate() {
        List<RecordSyncInfo> list = RecordSyncInfoDBHelper.getInstance().getList();
        UpdateTableUtil.getInstance().postGetNeedUpdateRequest(list);
        UpdateTableUtil.getInstance().loadVirtualTowers();
    }
}
