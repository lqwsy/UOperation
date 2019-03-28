package com.uflycn.uoperation.ui.fragment.plantask.model;

import com.uflycn.uoperation.bean.Gridline;

import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */
public interface GridlineCallBack {
    void onGridLineCallBack(List<Gridline> gridlines);
    void onStartInspectCallBack(String mesage,int lineId);
}
