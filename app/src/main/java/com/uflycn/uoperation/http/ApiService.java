package com.uflycn.uoperation.http;

import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.BrokenInspectRecord;
import com.uflycn.uoperation.bean.BrokenType;
import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanFromWeekPlanBean;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.DayPlanSection;
import com.uflycn.uoperation.bean.DefectCount;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.DefectListBean;
import com.uflycn.uoperation.bean.DefectRemark;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.Document;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.JobContent;
import com.uflycn.uoperation.bean.LineBean;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.LineCrossType;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.MonthPlanBean;
import com.uflycn.uoperation.bean.NeedUpdateTable;
import com.uflycn.uoperation.bean.OperationDictionary;
import com.uflycn.uoperation.bean.Organizition;
import com.uflycn.uoperation.bean.PlanPatrolExecution;
import com.uflycn.uoperation.bean.PlanPatrolExecutionTowerList;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordList;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;
import com.uflycn.uoperation.bean.ReceiveBean;
import com.uflycn.uoperation.bean.Role;
import com.uflycn.uoperation.bean.RoleMenuData;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.bean.StartedTask;
import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TowerChange;
import com.uflycn.uoperation.bean.User;
import com.uflycn.uoperation.bean.UserRole;
import com.uflycn.uoperation.bean.VirtualTower;
import com.uflycn.uoperation.bean.WeekPlanBean;
import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.bean.WorksheetApanageTask;
import com.uflycn.uoperation.bean.YearPlanBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;

/**
 * Created by xiaoyehai on 2017/9/1.
 */
public interface ApiService {


    /**
     * 上传巡检位置信息
     */
    @POST("Operation/UploadPatrolTrack")
    Call<BaseCallBack<List<ListCallBack<String>>>> postPatrolTrack(@Body RequestBody requestBody);

    /**
     * 获取外破专档列表
     */
    @POST("OptBrokenDocument/List")
    @FormUrlEncoded
    Call<BaseCallBack<List<BrokenDocument>>> getBrokenDoucumentList(@Field("LineId") String lineid,
                                                                    @Field("TowerId") String towerId,
                                                                    @Field("LineName") String strLineName,
                                                                    @Field("PageIndex") int pageIndex,
                                                                    @Field("PageSize") int pageSize,
                                                                    @Field("OrderField") String orderField,
                                                                    @Field("sysPatrolExecutionID") String sysPatrolExecutionID,
                                                                    @Field("PlanDailyPlanSectionIDs") String planDailyPlanSectionIDs);

    /**
     * 外破建档retrofit
     */
    @POST("OptBrokenDocument/Create")
    @Multipart
    Call<BaseCallBack<List<ListCallBack<BrokenDocument>>>> postCreateDocument(@Part("JsonData") RequestBody requestbody, @Part List<MultipartBody.Part> request_img_part);

    /**
     * w修改外破档案
     *
     * @param params
     * @param request_img_part
     * @return
     */
    @POST("Operation/UpdateBrokenDocument")
    @Multipart
    Call<BaseCallBack<String>> postUpdateBrokenDocument(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);


    /**
     * 获取外破类型
     */
    @POST("Common/GetDictionaryByCategory")
    @FormUrlEncoded
    Call<BaseCallBack<List<BrokenType>>> getBrokenType(@Field("Category") String catagory);

    /**
     * 下载缩略图压缩包
     *
     * @return
     */
    @POST("Common/DownloadThumbs")
    @FormUrlEncoded
    @Streaming
    Call<ResponseBody> getImageZip(@Field("Category") int Catagory,
                                   @Field("Id") int Id);

    /**
     * 根据缩略图名称id  下载大图
     *
     * @return
     */
    @POST("Common/DownloadFile")
    @FormUrlEncoded
    @Streaming
    Call<ResponseBody> getImageById(@Field("sysFileInfoId") int sysFileInfoId);

    /**
     * 获取交跨类型
     */
    @POST("Common/DefectType")
    @FormUrlEncoded
    Call<BaseCallBack<List<LineCrossType>>> getLineCrossType(@Field("Group") String group, @Field("Category") int catagory);

