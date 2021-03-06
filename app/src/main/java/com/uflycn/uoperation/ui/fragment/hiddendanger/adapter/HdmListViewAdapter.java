package com.uflycn.uoperation.ui.fragment.hiddendanger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.HiddenDangerBean;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;

import java.util.List;

/**
 * 隐患管理列表适配器
 */
public class HdmListViewAdapter extends BaseAdapter {

    private MyListViewOnItemClickListener myListViewOnItemClickListener;
    private List<HiddenDangerBean> mDatas;
    private Context context;

    public HdmListViewAdapter(Context context, int i, List<HiddenDangerBean> data, MyListViewOnItemClickListener myListViewOnItemClickListener) {
        this.mDatas = data;
        this.context = context;
        this.myListViewOnItemClickListener = myListViewOnItemClickListener;
    }

    public void onDateChange(List<HiddenDangerBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        HiddenDangerBean testBean = mDatas.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hdm_list, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        myViewHolder.lineName.setText(testBean.getLineName());
        myViewHolder.lineDesc.setText(testBean.getDescript());
        myViewHolder.left.setText("特巡");
        myViewHolder.right.setText("特巡记录");
        myViewHolder.left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myListViewOnItemClickListener.onLeftClick();
            }
        });
        myViewHolder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListViewOnItemClickListener.onRightClick();
            }
        });
        return convertView;
    }

    private class MyViewHolder {
        TextView lineName, lineDesc;
        Button left, right;

        public MyViewHolder(View view) {
            lineName = (TextView) view.findViewById(R.id.tv_hdm_item_line_name);
            lineDesc = (TextView) view.findViewById(R.id.tv_hdm_item_description);
            left = (Button) view.findViewById(R.id.btn_hdm_sc);
            right = (Button) view.findViewById(R.id.btn_hdm_sc_record);
        }
    }

    public interface MyListViewOnItemClickListener {
        void onLeftClick();
        void onRightClick();
    }
}
