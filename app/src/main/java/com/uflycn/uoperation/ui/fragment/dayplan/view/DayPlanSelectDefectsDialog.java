package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.DayPlanSelectDefectsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class DayPlanSelectDefectsDialog extends Dialog {
    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.dlg_btn_left)
    Button dlgBtnLeft;
    @BindView(R.id.dlg_btn_right)
    Button dlgBtnRight;
    @BindView(R.id.dlg_linear_main)
    LinearLayout dlgLinearMain;
    @BindView(R.id.dlg_container)
    LinearLayout dlgContainer;
    private List<Object> mDatas;
    private DayPlanSelectDefectsAdapter mAdapter;

    public DayPlanSelectDefectsDialog(@NonNull Context context) {
        this(context, R.style.dialogWindowAnim);
        init();
    }

    public DayPlanSelectDefectsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public void init() {
        setContentView(R.layout.dialog_day_plan_select_defects);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        //设置adapter
        mDatas = new ArrayList<>();
        mAdapter = new DayPlanSelectDefectsAdapter(R.layout.item_select_defects_day_plan_recycler, mDatas);
        View headView = View.inflate(getContext(), R.layout.item_select_defects_day_plan_head, null);
        mAdapter.addHeaderView(headView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        //监听是否选中所有
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isAll) {
                List<Object> data = mAdapter.getData();
                if (isAll) {
                    for (Object datum : data) {
                        if (datum instanceof DefectBean) {
                            ((DefectBean) datum).setChecked(true);
                        } else if (datum instanceof TreeDefectPointBean) {
                            ((TreeDefectPointBean) datum).setChecked(true);
                        }
                    }
                } else {
                    for (Object datum : data) {
                        if (datum instanceof DefectBean) {
                            ((DefectBean) datum).setChecked(true);
                        } else if (datum instanceof TreeDefectPointBean) {
                            ((TreeDefectPointBean) datum).setChecked(true);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.dlg_btn_left, R.id.dlg_btn_right})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.dlg_btn_left://关闭巡视
                if (mListener != null) {
                    mListener.selectDefects(getSelectData());
                }
                break;
            case R.id.dlg_btn_right://取消
                dismiss();
                break;
        }
    }

    private List<Object> getSelectData() {
        final List<Object> list = new ArrayList<>();
        Observable.from(getData())
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object defectBean) {
                        if (defectBean instanceof DefectBean) {
                            return ((DefectBean) defectBean).isChecked();
                        } else {
                            return ((TreeDefectPointBean) defectBean).isChecked();
                        }

                    }
                }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object defectBean) {
                list.add(defectBean);
            }
        });
        return list;
    }

    public void setData(List<Object> data) {
//        mDatas.addAll(data);
//        将数据设置到对话框中
//        mAdapter.notifyDataSetChanged();
        mAdapter.setNewData(data);
    }

    public List<Object> getData() {
        return mDatas;
    }

    private OnSelectDefectsListener mListener;

    public interface OnSelectDefectsListener {
        void selectDefects(List<Object> datas);
    }

    public void setOnSelectDefectsListener(OnSelectDefectsListener listener) {
        this.mListener = listener;
    }
}
