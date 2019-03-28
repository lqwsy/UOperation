package com.uflycn.uoperation.ui.fragment.plantask.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.BaseFragment;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.ui.adapter.PlanTaskAdapter;
import com.uflycn.uoperation.ui.fragment.plantask.contract.PlanTaskContract;
import com.uflycn.uoperation.ui.fragment.plantask.presenter.PlanTaskPresenter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.ViewTool;
import com.uflycn.uoperation.widget.RecycleViewDivider;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 计划任务
 * Created by xiaoyehai on 2017/9/4.
 */
public class PlanTaskFragment extends BaseFragment<PlanTaskPresenter> implements PlanTaskContract.PlanTaskView {

    private static final int UPDATE_UI = 1;

    @BindView(R.id.et_search_line)
    EditText etSearchLine;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    Unbinder unbinder;

    private PlanTaskAdapter mPlanTaskAdapter;

    @BindView(R.id.btn_search_line)
    Button btnSearch;

    @BindView(R.id.pullLoadMoreRecyclerView)
    PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    private int mPage = 1;

    List<Gridline> mGridlines;
    private List<ItemDetail> mVoltageClass;
    private ProgressDialog mProgressDialog;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_plan_task;
    }

    @Override
    public PlanTaskPresenter getPresenter() {
        return new PlanTaskPresenter();
    }

    @Override
    protected void initView() {
        if (ProjectManageUtil.isShanDong()) {
            mTvTitle.setText("线路列表");
        }
        mGridlines = new ArrayList<>();
        mPlanTaskAdapter = new PlanTaskAdapter(this.getContext(), R.layout.item_plan_task, mGridlines);
        mPlanTaskAdapter.setTaskView(this);
        mPullLoadMoreRecyclerView.setLinearLayout();
        mPullLoadMoreRecyclerView.setAdapter(mPlanTaskAdapter);
        mPullLoadMoreRecyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 2, getResources().getColor(R.color.black_8)));
        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mGridlines.clear();
                mPlanTaskAdapter.notifyDataSetChanged();

                initData();
            }

            @Override
            public void onLoadMore() {
                mPage++;
                initData();
            }
        });
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mVoltageClass = ItemDetailDBHelper.getInstance().getItem("电压等级");
        mFlowLayout.setAdapter(new TagAdapter<ItemDetail>(mVoltageClass) {
            @Override
            public View getView(FlowLayout parent, int position, ItemDetail item) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_textview, mFlowLayout, false);
                tv.setText(item.getItemsName());
                return tv;
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //ToastUtil.show(mVoltageClass.get(position).getItemsName());

                return true;
            }
        });
        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet.isEmpty()) {
                    return;
                }
                showDialog();
                List<ItemDetail> item = new ArrayList<>();
                Iterator<Integer> selctIndex = selectPosSet.iterator();
                while (selctIndex.hasNext()) {
                    item.add(mVoltageClass.get(selctIndex.next()));
                }
                mGridlines.clear();
                mPlanTaskAdapter.notifyDataSetChanged();
                mPresenter.getPlanGridLinesByVol(item, mPage);
                //                ToastUtil.show("choose:" + selectPosSet.toString());
            }
        });
    }


    @Override
    protected void initData() {
        mPresenter.getPlanGirdlines("", mPage);
    }

    @Override
    public void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mRef.get());
            mProgressDialog.setMessage("正在加载线路，请稍等...");
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    @Override
    public void dismisDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    @Override
    public void updateGridlines(List<Gridline> gridlines) {
        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
        if (gridlines == null) {
            return;
        }
        hideKeyboard();
        mPlanTaskAdapter.addData(gridlines);

    }

    @Override
    public void startInspect(int lineId) {
        mPresenter.startInspect(lineId);
    }


    @OnClick({R.id.iv_open_close_drawer})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
                break;
        }
    }

    @OnClick({R.id.btn_search_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_line:
                if (!ViewTool.isFastDoubleClick(R.id.btn_search_line)) {
                    String lineName = etSearchLine.getText().toString();
                    mGridlines.clear();
                    mPlanTaskAdapter.notifyDataSetChanged();

                    mPresenter.getPlanGirdlines(lineName, mPage);
                }

                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPage = 1;
            mGridlines.clear();
            mPlanTaskAdapter.notifyDataSetChanged();
            initData();
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            // 隐藏键盘
            inputMethodManager.hideSoftInputFromWindow(etSearchLine.getWindowToken(), 0);
        }
    }

    @Override
    public void startInspectCallback(int lineId) {
        MyApplication.gridlineTaskStatus = 1;
        ((MainActivity) mRef.get()).mRbtnInspection.performClick();
        ((MainActivity) mRef.get()).addTowersById(lineId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
