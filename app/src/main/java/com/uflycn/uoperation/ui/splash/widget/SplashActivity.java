package com.uflycn.uoperation.ui.splash.widget;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.RecordSyncInfo;
import com.uflycn.uoperation.bean.RegistrationInfo;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.RecordSyncInfoDBHelper;
import com.uflycn.uoperation.service.UpdateService;
import com.uflycn.uoperation.ui.register.widget.RegisterActivity;
import com.uflycn.uoperation.ui.splash.presenter.SplashPresenter;
import com.uflycn.uoperation.ui.splash.presenter.SplashPresenterImpl;
import com.uflycn.uoperation.ui.splash.view.SplashView;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by user on 2016/7/18.
 */
public class SplashActivity extends Activity implements SplashView {

    private static final String SP_UPLOAD_SERVER = "upload_server";
    private static final String TAG = "SplashActivity";

    private final int SPLASH_DISPLAY_LENGHT = 2000; //延迟2秒

    private SplashPresenter mSplashPresenter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String flag = (String) msg.obj;
            moveToRegister(flag);
        }
    };

    private String mFlag = RegisterActivity.FLAG_SHOW_REGISTER;

    private Runnable mDelayRunnale = new Runnable() {
        @Override
        public void run() {
            moveToRegister(mFlag);
        }
    };


    private boolean isBind = false;

    private ImageView mLogoIv;

    private TextView mTitleTv;

    private TextView mNameTv;

    private TextView mNoticeTv;

    private AlertDialog.Builder normalDia;
    private RelativeLayout mTextInfoLayout;
    private UpdateService mUpdateService;
    private static final int SUCCESSCODE = 1;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mUpdateService = ((UpdateService.MyBinder) service).getUpdateService();
            try {
                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
                String fixedVersion = info.versionName.replace("v", "");
                mUpdateService.checkUpdate(fixedVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
/*
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                ToastUtil.show("使用本应用需要您允许出现在其他应用上，否则无法进行更新应用,立即前往设置");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }*/
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) { // 当前类不是该Task的根部，那么之前启动
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) { // 当前类是从桌面启动的
                    finish(); // finish掉该类，直接打开该Task中现存的Activity
                    return;
                }
            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
       /* if (mHandler != null){
            mHandler.postDelayed(mDelayRunnale, SPLASH_DISPLAY_LENGHT);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                doSomeThing();
            }
        }else{
            doSomeThing();
        }*/
        checkUpdate();
        doSomeThing();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void doSomeThing() {

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            ToastUtil.show("使用本应用需要您允许出现在其他应用上，否则无法进行更新应用,立即前往设置");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (XXPermissions.isHasPermission(this, Permission.Group.STORAGE)) {
            initAllDirs();
            init();
            getWindow().setBackgroundDrawable(null);
            initView();
            needPermission();
        } else {
            //判断是不是8.0以上，如果是8.0以上需要多申请一个安装未知应用的权限
            if (Build.VERSION.SDK_INT >= 23) {
                XXPermissions.with(this)
                        .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .permission(Permission.REQUEST_INSTALL_PACKAGES)
                        .permission(Permission.READ_PHONE_STATE, Permission.CAMERA) //支持请求6.0悬浮窗权限8.0请求安装权限 Permission.SYSTEM_ALERT_WINDOW,
                        .permission(Permission.Group.STORAGE, Permission.Group.LOCATION) //不指定权限则自动获取清单中的危险权限
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                doSomeThing();
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                ToastUtil.show("权限申请失败，强制退出程序");
                                finish();
                            }
                        });
            } else {
                XXPermissions.with(this)
                        .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .permission(Permission.READ_PHONE_STATE, Permission.CAMERA) //支持请求6.0悬浮窗权限8.0请求安装权限 Permission.SYSTEM_ALERT_WINDOW,
                        .permission(Permission.Group.STORAGE, Permission.Group.LOCATION) //不指定权限则自动获取清单中的危险权限
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                doSomeThing();
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                ToastUtil.show("权限申请失败，强制退出程序");
                                finish();
                            }
                        });
            }

        }
    }

    private void initView() {
        mLogoIv = (ImageView) findViewById(R.id.iv_logo);
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mNameTv = (TextView) findViewById(R.id.tv_name);
        mNoticeTv = (TextView) findViewById(R.id.tv_notice);
        mTextInfoLayout = (RelativeLayout) findViewById(R.id.rl_text_info);

        AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.anim_alpha_0_to_1);
        mTextInfoLayout.startAnimation(animation);
        mNoticeTv.startAnimation(animation);
        RegistrationInfo info = mSplashPresenter.getRegInfo();
        if (info != null) {
            mNameTv.setText(info.getCompany() + "");
            AppConstant.COMPANY_NAME = info.getCompany();
        } else {
            mSplashPresenter.downloadRegisterCode();
        }
    }


    private void init() {
        mSplashPresenter = new SplashPresenterImpl(this, this);
        //        mHandler.postDelayed(mDelayRunnale, SPLASH_DISPLAY_LENGHT);
        mStartTime = System.currentTimeMillis();
        mSplashPresenter.judge();
    }

    private void needPermission() {
        // When you need the permission, e.g. onCreate, OnClick etc.
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() > 0) {
                ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), 1);
            }
        }
    }

    private void checkUpdate() {
        Intent intent = new Intent(this, UpdateService.class);
        // 先start 再bind
        startService(intent);
        isBind = bindService(intent, mConn, BIND_AUTO_CREATE);
        Log.d(TAG, "checkUpdate: 开启服务");
    }

    @Override
    public void moveToRegister(String flag) {
        removeCallback();
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(RegisterActivity.FLAG, flag);
        System.out.println("设置flag" + flag);
        startActivity(intent);
        finish();
    }

    @Override
    public void removeCallback() {
        if (mHandler != null && mDelayRunnale != null) {
            mHandler.removeCallbacks(mDelayRunnale);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        removeCallback();
    }

    @Override
    public void setFlag(final String flag) {
        //        mFlag = flag;
        long mEndTime = System.currentTimeMillis();
        //间隔时间
        long interval = mEndTime - mStartTime;
        System.out.println("间隔时间：" + interval);
        if (interval < 2000) {
            long postTime = 2000 - interval;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.obj = flag;
                    mHandler.sendMessage(message);
                }
            }, postTime);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Message message = Message.obtain();
                    message.obj = flag;
                    mHandler.sendMessage(message);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (isBind && mConn != null) {
            unbindService(mConn);
            isBind = false;
        }

    }


    private void initAllDirs() {
        String root = IOUtils.getRootStoragePath(this);
        for (String dir : AppConstant.DIRECTORYS) {
            File d = new File(root + dir);
            if (!d.exists()) {
                d.mkdirs();
            }
        }
        String root1 = IOUtils.getUflyRootPath(this);
        for (String dir : AppConstant.DIRECTORYS1) {
            File d = new File(root1 + dir);
            if (!d.exists()) {
                d.mkdirs();
            }
        }
        initRecordSyncInfo();
    }


    private void initRecordSyncInfo() {
        RecordSyncInfoDBHelper helper = RecordSyncInfoDBHelper.getInstance();
        List<RecordSyncInfo> list = helper.getList();
        if (list == null || list.size() < 9) {
            helper.clearDatas();
            for (int i = 1; i <= 9; i++) {
                RecordSyncInfo recordSyncInfo = new RecordSyncInfo();
                recordSyncInfo.setCategory(i);
                recordSyncInfo.setLastSyncTime(AppConstant.DATE_INIT_TIME);
                helper.insert(recordSyncInfo);
            }
        }
    }
}
