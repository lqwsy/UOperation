package com.uflycn.uoperation.widget;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.navi.model.AMapNaviPath;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.eventbus.ClearCarRouteEvent;
import com.uflycn.uoperation.eventbus.SelectNaviPathEvent;
import com.uflycn.uoperation.eventbus.StartNaviEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by user on 2017/6/12.
 */
public class CarRoutePanel extends LinearLayout implements View.OnClickListener {
    private RouteCarTagMapView routeLeft;
    private RouteCarTagMapView routeRight;
    private RouteCarTagMapView routeCenter;

    private List<AMapNaviPath> mPaths;
    private TextView mCost;
    private TextView mCameras;
    private AMapNaviPath mSelectPath;
    private RelativeLayout mStartNavi;
    private RelativeLayout mCloseNavi;
    private RelativeLayout mClearRouteResult;
    public CarRoutePanel(Context context) {
        this(context, null);
    }

    public CarRoutePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            routeLeft = (RouteCarTagMapView) findViewById(R.id.car_tag_left);
            routeLeft.setOnClickListener(this);
            routeRight = (RouteCarTagMapView) findViewById(R.id.car_tag_right);
            routeRight.setOnClickListener(this);
            routeCenter = (RouteCarTagMapView) findViewById(R.id.car_tag_center);
            routeCenter.setOnClickListener(this);
            mCameras = (TextView) findViewById(R.id.group_des);
            mCost = (TextView) findViewById(R.id.car_footer_sub_des);
            mStartNavi = (RelativeLayout)findViewById(R.id.layout_button);
            mCloseNavi = (RelativeLayout)findViewById(R.id.layout_button_out);
            mStartNavi.setOnClickListener(this);
            mCloseNavi.setOnClickListener(this);
            mClearRouteResult = (RelativeLayout) findViewById(R.id.rl_clear_car_route);
            mClearRouteResult.setOnClickListener(this);
        }
    }

    public void setData(List<AMapNaviPath> paths) {
        mPaths = paths;
        if (paths.size() == 1) {
            routeLeft.setVisibility(VISIBLE);
            routeRight.setVisibility(GONE);
            routeCenter.setVisibility(GONE);
            routeLeft.setData(paths.get(0));
        } else if (paths.size() == 2) {
            routeLeft.setVisibility(VISIBLE);
            routeRight.setVisibility(GONE);
            routeCenter.setVisibility(VISIBLE);
            routeLeft.setData(paths.get(0));
            routeCenter.setData(paths.get(1));
        } else {
            routeLeft.setVisibility(VISIBLE);
            routeRight.setVisibility(VISIBLE);
            routeCenter.setVisibility(VISIBLE);
            routeLeft.setData(paths.get(0));
            routeCenter.setData(paths.get(1));
            routeRight.setData(paths.get(2));
        }
        selectRoute(0);
    }

    public void selectRoute(int index) {
        if (mPaths == null) return;
        if (index == 0) {
            routeLeft.setChecked(true);
            routeRight.setChecked(false);
            routeCenter.setChecked(false);
            mCameras.setText(mPaths.get(0).getAllCameras().size() + "个");
            mCost.setText(mPaths.get(0).getTollCost() + "元");
        } else if (index == 1) {
            routeLeft.setChecked(false);
            routeRight.setChecked(false);
            routeCenter.setChecked(true);
            mCameras.setText(mPaths.get(1).getAllCameras().size() + "个");
            mCost.setText(mPaths.get(1).getTollCost() + "元");
        } else {
            routeLeft.setChecked(false);
            routeRight.setChecked(true);
            routeCenter.setChecked(false);
            mCameras.setText(mPaths.get(2).getAllCameras().size() + "个");
            mCost.setText(mPaths.get(2).getTollCost() + "元");
        }
        EventBus.getDefault().post(new SelectNaviPathEvent(index));
        mSelectPath = mPaths.get(index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_tag_left:
                selectRoute(0);
                break;
            case R.id.car_tag_right:
                selectRoute(2);
                break;
            case R.id.car_tag_center:
                selectRoute(1);
                break;
            case R.id.layout_button:
                EventBus.getDefault().post(new StartNaviEvent());
                break;
            case R.id.layout_button_out:
                EventBus.getDefault().post(new ClearCarRouteEvent());
                break;
            case R.id.rl_clear_car_route:
                SimpleDlg.Builder builder = new SimpleDlg.Builder();
                final SimpleDlg dlg = builder.create(getContext());
                builder.setContentText("确定清除导航路线吗?");
                builder.setOnclickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.dlg_btn_left:
                                dlg.dismiss();
                            case R.id.dlg_btn_right:
                                EventBus.getDefault().post(new ClearCarRouteEvent());
                                dlg.dismiss();
                                break;
                        }
                    }
                });
                dlg.show();
                break;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onEventMainThread(Object event) {

    }

    public AMapNaviPath getSelectedPath(){
        return mSelectPath;
    }

}
