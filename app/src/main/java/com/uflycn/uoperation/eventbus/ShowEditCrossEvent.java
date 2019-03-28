package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.LineCrossEntity;

/**
 * Created by Administrator on 2017/9/28.
 */
public class ShowEditCrossEvent extends BaseMainThreadEvent{
    LineCrossEntity lineCrossEntity;

    public ShowEditCrossEvent(LineCrossEntity lineCrossEntity) {
        this.lineCrossEntity = lineCrossEntity;
    }
    public LineCrossEntity getLineCrossEntity() {
        return lineCrossEntity;
    }
}
