package com.uflycn.uoperation.base;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public interface BaseRequestView<T> {
    void updateList(List<T> datas);//获取和更新数据
    void handRequestErr(String message);
}
