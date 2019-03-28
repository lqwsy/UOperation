package com.uflycn.uoperation.eventbus;

public class StopLineFromPlanPatrilExecutionEvent {
    private String lineIds;

    public StopLineFromPlanPatrilExecutionEvent(String lineIds) {
        this.lineIds = lineIds;
    }

    public String getLineIds() {
        return lineIds;
    }
}
