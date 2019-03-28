package com.uflycn.uoperation.ui.activity.workManage.view;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordInfo;
import com.uflycn.uoperation.bean.PlanPatrolExecutionWorkRecordSubmit;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.workManage.presenter.AddWorkManagePresenterImp;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.adapter.PlanPatrolExecutionRecordInfoAdapter;
import com.uflycn.uoperation.ui.adapter.PlanPatrolExecutionRecordTowerInfoAdapter;
import com.uflycn.uoperation.ui.adapter.SpinnerVcAdapter;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
 * 新增提交
 */
public class AddWorkManageActivity extends Activity implements AddWorkManageView.PlanPatrolExecutionRecordInfoView {

    @BindView(R.id.top_layout)
    RelativeLayout mTopLayout;
    @BindView(R.id.sp_line_name)
    Spinner mSpLineName;//线路名称
    @BindView(R.id.sp_tower_from)
    Spinner mSpTowerFrom;
    @BindView(R.id.sp_tower_to)
    Spinner mSpTowerTo;
    @BindView(R.id.sp_work_type)
    Spinner mSpWorkType;
    @BindView(R.id.et_project_description)
    EditText mEtProjectDescription;
    @BindView(R.id.add_img_project)
    AddImageGridView mAddImgProject;
    @BindView(R.id.sp_work_project)
    Spinner mSpWorkProject;//作业项目
    private SpinnerVcAdapter mVoltageClassAdapter;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int MAX_IMAGE_LENGTH = 3;
    private File tempFile;
    private String IMAGE_DIR;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private ProgressDialog mProgressDialog;
    private AddWorkManagePresenterImp mPresenter;
    private PlanPatrolExecutionRecordInfoAdapter mAdapter;
    private PlanPatrolExecutionRecordTowerInfoAdapter mTowerAdapter;
    private List<String> mTypeOfWorkList;
    private List<PlanPatrolExecutionWorkRecordInfo> mInfoList;
    private Call<BaseCallBack<List<String>>> mCall;

    public static Intent newInstance(Context context) {
        return new Intent(context, AddWorkManageActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_add_work_manage);

        ButterKnife.bind(this);
        mPresenter = new AddWorkManagePresenterImp(this);
        initView();
        initData();
    }

