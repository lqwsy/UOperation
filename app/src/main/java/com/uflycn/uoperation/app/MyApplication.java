package com.uflycn.uoperation.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Process;
import android.support.multidex.MultiDexApplication;

import com.uflycn.uoperation.bean.AssistRecord;
import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TypeOfWork;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.util.IOUtils;
import com.xflyer.exception.CrashLogCatchUtils;
import com.xflyer.services.GaodeLocationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public class MyApplication extends MultiDexApplication {

    private static Context mContext;
    private static Handler mHandler;
    private static int mMainThreadId;
    //    private static DisplayImageOptions options;

    public static Tower registeredTower;//当前最后一次到位登记的杆塔
    public static Tower redisteredSecondTower;//当前离到位登记最近的它
    public static Tower currentNearestTower;//当前最近的杆塔
    public static Tower nearSecondTower;//当前第二近的塔，如果为空表示 在塔20米以内
    public static Tower crossSecondTower;
    public static double nearestDistance;//当前最近杆塔的最近距离
    public static double nearSecondDistance;//第二近杆塔的距离
    public static Map<String, List<Tower>> mAllTowersInMap;//<线路ID，线路的塔集合>用来存放所有加载进来的点  计算最近的塔 以及线路
    public static Map<String, String> mLineIdNamePairs;//线路id 名称，存放已开启的线路，不超过3
    public static boolean isRegisterAuto = true; // 是否自动获取到位登记杆塔

    public static String mPlanPatrolExecutionId="";

    public static Tower specialRegisteredStartTower;//特殊巡视起始塔
    public static Tower specialRegisteredEndTower;//当前最后一次到位登记的杆塔

    public static DayPlan mCurrentDayPlan = null;

    public static StringBuffer mDayPlanLineSb;
    public static StringBuffer mOpenDayPlanLineSb;
    /**
     * 0:默认 1：计划线路开启 2：每日任务开启 3:我的任务
     */
    public static int gridlineTaskStatus = 0;
    public static String mTypeString = "";

    public static Map<Integer, String> mDayPlanLineMap;
    public static Map<String, String> mOpenDayPlanLineMap;

    public static String currentTowerDefectLevel;
    public static boolean isFirstShow = true;
    /**
     * 已开启线路的集合
     */
    public static Map<Integer, Gridline> gridlines;
    public static Map<Integer, List<TypeOfWork>> mWorkTypeMap;

    //临时塔
    public static Map<Integer, List<Tower>> mTempTowerMap;
    //临时线路集合
    public static Map<Integer, Gridline> mTempLineMap;

    public static List<AssistRecord> assistRecords;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mHandler = new Handler();
        //获取当前线程的id,此处是主线程id
        mMainThreadId = Process.myTid();
        CrashLogCatchUtils crashHandler = CrashLogCatchUtils.getInstance();
        crashHandler.init(getApplicationContext(), IOUtils.getRootStoragePath(this) + AppConstant.DIR_LOG);
        initGaodeLocationService();//开启定位服务
        //        initImageLoader();
        mAllTowersInMap = new HashMap<>();
        mLineIdNamePairs = new HashMap<>();
        mTempTowerMap = new HashMap<>();
        mTempLineMap = new HashMap<>();
        gridlines = new HashMap<>();
        mDayPlanLineMap = new HashMap<>();
        mOpenDayPlanLineMap = new HashMap<>();
        mDayPlanLineSb = new StringBuffer();
        mOpenDayPlanLineSb = new StringBuffer();
        assistRecords = new ArrayList<>();
        mWorkTypeMap = new HashMap<>();
    }


    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }


    private void initGaodeLocationService() {
        startService(new Intent(this, GaodeLocationService.class));
    }
/*

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getContext())
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                //            .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();//开始构建

        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                //                .showImageForEmptyUri(R.drawable.no_pic)
                //                .showImageOnFail(R.drawable.no_pic)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                //        .imageScaleType(ImageScaleType.)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                //       .delayBeforeLoading(200)//载入图片前稍做延时可以提高整体滑动的流畅度
                .build();
    }

    public static DisplayImageOptions getDisplayImageOptions() {
        return options;
    }
*/


}
