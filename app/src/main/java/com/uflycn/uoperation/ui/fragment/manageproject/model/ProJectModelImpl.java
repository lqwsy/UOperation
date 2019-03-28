package com.uflycn.uoperation.ui.fragment.manageproject.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.CreateProjectEvent;
import com.uflycn.uoperation.eventbus.EditProjectCallback;
import com.uflycn.uoperation.eventbus.ProjectInspectEvent;
import com.uflycn.uoperation.eventbus.UpdateProjectListEvent;
import com.uflycn.uoperation.eventbus.UpdateProjectRecord;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.ProjectEntityDbHelper;
import com.uflycn.uoperation.greendao.ProjectInspectionDbHelper;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/9.
 * 项目管理 实体类
 */
public class ProJectModelImpl implements ProjectModel {

    private Call<BaseCallBack<List<ListCallBack<ProjectEntity>>>> mPostCreateRequest;
    private Call<BaseCallBack<String>> mPostProjectInspect;
    private Call<BaseCallBack<String>> mPostUpdateProject;
    private Call<BaseCallBack<List<ProjectEntity>>> mGetAllProjectRequest;
    private Call<BaseCallBack<List<ProjectInspection>>> mGetProjectInspectList;
    private LocalBroadcastManager localBroadcastManager;
    private Reference<Context> mContextRef;


    @Override
    public void attch() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void dettach() {
        EventBus.getDefault().unregister(this);
        if (mPostCreateRequest != null && !mPostCreateRequest.isCanceled()) {
            mPostCreateRequest.cancel();
        }
        if (mGetAllProjectRequest != null && !mGetAllProjectRequest.isCanceled()) {
            mGetAllProjectRequest.cancel();
        }
        if (mPostProjectInspect != null && !mPostProjectInspect.isCanceled()) {
            mPostProjectInspect.cancel();
        }
        if (mGetProjectInspectList != null && !mGetProjectInspectList.isCanceled()) {
            mGetProjectInspectList.cancel();
        }
        if (mPostUpdateProject != null && !mPostUpdateProject.isCanceled()) {
            mPostUpdateProject.cancel();
        }
    }


    @Subscribe
    public void onEventMainThread(Object object) {
    }

