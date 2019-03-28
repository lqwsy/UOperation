package com.uflycn.uoperation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStep;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.ui.adapter.RouteStepAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/6/12.
 */
public class CarRouteDetail extends LinearLayout{
    private ExpandableListView mListView;
    private RouteStepAdapter mAdapter;
    public CarRouteDetail(Context context) {
        this(context,null);
    }

    public CarRouteDetail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(!isInEditMode()){
            mListView = (ExpandableListView)findViewById(R.id.expand_list_route);
            mAdapter = new RouteStepAdapter(new ArrayList<AMapNaviStep>(),getContext());
            mListView.setAdapter(mAdapter);
        }
    }

    public void setData(AMapNaviPath path, List<AMapNaviGuide> guides, String endTowerName){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View header  = inflater.inflate(R.layout.car_detail_item_start,null);
        mListView.addHeaderView(header);
        View footer = inflater.inflate(R.layout.car_detail_item_end,null);
        TextView sectName = (TextView)footer.findViewById(R.id.section_name);
        sectName.setText(endTowerName);
        mListView.addFooterView(footer);
//        if(path.getSteps().size() == guides.size())
        List<AMapNaviStep> steps = path.getSteps();
        mAdapter.onDataChange(path.getSteps(),guides);
    }


}
