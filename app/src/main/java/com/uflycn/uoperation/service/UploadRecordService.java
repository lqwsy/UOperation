package com.uflycn.uoperation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.AssistRecord;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.BrokenInspectRecord;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectDeleteRecord;
import com.uflycn.uoperation.bean.DefectRemark;
import com.uflycn.uoperation.bean.EarthingResistance;
import com.uflycn.uoperation.bean.FileList;
import com.uflycn.uoperation.bean.IceCover;
import com.uflycn.uoperation.bean.InPlace;
import com.uflycn.uoperation.bean.InfraredTemperature;
import com.uflycn.uoperation.bean.LineCrossDelete;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.OptTensilePointTemperature;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;
import com.uflycn.uoperation.bean.TowerChange;
import com.uflycn.uoperation.bean.ZeroDetection;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.AssistRecordDBHelper;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.greendao.BrokenInspectRecordDBHelper;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.DefectDeleteDBHelper;
import com.uflycn.uoperation.greendao.DefectRemarkDBHelper;
import com.uflycn.uoperation.greendao.EarthingResistanceDBHelper;
import com.uflycn.uoperation.greendao.IceCoverDBHelper;
import com.uflycn.uoperation.greendao.ImagePathsDBHelper;
import com.uflycn.uoperation.greendao.InPlaceDBHelper;
import com.uflycn.uoperation.greendao.InfraredTemperatureDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDeleteDBHelper;
import com.uflycn.uoperation.greendao.ProjectEntityDbHelper;
import com.uflycn.uoperation.greendao.ProjectInspectionDbHelper;
import com.uflycn.uoperation.greendao.TowerChangeDBHelper;
import com.uflycn.uoperation.greendao.ZeroDetectionDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.listeners.NetWorkChangeListener;
import com.uflycn.uoperation.receiver.NetWorkReceiver;
import com.uflycn.uoperation.util.ToastUtil;
import com.xflyer.utils.ThreadPoolUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
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

public class UploadRecordService extends Service implements NetWorkChangeListener {
    private List<Call> mRequestCalls = new ArrayList<>();
    // 1.上传外破 (更新PlatFormId(包括特训的外破Id)) 之后上传 外破特训记录
    // 2.上传项目 (更新PlatFormId(包括特训的项目Id)) 之后上传 外破特训记录
    //3.上传缺陷登记（更新PlatFormId（包括备注的缺陷Id））之后上传备注 列表，消缺记录
    private Gson mGosn;
    private static boolean isUploadingBroken = false;
    private static boolean isUploadingProject = false;
    private static boolean isUploadDefects = false;

    private static boolean isUploadInplaces = false;
    private static boolean isUploadEarthResitance = false;
    private static boolean isUploadIceCover = false;
    private static boolean isTemperature = false;
    private static boolean isZeroDetection = false;
    private static boolean isTowerChange = false;
    private static boolean isUploadLineCross = false;
    private static boolean isUploadAssistRecord = false;
    private int brokenRecordSize = 0;
    private int projectRecordSize = 0;
    private int uploadDefectsSize = 0;
    private int uploadInplacesSize = 0;
    private int upTowerChange = 0;
    private int upUploadLineCross = 0;
    private int upUploadAssistRecord = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mGosn = new Gson();
        EventBus.getDefault().register(this);
        startUpload();
        NetWorkReceiver.addNetWorkChangeListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onNetWorkChange(boolean netAvailable) {
        Log.d("nate", "onNetWorkChange: " + netAvailable);
        if (netAvailable) {
            startUpload();
        } else {
            stopAllCalls();
        }
    }

    private synchronized void startUpload() {
        if (AppConstant.NET_WORK_AVAILABLE) {//有网
            Log.d("jsonStr", "startUpload: ");
            if (AppConstant.TOKEN != null && RetrofitManager.getInstance().getToken() == null) {
                RetrofitManager.getInstance().resetRetrofit();
            }
            if (!isUploadingBroken) {
                //上传外破
                ThreadPoolUtils.execute(new Runnable() {
                    @Override
                    public void run() {
                        isUploadingBroken = true;
                        //1.获取所有需要上传的外破记录
                        List<BrokenDocument> uploadList = BrokenDocumentDBHelper.getInstance().getUploadList();
                        brokenRecordSize = uploadList.size();
                        if (brokenRecordSize == 0) {
                            updateBrokenUploadingStatus();
                        }
                        for (BrokenDocument brokenDocument : uploadList) {
                            postUploadBroken(brokenDocument);
                        }
                    }
                });
            }
            postUploadInplaces();//上传 到位
            postUploadEarthResitance();//接地电阻
            uploadTemperature();//红外测温
            uploadIceCover();//符宾法国
            uploadZeroDetection();//零值检测
            postUploadLineCross();//交跨
            postUploadTowerChange();//杆塔变更登记
            postUploadProjects();//上传新建项目
            postUploadDefectBean();//更新 除了树障意外的缺陷
            postAssistRecordList();
        }
    }


