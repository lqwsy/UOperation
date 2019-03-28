package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/9/28.
 */
public class UpdateLineCrossList extends BaseMainThreadEvent{
    private boolean delete;

    public boolean isDelete() {
        return delete;
    }
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public UpdateLineCrossList(boolean delete) {
        this.delete = delete;
    }
}
