package com.uflycn.uoperation.bean;

/**
 * author : gxl
 * email : 1739037476@qq.com
 * create data : 2019/2/27
 * Describe  : 从线路中添加的选择对话框中的塔的bean类
 */
public class TowerNum {
    private String num;
    private boolean isChecked;//是否被选中

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