    /**
     * 获取所有的线路
     */
    @POST("Operation/ScheduleTaskList")
    @FormUrlEncoded
    Call<BaseCallBack<List<Gridline>>> getAllGridline(@Field("UserName") String uesername, @Field("LineName") String lineName);

    /**
     * 开启巡视
     */
    @POST("Operation/ExecuteTask")
    @FormUrlEncoded
    Call<BaseCallBack<String>> postStartInspect(@Field("LineId") String lineId);

    /**
     * 我的巡视任务
     */
    @POST("Operation/MyTaskList")
    @FormUrlEncoded
    Call<BaseCallBack<List<StartedTask>>> getstartedGridline(@Field("Query") String query);


    /**
     * 交跨登记
     *
     * @param json
     * @return
     */
    @POST("OptLineCross/Create")
    @Multipart
    Call<BaseCallBack<List<ListCallBack<String>>>> postCrossRegister(@Part("JsonData") RequestBody json, @Part List<MultipartBody.Part> request_img_part);

    /**
     * 交跨修改
     * Operation/UpdateLineCross
     *
     * @param params
     * @param request_img_part
     * @return
     */
    @POST("OptLineCross/Update")
    @Multipart
    Call<BaseCallBack<String>> postCrossUpdate(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);

    /**
     * 外破巡视登记
     *
     * @param params
     * @param request_img_part
     * @return
     */
    @POST("Operation/AddBrokenPatrolDetail")
    @Multipart
    Call<BaseCallBack<String>> postBrokenPatrolRegister(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);


    /**
     * 根据线路id获取杆塔列表
     *
     * @param lineid
     * @return
     */
    @POST("GridLine/GetTowerList")
    @FormUrlEncoded
    Call<BaseCallBack<List<Tower>>> getTowerByLineid(@Field("GridLineId") String lineid);

    //    /**
    //     * 到位登记
    //     *
    //     * @param longitude
    //     * @param latitude
    //     * @param altitude
    //     * @param type
    //     * @param towerid
    //     * @param nearTowerid
    //     * @param createDate
    //     * @param userID
    //     * @param planDailyPlanSectionIDs
    //     * @return
    //     */
    //    @POST("Operation/RegisterInPlace")
    //    @Multipart
    //    Call<BaseCallBack<String>> postRegisterInplace(@Field("Longitude") double longitude,
    //                                                   @Field("Latitude") double latitude,
    //                                                   @Field("Altitude") double altitude,
    //                                                   @Field("PatrolType") String type,
    //                                                   @Field("TowerId") int towerid,
    //                                                   @Field("NearTowerId") String nearTowerid,
    //                                                   @Field("CreateDate") String createDate,
    //                                                   @Field("sysUserId") String userID,
    //                                                   @Field("PlanDailyPlanSectionIDs") String planDailyPlanSectionIDs);

    @POST("Operation/RegisterInPlace")
    @Multipart
    Call<BaseCallBack<String>> postRegisterInplace(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);

    /**
     * 接地电阻
     *
     * @return
     */
    @POST("OptEarthingResistance/Create")
    Call<BaseCallBack<String>> postEarthingResistance(@Body RequestBody requestBody);

    /**
     * 接地电阻
     *
     * @return
     */
    @POST("OptIceCover/Create")
    Call<BaseCallBack<String>> postOptIceCover(@Body RequestBody requestBody);


    /**
     * 红外测温
     *
     * @param map
     * @return
     */
    @POST("Operation/AddInfraredTemperature")
    @FormUrlEncoded
    Call<BaseCallBack<String>> postInfraredTemperature(@FieldMap Map<String, String> map);

    /**
     * 红外测温
     *
     * @param json
     * @return
     */
    @POST("OptInfraredTemperature/Create")
    Call<BaseCallBack<List<ListCallBack<String>>>> postInfraredTemperature(@Body RequestBody json);


    /**
     * 零值检测
     *
     * @param json
     * @return
     */
    @POST("OptZeroDetection/Create")
    Call<BaseCallBack<List<ListCallBack<String>>>> postZeroDetection(@Body RequestBody json);

    /**
     * 获取缺陷类型
     *
     * @return
     */
    @POST("Common/DefectType")
    @FormUrlEncoded
    Call<DefectType> getBrokenType(@Field("Group") String Group, @Field("Category") String Category);

