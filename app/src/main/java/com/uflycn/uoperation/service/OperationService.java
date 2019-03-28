package com.uflycn.uoperation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.PatrolTrack;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.NaviLocationEvent;
import com.uflycn.uoperation.eventbus.NaviPathEvent;
import com.uflycn.uoperation.eventbus.NaviRoutesEvent;
import com.uflycn.uoperation.eventbus.StartNaviEvent;
import com.uflycn.uoperation.eventbus.StopAllTaskEvent;
import com.uflycn.uoperation.eventbus.UpdateLocationEvent;
import com.uflycn.uoperation.eventbus.UpdateNaviInfoEvent;
import com.uflycn.uoperation.eventbus.UpdateTowerEvent;
import com.uflycn.uoperation.greendao.PatrolTrackDBHleper;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.listeners.DistanceCallback;
import com.uflycn.uoperation.listeners.NavipathListener;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.LogUtils;
import com.uflycn.uoperation.util.Notificationutil;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.WebSocket_Client;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.services.GaodeLocationService;
import com.xflyer.utils.CoordinateUtils;
import com.xflyer.utils.LatLngUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.drafts.Draft_6455;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.uflycn.uoperation.constant.AppConstant.currentUser;
import static com.uflycn.uoperation.constant.ContantUrl.IP_ADDREDDS;
import static com.uflycn.uoperation.constant.ContantUrl.PUSH_PORT;

/**
 * Created by Administrator on 2017/9/7.
 */
