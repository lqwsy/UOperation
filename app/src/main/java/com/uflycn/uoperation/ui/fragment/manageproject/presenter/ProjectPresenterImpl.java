package com.uflycn.uoperation.ui.fragment.manageproject.presenter;

import android.content.Context;

import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;
import com.uflycn.uoperation.ui.fragment.manageproject.model.ProJectModelImpl;
import com.uflycn.uoperation.ui.fragment.manageproject.model.ProjectModel;
import com.uflycn.uoperation.ui.fragment.manageproject.view.ProjectView;
import com.uflycn.uoperation.util.DialogUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/9.
 * 实体类
 */
public class ProjectPresenterImpl implements ProjectPresenter{
    private Reference<ProjectView> mViewReference;
    private ProjectModel mProjectModel;
    public ProjectPresenterImpl(ProjectView projectView) {
        mViewReference = new WeakReference<ProjectView>(projectView);
        mProjectModel = new ProJectModelImpl();
    }

//    @Override
//    public void postCreateProject(Map<String, Object> params, String path) {
//        mProjectModel.postCreateProject(params,path);
//    }

    @Override
    public void getAllProject(String lineid, String towerid, String projectName, String voltage) {
        mProjectModel.getAllProject(lineid,towerid,projectName,voltage);
    }

//    @Override
//    public void postInspectProject(Map<String, Object> params, String path) {
//        mProjectModel.postProjectInspect(params,path);
//    }

    @Override
    public void attach() {
        mProjectModel.attch();
    }

    @Override
    public void dettach() {
        mProjectModel.dettach();
    }

    @Override
    public void createProject(ProjectEntity projectEntity,List<Map<String,Object>> imagePathses) {
        mProjectModel.createProject(projectEntity,imagePathses);
    }

    @Override
    public void getInspectRecord(int projectid) {
        mProjectModel.getInspectRecordList(projectid);
    }

    @Override
    public void postInspectProject(ProjectInspection projectInspection,List<Map<String, Object>> datas,Context context) {
        mProjectModel.postProjectInspect(projectInspection,datas,context);
    }

    @Override
    public void editProject(ProjectEntity projectEntity,List<Map<String, Object>> datas) {
        mProjectModel.editProject(projectEntity,datas);
    }

    @Override
    public void cancel() {
        mProjectModel.cancel();
    }

    //    @Override
//    public void postUpdateProject(Map<String, Object> params, String path) {
//        mProjectModel.postUpdateProject(params,path);
//    }
}
