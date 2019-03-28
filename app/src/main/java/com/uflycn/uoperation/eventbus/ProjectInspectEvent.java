package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/10/11.
 */
public class ProjectInspectEvent extends BaseMainThreadEvent{
    private String responseMesseage;

    public ProjectInspectEvent(String responseMesseage) {
        this.responseMesseage = responseMesseage;
    }

    public String getResponseMesseage() {
        return responseMesseage;
    }
}