    /**
     * 获取缺陷类型
     *
     * @return
     */
    @POST("Common/GetDefectTypeByParentId")
    @FormUrlEncoded
    Call<BaseCallBack<List<DefectType>>> GetDefectTypeByParentId(@Field("Group") String Group, @Field("ParentId") int ParentId);

    /**
     * /**
     * 上传缺陷
     *
     * @param params
     * @param request_img_part
     * @return
     */
    @POST("Operation/AddTowerDefect")
    //    @Streaming
    @Multipart
    Call<BaseCallBack<ListCallBack>> postAddTowerDefect(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);


    /**
     * 根据线路id获取缺陷信息
     *
     * @param gridLineId
     * @param category
     * @param status
     * @return
     */
    @POST("GridLine/GetLineDefectList")
    @FormUrlEncoded
    Call<DefectInfo> getLineDefectList(@Field("LineId") String gridLineId,
                                       @Field("Category") String category,
                                       @Field("Status") String status,
                                       @Field("VoltageClass") String voltageClass,
                                       @Field("LineName") String lineName,
                                       @Field("TowerId") String towerId,
                                       @Field("NearTowerId") String nearTowerId,
                                       @Field("sysPatrolExecutionID") String sysPatrolExecutionID,
                                       @Field("PlanDailyPlanSectionIDs") String planDailyPlanSectionIDs);


    /**
     * 通道缺陷消除
     *
     * @param params
     * @return
     */
    @POST("DefectManage/ClearDefect")
    @Multipart
    Call<BaseCallBack<String>> postClearDefect(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);


    /**
     * 树障缺陷消除
     *
     * @param params
     * @return
     */
    @POST("DefectManage/ClearTreeDefect")
    @Multipart
    Call<BaseCallBack<String>> postClearTreeDefect(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);

    /**
     * 下载图片
     *
     * @param category
     * @param id
     * @return
     */
    @POST("Common/DownloadPicture")
    @FormUrlEncoded
    Call<ResponseBody> downloadPicture(@Field("Category") String category, @Field("Id") String id, @Field("Index") String index);


    /**
     * 添加缺陷隐患巡检备注
     *
     * @param category
     * @param defectId
     * @param remark
     * @return
     */
    @POST("DefectManage/AddDefectVerification")
    @FormUrlEncoded
    Call<BaseCallBack<String>> postDefectVerification(@Field("Category") String category,
                                                      @Field("DefectId") int defectId,
                                                      @Field("Remark") String remark,
                                                      @Field("sysPatrolExecutionID") String sysPatrolExecutionID,
                                                      @Field("PlanDailyPlanSectionIDs") String planDailyPlanSectionIDs);


    /**
     * 杆塔变更登记
     */
    @POST("Operation/TowerChanged")
    @FormUrlEncoded
    Call<BaseCallBack<TowerChange>> postUpdateTower(@Field("Longitude") double longitude,
                                                    @Field("Latitude") double latitude,
                                                    @Field("Altitude") double altitude,
                                                    @Field("TowerId") int towerid,
                                                    @Field("sysPatrolExecutionID") String sysPatrolExecutionID,
                                                    @Field("PlanDailyPlanSectionIDs") String planDailyPlanSectionIDs);

    /**
     * 获取外破特巡记录
     */
    @POST("Operation/BrokenPatrolDetailList")
    @FormUrlEncoded
    Call<BaseCallBack<List<BrokenInspectRecord>>> getBrokenRecordList(@Field("DocumentId") int documentId);

    /*
     * 获取交跨列表
     * 已废弃
     */
    //
    //    @POST("Operation/LineCrossList")
    //    @FormUrlEncoded
    //    Call<BaseCallBack<List<LineCrossEntity>>> getLineCrossList(@Field("VoltageClass") String voltageClass,
    //                                                               @Field("LineName") String lineName,
    //                                                               @Field("LineId") String lineid, @Field("TowerId") String towerid);

