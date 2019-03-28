package com.uflycn.uoperation.ui.main.adapter;

import android.content.Context;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;

import java.util.List;

/**
 * Created by UF_PC on 2017/9/4.
 */
public class DrawerLayoutAdapter extends CommonAdapter<String> {

    public DrawerLayoutAdapter(List<String> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        String s = mDatas.get(position);

        holder.setText(R.id.tv_title, s);

    }
}
