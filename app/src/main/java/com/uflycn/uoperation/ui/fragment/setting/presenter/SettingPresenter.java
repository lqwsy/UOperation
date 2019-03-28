package com.uflycn.uoperation.ui.fragment.setting.presenter;

import com.uflycn.uoperation.ui.fragment.setting.contract.SettingContract;
import com.uflycn.uoperation.ui.fragment.setting.model.SettingModel;

/**
 * Created by xiaoyehai on 2017/9/4.
 */
public class SettingPresenter extends SettingContract.Presenter {


    @Override
    public SettingContract.Model getModel() {
        return new SettingModel();
    }
}
