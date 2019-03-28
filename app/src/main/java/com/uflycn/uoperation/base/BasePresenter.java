package com.uflycn.uoperation.base;

import java.lang.ref.WeakReference;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public abstract class BasePresenter<V extends BaseView, M extends BaseModel> {

    protected WeakReference<V> mWeakReference;

    public M model;

    public BasePresenter() {
        model=getModel();
    }

    public void attach(V view) {
        mWeakReference = new WeakReference<V>(view);
    }

    public void deAttch() {
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    public abstract M getModel();

    public V getView() {
        return mWeakReference == null ? null : mWeakReference.get();
    }
}
