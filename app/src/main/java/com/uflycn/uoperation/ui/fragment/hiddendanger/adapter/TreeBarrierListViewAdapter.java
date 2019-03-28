package com.uflycn.uoperation.ui.fragment.hiddendanger.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.bean.TreeBarrierBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;

import java.util.List;

/**
 * 树障管理列表适配器
 */
public class TreeBarrierListViewAdapter extends BaseAdapter {

    private MyListViewOnItemClickListener myListViewOnItemClickListener;
    private List<TreeDefectPointBean> mDatas;
    private Context context;

    public TreeBarrierListViewAdapter(Context context, List<TreeDefectPointBean> data, MyListViewOnItemClickListener myListViewOnItemClickListener) {
        this.mDatas = data;
        this.context = context;
        this.myListViewOnItemClickListener = myListViewOnItemClickListener;
    }

    public void onDataChange(List<TreeDefectPointBean> mDatas){
        this.mDatas = mDatas;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        TreeDefectPointBean treeDefectPointBean = mDatas.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_hdm_list, parent, false);
            myViewHolder = new MyViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        myViewHolder.lineName.setText(treeDefectPointBean.getLineName());
        myViewHolder.lineDesc.setText(treeDefectPointBean.getRemark());
        myViewHolder.left.setText("更新");
        myViewHolder.right.setText("清除树障");
        myViewHolder.left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myListViewOnItemClickListener.onLeftClick(position);
            }
        });
        myViewHolder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListViewOnItemClickListener.onRightClick(position);
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
        void onLeftClick(int position);
        void onRightClick(int position);
    }
}
