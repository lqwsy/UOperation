package com.uflycn.uoperation.ui.fragment.mytour.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.BaseFragment;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectCount;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.InPlace;
import com.uflycn.uoperation.bean.InitConfig;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TowerDefects;
import com.uflycn.uoperation.bean.TowerList;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.bean.VirtualTower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ClearCarRouteEvent;
import com.uflycn.uoperation.eventbus.CloseDialogEvent;
import com.uflycn.uoperation.eventbus.LoadLineFromPlanPatrolEvent;
import com.uflycn.uoperation.eventbus.NaviLocationEvent;
import com.uflycn.uoperation.eventbus.NaviPathEvent;
import com.uflycn.uoperation.eventbus.NaviRoutesEvent;
import com.uflycn.uoperation.eventbus.RegisterLocationEvent;
import com.uflycn.uoperation.eventbus.SelectNaviPathEvent;
import com.uflycn.uoperation.eventbus.StartNaviEvent;
import com.uflycn.uoperation.eventbus.StopAllTaskEvent;
import com.uflycn.uoperation.eventbus.StopLineFromDayPlanEvent;
import com.uflycn.uoperation.eventbus.StopLineFromPlanPatrilExecutionEvent;
import com.uflycn.uoperation.eventbus.StopNaviEvent;
import com.uflycn.uoperation.eventbus.UpdateLineCrossList;
import com.uflycn.uoperation.eventbus.UpdateLocationEvent;
import com.uflycn.uoperation.eventbus.UpdateNaviInfoEvent;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.InPlaceDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.greendao.VirtualTowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.listeners.DistanceCallback;
import com.uflycn.uoperation.listeners.MapOnsatusChangeLis;
import com.uflycn.uoperation.ui.activity.DefectManageActivity;
import com.uflycn.uoperation.ui.activity.DetectionManageActivity;
import com.uflycn.uoperation.ui.activity.HiddenDangerManagerActivity;
import com.uflycn.uoperation.ui.activity.TourResultActivity;
import com.uflycn.uoperation.ui.activity.TowerDetailActivity;
import com.uflycn.uoperation.ui.activity.workManage.view.WorkManageActivity;
import com.uflycn.uoperation.ui.fragment.mytour.contract.MyTourContract;
import com.uflycn.uoperation.ui.fragment.mytour.presenter.MyTourPresenter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.NonBlockSyntherizer;
import com.uflycn.uoperation.util.PictureSymbolUtils;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.SysUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.uflycn.uoperation.widget.AnimDialog;
import com.uflycn.uoperation.widget.CarRouteDetail;
import com.uflycn.uoperation.widget.CarRoutePanel;
import com.uflycn.uoperation.widget.DialogUpdateTower;
import com.uflycn.uoperation.widget.ImageMarkerSymbol;
import com.uflycn.uoperation.widget.NaviInfoView;
import com.uflycn.uoperation.widget.PanelDragView;
import com.uflycn.uoperation.widget.RegisterLocationDialog;
import com.xflyer.entity.ArcgisMapType;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.services.GaodeLocationService;
import com.xflyer.utils.CoordinateUtils;
import com.xflyer.utils.GaodeMapLayer;
import com.xflyer.utils.GoogleMapLayer;
import com.xflyer.utils.LatLngUtils;
import com.xflyer.utils.ThreadPoolUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 巡视页面
 * Created by xiaoyehai on 2017/9/4.
 */
