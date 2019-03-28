package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.ProjectEntity;

/**
 * Created by Administrator on 2017/10/11.
 */
public class EditProjectEvent extends BaseMainThreadEvent{
    private ProjectEntity mProjectEntity;
    public EditProjectEvent(ProjectEntity mProjectEntity) {
        this.mProjectEntity = mProjectEntity;
    }
    public ProjectEntity getProjectEntity() {
        return mProjectEntity;
    }
}
