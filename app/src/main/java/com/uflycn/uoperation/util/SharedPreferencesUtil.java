package com.uflycn.uoperation.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedPreferencesUtil {

    public static final String MAP_CONFIG = "MAP_CONFIG";
    public static final String URL_CONFIG = "URL_CONFIG";
    public static final String ACCOUNT_CONFIG = "ACCOUNT_CONFIG";
    public static final String SavedACCOUNT_CONFIG = "All_ACCOUNT_CONFIG";

    //String的保存及获取
    public static void saveDataToSharedPreferences(Context context,
                                                   String key, String value, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getDataToSharedPreferences(Context context,
                                                    String key, String defaluevalue, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        return sp.getString(key, defaluevalue);
    }

    //Boolean的保存及获取
    public static void saveDataToSharedPreferences(Context context,
                                                   String key, boolean value, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getDataToSharedPreferences(Context context,
                                                     String key, boolean defaluevalue, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        return sp.getBoolean(key, defaluevalue);
    }

    //FLOAT的保存及获取
    public static void saveDataToSharedPreferences(Context context,
                                                   String key, float value, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).commit();
    }

    public static float getDataToSharedPreferences(Context context,
                                                 String key, float defaluevalue, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        return sp.getFloat(key, defaluevalue);
    }



    //int的保存及获取
    public static void saveDataToSharedPreferences(Context context,
                                                   String key, int value, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getDataToSharedPreferences(Context context,
                                                 String key, int defaluevalue, String configName) {
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        return sp.getInt(key, defaluevalue);
    }

    public static Map<String,?> getAll(Context context,String configName){
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static void remove(Context context,String configName,String account){
        SharedPreferences sp = context.getSharedPreferences(configName, context.MODE_PRIVATE);
        sp.edit().remove(account).commit();
    }

}
