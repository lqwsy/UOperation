package com.uflycn.uoperation.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/26.
 * <p>
 * Description:棚膜管理
 */
public class ShedFilmBean {
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
    public static List<ShedFilmBean> initTestData(){
        List<ShedFilmBean> testBeans = new ArrayList<>();
        for(int i=0;i<3;i++){
            ShedFilmBean testBean = new ShedFilmBean();
            testBean.setLineName("线路"+i);
            testBean.setDescript("测试描述"+i);
            testBeans.add(testBean);
        }
        return testBeans;
    }
}
