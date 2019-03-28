package com.uflycn.uoperation.eventbus;

public class StopLineFromDayPlanEvent {
    private int lineId;

    public StopLineFromDayPlanEvent(int lineId) {
        this.lineId = lineId;
    }

    public int getLineId() {
        return lineId;
    }
}
