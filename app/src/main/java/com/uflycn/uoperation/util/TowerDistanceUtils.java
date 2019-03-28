package com.uflycn.uoperation.util;

import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;
import com.xflyer.utils.LatLngUtils;

import java.util.List;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2018/12/18
 * Describe  :
 */
public class TowerDistanceUtils {
    //获取同一条线路中第二近的塔
    public Tower getSecondNearTower(Tower tower_regis) {
        if (tower_regis == null) {
            return null;
        }
        double currentDis = getDistance(tower_regis); //当前位置到到登记塔距离
        double minDistance = Double.MAX_VALUE;      //最小距离
        Tower secondNearTower = null;               //第二近的塔
        int order = tower_regis.getDisplayOrder();  //到位登记塔的oreder

        //        List<Tower> towers = MyApplication.mAllTowersInMap.get(tower_regis.getSysGridLineId() + "");
        //        if (towers == null || towers.size() <= 0) {
        //            towers = MyApplication.mTempTowerMap.get(tower_regis.getSysGridLineId());
        //        }
        List<Tower> towers = TowerDBHelper.getInstance().getLineTowers(tower_regis.getSysGridLineId());
        boolean isNearSmall = false;//是否在大号侧  // 这个命名 有问题
        //TODO:+size
        if (towers.size() < 2) {
            MyApplication.nearSecondDistance = 0;
            MyApplication.nearSecondTower = tower_regis;
            MyApplication.crossSecondTower = tower_regis;
        }
        for (int i = 0; i < towers.size(); i++) {
            Tower tower = towers.get(i);
            int displayOrder = tower.getDisplayOrder();
            if (order == towers.get(0).getDisplayOrder() && displayOrder == towers.get(1).getDisplayOrder()) {//第二个塔
                double towerDistance = getDistance(tower_regis, tower);
                double distance = getDistance(tower);

                if (distance > towerDistance) {
                    minDistance = currentDis;
                    secondNearTower = tower_regis;
                } else {
                    secondNearTower = tower;
                    minDistance = currentDis;
                }
                break;
            }
            if (i + 1 < towers.size() && towers.get(i + 1).getDisplayOrder() == order) {
                double towerDistance = getDistance(tower_regis, tower);
                double distance = getDistance(tower);
                //两杆塔的距离 大于 人到杆塔的距离 表示在 小号侧 如果杆塔距离小于人到杆塔的距离 说明在大号侧
                if (towerDistance < distance) {//大号侧
                    isNearSmall = true;
                } else {
                    secondNearTower = tower;
                    minDistance = distance;
                    break;
                }
            }
            if (isNearSmall) {//在大号侧
                if (i == 0) {
                    continue;
                }
                if (towers.get(i - 1).getDisplayOrder() == order || i == towers.size() - 1) {//最后一级塔
                    secondNearTower = tower;
                    minDistance = currentDis;//小号侧距离就是 最近杆塔的距离
                    break;
                }
            }
        }
        return secondNearTower;
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

    private double getDistance(Tower from, Tower to) {
        double latitude = from.getLatitude();
        double longitude = from.getLongitude();

        double toLat = to.getLatitude();
        double toLng = to.getLongitude();
        return LatLngUtils.getDistance(latitude, longitude, toLat, toLng);
    }
}
