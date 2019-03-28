package com.uflycn.uoperation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.uflycn.uoperation.ui.login.view.LoginActivity;

/**
 * 开机启动的广播接收者
 * Created by xiaoyehai on 2017/9/18.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent mainActivityIntent = new Intent(context, LoginActivity.class);  // 要启动的Activity
            mainActivityIntent.putExtra("is_auto_login", 100);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }

    }
}
