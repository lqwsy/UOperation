package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * Created by UF_PC on 2017/10/23.
 */
public class TempTask {
    private int sysMessageInfoId;
    private String Title;
    private String MessageContent;
    private String PublishTime;
    private String ExpireTime;
    private String ReadState;
    private String ReadTime;
    private List<SectionList> SectionList;


    public int getSysMessageInfoId() {
        return sysMessageInfoId;
    }

    public void setSysMessageInfoId(int sysMessageInfoId) {
        this.sysMessageInfoId = sysMessageInfoId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
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

    public String getReadState() {
        return ReadState;
    }

    public void setReadState(String readState) {
        ReadState = readState;
    }

    public String getReadTime() {
        return ReadTime;
    }

    public void setReadTime(String readTime) {
        ReadTime = readTime;
    }

    public List<com.uflycn.uoperation.bean.SectionList> getSectionList() {
        return SectionList;
    }

    public void setSectionList(List<com.uflycn.uoperation.bean.SectionList> sectionList) {
        SectionList = sectionList;
    }
}
