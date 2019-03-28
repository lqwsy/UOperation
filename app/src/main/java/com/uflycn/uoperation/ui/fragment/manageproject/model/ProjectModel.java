package com.uflycn.uoperation.ui.fragment.manageproject.model;

import android.content.Context;

import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 */
public interface ProjectModel {
    void getAllProject(String lineid, String towerid, String projectName, String voltage);

    void attch();

    void dettach();

    void getInspectRecordList(int projectId);

    void createProject(ProjectEntity projectEntity, List<Map<String, Object>> datas);

    void postProjectInspect(ProjectInspection projectInspection, List<Map<String, Object>> datas, Context context);

    void editProject(ProjectEntity projectEntity, List<Map<String, Object>> datas);

    void cancel();
}
