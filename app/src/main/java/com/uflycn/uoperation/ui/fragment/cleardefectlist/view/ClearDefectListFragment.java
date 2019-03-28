package com.uflycn.uoperation.ui.fragment.cleardefectlist.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.WorksheetApanageTask;
import com.uflycn.uoperation.eventbus.RefreshListEvenBus;
import com.uflycn.uoperation.ui.adapter.ClearDefListAdapter;
import com.uflycn.uoperation.ui.fragment.cleardefectlist.presenter.ClearDefectListPresentImpl;
import com.uflycn.uoperation.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/5.
 */
public class ClearDefectListFragment  extends Fragment implements ClearDefectListView{
    private Reference<Activity> mRef;
    private ProgressDialog mProgressDialog;
    private List<WorksheetApanageTask> mClearDefLists;
    private ClearDefListAdapter mClearDefListAdapter;
    private ClearDefectListPresentImpl mClearDefectListPresentImpl;

    @BindView(R.id.lv_new_clear_def_list)
    ListView lv_document;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cleardefectlist, container, false);
        ButterKnife.bind(this, view);
        mRef = new WeakReference<Activity>(getActivity());
        mClearDefectListPresentImpl = new ClearDefectListPresentImpl(this);

        initView();
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mClearDefLists = new ArrayList<>();
        mClearDefListAdapter = new ClearDefListAdapter(mClearDefLists, mRef.get(), R.layout.item_clear_def_list);
        lv_document.setAdapter(mClearDefListAdapter);
    }

    private  void initData() {
        mClearDefectListPresentImpl.getClearDefectList("");
    }


    @OnClick({R.id.iv_back})
    public void sumbit(View view) {
        //TODO:
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void showDialog() {
        mProgressDialog = new ProgressDialog(mRef.get());
        mProgressDialog.setMessage("正在加载数据，请稍等...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void missDialog() {
        if (mProgressDialog!=null){
            if (mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshListEvenBus event){
        initData();
    }

    @Override
    public void refeshListData(List<WorksheetApanageTask> clearDefLists) {
        if (mClearDefListAdapter == null){
            mClearDefListAdapter = new ClearDefListAdapter(clearDefLists, mRef.get(), R.layout.item_clear_def_list);
            lv_document.setAdapter(mClearDefListAdapter);

        }else{
            mClearDefListAdapter.onDataChange(clearDefLists);
            mClearDefListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
