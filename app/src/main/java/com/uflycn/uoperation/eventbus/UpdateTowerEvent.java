package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.Tower;

/**
 * Created by diyang on 2017/9/24.
 */
public class UpdateTowerEvent extends BaseMainThreadEvent{
    private Tower currentNearestTower;

    public UpdateTowerEvent(Tower currentNearestTower) {
        this.currentNearestTower = currentNearestTower;
    }
    public Tower getCurrentNearestTower() {
        return currentNearestTower;
    }
}
