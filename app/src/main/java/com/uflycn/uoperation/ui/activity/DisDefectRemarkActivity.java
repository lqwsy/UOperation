package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectRemark;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.greendao.DefectRemarkDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.DefectRemarkAdapter;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.Network;
import com.uflycn.uoperation.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消缺备注页面
 */
public class DisDefectRemarkActivity extends Activity {

    @BindView(R.id.listview)
    ListView mDefectListView;

    @BindView(R.id.et_defect_describe)
    EditText etDefectDesc;

    private List<String> mDefectRemarks;

    private DefectBean mChannelDefectBean;
    private TreeDefectPointBean mTreeDefectBean;
    private Call<BaseCallBack<String>> mSubmitDefectRemarkQuest;
    private Call<BaseCallBack<List<DefectRemark>>> mGetDefectRemarksQuest;
    private DefectRemarkAdapter mDefectRemarkAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dis_defect_remark);
        ButterKnife.bind(this);
        try {
            mChannelDefectBean = (DefectBean) getIntent().getSerializableExtra("channelDefectBean");
            mTreeDefectBean = null;
        } catch (Exception e) {
            mTreeDefectBean = (TreeDefectPointBean) getIntent().getSerializableExtra("channelDefectBean");
            mChannelDefectBean = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        //dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        ArrayList<DefectRemark> arrayList = new ArrayList<DefectRemark>();
        //        Collections.reverse(arrayList);
        mDefectRemarkAdapter = new DefectRemarkAdapter(arrayList, this, R.layout.item_defect_remark);
        mDefectListView.setAdapter(mDefectRemarkAdapter);
        getDefectRemarks();
    }

    @OnClick({R.id.iv_back, R.id.btn_submit_remark})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit_remark:
                doSubmit();
                break;
        }
    }


    private void doSubmit() {
        String strDefectDesc = etDefectDesc.getText().toString().trim();
        if (strDefectDesc.equals("")) {
            ToastUtil.show(this, "请输入备注信息。");
            return;
        }
        String type = "ChannelDefect";
        int defectId = -1;
        final DefectRemark defectRemark = new DefectRemark();
        if (mChannelDefectBean != null) {
            defectId = mChannelDefectBean.getSysTowerDefectId();
            if (mChannelDefectBean.getId() != null)
                defectRemark.setLocalDefectId(mChannelDefectBean.getId().intValue());
        } else {
            defectId = mTreeDefectBean.getSysTreeDefectPointID().intValue();
            type = "TreeDefectPoint";

        }

        defectRemark.setDefectId(defectId);
        defectRemark.setDefectType(type);
        defectRemark.setUploadFlag(0);
        defectRemark.setRemark(etDefectDesc.getText().toString().trim());


        etDefectDesc.setText("");
        defectRemark.setCreatedTime(DateUtil.format(new Date()));
//        if (MyApplication.gridlineTaskStatus == 2) {
//            defectRemark.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
//        }
        int towerId;
        if (mChannelDefectBean != null) {
            towerId = mChannelDefectBean.getSysTowerID();
        } else {
            towerId = mTreeDefectBean.getTowerA_Id();
        }
        Tower t = TowerDBHelper.getInstance().getTower(towerId);
        if (t != null && MyApplication.mDayPlanLineMap.get(t.getSysGridLineId()) != null) {
            defectRemark.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(t.getSysGridLineId()));
        }
        if (MyApplication.gridlineTaskStatus==3){
            defectRemark.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        DefectRemarkDBHelper.getInstance().insertDefectRemark(defectRemark);
//        if (Network.checkNetWork(this)) {
        postSubmitCall(defectRemark);
//        } else {
//            getDefectRemarks();
//        }
    }

    private void postSubmitCall(final DefectRemark defectRemark) {
        mProgressDialog.show();
        mSubmitDefectRemarkQuest = RetrofitManager.getInstance().getService(ApiService.class)
                .postDefectVerification(defectRemark.getDefectType(), defectRemark.getDefectId(),
                        defectRemark.getRemark(),defectRemark.getSysPatrolExecutionID(),defectRemark.getPlanDailyPlanSectionIDs());
        mSubmitDefectRemarkQuest.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mProgressDialog.dismiss();
                if (response == null || response.body() == null) {
//                    Log.e("err", "postSubmitCall--onResponse:== null ");
                    ToastUtil.show("提交到数据库!");
                    return;
                }
                if (response.body().getCode() == 1) {
                    ToastUtil.show("提交成功");
                    defectRemark.setUploadFlag(1);
                    DefectRemarkDBHelper.getInstance().update(defectRemark);
                }
                getDefectRemarks();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
//                getDefectRemarks();
                mProgressDialog.dismiss();
                ToastUtil.show("提交到数据库!");
            }
        });
    }


    private void getDefectRemarks() {
        String type = "ChannelDefect";
        int defectId = -1;
        if (mChannelDefectBean != null) {
            defectId = mChannelDefectBean.getSysTowerDefectId();
        } else {
            defectId = mTreeDefectBean.getSysTreeDefectPointID().intValue();
            type = "TreeDefectPoint";
        }


        if (Network.checkNetWork(this) && defectId != 0) {
            postGetList(type, defectId);
        } else {
            List<DefectRemark> remarks;
            if (mChannelDefectBean != null) {
                remarks = DefectRemarkDBHelper.getInstance().getAllListByDefectId(mChannelDefectBean);
            } else
                remarks = DefectRemarkDBHelper.getInstance().getAllListByDefectId(defectId);
            Collections.reverse(remarks);
            mDefectRemarkAdapter.onDataChange(remarks);
        }
    }

    int i;

    private void postGetList(String type, int defectId) {
        Log.d("nate", "postGetList: ");
        mGetDefectRemarksQuest = RetrofitManager.getInstance().getService(ApiService.class).
                getDefectRemarkList(type, defectId);
        mGetDefectRemarksQuest.enqueue(new Callback<BaseCallBack<List<DefectRemark>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<DefectRemark>>> call, Response<BaseCallBack<List<DefectRemark>>> response) {
                if (response == null || response.body() == null) {
                    Log.e("RequestErr", "postGetList--response == null || response.body() == null");
                    return;
                }
                if (response.body().getCode() == 0) {
                    Log.e("RequestErr", "postGetList-" + response.body().getMessage());
                } else {
                    mDefectRemarkAdapter.onDataChange(response.body().getData());
                }
//                getDefectRemarks();
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<DefectRemark>>> call, Throwable t) {
                if (t != null) {
                    Log.e("RequestErr", "onFailure: postGetList" + t.getMessage());
                }
//                getDefectRemarks();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubmitDefectRemarkQuest != null && !mSubmitDefectRemarkQuest.isCanceled()) {
            mSubmitDefectRemarkQuest.cancel();
        }
        if (mGetDefectRemarksQuest != null && !mGetDefectRemarksQuest.isCanceled()) {
            mGetDefectRemarksQuest.cancel();
        }
    }
}
