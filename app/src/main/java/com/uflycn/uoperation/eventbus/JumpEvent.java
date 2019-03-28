package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.ProjectEntity;

/**
 * Created by Administrator on 2018/1/18.
 */
public class JumpEvent extends BaseMainThreadEvent{
    private int flag;
    private ProjectEntity mProjectEntity;

    public JumpEvent(int flag, ProjectEntity mProjectEntity) {
        this.flag = flag;
        this.mProjectEntity = mProjectEntity;
    }

    public int getFlag() {
        return flag;
    }

    public ProjectEntity getmProjectEntity() {
        return mProjectEntity;
    }

}
