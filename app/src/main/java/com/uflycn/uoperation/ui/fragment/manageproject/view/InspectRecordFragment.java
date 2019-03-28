package com.uflycn.uoperation.ui.fragment.manageproject.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.EditProjectEvent;
import com.uflycn.uoperation.eventbus.InspectRecordEvent;
import com.uflycn.uoperation.eventbus.UpdateProjectRecord;
import com.uflycn.uoperation.ui.adapter.ProjectRecordAdapter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenterImpl;
import com.uflycn.uoperation.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class InspectRecordFragment extends Fragment implements ProjectView{
    @BindView(R.id.list_inspect_record)
    ListView mListView;

    private ProjectRecordAdapter mAdapter;
    private ProgressDialog mProgressDialog;
    private Reference<Context> mContextReference;
    private ProjectPresenter mProjectPresenter;
    private ProjectEntity mProjectEntity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView =  inflater.inflate(R.layout.fragment_inspect_record, container, false);
        ButterKnife.bind(this,contentView);
        mContextReference = new WeakReference<Context>(getActivity());
        mProjectPresenter = new ProjectPresenterImpl(this);
        return contentView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(BaseMainThreadEvent event){
        if(event instanceof EditProjectEvent){
            initData(((EditProjectEvent) event).getProjectEntity());
        }else if(event instanceof InspectRecordEvent){
            initData(((InspectRecordEvent) event).getRecord());
        }else if(event instanceof UpdateProjectRecord){
            if(mProgressDialog != null && mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            handleUpdateList((UpdateProjectRecord) event);
        }
    }

    private void initData(ProjectEntity projectEntity){
        mProjectEntity = projectEntity;
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(mContextReference.get());
            mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mProjectPresenter.cancel();
                }
            });
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if(!mProgressDialog.isShowing()){
            mProgressDialog.show();
        }
//        mProjectPresenter.getInspectRecord(mProjectEntity.getPlatformId()+"");
        mProjectPresenter.getInspectRecord(mProjectEntity.getSysBrokenDocumentId().intValue());
    }

    private void updateList(List<ProjectInspection> list){
        if(mAdapter == null){
            mAdapter = new ProjectRecordAdapter(list,mContextReference.get(),R.layout.item_project_inspect_record);
            mListView.setAdapter(mAdapter);
        }else {
            mAdapter.onDataChange(list);
        }
    }

    private void handleUpdateList(UpdateProjectRecord event) {
        if (event.getMessage() == null || event.getMessage().equalsIgnoreCase("")) {//更新成功
            updateList(event.getProjectEntityList());
        } else {
            ToastUtil.show(event.getMessage());
        }
    }

    @OnClick({R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(new ChangePageEvent(1));
                break;
        }
    }
}