    private Map<String, RequestBody> initParams(Map<String, Object> map) {
        Map<String, RequestBody> params = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            RequestBody brokeType = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue() + "");
            params.put(entry.getKey(), brokeType);
        }
        return params;
    }

    @Override
    public void getAllProject(String lineid, String towerid, final String projectName, String voltage) {
        mGetAllProjectRequest = RetrofitManager.getInstance().getService(ApiService.class).getProjectList(lineid, towerid, projectName, voltage);
        mGetAllProjectRequest.enqueue(new Callback<BaseCallBack<List<ProjectEntity>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ProjectEntity>>> call, Response<BaseCallBack<List<ProjectEntity>>> response) {
                if (response == null || response.body() == null) {
                    getDateFromDb(projectName);
                    return;
                }
                if (response.body().getCode() == 0) {
                    EventBus.getDefault().post(new UpdateProjectListEvent(response.body().getMessage(), null));
                    //从数据库获取数据
                    getDateFromDb(projectName);
                } else {
                    ProjectEntityDbHelper.getInstance().updateProjectEntityList(response.body().getData());
                    EventBus.getDefault().post(new UpdateProjectListEvent(null, response.body().getData()));
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ProjectEntity>>> call, Throwable t) {
                getDateFromDb(projectName);
                if (t != null) {
                }
            }
        });
    }

    /**
     * 从数据库获取数据
     *
     * @param projectName
     */
    private void getDateFromDb(String projectName) {
        List<ProjectEntity> projectEntities = ProjectEntityDbHelper.getInstance().getProjectEntityList(projectName);
        for (ProjectEntity p : projectEntities) {
            if (p.getVoltageClass()!=null){
                if (!p.getVoltageClass().contains("kV")) {
                    //新加的判null
                    p.setVClass(ItemDetailDBHelper.getInstance().getItemDetail(p.getVoltageClass()).getItemsName());
                }
            }
        }
        EventBus.getDefault().post(new UpdateProjectListEvent(null, projectEntities));
    }


    @Override
    public void getInspectRecordList(final int projectId) {
        final ProjectEntity projectEntity = ProjectEntityDbHelper.getInstance().getProjectEntityById(projectId);
        List<ProjectInspection> projectInspections = ProjectInspectionDbHelper.getInstance().getProjectInspectionById(projectId);

        if (projectEntity == null || projectEntity.getPlatformId() == 0) {
            EventBus.getDefault().post(new UpdateProjectRecord(null, projectInspections));
        } else {
            mGetProjectInspectList = RetrofitManager.getInstance().getService(ApiService.class).getProjectInspectList(projectEntity.getPlatformId());
            mGetProjectInspectList.enqueue(new Callback<BaseCallBack<List<ProjectInspection>>>() {
                @Override
                public void onResponse(Call<BaseCallBack<List<ProjectInspection>>> call, Response<BaseCallBack<List<ProjectInspection>>> response) {
                    if (response == null || response.body() == null) {
                        Log.e("requestErr", "getInspectRecordList-->response == null || response.body() == null");
                        EventBus.getDefault().post(new UpdateProjectRecord(null, ProjectInspectionDbHelper.getInstance().getProjectInspectionByPlatformId(projectEntity.getPlatformId())));

                        return;
                    }
                    if (response.body().getCode() == 0) {
                        Log.v("response", "" + response.body().getData());
                        EventBus.getDefault().post(new UpdateProjectRecord(null, response.body().getData()));
                        //EventBus.getDefault().post(new UpdateProjectRecord(null, ProjectInspectionDbHelper.getInstance().getProjectInspectionByPlatformId(projectEntity.getPlatformId())));
                    } else {
                        ProjectInspectionDbHelper.getInstance().updateProjectInspection(response.body().getData());
                        EventBus.getDefault().post(new UpdateProjectRecord(null, response.body().getData()));
                    }
                }

                @Override
                public void onFailure(Call<BaseCallBack<List<ProjectInspection>>> call, Throwable t) {
                    if (t != null) {
                        Log.e("requestErr", "" + t.getMessage());
                    }
                    EventBus.getDefault().post(new UpdateProjectRecord(null, ProjectInspectionDbHelper.getInstance().getProjectInspectionByPlatformId(projectEntity.getPlatformId())));
                }
            });

            for (ProjectInspection projectInspection : projectInspections) {
                if (projectInspection.getProjectStatus() == 1) {
                    ProjectEntityDbHelper.getInstance().delete(projectEntity);//已经结束的时候本地删除
                }
            }
        }


    }

    private void saveImagePaths(List<Map<String, Object>> datas, ProjectEntity projectEntity) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(5);
            imagePaths.setLocalId(projectEntity.getSysBrokenDocumentId() + "");
            imagePaths.setFatherPlatformId(projectEntity.getPlatformId() + "");
            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }

    private void saveImagePaths(List<Map<String, Object>> datas, ProjectInspection projectInspection) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(6);
            imagePaths.setLocalId(projectInspection.getSysProjectInspectionId() + "");

            imagePaths.setFatherPlatformId("");

            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }

    @Override
    public void createProject(final ProjectEntity projectEntity, List<Map<String, Object>> datas) {
        //1、存储到数据库
        Long id = ProjectEntityDbHelper.getInstance().insertProjectEntity(projectEntity);
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ProjectImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        projectEntity.setSysBrokenDocumentId(id);
        saveImagePaths(datas, projectEntity);

        Map<String, Object> params = new HashMap<>();
        params.put("VoltageClass", projectEntity.getVoltageClass());
        params.put("GridLine", projectEntity.getGridLineName());
        params.put("StartTower", projectEntity.getStartTowerNo());
        params.put("EndTower", projectEntity.getEndTowerNo());
        params.put("ProjectName", projectEntity.getProjectName());
        params.put("ProjectDescription", projectEntity.getProjectDescription());
        params.put("ProjectManager", projectEntity.getProjectManager());
        params.put("PlanDailyPlanSectionIDs", projectEntity.getPlanDailyPlanSectionIDs());
        mPostCreateRequest = RetrofitManager.getInstance().getService(ApiService.class).postCreateProject(initParams(params), requestImgParts);
        mPostCreateRequest.enqueue(new Callback<BaseCallBack<List<ListCallBack<ProjectEntity>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<ProjectEntity>>>> call, Response<BaseCallBack<List<ListCallBack<ProjectEntity>>>> response) {
                if (response == null || response.body() == null) {
                    Log.e("requestErr", "createProject-->response == null || response.body() == null");
                    return;
                }
                if (response.body().getCode() == 0) {
                    Log.e("requestErr", "createProject" + response.body().getMessage());
                    EventBus.getDefault().post(new CreateProjectEvent(null));
                } else {
                    //2、上传项目到平台 若成功 则修改项目的状态为已上传（1）
                    ProjectEntity projectEntity1 = response.body().getData().get(0).getRecordData();
                    projectEntity.setProjectNo(projectEntity1.getProjectNo());
                    projectEntity.setUploadFlag(1);
                    projectEntity.setPlatformId(response.body().getData().get(0).getPlatformId());
                    ProjectEntityDbHelper.getInstance().updateProjectStatus(projectEntity);
                    EventBus.getDefault().post(new CreateProjectEvent(null));
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<ProjectEntity>>>> call, Throwable t) {
                if (t != null) {
                    Log.e("requestErr", "createProject" + t.getMessage());
                }
                EventBus.getDefault().post(new CreateProjectEvent(null));
            }
        });


    }

    @Override
    public void postProjectInspect(final ProjectInspection projectInspection, List<Map<String, Object>> datas, final Context context) {

        Long id = ProjectInspectionDbHelper.getInstance().insertProjectInspection(projectInspection);
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ProjectInspectImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        projectInspection.setSysProjectInspectionId(id);
        saveImagePaths(datas, projectInspection);

        Map<String, Object> params = new HashMap<>();
        params.put("sysProjectId", projectInspection.getSysProjectId());
        params.put("Status", projectInspection.getProjectStatus());
        params.put("Remark", projectInspection.getRemark());
        params.put("sysTaskId", projectInspection.getSysTaskId());
        params.put("PlanDailyPlanSectionIDs", projectInspection.getSysTaskId());
        params.put("sysPatrolExecutionID", projectInspection.getSysPatrolExecutionID());


        mPostProjectInspect = RetrofitManager.getInstance().getService(ApiService.class).postInspectProject(initParams(params), requestImgParts);
        mPostProjectInspect.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response == null || response.body() == null) {
                    Log.e("requestErr", "postProjectInspect response == null || response.body() == null");
                    EventBus.getDefault().post(new ProjectInspectEvent(" response.body() == null"));
                    return;
                }
                if (response.body().getCode() == 0) {
                    Log.e("requestErr", "" + response.body().getMessage());
                    if (projectInspection.getSysTaskId() != 0) {
                        ToastUtil.show("工单已被其他用户处理");
                    }
                    EventBus.getDefault().post(new ProjectInspectEvent(response.body().getMessage()));
                } else {
                    EventBus.getDefault().post(new ProjectInspectEvent(response.body().getMessage()));
                    projectInspection.setUploadFlag(1);
                    ProjectInspectionDbHelper.getInstance().updateProjectInspection(projectInspection);
                    if (projectInspection.getSysTaskId() != 0) {
                        localBroadcastManager = LocalBroadcastManager.getInstance(context);
                        WorkSheetTaskDBHelper.getInstance().delete(projectInspection.getSysTaskId());
                        Intent intent = new Intent(AppConstant.WORK_SHEET_NUM_DEL);
                        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        localBroadcastManager.sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                ToastUtil.show("提交到数据库");
                EventBus.getDefault().post(new ProjectInspectEvent("网络连接失败"));
                if (t != null) {
                    Log.e("requestErr", "" + t.getMessage());
                }
            }
        });

    }

    @Override
    public void editProject(final ProjectEntity projectEntity, List<Map<String, Object>> datas) {
        Long id = ProjectEntityDbHelper.getInstance().insertProjectEntity(projectEntity);
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ProjectImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        projectEntity.setSysBrokenDocumentId(id);
        saveImagePaths(datas, projectEntity);

        Map<String, Object> params = new HashMap<>();
        params.put("sysProjectId", projectEntity.getPlatformId());
        params.put("VoltageClass", projectEntity.getVoltageClass());
        params.put("GridLine", projectEntity.getGridLineName());
        params.put("StartTower", projectEntity.getStartTowerNo());
        params.put("EndTower", projectEntity.getEndTowerNo());
        params.put("ProjectName", projectEntity.getProjectName());
        params.put("ProjectDescription", projectEntity.getProjectDescription());
        params.put("ProjectManager", projectEntity.getProjectManager());
        params.put("PlanDailyPlanSectionIDs", projectEntity.getSysTaskId());
        params.put("sysPatrolExecutionID", projectEntity.getSysPatrolExecutionID());


        mPostUpdateProject = RetrofitManager.getInstance().getService(ApiService.class).postUpdateProject(initParams(params), requestImgParts);

        mPostUpdateProject.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response == null || response.body() == null) {
                    Log.e("requestErr", "editProject response == null || response.body() == null --> editProject ");
                    EventBus.getDefault().post(new EditProjectCallback("返回错误"));
                    return;
                }
                if (response.body().getCode() == 0) {
                    Log.e("requestErr", "" + response.body().getMessage());
                    EventBus.getDefault().post(new EditProjectCallback(response.body().getMessage()));
                } else {

                    projectEntity.setUploadFlag(1);
                    ProjectEntityDbHelper.getInstance().updateProjectStatus(projectEntity);
                    EventBus.getDefault().post(new EditProjectCallback(null));

                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                EventBus.getDefault().post(new EditProjectCallback("网络连接失败"));
                if (t != null) {
                    Log.e("requestErr", "editProject" + t.getMessage());
                }
            }
        });


    }

    @Override
    public void cancel() {
        if (mPostCreateRequest != null && !mPostCreateRequest.isCanceled()) {
            mPostCreateRequest.cancel();
        }
        if (mGetAllProjectRequest != null && !mGetAllProjectRequest.isCanceled()) {
            mGetAllProjectRequest.cancel();
        }
        if (mPostProjectInspect != null && !mPostProjectInspect.isCanceled()) {
            mPostProjectInspect.cancel();
        }
        if (mGetProjectInspectList != null && !mGetProjectInspectList.isCanceled()) {
            mGetProjectInspectList.cancel();
        }
        if (mPostUpdateProject != null && !mPostUpdateProject.isCanceled()) {
            mPostUpdateProject.cancel();
        }
    }


}
