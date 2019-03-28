package com.uflycn.uoperation.ui.fragment.cleardefectlist.presenter;

import com.uflycn.uoperation.bean.WorksheetApanageTask;
import com.uflycn.uoperation.ui.fragment.cleardefectlist.ClearDefectListListener;
import com.uflycn.uoperation.ui.fragment.cleardefectlist.model.ClearDefectListModelImpl;
import com.uflycn.uoperation.ui.fragment.cleardefectlist.view.ClearDefectListFragment;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */
public class ClearDefectListPresentImpl implements ClearDefectListPresent{

    private ClearDefectListFragment mClearDefectListFragment;
    private ClearDefectListModelImpl mClearDefectListModelImpl;

    public ClearDefectListPresentImpl(ClearDefectListFragment mClearDefectListFragment){
        this.mClearDefectListFragment = mClearDefectListFragment;
        mClearDefectListModelImpl = new ClearDefectListModelImpl();
    }

    @Override
    public void getClearDefectList(String num) {
        mClearDefectListFragment.showDialog();
        mClearDefectListModelImpl.getClearDefectList(num, new ClearDefectListListener() {
            @Override
            public void onSeccess(List<WorksheetApanageTask> clearDefLists) {
                mClearDefectListFragment.refeshListData(clearDefLists);
                mClearDefectListFragment.missDialog();
            }

            @Override
            public void onFailed(String message) {
                mClearDefectListFragment.missDialog();
                ToastUtil.show(message);
            }
        });
    }
}