    /**
     * 获取交跨列表
     */
    @POST("OptLineCross/List")
    @FormUrlEncoded
    Call<BaseCallBack<List<LineCrossEntity>>> getLineCrossList(@Field("LineId") String LineId,
                                                               @Field("TowerId") String TowerId,
                                                               @Field("EndTowerId") String EndTowerId,
                                                               @Field("sysPatrolExecutionID") String sysPatrolExecutionID,
                                                               @Field("PlanDailyPlanSectionIDs") String planDailyPlanSectionIDs);

    /*
     * 获取缺陷备注列表
     */
    @POST("DefectManage/DefectVerificationList")
    @FormUrlEncoded
    Call<BaseCallBack<List<DefectRemark>>> getDefectRemarkList(@Field("Category") String category,
                                                               @Field("DefectId") int defectId);

    /**
     * @param Title 无用参数
     * @return
     */
    @POST("Common/GetMessageInfo")
    @FormUrlEncoded
    Call<BaseCallBack<List<TempTask>>> getTempTaskList(@Field("Title") String Title);


    /**
     * 获取外破类型
     */
    @POST("Common/Dictionary")
    @FormUrlEncoded
    Call<BaseCallBack<List<OperationDictionary>>> getCrossType(@Field("FullName") String fullName);

    @POST("Common/ReadMessageInfo")
    @FormUrlEncoded
    Call<BaseCallBack<String>> readMessageInfo(@Field("sysMessageInfoId") int sysMessageInfoId);


    /**
     * 新增项目管理
     */
    @POST("OptProjectManage/Create")
    @Multipart
    Call<BaseCallBack<List<ListCallBack<ProjectEntity>>>> postCreateProject(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);


    @POST("OptProjectManage/List")
    @FormUrlEncoded
    Call<BaseCallBack<List<ProjectEntity>>> getProjectList(@Field("LineId") String lineidFs,
                                                           @Field("TowerId") String TowerId,
                                                           @Field("ProjectName") String projectName,
                                                           @Field("VoltageClass") String voltage);


    /**
     * 新增巡视登记
     */
    @POST("OptProjectManage/Inspect")
    @Multipart
    Call<BaseCallBack<String>> postInspectProject(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);


    @POST("OptProjectManage/InspectList")
    @FormUrlEncoded
    Call<BaseCallBack<List<ProjectInspection>>> getProjectInspectList(@Field("ProjectId") int projectid);

    /**
     * 巡视修改
     */
    @POST("OptProjectManage/Update")
    @Multipart
    Call<BaseCallBack<String>> postUpdateProject(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> request_img_part);