    private void initData() {
        if (StringUtils.isEmptyOrNull(MyApplication.mPlanPatrolExecutionId)) {
            ToastUtil.show("请先开启我的任务");
            return;
        }
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
        mPresenter.getPlanPatrolExecutionRecordInfo(MyApplication.mPlanPatrolExecutionId);
        mTypeOfWorkList = Arrays.asList(getResources().getStringArray(R.array.type_of_work));
        mSpWorkType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mTypeOfWorkList));
        mSpWorkType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpWorkProject.setAdapter(getAdapter(mSpWorkType.getSelectedItem().toString(), "请选择作业项目"));//设置数据
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpWorkProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateGridLineSp((SpinnerOption) mSpWorkProject.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpLineName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTowerSp();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateTowerSp();
            }
        });
    }

    private void updateTowerSp() {
        if (mSpLineName.getSelectedItem() == null) {
            mTowerAdapter.onDataChange(new ArrayList<Tower>());//加载数据
            return;
        }
        PlanPatrolExecutionWorkRecordInfo planPatrolExecutionWorkRecordInfo = (PlanPatrolExecutionWorkRecordInfo) mSpLineName.getSelectedItem();
        List<Tower> list = planPatrolExecutionWorkRecordInfo.getTowerList();
        mTowerAdapter.onDataChange(list);
//        mProgressDialog.dismiss();
    }

    private void updateGridLineSp(SpinnerOption selectedItem) {
        if (mInfoList == null) {
            mAdapter.onDataChange(new ArrayList<PlanPatrolExecutionWorkRecordInfo>());
            return;
        }
        List<PlanPatrolExecutionWorkRecordInfo> list = new ArrayList<>();
        for (int i = 0; i < mInfoList.size(); i++) {
            if (mInfoList.get(i).getTypeOfWork().equals(selectedItem.getValue())) {
                list.add(mInfoList.get(i));
            }
        }
        mAdapter.onDataChange(list);
    }

    private void initView() {
        mVoltageClassAdapter = new SpinnerVcAdapter(ItemDetailDBHelper.getInstance().getItem("电压等级"), this);
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        mAddImgProject.setAdapter(gridViewAddImgesAdpter);
        mAddImgProject.getCodeResult(new AddImageGridView.AddCallBack() {
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
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在联网加载数据...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mPresenter.getPlanPatrolExecutionRecordInfo(MyApplication.mPlanPatrolExecutionId);
            }
        });
        mAdapter = new PlanPatrolExecutionRecordInfoAdapter(new ArrayList<PlanPatrolExecutionWorkRecordInfo>(), this, R.layout.item_plan_patrol_execution_record_info);
        mSpLineName.setAdapter(mAdapter);
        mTowerAdapter = new PlanPatrolExecutionRecordTowerInfoAdapter(new ArrayList<Tower>(), this, R.layout.item_plan_patrol_execution_record_info);
        mSpTowerFrom.setAdapter(mTowerAdapter);
        mSpTowerTo.setAdapter(mTowerAdapter);
    }


    /**
     * 拍照
     */
    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "作业";
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


    public ArrayAdapter<SpinnerOption> getAdapter(String msg, String title) {
        List<ItemDetail> itemDetails = ItemDetailDBHelper.getInstance().getItem(msg);
        List<SpinnerOption> towerTypeList = new ArrayList<>();
        for (int i = 0; i < itemDetails.size(); i++) {
            towerTypeList.add(new SpinnerOption(itemDetails.get(i).getItemDetailsId() + "", itemDetails.get(i).getItemsName()));
        }
        return getSpinnerAdapter(title, towerTypeList);
    }

    private ArrayAdapter<SpinnerOption> getSpinnerAdapter(String message, List<SpinnerOption> mItem) {
        SpinnerOption firstSpinner = new SpinnerOption("", message);
        mItem.add(0, firstSpinner);
        ArrayAdapter<SpinnerOption> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mItem);
        return adapter;
    }

    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
        }
    }

    private void doSubmit() {
        if (mSpWorkProject.getSelectedItem()==null||StringUtils.isEmptyOrNull(((SpinnerOption) mSpWorkProject.getSelectedItem()).getValue())) {
            ToastUtil.show("请先选择作业项目");
            return;
        }
        if (mSpLineName.getSelectedItem() == null) {
            ToastUtil.show("请先选择线路名称");
            return;
        }
        if (mSpTowerTo.getSelectedItem() == null || mSpTowerFrom.getSelectedItem() == null) {
            ToastUtil.show("请先选择起止杆塔");
            return;
        }
        if (mEtProjectDescription.getText().toString().equals("")){
            ToastUtil.show("请填写完成情况");
            return;
        }
        PlanPatrolExecutionWorkRecordSubmit submit = new PlanPatrolExecutionWorkRecordSubmit();
        submit.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        submit.setTypeOfWork(((SpinnerOption) mSpWorkProject.getSelectedItem()).getValue());
        submit.setStartTowerID(((Tower) mSpTowerFrom.getSelectedItem()).getSysTowerID().intValue());
        submit.setEndTowerID(((Tower) mSpTowerTo.getSelectedItem()).getSysTowerID().intValue());
        submit.setWorkNote(mEtProjectDescription.getText().toString());
        postSubmit(submit);
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
        IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "项目";
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


    @Override
    public void onSuccess(List<PlanPatrolExecutionWorkRecordInfo> list) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mInfoList = list;
    }

    @Override
    public void onFailed(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        ToastUtil.show(msg);
    }

    private void postSubmit(final PlanPatrolExecutionWorkRecordSubmit submit) {
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.show();
        final List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ImageData" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        Gson gson = new Gson();
        String json = gson.toJson(submit);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        mCall = RetrofitManager.getInstance().getService(ApiService.class).postPlanPatrolExecutionWorkRecordSubmit(body, requestImgParts);
        mCall.enqueue(new Callback<BaseCallBack<List<String>>>() {

            @Override
            public void onResponse(Call<BaseCallBack<List<String>>> call, Response<BaseCallBack<List<String>>> response) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (response != null && response.body() != null) {
                    if (response.body().getCode() == 1) {
                        ToastUtil.show("提交成功");
                        finish();
                    } else {
                        ToastUtil.show(response.body().getMessage());
                    }
                } else {
                    ToastUtil.show("提交数据失败");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<String>>> call, Throwable t) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                ToastUtil.show("网络请求出错");
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        super.onDestroy();
    }
}
