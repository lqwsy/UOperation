package com.uflycn.uoperation.service;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.http.RegisterFactory;
import com.uflycn.uoperation.util.AppInnerDownLoder;
import com.uflycn.uoperation.util.Network;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ryan on 2016/12/29.
 */
public class UpdateService extends Service {

    private static final String TAG = "UpdateService";

    public interface ProgressListener {
        void onProgressChange(int doneByte, int totalByle, int status);
    }

    //    public static final String APP_NAME = "运维管理_赣西";
//        public static final String APP_NAME = "运维管理_赣州";

    public static final String APP_NAME = "运维管理_山东";

    //    public static final String APP_NAME = "运维管理_萍乡";
    //    public static final String APP_NAME = "运维管理_演示";
    //    public static final String APP_NAME = "运维管理_宜春";

    private static final String DOWNLOAD_URL = "http://www.uflycn.com:88/AppVersion/DownloadApk?appName=" + APP_NAME;

    public static final String FILE_NAME = "UOperation.apk";

    private static final String SP_DOWNLOAD_ID = "download_id";

    private static final long DEFAULT_DOWNLOAD_ID = -1;

    private DownloadManager mDownloadManager;

    private long mDownloadId = DEFAULT_DOWNLOAD_ID;

    private DownloadReceiver mReceiver;

    private DownloadChangeObserver mObserver;

    private ScheduledExecutorService mScheduledExecutorService;

    private MyBinder mBinder;

    private String versionName;

