package com.uflycn.uoperation.constant;

import com.uflycn.uoperation.bean.InPlace;
import com.uflycn.uoperation.bean.User;
import com.xflyer.entity.LatLngInfo;

public class AppConstant {

    public static String COMPANY_NAME = "";

    public static final String DATE_INIT_TIME = "2000-01-01 00:00:00";
    public static final double BING_INIT_LON = 113.451640;
    public static final double BING_INIT_LAT = 23.163750;
    public static final double BING_INIT_SCALE = 12000;
    public static final String APP_STORAGE_PATH = "UOperation";
    public static final String UFLY_STORAGE_PATH = "优飞科技";

    public static final String DIR_GOOGLE_CACHE = "GoogleCache";
    public static final String DIR_GAODE_CACHE = "GaodeCache";

    public static final String CAMERA_PHOTO_PATH = "CAMERA_PHOTO_PATH";
    public static final String BREAK_PHOTO_PATH = "BREAK_PHOTO_PATH";

    public static final String DEFECT_PHOTO_PATH = "DEFECT_PHOTO_PATH";

    public static final String DOCUMENT_PATH = "DOCUMENT_PATH";

    public static final String DIR_LOG = "Log";
    public static final String MAP_TYPE_KEY = "MAP_TYPE_KEY";
    public static final int REQUEST_CODE_CAMERA = 10001;
    public static final String PRODUCT_NAME = "UOperation";
    public static final String REGISTER_KEY_PATH = "register/";

    public static final String REGISTER_KEY_NAME = "key";

    public static final String REGISTER_INFO_NAME = "info";

    public static final String REGISTER_SEED = "uflyseed";
    public static final String SP_ALREADY_SUBMIT = "already_submit";
    public static final String SP_Tower_Radius = "tower_radius";
    public static boolean NET_WORK_AVAILABLE = false;

    public static int mDistance = 0;

    public static String DEVICE_NO = "";

    public static String TOKEN = "";
    public static User currentUser;
    public static final String DB_DIR = "DB";
    public static final String DB_NAME = "operation.db";
    public static int position = 0;
    public static final String POSITION = "POSITION";
    public static final String PICTURE_PATH = "PICTURE_PATH";
    public static final String DEFECT_ID = "DEFECT_ID";
    //定位当前位置
    public static LatLngInfo CURRENT_LOCATION;
    public static int LAST_INSPECTION_TYPE_ID = 0;

    // 注册状态网络返回值
    // 已注册
    public static final String REGISTER_STATUS_REGISTERED = "1";

    // 等待审核
    public static final String REGISTER_STATUS_WAITING = "2";

    // 未注册
    public static final String REGISTER_STATUS_UNREGISTER = "3";

    // 过期
    public static final String REGISTER_STATUS_OVERDUE = "4";

    // 被拒绝
    public static final String REGISTER_STATUS_REJECT = "5";

    // 已续期
    public static final String REGISTER_STATUS_RENEW = "6";

    // 账号已被锁定
    public static final String REGISTER_STATUS_DISABLE = "7";


    public static final String GPS_LOCATION_ID = "gps_location";
    public static final String TOWER_LAT_KEY = "TOWER_LAT_KEY";
    public static final String TOWER_LNG_KEY = "TOWER_LNG_KEY";
    public static final String TOWER_ALTITUDE_KEY = "TOWER_ALTITUDE_KEY";
    public static final String TOWER_LINENAME_KEY = "TOWER_LINENAME_KEY";
    public static final String TOWER_NUM_KEY = "TOWER_NUM_KEY";
    public static final String TOWER_ID_KEY = "TOWER_ID_KEY";
    public static final String IMAGE_CATAGORY = "Category";
    public static final String LINE_ID = "LINE_ID";
    public static final String IS_FROM_AREA_TOWER = "FROM_AREA_TOWER";
    public static final String AREA_TOWERS = "AREA_TOWERS";

