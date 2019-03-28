package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.BrokenInspectRecord;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.greendao.BrokenInspectRecordDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

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

/**
 * 特巡
 */
public class SpecialInspectActivity extends Activity {


    @BindView(R.id.tv_start_end_tower)
    TextView tvStartEndTower;

    @BindView(R.id.tv_break_num)
    TextView tvBreakNum;

    @BindView(R.id.tv_break_type)
    TextView tvBreakType;

    @BindView(R.id.tv_break_conpany)
    TextView tvBreakConpany;

    @BindView(R.id.tv_break_contact)
    TextView tvBreakContact;

    @BindView(R.id.tv_break_phone)
    TextView tvBreakPhone;

    @BindView(R.id.radiogroup)
    RadioGroup mRadioGroup;

    @BindView(R.id.tv_line_name)
    TextView tvLineName;

    @BindView(R.id.add_img_special)
    AddImageGridView add_img_special;


    @BindView(R.id.et_cross_description)
    EditText mEtRemark;

    private BrokenDocument mBrokenDocument;

    private int status = -1;
    private ProgressDialog mProgressDialog;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;
    private Call<BaseCallBack<String>> mCall;
    private int FLAG;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_special_inspect);

        ButterKnife.bind(this);

        mBrokenDocument = (BrokenDocument) getIntent().getSerializableExtra("brokenDocument");
        FLAG = getIntent().getIntExtra("FLAG",-1);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mCall != null && !mCall.isCanceled()) {
                    mCall.cancel();
                }
            }
        });
        initData();
    }

    private void initData() {
        tvLineName.setText(mBrokenDocument.getLineName());
        tvStartEndTower.setText(getTowerNO(mBrokenDocument.getStartTowerId()) + "-" + getTowerNO(mBrokenDocument.getEndTowerId()));
        tvBreakNum.setText(mBrokenDocument.getDocmentNo());
        if (mBrokenDocument.getBrokenTypeName() != null){
            tvBreakType.setText(mBrokenDocument.getBrokenTypeName());
        }else{
            tvBreakType.setText(ItemDetailDBHelper.getInstance().getItemDetail(mBrokenDocument.getBrokenType()).getItemsName());
        }
        tvBreakConpany.setText(mBrokenDocument.getCompany());
        tvBreakContact.setText(mBrokenDocument.getContactPerson());
        tvBreakPhone.setText(mBrokenDocument.getPhoneNo());

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_no_solve://为消除
                        status = 0;
                        break;
                    case R.id.rb_solve://已消除
                        status = 1;
                        break;
                }
            }
        });

        mRadioGroup.check(R.id.rb_no_solve);

        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        add_img_special.setAdapter(gridViewAddImgesAdpter);
        add_img_special.getCodeResult(new AddImageGridView.AddCallBack() {
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

    private String getTowerNO(int id){
        Tower tower = TowerDBHelper.getInstance().getTower(id);
        if (tower==null)
            return "杆塔已删除";
        return tower.getTowerNo();
    }


    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                doSubMit();
                break;
        }
    }

    /**
     * 拍照
     */
    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            IMAGE_DIR = IOUtils.getRootStoragePath(this)  + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator+"外破";
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
        IMAGE_DIR = IOUtils.getRootStoragePath(this)  + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator +"外破";
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

    private void doSubMit() {
        Map<String, RequestBody> params = initParams();

        if (datas == null || datas.size() == 0) {
            ToastUtil.show("无特巡照片，请先拍照");
            return;
        }

        if (params == null) {
            return;
        }
        mProgressDialog.show();
        final BrokenInspectRecord record = createRecord();
        long id = insertRecord(record);//数据库插入一条数据
        record.setSysBrokenInspectRecordId(id);
        saveImagePaths(record);
        //本地数据库删除 该转档

        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        //外破图片
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("PatrolImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        mCall = RetrofitManager.getInstance().getService(ApiService.class)
                .postBrokenPatrolRegister(params, requestImgParts);
        mCall.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mProgressDialog.dismiss();
                BaseCallBack<String> body = response.body();
                if (body != null && body.getCode() == 1) {
                    ToastUtil.show("提交成功");
                    record.setUploadFlag(1);
                    BrokenInspectRecordDBHelper.getInstance().update(record);
                    if (record.getSysTaskId() != 0){
                        WorkSheetTaskDBHelper.getInstance().delete(record.getSysTaskId());
                        localBroadcastManager = LocalBroadcastManager.getInstance(SpecialInspectActivity.this);
                        Intent intent = new Intent(AppConstant.WORK_SHEET_NUM_DEL);
                        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        localBroadcastManager.sendBroadcast(intent);
                    }
                }else{
                    if (record.getSysTaskId() != 0){
                        ToastUtil.show("工单已被其他用户处理");
                    }else{
                        ToastUtil.show("提交到数据库");
                    }
                }
                jumpToRecordActivity();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                ToastUtil.show("提交到数据库");
                mProgressDialog.dismiss();
                jumpToRecordActivity();
            }
        });
    }

    private void saveImagePaths(BrokenInspectRecord brokenInspectRecord) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(4);
            imagePaths.setLocalId(brokenInspectRecord.getSysBrokenInspectRecordId()+"");
            imagePaths.setFatherPlatformId(brokenInspectRecord.getSysBrokenPatrolDetailId()+"");
            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }


    private Map<String, RequestBody> initParams() {
        Map<String, RequestBody> params = new HashMap<>();

        RequestBody contact = RequestBody.create(MediaType.parse("multipart/form-data"), mBrokenDocument.getPlatformId() + "");
        params.put("BrokenDocumentId", contact);

        RequestBody statu = RequestBody.create(MediaType.parse("multipart/form-data"), status + "");
        params.put("Status", statu);
        if (TextUtils.isEmpty(mEtRemark.getText())) {
            ToastUtil.show(getString(R.string.tip_input_remark));
            return null;
        }
        //remark
        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), mEtRemark.getText().toString());
        params.put("Remark", remark);

        if(mBrokenDocument.getSysTaskId() == 0){
            int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(mBrokenDocument.getPlatformId(),AppConstant.CROSS_DEFECT_SHEET);
            if (sysWorkId != -1){
                RequestBody sysTaskid = RequestBody.create(MediaType.parse("multipart/form-data"), sysWorkId+"");
                params.put("sysTaskId", sysTaskid);
                mBrokenDocument.setSysTaskId(sysWorkId);
            }
        }else{
            RequestBody sysTaskid = RequestBody.create(MediaType.parse("multipart/form-data"), mBrokenDocument.getSysTaskId()+"");
            params.put("sysTaskId", sysTaskid);
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                mBrokenDocument.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                mBrokenDocument.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            mBrokenDocument.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        return params;
    }

    private void jumpToRecordActivity() {
        Intent intent = new Intent(this, SpecialRecordActivity.class);
        if (FLAG != -1){
            Intent toMain = new Intent(this, MainActivity.class);
            intent.putExtra("toValue", AppConstant.WORK_SHEET);
            startActivity(toMain);
        }else if (mBrokenDocument.getPlatformId() != 0) {
            intent.putExtra("DocumentId", mBrokenDocument.getPlatformId());
            startActivity(intent);
        } else {
            intent.putExtra("DocumentId", mBrokenDocument.getId().intValue());
            startActivity(intent);
        }


        finish();
    }


    private BrokenInspectRecord createRecord() {
        BrokenInspectRecord inspectRecord = new BrokenInspectRecord();
        inspectRecord.setDocumentPlatformId(mBrokenDocument.getPlatformId());//平台外破 的Id
        inspectRecord.setRemark(mEtRemark.getText().toString());
        inspectRecord.setBrokenStatus(status);
        if (status == 1) {
            BrokenDocumentDBHelper.getInstance().delete(mBrokenDocument);
        }
        if (mBrokenDocument.getId() != null) {
            inspectRecord.setBrokenDocumentId(mBrokenDocument.getId().intValue());
        }
        inspectRecord.setCreateDate(DateUtil.format(new Date()));
        inspectRecord.setUploadFlag(0);
        int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(mBrokenDocument.getPlatformId(),AppConstant.BROKEN_SHEET);
        if (sysWorkId != -1){
            inspectRecord.setSysTaskId(sysWorkId);
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                inspectRecord.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                inspectRecord.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            inspectRecord.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }


        return inspectRecord;
    }

    private long insertRecord(BrokenInspectRecord record) {
        return BrokenInspectRecordDBHelper.getInstance().insert(record);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (FLAG != -1){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("toValue", AppConstant.WORK_SHEET);
            startActivity(intent);
        }
    }
}
