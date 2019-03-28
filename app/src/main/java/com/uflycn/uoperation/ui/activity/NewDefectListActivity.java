package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectListBean;
import com.uflycn.uoperation.bean.DefectTreeBeanJson;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.bean.WorksheetApanageTask;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.BatchDefectAdapter;
import com.uflycn.uoperation.ui.adapter.SimpleDefectBean;
import com.uflycn.uoperation.util.ToastUtil;
import com.xflyer.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 新增清障工单
 */
public class NewDefectListActivity extends Activity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.tv_line_name)
    TextView mTvLineName;

    @BindView(R.id.sp_break_tower_from)
    Spinner mSpTowerFromTo;

    @BindView(R.id.lv_tree_defect)
    SwipeMenuListView mLvTreeDefect;

    private Context mContext;
    private BatchDefectAdapter batchDefectAdapter;
    private List<SimpleDefectBean> mTreeDefects;
    private List<SpinnerOption> mSpTowerNo;
    private List<Tower> mTowers;
    private Call<BaseCallBack<WorksheetApanageTask>> mSubmitCall;
    private Call<BaseCallBack<DefectListBean>> mDefectList;
    private ProgressDialog mProgressDialog;
    private List<SimpleDefectBean> sysIdList = new ArrayList<>();
    private String tower2;
    private String GridLineID = "";
    private int towerA_id;
    private int towerB_id;
    private List<SimpleDefectBean> SimpleDefetList;
    private Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_defect_list);
        ButterKnife.bind(this);
        mContext = this;
        mGson = new Gson();


        initView();
        initData();
    }

    private void initView() {
        //设置线路名称
        if (MyApplication.specialRegisteredStartTower != null && MyApplication.specialRegisteredEndTower != null) {
            GridLineID = MyApplication.specialRegisteredStartTower.getSysGridLineId() + "";
            String lineName = GridLineDBHelper.getInstance().getLine(MyApplication.specialRegisteredStartTower.getSysGridLineId()).getLineName();
            mTvLineName.setText(lineName);
            mTowers = TowerDBHelper.getInstance().getLineTowers(MyApplication.specialRegisteredStartTower.getSysGridLineId());
        } else if (MyApplication.isRegisterAuto == true) {
            if (MyApplication.currentNearestTower != null) {
                String lineName = GridLineDBHelper.getInstance().getLine(MyApplication.currentNearestTower.getSysGridLineId()).getLineName();
                mTvLineName.setText(lineName);
                mTowers = TowerDBHelper.getInstance().getLineTowers(MyApplication.currentNearestTower.getSysGridLineId());
                GridLineID = MyApplication.currentNearestTower.getSysGridLineId() + "";
            } else {
                ToastUtil.show("请先进行到位登记");
            }
        } else {
            if (MyApplication.registeredTower != null) {
                String lineName = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId()).getLineName();
                mTvLineName.setText(lineName);
                mTowers = TowerDBHelper.getInstance().getLineTowers(MyApplication.registeredTower.getSysGridLineId());
                GridLineID = MyApplication.registeredTower.getSysGridLineId() + "";
            } else {
                ToastUtil.show("请先进行到位登记");
            }
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);

        mSpTowerFromTo.setOnItemSelectedListener(this);
        mLvTreeDefect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_batch);
                if (!checkBox.isChecked()) {
                    sysIdList.add(mTreeDefects.get(position));
                    mTreeDefects.get(position).setSelect(true);
                    removeDuplicate(sysIdList);
                } else {
                    mTreeDefects.get(position).setSelect(false);
                    sysIdList.remove(mTreeDefects.get(position));
                }
                checkBox.toggle();
            }
        });
    }

    public void removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
    }

    private void initData() {
        mTreeDefects = new ArrayList<>();
        batchDefectAdapter = new BatchDefectAdapter(mTreeDefects, mContext, R.layout.item_batch_defcet);
        //initListViewCreater();
        mLvTreeDefect.setAdapter(batchDefectAdapter);

        mSpTowerNo = new ArrayList<>();
        mSpTowerNo.add(new SpinnerOption("", "请选择杆塔区间"));
        if (mTowers == null) {
            return;
        }

        if (MyApplication.isRegisterAuto == true) {
            if (MyApplication.currentNearestTower != null) {
                for (int i = 0; i < mTowers.size() - 1; i++) {
                    Tower from = mTowers.get(i);
                    Tower to = mTowers.get(i + 1);
                    if (from.equals(MyApplication.currentNearestTower) || to.equals(MyApplication.currentNearestTower))
                        mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
                }
            }
        } else {
            if (MyApplication.registeredTower != null) {
                for (int i = 0; i < mTowers.size() - 1; i++) {
                    Tower from = mTowers.get(i);
                    Tower to = mTowers.get(i + 1);
                    if (from.equals(MyApplication.registeredTower) || to.equals(MyApplication.registeredTower))
                        mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
                }
            }
        }
        //如果是特殊巡视 就特殊处理
        if (MyApplication.specialRegisteredStartTower != null) {
            mSpTowerNo.clear();
            mSpTowerNo.add(new SpinnerOption("", "请选择杆塔区间"));
            for (int i = 0; i < mTowers.size() - 1; i++) {
                Tower from = mTowers.get(i);
                Tower to = mTowers.get(i + 1);
                if (from.equals(MyApplication.specialRegisteredStartTower) || to.equals(MyApplication.specialRegisteredStartTower)) {
                    while (!from.equals(MyApplication.specialRegisteredEndTower)) {
                        if (i == mTowers.size() - 2) {
                            break;
                        }
                        mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
                        i = i + 1;
                        from = mTowers.get(i);
                        to = mTowers.get(i + 1);
                    }
                    mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
                }
            }
        }


        ArrayAdapter<SpinnerOption> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, mSpTowerNo);
        mSpTowerFromTo.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mTreeDefects = TreeDefectDBHelper.getInstance().getTreeDefectPointBy2(mTvLineName.getText().toString(), tower2);
        batchDefectAdapter.onDataChange(mTreeDefects);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sysIdList.clear();
        tower2 = mSpTowerNo.get(position).getText();
        if (position != 0) {
            String[] Tower_NO = tower2.split(" - ");
            towerA_id = Integer.valueOf(TowerDBHelper.getInstance().getTower(GridLineID, Tower_NO[0]).getSysTowerID() + "");
            towerB_id = Integer.valueOf(TowerDBHelper.getInstance().getTower(GridLineID, Tower_NO[1]).getSysTowerID() + "");
        }
        if (mTreeDefects.size() > 0) {
            mLvTreeDefect.removeAllViewsInLayout();
            //mLvTreeDefect.removeAllViews();
        }
        if (position != 0) {
            createDefectList();
        }


    }

    private void createDefectList() {
        //mTreeDefects = new ArrayList<>();
        SimpleDefetList = new ArrayList<>();
        mProgressDialog.setMessage("正在查询树障...");
        mProgressDialog.show();
        mDefectList = RetrofitManager.getInstance().getService(ApiService.class).getDefectList(towerA_id, towerB_id);
        mDefectList.enqueue(new Callback<BaseCallBack<DefectListBean>>() {
            @Override
            public void onResponse(Call<BaseCallBack<DefectListBean>> call, Response<BaseCallBack<DefectListBean>> response) {
                BaseCallBack<DefectListBean> body = response.body();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (body == null) {
                    return;
                }

                if (body != null && body.getCode() == 1) {
                    List<TreeDefectPointBean> treeDefects = body.getData().getTreeDefectList();
                    List<DefectBean> towerDefects = body.getData().getTowerDefectList();

                    for (TreeDefectPointBean treeDefect : treeDefects) {
                        SimpleDefectBean spdf = new SimpleDefectBean();
                        spdf.setDefectID(treeDefect.getSysTreeDefectPointID() + "");
                        spdf.setGridLine(treeDefect.getLineName());
                        spdf.setRemark(treeDefect.getRemark());
                        spdf.setType(1);
                        SimpleDefetList.add(spdf);
                    }
                    for (DefectBean towerDefect : towerDefects) {
                        SimpleDefectBean spdf = new SimpleDefectBean();
                        spdf.setDefectID(towerDefect.getSysTowerDefectId() + "");
                        spdf.setGridLine(towerDefect.getLineName());
                        spdf.setRemark(towerDefect.getRemark());
                        spdf.setType(2);
                        SimpleDefetList.add(spdf);
                    }
                    mTreeDefects = SimpleDefetList;
                    batchDefectAdapter.onDataChange(mTreeDefects);
                    for (SimpleDefectBean treeDefectPointBean : mTreeDefects) {
                        sysIdList.add(treeDefectPointBean);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<DefectListBean>> call, Throwable t) {
                ToastUtil.show("网络链接失败");
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick({R.id.btn_submit, R.id.iv_back})
    public void sumbit(View view) {
        //TODO:
        switch (view.getId()) {
            case R.id.btn_submit:
                doSumbit();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void doSumbit() {

        if (sysIdList.size() == 0) {
            ToastUtil.show("此区间暂无可操作的树障缺陷");
            return;
        }
        DefectTreeBeanJson defectTreeBeanJson = new DefectTreeBeanJson();
        int countOfTree = 0;
        int[] TreeDefectPointList1 = new int[sysIdList.size()];
        int countOfTower = 0;
        int[] TowerDefectList1 = new int[sysIdList.size()];
        for (SimpleDefectBean sysId : sysIdList) {
            if (sysId.getType() == 1) {
                TreeDefectPointList1[countOfTree++] = Integer.valueOf(sysId.getDefectID());
            } else {
                TowerDefectList1[countOfTower++] = Integer.valueOf(sysId.getDefectID());
            }
        }
        int[] TreeDefectPointList = new int[countOfTree];
        int[] TowerDefectList = new int[countOfTower];
        for (int i = 0; i < countOfTree; i++) {
            TreeDefectPointList[i] = TreeDefectPointList1[i];
        }
        for (int i = 0; i < countOfTower; i++) {
            TowerDefectList[i] = TowerDefectList1[i];
        }
        mProgressDialog.setMessage("正在提交");
        mProgressDialog.show();
        defectTreeBeanJson.setTowerA_ID(towerA_id);
        defectTreeBeanJson.setTowerB_ID(towerB_id);
        defectTreeBeanJson.setTreeDefectPointList(TreeDefectPointList);
        defectTreeBeanJson.setTowerDefectList(TowerDefectList);
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                defectTreeBeanJson.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                defectTreeBeanJson.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            defectTreeBeanJson.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        String postData = mGson.toJson(defectTreeBeanJson);


        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), postData);
        mSubmitCall = RetrofitManager.getInstance().getService(ApiService.class).createApanageTask(body);
        mSubmitCall.enqueue(new Callback<BaseCallBack<WorksheetApanageTask>>() {
            @Override
            public void onResponse(Call<BaseCallBack<WorksheetApanageTask>> call, Response<BaseCallBack<WorksheetApanageTask>> response) {
                BaseCallBack<WorksheetApanageTask> body = response.body();
                if (body == null) {
                    mProgressDialog.dismiss();
                    return;
                }

                if (body != null && body.getCode() == 1) {
                    mProgressDialog.dismiss();
                    //ToastUtil.show("编号:" + body.getData().getTaskNo());
                    final Dialog dialog = DialogUtils.createNormalDialog(mContext, -1, "清障工单编号",
                            "\t" + body.getData().getTaskNo(), "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    finish();
                                }
                            });
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                } else {
                    ToastUtil.show("" + body);
                    mProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<WorksheetApanageTask>> call, Throwable t) {
                ToastUtil.show("提交到数据库");
                mProgressDialog.dismiss();
                finish();
            }
        });
    }
}
