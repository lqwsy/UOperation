package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.TreeBarrierBean;

public interface HiddenDangerView {

    interface TreeBarrierAddView{
        void onPostSuccess(TreeBarrierBean treeBarrierBean);
        void onPostFail(String msg);
    }

    interface TreeBarrierListView{
        void onPostSuccess(DefectInfo defectInfo);
        void onPostFail(String msg);
    }

}