public class MyTourFragment extends BaseFragment<MyTourPresenter> implements MyTourContract.View, DistanceCallback,
        OnSingleTapListener, View.OnClickListener, TextView.OnEditorActionListener,
        SensorEventListener, OnLongPressListener {

    private static final long TIME_SENSOR = 100;
    public static final int SHOW_TOWERS = 1;
    @BindView(R.id.frag_map)
    MapView mMapView;

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingLayout;

    @BindView(R.id.et_search_line_name)
    EditText mSearchLineEdit;
    @BindView(R.id.img_search_clear)
    ImageView mClearSearchImg;
    @BindView(R.id.btn_register_loaction)
    Button mRegisterBtn;
    @BindView(R.id.dragView)
    PanelDragView dragView;
    @BindView(R.id.rllayout_common)
    RelativeLayout mCommonLayout;
    @BindView(R.id.autonavi_port_title)
    NaviInfoView mNaviInfoview;

    @BindView(R.id.img_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.img_area_tower)
    ImageView mImgAreaTower;
    Unbinder unbinder;
    @BindView(R.id.tv_tour_result)
    TextView mTvTourResult;
    @BindView(R.id.tv_defect_list)
    TextView mTvDefectList;
    @BindView(R.id.tv_dis_defect)
    TextView mTvDisDefect;
    @BindView(R.id.tv_outside_break)
    TextView mTvOutsideBreak;

    private boolean isNaving = false;

    private String naviTowerName;
    private TiledServiceLayer mGoogleImageMapLayer = null;
    private TiledServiceLayer mGoogleRoadMapLayer = null;
    private TiledServiceLayer mGoogleVectorMapLayer = null;

    private GraphicsLayer mAreaVirtualLayer;//用来加附近虚拟塔
    private GraphicsLayer mVirtualPointLayer;//用来加虚拟塔
    private GraphicsLayer mPointLayer;//用来加点
    private GraphicsLayer mAreaPointLayer;//用来加点
    private GraphicsLayer mGpsLayer;//用来加载GPS定位点
    private GraphicsLayer mNaviLayer;//加载导航线路
    private GraphicsLayer mLineCrossLayer;//加载交跨 点
    private Map<Integer, Integer> mPointMap;//用来存放塔的id 后面graphic id 用来保存何移除 塔
    private Map<Integer, Integer> mAreaPointMap;//用来存放塔的id 后面graphic id 用来保存何移除 塔
    private Map<Integer, Integer> mLineMap;//用来存放线路 id  对应的Graphic id 用来移除 线
    private Map<Integer, List<Integer>> mLineTowerMap;//用来存放线路id 以及对应的塔的 id；用来移除一条线的塔
    private Map<Integer, List<Integer>> mLineDefectMap;//用来存放线路id 和对应线路的 缺陷列表；
    private Map<Integer, List<Integer>> mVirtualTowerGraphics = new HashMap<>();//用来存放线路内的虚拟塔GraphicId
    private List<Integer> mTowerDefectList;//用来存放线路id 和对应线路的 缺陷列表；
    private List<Tower> mAreaTowers = new ArrayList<>();//用来存放当前周边塔
    private Map<Integer, List<Integer>> mLineTextMap;
    private Map<String, List<Integer>> mLineCrossMap;//线路名称 和 对应单个 GraPhic
    private Map<String, Integer> mGpsLocation;
    private GraphicsLayer mLineLayer;//用来加载线
    private Map<Integer, Integer> mLineIndexs = new HashMap<>();
    private ImageMarkerSymbol mTowerSymbol;
    private ImageMarkerSymbol mNearestTowerSymbol;
    private ImageMarkerSymbol mCrossPointSymbol;
    private Symbol mLineSymbol;
    private Symbol mVirtualLineSymbol;
    private ImageMarkerSymbol mGpsPointSymbol;
    private String currentInpectionType;

    private Call<BaseCallBack<String>> mSubmitCallback;
    private Call<DefectCount> mCall;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private long lastTime;
    private float mAngle;

    private boolean isShow;

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.MIX;

    private static String TEMP_DIR = "/sdcard/baiduTTS"; //重要！请手动将assets目录下的3个dat 文件复制到该目录

    private static String TEXT_FILENAME = TEMP_DIR + "/" + "bd_etts_text.dat"; // 请确保该PATH下有这个文件

    private static String MODEL_FILENAME = TEMP_DIR + "/" + "bd_etts_speech_male.dat"; // 请确保该PATH下有这个文件 male是男声 female女声

    protected static NonBlockSyntherizer nonBlockSyntherizer;

    /**
     * 离线发音，离线男声或者女声
     */
    private String offlineVoice = "F";

    private int mLastChangeGraphicId;
    private ImageMarkerSymbol mVirtualTowerSymbol;
    private boolean isViewActive;

    //    private int mTestPolygoneId = -1;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_mytour;
    }

    @Override
    public MyTourPresenter getPresenter() {
        return new MyTourPresenter();
    }

    @Override
    protected void initView() {
        initMapView();
        initSearch();
        initSensorManager();
        initTTs();
        if (ProjectManageUtil.isShanDong()) {
            mTvTourResult.setText("缺陷管理");
            mTvDefectList.setText("检测管理");
            mTvDisDefect.setText("作业管理");
            mTvOutsideBreak.setText("隐患管理");
        }
    }

    private void initTTs() {
        if (nonBlockSyntherizer != null) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9"); // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT); // 设置合成的音量，0-9 ，默认 5
        InitConfig initConfig = new InitConfig(AppConstant.APPID, AppConstant.APPKEY, AppConstant.SECRETKEY, ttsMode, offlineVoice, params, new SpeechSynthesizerListener() {
            @Override
            public void onSynthesizeStart(String s) {

            }

            @Override
            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

            }

            @Override
            public void onSynthesizeFinish(String s) {

            }

            @Override
            public void onSpeechStart(String s) {

            }

            @Override
            public void onSpeechProgressChanged(String s, int i) {

            }

            @Override
            public void onSpeechFinish(String s) {

            }

            @Override
            public void onError(String s, SpeechError speechError) {

            }
        });
        nonBlockSyntherizer = new NonBlockSyntherizer(mRef.get(), initConfig, MyApplication.getHandler());

    }

    private void initSensorManager() {
        // 获取传感器管理对象
        mSensorManager = (SensorManager) mRef.get().getSystemService(Context.SENSOR_SERVICE);
        // 获取传感器的类型(TYPE_ACCELEROMETER:加速度传感器)
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void initData() {
        super.initData();
        mPointMap = new HashMap<>();
        mAreaPointMap = new HashMap<>();
        mLineMap = new HashMap<>();
        mLineTowerMap = new HashMap<>();
        mLineTextMap = new HashMap<>();
        mGpsLocation = new HashMap<>();
        mLineDefectMap = new HashMap<>();
        mLineCrossMap = new HashMap<>();
        mTowerDefectList = new ArrayList<>();
        mTowerSymbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.p_icon));
        mNearestTowerSymbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.r_icon));
        mVirtualTowerSymbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.virtual_tower));
        if (AppConstant.ScreenHeight == 1280) {
            mTowerSymbol.setHeight(getResources().getDimension(R.dimen.tower_size));
            mTowerSymbol.setWidth(getResources().getDimension(R.dimen.tower_size));
            mNearestTowerSymbol.setHeight(getResources().getDimension(R.dimen.tower_size));
            mNearestTowerSymbol.setWidth(getResources().getDimension(R.dimen.tower_size));
            mVirtualTowerSymbol.setHeight(getResources().getDimension(R.dimen.tower_size));
            mVirtualTowerSymbol.setWidth(getResources().getDimension(R.dimen.tower_size));
        } else if (AppConstant.ScreenHeight == 1920) {
            mTowerSymbol.setHeight(getResources().getDimension(R.dimen.tower_size_1920));
            mTowerSymbol.setWidth(getResources().getDimension(R.dimen.tower_size_1920));
            mNearestTowerSymbol.setHeight(getResources().getDimension(R.dimen.tower_size_1920));
            mNearestTowerSymbol.setWidth(getResources().getDimension(R.dimen.tower_size_1920));
            mVirtualTowerSymbol.setHeight(getResources().getDimension(R.dimen.tower_size_1920));
            mVirtualTowerSymbol.setWidth(getResources().getDimension(R.dimen.tower_size_1920));
        }
        mTowerSymbol.setOffsetY(mTowerSymbol.getHeight() / 2 - 5);
        mNearestTowerSymbol.setOffsetY(mNearestTowerSymbol.getHeight() / 2 - 5);
        mVirtualTowerSymbol.setOffsetY(mVirtualTowerSymbol.getHeight() / 2 - 5);


        mLineSymbol = new SimpleLineSymbol(Color.parseColor("#ffa800"), 3.0f);
        mLineSymbol = new SimpleLineSymbol(Color.parseColor("#ffa800"), 1.0f);

        mVirtualLineSymbol = new SimpleLineSymbol(Color.parseColor("#ffa800"), 2.0f, SimpleLineSymbol.STYLE.DOT);

        mGpsPointSymbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.navi_map_gps_locked));
        //        mGpsPointSymbol.setWidth(UIUtils.dp2px(30));
        //        mGpsPointSymbol.setHeight(UIUtils.dp2px(47));

        mCrossPointSymbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.line_cross));
        //        mCrossPointSymbol.setHeight(UIUtils.dp2px(32));
        //        mCrossPointSymbol.setWidth(UIUtils.dp2px(32));
        if (AppConstant.ScreenHeight == 1920) {
            mCrossPointSymbol.setHeight(getResources().getDimension(R.dimen.cross_width_1920));
            mCrossPointSymbol.setWidth(getResources().getDimension(R.dimen.cross_height_1920));
            mGpsPointSymbol.setHeight(getResources().getDimension(R.dimen.tower_size_1920));
            mGpsPointSymbol.setWidth(getResources().getDimension(R.dimen.tower_size_1920));
        } else if (AppConstant.ScreenHeight != 1280) {
            mCrossPointSymbol.setOffsetY(mCrossPointSymbol.getHeight() / 2 - 3);
            mCrossPointSymbol.setOffsetX(-mCrossPointSymbol.getWidth() / 2);
        } else {
            mCrossPointSymbol.setHeight(getResources().getDimension(R.dimen.cross_width));
            mCrossPointSymbol.setWidth(getResources().getDimension(R.dimen.cross_height));
            mGpsPointSymbol.setHeight(getResources().getDimension(R.dimen.icon_size));
            mGpsPointSymbol.setWidth(getResources().getDimension(R.dimen.icon_size));
        }

    }


    private void initMapView() {//初始化MapView
        ArcGISRuntime.setClientId("o4AGICRmrKDsNHRY");
        String mapType = ShareUtil.getString(mRef.get(), AppConstant.MAP_TYPE_KEY, mRef.get().getResources().getString(R.string.map_type_google));
        initMapType(mapType);
        mMapView.addLayer(mGoogleImageMapLayer, 0);
        mMapView.addLayer(mGoogleRoadMapLayer, 1);
        //mMapView.addLayer(mGoogleVectorMapLayer, 0);
        mMapView.setOnStatusChangedListener(new MapOnsatusChangeLis(mMapView));

        mVirtualPointLayer = new GraphicsLayer();
        mMapView.addLayer(mVirtualPointLayer);
        mAreaVirtualLayer = new GraphicsLayer();
        mMapView.addLayer(mAreaVirtualLayer);

        mLineLayer = new GraphicsLayer();
        mMapView.addLayer(mLineLayer);

        mPointLayer = new GraphicsLayer();
        mMapView.addLayer(mPointLayer);

        mAreaPointLayer = new GraphicsLayer();
        mMapView.addLayer(mAreaPointLayer);

        mLineCrossLayer = new GraphicsLayer();
        mMapView.addLayer(mLineCrossLayer);

        mGpsLayer = new GraphicsLayer();
        mMapView.addLayer(mGpsLayer);

        mNaviLayer = new GraphicsLayer();
        mMapView.addLayer(mNaviLayer);

        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mSlidingLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        initMapEvent();

    }

    private void initMapType(String mapType) {
        if (mapType.equalsIgnoreCase(mRef.get().getResources().getString(R.string.map_type_google))) {
            mGoogleImageMapLayer = new GoogleMapLayer(ArcgisMapType.ImageMap, IOUtils.getUflyRootPath(mRef.get()) + AppConstant.DIR_GOOGLE_CACHE);
            mGoogleRoadMapLayer = new GoogleMapLayer(ArcgisMapType.RoadMap, IOUtils.getUflyRootPath(mRef.get()) + AppConstant.DIR_GOOGLE_CACHE);
            mGoogleVectorMapLayer = new GoogleMapLayer(ArcgisMapType.VectorMap, IOUtils.getUflyRootPath(mRef.get()) + AppConstant.DIR_GOOGLE_CACHE);
        } else {
            mGoogleImageMapLayer = new GaodeMapLayer(ArcgisMapType.ImageMap, IOUtils.getUflyRootPath(mRef.get()) + AppConstant.DIR_GAODE_CACHE);
            mGoogleRoadMapLayer = new GaodeMapLayer(ArcgisMapType.RoadMap, IOUtils.getUflyRootPath(mRef.get()) + AppConstant.DIR_GAODE_CACHE);
            mGoogleVectorMapLayer = new GaodeMapLayer(ArcgisMapType.VectorMap, IOUtils.getUflyRootPath(mRef.get()) + AppConstant.DIR_GAODE_CACHE);
        }
    }

    @OnClick({R.id.ll_outside_break, R.id.ll_tour_result, R.id.ll_dis_defect, R.id.ll_defect_list,
            R.id.btn_register_loaction, R.id.iv_open_close_drawer, R.id.img_locate, R.id.img_search_clear,
            R.id.img_navigation, R.id.img_area_tower})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_outside_break://外破专档 | 隐患管理
                if (ProjectManageUtil.isShanDong()) {
                    startActivity(HiddenDangerManagerActivity.newInstance(mRef.get()));
                }else{
                    AppConstant.position = 2;
                    jumpToResultActivity(AppConstant.position);
                }
                break;
            case R.id.ll_tour_result://巡视登记 | 缺陷管理
                if (ProjectManageUtil.isShanDong()) {
                    startActivity(DefectManageActivity.newInstance(mRef.get()));
                } else {
                    AppConstant.position = 0;
                    jumpToResultActivity(AppConstant.position);
                }
                break;
            case R.id.ll_dis_defect://项目管理 | 作业管理
                if (ProjectManageUtil.isShanDong()) {
                    startActivity(WorkManageActivity.newInstance(mRef.get()));
                } else {
                    AppConstant.position = 1;
                    jumpToResultActivity(AppConstant.position);
                }
                break;
            case R.id.ll_defect_list://清障工单 | 检测管理
                if (ProjectManageUtil.isShanDong()) {
                    startActivity(DetectionManageActivity.newInstance(mRef.get()));
                } else {
                    AppConstant.position = 11;
                    jumpToResultActivity(AppConstant.position);
                }
                break;
            case R.id.btn_register_loaction://到位登记
                if (currentInpectionType == null || !
                        //                        currentInpectionType.equalsIgnoreCase(ItemDetailDBHelper.getInstance().getItemDetailByItemsName("特殊巡视").getItemDetailsId())) {//没有到位登记，或者到位登记的不是特殊巡视
                        currentInpectionType.equalsIgnoreCase(ItemDetailDBHelper.getInstance().getTourItemDetail("特殊巡视").getItemDetailsId())) {//没有到位登记，或者到位登记的不是特殊巡视
                    showRegisterLocationDialog();
                } else {
                    updateRegisterButton(1);
                }
                break;
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
            case R.id.img_locate://定位
                locate(true);
                break;
            case R.id.img_search_clear:
                mSearchLineEdit.setText("");
                break;
            case R.id.img_navigation://查找路线
                AmapNaviPage.getInstance().showRouteActivity(mRef.get(), new AmapNaviParams(null), new NavipathListener());
                break;
            case R.id.img_area_tower://显示塔
                showTowerInArea();
                break;
            //            case R.id.img_close_area_tower:
            //                closeTowerInArea();
            //                break;
            default:
                break;
        }
    }
    //
    //    private void closeTowerInArea() {
    //        mAreaPointLayer.removeAll();
    //        if (MyApplication.mAllTowersInMap.containsKey("-1")) {
    //            MyApplication.mAllTowersInMap.remove("-1");
    //        }
    //        if (MyApplication.gridlines.containsKey(-1)) {
    //            MyApplication.gridlines.remove(-1);
    //
    //        }
    //    }


    private void showTowerInArea() {
        if (MyApplication.gridlineTaskStatus == 2) {
            ToastUtil.show("请先关闭每日计划!");
            return;
        }
        if (MyApplication.gridlineTaskStatus == 3) {
            ToastUtil.show("请先关闭我的任务!");
            return;
        }
        if (MyApplication.mTempTowerMap.size() > 0) {//不显示
            MyApplication.gridlineTaskStatus = 0;

            MyApplication.registeredTower = null;
            MyApplication.mTempTowerMap.clear();
            MyApplication.mTempLineMap.clear();
            mAreaPointMap.clear();
            mAreaPointLayer.removeAll();
            mAreaVirtualLayer.removeAll();

            MyApplication.currentNearestTower = null;
            mImgAreaTower.setBackgroundResource(R.drawable.near);
        } else {//显示
            MyApplication.gridlineTaskStatus = 1;

            if (AppConstant.CURRENT_LOCATION == null) {
                return;
            }
            mImgAreaTower.setBackgroundResource(R.drawable.near_d);
            Observable.just(AppConstant.CURRENT_LOCATION)
                    .flatMap(new Func1<LatLngInfo, Observable<? extends List<Tower>>>() {
                        @Override
                        public Observable<? extends List<Tower>> call(LatLngInfo lngInfo) {
                            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(lngInfo.latitude, lngInfo.longitude);
                            List<Tower> towerList = TowerDBHelper.getInstance().getTowerInAreaList(gps.latitude, gps.longitude);
                            return Observable.just(towerList);
                        }
                    })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(Schedulers.newThread())
                    .subscribe(new Action1<List<Tower>>() {
                        @Override
                        public void call(List<Tower> towers) {
                            showAreaTowers(towers);
                        }
                    });
        }
    }

    public void startLine(int lineId) {
        if (MyApplication.mLineIdNamePairs.size() >= 3) {
            ToastUtil.show("线路最多开启3个");
            return;
        }
        Gridline gridline = GridLineDBHelper.getInstance().getLine(lineId);
        if (!MyApplication.mLineIdNamePairs.containsKey(lineId + "") && !MyApplication.gridlines.containsKey(lineId)) {
            MyApplication.mLineIdNamePairs.put(lineId + "", gridline.getLineName());
            MyApplication.gridlines.put(lineId, gridline);
            ((MainActivity) mRef.get()).mRbtnInspection.performClick();
            ((MainActivity) mRef.get()).addTowersById(lineId);
        }
    }


    private void showNearestTower() {
        if (AppConstant.CURRENT_LOCATION == null || MyApplication.currentNearestTower == null) {
            return;
        }
        Observable.just("")
                .observeOn(Schedulers.trampoline())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        LatLngInfo gps = CoordinateUtils.lonLatToMercator(AppConstant.CURRENT_LOCATION.longitude, AppConstant.CURRENT_LOCATION.latitude);
                        Point point = new Point(gps.longitude, gps.latitude);
                        Polygon polygon = GeometryEngine.buffer(point, SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR),
                                AppConstant.mDistance, new LinearUnit(LinearUnit.Code.METER));
                    /*    if (mTestPolygoneId != -1) {
                            mNaviLayer.updateGraphic(mTestPolygoneId, new Graphic(polygon, new SimpleFillSymbol(Color.BLUE, SimpleFillSymbol.STYLE.CROSS)));
                        } else {
                            mTestPolygoneId = mNaviLayer.addGraphic(new Graphic(polygon, new SimpleFillSymbol(Color.BLUE, SimpleFillSymbol.STYLE.CROSS)));
                        }*/
                        Point nearestPoint = getPointfromgps(MyApplication.currentNearestTower.getLatitude(), MyApplication.currentNearestTower.getLongitude());
                        boolean in = GeometryEngine.within(nearestPoint, polygon, SpatialReference.create(SpatialReference.WKID_WGS84_WEB_MERCATOR));
                        int graphicId;
                        if (MyApplication.gridlines.containsKey(MyApplication.currentNearestTower.getSysGridLineId())) {
                            graphicId = mPointMap.get(MyApplication.currentNearestTower.getSysTowerID().intValue());
                        } else {
                            graphicId = mAreaPointMap.get(MyApplication.currentNearestTower.getSysTowerID().intValue());
                        }
                        if (in) {
                            updateGraphic(mPointLayer, mAreaPointLayer, graphicId, mNearestTowerSymbol);
                            if (mLastChangeGraphicId != graphicId) {
                                updateGraphic(mPointLayer, mAreaPointLayer, mLastChangeGraphicId, mTowerSymbol);
                            }
                        } else {
                            updateGraphic(mPointLayer, mAreaPointLayer, graphicId, mTowerSymbol);
                        }
                        mLastChangeGraphicId = graphicId;
                    }
                });
    }

    private void updateGraphic(GraphicsLayer graphicsLayer, GraphicsLayer graphicsLayer1, int id, ImageMarkerSymbol symbol) {
        if (graphicsLayer.getGraphic(id) != null) {
            graphicsLayer.updateGraphic(id, symbol);
        } else {
            graphicsLayer1.updateGraphic(id, symbol);
        }
    }

