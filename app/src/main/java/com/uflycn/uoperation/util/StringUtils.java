package com.uflycn.uoperation.util;

import android.text.TextUtils;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2014/8/18 0018.
 */
public class StringUtils {
    public static boolean isEmptyOrNull(String s) {
        return (s == null || s.equalsIgnoreCase(""));
    }

    public static String newGUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getTimeStamp() {
        return System.currentTimeMillis() + "";
    }

    public static String getNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(System.currentTimeMillis());
    }

    public static int count(String text, String sub) {
        int count = 0, start = 0;
        while ((start = text.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count;
    }

    public static boolean IPCheck(String ip) {
        if (count(ip, ".") != 3) {
            return false;
        }
        String[] IPs = ip.split(".");
        for (String ips : IPs) {
            if (TextUtils.isEmpty(ips) || ips.length() > 3) {
                return false;
            }
        }
        return true;
    }

    public static String getNow2() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(System.currentTimeMillis());
    }

    public static String convertTimestampToHourAndMins(long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(timeStamp);
    }

    public static String convertTimestampToDate(long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(timeStamp);
    }

    public static String convertTimestampToYYYYMMDDHHmm(long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(timeStamp);
    }

    public static String convertTimestampToYYYYMMDD(long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(timeStamp);
    }

    public static String convertNum2f(double num) {
        return String.format("%.2f", num);
        //        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        //        df.format(num);
    }

    public static String convertNum(double num) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
        return df.format(num);
    }

    /**
     * 判断是否为合法的日期时间字符串
     *
     * @param str_input
     * @param rDateFormat
     * @return boolean;符合为true,不符合为false
     */
    public static boolean isDate(String str_input, String rDateFormat) {
        if (!isEmptyOrNull(str_input)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str_input));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 将指定对象转换成字符串
     *
     * @param obj 指定对象
     * @return 转换后的字符串
     */
    public static String toString(Object obj) {
        StringBuffer buffer = new StringBuffer();
        if (obj != null) {
            buffer.append(obj);
        }
        return buffer.toString();
    }

    /**
     * 将指定字符串首字母转换成大写字母
     *
     * @param str 指定字符串
     * @return 返回首字母大写的字符串
     */
    public static String firstCharUpperCase(String str) {
        StringBuffer buffer = new StringBuffer(str);
        if (buffer.length() > 0) {
            char c = buffer.charAt(0);
            buffer.setCharAt(0, Character.toUpperCase(c));
        }
        return buffer.toString();
    }


    /**
     * 从edittext获取数据并返回
     *
     * @param editText
     * @return
     */
    public static double getValueFromEditText(EditText editText) {
        if (isEmptyOrNull(editText.getText().toString())) {
            return 0;
        } else {
            return Double.valueOf(editText.getText().toString());
        }

    }

    /**
     * 从edittext获取数据并返回
     *
     * @param editText
     * @return
     */
    public static Double getValueFromEText(EditText editText) {
        if (isEmptyOrNull(editText.getText().toString())) {
            return null;
        } else {
            return Double.valueOf(editText.getText().toString());
        }

    }

    /**
     * 从edittext获取数据并返回
     *
     * @param editText
     * @return
     */
    public static Double getValue1FromEditText(EditText editText) {
        if (isEmptyOrNull(editText.getText().toString())) {
            return null;
        } else {
            return Double.valueOf(editText.getText().toString());
        }

    }


    /**
     * 二维数组转string
     *
     * @param arry
     * @return
     */
    public static ArrayList<String> intArray2String(int[][] arry) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < arry.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < arry[i].length; j++) {
                sb.append(arry[i][j]);
            }
            list.add(sb.toString());
        }

        return list;
    }

    /**
     * 一维数组集合转string集合
     *
     * @param arrayList
     * @return
     */
    public static ArrayList<String> arrayInt2ArrayString(ArrayList<int[]> arrayList) {
        ArrayList<String> array = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < arrayList.get(i).length; j++) {
                sb.append(arrayList.get(i)[j]);
            }
            array.add(sb.toString());
        }
        return array;
    }


    /**
     * 数组转字符串
     *
     * @return
     */
    public static String array2String(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < array.length; j++) {
            sb.append(array[j]);
        }
        return sb.toString();
    }

    /**
     * list转字符串 加间隔符号
     *
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return list.isEmpty() ? "" : sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 判断是否是整型字符串
     */
    public static boolean isIntString(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 判断是否是float字符串
     */
    public static boolean isFloatString(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断double是否为空
     */
    public static boolean isDoubleNull(double value) {
        try {
            Double.valueOf(value);
            return false;
        } catch (Exception e) {
        }
        return true;
    }

}
