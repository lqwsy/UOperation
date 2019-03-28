package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviStep;
import com.uflycn.uoperation.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/6/12.
 */
public class RouteStepAdapter extends BaseExpandableListAdapter {
    private List<AMapNaviStep> mSteps;
    private List<AMapNaviGuide> mGuides = new ArrayList<>();
    private Context mContext;
    public RouteStepAdapter(List<AMapNaviStep> setps, Context context) {
        mSteps = setps;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return mGuides.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGuides.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mSteps.get(groupPosition).getLinks().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if(convertView == null){
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.car_detail_item_group,null);
            holder.setctionName = (TextView)convertView.findViewById(R.id.section_name);
            holder.groupeDes = (TextView)convertView.findViewById(R.id.group_des);
            holder.actionIcon = (ImageView)convertView.findViewById(R.id.group_icon);
            convertView.setTag(holder);
        }else{
            holder = (GroupViewHolder)convertView.getTag();
        }
        AMapNaviGuide guide = mGuides.get(groupPosition);
        AMapNaviStep step  = mSteps.get(groupPosition);
        holder.setctionName.setText(guide.getName());
        holder.groupeDes.setText(guide.getLength()+"米");
//        holder.actionIcon.setImageResource(guide.getIconType());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    /*    SubViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.car_detail_item_group_subitem,null);
            holder = new SubViewHolder();
            holder.icon = (ImageView)convertView.findViewById(R.id.action_icon);
            holder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }else{
            holder = (SubViewHolder) convertView.getTag();
        }
        AMapNaviLink link = mSteps.get(groupPosition).getLinks().get(childPosition);
        holder.name.setText("在"+link.getRoadName()+"行驶"+link.getLength()+"米");*/
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void onDataChange(List<AMapNaviStep> steps, List<AMapNaviGuide> guides){
        mSteps = steps;
        mGuides = guides;
        notifyDataSetChanged();
    }

    private class  GroupViewHolder{
         TextView setctionName;
         TextView groupeDes;
        ImageView actionIcon;
    }
    private class SubViewHolder{
        TextView name;
        ImageView icon;
    }
}
