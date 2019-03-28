package com.uflycn.uoperation.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.fragment.mytour.view.MyTourFragment;
import com.uflycn.uoperation.ui.main.view.MainActivity;

/**
 * 显示通知栏工具类
 * Created by Administrator on 2016-11-14.
 */

public class Notificationutil {
    private static NotificationCompat.Builder builder;
    private static String voiceMsg;
    private static String tickerMsg;
    private static String summaryMsg;
    private static String intentMsg;

    /**
     * 悬挂式，支持6.0以上系统
     *
     * @param context
     */
    public static void showFullScreen(Context context, String title, String msg, int type) {
        if (type == 1){
            voiceMsg = "您有新的工单任务,请及时处理！";
            tickerMsg = "工单任务来了";
            summaryMsg = "点击进入工单任务查看详情";
            intentMsg = AppConstant.WORK_SHEET;
        }else if (type == 0){
            voiceMsg = "您有新的临时通知,请及时查看！";
            tickerMsg = "临时通知来了";
            summaryMsg = "点击进入临时通知查看详情";
            intentMsg = AppConstant.TEMP_TASK;
        }
        Log.d("Server","声音");

        new MyTourFragment.NavipathListener().onGetNavigationText(voiceMsg);
        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.new_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle(title);
        builder.setContentText(msg);
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        builder.setTicker(tickerMsg);
        builder.setWhen(System.currentTimeMillis());
        android.support.v4.app.NotificationCompat.BigTextStyle style = new android.support.v4.app.NotificationCompat.BigTextStyle();
        style.bigText(msg);
        style.setBigContentTitle(title); //SummaryText没什么用 可以不设置
        style.setSummaryText(summaryMsg);
        builder.setStyle(style);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("toValue", intentMsg);
        PendingIntent pend = PendingIntent.getActivity(context,201,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pend);
        builder.setFullScreenIntent(pend, true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, builder.build());
    }

    public static void quitNotify(Context context){
        if (builder != null){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.cancel(3);
        }
    }

}