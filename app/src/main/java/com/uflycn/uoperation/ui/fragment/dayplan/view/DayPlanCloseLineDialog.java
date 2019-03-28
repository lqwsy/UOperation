package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.DayPlanCloseLineAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DayPlanCloseLineDialog extends Dialog {
    @BindView(R.id.start_tour_time)
    TextView startTourTime;
    @BindView(R.id.start_line_name)
    TextView startLineName;
    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private onCloseLineListener mListener;
    private SelectTower data;
    private DayPlanCloseLineAdapter mAdapter;
    private List<SelectTower.TowerListBean> mDatas;

    public DayPlanCloseLineDialog(@NonNull Context context) {
        this(context, R.style.dialogWindowAnim);
        init();
    }

    public DayPlanCloseLineDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public void init() {
        setContentView(R.layout.dialog_close_day_plan_line);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        //设置adapter
        mDatas = new ArrayList<>();
        mAdapter = new DayPlanCloseLineAdapter(R.layout.item_close_day_plan_recycler, mDatas);
        View headView = View.inflate(getContext(), R.layout.item_close_day_plan_head, null);
        mAdapter.addHeaderView(headView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recycler.setAdapter(mAdapter);
        //监听是否选中所有
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isAll) {
                List<SelectTower.TowerListBean> data = mAdapter.getData();
                if (isAll) {
                    for (SelectTower.TowerListBean datum : data) {
                        datum.setCheck(true);
                    }
                } else {
                    for (SelectTower.TowerListBean datum : data) {
                        datum.setCheck(false);
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
                mListener.onClose(mAdapter.getData());
                break;
            case R.id.dlg_btn_right://取消
                dismiss();
                break;
        }
    }

    public void setData(String tourData, SelectTower data) {
        this.data = data;
        //将数据设置到对话框中
        startTourTime.setText(tourData);
        startLineName.setText(data.getVoltageClass() + "  " + data.getLineName());
        //设置adapter
        mAdapter.setNewData(data.getTowerList());
    }

    public SelectTower getData() {
        return data;
    }

    public interface onCloseLineListener {
        void onClose(List<SelectTower.TowerListBean> datas);
    }

    public void setListener(onCloseLineListener listener) {
        mListener = listener;
    }
}