public class OperationService extends Service {
    private ScheduledExecutorService mScheduledExecutorService;//定时任务的线程池
    private ScheduledFuture mUploadScheduledFuture;
    private ScheduledFuture mUpdateLocationFuture;
    private Reference<DistanceCallback> mDistanceReference;
    private AMapNavi mAmapNavi;
    private Gson mGosn;
    private boolean isUpdate = false;
    //TODO:  我们的端口 6013
    private final String ADDRESS = "ws://" + IP_ADDREDDS + ":" + PUSH_PORT + "/" + (currentUser == null ? "" : currentUser.getUserId());
    private WebSocket_Client webSocket_client;
    private URI uri;
    private String notic_content;
    private String notic_title;
    private LocalBroadcastManager localBroadcastManager;
    private double lastLat;
    private double lastLng;
    private int MessageType;
    private int sysTaskId;
    private LogUtils mLogUtils = LogUtils.getInstance();
    private PowerManager.WakeLock mWakeLock;
    private boolean isFrist = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new OperationBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getLock(this);
        mGosn = new Gson();
        mScheduledExecutorService = Executors.newScheduledThreadPool(5);//线程池
        AppConstant.mDistance = ShareUtil.getInt(getBaseContext(), AppConstant.SP_Tower_Radius, 30);
        getTowerRadius();
        startUploadPatrolTrack(10);
        UploadPatrolTrack(5 * 60);
        startLocationUpdate(1);
        openWebSocket();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        EventBus.getDefault().register(this);
    }

    private void openWebSocket() {
        uri = URI.create(ADDRESS);
        Draft_6455 draft_17 = new Draft_6455();
        webSocket_client = new WebSocket_Client(uri, draft_17);
        webSocket_client.connect();
        webSocket_client.setRecviveMessage(new WebSocket_Client.onMessageCallBack() {
            @Override
            public void onNotificationContent(String msg) {
                parseJSONWithJSONObject(msg);
                Notificationutil.showFullScreen(getBaseContext(), notic_title, notic_content, MessageType);
                Intent intent;
                if (MessageType == 0) {
                    intent = new Intent(AppConstant.TEMP_TASK_NUM_ADD);
                } else {
                    intent = new Intent(AppConstant.WORK_SHEET_NUM_ADD);
                }
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            //将json字符串jsonData装入JSON数组，即JSONArray
            //jsonData可以是从文件中读取，也可以从服务器端获得
            JSONObject jsonObject = new JSONObject(jsonData);
            MessageType = jsonObject.optInt("MessageType");
            if (MessageType == 1) {
                //工单
                sysTaskId = jsonObject.optInt("sysTaskId");
                notic_content = "点击查看";
                notic_title = "您有新的工单任务";
            } else if (MessageType == 0) {
                notic_content = jsonObject.optString("MessageContent");
                notic_title = jsonObject.optString("Title");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UploadPatrolTrack(int duringTime) {
        mUploadScheduledFuture = mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                if (AppConstant.NET_WORK_AVAILABLE) {
                    final List<PatrolTrack> patrolTracks = PatrolTrackDBHleper.getInstance().getNeedUploadList();
                    if (patrolTracks.size() == 0) {
                        return;
                    }
                    PatrolTrack lastestP = patrolTracks.get(0);
                    int second = DateUtil.getOffsetSeconds(DateUtil.parse(lastestP.getCreateDate()), new Date());
                    if (second < 30) {
                        patrolTracks.remove(0);
                    }
                    String jsonStr = mGosn.toJson(patrolTracks);
                    RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
                    RetrofitManager.getInstance().getService(ApiService.class).postPatrolTrack(requestBody).enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
                        @Override
                        public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                            if (response != null && response.body() != null && response.body().getCode() == 1) {
                                for (PatrolTrack patrolTrack : patrolTracks) {
                                    patrolTrack.setUploadFlag(1);
                                }
                                PatrolTrackDBHleper.getInstance().updateList(patrolTracks);
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                        }
                    });
                }

            }
        }, 1, duringTime, TimeUnit.SECONDS);
    }


    /**
     * 获取检索距离
     */
    private void getTowerRadius() {
        RetrofitManager.getInstance().getService(ApiService.class).getTowerRadius("").enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response == null || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 1) {
                    AppConstant.mDistance = Integer.valueOf(response.body().getData());
                    ShareUtil.setInt(getBaseContext(), AppConstant.SP_Tower_Radius, Integer.valueOf(response.body().getData()));
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 记录和上传轨迹信息
     *
     * @param duringTime 上传时间间隔
     */
    private void startUploadPatrolTrack(int duringTime) {
        mUploadScheduledFuture = mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                PatrolTrack patrolTrack = new PatrolTrack();
                if (AppConstant.CURRENT_LOCATION == null) {
                    return;
                }
                patrolTrack.setAltitude(AppConstant.CURRENT_LOCATION.altitude);
                if (currentUser.getUserId() == null) {
                    currentUser = UserDBHellper.getInstance().getUser(currentUser.getUserId());
                }
                patrolTrack.setSysUserId(currentUser.getUserId());
                if (AppConstant.CURRENT_INPLACE != null) {//到位登记
                    //                    patrolTrack.setPatrolType(AppConstant.CURRENT_INPLACE.getPatrolType());
                    patrolTrack.setSysTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
                    if (AppConstant.CURRENT_INPLACE.getPatrolType().equals(AppConstant.SPECIAL_INSPECTION)) {
                        patrolTrack.setNearTowerId(MyApplication.nearSecondTower.getSysTowerID().intValue());
                    }
                    patrolTrack.setPatrolTypeId(AppConstant.CURRENT_INPLACE.getPatrolType());
                    if (MyApplication.gridlineTaskStatus == 2) {
                        patrolTrack.setSysDailyPlanSectionID(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
                    } else if (MyApplication.gridlineTaskStatus == 3) {
                        patrolTrack.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
                    }

                } else {
                    patrolTrack.setPatrolTypeId("");
                    if (MyApplication.gridlineTaskStatus == 2) {
                        patrolTrack.setSysDailyPlanSectionID(MyApplication.mDayPlanLineSb.toString());
                        patrolTrack.setDailyPlanTimeSpanIDs(MyApplication.mOpenDayPlanLineSb.toString());
                    } else if (MyApplication.gridlineTaskStatus == 3) {
                        patrolTrack.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
                    }

                }
                LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
                double distance = getDistance(lastLat, lastLng, gps.latitude, gps.longitude);

                //                mLogUtils.e("lastLat" + gps.latitude,2);
                //
                mLogUtils.e("dis  = " + distance + "\n", 2);
                if (!isFrist) {
                    if (distance < 3) {
                        //                        mLogUtils.e("distance======"+distance+"\n",2);
                        //                        mLogUtils.e("The displacement distance is less than 3 meters.  Don't upload. \n",2);
                        return;
                    }
                }

                isUpdate = false;
                lastLat = gps.latitude;
                lastLng = gps.longitude;

                patrolTrack.setLatitude(gps.latitude);
                patrolTrack.setLongitude(gps.longitude);
                patrolTrack.setCreateDate(DateUtil.format(new Date()));
                saveLocalPatrol(patrolTrack);
                try {
                    uploadPatrolTrack();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //
            }
        }, 1, duringTime, TimeUnit.SECONDS);
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        return LatLngUtils.getDistance(lat1, lon1, lat2, lon2);
    }

    private void saveLocalPatrol(PatrolTrack patrolTrack) {
        try {
            PatrolTrackDBHleper.getInstance().insertPatrolTrack(patrolTrack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadPatrolTrack() {
        if (AppConstant.NET_WORK_AVAILABLE && !isUpdate) {
            isFrist = false;
            isUpdate = true;
            final List<PatrolTrack> patrolTracks = PatrolTrackDBHleper.getInstance().getLastestPatrolTack();
            if (patrolTracks.size() == 0) {
                return;
            }
            String jsonStr = mGosn.toJson(patrolTracks);
            //mLogUtils.e("" + jsonStr,2);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
            RetrofitManager.getInstance().getService(ApiService.class).postPatrolTrack(requestBody).enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
                @Override
                public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                    isUpdate = false;
                    if (response != null && response.body() != null && response.body().getCode() == 1) {
                        for (PatrolTrack patrolTrack : patrolTracks) {
                            patrolTrack.setUploadFlag(1);
                            //                            mLogUtils.e("Uploaded successfully. \n",2);
                        }
                        PatrolTrackDBHleper.getInstance().updateList(patrolTracks);
                    }
                }

                @Override
                public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                    isUpdate = false;
                    //                    mLogUtils.e("Upload failed. \n",2);
                    uploadPatrolTrack();
                }
            });
        } else {
            //            mLogUtils.e("isUpdate = \n" + (!isUpdate),2);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("nate", "onDestroy: ");
        releaseLock();
        webSocket_client.close();
        Notificationutil.quitNotify(this);
        mUploadScheduledFuture.cancel(true);
        mScheduledExecutorService.shutdown();
        mUpdateLocationFuture.cancel(true);
        EventBus.getDefault().unregister(this);
        if (mAmapNavi != null) {
            mAmapNavi.destroy();
        }
    }

    public void caculateNearestDistance(Map<String, List<Tower>> lineTowerMap, Map<Integer, List<Tower>> mTempTowerMap) {
        List<Tower> towerList = new ArrayList<>();
        for (Map.Entry entry : lineTowerMap.entrySet()) {
            towerList.addAll((Collection<? extends Tower>) entry.getValue());
        }
        for (Map.Entry entry : mTempTowerMap.entrySet()) {
            towerList.addAll((Collection<? extends Tower>) entry.getValue());
        }
        if (towerList.size() == 0) {
            MyApplication.registeredTower = null;
            MyApplication.currentNearestTower = null;
            MyApplication.nearestDistance = 0;
            MyApplication.nearSecondDistance = 0;
            MyApplication.nearSecondTower = null;
            return;
        }
        double minDistance = Double.MAX_VALUE;
        double distance;
        Tower nereastTower = null;
        for (int i = 0; i < towerList.size(); i++) {
            if (MyApplication.registeredTower != null && MyApplication.registeredTower.getSysGridLineId() != towerList.get(i).getSysGridLineId()) {
                continue;
            }
            distance = getDistance(towerList.get(i));
            if (minDistance > distance) {
                minDistance = distance;
                nereastTower = towerList.get(i);//最近的杆塔
            }
        }
        if (nereastTower == null) {
            return;
        }
        //没有最近的塔 或者最近的杆塔变化
        if (MyApplication.currentNearestTower == null || !nereastTower.getSysTowerID().equals(MyApplication.currentNearestTower.getSysTowerID())) {//最近的杆塔变化
            MyApplication.isFirstShow = true;
        }
        if (minDistance < AppConstant.mDistance && MyApplication.nearestDistance > AppConstant.mDistance) {//从大于30米进入到 30米以内的区域
            //如果没有到位登记 或者到位登记的塔 不是最近的塔
            if ((MyApplication.registeredTower == null || MyApplication.registeredTower.getSysTowerID() != nereastTower.getSysTowerID())) {
                if (MyApplication.isFirstShow) {
                    MyApplication.isFirstShow = false;
                    mDistanceReference.get().onNearestTowerCallBack(nereastTower, minDistance);
                }
            }
        }
        if (MyApplication.currentNearestTower == null || !MyApplication.currentNearestTower.getSysTowerID().equals(nereastTower.getSysTowerID())) {
            MyApplication.currentNearestTower = nereastTower;
        }
        MyApplication.nearestDistance = minDistance;
        EventBus.getDefault().post(new UpdateTowerEvent(nereastTower));//更新最近的杆塔
    }

    //根据Gps坐标获取距离
    private double getDistance(Tower tower) {
        double latitude = tower.getLatitude();
        double longitude = tower.getLongitude();
        double currentLatitude = AppConstant.CURRENT_LOCATION.latitude;
        double currentLongitude = AppConstant.CURRENT_LOCATION.longitude;
        LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(currentLatitude, currentLongitude);
        return LatLngUtils.getDistance(latitude, longitude, gps.latitude, gps.longitude);
    }

    public class OperationBinder extends Binder {
        public OperationService getOprationService() {
            return OperationService.this;
        }
    }

    public void setDistanceCallBack(DistanceCallback callBack) {
        this.mDistanceReference = new WeakReference<>(callBack);
    }

    //
    //    public void stopAllTask() {
    //        RetrofitManager.getInstance().getService(ApiService.class).postStopTask("all")
    //                .enqueue(new Callback<BaseCallBack<String>>() {
    //                    @Override
    //                    public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
    //                        if (response == null || response.body() == null) {
    //                            return;
    //                        }
    //                    }
    //
    //                    @Override
    //                    public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
    //                        //                        ToastUtil.show(MyApplication.getContext(), t.getMessage());
    //                    }
    //                });
    //    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseMainThreadEvent event) {
        if (event instanceof NaviPathEvent) {
            calculateRoutePath(((NaviPathEvent) event).getLatitude(), ((NaviPathEvent) event).getLongitude());
        } else if (event instanceof StartNaviEvent) {
         /*   mAmapNavi.startNavi(NaviType.EMULATOR);
            mAmapNavi.setEmulatorNaviSpeed(50);*/
            mAmapNavi.startNavi(NaviType.GPS);
        } else if (event instanceof StopAllTaskEvent) {
            //            stopAllTask();
            MyApplication.nearestDistance = 0;
            MyApplication.currentNearestTower = null;
            MyApplication.mAllTowersInMap.clear();
            MyApplication.registeredTower = null;
        }
    }

    private void calculateRoutePath(double lat, double longitude) {
        if (mAmapNavi == null) {
            mAmapNavi = AMapNavi.getInstance(MyApplication.getContext());
            mAmapNavi.addAMapNaviListener(new NavipathListener() {
                @Override
                public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
                    EventBus.getDefault().post(new NaviLocationEvent(aMapNaviLocation));
                }

                @Override
                public void onCalculateRouteFailure(int i) {
                    Log.i("CalculateRouteFailure", "err=" + i);
                }

                @Override
                public void onNaviInfoUpdate(NaviInfo naviInfo) {
                    EventBus.getDefault().post(new UpdateNaviInfoEvent(naviInfo));
                }

                @Override
                public void onCalculateRouteSuccess(int[] ints) {
                    mAmapNavi.selectRouteId(ints[0]);
                    EventBus.getDefault().post(new NaviRoutesEvent(ints, mAmapNavi.getNaviPaths(), mAmapNavi));
                }
            });
        }
        List<NaviLatLng> sList = new ArrayList<>();
        List<NaviLatLng> eList = new ArrayList<>();
        NaviLatLng start = new NaviLatLng(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
        sList.add(start);
        NaviLatLng endPoint = new NaviLatLng(lat, longitude);
        eList.add(endPoint);
        int strategyFlag = mAmapNavi.strategyConvert(true, true, true, false, true);
        mAmapNavi.calculateDriveRoute(sList, eList, null, strategyFlag);
    }

    private double getDistance(Tower from, Tower to) {
        double latitude = from.getLatitude();
        double longitude = from.getLongitude();

        double toLat = to.getLatitude();
        double toLng = to.getLongitude();
        return LatLngUtils.getDistance(latitude, longitude, toLat, toLng);
    }

    private void startLocationUpdate(int duringTime) {
        mUpdateLocationFuture = mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (GaodeLocationService.CurrentLocation != null) {
                    AppConstant.CURRENT_LOCATION = GaodeLocationService.CurrentLocation;
                }
                if (AppConstant.CURRENT_LOCATION != null) {
                    EventBus.getDefault().post(new UpdateLocationEvent());
                    caculateNearestDistance(MyApplication.mAllTowersInMap, MyApplication.mTempTowerMap);
                    if (MyApplication.isRegisterAuto == false) {
                        getSecondNearTower(MyApplication.registeredTower);//获取第二近的杆塔
                    } else {
                        getSecondNearTower(MyApplication.registeredTower);//获取第二近的杆塔
                    }
                }
            }
        }, 1, duringTime, TimeUnit.SECONDS);
    }



