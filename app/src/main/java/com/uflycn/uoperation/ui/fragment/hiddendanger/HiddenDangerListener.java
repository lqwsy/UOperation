package com.uflycn.uoperation.ui.fragment.hiddendanger;

import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.TreeBarrierBean;

public interface HiddenDangerListener {

    interface CreateTreeBarrierDefectListener{
        void onSuccess(TreeBarrierBean treeBarrierBean);
        void onFail(String msg);
    }

    interface GetTreeBarrierDefectListListener{
        void onSuccess(DefectInfo defectInfo);
        void onFail(String msg);
    }


}
