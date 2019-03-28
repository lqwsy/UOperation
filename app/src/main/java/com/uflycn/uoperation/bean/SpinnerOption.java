package com.uflycn.uoperation.bean;

/**
 * Created by Xiong on 2017/9/23.
 */
public class SpinnerOption {
    private String value = "";
    private String text = "";

    public SpinnerOption() {
        value = "";
        text = "";
    }

    public SpinnerOption(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    public String getValue() {
        return value;
    }
    public String getText() {
        return text;
    }

    public boolean equals(SpinnerOption spinnerOption){
        if(this.getValue().equals(spinnerOption.getValue())) return true;
        return false;
    }
}
