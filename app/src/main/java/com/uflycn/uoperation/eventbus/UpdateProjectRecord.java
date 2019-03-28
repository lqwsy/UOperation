package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.ProjectInspection;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */
public class UpdateProjectRecord extends BaseMainThreadEvent{
    private String message;
    private List<ProjectInspection> projectEntityList;

    public UpdateProjectRecord(String message, List<ProjectInspection> projectEntityList) {
        this.message = message;
        this.projectEntityList = projectEntityList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProjectInspection> getProjectEntityList() {
        return projectEntityList;
    }

    public void setProjectEntityList(List<ProjectInspection> projectEntityList) {
        this.projectEntityList = projectEntityList;
    }
}
