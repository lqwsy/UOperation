package com.uflycn.uoperation.util;

import android.os.Handler;
import android.os.Message;

import com.uflycn.uoperation.base.BaseRequestView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class CallBackHandler extends Handler {
    private Reference<BaseRequestView> mViewReference;

    public CallBackHandler(BaseRequestView requestView) {
        mViewReference = new WeakReference<>(requestView);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 1:
                mViewReference.get().updateList((List) msg.obj);
                break;
            default:
                mViewReference.get().handRequestErr((String) msg.obj);
        }
    }
}