    private void uploadZeroDetection() {
        if (isZeroDetection)
            return;

        final List<ZeroDetection> list = ZeroDetectionDBHelper.getInstance().getUploadList();
        if (list.size() == 0) {
            return;
        }
        isZeroDetection = true;
        String jsonStr = mGosn.toJson(list);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<BaseCallBack<List<ListCallBack<String>>>> call =
                RetrofitManager.getInstance().getService(ApiService.class)
                        .postZeroDetection(body);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    List<ListCallBack<String>> listCallBacks = response.body().getData();
                    for (ListCallBack<String> listCallBack : listCallBacks) {
                        if (listCallBack.getPlatformId() != 0) {
                            ZeroDetection zeroDetection = list.get(listCallBack.getIndex());
                            zeroDetection.setUploadFlag(1);
                            ZeroDetectionDBHelper.getInstance().updateZeroDetction(zeroDetection);
                        }
                    }
                }
                isZeroDetection = false;
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                mRequestCalls.add(call);
                isZeroDetection = false;
            }
        });
    }

    private void postUploadBroken(final BrokenDocument brokenDocument) {
        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(3, brokenDocument.getPlatformId() + "", brokenDocument.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("BrokenImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        String jsonStr = mGosn.toJson(brokenDocument);
        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<BaseCallBack<List<ListCallBack<BrokenDocument>>>> call = RetrofitManager.getInstance().getService(ApiService.class).postCreateDocument(body, requestImgParts);
        mRequestCalls.add(call);//加入
        call.enqueue(new Callback<BaseCallBack<List<ListCallBack<BrokenDocument>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<BrokenDocument>>>> call, Response<BaseCallBack<List<ListCallBack<BrokenDocument>>>> response) {
                mRequestCalls.remove(call);//移除
                brokenRecordSize--;
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    brokenDocument.setPlatformId(response.body().getData().get(0).getPlatformId());
                    brokenDocument.setDocmentNo(response.body().getData().get(0).getRecordData().getDocmentNo());
                    BrokenDocumentDBHelper.getInstance().updateById(brokenDocument);
                    updateBrokenInspectRecord(brokenDocument);
                }
                updateBrokenUploadingStatus();
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<BrokenDocument>>>> call, Throwable t) {
                mRequestCalls.remove(call);//移除
                brokenRecordSize--;
                updateBrokenUploadingStatus();
            }
        });
    }

    private void stopAllCalls() {
        for (Call call : mRequestCalls) {
            if (call != null && !call.isCanceled()) {
                call.cancel();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopAllCalls();
    }

    private synchronized void updateBrokenUploadingStatus() {
        if (brokenRecordSize <= 0) {
            isUploadingBroken = false;
        }
        if (!isUploadingBroken) {//外破上传完了之后 开始上传  修改的 和 特巡记录
            postUploadBrokenInspectRecord();
        }
    }

    private synchronized void updateProjectploadingStatus() {
        if (projectRecordSize <= 0) {
            isUploadingProject = false;
        }
        if (!isUploadingProject) {//项目上传完了之后 开始上传 巡视记录 以及项目修改
            postUploadProjectInspectRecord();
        }
    }

    private void postUploadBrokenInspectRecord() {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                List<BrokenInspectRecord> list = BrokenInspectRecordDBHelper.getInstance().getNeedUploadList();
                for (BrokenInspectRecord record : list) {
                    postUploadBrokenInspectRecord(record);
                }
                //上传需要修改的外破
                List<BrokenDocument> brokenDocuments = BrokenDocumentDBHelper.getInstance().getNeedUpdateList();
                for (BrokenDocument document : brokenDocuments) {
                    postUpdateBroken(document);
                }
            }
        });
    }

    /**
     * 更新外破信息    3    BrokenImage
     *
     * @param brokenDocument
     */
    private void postUpdateBroken(final BrokenDocument brokenDocument) {
        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(3, brokenDocument.getPlatformId() + "", brokenDocument.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("BrokenImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postUpdateBrokenDocument(initParams(brokenDocument), requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    brokenDocument.setUploadFlag(1);
                    BrokenDocumentDBHelper.getInstance().updateById(brokenDocument);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                mRequestCalls.remove(call);
            }
        });
    }

    private Map<String, RequestBody> initParams(BrokenDocument brokenDocument) {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getPlatformId() + "");
        params.put("sysBrokenDocumentId", id);

        //外破类型
        RequestBody brokeType = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getBrokenType());
        params.put("BrokenType", brokeType);
        //StartTowerId
        RequestBody startTowerRequest = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getStartTowerId() + "");
        params.put("StartTowerId", startTowerRequest);
        //EndTowerId
        RequestBody endTowerRequest = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getEndTowerId() + "");
        params.put("EndTowerId", endTowerRequest);

        //公司
        RequestBody company = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getCompany());
        params.put("Company", company);
        //联系人
        RequestBody contact = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getContactPerson());
        params.put("ContactPerson", contact);
        //电话
        RequestBody phone = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getPhoneNo());
        params.put("PhoneNo", phone);
        //remark
        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getRemark());
        params.put("Remark", remark);

        RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getPlanDailyPlanSectionIDs() + "");
        params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);

        RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), brokenDocument.getSysPatrolExecutionID());
        params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        return params;
    }

    private void updateBrokenInspectRecord(BrokenDocument brokenDocument) {
        List<BrokenInspectRecord> list = BrokenInspectRecordDBHelper.getInstance().getAllCacheById(brokenDocument.getId().intValue());
        for (BrokenInspectRecord record : list) {
            record.setDocumentPlatformId(brokenDocument.getPlatformId());
        }
        BrokenInspectRecordDBHelper.getInstance().updateList(list);
    }

    /**
     * 上传外破特训记录     4   BrokenImage
     *
     * @param brokenInspectRecord
     */
    private void postUploadBrokenInspectRecord(final BrokenInspectRecord brokenInspectRecord) {
        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(4, brokenInspectRecord.getSysBrokenPatrolDetailId() + "", brokenInspectRecord.getSysBrokenInspectRecordId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("BrokenImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postBrokenPatrolRegister(initParams(brokenInspectRecord), requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    brokenInspectRecord.setUploadFlag(1);
                    BrokenInspectRecordDBHelper.getInstance().update(brokenInspectRecord);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                mRequestCalls.remove(call);
            }
        });

    }

    private Map<String, RequestBody> initParams(BrokenInspectRecord brokenInspectRecord) {
        Map<String, RequestBody> params = new HashMap<>();

        RequestBody contact = RequestBody.create(MediaType.parse("multipart/form-data"), brokenInspectRecord.getDocumentPlatformId() + "");
        params.put("BrokenDocumentId", contact);

        RequestBody statu = RequestBody.create(MediaType.parse("multipart/form-data"), brokenInspectRecord.getBrokenStatus() + "");
        params.put("Status", statu);
        //remark
        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), brokenInspectRecord.getRemark());
        params.put("Remark", remark);

        RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), brokenInspectRecord.getPlanDailyPlanSectionIDs() + "");
        params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);

        RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), brokenInspectRecord.getSysPatrolExecutionID());
        params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        return params;
    }

    /**
     * 以下上传项目
     */
    private void postUploadProjects() {
        if (!isUploadingProject) {
            ThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    isUploadingProject = true;
                    //1.获取所有需要上传的外破记录
                    List<ProjectEntity> uploadList = ProjectEntityDbHelper.getInstance().getNeedUploadList();
                    projectRecordSize = uploadList.size();
                    if (projectRecordSize == 0) {
                        updateProjectploadingStatus();
                    }
                    for (ProjectEntity projectEntity : uploadList) {
                        postUploadProject(projectEntity);
                    }
                }
            });
        }
    }

    /**
     * 上传项目     5
     *
     * @param projectEntity
     */
    private void postUploadProject(final ProjectEntity projectEntity) {


        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(5, projectEntity.getPlatformId() + "", projectEntity.getSysBrokenDocumentId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ProjectImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("VoltageClass", projectEntity.getVoltageClass());
        params.put("GridLine", projectEntity.getGridLineName());
        params.put("StartTower", projectEntity.getStartTowerNo());
        params.put("EndTower", projectEntity.getEndTowerNo());
        params.put("ProjectName", projectEntity.getProjectName());
        params.put("ProjectDescription", projectEntity.getProjectDescription());
        params.put("ProjectManager", projectEntity.getProjectManager());
        params.put("PlanDailyPlanSectionIDs", projectEntity.getPlanDailyPlanSectionIDs());

        Call<BaseCallBack<List<ListCallBack<ProjectEntity>>>> call = RetrofitManager.getInstance().getService(ApiService.class).postCreateProject(initParams(params), requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<List<ListCallBack<ProjectEntity>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<ProjectEntity>>>> call, Response<BaseCallBack<List<ListCallBack<ProjectEntity>>>> response) {
                projectRecordSize--;
                updateProjectploadingStatus();
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    ProjectEntity projectEntity1 = response.body().getData().get(0).getRecordData();
                    projectEntity.setUploadFlag(1);
                    projectEntity.setProjectNo(projectEntity1.getProjectNo());

                    projectEntity.setPlatformId(response.body().getData().get(0).getPlatformId());
                    ProjectEntityDbHelper.getInstance().updateProjectStatus(projectEntity);
                    //上传完项目之后 更新项目巡视记录
                    List<ProjectInspection> list = ProjectInspectionDbHelper.getInstance().getProjectInspectionById(projectEntity.getSysBrokenDocumentId().intValue());
                    for (ProjectInspection record : list) {
                        record.setSysProjectId(projectEntity.getPlatformId());
                    }
                    ProjectInspectionDbHelper.getInstance().updateList(list);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<ProjectEntity>>>> call, Throwable t) {
                projectRecordSize--;
                updateProjectploadingStatus();
            }
        });
    }

    private Map<String, RequestBody> initParams(Map<String, Object> map) {
        Map<String, RequestBody> params = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            RequestBody brokeType = RequestBody.create(MediaType.parse("multipart/form-data"), entry.getValue() + "");
            params.put(entry.getKey(), brokeType);
        }
        return params;
    }

    private synchronized void postUploadProjectInspectRecord() {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                List<ProjectInspection> uploadList = ProjectInspectionDbHelper.getInstance().getNeedUploadList();
                for (ProjectInspection inspection : uploadList) {
                    postUpladProjectRecord(inspection);
                }
                List<ProjectEntity> list = ProjectEntityDbHelper.getInstance().getNeedUpdateList();
                for (ProjectEntity projectEntity : list) {
                    postUpdateProject(projectEntity);
                }
            }
        });
    }

    /**
     * 修改项目    5   ProjectImage
     *
     * @param projectEntity
     */
    private void postUpdateProject(final ProjectEntity projectEntity) {

        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(5, projectEntity.getPlatformId() + "", projectEntity.getSysBrokenDocumentId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ProjectImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
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

        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postUpdateProject(initParams(params), requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    projectEntity.setUploadFlag(1);
                    ProjectEntityDbHelper.getInstance().updateProjectStatus(projectEntity);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                mRequestCalls.remove(call);
            }
        });

    }

    /**
     * 上传项目特训     6   ProjectInspectImage
     *
     * @param inspection
     */
    private void postUpladProjectRecord(final ProjectInspection inspection) {

        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(6, "", inspection.getSysProjectInspectionId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ProjectInspectImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("sysProjectId", inspection.getSysProjectId());
        params.put("Status", inspection.getProjectStatus());
        params.put("Remark", inspection.getRemark());
        params.put("PlanDailyPlanSectionIDs", inspection.getSysTaskId());
        params.put("sysPatrolExecutionID", inspection.getSysPatrolExecutionID());

        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postInspectProject(initParams(params), requestImgParts);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    inspection.setUploadFlag(1);
                    ProjectInspectionDbHelper.getInstance().updateProjectInspection(inspection);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                int i = 0;
            }
        });
    }

    /**
     * 以下上传缺陷记录
     */
    private void uploadDefects() {
        //1.获取需要上传的缺陷记录 （新建和修改）包括树障
        //2.上传缺陷备注
        //3.上传对应的缺陷消缺记录
    }

    //更新 除了树障意外的缺陷
    private void postUploadDefectBean() {
        if (isUploadDefects)
            return;
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                isUploadDefects = true;
                //1.获取全部未上传的记录 树障没有缺陷 的修改新建操作 UPLOADFLAG == 1 无需上传
                List<DefectBean> list = DefectBeanDBHelper.getInstance().getNeedUploadList();
                uploadDefectsSize = list.size();
                if (uploadDefectsSize == 0) {
                    updateDefectsUploadingStatus();
                }
                for (DefectBean defectBean : list) {
                    postUploadDefectBean(defectBean);
                }
            }
        });

    }

    private Map<String, RequestBody> initParams(DefectBean defectBean) {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody towerId = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getSysTowerID() + "");
        params.put("TowerId", towerId);
        RequestBody defectType = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getDefectTypeId());
        params.put("DefectType", defectType);
        //remark
        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getRemark());
        params.put("Remark", remark);
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getLatitude() + "");
        params.put("Latitude", latitude);
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getLongitude() + "");
        params.put("Longitude", longitude);
        RequestBody nearTowerID = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getNearTowerID() + "");
        params.put("NearTowerID", nearTowerID);
        RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getPlanDailyPlanSectionIDs() + "");
        params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);
        RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getSysPatrolExecutionID()+"");
        params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        RequestBody DefectPosition = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getDefectPosition());
        params.put("DefectPosition", DefectPosition);
        RequestBody Phase = RequestBody.create(MediaType.parse("multipart/form-data"), defectBean.getPhase());
        params.put("Phase", Phase);
        return params;
    }

    private synchronized void updateDefectsUploadingStatus() {
        if (uploadDefectsSize <= 0) {
            isUploadDefects = false;
        }
        if (!isUploadDefects) {//缺陷上传完了之后 开始上传备注
            // 和消缺记录
            postUploadDefectRemarks();
        }
    }


    private void postUploadDefectRemarks() {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                List<DefectRemark> remarks = DefectRemarkDBHelper.getInstance().getNeedUploadList();
                for (DefectRemark remark : remarks) {//上传备注
                    postUploadRemark(remark);
                }
                List<DefectDeleteRecord> defectDeleteRecords = DefectDeleteDBHelper.getInstance().getUploadList();
                for (DefectDeleteRecord defectDeleteRecord : defectDeleteRecords) {//上传消缺记录
                    postUploadDeleteRecord(defectDeleteRecord);
                }
            }
        });
    }

    /**
     * 上传缺陷备注
     *
     * @param defectRemark
     */
    private void postUploadRemark(final DefectRemark defectRemark) {
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class)
                .postDefectVerification(defectRemark.getDefectType(), defectRemark.getDefectId(), defectRemark.getRemark(),
                        defectRemark.getSysPatrolExecutionID(), defectRemark.getPlanDailyPlanSectionIDs());
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    ToastUtil.show("提交成功");
                    defectRemark.setUploadFlag(1);
                    DefectRemarkDBHelper.getInstance().update(defectRemark);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
            }
        });
    }

    /**
     * 上传缺陷    10  DefectPhoto
     *
     * @param defectBean
     */
    private void postUploadDefectBean(final DefectBean defectBean) {

        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(10, defectBean.getSysTowerDefectId() + "", defectBean.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("DefectPhoto" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        /**
         * 上传缺陷
         */
        Call<BaseCallBack<ListCallBack>> call = RetrofitManager.getInstance().getService(ApiService.class).postAddTowerDefect(initParams(defectBean), requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<ListCallBack>>() {
            @Override
            public void onResponse(Call<BaseCallBack<ListCallBack>> call, Response<BaseCallBack<ListCallBack>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    defectBean.setUploadFlag(1);
                    int platFormId = response.body().getData().getPlatformId();
                    defectBean.setSysTowerDefectId(platFormId);
                    DefectBeanDBHelper.getInstance().updateById(defectBean);
                    //修改缺陷之后 需要修改对应的  消缺，备注记录
                    List<DefectRemark> defectRemarks = DefectRemarkDBHelper.getInstance().getListByLocalDefectId(defectBean.getId().intValue());
                    for (DefectRemark defectRemark : defectRemarks) {
                        defectRemark.setDefectId(platFormId);
                        DefectRemarkDBHelper.getInstance().update(defectRemark);
                    }
                    //修改对应的消缺记录
                    List<DefectDeleteRecord> recordList = DefectDeleteDBHelper.getInstance().getDefectDeleteRecordByLocalId(defectBean.getId().intValue());
                    for (DefectDeleteRecord record : recordList) {
                        record.setLocalDefectId(platFormId + "");
                        DefectDeleteDBHelper.getInstance().update(record);
                    }
                }
                uploadDefectsSize--;
                updateDefectsUploadingStatus();
            }

            @Override
            public void onFailure(Call<BaseCallBack<ListCallBack>> call, Throwable t) {
                mRequestCalls.remove(call);
                uploadDefectsSize--;
                updateDefectsUploadingStatus();
            }
        });
    }

    /**
     * 上传消缺记录     11   CheckedPhoto
     *
     * @param defectDeleteRecord
     */
    private void postUploadDeleteRecord(final DefectDeleteRecord defectDeleteRecord) {
        Call<BaseCallBack<String>> call;

        List<Map<String, Object>> datas = new ArrayList<>();
        if (defectDeleteRecord.getDefectCategory() == 1) {
            datas = ImagePathsDBHelper.getInstance().getImagePaths(14, defectDeleteRecord.getSysDefectId(), defectDeleteRecord.getId());
        } else {
            datas = ImagePathsDBHelper.getInstance().getImagePaths(11, defectDeleteRecord.getSysDefectId(), defectDeleteRecord.getId());
        }

        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CheckedPhoto" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        if (defectDeleteRecord.getDefectCategory() == 1) {//树障 消缺
            call = RetrofitManager.getInstance().getService(ApiService.class).postClearTreeDefect(initParams(defectDeleteRecord), requestImgParts);
        } else {//其他缺陷消缺
            call = RetrofitManager.getInstance().getService(ApiService.class).postClearDefect(initParams(defectDeleteRecord), requestImgParts);
        }
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response.body().getMessage() != null && (
                        response.body().getMessage().equals("该缺陷已消除") ||
                                response.body().getMessage().equals("请选择上传文件"))) {
                    DefectDeleteDBHelper.getInstance().delete(defectDeleteRecord);
                }
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    defectDeleteRecord.setUploadFlag(1);
                    DefectDeleteDBHelper.getInstance().update(defectDeleteRecord);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                int i = 0;
            }
        });
    }

    private Map<String, RequestBody> initParams(DefectDeleteRecord defectDeleteRecord) {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody defectId = RequestBody.create(MediaType.parse("multipart/form-data"), defectDeleteRecord.getSysDefectId() + "");
        params.put("sysDefectId", defectId);
        //remark
        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), defectDeleteRecord.getRemark());
        params.put("Remark", remark);

        RequestBody CreateDate = RequestBody.create(MediaType.parse("multipart/form-data"), defectDeleteRecord.getCreateDate());
        params.put("CheckedTime", CreateDate);

        if (defectDeleteRecord.getDefectCategory() == 1) {
            RequestBody treeCutCount;
            if (defectDeleteRecord.getTreeCutCount() != null) {
                treeCutCount = RequestBody.create(MediaType.parse("multipart/form-data"), defectDeleteRecord.getTreeCutCount().toString());
            } else {
                treeCutCount = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            }
            params.put("TreeCutCount", treeCutCount);
        }
