package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/10/17.
 */
public class UpdateTableEvent {
    private int success;

    /**
     * @param success 1:成功 0:失败
     */
    public UpdateTableEvent(int success) {
        this.success = success;
    }

    public int getSuccess() {
        return success;
    }
}
