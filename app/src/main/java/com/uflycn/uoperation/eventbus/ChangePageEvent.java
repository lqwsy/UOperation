package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/9/26.
 */
public class ChangePageEvent extends BaseMainThreadEvent{
    private int changeToPos;

    public ChangePageEvent(int changeToPos) {
        this.changeToPos = changeToPos;
    }

    public int getChangeToPos() {
        return changeToPos;
    }
}