    public static final String LINE_CROSS_ID = "LINE_CROSS_ID";
    public static final String LINE_CROSS_NAME = "LINE_CROSS_NAME";
    public static final String LINE_CROSS_START_TOWER = "LINE_CROSS_START_TOWER";
    public static final String LINE_CROSS_END_TOWER = "LINE_CROSS_END_TOWER";
    public static final String LINE_CROSS_TYPE_FRIST = "LINE_CROSS_TYPE_FRIST";
    public static final String LINE_CROSS_TYPE_NAME = "LINE_CROSS_TYPE_NAME";
    public static final String LINE_CROSS_CREATE_TIME = "LINE_CROSS_CREATE_TIME";
    public static final String LINE_CROSS_STATE = "LINE_CROSS_STATE";
    public static final String LINE_CROSS_Remark = "LINE_CROSS_Remark";


    /**
     * 塔类型 直线塔 耐张塔
     */
    public static final String TOWER_TYPE = "tower_type";
    /**
     * 绝缘串 单串 双串
     */
    public static final String TOWER_SUB_TYPE = "tower_sub_type";

    /**
     * 跳线串类型 单串 双串
     */
    public static final String JUMP_TYPE = "jump_type";

    /**
     * 跳线片数
     */
    public static final String JUMP_COUNT = "jump_count";

    /**
     * 已选择的塔
     */
    public static final String SELECT_TOWER = "select_tower";

    /**
     * 绝缘子片数
     */
    public static final String INSULATOR_COUNT = "insulator_count";

    public static InPlace CURRENT_INPLACE;

    public static final String SPECIAL_INSPECTION = "6fa7e4b6-70bb-40e0-b768-d05fe8152ec8";

    /**
     * 系统用到的文件目录
     */
    public static final String[] DIRECTORYS = new String[]{
            DIR_LOG, DB_DIR, CAMERA_PHOTO_PATH};
    public static final String[] DIRECTORYS1 = new String[]{
            DIR_GOOGLE_CACHE, DIR_GAODE_CACHE};
    public static boolean isDisDefeect = false;

    public static boolean IS_AUTO_LOGIN = true;

    public static int JUMP_DEFECT = 1;

    public static int ScreenHeight = 0;
    public static int ScreenWidth = 0;

    /**
     * 避免拥挤
     */
    public static final String AVOID_CONGESTION = "avoid_congestion";

    /**
     * 不走高速
     */
    public static final String NOT_HIGH_SPEED = "not_high_speed";

    /**
     * 避免收费
     */
    public static final String AVOID_CHARGES = "avoid_charges";

    /**
     * 高速优先
     */
    public static final String HIGH_SPEED_PRIORITY = "HIGH_SPEED_PRIORITY";

    public static final String APPID = "10353947";

    public static final String APPKEY = "wDoPM4vGvmkUCAWhIoUNkUTf";

    public static final String SECRETKEY = "qDk802M324YGI9QaNWjr6ITGdDvGZKCP ";

    public static final String TOWER_DEFECT_KEY = "TOWER_DEFECT_KEY";


    public static final String TEMP_TASK_NUM_DEL = "com.uflycn.uoperation.TEMP_TASK_NUM_DEL";
    public static final String TEMP_TASK_NUM_ADD = "com.uflycn.uoperation.TEMP_TASK_NUM_ADD";
    public static final String TEMP_TASK_NET_CHANGE = "com.uflycn.uoperation.TEMP_TASK_NET_CHANGE";

    public static final String WORK_SHEET_NUM_DEL = "com.uflycn.uoperation.WORK_SHEET_NUM_DEL";
    public static final String WORK_SHEET_NUM_ADD = "com.uflycn.uoperation.WORK_SHEET_NUM_ADD";

    public static final String TEMP_TASK = "rb_temp_task";
    public static final String WORK_SHEET = "rb_work_sheet";


    //通道缺陷 = 0,
    //精细化缺陷 = 1,
    //人巡缺陷 = 2,
    //树障缺陷 = 3,
    //交跨 = 4,
    //外破 = 5,
    //项目 = 6
    public static final int CHANNEL_DEFECT_SHEET = 0;
    public static final int FINE_DEFECT_SHEET = 1;
    public static final int PATROL_DEFECT_SHEET = 2;
    public static final int TREE_DEFECT_SHEET = 3;
    public static final int CROSS_DEFECT_SHEET = 4;
    public static final int BROKEN_SHEET = 5;
    public static final int PROJECT_SHEET = 6;

    //到位登记是否可以选择
    public static final Boolean isChooseRegister = true;


}
