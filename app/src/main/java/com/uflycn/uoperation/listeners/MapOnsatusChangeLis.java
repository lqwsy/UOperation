package com.uflycn.uoperation.listeners;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.uflycn.uoperation.constant.AppConstant;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.services.GaodeLocationService;
import com.xflyer.utils.CoordinateUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/9/4.
 */
public class MapOnsatusChangeLis implements OnStatusChangedListener{
    private Reference<MapView> mRef;
    private long mLastLoadLayerTime;//上次加载 layer的时间
    private boolean isFirstLoad = true;
    public MapOnsatusChangeLis(MapView mapView) {
        mRef = new WeakReference<MapView>(mapView);
    }

    @Override
    public void onStatusChanged(Object o, STATUS status) {
        {
            if (status.equals(STATUS.LAYER_LOADED)) {
                long now = System.currentTimeMillis();
                if (now - mLastLoadLayerTime < 2000) {
                    return;
                }
                mLastLoadLayerTime = now;
                if (isFirstLoad) {//只在第一次的时候自动定位
                    isFirstLoad = false;
                    LatLngInfo moveTo = null;
                    if (GaodeLocationService.CurrentLocation != null) {
                        moveTo = CoordinateUtils.lonLatToMercator(GaodeLocationService.CurrentLocation.longitude, GaodeLocationService.CurrentLocation.latitude);
                    } else {
                        moveTo = CoordinateUtils.lonLatToMercator(AppConstant.BING_INIT_LON, AppConstant.BING_INIT_LAT);
                    }
                    mRef.get().centerAt(new Point(moveTo.longitude, moveTo.latitude), true);
                    mRef.get().setScale(AppConstant.BING_INIT_SCALE);
                }
            }
        }
    }
}
