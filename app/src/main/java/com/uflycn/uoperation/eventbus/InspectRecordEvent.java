package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectRecord;

/**
 * Created by Administrator on 2017/10/11.
 */
public class InspectRecordEvent extends BaseMainThreadEvent{
    private ProjectEntity mRecord;

    public InspectRecordEvent(ProjectEntity mRecord) {
        this.mRecord = mRecord;
    }

    public ProjectEntity getRecord() {
        return mRecord;
    }
}