/*
    private void registerLocation(Tower from,Tower to){
        InPlace inPlace = new InPlace();
        double lat = AppConstant.CURRENT_LOCATION.latitude;
        double lng = AppConstant.CURRENT_LOCATION.longitude;
        double altitude = AppConstant.CURRENT_LOCATION.altitude;
        LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(lat, lng);
        inPlace.setPatrolType( "6fa7e4b6-70bb-40e0-b768-d05fe8152ec8");//巡视种类
        if(AppConstant.currentUser != null){
            inPlace.setSysUserId(AppConstant.currentUser.getUserId());//用户id
        }
        inPlace.setSysTowerId(from.getSysTowerID().intValue());//开始杆塔
        if(to != null){//区段
            inPlace.setNearTowerId(to.getSysTowerID().intValue());//结束杆塔
        }
        inPlace.setLongitude(gps.longitude);//经度
        inPlace.setLatitude(gps.latitude);//维度
        inPlace.setAltitude(altitude);//高度
        inPlace.setCreateDate(DateUtil.format(new Date()));
        inPlace.setUploadFlag(0);
        EventBus.getDefault().post(new RegisterLocationEvent(inPlace));//发送请求
        InPlaceDBHelper.getInstance().insertInplace(inPlace);//数据库保存到位登记
    }
*/


    /**
     * 到位登记
     */
    private void showRegisterLocationDialog() {
        RegisterLocationDialog registerLocationDialog = new RegisterLocationDialog(mRef.get());
        registerLocationDialog.show();
    }

    /**
     * 外破专档
     *
     * @param position
     */
    private void jumpToResultActivity(int position) {
        Intent intent = new Intent(new Intent(mRef.get(), TourResultActivity.class));
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.POSITION, position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 增加一条线路
     */
    @Override
    public void addTowerByid(int lineid) {
        mPresenter.addTowerByid(lineid);
        mPresenter.getLineCrossList(lineid + "");
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startLineFromDayPlan(DayPlanInfo dayPlanInfo) {
        ((MainActivity) mRef.get()).mRbtnInspection.performClick();
        //        mPresenter.addTowersBewteenTowerById(dayPlanInfo.getStartTowerID(), dayPlanInfo.getEndTowerID(), dayPlanInfo.getSysGridLineID());
        //        mPresenter.getLineCrossList(dayPlanInfo.getSysGridLineID() + "");
        addTowerByid(dayPlanInfo.getSysGridLineID().intValue());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void startLineFromPlanPatrolExecution(LoadLineFromPlanPatrolEvent event) {
        if (event.getList() == null || event.getList().size() == 0) {
            ToastUtil.show("当前线路异常");
            return;
        }
        ((MainActivity) mRef.get()).mRbtnInspection.performClick();
        TowerList tower;
        for (int i = 0; i < event.getList().size(); i++) {
            List<Integer> mTowerIds = new ArrayList<>();
            for (int j = 0; j < event.getList().get(i).getTowerList().size(); j++) {
                tower = event.getList().get(i).getTowerList().get(j);
                mTowerIds.add(tower.getSysTowerID());
                MyApplication.mWorkTypeMap.put(tower.getSysTowerID(), tower.getTypeOfWork());
            }

            List<Tower> towers = TowerDBHelper.getInstance().getTowerInList(mTowerIds);
            showTowers(towers);
            Gridline gridline = GridLineDBHelper.getInstance().getLine(event.getList().get(i).getSysGridLineID());
            MyApplication.gridlines.put(gridline.getSysGridLineID().intValue(), gridline);
            MyApplication.mLineIdNamePairs.put(gridline.getSysGridLineID() + "", gridline.getLineName());

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeLineFromDayPlan(StopLineFromDayPlanEvent event) {
        removeGridLine(event.getLineId());
        if (mVirtualPointLayer != null) {
            mVirtualPointLayer.removeAll();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeLineFromPlanPatrolExecution(StopLineFromPlanPatrilExecutionEvent event) {
        if (StringUtils.isEmptyOrNull(event.getLineIds())) {
            ToastUtil.show("当前线路异常");
            return;
        }
        String[] lineIds = event.getLineIds().split(",");
        for (int i = 0; i < lineIds.length; i++) {
            //            MyApplication.mLineIdNamePairs.remove(lineIds[i]);
            removeGridLine(Integer.parseInt(lineIds[i]));
            MyApplication.gridlines.remove(Integer.parseInt(lineIds[i]));
        }
        if (mVirtualPointLayer != null) {
            mVirtualPointLayer.removeAll();
        }
    }


    @Override
    public void showTowers(final List<Tower> towers) {//增加一条线路
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                if (towers.size() == 0) {
                    return;
                }
                int lineId = towers.get(0).getSysGridLineId();
                List<Integer> towerGraphicIds = new ArrayList<>();
                List<Integer> textGraphicIds = new ArrayList<>();
                Tower tower;
                Polyline polyline = new Polyline();
                int id;
                int textid;
                MyApplication.mAllTowersInMap.put(lineId + "", towers);
                for (int i = 0; i < towers.size(); i++) {
                    tower = towers.get(i);
                    double latitude = tower.getLatitude();
                    double longitude = tower.getLongitude();
                    Point point = getPointfromgps(latitude, longitude);
                    id = addSingleTower(tower, point);
                    mPointMap.put(tower.getSysTowerID().intValue(), id);//杆塔id 对应 graphic id
                    towerGraphicIds.add(id);
                    textid = addSingleText(point, tower.getTowerNo());
                    textGraphicIds.add(textid);
                    // 加杆塔编号文字说明
                    if (i == 0) {
                        polyline.startPath(point);
                        mMapView.centerAt(point, true);
                    } else {
                        polyline.lineTo(point);
                    }
                }
                mLineTowerMap.put(lineId, towerGraphicIds);//线路id 对应的所有 塔的graphic id
                mLineTextMap.put(lineId, textGraphicIds);
                int polylineId = mLineLayer.addGraphic(new Graphic(polyline, mLineSymbol));
                mLineMap.put(lineId, polylineId);//线路 id  graphic id

                //显示修改经纬度后的虚拟塔
                Map<String, Object> attrs = new HashMap<>();
                attrs.put(AppConstant.IS_FROM_AREA_TOWER, false);
                attrs.put(AppConstant.LINE_ID, lineId);
                showVirtualTowers(attrs);
            }
        });
    }

    public void showAreaTowers(List<Tower> towers) {//增加一条线路
        mAreaPointLayer.removeAll();
        Tower tower;
        for (int i = 0; i < towers.size(); i++) {
            refreshTempMap(towers.get(i));
            tower = towers.get(i);
            double latitude = tower.getLatitude();
            double longitude = tower.getLongitude();
            Point point = getPointfromgps(latitude, longitude);
            int id = addAreaSingleTower(tower, point);
            addAreaSingleText(point, tower.getTowerNo());
            mAreaPointMap.put(tower.getSysTowerID().intValue(), id);
            // 加杆塔编号文字说明
            if (i == 0) {
                mMapView.centerAt(point, true);
            }
        }
        //显示修改经纬度后的虚拟塔
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(AppConstant.IS_FROM_AREA_TOWER, true);
        mAreaTowers.clear();
        mAreaTowers.addAll(towers);
        showVirtualTowers(attrs);
    }

    private void refreshTempMap(Tower tower) {
        if (!MyApplication.mTempLineMap.containsKey(tower.getSysGridLineId())) {
            Gridline gridline = GridLineDBHelper.getInstance().getLine(tower.getSysGridLineId());
            MyApplication.mTempLineMap.put(tower.getSysGridLineId(), gridline);
        }
        if (MyApplication.mTempTowerMap.containsKey(tower.getSysGridLineId())) {
            MyApplication.mTempTowerMap.get(tower.getSysGridLineId()).add(tower);
        } else {
            List<Tower> towers = new ArrayList<>();
            towers.add(tower);
            MyApplication.mTempTowerMap.put(tower.getSysGridLineId(), towers);
        }
    }

    @Override
    public void showDefectsByLineId(DefectInfo defectInfo) {
        showChannelDefects(defectInfo.getChannelDefect());
        showTreeDefects(defectInfo.getTreeDefectPoint());
        showPersonalDefects(defectInfo.getPersonalDefect());
        showPropertyDefect(defectInfo.getPropertyDefect());
    }

    @Override
    public void showDefectsByTowerId(TowerDefects towerDefects) {
      /*  showChannelDefects(towerDefects.getChannelDefect());
        showPersonalDefects(towerDefects.getPersonalDefect());
        showTreeDefects(towerDefects.getTreeDefectPoint());
        showPropertyDefect(towerDefects.getPropertyDefect());*/
    }

    private int addSingleTower(Tower tower, Point point) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(AppConstant.TOWER_ID_KEY, tower.getSysTowerID());
        attrs.put(AppConstant.TOWER_LAT_KEY, tower.getLatitude());
        attrs.put(AppConstant.TOWER_LNG_KEY, tower.getLongitude());
        attrs.put(AppConstant.TOWER_ALTITUDE_KEY, tower.getAltitude());
        attrs.put(AppConstant.TOWER_NUM_KEY, tower.getTowerNo());
        attrs.put(AppConstant.TOWER_ID_KEY, tower.getSysTowerID());
        attrs.put(AppConstant.LINE_ID, tower.getSysGridLineId());
        attrs.put(AppConstant.IS_FROM_AREA_TOWER, false);
        attrs.put(AppConstant.TOWER_LINENAME_KEY, MyApplication.mLineIdNamePairs.get(tower.getSysGridLineId() + ""));
        return mPointLayer.addGraphic(new Graphic(point, mTowerSymbol, attrs));
    }

    private int addAreaSingleTower(Tower tower, Point point) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(AppConstant.TOWER_ID_KEY, tower.getSysTowerID());
        attrs.put(AppConstant.TOWER_LAT_KEY, tower.getLatitude());
        attrs.put(AppConstant.TOWER_LNG_KEY, tower.getLongitude());
        attrs.put(AppConstant.TOWER_ALTITUDE_KEY, tower.getAltitude());
        attrs.put(AppConstant.TOWER_NUM_KEY, tower.getTowerNo());
        attrs.put(AppConstant.TOWER_ID_KEY, tower.getSysTowerID());
        attrs.put(AppConstant.LINE_ID, tower.getSysGridLineId());
        attrs.put(AppConstant.IS_FROM_AREA_TOWER, true);
        attrs.put(AppConstant.TOWER_LINENAME_KEY, MyApplication.mLineIdNamePairs.get(tower.getSysGridLineId() + ""));
        return mAreaPointLayer.addGraphic(new Graphic(point, mTowerSymbol, attrs));
    }


    //通道缺陷
    private void showChannelDefects(List<DefectBean> channelDefectBeens) {
        for (DefectBean defectBean : channelDefectBeens) {
            if (defectBean.getNearTowerNo() == null || defectBean.getNearTowerNo().equalsIgnoreCase("")) {
                Point point = getPointfromgps(defectBean.getLatitude(), defectBean.getLongitude());
                int id = mPointLayer.addGraphic(new Graphic(point, getImageMarkerByLevel(defectBean.getServerityLevelString())));
                mTowerDefectList.add(id);
            }
        }
    }

    //树障缺陷
    private void showTreeDefects(List<TreeDefectPointBean> treeDefectPointBeen) {
        for (TreeDefectPointBean defectBean : treeDefectPointBeen) {
            Point point = getPointfromgps(defectBean.getLatitude(), defectBean.getLongitude());
            int id = mPointLayer.addGraphic(new Graphic(point, getImageMarkerByLevel(defectBean.getTreeDefectPointType())));
            mTowerDefectList.add(id);
        }
    }

    //人巡缺陷
    private void showPersonalDefects(List<DefectBean> personalDefectBeens) {
        for (DefectBean defectBean : personalDefectBeens) {
            if (defectBean.getNearTowerNo() == null || defectBean.getNearTowerNo().equalsIgnoreCase("")) {
                Point point = getPointfromgps(defectBean.getLatitude(), defectBean.getLongitude());
                int id = mPointLayer.addGraphic(new Graphic(point, getImageMarkerByLevel(defectBean.getServerityLevelString())));
                mTowerDefectList.add(id);
            }
        }
    }

    private void showPropertyDefect(List<DefectBean> propertyDefects) {
        for (DefectBean defectBean : propertyDefects) {
            if (defectBean.getNearTowerNo() == null || defectBean.getNearTowerNo().equalsIgnoreCase("")) {
                Point point = getPointfromgps(defectBean.getLatitude(), defectBean.getLongitude());
                int id = mPointLayer.addGraphic(new Graphic(point, getImageMarkerByLevel(defectBean.getServerityLevelString())));
                mTowerDefectList.add(id);
            }
        }
    }

    /**
     * 移除一条线路
     */
    public void removeGridLine(int lineid) {
        try {
            int graphiclineId = mLineMap.get(lineid);
            if (MyApplication.registeredTower != null && MyApplication.registeredTower.getSysGridLineId() == lineid) {
                MyApplication.registeredTower = null;
                for (int i = 0; i < mTowerDefectList.size(); i++) {
                    mPointLayer.removeGraphic(mTowerDefectList.get(i));
                }
                mRegisterBtn.setText("到位登记");
                currentInpectionType = null;
            }
            MyApplication.currentNearestTower = null;
            mLineLayer.removeGraphic(graphiclineId);//移除线路

            MyApplication.mAllTowersInMap.remove(lineid + "");
            List<Integer> towersGraphics = mLineTowerMap.get(lineid);//杆塔
            List<Integer> textGraphics = mLineTextMap.get(lineid);//文字
            if (towersGraphics == null) {
                return;
            }
            for (int i : towersGraphics) {//移除该线路所有的塔
                mPointLayer.removeGraphic(i);
            }
            for (int i : textGraphics) {
                mPointLayer.removeGraphic(i);
            }
            removeLineCross(lineid + "", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将gps坐标转换成 墨卡托坐标
    private Point getPointfromgps(double lat, double lgt) {
        LatLngInfo info1 = CoordinateUtils.gps84_To_Gcj02(lat, lgt);
        LatLngInfo lgps = CoordinateUtils.lonLatToMercator(info1.longitude, info1.latitude);
        return new Point(lgps.longitude, lgps.latitude);
    }

    @Override
    public void onNearestTowerCallBack(Tower tower, double distance) {
        UIUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                //showRegisterLocationDialog();
            }
        });
    }

    @Override
    public void updateGpsLocate() {
        locate(false);
    }


    @Override
    public void onDestroy() {
        nonBlockSyntherizer.release();
        super.onDestroy();
        if (mSubmitCallback != null && !mSubmitCallback.isCanceled()) {
            mSubmitCallback.cancel();
        }
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        mSensorManager.unregisterListener(this);
       /* if (nonBlockSyntherizer != null) {
            nonBlockSyntherizer.release();
            nonBlockSyntherizer = null;
        }*/
    }

    public void removeAllLines() {
        removeAllLineCross();
        mPointLayer.removeAll();
        mLineLayer.removeAll();
        mVirtualPointLayer.removeAll();
        clearAllPoints();
        MyApplication.mLineIdNamePairs.clear();
        MyApplication.registeredTower = null;
        MyApplication.currentNearestTower = null;
        MyApplication.mAllTowersInMap.clear();//清空所有的塔

        updateRegisterButton(1);
    }

    private void locate(boolean bCenter) {
        if (GaodeLocationService.CurrentLocation == null) {
            if (AppConstant.CURRENT_LOCATION == null) {
                ToastUtil.show("定位失败，请稍后再试！");
            } else {
                updateGpsLocation(AppConstant.CURRENT_LOCATION, bCenter);
            }
        } else {
            updateGpsLocation(GaodeLocationService.CurrentLocation, bCenter);
        }
    }


    private void updateGpsLocation(LatLngInfo latLngInfo, boolean bCenter) {
        LatLngInfo lgps = CoordinateUtils.lonLatToMercator(latLngInfo.longitude, latLngInfo.latitude);
        Point mecator = new Point(lgps.longitude, lgps.latitude);
        int id;
        if (AppConstant.CURRENT_LOCATION != null) {
            mGpsPointSymbol.setAngle(AppConstant.CURRENT_LOCATION.direction);
        }
        if (mGpsLocation.get(AppConstant.GPS_LOCATION_ID) == null) {
            mGpsLayer.removeAll();
            id = mGpsLayer.addGraphic(new Graphic(mecator, mGpsPointSymbol));
            mGpsLocation.put(AppConstant.GPS_LOCATION_ID, id);
        } else {
            id = mGpsLocation.get(AppConstant.GPS_LOCATION_ID);
            mGpsLayer.updateGraphic(id, mecator);
        }
        if (bCenter) {
            mMapView.centerAt(mecator, true);
        }
    }

    private void initMapEvent() {
        mMapView.setOnSingleTapListener(this);
        mMapView.setOnLongPressListener(this);
    }

    @Override
    public void onSingleTap(float v, float v1) {
        Geometry geometry;
        int[] touchGraphicsIds = mPointLayer.getGraphicIDs(v, v1, 20);
        if (touchGraphicsIds != null && touchGraphicsIds.length > 0) {
            for (int i : touchGraphicsIds) {
                geometry = mPointLayer.getGraphic(i).getGeometry();
                if (geometry instanceof Point) {
                    Map<String, Object> attrs = mPointLayer.getGraphic(i).getAttributes();
                    if (attrs != null && attrs.size() >= 6) {
                        showTowerDetailDialog(attrs);
                        break;
                    }
                }
            }
            return;
        }
        int[] touchGraphicsIds1 = mAreaPointLayer.getGraphicIDs(v, v1, 20);
        if (touchGraphicsIds1 != null && touchGraphicsIds1.length > 0) {
            for (int i : touchGraphicsIds1) {
                geometry = mAreaPointLayer.getGraphic(i).getGeometry();
                if (geometry instanceof Point) {
                    Map<String, Object> attrs = mAreaPointLayer.getGraphic(i).getAttributes();
                    if (attrs != null && attrs.size() >= 6) {
                        showTowerDetailDialog(attrs);
                        break;
                    }
                }
            }
            return;
        }
        int[] graphicIDs = mVirtualPointLayer.getGraphicIDs(v, v1, 20);
        if (graphicIDs != null && graphicIDs.length > 0) {
            int graphicID = graphicIDs[0];
            geometry = mVirtualPointLayer.getGraphic(graphicID).getGeometry();
            if (geometry instanceof Point) {
                Map<String, Object> attrs = mVirtualPointLayer.getGraphic(graphicID).getAttributes();
                showVirtualTowerDialog(attrs);
            }
            return;
        }
        int[] areaGraphicIDs = mAreaVirtualLayer.getGraphicIDs(v, v1, 20);
        if (areaGraphicIDs != null && areaGraphicIDs.length > 0) {
            int graphicID = areaGraphicIDs[0];
            geometry = mAreaVirtualLayer.getGraphic(graphicID).getGeometry();
            if (geometry instanceof Point) {
                Map<String, Object> attrs = mAreaVirtualLayer.getGraphic(graphicID).getAttributes();
                showVirtualTowerDialog(attrs);
            }
            return;
        }
    }

    private void showVirtualTowerDialog(Map<String, Object> attrs) {
        if (attrs == null)
            return;
        String towerNo = (String) attrs.get(AppConstant.TOWER_NUM_KEY);
        new AlertDialog.Builder(getActivity()).setMessage(towerNo + "号杆塔变更\n\n待审核").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public boolean onLongPress(float v, float v1) {
        int[] touchGraphicsIds = mLineCrossLayer.getGraphicIDs(v, v1, 20);
        if (touchGraphicsIds == null || touchGraphicsIds.length == 0) {
            return false;
        }
        Geometry geometry;
        for (int i : touchGraphicsIds) {
            geometry = mLineCrossLayer.getGraphic(i).getGeometry();
            if (geometry instanceof Point) {
                Map<String, Object> attrs = mLineCrossLayer.getGraphic(i).getAttributes();
                if (attrs != null && attrs.size() == 1) {
                    showLineCrossDetailDialog(attrs);
                    break;
                }
            }
        }
        return true;
    }

    private ImageMarkerSymbol getImageMarkerByLevel(String level) {
        ImageMarkerSymbol symbol;
        if (level.equalsIgnoreCase("一般")) {
            symbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.defect_green));
        } else if (level.equalsIgnoreCase("严重")) {
            symbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.defect_yellow));
        } else {
            symbol = new ImageMarkerSymbol(mRef.get().getResources().getDrawable(R.drawable.defect_red));
        }
        symbol.setWidth(UIUtils.dp2px(17));
        symbol.setHeight(UIUtils.dp2px(17));
        symbol.setOffsetY(symbol.getHeight() - 3);
        return symbol;
    }

    private void removeAllDefectsByLineid(String lineid) {
        if (mLineDefectMap.containsKey(lineid)) {
            List<Integer> ids = mLineDefectMap.get(lineid);
            for (int id : ids) {
                mPointLayer.removeGraphic(id);
            }
        }

    }


    private int addSingleText(Point point, String name) {
        ImageMarkerSymbol textSymbol;
        if (AppConstant.ScreenHeight == 1280) {
            textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33")).setTextSize(SysUtils.dip2px(MyApplication.getContext(), 40)).createMapBitMap(name));
        } else if (AppConstant.ScreenHeight == 1920) {
            textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33")).setTextSize(SysUtils.dip2px(MyApplication.getContext(), 55)).createMapBitMap(name));
            textSymbol.setOffsetY(mTowerSymbol.getHeight() / 2 + textSymbol.getHeight() / 2 + 35);
        } else {
            textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33")).setTextSize(UIUtils.getDimen(R.dimen.tower_name_text_size)).createMapBitMap(name));
        }
        textSymbol.setOffsetY(mTowerSymbol.getHeight() / 2 + textSymbol.getHeight() / 2 + 20);
        textSymbol.setOffsetX(-3);
        Graphic textGraph = new Graphic(point, textSymbol);
        return mPointLayer.addGraphic(textGraph);
    }

    private void addAreaSingleText(Point point, String name) {
        ImageMarkerSymbol textSymbol;
        if (AppConstant.ScreenHeight == 1280) {
            textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33")).setTextSize(SysUtils.dip2px(getContext(), 40)).createMapBitMap(name));
        } else if (AppConstant.ScreenHeight == 1920) {
            textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33")).setTextSize(SysUtils.dip2px(getContext(), 55)).createMapBitMap(name));
            textSymbol.setOffsetY(mTowerSymbol.getHeight() / 2 + textSymbol.getHeight() / 2 + 35);
        } else {
            textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33")).setTextSize(UIUtils.getDimen(R.dimen.tower_name_text_size)).createMapBitMap(name));
        }
        textSymbol.setOffsetY(mTowerSymbol.getHeight() / 2 + textSymbol.getHeight() / 2 + 20);
        textSymbol.setOffsetX(-3);
        Graphic textGraph = new Graphic(point, textSymbol);
        mAreaPointLayer.addGraphic(textGraph);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
            isShow = false;
            mMapView.removeLayer(mGoogleImageMapLayer);
            mMapView.removeLayer(mGoogleRoadMapLayer);
        } else {// 重新显示到最前端中
            isShow = true;
            String mapType = ShareUtil.getString(mRef.get(), AppConstant.MAP_TYPE_KEY, mRef.get().getResources().getString(R.string.map_type_google));
            initMapType(mapType);
            mMapView.addLayer(mGoogleImageMapLayer, 0);
            mMapView.addLayer(mGoogleRoadMapLayer, 1);
        }
    }

    private void showLineCrossDetailDialog(final Map<String, Object> attrs) {
        try {
            final AnimDialog dialog = new AnimDialog(mRef.get());
            final int platformId = (int) attrs.get(AppConstant.LINE_CROSS_ID);
            final LineCrossEntity lineCrossEntity = LineCrossDBHelper.getInstance().getLineCross(platformId);
            if (lineCrossEntity == null) {
                return;//5308
            }
            dialog.showDialog(R.layout.update_tower_layout, 0, 250);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (mCall != null && !mCall.isCanceled()) {
                        mCall.cancel();
                    }
                }
            });
            dialog.findViewById(R.id.layout_tower).setVisibility(View.GONE);
            dialog.findViewById(R.id.layout_cross).setVisibility(View.VISIBLE);
            String tower = showTowerId(lineCrossEntity.getStartTower(), lineCrossEntity.getEndTower());
            DefectType defectType = DefectTypeDBHelper.getInstance().getParentDefect(lineCrossEntity.getCrossType());
            lineCrossEntity.setCrossTypeFirst(defectType.getDefectName());

            ((TextView) dialog.findViewById(R.id.tv_line_name)).setText(lineCrossEntity.getVoltageClass() + lineCrossEntity.getLineName());
            ((TextView) dialog.findViewById(R.id.tv_cross_state)).setText("交跨点状态：");
            ((TextView) dialog.findViewById(R.id.tv_tower_defect_num)).setText("" + lineCrossEntity.getCrossStatus());

            ((TextView) dialog.findViewById(R.id.tv_tower_start_end)).setText(tower);
            ((TextView) dialog.findViewById(R.id.tv_small_size)).setText(lineCrossEntity.getDtoSmartTower() + "");
            ((TextView) dialog.findViewById(R.id.tv_tower_air_size)).setText(lineCrossEntity.getClearance() + "");
            ((TextView) dialog.findViewById(R.id.tv_cross_foot)).setText(lineCrossEntity.getCrossAngle() + "");
            ((TextView) dialog.findViewById(R.id.tv_type1)).setText(lineCrossEntity.getCrossTypeFirst());
            ((TextView) dialog.findViewById(R.id.tv_type2)).setText(lineCrossEntity.getCrossTypeName());

            Button btn_navi = (Button) dialog.findViewById(R.id.btn_navi);
            Button btn_more = (Button) dialog.findViewById(R.id.btn_more);
            btn_navi.setText("标注不存在");
            btn_more.setText("修改");
            final Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.putExtra("FLAG", 2);
            bundle.putSerializable("OptProject", lineCrossEntity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);

            dialog.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //标注不存在

                    intent.putExtra("flag", 1);
                    intent.setClass(mRef.get(), TourResultActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("flag", 0);
                    intent.setClass(mRef.get(), TourResultActivity.class);
                    startActivity(intent);

                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String showTowerId(String towerNo, String nearTowerNo) {
        try {
            int tower = Integer.valueOf(towerNo);
            int nearTower = Integer.valueOf(nearTowerNo);
            if (tower < nearTower) {
                towerNo = towerNo + "-" + nearTowerNo;
            } else {
                towerNo = nearTowerNo + "-" + towerNo;
            }
        } catch (Exception e) {
            towerNo = towerNo + "-" + nearTowerNo;
        }

        return towerNo;
    }

    /**
     * 展示塔的详情弹出框
     *
     * @param attrs
     */
    private String lineName;

    private void showTowerDetailDialog(final Map<String, Object> attrs) {
        try {
            final AnimDialog dialog = new AnimDialog(mRef.get());
            final double lat = (double) attrs.get(AppConstant.TOWER_LAT_KEY);
            final double lng = (double) attrs.get(AppConstant.TOWER_LNG_KEY);
            double altitude = (double) attrs.get(AppConstant.TOWER_ALTITUDE_KEY);
            final long towerId = (long) attrs.get(AppConstant.TOWER_ID_KEY);
            final String towerNum = (String) attrs.get(AppConstant.TOWER_NUM_KEY);
            lineName = (String) attrs.get(AppConstant.TOWER_LINENAME_KEY);
            if (lineName == null) {
                lineName = GridLineDBHelper.getInstance().getLine(Long.parseLong("" + attrs.get(AppConstant.LINE_ID))).getLineName();
            }
            dialog.showDialog(R.layout.update_tower_layout, 0, 250);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (mCall != null && !mCall.isCanceled()) {
                        mCall.cancel();
                    }
                }
            });
            ((TextView) dialog.findViewById(R.id.tv_line_name)).setText(lineName);
            ((TextView) dialog.findViewById(R.id.tv_tower_num)).setText("#" + towerNum);
            ((TextView) dialog.findViewById(R.id.tv_tower_lat)).setText(lat + "");
            ((TextView) dialog.findViewById(R.id.tv_tower_lng)).setText(lng + "");
            ((TextView) dialog.findViewById(R.id.tv_tower_altitude)).setText(altitude + "");

            TextView tv_big = ((TextView) dialog.findViewById(R.id.tv_big));
            TextView tv_small = ((TextView) dialog.findViewById(R.id.tv_small));

            Tower tower = TowerDBHelper.getInstance().getTower(towerId);
            if (tower != null) {
                Tower tower_small = TowerDBHelper.getInstance().getTower(tower.getSysGridLineId(), tower.getDisplayOrder() - 1);
                Tower tower_big = TowerDBHelper.getInstance().getTower(tower.getSysGridLineId(), tower.getDisplayOrder() + 1);
                if (tower_small != null) {
                    tv_small.setText(getDistance(tower_small, tower) + "米");
                }
                if (tower_big != null) {
                    tv_big.setText(getDistance(tower, tower_big) + "米");
                }
            }

            TextView tv = (TextView) dialog.findViewById(R.id.tv_tower_defect_num);
            setDefectCount(tv, towerId);
            naviTowerName = (lineName == null ? "" : lineName) + (towerNum == null ? "" : towerNum) + "号杆塔";
            dialog.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.btn_navi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectDialog(lat, lng);
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.btn_update_tower).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUpdateTowerDialog(attrs);
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toTowerDtail = new Intent(getActivity(), TowerDetailActivity.class);
                    toTowerDtail.putExtra("lineId", getLineIdByLineName(lineName));
                    toTowerDtail.putExtra("lineName", lineName);
                    toTowerDtail.putExtra("towerNum", towerNum);
                    toTowerDtail.putExtra("towerId", towerId);
                    startActivity(toTowerDtail);
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSelectDialog(final double lat, final double lng) {

        final String[] types = {"显示导航路线", "跳转导航页面"};
        final AlertDialog.Builder alert = new AlertDialog.Builder(mRef.get());
        alert.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LatLngInfo info = CoordinateUtils.gps84_To_Gcj02(lat, lng);
                switch (which) {
                    case 0:
                        EventBus.getDefault().post(new NaviPathEvent(info.latitude, info.longitude));
                        break;
                    case 1:
                        Poi end = new Poi(naviTowerName, new LatLng(info.latitude, info.longitude), "");
                        AmapNaviPage.getInstance().showRouteActivity(mRef.get(), new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), new NavipathListener());
                        break;
                }
            }
        });
        alert.create().show();
    }

    private int getDistance(Tower from, Tower to) {
        double latitude = from.getLatitude();
        double longitude = from.getLongitude();

        double toLat = to.getLatitude();
        double toLng = to.getLongitude();
        return (int) LatLngUtils.getDistance(latitude, longitude, toLat, toLng);
    }

    private void setDefectCount(final TextView tv, long towerId) {
        if (!AppConstant.NET_WORK_AVAILABLE) {
            return;
        }
        mCall = RetrofitManager.getInstance().getService(ApiService.class).getLineDefectCount((int) towerId, 0, "all", 0);
        mCall.enqueue(new Callback<DefectCount>() {
            @Override
            public void onResponse(Call<DefectCount> call, Response<DefectCount> response) {
                if (response != null && response.body().getCode() == 1) {
                    tv.setText(response.body().getDefectCount() + "");
                }
            }

            @Override
            public void onFailure(Call<DefectCount> call, Throwable t) {

            }
        });
    }

    private void showUpdateTowerDialog(final Map<String, Object> attrs) {
        DialogUpdateTower dialogUpdateTower = new DialogUpdateTower(mRef.get());
        dialogUpdateTower.setCanceledOnTouchOutside(false);
        dialogUpdateTower.setDatas(attrs);
        dialogUpdateTower.show();
        dialogUpdateTower.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handleDialogDismiss(attrs);
            }
        });
    }

    private void handleDialogDismiss(final Map<String, Object> attrs) {

        Long towerId = (Long) attrs.get(AppConstant.TOWER_ID_KEY);
        VirtualTower virtualTower = VirtualTowerDBHelper.getInstance().getVirtualTower(towerId.intValue());
        if (virtualTower == null)
            return;
        double lat = (double) attrs.get(AppConstant.TOWER_LAT_KEY);
        double lng = (double) attrs.get(AppConstant.TOWER_LNG_KEY);

        double latitude = virtualTower.getLatitude();
        double longitude = virtualTower.getLongitude();

        if (lat == latitude && lng == longitude)
            return;

        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                showVirtualTowers(attrs);
            }
        });
    }

    /**
     * 显示修改经纬度后的虚拟塔
     *
     * @param attrs
     */
    private void showVirtualTowers(Map<String, Object> attrs) {
        boolean isFromAreaTower = (boolean) attrs.get(AppConstant.IS_FROM_AREA_TOWER);
        if (isFromAreaTower) {
            showAreaVirtualTower();
        } else {
            showSingleVirtualTower((int) attrs.get(AppConstant.LINE_ID));
        }
    }

    private void showSingleVirtualTower(int lineId) {
        if (mVirtualTowerGraphics.containsKey(lineId)) {//根据lineId移除相关塔
            List<Integer> graphics = mVirtualTowerGraphics.get(lineId);
            for (Integer graphic : graphics) {
                mVirtualPointLayer.removeGraphic(graphic);
            }
            mVirtualTowerGraphics.remove(lineId);
        }
        List<VirtualTower> virtualTowers = VirtualTowerDBHelper.getInstance().getVirtualTowers(lineId);
        List<Integer> graphics = new ArrayList<>();
        if (virtualTowers != null && virtualTowers.size() > 0) {
            for (VirtualTower tower : virtualTowers) {
                Tower towerEx = TowerDBHelper.getInstance().getTower(tower.getTowerId());
                if (tower.getStatus() != 0 || towerEx == null || (towerEx.getLatitude() == tower.getLatitude() && towerEx.getLongitude() == tower.getLongitude())) {
                    VirtualTowerDBHelper.getInstance().delete(tower);
                    continue;
                }
                //画虚拟塔
                Point point = getPointfromgps(tower.getLatitude(), tower.getLongitude());
                Map<String, Object> map = new HashMap<>();
                map.put(AppConstant.TOWER_NUM_KEY, tower.getTowerName());
                int towerGraphic = mVirtualPointLayer.addGraphic(new Graphic(point, mVirtualTowerSymbol, map));
                graphics.add(towerGraphic);
                //添加塔编号
                ImageMarkerSymbol textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33"))
                        .setTextSize(UIUtils.getDimen(R.dimen.tower_name_text_size)).createMapBitMap(tower.getTowerName()));
                textSymbol.setOffsetY(mVirtualTowerSymbol.getHeight() / 2 + textSymbol.getHeight() / 2 + 20);
                textSymbol.setOffsetX(-3);
                int textGraphic = mVirtualPointLayer.addGraphic(new Graphic(point, textSymbol));
                graphics.add(textGraphic);

                //获取前后位置塔
                Tower preTower = TowerDBHelper.getInstance().getTower(tower.getLineId(), tower.getPreTowerOrder());
                Tower nextTower = TowerDBHelper.getInstance().getTower(tower.getLineId(), tower.getNextTowerOrder());

                if (preTower == null && nextTower == null)
                    continue;

                Polyline polyline = new Polyline();
                Point virtual = getPointfromgps(tower.getLatitude(), tower.getLongitude());

                //根据displayOrder获取前后位置虚拟塔
                VirtualTower preVT = VirtualTowerDBHelper.getInstance().getVirtualTower(tower.getLineId(), tower.getPreTowerOrder());
                VirtualTower nextVT = VirtualTowerDBHelper.getInstance().getVirtualTower(tower.getLineId(), tower.getNextTowerOrder());

                if (preTower != null) {
                    Point previous = getPointfromgps(preVT == null ? preTower.getLatitude() : preVT.getLatitude(), preVT == null ? preTower.getLongitude() : preVT.getLongitude());
                    polyline.startPath(previous);
                    polyline.lineTo(virtual);
                } else {
                    polyline.startPath(virtual);
                }
                if (nextTower != null) {
                    Point next = getPointfromgps(nextVT == null ? nextTower.getLatitude() : nextVT.getLatitude(), nextVT == null ? nextTower.getLongitude() : nextVT.getLongitude());
                    polyline.lineTo(next);
                }
                //画虚拟线
                int graphic = mVirtualPointLayer.addGraphic(new Graphic(polyline, mVirtualLineSymbol));
                graphics.add(graphic);
            }
            mVirtualTowerGraphics.put(lineId, graphics);
        }
    }

    private void showAreaVirtualTower() {
        mAreaVirtualLayer.removeAll();
        if (mAreaTowers != null && mAreaTowers.size() > 0) {
            for (Tower areaTower : mAreaTowers) {
                VirtualTower virtualTower = VirtualTowerDBHelper.getInstance().getVirtualTower(areaTower.getSysTowerID());
                if (virtualTower != null) {
                    if (virtualTower.getStatus() != 0 || (virtualTower.getLatitude() == areaTower.getLatitude() && virtualTower.getLongitude() == areaTower.getLongitude())) {
                        VirtualTowerDBHelper.getInstance().delete(virtualTower);
                        continue;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put(AppConstant.TOWER_NUM_KEY, areaTower.getTowerNo());//暂时只用到塔编号
                    drawVirtualTower(virtualTower, mAreaVirtualLayer, map);
                }
            }
        }
    }

    private void drawVirtualTower(VirtualTower virtualTower, GraphicsLayer layer, Map<String, Object> map) {
        if (virtualTower == null || layer == null)
            return;
        //画虚拟塔
        Point point = getPointfromgps(virtualTower.getLatitude(), virtualTower.getLongitude());
        layer.addGraphic(new Graphic(point, mVirtualTowerSymbol, map));
        //添加塔编号
        ImageMarkerSymbol textSymbol = new ImageMarkerSymbol(new PictureSymbolUtils.Builder().setTextColor(Color.parseColor("#ccff33"))
                .setTextSize(UIUtils.getDimen(R.dimen.tower_name_text_size)).createMapBitMap(virtualTower.getTowerName()));
        textSymbol.setOffsetY(mVirtualTowerSymbol.getHeight() / 2 + textSymbol.getHeight() / 2 + 20);
        textSymbol.setOffsetX(-3);
        layer.addGraphic(new Graphic(point, textSymbol));
    }

    private void initSearch() {
        mSearchLineEdit.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 先隐藏键盘
            ((InputMethodManager) mRef.get()
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mSearchLineEdit.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            // 搜索，进行自己要的操作...
            seachList();//这里是我要做的操作！
            return true;
        }
        return false;
    }

    private void seachList() {
        String lineName = mSearchLineEdit.getText().toString();
        locateAtTower(getLineIdByLineName(lineName));
    }

    private String getLineIdByLineName(String lineName) {
        String lineId = "";
        for (Map.Entry<String, String> entry : MyApplication.mLineIdNamePairs.entrySet()) {
            if (entry.getValue().contains(lineName)) {
                lineId = entry.getKey();
                break;
            }
        }
        return lineId;
    }


    public void locateAtTower(String lineid) {
        if (!lineid.equalsIgnoreCase("")) {
            List<Tower> towers = MyApplication.mAllTowersInMap.get(lineid);
            if (towers != null && towers.size() > 0) {
                Tower tower = towers.get(0);
                double latitude = tower.getLatitude();
                double longitude = tower.getLongitude();
                Point point = getPointfromgps(latitude, longitude);
                mMapView.centerAt(point, false);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.pause();
        isViewActive = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.unpause();
        isViewActive = true;
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onStop() {
        super.onStop();
        // 取消监听

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThread(BaseMainThreadEvent event) {
        if (event instanceof RegisterLocationEvent) {
            currentInpectionType = ((RegisterLocationEvent) event).getInplace().getPatrolType();
            if (currentInpectionType.equalsIgnoreCase(AppConstant.SPECIAL_INSPECTION)) {
                updateRegisterButton(0);
            }
            submitLocationRegist(((RegisterLocationEvent) event).getInplace());//到位登记
        } else if (event instanceof NaviLocationEvent) {
            updateNaviLocation(((NaviLocationEvent) event).getLocation());
        } else if (event instanceof NaviRoutesEvent) {//地图上显示导航路径
            if (!isViewActive)
                return;
            mNaviLayer.removeAll();
            mLineIndexs.clear();
            showAuoNaviPath(((NaviRoutesEvent) event).getRouteIds(), ((NaviRoutesEvent) event).getPaths());
            showCarRouteResult(((NaviRoutesEvent) event).getPaths(), ((NaviRoutesEvent) event).getAMapNavi());
        } else if (event instanceof SelectNaviPathEvent) {
            List<Integer> list = new ArrayList<>();
            for (Integer i : mLineIndexs.keySet()) {
                list.add(i);
            }
            Collections.sort(list);
            changeRoute(list.get(((SelectNaviPathEvent) event).getPathIndex()));
            changeDetail(mLineIndexs.get(list.get(((SelectNaviPathEvent) event).getPathIndex())));
        } else if (event instanceof StartNaviEvent) {
            isNaving = true;
            showNaviInfoView();
        } else if (event instanceof UpdateNaviInfoEvent) {
            mNaviInfoview.setData(((UpdateNaviInfoEvent) event).getNaviInfo());
        } else if (event instanceof StopNaviEvent) {
            stopNavi();
            isNaving = false;
        } else if (event instanceof ClearCarRouteEvent) {
            mNaviLayer.removeAll();
            mLineIndexs.clear();
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        } else if (event instanceof UpdateLocationEvent) {
            if (!isNaving) {//不是导航 模式 更新定位
                updateGpsLocate();
            }
            showNearestTower();
        } else if (event instanceof StopAllTaskEvent) {
            removeAllLines();
        } else if (event instanceof UpdateLineCrossList) {
            updateLineCrossList(((UpdateLineCrossList) event).isDelete());
        }
    }

    //将到位登记文字 更改为 开启巡视 和 结束巡视
    private void updateRegisterButton(int type) {
        switch (type) {
            case 0://将文字改为结束巡视
                mRegisterBtn.setText("结束巡视");
                break;
            case 1://将文字改为 到位登记
                mRegisterBtn.setText("到位登记");
                ToastUtil.show("巡视已结束");
                currentInpectionType = null;
                break;
            default:
                break;
        }
    }


    //显示计算出来的线路
    private void showAuoNaviPath(int[] ids, Map<Integer, AMapNaviPath> paths) {
        for (int i = 0; i < ids.length; i++) {
            AMapNaviPath path = paths.get(ids[i]);
            if (path != null) {
                drawPath(ids[i], path);
            }
        }
    }


    //更新 导航当前位置
    private void updateNaviLocation(AMapNaviLocation location) {
        try {
            NaviLatLng gcj = location.getCoord();
            LatLngInfo me = CoordinateUtils.lonLatToMercator(gcj.getLongitude(), gcj.getLatitude());
            Point point = new Point(me.longitude, me.latitude);
            mGpsPointSymbol.setAngle(location.getBearing());
            mGpsLayer.updateGraphic(mGpsLocation.get(AppConstant.GPS_LOCATION_ID), new Graphic(point, mGpsPointSymbol));
            mMapView.centerAt(point, false);
        } catch (Exception e) {
            e.printStackTrace();
            locate(true);
        }

    }


    private void drawPath(int routeid, AMapNaviPath path) {
        List<NaviLatLng> pointList = path.getCoordList();
        Polyline polyline = new Polyline();
        for (int i = 0; i < pointList.size(); i++) {
            LatLngInfo mercator = CoordinateUtils.lonLatToMercator(pointList.get(i).getLongitude(), pointList.get(i).getLatitude());
            Point point = new Point(mercator.longitude, mercator.latitude);
            if (i == 0) {
                polyline.startPath(point);
                mMapView.centerAt(point, true);
            } else {
                polyline.lineTo(point);
            }
        }
        int lineid = mNaviLayer.addGraphic(new Graphic(polyline, new SimpleLineSymbol(Color.BLUE, 3.0f)));
        mLineIndexs.put(lineid, routeid);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    private void showCarRouteResult(Map<Integer, AMapNaviPath> map, AMapNavi aMapNavi) {
        mSlidingLayout.setPanelHeight(UIUtils.dp2px(150));
        if (map == null || map.size() == 0) {
            ToastUtil.show("当前没有规划线路，请先规划线路");
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(mRef.get());
        final CarRoutePanel panel = (CarRoutePanel) inflater.inflate(R.layout.layout_route_car_result, null);
        dragView.setPanel(panel);
        List<AMapNaviPath> paths = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        for (Integer i : mLineIndexs.keySet()) {
            list.add(i);
        }
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            AMapNaviPath path = map.get(mLineIndexs.get(list.get(i)));
            paths.add(path);
        }
        panel.setData(paths);
        CarRouteDetail detail = (CarRouteDetail) inflater.inflate(R.layout.layout_car_route_detail, null);
        List<AMapNaviGuide> guides = aMapNavi.getNaviGuideList();
        AMapNaviPath path = aMapNavi.getNaviPath();
        detail.setData(path, guides, naviTowerName);
        dragView.setContent(detail);
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void changeRoute(int lineid) {
        Log.i("lineid", "changeRoute=" + lineid);
        for (Map.Entry<Integer, Integer> entry : mLineIndexs.entrySet()) {
            Log.i("lineid", "line=" + entry.getKey());
            if (entry.getKey() != lineid) {
                mNaviLayer.updateGraphic(entry.getKey(), new SimpleLineSymbol(Color.argb(50, 0, 51, 255), 3.0f));
            }
        }
        mNaviLayer.updateGraphic(lineid, new SimpleLineSymbol(Color.argb(255, 0, 51, 255), 3.0f));
    }

    private void changeDetail(int routeId) {
        AMapNavi.getInstance(MyApplication.getContext()).selectRouteId(routeId);
        AMapNaviPath path = AMapNavi.getInstance(MyApplication.getContext()).getNaviPaths().get(routeId);
        LayoutInflater inflater = LayoutInflater.from(mRef.get());
        CarRouteDetail detail = (CarRouteDetail) inflater.inflate(R.layout.layout_car_route_detail, null);
        List<AMapNaviGuide> guides = AMapNavi.getInstance(MyApplication.getContext()).getNaviGuideList();
        detail.setData(path, guides, naviTowerName);
        dragView.setContent(detail);
    }

    //显示导航 信息
    private void showNaviInfoView() {
        mCommonLayout.setVisibility(View.INVISIBLE);
        mNaviInfoview.setVisibility(View.VISIBLE);
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    private void stopNavi() {
        mCommonLayout.setVisibility(View.VISIBLE);
        mNaviInfoview.setVisibility(View.INVISIBLE);
        mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void clearAllPoints() {
        mPointMap.clear();
        mLineDefectMap.clear();
        mLineTextMap.clear();
        mLineMap.clear();
        mLineTowerMap.clear();
        mTowerDefectList.clear();
        mVirtualTowerGraphics.clear();
    }

    @Override
    public void showCrossList(List<LineCrossEntity> lineCrossEntities) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < lineCrossEntities.size(); i++) {
            int id = addSingleCrossP(lineCrossEntities.get(i));
            if (id != -1) {
                ids.add(id);
            }
        }
        mLineCrossMap.put(lineCrossEntities.get(0).getLineName(), ids);

    }


    private int addSingleCrossP(LineCrossEntity entity) {
        String latitude = entity.getCrossLatitude();
        String longitude = entity.getCrossLongitude();
        int id = -1;
        try {
            double lat = Double.parseDouble(latitude);
            double lng = Double.parseDouble(longitude);
            Point point = getPointfromgps(lat, lng);

            Map<String, Object> attrs = new HashMap<>();
            DefectType defectType = DefectTypeDBHelper.getInstance().getParentDefect(entity.getCrossType());
            entity.setCrossTypeFirst(defectType.getDefectName());
            attrs.put(AppConstant.LINE_CROSS_ID, entity.getPlatformId());
            //            attrs.put(AppConstant.LINE_CROSS_NAME, entity.getLineName());
            //            attrs.put(AppConstant.LINE_CROSS_START_TOWER, entity.getStartTower());
            //            attrs.put(AppConstant.LINE_CROSS_END_TOWER, entity.getEndTower());
            //            attrs.put(AppConstant.LINE_CROSS_TYPE_FRIST, entity.getCrossType());
            //            attrs.put(AppConstant.LINE_CROSS_TYPE_NAME, entity.getCrossTypeName());
            //            attrs.put(AppConstant.LINE_CROSS_CREATE_TIME, entity.getCreatedTime());
            //            attrs.put(AppConstant.LINE_CROSS_STATE, entity.getCrossStatus());
            //            attrs.put(AppConstant.LINE_CROSS_Remark, entity.getRemark());
            //            attrs.put()


            id = mLineCrossLayer.addGraphic(new Graphic(point, mCrossPointSymbol, attrs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private void removeLineCross(String lineid, boolean delete) {
        if (MyApplication.mLineIdNamePairs != null && MyApplication.mLineIdNamePairs.size() > 0) {
            for (Map.Entry entry : MyApplication.mLineIdNamePairs.entrySet()) {
                if (entry.getKey().equals(lineid)) {
                    List<Integer> ids = mLineCrossMap.get(entry.getValue());
                    if (ids == null) {
                        break;
                    }
                    for (int i : ids) {
                        mLineCrossLayer.removeGraphic(i);
                    }
                    mLineCrossMap.remove(lineid);
                    break;
                }
            }
            if (delete) {
                MyApplication.mLineIdNamePairs.remove(lineid + "");
            }
        }
    }

    private void removeAllLineCross() {
        mLineCrossLayer.removeAll();
    }

    private void updateLineCrossList(boolean delete) {
        removeLineCross(MyApplication.registeredTower.getSysGridLineId() + "", delete);
        mPresenter.getLineCrossList(MyApplication.registeredTower.getSysGridLineId() + "");
    }

    /**
     * 到位登记请求
     * @param inPlace
     */
    public void submitLocationRegist(final InPlace inPlace) {
        double lat = AppConstant.CURRENT_LOCATION.latitude;
        double lng = AppConstant.CURRENT_LOCATION.longitude;
        double altitude = AppConstant.CURRENT_LOCATION.altitude;
        LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(lat, lng);
        AppConstant.CURRENT_INPLACE = inPlace;
        String neartTowerId = null;
        if (inPlace.getNearTowerId() != 0) {
            neartTowerId = inPlace.getNearTowerId() + "";
        }
        Map<String, RequestBody> params = initParams(inPlace, gps, altitude, neartTowerId);
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        mSubmitCallback = RetrofitManager.getInstance().getService(ApiService.class).postRegisterInplace(params, requestImgParts);
        mSubmitCallback.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response == null || response.body() == null) {
                    return;
                }
                if (response.body().getCode() == 1) {
                    currentInpectionType = inPlace.getPatrolType();
                    inPlace.setUploadFlag(1);//将状态改为已上传
                    InPlaceDBHelper.getInstance().updateInplace(inPlace);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                if (t != null) {
                    Log.e("到位登记", "" + t.getMessage());
                }
            }
        });
        EventBus.getDefault().post(new CloseDialogEvent());
        if (MyApplication.registeredTower != null) {//上次到位登记的塔
            if (MyApplication.registeredTower.getSysGridLineId() != MyApplication.currentNearestTower.getSysGridLineId()) {
                removeAllDefectsByLineid(MyApplication.registeredTower.getSysGridLineId() + "");
            }
        }
        //        mPresenter.getDefectsByTowerId(MyApplication.currentNearestTower.getSysTowerID() + "");

        if (MyApplication.isRegisterAuto == false) {
            MyApplication.registeredTower = TowerDBHelper.getInstance().getTower(inPlace.getSysTowerId());
        } else {
            MyApplication.registeredTower = MyApplication.currentNearestTower;
        }


        ((MainActivity) mRef.get()).getSecondNearTower();
    }

    private Map<String, RequestBody> initParams(InPlace inPlace, LatLngInfo gps, double altitude, String neartTowerId) {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody longtitude = RequestBody.create(MediaType.parse("multipart/form-data"), gps.longitude + "");
        params.put("Longitude", longtitude);
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), gps.latitude + "");
        params.put("Latitude", latitude);
        RequestBody altitudeR = RequestBody.create(MediaType.parse("multipart/form-data"), altitude + "");
        params.put("Altitude", altitudeR);
        RequestBody partolType = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getPatrolType());
        params.put("PatrolType", partolType);
        RequestBody towerId = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getSysTowerId() + "");
        params.put("TowerId", towerId);
      /*  RequestBody nearTowerId = RequestBody.create(MediaType.parse("multipart/form-data"), neartTowerId + "");
        params.put("NearTowerId", nearTowerId);*/
        if (neartTowerId != null) {
            RequestBody nearTowerId = RequestBody.create(MediaType.parse("multipart/form-data"), neartTowerId);
            params.put("NearTowerId", nearTowerId);
        } else {
            RequestBody nearTowerId = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getNearTowerId() + "");
            params.put("NearTowerId", nearTowerId);
        }
        RequestBody createDate = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getCreateDate() + "");
        params.put("CreateDate", createDate);
        RequestBody sysUserId = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getSysUserId() + "");
        params.put("sysUserId", sysUserId);
        RequestBody sysPatrolExecutionID = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getSysPatrolExecutionID() + "");
        params.put("sysPatrolExecutionID", sysPatrolExecutionID);
        return params;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
            return;
        }
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION:
                float x = event.values[0];
                x += getScreenRotationOnPhone(mRef.get());
                x %= 360.0F;
                if (x > 180.0F) {
                    x -= 360.0F;
                } else if (x < -180.0F) {
                    x += 360.0F;
                }
                if (Math.abs(mAngle - x) < 3.0f) {
                    break;
                }
                mAngle = Float.isNaN(x) ? 0 : x;
                if (mGpsPointSymbol != null) {
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            mGpsPointSymbol.setAngle(mAngle);
                            if (mGpsLocation.get(AppConstant.GPS_LOCATION_ID) != null) {
                                mGpsLayer.updateGraphic(mGpsLocation.get(AppConstant.GPS_LOCATION_ID), mGpsPointSymbol);
                            }

                        }
                    });
                }
                lastTime = System.currentTimeMillis();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /**
     * 获取当前屏幕旋转角度
     *
     * @param context
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getScreenRotationOnPhone(Context context) {
        final Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                return 0;

            case Surface.ROTATION_90:
                return 90;

            case Surface.ROTATION_180:
                return 180;

            case Surface.ROTATION_270:
                return -90;
            default:
                break;
        }
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public static class NavipathListener implements INaviInfoCallback {


        @Override
        public void onInitNaviFailure() {

        }

        @Override
        public void onGetNavigationText(String s) {
            Log.d("nate", "onGetNavigationText: " + s);
            nonBlockSyntherizer.speak(s);
        }

        @Override
        public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

        }

        @Override
        public void onArriveDestination(boolean b) {

        }

        @Override
        public void onStartNavi(int i) {

        }

        @Override
        public void onCalculateRouteSuccess(int[] ints) {

        }

        @Override
        public void onCalculateRouteFailure(int i) {

        }

        @Override
        public void onStopSpeaking() {
            if (nonBlockSyntherizer != null) {
                nonBlockSyntherizer.stop();
            }
        }
    }

}