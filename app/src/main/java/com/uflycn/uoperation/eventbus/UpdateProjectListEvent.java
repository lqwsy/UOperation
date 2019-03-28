package com.uflycn.uoperation.eventbus;

import com.uflycn.uoperation.bean.ProjectEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */
public class UpdateProjectListEvent extends BaseMainThreadEvent{
    private String message;
    private List<ProjectEntity> projectEntityList;

    public UpdateProjectListEvent(String message,List<ProjectEntity> projectEntityList) {
        this.message = message;
        this.projectEntityList = projectEntityList;
    }

    public String getMessage() {
        return message;
    }

    public List<ProjectEntity> getProjectEntityList() {
        return projectEntityList;
    }
}
