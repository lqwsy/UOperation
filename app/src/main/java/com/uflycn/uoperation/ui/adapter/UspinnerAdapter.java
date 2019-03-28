package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.Tower;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */
public class UspinnerAdapter extends BaseAdapter implements SpinnerAdapter{
    private List<Tower> mDatas;
    private Reference<Context> mContextRef;
    public UspinnerAdapter(List<Tower> datas,Context context) {
        mDatas = datas;
        mContextRef = new WeakReference<Context>(context);
    }

    @Override
    public int getCount() {
        return mDatas == null ?0:mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if (position < 0){
            return null;
        }
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContextRef.get()).inflate(R.layout.spinner_text,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_spinner_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mDatas.get(position).getTowerNo());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContextRef.get()).inflate(R.layout.spinner_text,null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_spinner_text);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mDatas.get(position).getTowerNo());

        return convertView;
    }



    private static class ViewHolder{
         TextView textView;
    }
    public  void onDataChange(List<Tower> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }
}
