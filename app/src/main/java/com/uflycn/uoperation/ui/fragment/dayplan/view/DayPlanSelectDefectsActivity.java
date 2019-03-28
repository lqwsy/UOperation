package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.MyBaseActivity;
import com.uflycn.uoperation.bean.AddDayPlanDefectBean;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectInfo;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.fragment.dayplan.adapter.DayPlanDefectAdapter;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 添加缺陷-缺陷列表
 */
public class DayPlanSelectDefectsActivity extends MyBaseActivity {
    public static final String KEY_IDS = "key_ids";
    public static final String KEY_DEFECT = "KEY_DEFECT";
    public static final int RESULT_CODE = 0x0013;
    private List<Object> defectList;
    private String mIds;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private DayPlanDefectAdapter mAdapter;
    private boolean isSelectAll;

    public static Intent newInstance(Context context, String ids) {
        Intent intent = new Intent(context, DayPlanSelectDefectsActivity.class);
        intent.putExtra(KEY_IDS, ids);
        return intent;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_day_plan_select_defects;
    }

    @Override
    public void initView() {
        super.initView();
        defectList = new ArrayList<>();
        mIds = getIntent().getStringExtra(KEY_IDS);
        //设置相应的adapter
        mAdapter = new DayPlanDefectAdapter(R.layout.item_day_plan_defect, defectList);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecycler.setAdapter(mAdapter);
        //下拉刷新事件
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        //请求网络获取相应线路的缺陷列表
        getLineDfaultDefects(mIds, "", "", "", "");
    }

    /**
     * 获取线路的缺陷
     */
    private void getLineDfaultDefects(String lineIds, String voltageClass, String lineName, String towerid, String nearTowerid) {
//        showDialog("加载中..");
        showRefresh();
        Call<DefectInfo> call = RetrofitManager.getInstance().getService(ApiService.class)
                .getLineDefectList(lineIds, "0", "0", voltageClass, lineName, towerid, nearTowerid, MyApplication.mPlanPatrolExecutionId,null);
        call.enqueue(new Callback<DefectInfo>() {
            @Override
            public void onResponse(Call<DefectInfo> call, Response<DefectInfo> response) {
//                missDialog();
                disRefresh();
                DefectInfo defectInfo = response.body();
                if (defectInfo == null) {//关闭对话框
                    return;
                }
                defectList.clear();
                if (defectInfo.getCode() == 1) {
                    List<DefectBean> channelDefect = defectInfo.getChannelDefect();
                    defectList.addAll(channelDefect);
                    //sortList(dataList,1);

                    List<TreeDefectPointBean> treeDefectPoint = defectInfo.getTreeDefectPoint();
                    defectList.addAll(treeDefectPoint);
//                    sortList(dataList,1);

                    List<DefectBean> propertyDefect = defectInfo.getPropertyDefect();
                    defectList.addAll(propertyDefect);
                    //sortList(dataList,1);

                    List<DefectBean> personalDefect = defectInfo.getPersonalDefect();
                    defectList.addAll(personalDefect);
                    //sortList(dataList,1);

                    List<DefectBean> lineCrossList = defectInfo.getLineCrossList();
                    defectList.addAll(lineCrossList);
                    sortList(defectList, 1);
                }
            }

            @Override
            public void onFailure(Call<DefectInfo> call, Throwable throwable) {
//                missDialog();
                disRefresh();
                ToastUtil.show("网络连接失败!");
            }
        });
    }

