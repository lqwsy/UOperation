package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/10/11.
 */
public class CreateProjectEvent extends BaseMainThreadEvent{
    private String responseMesseage;

    public CreateProjectEvent(String responseMesseage) {
        this.responseMesseage = responseMesseage;
    }

    public String getResponseMesseage() {
        return responseMesseage;
    }
}
