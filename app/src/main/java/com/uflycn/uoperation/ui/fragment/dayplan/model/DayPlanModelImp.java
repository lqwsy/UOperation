package com.uflycn.uoperation.ui.fragment.dayplan.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanDetail;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.DayPlanRequestBean;
import com.uflycn.uoperation.bean.DayPlanSection;
import com.uflycn.uoperation.bean.MonthPlanBean;
import com.uflycn.uoperation.bean.SelectTower;
import com.uflycn.uoperation.bean.WeekPlanBean;
import com.uflycn.uoperation.bean.YearPlanBean;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.DayPlanDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.dayplan.DayPlanListener;
import com.uflycn.uoperation.util.LogUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 每日计划
 */
public class DayPlanModelImp implements DayPlanModel {
    private List<Call> mRequestList = new ArrayList<>();

    @Override
    public void getDayPlanList(boolean isFromNet, int type, final DayPlanListener.DayPlanListListener listener) {
        if (!isFromNet) {
            List<DayPlan> list = DayPlanDBHelper.getInstance().getList();
            if (list.size() == 0) {
                getDayPlanFromNet(type, listener);
            } else {
                listener.onSuccess(list);
            }
        } else {
            getDayPlanFromNet(type, listener);
        }
    }

    private void getDayPlanFromNet(int type, final DayPlanListener.DayPlanListListener listener) {
        Call call = RetrofitManager.getInstance().getService(ApiService.class).getDayPlanList(AppConstant.TOKEN, type);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<List<DayPlan>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<DayPlan>>> call, Response<BaseCallBack<List<DayPlan>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    listener.onSuccess(response.body().getData());
                    DayPlanDBHelper.getInstance().insert(response.body().getData());
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                } else {
                    listener.onFail("网络请求出错");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<DayPlan>>> call, Throwable t) {
//                call.isCanceled()
                if (!call.isCanceled()) {
                    listener.onFail("网络请求出错");
                }
            }
        });
        mRequestList.add(call);
    }

    @Override
    public void getDailyTaskInfo(String planId, final DayPlanListener.DayPlanDetailListener listener) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sysDailyPlanSectionID", planId);
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(jsonObject));
        Call call = RetrofitManager.getInstance().getService(ApiService.class).getDailyTaskInfo(body);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<List<DayPlanInfo>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<DayPlanInfo>>> call, Response<BaseCallBack<List<DayPlanInfo>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    listener.onSuccess(response.body().getData());
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                } else {
                    listener.onFail("网络请求出错");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<DayPlanInfo>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFail("网络请求出错");
                }
            }
        });
        mRequestList.add(call);

    }

    public void getDailyTaskInfoFromResponse(String planId, final DayPlanListener.DayPlanDetaiFromResponselListener listener) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sysDailyPlanSectionID", planId);
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(jsonObject));
        Call call = RetrofitManager.getInstance().getService(ApiService.class).getDailyTaskInfo(body);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<List<DayPlanInfo>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<DayPlanInfo>>> call, Response<BaseCallBack<List<DayPlanInfo>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    listener.onSuccess(response.body().getData());
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                } else {
                    listener.onFail("网络请求出错");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<DayPlanInfo>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFail("网络请求出错");
                }
            }
        });
        mRequestList.add(call);

    }

    @Override
    public void openPlanDailyPlanSection(final DayPlanInfo dayPlanInfo, final DayPlanListener.OpenPlanDailyPlanSectionListener listener) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("sysDailyPlanSectionID", dayPlanInfo.getSysDailyPlanSectionID());
        //开始的经纬度
        Gson gson = new Gson();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(addStartLocation(jsonObject)));
        final Call call = RetrofitManager.getInstance().getService(ApiService.class).OpenPlanDailyPlanSection(body);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<DayPlanSection>>() {
            @Override
            public void onResponse(Call<BaseCallBack<DayPlanSection>> call, Response<BaseCallBack<DayPlanSection>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    listener.onSuccess(response.body().getData(), dayPlanInfo);
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                } else {
                    listener.onFail(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<DayPlanSection>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFail("网络请求出错");
                }
            }
        });
        mRequestList.add(call);

    }

    @Override
    public void closePlanDailyPlanSection(String sysDailyPlanTimeSpanID, List<SelectTower.TowerListBean> datas, final DayPlanListener.ClosePlanDailyPlanSectionListener listener) {
        DayPlanRequestBean bean = new DayPlanRequestBean();
        if (AppConstant.CURRENT_LOCATION != null) {
            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
            bean.setEndLatitude(gps.latitude + "");
            bean.setEndLongitude(gps.longitude + "");
        }
        Gson gson = new Gson();
        bean.setPlanDailyPlanPatrolTowerList(datas);
        bean.setStatus(0);
        bean.setSysDailyPlanTimeSpanID(sysDailyPlanTimeSpanID);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(bean));
        final Call call = RetrofitManager.getInstance().getService(ApiService.class).ClosePlanDailyPlanSection(body);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<DayPlanSection>>() {
            @Override
            public void onResponse(Call<BaseCallBack<DayPlanSection>> call, Response<BaseCallBack<DayPlanSection>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    listener.onSuccess(null);
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                } else {
                    listener.onFail("关闭线路异常");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<DayPlanSection>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFail("网络请求出错");
                }
            }
        });
        mRequestList.add(call);

    }

    @Override
    public void postDayPlanDetailToNet(final DayPlanDetail dayPlanDetail, List<MultipartBody.Part> requestImgParts, final DayPlanListener.PostDayPlanDetailToNetListener listener) {
        Gson gson = new Gson();
        String json = gson.toJson(dayPlanDetail);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        final Call call;
        call = RetrofitManager.getInstance().getService(ApiService.class).updatePlanDailyPlanSection(body, requestImgParts);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<Integer>>() {
            @Override
            public void onResponse(Call<BaseCallBack<Integer>> call, Response<BaseCallBack<Integer>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    if (dayPlanDetail.getStatus() == 1) {
                        DayPlanDBHelper.getInstance().updateDayPlanToFinish(dayPlanDetail.getSysDailyPlanSectionID());
                    }
                    listener.onSuccess(null);
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                } else {
                    listener.onFail("关闭计划异常");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<Integer>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFail("网络请求出错");
                }
            }
        });
        mRequestList.add(call);
    }

    @Override
    public void cancelAll() {
        for (Call call : mRequestList) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }

    @Override
    public void getSelectTowers(String sysDailyPlanTimeSpanID, final DayPlanListener.PostSelectTowers listener) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("LoginToken", AppConstant.TOKEN);
        jsonObject.addProperty("sysDailyPlanTimeSpanID", sysDailyPlanTimeSpanID);
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<BaseCallBack<List<SelectTower>>> call = RetrofitManager.getInstance().getService(ApiService.class)
                .getClosePlanDialyPlanSelectTower(body);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<List<SelectTower>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<SelectTower>>> call, Response<BaseCallBack<List<SelectTower>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    listener.onSuccess(response.body().getData());
                } else if (response.body() != null && response.body().getCode() == 401) {
                    listener.onFail("登录失效");
                } else {
                    listener.onFail("获取列表失败!");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<SelectTower>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    listener.onFail("网络连接失败!");
                }
            }
        });
    }

    @Override
    public void getYearPlanList(final DayPlanListener.PostYearPlanList postYearPlanList) {
        Call<BaseCallBack<List<YearPlanBean>>> call = RetrofitManager.getInstance().getService(ApiService.class)
                .getYearPlanList(AppConstant.TOKEN);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<List<YearPlanBean>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<YearPlanBean>>> call, Response<BaseCallBack<List<YearPlanBean>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    postYearPlanList.onSuccess(response.body().getData());
                } else if (response.body() != null && response.body().getCode() == 401) {
                    postYearPlanList.onFail("登录失效");
                } else {
                    postYearPlanList.onFail("获取列表失败!");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<YearPlanBean>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    postYearPlanList.onFail("网络连接失败");
                }
            }
        });
    }

    @Override
    public void getWeekPlanList(final DayPlanListener.PostWeekPlanList postWeekPlanList) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ClassID", "");
        jsonObject.addProperty("StartDate", "");
        jsonObject.addProperty("EndDate", "");
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(jsonObject));
        Call<BaseCallBack<List<WeekPlanBean>>> call = RetrofitManager.getInstance()
                .getService(ApiService.class)
                .getWeekPlanList(requestBody);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<List<WeekPlanBean>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<WeekPlanBean>>> call, Response<BaseCallBack<List<WeekPlanBean>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    postWeekPlanList.onSuccess(response.body().getData());
                } else if (response.body() != null && response.body().getCode() == 401) {
                    postWeekPlanList.onFail("登录失效");
                } else {
                    postWeekPlanList.onFail("获取列表失败!");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<WeekPlanBean>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    postWeekPlanList.onFail("网络连接失败!");
                }
            }
        });
    }

    @Override
    public void getMonthPlanList(final DayPlanListener.PostMonthPlanList postMonthPlanList) {
        Call<BaseCallBack<List<MonthPlanBean>>> call = RetrofitManager.getInstance().getService(ApiService.class)
                .getMonthPlanList(AppConstant.TOKEN);
        cancelAll();
        mRequestList.add(call);
        call.enqueue(new Callback<BaseCallBack<List<MonthPlanBean>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<MonthPlanBean>>> call, Response<BaseCallBack<List<MonthPlanBean>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    postMonthPlanList.onSuccess(response.body().getData());
                } else if (response.body() != null && response.body().getCode() == 401) {
                    postMonthPlanList.onFail("登录失效");
                } else {
                    postMonthPlanList.onFail("获取列表失败!");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<MonthPlanBean>>> call, Throwable t) {
                if (!call.isCanceled()) {
                    postMonthPlanList.onFail("网络连接失败!");
                }
            }
        });
    }


    private JsonObject addStartLocation(JsonObject jsonObject) {
        if (MyApplication.currentNearestTower != null) {
            jsonObject.addProperty("StartTowerID", MyApplication.currentNearestTower.getSysTowerID());
        }
        if (AppConstant.CURRENT_LOCATION != null) {
            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
            jsonObject.addProperty("StartLongitude", gps.longitude);
            jsonObject.addProperty("StartLatitude", gps.latitude);
        }
        jsonObject.addProperty("StartMinDistance", MyApplication.nearestDistance);
        return jsonObject;
    }

    private JsonObject addStopLocation(JsonObject jsonObject) {
        if (MyApplication.currentNearestTower != null) {
            jsonObject.addProperty("EndTowerID", MyApplication.currentNearestTower.getSysTowerID());
        }
        if (AppConstant.CURRENT_LOCATION != null) {
            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
            jsonObject.addProperty("EndLongitude", gps.longitude);
            jsonObject.addProperty("EndLatitude", gps.latitude);
        }
        jsonObject.addProperty("EndMinDistance", MyApplication.nearestDistance);
        return jsonObject;
    }

}
