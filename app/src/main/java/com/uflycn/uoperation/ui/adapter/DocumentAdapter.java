package com.uflycn.uoperation.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.Document;
import com.uflycn.uoperation.util.FileUtils;

import java.util.List;

/**
 * Created by UF_PC on 2017/11/8.
 */
public class DocumentAdapter extends CommonAdapter<Document> {


    public DocumentAdapter(int layoutResId, Context context, List<Document> data) {
        super(data,context,layoutResId);
    }

    @Override
    public void fillData(int position, CommomViewHolder holder) {
        Document document = getItem(position);

        holder.setText(R.id.tv_document_title, document.getFileName());
        if (document.getExtensions().equals(".xlsx") || document.getExtensions().equals(".xls")) {
            holder.setImageResource(R.id.img_document_icon, R.drawable.icon_xlsx);
        } else if (document.getExtensions().equals(".docx") || document.getExtensions().equals(".doc")) {
            holder.setImageResource(R.id.img_document_icon,R.drawable.icon_doc);
        } else if (document.getExtensions().equals(".pptx") || document.getExtensions().equals(".ppt")) {
            holder.setImageResource(R.id.img_document_icon, R.drawable.icon_ppt);
        } else if (document.getExtensions().equals(".png") || document.getExtensions().equals(".jpeg")|| document.getExtensions().equals(".jpg")) {
            holder.setImageResource(R.id.img_document_icon, R.drawable.icon_png);
        } else if (document.getExtensions().equals(".txt")) {
            holder.setImageResource(R.id.img_document_icon, R.drawable.icon_txt);
        } else if (document.getExtensions().equals(".pdf")) {
            holder.setImageResource(R.id.img_document_icon, R.drawable.icon_pdf);
        } else {
            holder.setImageResource(R.id.img_document_icon, R.drawable.ic_nofile);
        }

        String date = document.getCreatedTime();
        if(date.indexOf("T") > -1) {
            holder.setText(R.id.tv_document_time,date.replace("T","  "));
        }else{
            holder.setText(R.id.tv_document_time, date);
        }
    }
}
