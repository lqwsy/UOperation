package com.uflycn.uoperation.ui.fragment.dayplan.adapter;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.LineBean;
import com.uflycn.uoperation.ui.fragment.dayplan.view.AddLineActivity;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/26
 * Describe  :
 * ��·�б����
 */
public class AddLineAdapter extends BaseQuickAdapter<LineBean> {

    public AddLineActivity.CheckBoxChangeListener checkBoxChangeListener;

    public AddLineAdapter(int layoutResId, List<LineBean> data, AddLineActivity.CheckBoxChangeListener checkBoxChangeListener) {
        super(layoutResId, data);
        this.checkBoxChangeListener = checkBoxChangeListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final LineBean lineBean) {
        baseViewHolder.setText(R.id.tv_voltage, lineBean.getVoltageClass())
                .setText(R.id.tv_line_name, lineBean.getLineName())
                .setText(R.id.tv_danger_defects, lineBean.getDangerDefectCount() == null ? 0 + "" : lineBean.getDangerDefectCount())
                .setText(R.id.tv_serious_defects, lineBean.getSeriousDefectCount() == null ? 0 + "" : lineBean.getSeriousDefectCount())
                .setText(R.id.tv_general_defects, lineBean.getNormalDefectCount() == null ? 0 + "" : lineBean.getNormalDefectCount());
        final CheckBox checkBox = baseViewHolder.getView(R.id.cb_select);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(lineBean.isSelect());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
//                if (isCheck) {
//                    lineBean.setSelect(isCheck);
//                    if (listener != null) {
//                        listener.selectLine(lineBean);
//                    }
//                }
                lineBean.setSelect(isCheck);
                checkBoxChangeListener.onSelectChanged(lineBean);
            }
        });
        baseViewHolder.itemView.setTag(lineBean);
    }


    public ArrayList<LineBean> getSelectLine() {
        final ArrayList<LineBean> lineBeans = new ArrayList<>();
        Observable.from(getData())
                .filter(new Func1<LineBean, Boolean>() {
                    @Override
                    public Boolean call(LineBean lineBean) {
                        return lineBean.isSelect() == true;
                    }
                }).subscribe(new Action1<LineBean>() {
            @Override
            public void call(LineBean lineBean) {
                lineBeans.add(lineBean);
            }
        });
        return lineBeans;
    }

    private AddLineAdapterListener listener;

    public interface AddLineAdapterListener {

        void selectLine(LineBean lineBean);
    }

    public void setAddLineListener(AddLineAdapterListener listener) {
        this.listener = listener;
    }
}
