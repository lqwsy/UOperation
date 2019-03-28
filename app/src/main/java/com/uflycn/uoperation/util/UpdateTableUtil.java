package com.uflycn.uoperation.util;

import android.util.Log;

import com.google.gson.Gson;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.BrokenInspectRecord;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.DefectRemark;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.NeedUpdateTable;
import com.uflycn.uoperation.bean.Organizition;
import com.uflycn.uoperation.bean.RecordSyncInfo;
import com.uflycn.uoperation.bean.Role;
import com.uflycn.uoperation.bean.RoleMenuData;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.bean.User;
import com.uflycn.uoperation.bean.UserRole;
import com.uflycn.uoperation.bean.VirtualTower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.UpdateTableEvent;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.greendao.BrokenInspectRecordDBHelper;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.DefectRemarkDBHelper;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.greendao.OrganizitionDbHelper;
import com.uflycn.uoperation.greendao.RecordSyncInfoDBHelper;
import com.uflycn.uoperation.greendao.RoleDBHelp;
import com.uflycn.uoperation.greendao.RoleMenuDataDBHelp;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.greendao.TreeDefectDBHelper;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.greendao.UserRoleDBHelp;
import com.uflycn.uoperation.greendao.VirtualTowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.listeners.CommonCallback;
import com.xflyer.utils.ThreadPoolUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.EventBusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/18.
 */
public class UpdateTableUtil {
    private RetrofitManager mRetrofitManager;
    private Gson mGson;
    private List<Call> mRequestList = new ArrayList<>();

    /**
     * 需要更新表的数量
     */
    private int mNeedUpdateCount;

    /**
     * 已更新表的数量
     */
    private int hasUpdateCount;

    private UpdateTableUtil() {
        mGson = new Gson();
        mRetrofitManager = RetrofitManager.getInstance();
    }

    public static UpdateTableUtil getInstance() {
        return SingletonHolder.sInstance;
    }

