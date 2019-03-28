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
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.FromLineTowerNumsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class FromLineTowerNumsDialog extends Dialog {

    List<Tower> mDatas;
    @BindView(R.id.start_line_name)
    TextView startLineName;
    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private FromLineTowerNumsAdapter mAdapter;
    private Unbinder unbinder;

    public FromLineTowerNumsDialog(@NonNull Context context) {
        this(context, R.style.dialogWindowAnim);
        init();
    }

    public FromLineTowerNumsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public void init() {
        setContentView(R.layout.dialog_from_line_tower_nums);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        unbinder = ButterKnife.bind(this);
        //设置adapter
        mDatas = new ArrayList<>();
        View headView = View.inflate(getContext(), R.layout.item_close_day_plan_head, null);
        mAdapter = new FromLineTowerNumsAdapter(R.layout.item_close_day_plan_recycler, mDatas);
        mAdapter.addHeaderView(headView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(mAdapter);
        recycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //监听是否选中所有
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isAll) {
                List<Tower> data = mAdapter.getData();
                if (isAll) {
                    for (Tower datum : data) {
                        datum.setChecked(true);
                    }
                } else {
                    for (Tower datum : data) {
                        datum.setChecked(false);
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
                if (listener != null) {
                    listener.onSureTowerNums(getSelectDatas());
                }
                break;
            case R.id.dlg_btn_right://取消
                dismiss();
                break;
        }
    }

    /**
     * 获取所有选中的列表
     */
    private List<Tower> getSelectDatas() {
        final List<Tower> datas = new ArrayList<>();
        Observable.from(mDatas)
                .filter(new Func1<Tower, Boolean>() {
                    @Override
                    public Boolean call(Tower towerNum) {
                        return towerNum.isChecked();
                    }
                })
                .subscribe(new Action1<Tower>() {
                    @Override
                    public void call(Tower towerNum) {
                        datas.add(towerNum);
                    }
                });
        return datas;
    }

    public void setData(List<Tower> data, String lineName) {
        this.mDatas = data;
        //将数据设置到对话框中
        startLineName.setText(lineName);
        //设置adapter
        mAdapter.setNewData(data);
    }

    private OnSureTowerNumsListener listener;

    public interface OnSureTowerNumsListener {
        void onSureTowerNums(List<Tower> datas);
    }

    public void setOnSureTowerListener(OnSureTowerNumsListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbinder.unbind();
    }
}
