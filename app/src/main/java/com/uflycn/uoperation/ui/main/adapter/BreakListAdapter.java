package com.uflycn.uoperation.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.ui.activity.AlterBreakInfoActivity;
import com.uflycn.uoperation.ui.activity.SpecialInspectActivity;
import com.uflycn.uoperation.ui.activity.SpecialRecordActivity;
import com.uflycn.uoperation.util.UIUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */
public class BreakListAdapter extends CommonAdapter<BrokenDocument> {
    public BreakListAdapter(List<BrokenDocument> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        final BrokenDocument entity = getItem(position);
        if(entity.getPlatformId() != 0){
            holder.setText(R.id.tv_special_num, "专档编号：" + entity.getDocmentNo());
        }else{
            holder.setText(R.id.tv_special_num, "专档编号：" +"");
        }
        holder.setText(R.id.tv_tower_num, "线路信息：" + entity.getVoltageClass() + " " + entity.getLineName() + " " + (entity.getStartTowerNo() + "-" + entity.getEndTowerNo()));
        holder.setText(R.id.tv_special_type, "类别：" + entity.getBrokenTypeName());
        holder.setText(R.id.tv_break_descreiption, entity.getRemark());


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_special:
                        Intent intent = new Intent(UIUtils.getContext(), SpecialInspectActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("brokenDocument", entity);
                        intent.putExtras(bundle);
                        mContextRef.get().startActivity(intent);
                        break;
                    case R.id.btn_special_record:
                        Intent recordIntent = new Intent(UIUtils.getContext(), SpecialRecordActivity.class);
                        if(entity.getPlatformId() !=0){
                            recordIntent.putExtra("DocumentId", entity.getPlatformId());
                        }else{
                            recordIntent.putExtra("DocumentId", entity.getId().intValue());
                        }
                        mContextRef.get().startActivity(recordIntent);
                        break;
                    case R.id.btn_alter:
                        Intent intent2 = new Intent(UIUtils.getContext(), AlterBreakInfoActivity.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("brokenDocument", entity);
                        intent2.putExtras(bundle2);
                        mContextRef.get().startActivity(intent2);
                        break;
                }

            }
        };
        holder.getView(R.id.btn_special).setOnClickListener(listener);
        holder.getView(R.id.btn_special_record).setOnClickListener(listener);
        holder.getView(R.id.btn_alter).setOnClickListener(listener);
    }


    public void onDataChange(List<BrokenDocument> list) {
        mDatas = list;
        notifyDataSetChanged();
    }

}