/*//根据角度来计算大号测 还是小号测
    double Dot(Tower towerOrigin, Tower next,LatLngInfo location){
        return (location.longitude-towerOrigin.getLongitude())*
                (next.getLongitude()-towerOrigin.getLongitude())+
                (location.latitude-towerOrigin.getLatitude()*(next.getLatitude()-towerOrigin.getLatitude()));}

    private double dot(double x1,double y1,double x2,double y2){
        return x1*x2+y1*y2;
    }

double Angle(Tower towerOrigin, Tower next,LatLngInfo location){
    double res  = Dot(towerOrigin,next,location)/Math.sqrt(dot(towerOrigin.getLongitude(),towerOrigin.getLatitude(),next.getLongitude(),next.getLatitude()))
            /Math.sqrt(dot(towerOrigin.getLongitude(),towerOrigin.getLatitude(),location.longitude,location.latitude));
    return Math.acos(res);
}*/


    //获取同一条线路中第二近的塔
    public void getSecondNearTower(final Tower tower_regis) {
        if (tower_regis == null) {
            return;
        }
        double currentDis = getDistance(tower_regis); //当前位置到到登记塔距离
//        double minDistance = Double.MAX_VALUE;      //最小距离
        Tower secondNearTower = null;               //第二近的塔
//        int order = tower_regis.getDisplayOrder();  //到位登记塔的oreder
        if (MyApplication.mAllTowersInMap.containsKey(tower_regis.getSysGridLineId() + "") ||
                MyApplication.mTempLineMap.containsKey(tower_regis.getSysGridLineId())) {
            List<Tower> towers = MyApplication.mAllTowersInMap.get(tower_regis.getSysGridLineId() + "");
            if (towers == null || towers.size() <= 0) {
                towers = MyApplication.mTempTowerMap.get(tower_regis.getSysGridLineId());
            }
//            boolean isNearSmall = false;//是否在大号侧  // 这个命名 有问题
            //TODO:+size
            if (towers.size() < 2) {
                MyApplication.nearSecondDistance = 0;
                MyApplication.nearSecondTower = tower_regis;
                MyApplication.crossSecondTower = tower_regis;
            }

            Collections.sort(towers, new Comparator<Tower>() {
                @Override
                public int compare(Tower lhs, Tower rhs) {
                    double distance1 = getDistance(tower_regis, lhs);
                    double distance2 = getDistance(tower_regis, rhs);
                    if (distance1 > distance2) {
                        return 1;
                    } else if (distance1 < distance2) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });

            //            for (int i = 0; i < towers.size(); i++) {
            //                Tower tower = towers.get(i);
            //                if (MyApplication.currentNearestTower.getSysTowerID().equals(tower.getSysTowerID())) {
            //                    continue;
            //                }
            //                int displayOrder = tower.getDisplayOrder();
            //                if (order == towers.get(0).getDisplayOrder() && displayOrder == towers.get(1).getDisplayOrder()) {//第二个塔
            //                    double towerDistance = getDistance(tower_regis, tower);
            //                    double distance = getDistance(tower);
            //
            //                    if (distance > towerDistance) {
            //                        minDistance = currentDis;
            //                        secondNearTower = tower_regis;
            //                    } else {
            //                        secondNearTower = tower;
            //                        minDistance = currentDis;
            //                    }
            //                    break;
            //                }
            //                if (towers.get(i + 1).getDisplayOrder() == order) {
            //                    double towerDistance = getDistance(tower_regis, tower);
            //                    double distance = getDistance(tower);
            //                    //两杆塔的距离 大于 人到杆塔的距离 表示在 小号侧 如果杆塔距离小于人到杆塔的距离 说明在大号侧
            //                    if (towerDistance < distance) {//大号侧
            //                        isNearSmall = true;
            //                    } else {
            //                        secondNearTower = tower;
            //                        minDistance = distance;
            //                        break;
            //                    }
            //                }
            //                if (isNearSmall) {//在大号侧
            //                    if (i == 0) {
            //                        continue;
            //                    }
            //                    if (towers.get(i - 1).getDisplayOrder() == order || i == towers.size() - 1) {//最后一级塔
            //                        secondNearTower = tower;
            //                        minDistance = currentDis;//小号侧距离就是 最近杆塔的距离
            //                        break;
            //                    }
            //                }
            //            }
            if (towers.size() > 2) {
                MyApplication.nearSecondDistance =  getDistance(tower_regis, towers.get(1));
                MyApplication.nearSecondTower = towers.get(1);
                MyApplication.crossSecondTower = towers.get(1);
                if (currentDis <= 20) {
                    MyApplication.nearSecondTower = null;
                }
            }


        }
    }

    /**
     * 在创建Service以后调用方法
     *
     * @param context
     */
    synchronized private void getLock(Context context) {
        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, OperationService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((System.currentTimeMillis()));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour <= 6) {
                mWakeLock.acquire(300000);
            } else {
                mWakeLock.acquire();
            }
        }
    }

    /**
     * 在onDestroy()方法中调用释放锁的方法（避免占用内存）
     */
    synchronized private void releaseLock() {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld()) {
                mWakeLock.release();
            }
            mWakeLock = null;
        }
    }


    public class PatrolTrackAdapter extends TypeAdapter<PatrolTrack> {

        @Override
        public void write(JsonWriter out, PatrolTrack value) throws IOException {
            out.beginObject();
            out.name("sysUserId").value(value.getSysUserId());
            out.name("Longitude").value(value.getLongitude());
            out.name("Latitude").value(value.getLatitude());
            out.name("Altitude").value(value.getAltitude());
            out.name("CreateDate").value(value.getCreateDate());
            out.name("PatrolTypeId").value(value.getPatrolTypeId());
            out.name("sysTowerId").value(value.getSysTowerId());
            out.name("NearTowerId").value(value.getNearTowerId());
            out.name("PatrolType").value(value.getPatrolType());
            out.name("sysDailyPlanSectionID").beginArray();
            String[] gridPlanId = value.getSysDailyPlanSectionID().split(",");
            for (String s : gridPlanId) {
                out.beginObject();
                out.value(s);
                out.endObject();
            }
            out.endArray();
            out.endObject();
        }

        @Override
        public PatrolTrack read(JsonReader in) throws IOException {
            PatrolTrack patrolTrack = new PatrolTrack();
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "sysUserId":
                        patrolTrack.setSysUserId(in.nextString());
                        break;
                    case "Longitude":
                        patrolTrack.setLongitude(in.nextDouble());
                        break;
                    case "Latitude":
                        patrolTrack.setLatitude(in.nextDouble());
                        break;
                    case "Altitude":
                        patrolTrack.setAltitude(in.nextDouble());
                        break;
                    case "CreateDate":
                        patrolTrack.setCreateDate(in.nextString());
                        break;
                    case "PatrolTypeId":
                        patrolTrack.setPatrolTypeId(in.nextString());
                        break;
                    case "sysTowerId":
                        patrolTrack.setSysTowerId(in.nextInt());
                        break;
                    case "NearTowerId":
                        patrolTrack.setNearTowerId(in.nextInt());
                        break;
                    case "PatrolType":
                        patrolTrack.setPatrolType(in.nextString());
                        break;
                    case "sysDailyPlanSectionID":
                        in.beginArray();
                        String[] s = in.nextString().split(",");
                        StringBuffer sb = new StringBuffer();
                        for (String str : s) {
                            sb.append(str).append(",");
                        }
                        if (sb.length() > 1) {
                            sb.deleteCharAt(sb.length() - 1);
                        }
                        patrolTrack.setSysDailyPlanSectionID(sb.toString());
                        break;
                }
            }

            return null;
        }
    }
}