    private Handler popupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    AppInnerDownLoder.downLoadApk(UpdateService.this, DOWNLOAD_URL, APP_NAME);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new MyBinder();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        return super.onStartCommand(intent, flags, startId);
    }


    public void executeUpdate() {
        String path = getExternalFilesDir("download").getPath();
        File file = new File(path, UpdateService.FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DOWNLOAD_URL));
        /**设置用于下载时的网络状态*/
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        /**设置通知栏是否可见*/
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        /**设置漫游状态下是否可以下载*/
        request.setAllowedOverRoaming(false);
        /**如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，
         我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true.*/
        request.setVisibleInDownloadsUi(true);
        /**设置文件保存路径*/
        request.setDestinationInExternalFilesDir(getApplicationContext(), "download", FILE_NAME);
        /**将下载请求放入队列， return下载任务的ID*/
        mDownloadId = mDownloadManager.enqueue(request);
        //执行下载任务时注册广播监听下载成功状态
        registerBroadcast();

        Toast.makeText(UIUtils.getContext(), "正在下载,下载完成后自动安装", Toast.LENGTH_LONG).show();
    }

    private void registerObserver() {
        mObserver = new DownloadChangeObserver(UIUtils.getHandler());
        getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), false, mObserver);
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        mReceiver = new DownloadReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    private void unRegisterBroadcast() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    /**
     * 检查是否需要更新
     *
     * @param versionName
     * @return
     */
    public void checkUpdate(String versionName) {
        checkUpdate(versionName, false);
    }

    private String addName(String strAppName) {
        if (AppConstant.COMPANY_NAME.contains("赣西")) {
            strAppName += "_赣西";
        } else if (AppConstant.COMPANY_NAME.contains("赣州")) {
            strAppName += "_赣州";
        } else if (AppConstant.COMPANY_NAME.contains("宁波")) {
            strAppName += "_宁波";
        } else if (AppConstant.COMPANY_NAME.contains("萍乡")) {
            strAppName += "萍乡";
        } else if (AppConstant.COMPANY_NAME.contains("九江")) {
            strAppName += "九江";
        } else if (AppConstant.COMPANY_NAME.contains("测试") || AppConstant.COMPANY_NAME.contains("ufly")
                || AppConstant.COMPANY_NAME.contains("优飞") || AppConstant.COMPANY_NAME.contains("UFLY")) {
            strAppName += "_优飞";
        }
        return strAppName;
    }


    public void checkUpdate(String versionName, final boolean showToast) {

        //        String strAppName = addName(APP_NAME);

        RegisterFactory.getUpgraService().CheckUpgrade(APP_NAME, versionName).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        parseUpgradeInfo(result, showToast);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (showToast) {
                    ToastUtil.show("查询失败，请检查网络后重试");
                }
            }
        });
    }

    /**
     * 解析更新信息
     *
     * @param upgradeInfo
     */
    private void parseUpgradeInfo(String upgradeInfo, boolean showToast) {
        String upgradeResult[] = upgradeInfo.split("\\|");

        //result = "1.1.0|1|我是第一条 我是第二条 我是第三条 我是第四条";  原来的更新日志

        try {
            int result = Integer.parseInt(upgradeInfo);
            if (result == 0) {
                // 不需要更新
                if (showToast) {
                    ToastUtil.show("当前已是最新版本");
                }
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (upgradeResult.length >= 2) {
            versionName = upgradeResult[0];
            if (!TextUtils.isEmpty(versionName) && versionName.charAt(0) != 'v') {
                //不是v开头
                versionName = "v" + versionName;
            }
            String detail = "";
            if (upgradeResult.length > 2) {
                detail = upgradeResult[2];
            }
            if (upgradeResult[1].equals(0 + "")) {
                // 非强制更新
                showDialog(false, detail);
            } else if (upgradeResult[1].equals(1 + "")) {
                // 强制更新
                showDialog(true, detail);
            }
        }
    }


    private void showDialog(final boolean enforce, String detail) {
        AlertDialog dialog = null;
        final boolean exist = existInLocal();
        StringBuilder sb = new StringBuilder();
        sb.append("有新的版本 " + versionName);
        if (exist) {
            // 已下载
            sb.append("(已下载)");
        }
        sb.append("\n");
        String updateList[] = detail.split("#");
        if (updateList.length > 0) {
            sb.append("更新内容: \n");
            for (int i = 0; i < updateList.length; i++) {
                sb.append(i + 1 + ". " + updateList[i] + "\n");
            }
        }


        if (enforce) {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("发现新版本")
                    .setMessage(sb.toString())
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (exist) {
                                String path = getExternalFilesDir("download").getPath();
                                File file = new File(path, FILE_NAME);
                                Uri uri = Uri.fromFile(file);
                                installApk(UpdateService.this, uri);
                            } else {
                                int status = Network.checkNetWorkType(UpdateService.this);
                                if (status == Network.NOWIFI) {
                                    showNoticeDialog(enforce);
                                } else {
                                    //executeUpdate();
                                    //AppInnerDownLoder.downLoadApk(UpdateService.this, DOWNLOAD_URL, "运维管理");
                                    //MyApplication.getInstance().exit();
                                    popupHandler.sendEmptyMessage(0);
                                }
                            }
                        }
                    })
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //CustomApplication.getInstance().exit();
                        }
                    })
                    .setCancelable(false)
                    .create();
        } else {
            dialog = new AlertDialog.Builder(this)
                    .setTitle("发现新版本")
                    .setMessage(sb.toString())
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (exist) {
                                String path = getExternalFilesDir("download").getPath();
                                File file = new File(path, FILE_NAME);
                                Uri uri = Uri.fromFile(file);
                                installApk(UpdateService.this, uri);
                            } else {
                                int status = Network.checkNetWorkType(UpdateService.this);
                                if (status == Network.NOWIFI) {
                                    showNoticeDialog(enforce);
                                } else {
                                    //executeUpdate();
                                    popupHandler.sendEmptyMessage(0);
                                }
                            }
                        }
                    })
                    .setNegativeButton("稍后", null)
                    .create();
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        dialog.show();
    }

    private void showNoticeDialog(final boolean enforce) {
        AlertDialog notiDialog = new AlertDialog.Builder(UpdateService.this)
                .setTitle("注意")
                .setMessage("您现在处于移动网络中,下载会消耗您的流量,确定要下载吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //executeUpdate();
                        popupHandler.sendEmptyMessage(0);
                        if (enforce) {
                            // 强制
                            //CustomApplication.getInstance().exit();
                        }
                    }
                })
                .setNegativeButton("取消", null).create();

        notiDialog.setCancelable(false);
        notiDialog.setCanceledOnTouchOutside(false);
        notiDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        notiDialog.show();
    }


    private boolean existInLocal() {
        String path = getExternalFilesDir("download").getPath();
        File file = new File(path, UpdateService.FILE_NAME);
        if (!file.exists()) {
            return false;
        } else {
            // 存在文件
            try {
                String filePath = file.getPath();
                PackageInfo info = getPackageManager().getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
                if (info != null) {
                    int targetCode = info.versionCode;
                    PackageInfo myInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
                    int myCode = myInfo.versionCode;
                    if (myCode <= targetCode && info.versionName.equals(versionName)) {
                        return true;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    /**
     * 安装APK
     *
     * @param context
     * @param apkPath 安装包的路径
     */
    public static void installApk(Context context, Uri apkPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //此处因为上下文是Context，所以要加此Flag，不然会报错
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(apkPath, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    class DownloadChangeObserver extends ContentObserver {
        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mScheduledExecutorService.scheduleAtFixedRate(mUpdateRunnable, 0, 2, TimeUnit.SECONDS);
        }
    }


    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            // 查询下载进度
            int status[] = getBytesAndStatus(mDownloadId);

        }
    };

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId
     * @return
     */
    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }


    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                    int id = (int) intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, DEFAULT_DOWNLOAD_ID);
                    if (mDownloadId == id && id != -1 && mDownloadManager != null) {
                        // 下载完成
                        Uri downloadUri = mDownloadManager.getUriForDownloadedFile(mDownloadId);
                        if (downloadUri != null) {
                            String path = downloadUri.getPath();
                            //LogUtils.getInstance(UpdateService.this).e("下载完成,路径:" + path);
                            installApk(UpdateService.this, downloadUri);
                        }
                    }
                    break;

                case DownloadManager.ACTION_NOTIFICATION_CLICKED:
                    long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                    //点击通知栏取消下载
                    mDownloadManager.remove(ids);
                    ToastUtil.show("已经取消下载");
            }
        }
    }


    public class MyBinder extends Binder {
        public UpdateService getUpdateService() {
            return UpdateService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

}

