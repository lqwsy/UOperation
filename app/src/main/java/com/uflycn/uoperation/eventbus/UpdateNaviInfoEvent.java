package com.uflycn.uoperation.eventbus;

import com.amap.api.navi.model.NaviInfo;
import com.uflycn.uoperation.widget.NaviInfoView;

/**
 * Created by Administrator on 2017/9/27.
 */
public class UpdateNaviInfoEvent extends BaseMainThreadEvent{
    private NaviInfo mNaviInfo;

    public UpdateNaviInfoEvent(NaviInfo mNaviInfo) {
        this.mNaviInfo = mNaviInfo;
    }

    public NaviInfo getNaviInfo() {
        return mNaviInfo;
    }
}
