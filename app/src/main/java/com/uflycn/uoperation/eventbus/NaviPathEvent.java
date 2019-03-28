package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/9/26.
 */
public class NaviPathEvent extends BaseMainThreadEvent{
    private double latitude;
    private double longitude;

    public NaviPathEvent(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
