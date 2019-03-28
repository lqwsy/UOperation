package com.uflycn.uoperation.ui.fragment.hiddendanger.presenter;

import com.uflycn.uoperation.bean.TreeBarrierBean;
import com.uflycn.uoperation.ui.fragment.hiddendanger.HiddenDangerListener;

import java.util.List;

import okhttp3.MultipartBody;

public interface HiddenDangerPresenter {

    void createTreeBarrierDefect(final TreeBarrierBean treeBarrierBean, final List<MultipartBody.Part> requestImgParts);
    void getTreeBarrierDefectList(String LineId, String Category, String Status,String lineName,String towerId);
    void callAll();

}
