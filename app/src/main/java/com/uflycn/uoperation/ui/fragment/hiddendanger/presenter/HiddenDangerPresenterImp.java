package com.uflycn.uoperation.ui.fragment.hiddendanger.presenter;

import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.TreeBarrierBean;
import com.uflycn.uoperation.ui.fragment.hiddendanger.HiddenDangerListener;
import com.uflycn.uoperation.ui.fragment.hiddendanger.model.HiddenDangerModelImp;
import com.uflycn.uoperation.ui.fragment.hiddendanger.view.HiddenDangerView;

import java.util.List;

import okhttp3.MultipartBody;

/**
 * 隐患管理业务逻辑类
 */
public class HiddenDangerPresenterImp implements HiddenDangerPresenter {

    private HiddenDangerView.TreeBarrierAddView tbAddView;
    private HiddenDangerView.TreeBarrierListView tbListView;
    private HiddenDangerModelImp mMode;

    public HiddenDangerPresenterImp(HiddenDangerView.TreeBarrierAddView tbView) {
        this.tbAddView = tbView;
        mMode = new HiddenDangerModelImp();
    }

    public HiddenDangerPresenterImp(HiddenDangerView.TreeBarrierListView tbListView) {
        this.tbListView = tbListView;
        mMode = new HiddenDangerModelImp();
    }

    /**
     * 新建树障
     */
    @Override
    public void createTreeBarrierDefect(TreeBarrierBean treeBarrierBean, List<MultipartBody.Part> requestImgParts) {
        mMode.createTreeBarrierDefect(treeBarrierBean, requestImgParts, new HiddenDangerListener.CreateTreeBarrierDefectListener() {
            @Override
            public void onSuccess(TreeBarrierBean treeBarrierBean) {
                tbAddView.onPostSuccess(treeBarrierBean);
            }

            @Override
            public void onFail(String msg) {
                tbAddView.onPostFail(msg);
            }
        });
    }

    /**
     * 获取树障列表
     */
    @Override
    public void getTreeBarrierDefectList(String LineId, String Category, String Status, String lineName, String towerId) {
        mMode.getTreeBarrierDefectList(LineId, Category, Status, lineName, towerId, new HiddenDangerListener.GetTreeBarrierDefectListListener() {
            @Override
            public void onSuccess(DefectInfo defectInfo) {
                tbListView.onPostSuccess(defectInfo);
            }

            @Override
            public void onFail(String msg) {
                tbListView.onPostFail(msg);
            }
        });
    }


    @Override
    public void callAll() {

    }
}
