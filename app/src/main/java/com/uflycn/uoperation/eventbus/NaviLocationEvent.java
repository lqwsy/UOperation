package com.uflycn.uoperation.eventbus;


import com.amap.api.navi.model.AMapNaviLocation;

/**
 * Created by Administrator on 2017/9/26.
 */
public class NaviLocationEvent extends BaseMainThreadEvent{
    private AMapNaviLocation mLocation;

    public NaviLocationEvent(AMapNaviLocation mLocation) {
        this.mLocation = mLocation;
    }

    public AMapNaviLocation getLocation() {
        return mLocation;
    }
}