    /**
     * 发送判断这些表是否需要更新,只会执行一次
     */
    public void postGetNeedUpdateRequest(List<RecordSyncInfo> list) {
        final String requestBean = mGson.toJson(list);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestBean);
        Call<BaseCallBack<List<NeedUpdateTable>>> call = mRetrofitManager.getService(ApiService.class).postNeedUpdate(body);
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<NeedUpdateTable>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<NeedUpdateTable>>> response) {
                if (response.body().getCode() == 0) {
                    //ToastUtil.show("获取平台数据失败");
                    updateFail();

                } else {
                    postUpdateTables(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();

            }
        });
    }

    public void cancelAll() {
        for (Call call : mRequestList) {
            if (!call.isCanceled()) {
                call.cancel();
            }
        }
    }

    public void loadVirtualTowers() {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                Call call = mRetrofitManager.getInstance().getService(ApiService.class).towerChangedList(AppConstant.currentUser.getUserId());
                call.enqueue(new CommonCallback<BaseCallBack<List<VirtualTower>>>() {
                    @Override
                    public void handleResponse(Response<BaseCallBack<List<VirtualTower>>> response) {
                        if (response != null && response.body() != null && response.body().getCode() == 1) {
                            saveVirtualTowers(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        super.onFailure(call, t);
                    }
                });
                mRequestList.add(call);
            }
        });
    }

    private void saveVirtualTowers(final List<VirtualTower> towers) {
        if (towers == null || towers.size() <= 0) return;
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                for (VirtualTower tower : towers) {
                    VirtualTowerDBHelper.getInstance().insertOrUpdate(tower);
                }
            }
        });
    }

    private static class SingletonHolder {
        private static UpdateTableUtil sInstance = new UpdateTableUtil();
    }


    private void postUpdateTables(List<NeedUpdateTable> list) {
        for (NeedUpdateTable table : list) {
            if (table.isUpdated()) {
                mNeedUpdateCount++;
            }
        }
        if (mNeedUpdateCount == 0) {
            EventBus.getDefault().post(new UpdateTableEvent(1));
            return;
        }
        for (NeedUpdateTable table : list) {
            if (table.isUpdated()) {
                postUpdateTable(table);
            }
        }
    }


    private void postUpdateTable(NeedUpdateTable table) {

        switch (table.getCategory()) {
            case 1:
                postUpdateItemDetals();
                break;
            case 2:
                postUpdateGridLines();
                break;
            case 3:
                postUpdateTowerList();
                break;
            case 4:
                postUpdateDefectType();
                break;
            case 5:
                postUpdateUser();
                break;
            case 6:
                postUpdateOrgnizition();
                break;
            case 7:
                //更新角色表
                postUpdateRole();
                break;
            case 8:
                //更新人物角色表
                postUpdateUserRole();
                break;
            case 9:
                //更新角色权限
                postUpdateRoleMenuData();
                break;
        }
    }


    //更新数据字典表
    private void postUpdateItemDetals() {
        Call call = mRetrofitManager.getService(ApiService.class).postUpdateItemDetails("ItemDetails");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<ItemDetail>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<ItemDetail>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateItemDetais(response.body().getData());

                } else {
                    updateFail();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }

    //更新线路信息表
    private void postUpdateGridLines() {
        Call<BaseCallBack<List<Gridline>>> call = mRetrofitManager.getService(ApiService.class).postUpdateGridLine("GridLines");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<Gridline>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<Gridline>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateGridLine(response.body().getData());
                } else {
                    updateFail();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }

    //更新杆塔信息表
    private void postUpdateTowerList() {
        Call<BaseCallBack<List<Tower>>> call = mRetrofitManager.getService(ApiService.class).postUpdateTowerList("GridLines");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<Tower>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<Tower>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateTower(response.body().getData());
                } else {
                    updateFail();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }

    //更新 缺陷种类表
    private void postUpdateDefectType() {
        Call<BaseCallBack<List<DefectType>>> call = mRetrofitManager.getService(ApiService.class).postUpdateDefectType("DefectType");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<DefectType>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<DefectType>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateDefectType(response.body().getData());
                } else {
                    updateFail();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }

    //更新 用户表
    private void postUpdateUser() {
        Call<BaseCallBack<List<User>>> call = mRetrofitManager.getService(ApiService.class).postUpdateUsers("DefectType");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<User>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<User>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateUser(response.body().getData());
                } else {
                    updateFail();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }

    //更新部门表
    private void postUpdateOrgnizition() {
        Call<BaseCallBack<List<Organizition>>> call = mRetrofitManager.getService(ApiService.class).postUpdateOrgnizitions("DefectType");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<Organizition>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<Organizition>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateOrgnize(response.body().getData());
                } else {
                    updateFail();

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }

    //更新角色表
    private void postUpdateRole() {
        Call<BaseCallBack<List<Role>>> call = mRetrofitManager.getService(ApiService.class).postUpdateRole("DefectType");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<Role>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<Role>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateRole(response.body().getData());
                } else {
                    updateFail();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }


    //更新人物角色表
    private void postUpdateUserRole() {
        Call<BaseCallBack<List<UserRole>>> call = mRetrofitManager.getService(ApiService.class).postUpdateUserRole("DefectType");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<UserRole>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<UserRole>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateUserRole(response.body().getData());
                } else {
                    updateFail();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }

    //更新校色权限
    private void postUpdateRoleMenuData() {
        Call<BaseCallBack<List<RoleMenuData>>> call = mRetrofitManager.getService(ApiService.class).postUpdateRoleMenuData("DefectType");
        mRequestList.add(call);
        call.enqueue(new CommonCallback<BaseCallBack<List<RoleMenuData>>>() {
            @Override
            public void handleResponse(Response<BaseCallBack<List<RoleMenuData>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateRoleMenuData(response.body().getData());
                } else {
                    updateFail();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                updateFail();
            }
        });
    }


    private void updateItemDetais(final List<ItemDetail> list) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                ItemDetailDBHelper.getInstance().clearAll();
                ItemDetailDBHelper.getInstance().insertList(list);
                updateRecordSyncInfo(1);
                checkHasFinish();

            }
        });
    }

    private void updateGridLine(final List<Gridline> list) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                GridLineDBHelper.getInstance().clearAll();
                GridLineDBHelper.getInstance().inertList(list);
                updateRecordSyncInfo(2);
                checkHasFinish();

            }
        });
    }

    private void updateTower(final List<Tower> towers) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TowerDBHelper.getInstance().clearAll();
                    TowerDBHelper.getInstance().inserList(towers);
                    updateRecordSyncInfo(3);
                    checkHasFinish();
                } catch (EventBusException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void updateDefectType(final List<DefectType> defectTypes) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                DefectTypeDBHelper.getInstance().clearAll();
                DefectTypeDBHelper.getInstance().insertList(defectTypes);
                updateRecordSyncInfo(4);
                checkHasFinish();
            }
        });
    }


    private void updateUser(final List<User> users) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                UserDBHellper.getInstance().clearAll();
                UserDBHellper.getInstance().insertList(users);
                updateRecordSyncInfo(5);
                checkHasFinish();

            }
        });
    }

    private void updateOrgnize(final List<Organizition> organizitions) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                OrganizitionDbHelper.getInstance().clearAll();
                OrganizitionDbHelper.getInstance().insertList(organizitions);
                updateRecordSyncInfo(6);
                checkHasFinish();
            }
        });
    }

    private void updateRole(final List<Role> role) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                RoleDBHelp.getInstance().clearAll();
                RoleDBHelp.getInstance().insertList(role);
                updateRecordSyncInfo(7);
                checkHasFinish();
            }
        });
    }


    private void updateUserRole(final List<UserRole> userrole) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                UserRoleDBHelp.getInstance().clearAll();
                UserRoleDBHelp.getInstance().insertList(userrole);
                updateRecordSyncInfo(8);
                checkHasFinish();
            }
        });
    }

    private void updateRoleMenuData(final List<RoleMenuData> roleMenuData) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                RoleMenuDataDBHelp.getInstance().clearAll();
                RoleMenuDataDBHelp.getInstance().insertList(roleMenuData);
                updateRecordSyncInfo(9);
                checkHasFinish();
            }
        });
    }

    private void updateRecordSyncInfo(int catagory) {
        RecordSyncInfo recordSyncInfo = new RecordSyncInfo();
        recordSyncInfo.setCategory(catagory);
        Date date = new Date();
        String dateStr = DateUtil.format(date);
        recordSyncInfo.setLastSyncTime(dateStr);
        RecordSyncInfoDBHelper.getInstance().update(catagory, recordSyncInfo);
    }


    /*
    检查是否更新完数据库
    1、根据网上获取的列表，得出需要更新的数据表的数量 mNeedUpdateCount
    2、每更新完一张表，hasUpdateCount++。调用该方法
    3、若hasUpdateCount>=mNeedUpdateCount,通过eventbus通知登录页面跳转
     */
    private void checkHasFinish() {

        hasUpdateCount++;
        if (hasUpdateCount < mNeedUpdateCount) {
            return;
        }
        mNeedUpdateCount = 0;
        hasUpdateCount = 0;
        EventBus.getDefault().post(new UpdateTableEvent(1));
    }


    private void updateFail() {
        mNeedUpdateCount = 0;
        hasUpdateCount = 0;
        EventBus.getDefault().post(new UpdateTableEvent(0));

    }

    public void saveLineCrossCache(final List<LineCrossEntity> crossEntityList) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                LineCrossDBHelper.getInstance().updateList(crossEntityList);
            }
        });
    }

    //每次只更新一条线路的 缓存
    public void updateBrokenDocumentCache(final List<BrokenDocument> list) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                BrokenDocumentDBHelper.getInstance().updateList(list);
                postUpdateBrokenInspectRecord(list);//更新所有的特训记录
            }
        });
    }


    //更新外破线路的 特训记录
    public void postUpdateBrokenInspectRecord(List<BrokenDocument> list) {
        for (BrokenDocument brokenDocument : list) {
            requestBrokenInspectRecord(brokenDocument.getPlatformId());
        }
    }

    private void requestBrokenInspectRecord(final int documentId) {
        Call<BaseCallBack<List<BrokenInspectRecord>>> request = RetrofitManager.getInstance().getService(ApiService.class).getBrokenRecordList(documentId);
        request.enqueue(new Callback<BaseCallBack<List<BrokenInspectRecord>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<BrokenInspectRecord>>> call, Response<BaseCallBack<List<BrokenInspectRecord>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    updateBrokenInspectRecord(response.body().getData(), documentId);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<BrokenInspectRecord>>> call, Throwable t) {
            }
        });
    }


    private void updateBrokenInspectRecord(final List<BrokenInspectRecord> list, final int platformId) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                BrokenInspectRecordDBHelper.getInstance().deleteAllList();
                for (BrokenInspectRecord record : list) {
                    record.setDocumentPlatformId(platformId);
                }
                BrokenInspectRecordDBHelper.getInstance().insertCacheList(list);
            }
        });
    }


    public void UpdateLineDefects(DefectInfo defectInfo, int lineId) {
        String lineName = GridLineDBHelper.getInstance().getLine(lineId).getLineName();
        updateTreeDefects(defectInfo.getTreeDefectPoint());
        List<DefectBean> list = new ArrayList<>();
        List<DefectBean> channels = updateDefects(defectInfo.getChannelDefect(), "通道");
        list.addAll(channels);
        List<DefectBean> property = updateDefects(defectInfo.getPropertyDefect(), "精细化");
        list.addAll(property);
        List<DefectBean> personal = updateDefects(defectInfo.getPersonalDefect(), "人巡");
        list.addAll(personal);
        List<DefectBean> lineCross = updateDefects(defectInfo.getLineCrossList(), "交跨");
        list.addAll(lineCross);
        updateDefectBean(list, lineName);
    }

    public void updateTreeDefects(final List<TreeDefectPointBean> treeDefectPointBeens) {
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                for (TreeDefectPointBean record : treeDefectPointBeens) {
                    record.setCategory("树障");
                }
                TreeDefectDBHelper.getInstance().updateList(treeDefectPointBeens);
                postUpdateTreeDefectsRemark(treeDefectPointBeens);
            }
        });

    }

    private List<DefectBean> updateDefects(List<DefectBean> defectBeanList, String category) {
        for (DefectBean defectBean : defectBeanList) {
            defectBean.setUploadFlag(1);
        }
        return defectBeanList;
    }

    public void updateDefectBean(final List<DefectBean> defectBeanList, String lineName) {
        DefectBeanDBHelper.getInstance().deleteAllByLineId(lineName);
        ThreadPoolUtils.execute(new Runnable() {
            @Override
            public void run() {
                DefectBeanDBHelper.getInstance().insertLineDefectList(defectBeanList);
                postUpdateDefectRemarkList(defectBeanList);
            }
        });
    }

    //更新所有缺陷的缺陷备注
    private void postUpdateDefectRemarkList(List<DefectBean> list) {
//        for (DefectBean defectBean : list) {
//            postUpdateDefectRemark(defectBean);
//        }
    }

    private void postUpdateTreeDefectsRemark(List<TreeDefectPointBean> list) {
//        for (TreeDefectPointBean defectBean : list) {
//            postUpdateTreeDefectRemark(defectBean);
//
//
//        }
    }

    int i;

