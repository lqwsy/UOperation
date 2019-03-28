package com.uflycn.uoperation.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.LoginCallbackEntity;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.constant.ContantUrl;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.http.LoginService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.listeners.NetWorkChangeListener;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 检测网络
 *
 * @author Administrator
 */
public class Network {

    /**
     * 网络不可用
     */
    public static final int NONETWORK = 0;
    /**
     * 是wifi连接
     */
    public static final int WIFI = 1;
    /**
     * 不是wifi连接
     */
    public static final int NOWIFI = 2;

    /**
     * 检验网络连接 并判断是否是wifi连接
     *
     * @param context
     * @return <li>没有网络：Network.NONETWORK;</li> <li>wifi 连接：Network.WIFI;</li> <li>mobile 连接：Network.NOWIFI</li>
     */
    public static int checkNetWorkType(Context context) {

        if (!checkNetWork(context)) {
            return Network.NONETWORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            return Network.WIFI;
        else
            return Network.NOWIFI;
    }

    /**
     * 检测网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean checkNetWork(Context context) {
        // 1.获得连接设备管理器
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null) {
            return ni.isAvailable();
        }
        return false;
    }

    /**
     * 判断GPS是否已经打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }


    /**
     * 判断WIFI是否已打开
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断3G网络是否已打开
     *
     * @param context
     * @return
     */
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * @return --经检测，此方法不可用。总是返回false
     * @author suncat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @ip 要Ping的IP地址
     */
    public static final boolean ping(String ip) {

        String result = null;
        try {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 3 -w 1 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            /*InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}*/
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {

        }
        return false;
    }


    //这个方法可以代替 查询网络是否可用
    public static boolean pingIpAddress(String ipAddress) {
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 3 -w 100 " + ipAddress);
            int status = process.waitFor();
         /*   InputStream input = process.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            System.out.print(stringBuffer.toString());*///输出ping的结果
            if (status == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void getToken(final NetWorkChangeListener netWorkChangeListener) {
        String fixedVersion = "";
        try {
            PackageInfo info = MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), PackageManager.GET_ACTIVITIES);
            fixedVersion = "v" + info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (AppConstant.TOKEN != null && !AppConstant.TOKEN.equalsIgnoreCase("")) {
            if (netWorkChangeListener != null) {
                netWorkChangeListener.onNetWorkChange(AppConstant.NET_WORK_AVAILABLE);
            }
            return;
        }
        if (AppConstant.currentUser != null && AppConstant.currentUser.getAccount() == null) {
            AppConstant.currentUser = UserDBHellper.getInstance().getUser(AppConstant.currentUser.getUserId());
        }
        if (AppConstant.currentUser == null) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ContantUrl.BASE_URL)
                .build();

        retrofit.create(LoginService.class).doLogin(AppConstant.currentUser.getAccount(), AppConstant.currentUser.getPassword(), "0", fixedVersion, getIMEI(MyApplication.getContext())).enqueue(new Callback<BaseCallBack<LoginCallbackEntity>>() {
            @Override
            public void onResponse(Call<BaseCallBack<LoginCallbackEntity>> call, Response<BaseCallBack<LoginCallbackEntity>> response) {
                if (response.body() == null || response.body().getData() == null) {
                    return;
                }
                AppConstant.TOKEN = response.body().getData().getTOKEN();
                RetrofitManager.getInstance().resetRetrofit();
                netWorkChangeListener.onNetWorkChange(true);
            }

            @Override
            public void onFailure(Call<BaseCallBack<LoginCallbackEntity>> call, Throwable t) {
                netWorkChangeListener.onNetWorkChange(false);
            }
        });
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        return imei;
    }


}
