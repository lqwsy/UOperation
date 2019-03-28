package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.BaseRequestView;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.UpdateBrokenListEvent;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.BrokenTypeAdapter;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.adapter.UspinnerAdapter;
import com.uflycn.uoperation.util.CallBackHandler;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.FileUtil;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.xflyer.utils.ThreadPoolUtils;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 修改外破信息
 */
public class AlterBreakInfoActivity extends Activity implements BaseRequestView {

    @BindView(R.id.sp_break_type)
    Spinner mBrokenTypeSpinner;
    @BindView(R.id.et_break_conpany)
    EditText mEtCompany;
    @BindView(R.id.et_break_contact)
    EditText mEtContactPerson;
    @BindView(R.id.et_break_phone)
    EditText mEtPhoneNum;
    @BindView(R.id.et_break_description)
    EditText mEtDescreiption;
    @BindView(R.id.tv_line_name)
    TextView tvLineName;
    @BindView(R.id.tv_break_num)
    TextView tvBreakNum;
    @BindView(R.id.add_img_break)
    AddImageGridView add_img_break;


    @BindView(R.id.sp_break_tower_from)
    Spinner mSpinnerBreakFrom;
    @BindView(R.id.sp_break_tower_to)
    Spinner mSpinnerBreaktTo;


    private UspinnerAdapter mTowerFromAdapter;
    private UspinnerAdapter mTowerToAdapter;
    private BrokenDocument mBrokenDocument;
    private int status = -1;
    private ProgressDialog mProgressDialog;
    protected WeakReference<Context> mWeakReference;
    private BrokenTypeAdapter mTypeAdapter;
    private CallBackHandler mHandler;
    private List<ItemDetail> mBrokenTypes;
    private Call<BaseCallBack<String>> mSubMitCall = null;

