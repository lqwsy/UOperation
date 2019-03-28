package com.uflycn.uoperation.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.uflycn.uoperation.constant.AppConstant;


/**
 * Created by Ryan on 2016/7/13.
 */
public class DeviceUtils {

    /**
     * 获取应用程序的IMEI号
     */
    public static String getIMEI(Context context) {
        if (context == null) {
            Log.e("YQY","getIMEI  context为空");
        }
        TelephonyManager telecomManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telecomManager.getDeviceId();
        Log.e("YQY","IMEI标识：" + imei);
        return imei;
    }


    public static String getFixedIMEI(Context context){
        //如果DeviceNo已存在，则不需要再重新计算
        if(AppConstant.DEVICE_NO!=null && !"".equals(AppConstant.DEVICE_NO)) return AppConstant.DEVICE_NO;
        String deviceNo = SysUtils.getDeviceId(context);//getIMEI(context);
        deviceNo = SysUtils.stringToMD5(deviceNo);
        StringBuilder sb = new StringBuilder(deviceNo);
        int length = deviceNo.length() + deviceNo.length()/4;
        for (int i = 0; i < length; i++) {
            if (i%5 == 0) {
                sb.insert(i,"-");
            }
        }
        sb.deleteCharAt(0);

        String fixedDeviceNo = sb.toString();
        fixedDeviceNo = fixedDeviceNo.toUpperCase();
        AppConstant.DEVICE_NO = fixedDeviceNo;
        return fixedDeviceNo;
    }

    /**
     * 获取设备的系统版本号
     */
    public static int getDeviceSDK() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        Log.e("YQY","设备版本：" + sdk);
        return sdk;
    }

    /**
     * 获取设备的型号
     */
    public static String getDeviceName() {
        String model = android.os.Build.MODEL;
        Log.e("YQY","设备型号：" + model);
        return model;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 判断是否是debug 模式
     * @param context
     * @return
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {

        }
        return false;
    }

}