//    private void postUpdateDefectRemark(final DefectBean defectBean) {
//        Call<BaseCallBack<List<DefectRemark>>> requestCall = RetrofitManager.getInstance().getService(ApiService.class).
//                getDefectRemarkList("ChannelDefect", defectBean.getSysTowerDefectId());
//        requestCall.enqueue(new Callback<BaseCallBack<List<DefectRemark>>>() {
//            @Override
//            public void onResponse(Call<BaseCallBack<List<DefectRemark>>> call, Response<BaseCallBack<List<DefectRemark>>> response) {
//                if (response != null && response.body() != null && response.body().getCode() == 1) {
//                    updateRemarkList(response.body().getData(), defectBean.getSysTowerDefectId());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseCallBack<List<DefectRemark>>> call, Throwable t) {
//
//            }
//        });
//    }
//
//
//    private void postUpdateTreeDefectRemark(final TreeDefectPointBean defectPointBean) {
//        Call<BaseCallBack<List<DefectRemark>>> requestCall = RetrofitManager.getInstance().getService(ApiService.class).
//                getDefectRemarkList("TreeDefectPoint", defectPointBean.getSysTreeDefectPointID().intValue());
//        requestCall.enqueue(new Callback<BaseCallBack<List<DefectRemark>>>() {
//            @Override
//            public void onResponse(Call<BaseCallBack<List<DefectRemark>>> call, Response<BaseCallBack<List<DefectRemark>>> response) {
//                if (response != null && response.body() != null && response.body().getCode() == 1) {
//                    updateRemarkList(response.body().getData(), defectPointBean.getSysTreeDefectPointID().intValue());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseCallBack<List<DefectRemark>>> call, Throwable t) {
//                int i = 0;
//            }
//        });
//    }
//
//    private void updateRemarkList(final List<DefectRemark> remarks, final int id) {
//        ThreadPoolUtils.execute(new Runnable() {
//            @Override
//            public void run() {
//                DefectRemarkDBHelper.getInstance().deleteAllByDefectId(id);
//                for (DefectRemark remark : remarks) {
//                    remark.setDefectId(id);
//                }
//                DefectRemarkDBHelper.getInstance().insertList(remarks);
//            }
//        });
//    }


}
