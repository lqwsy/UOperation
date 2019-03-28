package com.uflycn.uoperation.bean;

import java.util.Date;

/**
 * Created by Xiong on 2017/9/27.
 */
public class TempTaskInfo {
    private int sysMessageInfoId;
    private String VoltageClass;
    private String LineName;
    private String StartTower;
    private String EndTower;
    private String PublishTime;
    private String ExpireTime;
    private String MessageContent;
    private String ReadState;

    public int getSysMessageInfoId() {
        return sysMessageInfoId;
    }

    public void setSysMessageInfoId(int sysMessageInfoId) {
        this.sysMessageInfoId = sysMessageInfoId;
    }

    public String getVoltageClass() {
        return VoltageClass;
    }

    public void setVoltageClass(String voltageClass) {
        VoltageClass = voltageClass;
    }

    public String getLineName() {
        return LineName;
    }

    public void setLineName(String lineName) {
        LineName = lineName;
    }

    public String getStartTower() {
        return StartTower;
    }

    public void setStartTower(String startTower) {
        StartTower = startTower;
    }

    public String getEndTower() {
        return EndTower;
    }

    public void setEndTower(String endTower) {
        EndTower = endTower;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }

    public String getExpireTime() {
        return ExpireTime;
    }

    public void setExpireTime(String expireTime) {
        ExpireTime = expireTime;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    public String getReadState() {
        return ReadState;
    }

    public void setReadState(String readState) {
        ReadState = readState;
    }
}
