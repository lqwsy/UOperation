package com.uflycn.uoperation.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.http.LoginService;
import com.uflycn.uoperation.http.RetrofitManagerLoginOut;
import com.uflycn.uoperation.util.LogUtils;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 检查程序是否还在继续 运行
 * 如果没有运行 则向服务提交退出请求
 * 华为手机没有用
 * Created by Administrator on 2018/2/28.
 */
public class CheckService extends Service {
    private static final String PackageName = "com.uflycn.uoperation";
    private final Timer timerMail = new Timer();
    private TimerTask taskMail;
    private LogUtils mLogUtils = LogUtils.getInstance();
    private ActivityManager activityManager=null;
    private  int ServerId = 0;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 要做的事情
            try {
                if(!isBackgroundRunning()){
                    mLogUtils.e("   退出操作  ProcessId=" + ServerId, 2);
                    logout();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        ServerId = android.os.Process.myPid();
        mLogUtils.e("   开启检查服务  ProcessId=" + ServerId , 2);
        taskMail = new TimerTask() {
            @Override
            public void run() {
                mLogUtils.e("   发送消息  ProcessId=" + ServerId , 2);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timerMail.schedule(taskMail, 15000, 5000);
        super.onCreate();
    }

    /**
     * 检测package是否在运行
     * @return
     */
    public boolean isBackgroundRunning() {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> processList = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : processList) {
            if (info.baseActivity.getPackageName().startsWith(PackageName)) {
                return true;
            }
        }
        return false;
    }
    /* (non-Javadoc)
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        try {
            if(taskMail!=null){
                taskMail.cancel();
            }
            if (timerMail!= null){
                timerMail.cancel();
            }
        } catch (Exception e) {
            mLogUtils.e("   资源异常，未关闭  ProcessId=" + ServerId,2);
        }
        reset();
        mLogUtils.e("   销毁服务  ProcessId=" + ServerId, 2);
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 退出登陆
     */
    public void logout() {
        String token = ShareUtil.getString(UIUtils.getContext(), "LoginToken", "");
        if (token.isEmpty()){
            return;
        }
        RetrofitManagerLoginOut.getInstance().getService(LoginService.class).doLogout(token).enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response == null || response.body() == null) {
                    onDestroy();
                    return;
                }
                BaseCallBack<String> body = response.body();
                ShareUtil.setString(UIUtils.getContext(), "LoginToken", "");
                onDestroy();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                onDestroy();
            }
        });
        onDestroy();
    }

    /**
     * 清除数据
     */
    private void reset() {

        mLogUtils.e("   重置数据  ProcessId=" + ServerId, 2);
        AppConstant.currentUser = null;
        AppConstant.CURRENT_INPLACE = null;
        MyApplication.registeredTower = null;
        MyApplication.currentNearestTower = null;
        MyApplication.nearSecondTower = null;//当前第二近的塔，如果为空表示 在塔20米以内
        MyApplication.crossSecondTower = null;
        MyApplication.nearestDistance = 0;//当前最近杆塔的最近距离
        MyApplication.nearSecondDistance = 0;//第二近杆塔的距离
        MyApplication.mAllTowersInMap.clear();
        MyApplication.mLineIdNamePairs.clear();
        MyApplication.gridlines.clear();
    }

}
