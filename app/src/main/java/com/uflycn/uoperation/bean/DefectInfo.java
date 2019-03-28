package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * Created by xiaoyehai on 2017/9/11.
 */

public class DefectInfo {


    private int Code;
    private String Message;
    private List<DefectBean> ChannelDefect;
    private List<DefectBean> PropertyDefect;
    private List<TreeDefectPointBean> TreeDefectPoint;
    private List<DefectBean> PersonalDefect;

    private List<DefectBean> LineCrossList;

    private List<DefectBean> InfraredTemperatureList;
    private List<DefectBean> EarthingResistanceList;
    private List<DefectBean> ZeroDetectionList;

    public List<DefectBean> getLineCrossList() {
        return LineCrossList;
    }

    public void setLineCrossList(List<DefectBean> lineCrossList) {
        LineCrossList = lineCrossList;
    }

    public List<DefectBean> getInfraredTemperatureList() {
        return InfraredTemperatureList;
    }

    public void setInfraredTemperatureList(List<DefectBean> infraredTemperatureList) {
        InfraredTemperatureList = infraredTemperatureList;
    }

    public List<DefectBean> getEarthingResistanceList() {
        return EarthingResistanceList;
    }

    public void setEarthingResistanceList(List<DefectBean> earthingResistanceList) {
        EarthingResistanceList = earthingResistanceList;
    }

    public List<DefectBean> getZeroDetectionList() {
        return ZeroDetectionList;
    }

    public void setZeroDetectionList(List<DefectBean> zeroDetectionList) {
        ZeroDetectionList = zeroDetectionList;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DefectBean> getChannelDefect() {
        return ChannelDefect;
    }

    public void setChannelDefect(List<DefectBean> channelDefect) {
        ChannelDefect = channelDefect;
    }

    public List<DefectBean> getPropertyDefect() {
        return PropertyDefect;
    }

    public void setPropertyDefect(List<DefectBean> propertyDefect) {
        PropertyDefect = propertyDefect;
    }

    public List<DefectBean> getPersonalDefect() {
        return PersonalDefect;
    }

    public void setPersonalDefect(List<DefectBean> personalDefect) {
        PersonalDefect = personalDefect;
    }

    public List<TreeDefectPointBean> getTreeDefectPoint() {
        return TreeDefectPoint;
    }

    public void setTreeDefectPoint(List<TreeDefectPointBean> TreeDefectPoint) {
        this.TreeDefectPoint = TreeDefectPoint;
    }
}
