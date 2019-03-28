package com.uflycn.uoperation.eventbus;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapNaviPath;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/26.
 */
public class NaviRoutesEvent  extends BaseMainThreadEvent{
    private AMapNavi mAMapNavi;
    private int[] routeIds;
    Map<Integer, AMapNaviPath> paths;

    public AMapNavi getAMapNavi() {
        return mAMapNavi;
    }

    public NaviRoutesEvent(int[] routeIds, Map<Integer, AMapNaviPath> path, AMapNavi aMapNavi) {
        this.routeIds = routeIds;
        paths = path;
        mAMapNavi = aMapNavi;

    }

    public int[] getRouteIds() {
        return routeIds;
    }
    public Map<Integer, AMapNaviPath> getPaths() {
        return paths;
    }
}
