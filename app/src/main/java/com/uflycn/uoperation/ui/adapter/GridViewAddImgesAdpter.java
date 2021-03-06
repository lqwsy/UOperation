package com.uflycn.uoperation.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.uflycn.uoperation.R;
import com.zzti.fengyongge.imagepicker.PhotoPreviewActivity;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * com.bm.falvzixun.adapter.GridViewAddImgAdpter
 *
 * @author yuandl on 2015/12/24.
 * 添加上传图片适配器
 */
public class GridViewAddImgesAdpter extends BaseAdapter {
    private List<Map<String, Object>> datas;
    private Context context;
    private LayoutInflater inflater;
    private onRemovePhotoListener mListener;
    /**
     * 可以动态设置最多上传几张，之后就不显示+号了，用户也无法上传了
     * 默认9张
     */
    private int maxImages = 9;

    public GridViewAddImgesAdpter(List<Map<String, Object>> datas, Context context) {
        this.datas = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setListener(onRemovePhotoListener listener) {
        mListener = listener;
    }

    /**
     * 获取最大上传张数
     *
     * @return
     */
    public int getMaxImages() {
        return maxImages;
    }

    /**
     * 设置最大上传张数
     *
     * @param maxImages
     */
    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }


    /**
     * 让GridView中的数据数目加1最后一个显示+号
     *
     * @return 返回GridView中的数量
     */
    @Override
    public int getCount() {
        int count = datas == null ? 1 : datas.size() + 1;
        if (count > maxImages) {
            return datas.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<Map<String, Object>> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (datas != null && position < datas.size()) {
            //viewHolder.img_addimage.setVisibility(View.GONE);
            final File file = new File(datas.get(position).get("path").toString());
            Glide.with(context)
                    .load(file)
                    .priority(Priority.HIGH)
                    .into(viewHolder.ivimage);
            viewHolder.img_addimage.setVisibility(View.GONE);
            viewHolder.ivimage.setVisibility(View.VISIBLE);
            viewHolder.btdel.setVisibility(View.VISIBLE);
            viewHolder.btdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (file.exists()) {
                        file.delete();
                    }
                    datas.remove(position);
                    notifyDataSetChanged();
                    if (mListener!=null){
                        mListener.onRemovePhoto();
                    }
                }
            });
            viewHolder.ivimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (datas != null && position < datas.size()) {
                        List<PhotoModel> single_photos = new ArrayList<PhotoModel>();
                        for (Map<String, Object> data : datas) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath((String) data.get("path"));
                            photoModel.setChecked(false);
                            single_photos.add(photoModel);
                        }
                        //PhotoModel 开发者将自己本地bean的list封装成PhotoModel的list，PhotoModel属性源码可查看
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos", (Serializable) single_photos);
                        bundle.putInt("position", position);//position预览图片地址
                        bundle.putBoolean("isSave", false);//isSave表示是否可以保存预览图片，建议只有预览网络图片时设置true
                        CommonUtils.launchActivity(context, PhotoPreviewActivity.class, bundle);
                    } else {
                        return;
                    }
                }
            });
        } else {
            Glide.with(context)
                    .load(R.drawable.btn_photo_add)
                    .priority(Priority.HIGH)
                    .centerCrop()
                    .into(viewHolder.img_addimage);
            viewHolder.img_addimage.setScaleType(ImageView.ScaleType.FIT_XY);
            viewHolder.img_addimage.setVisibility(View.VISIBLE);
            viewHolder.btdel.setVisibility(View.GONE);
            viewHolder.ivimage.setVisibility(View.GONE);
            //
        }

        return convertView;

    }

    public class ViewHolder {
        public final ImageView ivimage;
        public final Button btdel;
        public final View root;
        public final ImageView img_addimage;

        public ViewHolder(View root) {
            img_addimage = (ImageView) root.findViewById(R.id.iv_addImage);

            ivimage = (ImageView) root.findViewById(R.id.iv_image);
            btdel = (Button) root.findViewById(R.id.bt_del);
            this.root = root;
        }
    }

    public interface onRemovePhotoListener {
        void onRemovePhoto();
    }

}
