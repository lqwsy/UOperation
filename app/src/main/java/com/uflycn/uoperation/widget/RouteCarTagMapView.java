package com.uflycn.uoperation.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.navi.model.AMapNaviPath;
import com.uflycn.uoperation.R;


/**
 * Created by user on 2017/6/9.
 */
public class RouteCarTagMapView extends LinearLayout {
    private View mBottomLine;
    private RelativeLayout mContainer;
    private TextView mLableView;
    private TextView mLengthDes;
    private TextView mTimeDes;

    public RouteCarTagMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.route_car_result_tag_map_portmode, (ViewGroup) this, true);
        this.mTimeDes = (TextView) view.findViewById(R.id.tv_timedes);
        this.mLengthDes = (TextView) view.findViewById(R.id.tv_lengthdes);
        this.mLableView = (TextView) view.findViewById(R.id.tv_label);
        this.mBottomLine = view.findViewById(R.id.bottom_line);
        this.mContainer = (RelativeLayout) view.findViewById(R.id.tab_ll);
        this.setChecked(false);
    }

    public void setChecked(boolean mIsChecked) {
        if (mIsChecked) {
            this.mLableView.setTextColor(this.getResources().getColorStateList(R.color.white_1));
            this.mLableView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.route_car_result_tab_select_inner));
            this.mTimeDes.setTextColor(this.getResources().getColorStateList(R.color.chart_blue));
            this.mLengthDes.setTextColor(this.getResources().getColorStateList(R.color.chart_blue));
            this.mContainer.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.route_car_result_tab_select_outer));
        } else {
            this.mLableView.setTextColor(this.getResources().getColorStateList(R.color.dark_gray));
            this.mLableView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.route_car_result_tab_normal_inner));
            this.mTimeDes.setTextColor(this.getResources().getColorStateList(R.color.dark_gray));
            this.mLengthDes.setTextColor(this.getResources().getColorStateList(R.color.dark_gray));
            this.mContainer.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.route_car_result_tab_norlmal_outer));
        }
    }

    public void setData(AMapNaviPath mNavigationPath) {
        if (mNavigationPath != null) {
            this.mLengthDes.setText(getDistance(mNavigationPath.getAllLength()));
            this.mLengthDes.setVisibility(VISIBLE);
            if (TextUtils.isEmpty(mNavigationPath.getLabels())) {
                this.mLableView.setVisibility(GONE);
            } else {
                this.mLableView.setText(mNavigationPath.getLabels());
                this.mLableView.setVisibility(VISIBLE);
            }
            this.mBottomLine.setVisibility(GONE);
            this.mTimeDes.setText(mNavigationPath.getAllTime() / 60 + "分钟");
            this.mTimeDes.setTextSize(1, 20.0f);
            this.requestLayout();
        }
    }

    private  String getDistance(int dis){
        if(dis<1000){
            return dis+"米";
        }else{
            return ((double)dis)/1000+"公里";
        }
    }
}
