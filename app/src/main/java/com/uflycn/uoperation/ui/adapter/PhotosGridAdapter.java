package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.uflycn.uoperation.R;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public class PhotosGridAdapter extends BaseAdapter{
    private List<String> datas;
    private Context context;
    private LayoutInflater inflater;
    private String photoPath;

    public PhotosGridAdapter(List<String> datas,String photoPath ,Context context) {
        this.datas = datas;
        this.context = context;
        this.photoPath = photoPath;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_gridview_photo, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivimage.setImageBitmap(BitmapFactory.decodeFile(photoPath+ File.separator +datas.get(position)));
        return convertView;
    }

    public class ViewHolder {
        public final ImageView ivimage;
        public final View root;
        public ViewHolder(View root) {
            ivimage = (ImageView) root.findViewById(R.id.img_photo);
            this.root = root;
        }
    }
}
