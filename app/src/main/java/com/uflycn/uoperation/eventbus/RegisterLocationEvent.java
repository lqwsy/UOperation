package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.InPlace;

/**
 * Created by diyang on 2017/9/24.
 */
public class RegisterLocationEvent extends BaseMainThreadEvent {
    private InPlace mInplace;

    public RegisterLocationEvent(InPlace mInplace) {
        this.mInplace = mInplace;
    }

    public InPlace getInplace() {
        return mInplace;
    }

    public void setInplace(InPlace mInplace) {
        this.mInplace = mInplace;
    }
}
