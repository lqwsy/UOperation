package com.uflycn.uoperation.ui.fragment.manageproject.presenter;

import android.content.Context;

import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 */
public interface ProjectPresenter {//项目 管理者

    void attach();
    void dettach();
    void createProject(ProjectEntity projectEntity,List<Map<String, Object>> datas);
//    void postCreateProject(Map<String, Object> params, String path);
//    void postUpdateProject(Map<String, Object> params, String path);
//    void postInspectProject(Map<String, Object> params, String path);
    void getAllProject(String lineid,String  towerid,String projectName,String voltage);
    void getInspectRecord(int projectid);

    void postInspectProject(ProjectInspection projectInspection,List<Map<String, Object>> datas,Context context);
    void editProject(ProjectEntity projectEntity,List<Map<String, Object>> datas);

    void cancel();
}
