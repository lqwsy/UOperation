package com.uflycn.uoperation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2014/8/18 0018.
 */
public class SysUtils {
    private static final String ROOTNAME = "LIGHT";

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    public static void writeCache(Context context, String name, String value) {
        SharedPreferences pref = context.getSharedPreferences(ROOTNAME, Context.MODE_PRIVATE);
        pref.edit().putString(name, value).commit();
    }

    public static String readCache(Context context, String name) {
        SharedPreferences pref = context.getSharedPreferences(ROOTNAME, Context.MODE_PRIVATE);
        return pref.getString(name, "");
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context, int dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void toggleInputMethod(Context context) {
        InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showInputMethod(View view) {
        view.requestFocus();
        InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideInputMethod(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(
                    v.getApplicationWindowToken(), 0);
        }
    }

    public static String newGUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static CoordConvertManager.CoordTrans7Param param = null;

    public static synchronized double[] ConvertWGS842BJ54(Context context, double x, double y, double z) {
        CoordConvertManager manager = null;
        CoordConvertManager.ZoneType zoneType = CoordConvertManager.ZoneType.Zone6;
        if (param == null) {
            param = new CoordConvertManager.CoordTrans7Param();
            String path = IOUtils.getRootStoragePath(context);
            File dir = new File(path);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                File file = null;
                for (File f : files) {
                    if (f.getName().toLowerCase().endsWith(".pam")) {
                        file = f;
                        break;
                    }
                }
                if (file != null) {
                    String str = IOUtils.readPamFile(file);
                    String[] v = str.split("\\$");
                    if (v.length == 9) {
                        try {
                            if (v[1] == "3") {
                                zoneType = CoordConvertManager.ZoneType.Zone3;
                            } else {
                                zoneType = CoordConvertManager.ZoneType.Zone6;
                            }
                            param.setDx(Double.parseDouble(v[2]));
                            param.setDy(Double.parseDouble(v[3]));
                            param.setDz(Double.parseDouble(v[4]));
                            param.setRx(Double.parseDouble(v[5]));
                            param.setRy(Double.parseDouble(v[6]));
                            param.setRz(Double.parseDouble(v[7]));
                            param.setK(Double.parseDouble(v[8]));
                        } catch (Exception ex) {
                            Log.e("ConvertWGS842BJ54", ex.getMessage());
                        }
                    }
                }
            }
        }

        manager = new CoordConvertManager(zoneType, param);

        try {
            int m_centerLine = 0;
            if (x >= 72 && x < 78) {
                m_centerLine = 75;
            } else if (x >= 78 && x < 84) {
                m_centerLine = 81;
            } else if (x >= 84 && x < 90) {
                m_centerLine = 87;
            } else if (x >= 90 && x <= 96) {
                m_centerLine = 93;
            } else {
                m_centerLine = 84;
            }
            double[] result = manager.WGS84ToBJ54(y, x, z, m_centerLine);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    public static String getDeviceId(Context context) {
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imeistring = tManager.getDeviceId();
        if (!StringUtils.isEmptyOrNull(imeistring)) {
            return imeistring;
        }

        String imsistring = tManager.getSubscriberId();
        if (!StringUtils.isEmptyOrNull((imsistring))) {
            return imsistring;
        }

        String androidId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (!StringUtils.isEmptyOrNull((androidId))) {
            return androidId;
        }

        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "UNKNOWN"));
        } catch (Exception ignored) {
            return "UFLY";
        }
        if (!StringUtils.isEmptyOrNull((serialnum))) {
            return serialnum;
        }
        return "UFLY";
    }

    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        String md5Str = hex.toString();
        int length = md5Str.length();
        if (length < 16) {
            md5Str = "1BF29B766F14C2DA";
        }
        return md5Str;
    }

    /**
     * 隐藏navigationBar
     * @param view   decorView
     */
    public static void hideNavigationBar(View view) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if( android.os.Build.VERSION.SDK_INT >= 19 ){
      //      uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        view.setSystemUiVisibility(uiFlags);
    }
}
