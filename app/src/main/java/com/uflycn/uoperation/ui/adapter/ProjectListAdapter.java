package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */
public class ProjectListAdapter extends CommonAdapter<ProjectEntity> {

    public ProjectListAdapter(List<ProjectEntity> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        ProjectEntity entity = getItem(position);
        //        holder.setText(R.id.tv_line_details,entity.getVoltageClass()+" "+entity.getGridLineName()+" "+entity.getTowerSection());
        if (entity.getVClass() != null) {
            holder.setText(R.id.tv_line_details, entity.getVClass() + " " + entity.getGridLineName());
        } else {
            holder.setText(R.id.tv_line_details, entity.getVoltageClass() + " " + entity.getGridLineName());
        }

        holder.setText(R.id.tv_project_name, entity.getProjectName());
        holder.setText(R.id.tv_project_description, entity.getProjectDescription());

        //        * "StartTowerId": 9713,
        //        * "EndTowerId": 9789,
        if (StringUtils.isEmptyOrNull(entity.getStartTowerNo()) || StringUtils.isEmptyOrNull(entity.getEndTowerNo())) {
            holder.setText(R.id.tv_start_end_point, entity.getTowerSection() + "");
        } else {
            holder.setText(R.id.tv_start_end_point, "#" + entity.getStartTowerNo() + "-#" + entity.getEndTowerNo());
        }
    }
}
