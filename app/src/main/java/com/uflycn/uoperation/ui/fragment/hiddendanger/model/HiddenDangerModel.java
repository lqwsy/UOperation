package com.uflycn.uoperation.ui.fragment.hiddendanger.model;

import com.uflycn.uoperation.bean.TreeBarrierBean;
import com.uflycn.uoperation.ui.fragment.hiddendanger.HiddenDangerListener;

import java.util.List;

import okhttp3.MultipartBody;

public interface HiddenDangerModel {

    void createTreeBarrierDefect(final TreeBarrierBean treeBarrierBean, final List<MultipartBody.Part> requestImgParts, HiddenDangerListener.CreateTreeBarrierDefectListener listener);
    void getTreeBarrierDefectList(String LineId, String Category, String Status,String lineName,String towerId, HiddenDangerListener.GetTreeBarrierDefectListListener listener);
    void cancelAll();
}
