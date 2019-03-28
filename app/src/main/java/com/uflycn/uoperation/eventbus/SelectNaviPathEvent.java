package com.uflycn.uoperation.eventbus;

/**
 * Created by Administrator on 2017/9/27.
 */
public class SelectNaviPathEvent extends BaseMainThreadEvent{
    private int pathIndex;

    public SelectNaviPathEvent(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public int getPathIndex() {
        return pathIndex;
    }
}
