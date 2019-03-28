package com.uflycn.uoperation.util;

import android.util.Log;

/**
 * 检测时间差
 * Created by Administrator on 2018/1/25.
 */
public class CheckTimeUtil {
    private static final String TAG = "CheckTimeUtil";
    private long time_old;
    private long time_new;

    public CheckTimeUtil(){
        time_old = System.currentTimeMillis();
        Log.d(TAG, "开始检测时间差L____");
    }

    /**
     * 打印log显示时间差
     * @param showLog 提示信息
     */
    public void showDelteTime(String showLog){
        time_new = System.currentTimeMillis();
        Log.d(TAG, showLog + (time_new - time_old));
        time_old = time_new;
    }


}
