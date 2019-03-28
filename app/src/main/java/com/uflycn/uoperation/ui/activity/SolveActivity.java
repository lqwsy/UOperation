package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectDeleteRecord;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.UpdateDefectList;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.DefectDeleteDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.greendao.TreeDefectDBHelper;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.FileUtil;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.StringUtils;
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

import com.alibaba.fastjson.JSONArray;


/**
 * 消缺页面
 */
public class SolveActivity extends Activity {

    @BindView(R.id.tv_line_name)
    TextView tv_line_name;
    @BindView(R.id.tv_tower_id)
    TextView tv_tower_id;
    @BindView(R.id.tv_defect_class2)
    TextView tvDefectClass;
    @BindView(R.id.et_defect_describe)
    TextView et_defect_describe;
    @BindView(R.id.et_solve_defect_remark)
    EditText et_solve_defect_remark;
    @BindView(R.id.add_img_defect)
    AddImageGridView add_img_defect;
    @BindView(R.id.ll_clean_tree_count)
    LinearLayout mLlCleanTreeCount;
    @BindView(R.id.et_clean_tree_count)
    EditText mEtCleanTreeCount;

    private Call<ResponseBody> mDownImage = null;
    private ProgressDialog mProgressDialog;
    private DefectBean mChannelDefectBean;
    private TreeDefectPointBean mTreeDefectPointBean;
    private Call<BaseCallBack<String>> mSubmitCall;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter addGridViewAdapter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH_ADD = 3;
    private String zipPath;
    private ProgressDialog progressDialog;
    private int FLAG = -1;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_solve);
        //        initState();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        Object object = getIntent().getSerializableExtra("channelDefectBean");
        FLAG = getIntent().getIntExtra("FLAG", -1);
        if (object instanceof DefectBean) {
            mChannelDefectBean = (DefectBean) object;
            mLlCleanTreeCount.setVisibility(View.GONE);
        } else if (object instanceof TreeDefectPointBean) {
            mTreeDefectPointBean = (TreeDefectPointBean) object;
            mLlCleanTreeCount.setVisibility(View.VISIBLE);
        }
        initData();
    }


    private void initData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载缺陷信息");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mSubmitCall != null && !mSubmitCall.isCanceled()) {
                    mSubmitCall.cancel();
                }
            }
        });
        SolvePicAsyncTask solvePicAsyncTask = new SolvePicAsyncTask();
        zipPath = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH +
                File.separator + AppConstant.BREAK_PHOTO_PATH;
        if (mChannelDefectBean != null) {
            String num = mChannelDefectBean.getTowerNo();
            if (mChannelDefectBean.getNearTowerID() != 0) {
                Tower tower = TowerDBHelper.getInstance().getTower(mChannelDefectBean.getNearTowerID());
                if (tower == null) {
                    mChannelDefectBean.setNearTowerNo("");
                } else {
                    mChannelDefectBean.setNearTowerNo(tower.getTowerNo());
                }

            }
            if (mChannelDefectBean.getNearTowerNo() != null) {
                if (!mChannelDefectBean.getNearTowerNo().equalsIgnoreCase("")) {
                    num = num + "-" + mChannelDefectBean.getNearTowerNo();
                }
            }
            tv_line_name.setText(mChannelDefectBean.getLineName());
            tv_tower_id.setText(num);
            if (mChannelDefectBean.getDefectCategory().equals("1")) {
                tvDefectClass.setText("缺陷");
            } else if (mChannelDefectBean.getDefectCategory().equals("2")) {
                tvDefectClass.setText("隐患");
            } else {
                tvDefectClass.setText("其他");
            }
            et_defect_describe.setText(mChannelDefectBean.getRemark());

            solvePicAsyncTask.execute("10_" + mChannelDefectBean.getSysTowerDefectId());
        } else if (mTreeDefectPointBean != null) {
            String num = mTreeDefectPointBean.getTowerA_Name();
            if (mTreeDefectPointBean.getTowerB_Name() != null) {
                num = num + "-" + mTreeDefectPointBean.getTowerB_Name();
            }
            tv_line_name.setText(mTreeDefectPointBean.getLineName());
            tv_tower_id.setText(num);
            tvDefectClass.setText(mTreeDefectPointBean.getTreeDefectPointType());
            et_defect_describe.setText(mTreeDefectPointBean.getRemark());

            solvePicAsyncTask.execute("13_" + mTreeDefectPointBean.getSysTreeDefectPointID().intValue());
        }

        datas = new ArrayList();
        addGridViewAdapter = new GridViewAddImgesAdpter(datas, this);
        addGridViewAdapter.setMaxImages(MAX_IMAGE_LENGTH_ADD);
        add_img_defect.setAdapter(addGridViewAdapter);
        add_img_defect.getCodeResult(new AddImageGridView.AddCallBack() {
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

    @OnClick({R.id.btn_submit, R.id.iv_back, R.id.tv_oldPic})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                doSubmit();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_oldPic:
                jumpToPhotoGrid();
                break;
            default:
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
            IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "缺陷";
            File dir = new File(IMAGE_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //String fileName = IMAGE_DIR + File.separator + DateUtil.format(new Date()) + ".jpg";
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
        int nowNeed = addGridViewAdapter.getMaxImages() - addGridViewAdapter.getCount() + 1;
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
        IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "缺陷";
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
        addGridViewAdapter.notifyDataSetChanged();
    }


    private void doSubmit() {
        if (et_solve_defect_remark.getText().toString().trim().isEmpty()) {
            ToastUtil.show("请先填写消缺备注");
            return;
        }

        Map<String, RequestBody> params = initParams();

        if (datas == null || datas.size() == 0) {
            ToastUtil.show("请先进行拍照");
            return;
        }
        final DefectDeleteRecord record = create();
        if (record == null) {
            return;
        }
        List<ImagePaths> imagePath = new ArrayList<>();

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(SolveActivity.this);
        int result = DefectDeleteDBHelper.getInstance().insert(record);

        if (result == -1) {
            normalDialog.setTitle("提示");
            normalDialog.setMessage("已经提交过消缺请求，请勿重复提交。\n如列表中还存在该工单，您可以在联网状态下重新登陆。");
            normalDialog.setCancelable(true);
            normalDialog.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SolveActivity.this.finish();
                        }
                    });
            normalDialog.show();

            try {
                Looper.loop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //        mProgressDialog.show();
        if (mChannelDefectBean != null) {
            for (int i = 0; i < datas.size(); i++) {
                ImagePaths imagePaths = new ImagePaths();
                imagePaths.setCategory(11);
                if (mChannelDefectBean.getId() == null) {
                    mChannelDefectBean.setId((long) 0);
                }
                imagePaths.setLocalId(mChannelDefectBean.getId() + "");
                imagePaths.setFatherPlatformId(mChannelDefectBean.getSysTowerDefectId() + "");
                imagePaths.setPath((String) datas.get(i).get("path"));
                imagePath.add(imagePaths);
            }
            DefectBeanDBHelper.getInstance().deleteDefectBean(mChannelDefectBean);
        } else {
            for (int i = 0; i < datas.size(); i++) {
                ImagePaths imagePaths = new ImagePaths();
                imagePaths.setCategory(14);
                imagePaths.setLocalId(mTreeDefectPointBean.getSysTreeDefectPointID() + "");
                imagePaths.setFatherPlatformId(mTreeDefectPointBean.getSysTreeDefectPointID().intValue() + "");
                imagePaths.setPath((String) datas.get(i).get("path"));
                imagePath.add(imagePaths);
            }
            TreeDefectDBHelper.getInstance().deleteTreePointDefect(mTreeDefectPointBean);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);//数据库存储
        postSolveNet(params, record);

    }

    private void postSolveNet(final Map<String, RequestBody> params, final DefectDeleteRecord record) {
        mProgressDialog.show();
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        //外破图片
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CheckedPhoto" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        if (mChannelDefectBean != null) {
            mSubmitCall = RetrofitManager.getInstance().getService(ApiService.class).postClearDefect(params, requestImgParts);
        } else {
            mSubmitCall = RetrofitManager.getInstance().getService(ApiService.class).postClearTreeDefect(params, requestImgParts);
        }

        Log.d("lqwtest", "params="+ JSONArray.toJSONString(params));
        Log.d("lqwtest", "request_img_part=" + new Gson().toJson(requestImgParts));

        mSubmitCall.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                Log.d("lqwtest", "mSubmitCall response" + new Gson().toJson(response));
                BaseCallBack<String> body = response.body();
                if (body == null) {
                    retryPost(params, record);
                    return;
                }
                if (body != null && body.getCode() == 1) {
                    ToastUtil.show("提交成功");
                    AppConstant.isDisDefeect = true;
                    record.setUploadFlag(1);
                    DefectDeleteDBHelper.getInstance().update(record);

                    if (record.getSysTaskId() != 0) {
                        WorkSheetTaskDBHelper.getInstance().delete(record.getSysTaskId());
                        localBroadcastManager = LocalBroadcastManager.getInstance(SolveActivity.this);
                        Intent intent = new Intent(AppConstant.WORK_SHEET_NUM_DEL);
                        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        localBroadcastManager.sendBroadcast(intent);
                    }
                } else if (response.code() == 401) {
                    //                    ToastUtil.show("提交到数据库,请重新登陆");
                    retryPost(params, record);
                    return;
                } else if (response.body().getMessage() != null && !response.body().getMessage().isEmpty()) {
                    ToastUtil.show(response.body().getMessage() + "\n工单已被其他用户处理");
                }
                mProgressDialog.dismiss();
                if (FLAG != -1) {
                    Intent intent = new Intent(SolveActivity.this, MainActivity.class);
                    intent.putExtra("toValue", AppConstant.WORK_SHEET);
                    startActivity(intent);
                } else {
                    EventBus.getDefault().post(new UpdateDefectList(null));
                }
                finish();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                //                ToastUtil.show("提交到数据库");
                mProgressDialog.dismiss();
                retryPost(params, record);
            }
        });
    }

    private void retryPost(final Map<String, RequestBody> params, final DefectDeleteRecord record) {
        DialogUtils.showRetryPostDialog(getApplicationContext(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //重新提交
                postSolveNet(params, record);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (FLAG != -1) {
                    Intent intent = new Intent(SolveActivity.this, MainActivity.class);
                    intent.putExtra("toValue", AppConstant.WORK_SHEET);
                    startActivity(intent);
                } else {
                    EventBus.getDefault().post(new UpdateDefectList(null));
                }
                finish();
            }
        });
    }

    private Map<String, RequestBody> initParams() {
        Map<String, RequestBody> params = new HashMap<>();
        String id = "";
        if (mChannelDefectBean != null) {
            id = mChannelDefectBean.getSysTowerDefectId() + "";
        } else {
            id = mTreeDefectPointBean.getSysTreeDefectPointID() + "";
            Log.d("lqwtest","initParams mTreeDefectPointBean id="+id);
            RequestBody treeCutCount;
            if (!StringUtils.isEmptyOrNull(mEtCleanTreeCount.getText().toString())) {
                Log.d("lqwtest","mEtCleanTreeCount.getText().toString()="+mEtCleanTreeCount.getText().toString());
                treeCutCount = RequestBody.create(MediaType.parse("multipart/form-data"), mEtCleanTreeCount.getText().toString());
            } else {
                Log.d("lqwtest","treeCutCount is null");
                treeCutCount = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            }
            Log.d("lqwtest","treeCutCount:"+ new Gson().toJson(treeCutCount));
            params.put("TreeCutCount", treeCutCount);
        }

        RequestBody defectId = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        Log.d("lqwtest","sysDefectId:"+ new Gson().toJson(defectId));
        params.put("sysDefectId", defectId);

        //remark
        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), et_solve_defect_remark.getText().toString().trim());
        Log.d("lqwtest","Remark:"+ et_solve_defect_remark.getText().toString().trim());
        params.put("Remark", remark);

        int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(id, 3);
        if (sysWorkId != -1) {
            RequestBody sysWorkID = RequestBody.create(MediaType.parse("multipart/form-data"), sysWorkId + "");
            params.put("sysTaskId", sysWorkID);
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            int towerId;
            if (mChannelDefectBean != null) {
                towerId = mChannelDefectBean.getSysTowerID();
            } else {
                towerId = mTreeDefectPointBean.getTowerA_Id();
            }
            Tower t = TowerDBHelper.getInstance().getTower(towerId);
            if (t != null && MyApplication.mDayPlanLineMap.get(t.getSysGridLineId()) != null) {
                RequestBody PlanDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mDayPlanLineMap.get(t.getSysGridLineId()));
                params.put("PlanDailyPlanSectionIDs", PlanDailyPlanSectionIDs);
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mPlanPatrolExecutionId);
            params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        }
        return params;
    }

    private DefectDeleteRecord create() {
        DefectDeleteRecord record = new DefectDeleteRecord();
        record.setCreateDate(DateUtil.format(new Date()));
        int id = -1;
        if (mChannelDefectBean != null) {
            id = mChannelDefectBean.getSysTowerDefectId();
            if (mChannelDefectBean.getId() != null) {
                record.setLocalDefectId(mChannelDefectBean.getId().intValue() + "");
            }
        } else {
            id = mTreeDefectPointBean.getSysTreeDefectPointID().intValue();
            record.setLocalDefectId(id + "");
            record.setDefectCategory(1);
            if (!StringUtils.isEmptyOrNull(mEtCleanTreeCount.getText().toString())) {
                record.setTreeCutCount(Integer.parseInt(mEtCleanTreeCount.getText().toString()));
            }
        }
        record.setSysDefectId(id + "");
        record.setRemark(et_solve_defect_remark.getText().toString().trim());
        record.setUploadFlag(0);
        int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(id + "", 3);
        if (sysWorkId != -1) {
            record.setSysTaskId(sysWorkId);
        }
        int towerId;
        if (mChannelDefectBean != null) {
            towerId = mChannelDefectBean.getSysTowerID();
        } else {
            towerId = mTreeDefectPointBean.getTowerA_Id();
        }
        Tower t = TowerDBHelper.getInstance().getTower(towerId);
        if (t != null && MyApplication.mDayPlanLineMap.get(t.getSysGridLineId()) != null) {
            record.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(t.getSysGridLineId()));
        }

        return record;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mSubmitCall != null && !mSubmitCall.isCanceled()) {
            mSubmitCall.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (FLAG != -1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("toValue", AppConstant.WORK_SHEET);
            startActivity(intent);
        }
    }

    @Subscribe
    public void onEventMainTread(Object object) {

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

