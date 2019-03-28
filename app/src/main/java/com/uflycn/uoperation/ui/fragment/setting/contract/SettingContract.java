package com.uflycn.uoperation.ui.fragment.setting.contract;

import com.uflycn.uoperation.base.BaseModel;
import com.uflycn.uoperation.base.BasePresenter;
import com.uflycn.uoperation.base.BaseView;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public interface SettingContract {

    interface Model extends BaseModel {

    }

    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<View, Model> {

    }
}
