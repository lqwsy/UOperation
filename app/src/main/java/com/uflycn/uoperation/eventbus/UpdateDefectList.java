package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/9/28.
 */
public class UpdateDefectList extends BaseMainThreadEvent{
   private String towerId;

    public UpdateDefectList(String towerId) {
        this.towerId = towerId;
    }

    public String getTowerId() {
        return towerId;
    }
}