//        RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), defectDeleteRecord.getPlanDailyPlanSectionIDs() + "");
//        params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);

        if (defectDeleteRecord.getPlanDailyPlanSectionIDs() == null) {
            RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);
        } else {
            RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), defectDeleteRecord.getPlanDailyPlanSectionIDs() + "");
            params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);
        }

//        RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), defectDeleteRecord.());
//        params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        return params;
    }

    /**
     * 以下是上传 到位登记和 位置记录(在OPservice 已在上传)
     */
    //到位登记
    private synchronized void postUploadInplaces() {
        if (isUploadInplaces)
            return;
        isUploadInplaces = true;
        //获取未上传的记录 上传
        List<InPlace> list = InPlaceDBHelper.getInstance().getNeedUploadInplace();
        uploadInplacesSize = list.size();
        for (InPlace inPlace : list) {
            postUploadInplace(inPlace);
        }
        if (uploadInplacesSize == 0) {
            isUploadInplaces = false;
        }
    }

    private void postUploadInplace(final InPlace inPlace) {
        String nearToweid = null;
        if (inPlace.getNearTowerId() != 0) {
            nearToweid = inPlace.getNearTowerId() + "";
        }
        Map<String, RequestBody> params = initParams(inPlace, nearToweid);
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postRegisterInplace(params, requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    inPlace.setUploadFlag(1);//将状态改为已上传
                    InPlaceDBHelper.getInstance().updateInplace(inPlace);

                }
                uploadInplacesSize--;
                if (uploadInplacesSize == 0) {
                    isUploadInplaces = false;
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                mRequestCalls.remove(call);
                uploadInplacesSize--;
                if (uploadInplacesSize == 0) {
                    isUploadInplaces = false;
                }
            }
        });
    }


    private Map<String, RequestBody> initParams(InPlace inPlace, String nearToweid) {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), AppConstant.TOKEN);
        params.put("LoginToken", token);
        RequestBody longtitude = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getLongitude() + "");
        params.put("Longitude", longtitude);
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getLatitude() + "");
        params.put("Latitude", latitude);
        RequestBody altitudeR = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getAltitude() + "");
        params.put("Altitude", altitudeR);
        RequestBody partolType = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getPatrolType());
        params.put("PatrolType", partolType);
        RequestBody towerId = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getSysTowerId() + "");
        params.put("TowerId", towerId);
        if (nearToweid != null) {
            RequestBody nearTowerId = RequestBody.create(MediaType.parse("multipart/form-data"), nearToweid);
            params.put("NearTowerId", nearTowerId);
        } else {
            RequestBody nearTowerId = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getNearTowerId() + "");
            params.put("NearTowerId", nearTowerId);
        }
        RequestBody createDate = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getCreateDate() + "");
        params.put("CreateDate", createDate);
        RequestBody sysUserId = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getSysUserId() + "");
        params.put("sysUserId", sysUserId);

        RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getPlanDailyPlanSectionIDs() + "");
        params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);

        RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), inPlace.getSysPatrolExecutionID() + "");
        params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        return params;
    }


    /**
     * 以下上传 接地电阻
     */
    private synchronized void postUploadEarthResitance() {
        if (isUploadEarthResitance)
            return;
        //获取上传记录
        final List<EarthingResistance> list = EarthingResistanceDBHelper.getInstance().getUploadList();
        if (list.size() == 0) {
            return;
        }
        isUploadEarthResitance = true;
        String jsonStr = mGosn.toJson(list);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class)
                .postEarthingResistance(body);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    for (EarthingResistance earthingResistance : list) {
                        earthingResistance.setUploadFlag(1);
                    }
                    EarthingResistanceDBHelper.getInstance().updateList(list);
                }
                isUploadEarthResitance = false;
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                Log.d("nate", "onFailure: postUploadEarthResitance");
                isUploadEarthResitance = false;

            }
        });
    }

    /**
     * 以下上传 覆冰厚度
     */
    private synchronized void uploadIceCover() {
        if (isUploadIceCover)
            return;
        //获取上传记录
        final List<IceCover> list = IceCoverDBHelper.getInstance().getUploadList();
        if (list.size() == 0) {
            return;
        }
        isUploadIceCover = true;
        String jsonStr = mGosn.toJson(list);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class)
                .postOptIceCover(body);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    for (IceCover iceCover : list) {
                        iceCover.setUploadFlag(1);
                    }
                    IceCoverDBHelper.getInstance().updateList(list);
                }
                isUploadIceCover = false;
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                Log.d("nate", "onFailure: postUploadEarthResitance");
                isUploadIceCover = false;

            }
        });
    }

    /**
     * 上传 红外测温
     */
    private void uploadTemperature() {
        if (isTemperature)
            return;
        final List<InfraredTemperature> list = InfraredTemperatureDBHelper.getInstance().getUploadList();
        if (list.size() == 0) {
            return;
        }
        isTemperature = true;
        //获取Inforared的记录的单元格输入的值,并添加
        for (InfraredTemperature infraredTemperature : list) {
            List<OptTensilePointTemperature> optList = infraredTemperature.getOptTensilePointTemperatureList();
            infraredTemperature.setTensilePointTemperatureList(optList);
        }
        for (final InfraredTemperature infraredTemperature : list) {
            String jsonStr = mGosn.toJson(infraredTemperature);
            Log.i("jsonStr", jsonStr);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
            Call<BaseCallBack<List<ListCallBack<String>>>> call =
                    RetrofitManager.getInstance().getService(ApiService.class)
                            .postInfraredTemperature(body);
            mRequestCalls.add(call);
            call.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
                @Override
                public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                    mRequestCalls.remove(call);
                    /*if (response != null && response.body() != null && response.body().getCode() == 1) {
                        List<ListCallBack<String>> listCallBacks = response.body().getData();
                        for (ListCallBack<String> listCallBack : listCallBacks) {
                            if (listCallBack.getPlatformId() != 0) {
                                InfraredTemperature temperature = list.get(listCallBack.getIndex());
                                temperature.setUploadFlag(1);
                                InfraredTemperatureDBHelper.getInstance().updateInfraredTemperature(temperature);
                            }
                        }
                    }*/
                    if (response.body() != null && response.body().getCode() == 1) {
                        infraredTemperature.setUploadFlag(1);
                        InfraredTemperatureDBHelper.getInstance().updateInfraredTemperature(infraredTemperature);
                    } else {
                        Log.e("responseErr", "onResponse: 红外温度上传失败");
                    }
                    isTemperature = false;
                }

                @Override
                public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                    mRequestCalls.add(call);
                    isTemperature = false;
                }
            });
        }

    }

    /**
     * 以下上传交跨记录
     */
    private void postUploadLineCross() {
        if (isUploadLineCross)
            return;
        isUploadLineCross = true;
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                // 上传新建的交跨 上传修改过的交跨 上传消除记录
                List<LineCrossEntity> lineCrossEntities = LineCrossDBHelper.getInstance().getNeedUploadList();
                upUploadLineCross += lineCrossEntities.size();
                for (LineCrossEntity lineCrossEntity : lineCrossEntities) {
                    postUploadLineCross(lineCrossEntity);
                }
                //交跨消除
                List<LineCrossDelete> deleteList = LineCrossDeleteDBHelper.getInstance().getNeedUploadList();
                for (LineCrossDelete lineCrossDelete : deleteList) {
                    postClearLineCross(lineCrossDelete);
                }
                upUploadLineCross += deleteList.size();

                //交跨修改
                List<LineCrossEntity> list = LineCrossDBHelper.getInstance().getNeedUpdateList();
                for (LineCrossEntity lineCrossEntity : list) {
                    postUpdateLineCross(lineCrossEntity);
                }
                upUploadLineCross += list.size();

                if (upUploadLineCross == 0)
                    isUploadLineCross = false;
            }
        });
    }

    /**
     * 修改交跨     8  CrossImage
     *
     * @param lineCrossEntity
     */
    private void postUpdateLineCross(final LineCrossEntity lineCrossEntity) {
        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(8, lineCrossEntity.getPlatformId() + "", lineCrossEntity.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CrossImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postCrossUpdate(initParams(lineCrossEntity), requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    lineCrossEntity.setUploadFlag(1);
                    //修改状态
                    LineCrossDBHelper.getInstance().updateByPlatFormId(lineCrossEntity, 1);
                }
                uploadInplacesSize--;
                if (upUploadLineCross == 0)
                    isUploadLineCross = false;
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                mRequestCalls.remove(call);
                uploadInplacesSize--;
                if (upUploadLineCross == 0)
                    isUploadLineCross = false;
            }
        });
    }

    private Map<String, RequestBody> initParams(LineCrossEntity lineCrossEntity) {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getCrossLatitude() + "");
        params.put("Latitude", lat);
        RequestBody lng = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getCrossLongitude() + "");
        params.put("Longitude", lng);
        //交跨物类型
        RequestBody brokeType = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getCrossType() + "");
        params.put("CrossType", brokeType);

        RequestBody lineCrossId = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getPlatformId() + "");
        params.put("sysLineCrossId", lineCrossId);
        //距小号侧
        RequestBody company = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getDtoSmartTower() + "");
        params.put("DtoSmartTower", company);

        //净空距离
        RequestBody contact = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getClearance() + "");
        params.put("Clearance", contact);

        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getRemark());
        params.put("Remark", remark);

        RequestBody crossAngle = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getCrossAngle() + "");
        params.put("CrossAngle", crossAngle);

        RequestBody planDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getPlanDailyPlanSectionIDs() + "");
        params.put("PlanDailyPlanSectionIDs", planDailyPlanSectionIDs);

        RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), lineCrossEntity.getSysPatrolExecutionID());
        params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        return params;
    }

    /**
     * 修改交跨    8    CrossImage
     *
     * @param lineCrossEntity
     */
    private void postUploadLineCross(final LineCrossEntity lineCrossEntity) {
        String jsonStr = mGosn.toJson(lineCrossEntity);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);

        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(8, lineCrossEntity.getPlatformId() + "", lineCrossEntity.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CrossImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        Call<BaseCallBack<List<ListCallBack<String>>>> call = RetrofitManager.getInstance().getService(ApiService.class).postCrossRegister(body, requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    ToastUtil.show("提交成功");
                    lineCrossEntity.setUploadFlag(1);
                    lineCrossEntity.setPlatformId(response.body().getData().get(0).getPlatformId());
                    LineCrossDBHelper.getInstance().updateById(lineCrossEntity);
                }
                uploadInplacesSize--;
                if (uploadInplacesSize == 0)
                    isUploadLineCross = false;
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                mRequestCalls.remove(call);
                uploadInplacesSize--;
                if (uploadInplacesSize == 0)
                    isUploadLineCross = false;
            }
        });
    }

    /**
     * 交跨消除     9    ImageData
     *
     * @param lineCrossDelete
     */
    private void postClearLineCross(final LineCrossDelete lineCrossDelete) {
        List<Map<String, Object>> datas = new ArrayList<>();
        datas = ImagePathsDBHelper.getInstance().getImagePaths(9, lineCrossDelete.getSysLineCrossId() + "", lineCrossDelete.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ImageData" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        String json = mGosn.toJson(lineCrossDelete);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        Call<BaseCallBack<List<ListCallBack<String>>>> call = RetrofitManager.getInstance().getService(ApiService.class).postClearCross(body, requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    lineCrossDelete.setUploadFlag(1);
                    LineCrossDeleteDBHelper.getInstance().update(lineCrossDelete);
                }
                uploadInplacesSize--;
                if (uploadInplacesSize == 0)
                    isUploadLineCross = false;
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                uploadInplacesSize--;
                if (uploadInplacesSize == 0)
                    isUploadLineCross = false;
            }
        });
    }

    /**
     * 以下提交杆塔变更登记
     */
    private void postUploadTowerChange() {
        if (isTowerChange)
            return;
        isTowerChange = true;
        List<TowerChange> list = TowerChangeDBHelper.getInstance().getUploadList();
        upTowerChange = list.size();
        for (TowerChange towerChange : list) {
            postUploadTowerChange(towerChange);
        }
        if (upTowerChange == 0)
            isTowerChange = false;
    }

    private void postUploadTowerChange(final TowerChange towerChange) {
        Call<BaseCallBack<TowerChange>> call = RetrofitManager.getInstance().getService(ApiService.class).postUpdateTower(towerChange.getLongitude(),
                towerChange.getLatitude(), towerChange.getAltitude(), towerChange.getSysTowerId(), towerChange.getSysPatrolExecutionID(), towerChange.getPlanDailyPlanSectionIDs());
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<TowerChange>>() {
            @Override
            public void onResponse(Call<BaseCallBack<TowerChange>> call, Response<BaseCallBack<TowerChange>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    towerChange.setUploadFlag(1);
                    TowerChangeDBHelper.getInstance().updateTowerChange(towerChange);
                }
                upTowerChange--;
                if (upTowerChange == 0)
                    isTowerChange = false;
            }

            @Override
            public void onFailure(Call<BaseCallBack<TowerChange>> call, Throwable t) {
                upTowerChange--;
                if (upTowerChange == 0)
                    isTowerChange = false;
            }
        });
    }

    private void postAssistRecordList() {
        if (isUploadAssistRecord) {
            return;
        }
        List<AssistRecord> assistRecords = AssistRecordDBHelper.getInstance().getNeedUpload();
        upUploadAssistRecord = assistRecords.size();
        for (int i = 0; i < assistRecords.size(); i++) {
            postAssistRecordFromDb(assistRecords.get(i));
        }
        if (upUploadAssistRecord == 0)
            isUploadAssistRecord = false;
    }

    @Subscribe
    public void postAssistRecord(final AssistRecord assistRecord) {
        MyApplication.assistRecords.add(assistRecord);
        List<Map<String, Object>> datas = ImagePathsDBHelper.getInstance().getImagePaths(15, "", assistRecord.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("AssistRecordPhoto" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        String jsonStr = mGosn.toJson(assistRecord);
        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postAssistPatrolRecord(body, requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                MyApplication.assistRecords.remove(assistRecord);
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    assistRecord.setIsUpload(true);
                    AssistRecordDBHelper.getInstance().update(assistRecord);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                MyApplication.assistRecords.remove(assistRecord);
            }
        });
    }

    public void postAssistRecordFromDb(final AssistRecord assistRecord) {
        for (int i = 0; i < MyApplication.assistRecords.size(); i++) {
            if (MyApplication.assistRecords.get(i).getId().equals(assistRecord.getId())) {
                return;
            }
        }
        if (assistRecord.getFileList() == null) {
            assistRecord.setFileList(mGosn.fromJson(assistRecord.getFileListJson(), FileList.class));
        }
        MyApplication.assistRecords.add(assistRecord);
        List<Map<String, Object>> datas = ImagePathsDBHelper.getInstance().getImagePaths(15, "", assistRecord.getId());
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("AssistRecordPhoto" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        String jsonStr = mGosn.toJson(assistRecord);
        final RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        Call<BaseCallBack<String>> call = RetrofitManager.getInstance().getService(ApiService.class).postAssistPatrolRecord(body, requestImgParts);
        mRequestCalls.add(call);
        call.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mRequestCalls.remove(call);
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    assistRecord.setIsUpload(true);
                    AssistRecordDBHelper.getInstance().update(assistRecord);
                    upUploadAssistRecord--;
                    if (upUploadAssistRecord == 0)
                        isUploadAssistRecord = false;
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                upUploadAssistRecord--;
                if (upUploadAssistRecord == 0)
                    isUploadAssistRecord = false;
            }
        });
    }
}