    /**
     * 判断是否需要更新基础表
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("Common/CheckSyncData")
    Call<BaseCallBack<List<NeedUpdateTable>>> postNeedUpdate(@Body RequestBody json);

    /**
     * @param lineids 无用参数
     */
    @POST("ItemDetails/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<ItemDetail>>> postUpdateItemDetails(@Field("LineId") String lineids);


    /**
     * @param lineids 无用参数
     */
    @POST("GridLine/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<Gridline>>> postUpdateGridLine(@Field("LineId") String lineids);

    /**
     * @param lineids 无用参数
     */
    @POST("Tower/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<Tower>>> postUpdateTowerList(@Field("LineId") String lineids);


    /**
     * @param lineids 无用参数
     */
    @POST("DefectType/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<DefectType>>> postUpdateDefectType(@Field("LineId") String lineids);


    /**
     * @param lineids 无用参数
     */
    @POST("Users/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<User>>> postUpdateUsers(@Field("LineId") String lineids);

    /**
     * @param lineids 无用参数
     */
    @POST("Organization/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<Organizition>>> postUpdateOrgnizitions(@Field("LineId") String lineids);

    /**
     * @param lineids 无用参数
     */
    @POST("Roles/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<Role>>> postUpdateRole(@Field("LineId") String lineids);

    /**
     * @param lineids 无用参数
     */
    @POST("UserRole/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<UserRole>>> postUpdateUserRole(@Field("LineId") String lineids);

    /**
     * @param lineids 无用参数
     */
    @POST("RoleMenuData/SyncData")
    @FormUrlEncoded
    Call<BaseCallBack<List<RoleMenuData>>> postUpdateRoleMenuData(@Field("LineId") String lineids);


    /**
     * 获取检索距离（参数无意义）
     *
     * @param radius 不用传
     * @return
     */
    @POST("Common/TowerRadius")
    @FormUrlEncoded
    Call<BaseCallBack<String>> getTowerRadius(@Field("radius") String radius);


    /**
     * 清除交跨
     */
    @POST("OptLineCross/Clear")
    @Multipart
    Call<BaseCallBack<List<ListCallBack<String>>>> postClearCross(@Part("JsonData") RequestBody requestbody, @Part List<MultipartBody.Part> request_img_part);


    /**
     * 获取输电手册文档
     *
     * @param fileName 文档关键字
     * @return
     */
    @POST("TechnicalFile/List")
    @FormUrlEncoded
    Call<BaseCallBack<List<Document>>> getDocument(@Field("FileName") String fileName);


    /**
     * 下载缩略图压缩包
     *
     * @return
     */
    @POST("Common/DownloadFile")
    @FormUrlEncoded
    @Streaming
    Call<ResponseBody> getFile(@Field("sysFileInfoId") int Catagory);


    /**
     * 根据线路获取缺陷数
     *
     * @param TowerId
     * @param Category
     * @param Status
     * @return
     */
    @POST("GridLine/GetLineDefectCount")
    @FormUrlEncoded
    Call<DefectCount> getLineDefectCount(@Field("TowerId") int TowerId,
                                         @Field("Category") int Category,
                                         @Field("GridLineId") String GridLineId,
                                         @Field("Status") int Status);

    /**
     * 获取线路缺陷数
     *
     * @param GridLineId
     * @param Category
     * @param Status
     * @param GroupByLine
     * @return
     */
    @POST("GridLine/GetLineDefectCount")
    @FormUrlEncoded
    Call<BaseCallBack<List<DefectCount>>> getLineDefectCount(@Field("GridLineId") String GridLineId,
                                                             @Field("Category") int Category,
                                                             @Field("Status") int Status,
                                                             @Field("GroupByLine") boolean GroupByLine);

    @POST("WorksheetTask/List")
    Call<BaseCallBack<List<WorkSheetTask>>> getWorksheetTask();

    @POST("WorksheetTask/Receive")
    @FormUrlEncoded
    Call<BaseCallBack<ReceiveBean>> sendReceive(@Field("sysTaskId") int sysTaskId);

    @POST("WorksheetTask/CreateApanageTask")
    Call<BaseCallBack<WorksheetApanageTask>> createApanageTask(@Body RequestBody json);

    @POST("WorksheetTask/ApanageDefectList")
    @FormUrlEncoded
    Call<BaseCallBack<DefectListBean>> getDefectList(@Field("TowerId") int TowerId,
                                                     @Field("NearTowerId") int NearTowerId);

    @POST("WorksheetTask/MyApanageTask")
    Call<BaseCallBack<List<WorksheetApanageTask>>> getMyWorksheetApanageTask();

    @POST("WorksheetTask/DiscussApanageTask")
    @FormUrlEncoded
    Call<BaseCallBack<WorksheetApanageTask>> sendDiscussApanageTask(@Field("sysApanageTaskId") int sysApanageTaskId);

    @POST("WorksheetTask/ClearApanageTask")
    @Multipart
    Call<BaseCallBack<String>> clearApanageTask(@Part List<MultipartBody.Part> request_img_part);

    @POST("Operation/TowerChangedList")
    @FormUrlEncoded
    Call<BaseCallBack<List<VirtualTower>>> towerChangedList(@Field("UserId") String userId);

    //-------日计划--------------------
    @POST("DailyPlan/DailyTaskList")
    @FormUrlEncoded
    Call<BaseCallBack<List<DayPlan>>> getDayPlanList(@Field("LoginToken") String token, @Field("type") int type);

    //年计划列表
    @POST("AnnualPlan/AnnualTaskList")
    @FormUrlEncoded
    Call<BaseCallBack<List<YearPlanBean>>> getYearPlanList(@Field("LoginToken") String token);

    //周计划列表
    @POST("WeeklyPlan/WeeklyTaskList")
    Call<BaseCallBack<List<WeekPlanBean>>> getWeekPlanList(@Body RequestBody requestBody);

    //月计划
    @POST("MonthlyPlan/MonthlyTaskList")
    @FormUrlEncoded
    Call<BaseCallBack<List<MonthPlanBean>>> getMonthPlanList(@Field("LoginToken") String token);


    @POST("DailyPlan/DailyTaskInfo")
    Call<BaseCallBack<List<DayPlanInfo>>> getDailyTaskInfo(@Body RequestBody requestBody);

    //开启巡视
    @POST("DailyPlan/OpenPlanDailyPlanSection")
    Call<BaseCallBack<DayPlanSection>> OpenPlanDailyPlanSection(@Body RequestBody requestBody);

    //获取巡视杆塔信息,关闭巡视中的杆塔信息
    @POST("DailyPlan/SelectTower")
    Call<BaseCallBack<List<SelectTower>>> getClosePlanDialyPlanSelectTower(@Body RequestBody requestBody);

    @POST("DailyPlan/DailyTaskClose")
    @Multipart
    Call<BaseCallBack<List<Integer>>> updatePlanDailyPlanSection(@Part("JsonData") RequestBody requestbody,
                                                                 @Part List<MultipartBody.Part> request_img_part);


    @POST("DailyPlan/ClosePlanDailyPlanSection")
    Call<BaseCallBack<List<DayPlanSection>>> ClosePlanDailyPlanSection(@Body RequestBody requestBody);

    @POST("DailyPlan/GetJobContentByTypeOfWork")
    @FormUrlEncoded
    Call<BaseCallBack<List<JobContent>>> getJobContent(@Field("LoginToken") String token, @Field("workType") String workTypeId);

    //获取添加线路的所有线路
    @POST("DailyPlan/GetLineDefectNumber")
    @FormUrlEncoded
    Call<BaseCallBack<List<LineBean>>> getLineBeans(@Field("VoltageClass") String voltage, @Field("LineName") String lineName);

    //添加日计划
    @POST("DailyPlan/Create")
    Call<BaseCallBack<String>> postAddDayPlan(@Body RequestBody requestBody);

    //从周计划中获取的bean
    @POST("DailyPlan/AddDailyPlanFromWeeklyPlan")
    Call<BaseCallBack<List<DayPlanFromWeekPlanBean>>> getDayPlanFromWeekList(@Body RequestBody requestBody);


    @POST("OptAssistPatrolRecord/Create")
    @Multipart
    Call<BaseCallBack<String>> postAssistPatrolRecord(@Part("JsonData") RequestBody requestbody, @Part List<MultipartBody.Part> request_img_part);


    @POST("PlanPatrolExecution/PlanPatrolExecutionList")
    Call<BaseCallBack<List<PlanPatrolExecution>>> getPlanPatrolExecutionList();

    /**
     * 任务详情
     *
     * @param sysPatrolExecutionID
     * @return
     */
    @POST("PlanPatrolExecution/PlanPatrolExecutionWorkRecordInfo")
    @FormUrlEncoded
    Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordInfo>>> getPlanPatrolExecutionWorkRecordInfo(@Field("sysPatrolExecutionID") String sysPatrolExecutionID);

    @POST("PlanPatrolExecution/LineTowerList")
    @FormUrlEncoded
    Call<BaseCallBack<List<PlanPatrolExecutionTowerList>>> getPlanPatrolExecutionLineTowerList(@Field("sysPatrolExecutionID") String sysPatrolExecutionID);

    @POST("PlanPatrolExecution/PlanPatrolExecutionWorkRecordList")
    Call<BaseCallBack<List<PlanPatrolExecutionWorkRecordList>>> getPlanPatrolExecutionWorkRecordList();


    @POST("PlanPatrolExecution/PlanPatrolExecutionWorkRecordSubmit")
    @Multipart
    Call<BaseCallBack<List<String>>> postPlanPatrolExecutionWorkRecordSubmit(@Part("JsonData") RequestBody requestbody,
                                                                             @Part List<MultipartBody.Part> request_img_part);

    /**
     * 新建树障
     */
    @POST("TreeProject/CreateDefectPointFromApp")
    @Multipart
    Call<BaseCallBack<ListCallBack>> postAddTreeBarrierDefect(@Part("JsonData") RequestBody requestbody, @Part List<MultipartBody.Part> request_img_part);

}





