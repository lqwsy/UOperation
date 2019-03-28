package com.uflycn.uoperation.ui.fragment.hiddendanger.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 树障请求
 */
public class TestBean {

    private String lineName;
    private String descript;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    //测试数据
    public static List<TestBean> initTestData(){
        List<TestBean> testBeans = new ArrayList<>();
        for(int i=0;i<3;i++){
            TestBean testBean = new TestBean();
            testBean.setLineName("线路"+i);
            testBean.setDescript("测试描述"+i);
            testBeans.add(testBean);
        }
        return testBeans;
    }

}
