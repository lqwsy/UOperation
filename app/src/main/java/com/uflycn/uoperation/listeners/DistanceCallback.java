package com.uflycn.uoperation.listeners;

import com.uflycn.uoperation.bean.Tower;

/**
 * Created by Administrator on 2017/9/12.
 */
public interface DistanceCallback {
    void onNearestTowerCallBack(Tower tower,double distance);//最近的杆塔 和最近的距离
    void updateGpsLocate();
}
