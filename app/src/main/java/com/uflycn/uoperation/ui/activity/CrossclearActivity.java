package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.LineCrossDelete;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.UpdateLineCrossList;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDeleteDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenter;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrossclearActivity extends Activity {


    @BindView(R.id.sp_cross_type)
    TextView mCrossTypeSpinner;
    @BindView(R.id.sp_cross_subtype)
    TextView mCrossSubTypeSpinner;
    @BindView(R.id.et_get_distance)
    TextView mDistance;
    @BindView(R.id.et_absolute_height)
    TextView mAbsoluteHeight;
    @BindView(R.id.et_cross_description)
    EditText mCrossDesc;
    @BindView(R.id.tv_grid_name)
    TextView tvGridName;
    @BindView(R.id.tv_tower_num)
    TextView tvTowerNum;
    @BindView(R.id.et_cross_angle)
    TextView etCrossAngle;
    @BindView(R.id.add_img_cross)
    AddImageGridView add_img_cross;


    private LineCrossEntity mLineCrossEntity;
    private Call<BaseCallBack<List<ListCallBack<String>>>> mRequestClear;
    private ProgressDialog mProgressDialog;

    private ProjectPresenter mProjectPresenter;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;
    private static int FLAG;
    private LocalBroadcastManager localBroadcastManager;

    public static Intent newInstance(Context context,LineCrossEntity lineCrossEntity){
        Intent intent = new Intent(context, CrossclearActivity.class);
        intent.putExtra("LineCrossEntity", lineCrossEntity);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.fragment_fragment_crossclear);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mRequestClear != null && !mRequestClear.isCanceled()) {
                    mRequestClear.cancel();
                }
            }
        });
    }

    private void initData() {
        mLineCrossEntity = (LineCrossEntity) getIntent().getSerializableExtra("LineCrossEntity");

        tvGridName.setText(mLineCrossEntity.getLineName());
        tvTowerNum.setText(mLineCrossEntity.getStartTower() + "-" + mLineCrossEntity.getEndTower());
        if (mLineCrossEntity.getStartTower() == null) {
            tvTowerNum.setText(getTowerNo(mLineCrossEntity.getStartTowerId()) + "-" + getTowerNo(mLineCrossEntity.getEndTowerId()));
        }
        DefectType secondLevel = DefectTypeDBHelper.getInstance().getDefectType((long) mLineCrossEntity.getCrossType());
        DefectType firstLevel = DefectTypeDBHelper.getInstance().getDefectType((long) secondLevel.getDefectParentId());
        mCrossTypeSpinner.setText(firstLevel.getDefectName());
        mCrossSubTypeSpinner.setText(mLineCrossEntity.getCrossTypeName());
        mDistance.setText(mLineCrossEntity.getDtoSmartTower() + "");
        mAbsoluteHeight.setText(mLineCrossEntity.getClearance() + "");
        etCrossAngle.setText(mLineCrossEntity.getCrossAngle() + "");
        mCrossDesc.setText("");
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        add_img_cross.setAdapter(gridViewAddImgesAdpter);
        add_img_cross.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                switch (code) {
                    case 1:
                        camera();
                        break;
                    case 2:
                        gallery();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private String getTowerNo(int id) {
        Tower tower = TowerDBHelper.getInstance().getTower(id);
        if (tower == null)
            return "杆塔已删除";
        return tower.getTowerNo();
    }

    /**
     * 拍照
     */
    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
            File dir = new File(IMAGE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
            tempFile = new File(fileName);
            //从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
        } else {
            ToastUtil.show("未找到存储卡，无法拍照！");
        }
    }

    /**
     * 判断sdcard是否被挂载
     */
    public boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 从相册获取2
     */
    public void gallery() {
        Intent intent = new Intent(this, PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        int nowNeed = gridViewAddImgesAdpter.getMaxImages() - gridViewAddImgesAdpter.getCount() + 1;
        intent.putExtra("limit", nowNeed);//number是选择图片的数量
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                        //处理代码
                        for (String path : paths) {
                            photoPath(path);
                        }
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:
                    // 从相机返回的数据
                    if (hasSdcard()) {
                        if (tempFile != null) {
                            photoPath(tempFile.getPath());
                        } else {
                            ToastUtil.show("相机异常请稍后再试！");
                        }
                    } else {
                        ToastUtil.show("未找到存储卡，无法存储照片！");
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 对于文件进行剪切。展示
     *
     * @param path
     */
    public void photoPath(String path) {
        IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
        FileUtils.changeBitmapPath(path, fileName);
        SendBroadcasd2MEDIA.sendBroadcasd(this, IMAGE_DIR);
        Map<String, Object> map = new HashMap<>();
        map.put("path", fileName);
        datas.add(map);
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }


    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                submitClear();
                break;
        }
    }


    private void submitClear() {
        if (datas == null || datas.size() == 0) {
            ToastUtil.show("未上传照片！");
            return;
        }
        if (TextUtils.isEmpty(mCrossDesc.getText())) {
            ToastUtil.show("标注描述不能为空！");
            return;
        }
        LineCrossDelete delete = new LineCrossDelete();
        delete.setSysLineCrossId(mLineCrossEntity.getPlatformId());
        delete.setUploadFlag(0);
        delete.setRemark(mCrossDesc.getText().toString());
        delete.setCheckerId(AppConstant.currentUser.getUserId());
        delete.setCheckedTime(DateUtil.format(new Date()));
        if (mLineCrossEntity.getSysTaskId() == 0) {
            int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(mLineCrossEntity.getPlatformId(), AppConstant.CROSS_DEFECT_SHEET);
            if (sysWorkId != -1) {
                mLineCrossEntity.setSysTaskId(sysWorkId);
                delete.setSysTaskId(sysWorkId);
            }
        } else {
            delete.setSysTaskId(mLineCrossEntity.getSysTaskId());
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                delete.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                delete.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            delete.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        LineCrossDeleteDBHelper.getInstance().insert(delete);
        saveImagePaths(delete);
        postClear(delete);
    }

    private void saveImagePaths(LineCrossDelete lineCrossDelete) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(9);
            imagePaths.setLocalId(lineCrossDelete.getId() + "");
            imagePaths.setFatherPlatformId(lineCrossDelete.getSysLineCrossId() + "");
            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestClear != null && !mRequestClear.isCanceled()) {
            mRequestClear.cancel();
        }
    }


    private void postClear(final LineCrossDelete delete) {
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ImageData" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        Gson gson = new Gson();
        String json = gson.toJson(delete);
        postClearNet(delete, requestImgParts, json);
    }

    private void postClearNet(final LineCrossDelete delete, final List<MultipartBody.Part> requestImgParts, final String json) {
        mProgressDialog.show();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        mRequestClear = RetrofitManager.getInstance().getService(ApiService.class).postClearCross(body, requestImgParts);
        mRequestClear.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    delete.setUploadFlag(1);
                    LineCrossDeleteDBHelper.getInstance().update(delete);
                    EventBus.getDefault().post(new UpdateLineCrossList(false));
                    ToastUtil.show("提交成功");
                    if (mLineCrossEntity.getSysTaskId() != 0) {
                        WorkSheetTaskDBHelper.getInstance().delete(mLineCrossEntity.getSysTaskId());
                        localBroadcastManager = LocalBroadcastManager.getInstance(CrossclearActivity.this);
                        Intent intent = new Intent(AppConstant.WORK_SHEET_NUM_DEL);
                        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        localBroadcastManager.sendBroadcast(intent);
                    }
                } else {
                    if (mLineCrossEntity.getSysTaskId() != 0) {
                        ToastUtil.show("工单已被其他用户处理");
                    } else {
                        //                        ToastUtil.show(response.body().getMessage());
                        retryPostClear(delete, requestImgParts, json);
                        return;
                    }
                }
                mProgressDialog.dismiss();
                //                reset();
                finish();

            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                retryPostClear(delete, requestImgParts, json);
            }
        });
    }

    //重写提交
    private void retryPostClear(final LineCrossDelete delete, final List<MultipartBody.Part> requestImgParts, final String json) {
        DialogUtils.showRetryPostDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postClearNet(delete, requestImgParts, json);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mProgressDialog.dismiss();
                ToastUtil.show("已提交到本地数据库");
                //                reset();
                finish();
            }
        });
    }

    private void reset() {
        datas.clear();
        gridViewAddImgesAdpter.notifyDataSetChanged();
        mCrossDesc.setText("");
    }


}
