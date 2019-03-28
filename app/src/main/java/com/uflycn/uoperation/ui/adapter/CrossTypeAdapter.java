package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.DefectType;

import java.util.List;

/**
 * Created by Administrator on 2017/9/27.
 */
public class CrossTypeAdapter extends CommonAdapter<DefectType>{
    public CrossTypeAdapter(List<DefectType> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        holder.setText(R.id.tv_broken_type,mDatas.get(position).getDefectName());
    }
}
