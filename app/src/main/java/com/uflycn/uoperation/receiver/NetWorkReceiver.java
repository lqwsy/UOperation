package com.uflycn.uoperation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.constant.ContantUrl;
import com.uflycn.uoperation.listeners.NetWorkChangeListener;
import com.uflycn.uoperation.util.Network;
import com.xflyer.utils.ThreadPoolUtils;

public class NetWorkReceiver extends BroadcastReceiver{
    private static NetWorkChangeListener mListener;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {//监听网络变化
            localBroadcastManager = LocalBroadcastManager.getInstance(context);
            tryToGetNetWork();
        }
    }
    private void tryToGetNetWork() {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                boolean available = Network.pingIpAddress(ContantUrl.IP_ADDREDDS);
                //如果Token == null 重新获取Token
                if(AppConstant.NET_WORK_AVAILABLE != available ){
                    AppConstant.NET_WORK_AVAILABLE = available;
                    if(available){
                        Network.getToken(mListener);
                        //如果ping 通了 开启上传队列
                        Intent intent = new Intent("com.uflycn.uoperation.TEMP_TASK_NET_CHANGE");
                        localBroadcastManager.sendBroadcast(intent);
                    }
                }
            }
        });
    }

    public static void addNetWorkChangeListener(NetWorkChangeListener listener){
        mListener = listener;
    }

}
