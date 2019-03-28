package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ReceiveBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.bean.WorkSheetTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.greendao.ProjectEntityDbHelper;
import com.uflycn.uoperation.greendao.TreeDefectDBHelper;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.FileUtil;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.xflyer.utils.ThreadPoolUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SheekDetailsActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.tv_work_num)
    TextView mTvWorkNum;

    @BindView(R.id.tv_work_man)
    TextView mTvWorkMan;

    @BindView(R.id.tv_work_tel)
    TextView mTvWorkTel;

    @BindView(R.id.tv_work_time)
    TextView mTvWorkTime;

    @BindView(R.id.et_work_remark)
    TextView mTvWorkRemark;

    @BindView(R.id.tv_work_line)
    TextView mTvWorkLine;

    @BindView(R.id.tv_work_tower)
    TextView mTvWorkTower;

    @BindView(R.id.tv_work_type)
    TextView mTvWorkType;

    @BindView(R.id.tv_work_level)
    TextView mTvWorkLevel;

    @BindView(R.id.tv_work_des)
    TextView mTvWorkDes;

    @BindView(R.id.tv_work_type_name)
    TextView mTvWorkTypeName;

    @BindView(R.id.tv_work_level_name)
    TextView mTvWorkLevelName;

    @BindView(R.id.tv_work_des_name)
    TextView mTvWorkDesName;

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.btn_load_line)
    Button mBtnLoadLine;

    @BindView(R.id.btn_play)
    Button mBtnPlay;

    @BindView(R.id.btn_play2)
    Button mBtnPlay2;

    @BindView(R.id.tv_oldPic)
    ImageView mIvOldImg;

    private WorkSheetTask mWorkSheetTask;
    private String zipPath;
    private ProgressDialog progressDialog;
    private Call<ResponseBody> mDownImage = null;
    private Call<BaseCallBack<ReceiveBean>> mCallReceive;

    private BrokenDocument BrokenDocument;
    private DefectBean TowerDefect;
    private TreeDefectPointBean TreeDefect;
    private LineCrossEntity LineCross;
    private ProjectEntity OptProject;
    private SolvePicAsyncTask solvePicAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        initView();
        initDialog();
        Object object = this.getIntent().getSerializableExtra("WorkSheetTask");

        mWorkSheetTask = (WorkSheetTask) object;
        loadWorkSheet(mWorkSheetTask);


    }

    //加载工单信息
    private void loadWorkSheet(WorkSheetTask mWorkSheetTask) {

        initData();
    }

    private void initView() {
        mBtnPlay.setOnClickListener(this);
        mBtnPlay2.setOnClickListener(this);
        mBtnLoadLine.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mIvOldImg.setOnClickListener(this);
    }

    private void initData() {
        solvePicAsyncTask = new SolvePicAsyncTask();
        zipPath = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH +
                File.separator + AppConstant.BREAK_PHOTO_PATH;
        mTvWorkNum.setText(mWorkSheetTask.getTaskNo());
        mTvWorkMan.setText(mWorkSheetTask.getCreatedUser());
        mTvWorkTel.setText(mWorkSheetTask.getPhone());
        if (mWorkSheetTask.getSysGridLineID() == null || mWorkSheetTask.getSysGridLineID() == 0) {
            mBtnLoadLine.setEnabled(false);
            mBtnLoadLine.setBackgroundColor(Color.GRAY);
        }
        String dateTime = mWorkSheetTask.getCreatedTime();
        if (dateTime.lastIndexOf(".") > -1)
            dateTime = dateTime.substring(0, dateTime.lastIndexOf("."));
        if (dateTime.indexOf("T") > -1) {
            mTvWorkTime.setText(dateTime.replace("T", "  "));
        } else {
            mTvWorkTime.setText(dateTime);
        }
        if (mWorkSheetTask.getTaskContent() != null) {
            mTvWorkRemark.setText(mWorkSheetTask.getTaskContent());
        } else {
            mTvWorkRemark.setText("无说明");
        }

        mTvWorkLine.setText(mWorkSheetTask.getLineName());
        if (mWorkSheetTask.getNearDeviceNo() == null || mWorkSheetTask.getNearDeviceNo().isEmpty()) {
            mTvWorkTower.setText("#" + mWorkSheetTask.getDeviceNo());
        } else {
            mTvWorkTower.setText("#" + mWorkSheetTask.getDeviceNo() + "-#" + mWorkSheetTask.getNearDeviceNo());
        }

        WorkSheetTask workSheetTask = WorkSheetTaskDBHelper.getInstance().queryWorkSheet(mWorkSheetTask.getSysTaskId().intValue());
        if (workSheetTask != null){
            if (workSheetTask.getReceiverId() != null){
                if (workSheetTask.getReceiverId().equals(AppConstant.currentUser.getUserId())) {
                    changeBtnText(mWorkSheetTask.getBusinessType());
                } else {
                    mBtnPlay.setEnabled(false);
                    mBtnPlay2.setEnabled(false);
                    mBtnPlay.setText("已被抢");
                    mBtnPlay.setBackgroundColor(Color.GRAY);
                }
            }
        }


        switch (mWorkSheetTask.getBusinessType()) {
            case AppConstant.BROKEN_SHEET://外破工单
                if (mWorkSheetTask.getBrokenDocument() != null) {
                    BrokenDocument = mWorkSheetTask.getBrokenDocument();
                } else {
                    BrokenDocument = BrokenDocumentDBHelper.getInstance().getBrokenDocument(mWorkSheetTask.getBusinessId());
                    if (BrokenDocument == null) {
                        ToastUtil.show("本地暂无该外破信息");
                        finish();
                        return;
                    }
                }
                BrokenDocument.setSysTaskId(Integer.valueOf(mWorkSheetTask.getSysTaskId() + ""));
                mTvWorkTypeName.setText("外破类型：");
                mTvWorkType.setText(ItemDetailDBHelper.getInstance().getItemDetail(BrokenDocument.getBrokenType()).getItemsName());
                mTvWorkLevelName.setText("联系人：");
                mTvWorkLevel.setText(BrokenDocument.getContactPerson());
                mTvWorkDes.setText(BrokenDocument.getRemark());
                solvePicAsyncTask.execute("3_" + mWorkSheetTask.getBusinessId());
                break;
            case AppConstant.CROSS_DEFECT_SHEET://交跨工单
                if (mWorkSheetTask.getLineCross() != null) {
                    LineCross = mWorkSheetTask.getLineCross();
                } else {
                    LineCross = LineCrossDBHelper.getInstance().getLineCross(mWorkSheetTask.getBusinessId());
                    if (LineCross == null) {
                        ToastUtil.show("本地暂无该交跨信息");
                        finish();
                        return;
                    }
                }
                mTvWorkTypeName.setText("距最小侧：");
                mTvWorkType.setText(LineCross.getDtoSmartTower() + "米");
                mTvWorkLevelName.setText("净空距离：");
                mTvWorkLevel.setText(LineCross.getClearance() + "米");
                mTvWorkDesName.setText("交跨角度：");
                mTvWorkDes.setText(LineCross.getCrossAngle() + "");
                solvePicAsyncTask.execute("8_" + mWorkSheetTask.getBusinessId());
                break;
            case AppConstant.PROJECT_SHEET://项目工单
                if (mWorkSheetTask.getOptProject() != null) {
                    OptProject = mWorkSheetTask.getOptProject();
                } else {
                    OptProject = ProjectEntityDbHelper.getInstance().getByPlatFormId(mWorkSheetTask.getBusinessId());
                    if (OptProject == null) {
                        ToastUtil.show("本地暂无该项目信息");
                        finish();
                        return;
                    }
                }

                OptProject.setSysTaskId(Integer.valueOf(mWorkSheetTask.getSysTaskId() + ""));
                mTvWorkTypeName.setText("项目名称：");
                mTvWorkType.setText(OptProject.getProjectName());
                mTvWorkLevelName.setText("项目状态：");
                if (OptProject.getProjectStatus() == 0) {
                    mTvWorkLevel.setText("未结束");
                } else {
                    mTvWorkLevel.setText("结束");
                }
                mTvWorkDesName.setText("项目描述：");
                mTvWorkDes.setText(OptProject.getProjectDescription());
                solvePicAsyncTask.execute("5_" + mWorkSheetTask.getBusinessId());
                break;
            case AppConstant.TREE_DEFECT_SHEET://树樟工单
                if (mWorkSheetTask.getTreeDefect() != null) {
                    TreeDefect = mWorkSheetTask.getTreeDefect();
                } else {
                    TreeDefect = TreeDefectDBHelper.getInstance().getTreeDefectPointBeanById(mWorkSheetTask.getBusinessId());
                    if (TreeDefect == null) {
                        ToastUtil.show("本地暂无该缺陷信息");
                        finish();
                        return;
                    }
                }
                mTvWorkTypeName.setText("缺陷类别：");
                mTvWorkType.setText("树障");
                mTvWorkLevelName.setText("缺陷等级：");
                mTvWorkLevel.setText(TreeDefect.getTreeDefectPointType());
                mTvWorkDesName.setText("缺陷描述：");
                mTvWorkDes.setText(TreeDefect.getRemark());
                solvePicAsyncTask.execute("13_" + mWorkSheetTask.getBusinessId());
                break;
            case AppConstant.CHANNEL_DEFECT_SHEET://通道缺陷工单
                sheetDefect("通道缺陷");
                break;
            case AppConstant.PATROL_DEFECT_SHEET://人巡工单
                sheetDefect("人巡缺陷");
                break;
            case AppConstant.FINE_DEFECT_SHEET://精细化缺陷工单
                sheetDefect("精细化缺陷");
                break;
        }
    }

    private void sheetDefect(String name) {
        if (mWorkSheetTask.getTowerDefect() != null) {
            TowerDefect = mWorkSheetTask.getTowerDefect();
        } else {
            TowerDefect = DefectBeanDBHelper.getInstance().getDefectBeanById(mWorkSheetTask.getBusinessId());
            if (TowerDefect == null) {
                ToastUtil.show("本地暂无该缺陷信息");
                finish();
                return;
            }
        }

        mTvWorkTypeName.setText("缺陷类别：");
        mTvWorkType.setText(name);
        mTvWorkLevelName.setText("缺陷等级：");
        if (TowerDefect.getServerityLevelString() != null) {
            mTvWorkLevel.setText(TowerDefect.getServerityLevelString());
        } else {
            mTvWorkLevel.setText("");
        }
        mTvWorkDesName.setText("缺陷描述：");
        mTvWorkDes.setText(TowerDefect.getRemark());
        solvePicAsyncTask.execute("10_" + mWorkSheetTask.getBusinessId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_load_line:
                if (MyApplication.mLineIdNamePairs.size() >= 3) {
                    ToastUtil.show("线路最多开启3个");
                    return;
                }
                Intent toMainActivity = new Intent(this, MainActivity.class);
                toMainActivity.putExtra("LineID", mWorkSheetTask.getSysGridLineID() + "");
                startActivity(toMainActivity);
                break;
            case R.id.btn_play:
                if (mBtnPlay.getText().toString().equals("抢单")) {
                    sendReceived();
                } else {
                    toActivity(0);
                }
                break;
            case R.id.btn_play2:
                toActivity(1);
                break;
            case R.id.tv_oldPic:
                jumpToPhotoGrid();
                break;
        }
    }

    private void sendReceived() {
        if (!AppConstant.NET_WORK_AVAILABLE) {
            ToastUtil.show("离线不支持抢单功能");
            return;
        }
        mCallReceive = RetrofitManager.getInstance().getService(ApiService.class).sendReceive(mWorkSheetTask.getSysTaskId().intValue());
        mCallReceive.enqueue(new Callback<BaseCallBack<ReceiveBean>>() {
            @Override
            public void onResponse(Call<BaseCallBack<ReceiveBean>> call, Response<BaseCallBack<ReceiveBean>> response) {
                if (response == null || response.body() == null) {
                    return;
                }

                if (response.body().getCode() == 1) {
                    String receiveId = response.body().getData().getReceiverId();



                    if (receiveId.equals(AppConstant.currentUser.getUserId())) {
//                        LogUtils mLogUtils = LogUtils.getInstance();
//                        mLogUtils.e(response.body().getData().getReceiverId() + "  \n",2);
//                        mLogUtils.e(response.body().getData().getReceiver() + "  \n",2);
//                        mLogUtils.e(response.body().getData().getReceiveTime() + "  \n",2);


                        ToastUtil.show("抢单成功");
                        changeBtnText(mWorkSheetTask.getBusinessType());
                    } else {
                        ToastUtil.show("抢单失败");
                        mBtnPlay.setEnabled(false);
                        mBtnPlay2.setEnabled(false);
                        mBtnPlay.setText("已被抢");
                        mBtnPlay.setBackgroundColor(Color.GRAY);
                    }
                    mWorkSheetTask.setReceiverId(receiveId);
                    WorkSheetTaskDBHelper.getInstance().updateWorkSheetTask(mWorkSheetTask);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<ReceiveBean>> call, Throwable t) {
                ToastUtil.show("网络问题导致抢单失败");
            }
        });
    }


    private void changeBtnText(int type) {
        switch (type) {
            case AppConstant.BROKEN_SHEET://外破工单
                mBtnPlay.setText("特巡");
                break;
            case AppConstant.CROSS_DEFECT_SHEET://交跨工单
                mBtnPlay.setText("修改交跨");
                mBtnPlay2.setVisibility(View.VISIBLE);
                break;
            case AppConstant.PROJECT_SHEET://项目工单
                mBtnPlay.setText("巡视登记");
                break;
            default://树樟工单
                mBtnPlay.setText("缺陷消除");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("toValue", AppConstant.WORK_SHEET);
        intent.putExtra("LineID", "");
        startActivity(intent);
    }

    private void toActivity(int flag) {
        Intent intent = new Intent();
        intent.putExtra("FLAG", 1);
        Bundle bundle = new Bundle();
        switch (mWorkSheetTask.getBusinessType()) {
            case AppConstant.BROKEN_SHEET://外破工单
                BrokenDocument.setLineName(mWorkSheetTask.getLineName());
                bundle.putSerializable("brokenDocument", BrokenDocument);
                intent.putExtras(bundle);
                intent.setClass(this, SpecialInspectActivity.class);
                startActivity(intent);
                break;
            case AppConstant.CROSS_DEFECT_SHEET://交跨工单
                LineCross.setSysTaskId(Integer.valueOf(mWorkSheetTask.getSysTaskId() + ""));
                bundle.putSerializable("OptProject", LineCross);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                intent.putExtra("flag", flag);
                intent.setClass(this, TourResultActivity.class);
                startActivity(intent);
                break;
            case AppConstant.PROJECT_SHEET://项目工单
                bundle.putSerializable("OptProject", OptProject);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                intent.setClass(this, TourResultActivity.class);
                startActivity(intent);
                break;
            case AppConstant.TREE_DEFECT_SHEET://树樟工单
                bundle.putSerializable("channelDefectBean", TreeDefect);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                intent.setClass(this, SolveActivity.class);
                startActivity(intent);
                break;
            case AppConstant.CHANNEL_DEFECT_SHEET://通道缺陷工单
                jump2Solve(intent, bundle);
                break;
            case AppConstant.PATROL_DEFECT_SHEET://人巡工单
                jump2Solve(intent, bundle);
                break;
            case AppConstant.FINE_DEFECT_SHEET://精细化缺陷工单
                jump2Solve(intent, bundle);
                break;
        }
    }

    private void jump2Solve(Intent intent, Bundle bundle) {
        bundle.putSerializable("channelDefectBean", TowerDefect);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        intent.setClass(this, SolveActivity.class);
        startActivity(intent);
    }


    private void jumpToPhotoGrid() {
        Intent photo = new Intent(this, PhotoGridActivity.class);
        if (zipPath == null) {
            ToastUtil.show("数据未加载成功");
            return;
        }
        photo.putExtra("path", zipPath);
        startActivity(photo);
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载缺陷信息...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    class SolvePicAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(final String... params) {
            FileUtil.makeDir(zipPath);
            zipPath = zipPath + File.separator + params[0] + ".zip";
            File file = new File(zipPath.substring(0, zipPath.lastIndexOf(".")));
            if (file.exists()) {
                progressDialog.dismiss();
                return null;
            }
            String[] str_cat = params[0].split("_");
            mDownImage = RetrofitManager.getInstance().getService(ApiService.class).getImageZip(Integer.valueOf(str_cat[0]), Integer.valueOf(str_cat[1]));
            mDownImage.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    if (response != null && response.body() != null) {
                        ThreadPoolUtils.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File file1 = new File(zipPath);
                                    if (!file1.exists()) {
                                        file1.createNewFile();
                                    }
                                    inputstreamtofile(response.body().byteStream(), file1);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    if (t != null && t.getMessage() != null) {
                        Log.i("SolvePicAsyncTask", t.getMessage());
                    }
                }
            });
            return null;
        }

        public void inputstreamtofile(InputStream ins, File file) throws IOException {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            try {
                while ((bytesRead = ins.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                    os.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            os.close();
            ins.close();
        }

    }
}