    private Call<ResponseBody> mDownImage = null;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;
    private Map<String, String> downLoadID;
    private String zipPath;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alter_break_info);
        ButterKnife.bind(this);
        mWeakReference = new WeakReference<Context>(this);
        EventBus.getDefault().register(this);
        mHandler = new CallBackHandler(this);
        mBrokenTypes = new ArrayList<>();

        mBrokenDocument = (BrokenDocument) getIntent().getSerializableExtra("brokenDocument");

        getBrokenType();
        initState();
        initTower();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mSubMitCall != null && !mSubMitCall.isCanceled()) {
                    mSubMitCall.cancel();
                }
            }
        });
        initData();

    }

    private void getBrokenType() {
        mBrokenTypes = ItemDetailDBHelper.getInstance().getItem("外破类型");
        updateList(mBrokenTypes);
    }


    private void initData() {
        tvLineName.setText(mBrokenDocument.getLineName());
        tvBreakNum.setText(mBrokenDocument.getDocmentNo());
        //mTowerFromTo.setText(mBrokenDocument.getStartTowerNo()+"-"+mBrokenDocument.getEndTowerNo());
        mEtCompany.setText(mBrokenDocument.getCompany());
        mEtContactPerson.setText(mBrokenDocument.getContactPerson());
        mEtPhoneNum.setText(mBrokenDocument.getPhoneNo());
        mEtDescreiption.setText(mBrokenDocument.getRemark());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载外破信息");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        datas = new ArrayList();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        add_img_break.setAdapter(gridViewAddImgesAdpter);
        add_img_break.getCodeResult(new AddImageGridView.AddCallBack() {
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
        zipPath = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH +
                File.separator + AppConstant.BREAK_PHOTO_PATH;
        BreakPicAsyncTask breakPicAsyncTask = new BreakPicAsyncTask();
        breakPicAsyncTask.execute(mBrokenDocument.getPlatformId());
    }


    @OnClick({R.id.iv_back, R.id.tv_oldPic})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_oldPic:
                jumpToPhotoGrid();
                break;
        }
    }

    private void jumpToPhotoGrid() {
        Intent photo = new Intent(this, PhotoGridActivity.class);
        photo.putExtra("path", zipPath);
        startActivity(photo);
    }

    /**
     * 拍照
     */
    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            IMAGE_DIR = IOUtils.getRootStoragePath(mWeakReference.get()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "外破";
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

    private void initState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void updateList(List datas) {
        if (datas.size() == 0) {
            return;
        }
        if (mTypeAdapter == null) {
            mTypeAdapter = new BrokenTypeAdapter(datas, mWeakReference.get(), R.layout.item_text_spinner);
            mBrokenTypeSpinner.setAdapter(mTypeAdapter);
            int count = mTypeAdapter.getCount();
            for (int i = 0; i < count; i++) {
                ItemDetail brokenType = mTypeAdapter.getItem(i);
                if (mBrokenDocument.getBrokenType().equals(String.valueOf(brokenType.getItemDetailsId()))) {
                    mBrokenTypeSpinner.setSelection(i);// 默认选中项
                    break;
                }
            }
        } else {
            mTypeAdapter.onDataChange(datas);
        }
    }

    @Override
    public void handRequestErr(String message) {
        ToastUtil.show(mWeakReference.get(), message);
        Log.i("handRequestErr", message);
    }

    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                doSubMit();
                break;
        }
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
        IMAGE_DIR = IOUtils.getRootStoragePath(mWeakReference.get()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "外破";
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


    private Map<String, RequestBody> initParams() {
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), mBrokenDocument.getPlatformId() + "");
        params.put("sysBrokenDocumentId", id);

        //外破类型
        int pos = mBrokenTypeSpinner.getSelectedItemPosition();
        RequestBody brokeType = RequestBody.create(MediaType.parse("multipart/form-data"), mBrokenTypes.get(pos).getItemDetailsId());
        params.put("BrokenType", brokeType);

        String companys = mEtCompany.getText().toString();
        String contactPerson = mEtContactPerson.getText().toString();
        String phoneNum = mEtPhoneNum.getText().toString();
        String desc = mEtDescreiption.getText().toString();
        //StartTowerId
        if (mSpinnerBreakFrom.getSelectedItemPosition() == -1) {
            return null;
        }
        if (mSpinnerBreaktTo.getSelectedItemPosition() == -1) {
            return null;
        }
        Tower startTower = (Tower) mTowerFromAdapter.getItem(mSpinnerBreakFrom.getSelectedItemPosition());
        RequestBody startTowerRequest = RequestBody.create(MediaType.parse("multipart/form-data"), startTower.getSysTowerID() + "");
        params.put("StartTowerId", startTowerRequest);
        //EndTowerId
        Tower endTower = (Tower) mTowerToAdapter.getItem(mSpinnerBreaktTo.getSelectedItemPosition());
        RequestBody endTowerRequest = RequestBody.create(MediaType.parse("multipart/form-data"), endTower.getSysTowerID() + "");
        params.put("EndTowerId", endTowerRequest);
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                RequestBody PlanDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
                params.put("PlanDailyPlanSectionIDs", PlanDailyPlanSectionIDs);
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                RequestBody PlanDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
                params.put("PlanDailyPlanSectionIDs", PlanDailyPlanSectionIDs);
            }

        } else if (MyApplication.gridlineTaskStatus == 3) {
            RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mPlanPatrolExecutionId);
            params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        }
        if (TextUtils.isEmpty(companys) || TextUtils.isEmpty(contactPerson) || TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(desc)) {
            return null;
        } else {
            //公司
            RequestBody company = RequestBody.create(MediaType.parse("multipart/form-data"), companys);
            params.put("Company", company);
            //联系人
            RequestBody contact = RequestBody.create(MediaType.parse("multipart/form-data"), contactPerson);
            params.put("ContactPerson", contact);
            //电话
            RequestBody phone = RequestBody.create(MediaType.parse("multipart/form-data"), phoneNum);
            params.put("PhoneNo", phone);
            //remark
            RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
            params.put("Remark", remark);
            mBrokenDocument.setBrokenType(mBrokenTypes.get(pos).getItemDetailsId());
            mBrokenDocument.setStartTowerId(startTower.getSysTowerID().intValue());
            mBrokenDocument.setEndTowerId(endTower.getSysTowerID().intValue());
            mBrokenDocument.setCompany(companys);
            mBrokenDocument.setContactPerson(contactPerson);
            mBrokenDocument.setPhoneNo(phoneNum);
            mBrokenDocument.setRemark(desc);
            if (MyApplication.gridlineTaskStatus == 2) {
                if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                    mBrokenDocument.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
                } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                    mBrokenDocument.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
                }
            } else if (MyApplication.gridlineTaskStatus == 3) {
                mBrokenDocument.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
            }
            return params;
        }
    }


    private void doSubMit() {
        Map<String, RequestBody> params = initParams();
        if (params == null) {
            ToastUtil.show("数据填写不完整,请输入缺少数据");
            return;
        }
        mProgressDialog.show();

        if (datas == null || datas.size() == 0) {
            mSubMitCall = RetrofitManager.getInstance().getService(ApiService.class).postUpdateBrokenDocument(params, null);
        } else {
            List<MultipartBody.Part> requestImgParts = new ArrayList<>();
            //外破图片
            for (int i = 0; i < datas.size(); i++) {
                File file = new File((String) datas.get(i).get("path"));
                RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("BrokenImage" + i, file.getName(), imgFile);
                requestImgParts.add(requestImgPart);
            }
            mSubMitCall = RetrofitManager.getInstance().getService(ApiService.class).postUpdateBrokenDocument(params, requestImgParts);
        }

        mBrokenDocument.setUploadFlag(0);
        mBrokenDocument.setUpdateTime(DateUtil.format(new Date()));

        BrokenDocumentDBHelper.getInstance().updateById(mBrokenDocument);
        saveImagePaths(mBrokenDocument);
        //无论上传是否成功 界面都将关闭
        mSubMitCall.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {//上传成功 或者失败都会刷新列表
                    mBrokenDocument.setUploadFlag(1);
                    BrokenDocumentDBHelper.getInstance().updateById(mBrokenDocument);
                }
                EventBus.getDefault().post(new UpdateBrokenListEvent());
                mProgressDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                mProgressDialog.dismiss();
                EventBus.getDefault().post(new UpdateBrokenListEvent());
                finish();
            }
        });
    }

    private void saveImagePaths(BrokenDocument brokenDocument) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(3);
            imagePaths.setLocalId(brokenDocument.getId() + "");
            imagePaths.setFatherPlatformId(brokenDocument.getPlatformId() + "");
            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mSubMitCall != null && !mSubMitCall.isCanceled()) {
            mSubMitCall.cancel();
        }
    }

    @Subscribe
    public void onMainThread(Object event) {
    }

    private void initTower() {
        Tower startTower = TowerDBHelper.getInstance().getTower(mBrokenDocument.getStartTowerId());
        if (startTower == null) {
            ToastUtil.show("该外破起始塔已被删除");
            return;
        }
        Tower endTower = TowerDBHelper.getInstance().getTower(mBrokenDocument.getEndTowerId());
        if (endTower == null) {
            ToastUtil.show("该外破终点塔已被删除");
            return;
        }
        List<Tower> towerList = TowerDBHelper.getInstance().getLineTowers(startTower.getSysGridLineId());
        mTowerFromAdapter = new UspinnerAdapter(towerList, mWeakReference.get());
        mTowerToAdapter = new UspinnerAdapter(towerList, mWeakReference.get());
        mSpinnerBreakFrom.setAdapter(mTowerFromAdapter);
        mSpinnerBreakFrom.setSelection(towerList.indexOf(startTower));
        mSpinnerBreaktTo.setAdapter(mTowerToAdapter);
        mSpinnerBreaktTo.setSelection(towerList.indexOf(endTower));
    }


    class BreakPicAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(final Integer... params) {
            FileUtil.makeDir(zipPath);

            zipPath = zipPath + File.separator + params[0] + ".zip";
            File file = new File(zipPath.substring(0, zipPath.lastIndexOf(".")));
            mDownImage = RetrofitManager.getInstance().getService(ApiService.class).getImageZip(3, params[0]);
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
                    if (t != null) {
                        Log.i("BreakPicAsyncTask", t.getMessage());
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