    /**
     * 对缺陷进行排序，具体的我也不清楚，代码抄DisDefectFragment的
     */
    private void sortList(final List<Object> dataList, final int flag) { //flag用于判断排列顺序
        Observable.create(new Observable.OnSubscribe<List<Object>>() {
            @Override
            public void call(Subscriber<? super List<Object>> subscriber) {
                Collections.sort(dataList, new Comparator<Object>() {
                    @Override
                    public int compare(Object arg0, Object arg1) {
                        int towerId1, towerId2;
                        if (arg0 instanceof DefectBean) {
                            towerId1 = ((DefectBean) arg0).getSysTowerID();
                        } else {
                            towerId1 = ((TreeDefectPointBean) arg0).getTowerA_Id();
                        }

                        if (arg1 instanceof DefectBean) {
                            towerId2 = ((DefectBean) arg1).getSysTowerID();
                        } else {
                            towerId2 = ((TreeDefectPointBean) arg1).getTowerA_Id();
                        }

                        if (towerId1 > towerId2) {
                            return 1;
                        } else if (towerId1 < towerId2) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }

                });

                subscriber.onNext(dataList);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Object>>() {
                    @Override
                    public void call(List<Object> objects) {
                        showDefects(objects);
                    }
                });


    }

    /**
     * 显示缺陷
     */
    private void showDefects(List<Object> objects) {
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_open_close_drawer, R.id.btn_select_all, R.id.btn_sure})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                finish();
                break;
            case R.id.btn_select_all:
                isSelectAll = !isSelectAll;
                handleSelectAll();
                break;
            case R.id.btn_sure:
                handleSure();
                break;
        }
    }

    private void handleSure() {
        //将defectList中的所有被选中的返回 选中object 封装要上传的bean对象然后返回
        final ArrayList<AddDayPlanDefectBean> defectSelectList = new ArrayList<>();
        Observable.from(defectList)
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        if (o instanceof DefectBean) {
                            DefectBean defectBean = (DefectBean) o;
                            return defectBean.isChecked();
                        } else {
                            TreeDefectPointBean defectPointBean = (TreeDefectPointBean) o;
                            return defectPointBean.isChecked();
                        }
                    }
                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        AddDayPlanDefectBean bean = new AddDayPlanDefectBean();
                        if (o instanceof DefectBean) {
                            DefectBean defectBean = (DefectBean) o;
                            bean.setDefectID((long) defectBean.getSysTowerDefectId());
                            int defType = getDefectType(defectBean.getDefectCategoryString());
                            bean.setDefectType(defType);
                            bean.setLineName(defectBean.getLineName());
                            bean.setTowerNo(defectBean.getTowerNo());
                            bean.setDefectDesc(defectBean.getRemark());
                            bean.setDefectStatus(defectBean.getDefectStateString());
                            //线路id
                            bean.setSysGridLineId(defectBean.getSysGridLineID());
                        } else {
                            TreeDefectPointBean defectPointBean = (TreeDefectPointBean) o;
                            bean.setDefectID(defectPointBean.getSysTreeDefectPointID());
                            int defType = getDefectType(defectPointBean.getTreeDefectType());
                            bean.setDefectType(defType);
                            ///towerno
                            String towerNo = defectPointBean.getTowerA_Name();
                            String nearTowerNo = defectPointBean.getTowerB_Name();
                            if (!StringUtils.isEmptyOrNull(nearTowerNo) && !nearTowerNo.equals(towerNo)) {
                                towerNo = showTowerId(towerNo, nearTowerNo);
                            }
                            bean.setLineName(defectPointBean.getLineName());
                            bean.setTowerNo(towerNo);
                            bean.setDefectDesc(defectPointBean.getRemark());
                            bean.setDefectStatus(defectPointBean.getDefectStateString());
                            //线路id
                            bean.setSysGridLineId(defectPointBean.getSysGridLineID());
                            //传给移除线路

                        }
                        defectSelectList.add(bean);
                    }
                });
        //获取到之后，将数据返回给上一层,由于这个类型是List<Object>的，所以用intent
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(KEY_DEFECT, defectSelectList);
        setResult(RESULT_CODE, intent);
        finish();
    }

    /**
     * 获取缺陷类型
     * 通道缺陷 = 0,
     * 精细化缺陷 = 1,
     * 人巡缺陷 = 2,
     * 树障缺陷 = 3
     */
    private int getDefectType(String defectCatage) {
        if (defectCatage.contains("通道")) {
            return 0;
        } else if (defectCatage.contains("精细化")) {
            return 1;
        } else if (defectCatage.contains("人巡")) {
            return 2;
        } else if (defectCatage.contains("树障")) {
            return 3;
        } else {
            return 2;
        }
    }

    private void handleSelectAll() {
        for (Object o : defectList) {
            if (o instanceof DefectBean) {
                DefectBean defectBean = (DefectBean) o;
                defectBean.setChecked(isSelectAll);
            } else {
                TreeDefectPointBean defectPointBean = (TreeDefectPointBean) o;
                defectPointBean.setChecked(isSelectAll);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    public void showRefresh() {
        if (mRefresh != null && !mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(true);
        }
    }

    public void disRefresh() {
        if (mRefresh != null && mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(false);
        }
    }

    //构造杆塔
    private String showTowerId(String towerNo, String nearTowerNo) {
        try {
            int tower = Integer.valueOf(towerNo);
            int nearTower = Integer.valueOf(nearTowerNo);
            if (tower < nearTower) {
                towerNo = towerNo + "-" + nearTowerNo;
            } else {
                towerNo = nearTowerNo + "-" + towerNo;
            }
        } catch (Exception e) {
            towerNo = towerNo + "-" + nearTowerNo;
        }

        return towerNo;
    }
}
