package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/10/11.
 */
public class EditProjectCallback extends BaseMainThreadEvent{
    private String message;

    public String getMessage() {
        return message;
    }

    public EditProjectCallback(String message) {

        this.message = message;
    }
}
