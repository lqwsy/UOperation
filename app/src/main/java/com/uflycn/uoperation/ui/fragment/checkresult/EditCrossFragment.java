package com.uflycn.uoperation.ui.fragment.checkresult;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.LineCrossType;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.UpdateLineCrossList;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.PhotoGridActivity;
import com.uflycn.uoperation.ui.activity.TourResultActivity;
import com.uflycn.uoperation.ui.adapter.CrossTypeAdapter;
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
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;
import com.xflyer.utils.ThreadPoolUtils;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * 交跨修改
 */
public class EditCrossFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private LineCrossEntity mLineCrossEntity;
    @BindView(R.id.sp_cross_type)
    Spinner mCrossTypeSpinner;
    @BindView(R.id.sp_cross_subtype)
    Spinner mCrossSubTypeSpinner;
    @BindView(R.id.et_get_distance)
    EditText mDistance;
    @BindView(R.id.et_absolute_height)
    EditText mAbsoluteHeight;
    @BindView(R.id.et_cross_description)
    EditText mCrossDesc;
    @BindView(R.id.tv_grid_name)
    TextView tvGridName;
    @BindView(R.id.tv_tower_num)
    TextView tvTowerNum;
    @BindView(R.id.et_cross_angle)
    EditText etCrossAngle;
    @BindView(R.id.add_img_cross)
    AddImageGridView add_img_cross;

    private static Reference<Context> mReference;
    private Call<BaseCallBack<String>> mSubMitCall;
    private Call<BaseCallBack<List<LineCrossType>>> mGetBrokenTypeCall;
    private ProgressDialog mProgressDialog;
    private CrossTypeAdapter mFirstLevelAdapter;//第一级菜单
    private CrossTypeAdapter mSecondLevelAdapter;

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
    private static int FLAG;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_edit_cross, container, false);
        ButterKnife.bind(this, contentView);
        mReference = new WeakReference<Context>(getActivity());
        initDatas();
        return contentView;
    }

    public void setCurrentLineCrossEntity(LineCrossEntity entity, int flag) {
        mLineCrossEntity = entity;
        FLAG = flag;
    }

    //初始化值
    private void initDatas() {
        mProgressDialog = new ProgressDialog(mReference.get());
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        tvGridName.setText(mLineCrossEntity.getLineName());

        tvTowerNum.setText(mLineCrossEntity.getStartTower() + "-" + mLineCrossEntity.getEndTower());
        if (mLineCrossEntity.getStartTower() == null) {
            tvTowerNum.setText(getTowerNo(mLineCrossEntity.getStartTowerId()) + "-" + getTowerNo(mLineCrossEntity.getEndTowerId()));
        }


        mDistance.setText(mLineCrossEntity.getDtoSmartTower() + "");
        mAbsoluteHeight.setText(mLineCrossEntity.getClearance() + "");
        etCrossAngle.setText(mLineCrossEntity.getCrossAngle() + "");
        mCrossDesc.setText(mLineCrossEntity.getRemark());
        initCrossType();

        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("正在加载交跨信息");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mGetBrokenTypeCall != null && !mGetBrokenTypeCall.isCanceled()) {
                    mGetBrokenTypeCall.cancel();
                }
                if (mSubMitCall != null && !mSubMitCall.isCanceled()) {
                    mSubMitCall.cancel();
                }
            }
        });
        datas = new ArrayList();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this.getContext());
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
        zipPath = IOUtils.getRootStoragePath(this.getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH +
                File.separator + AppConstant.BREAK_PHOTO_PATH;
        CrossPicAsyncTask breakPicAsyncTask = new CrossPicAsyncTask();
        breakPicAsyncTask.execute(mLineCrossEntity.getPlatformId());
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
            IMAGE_DIR = IOUtils.getRootStoragePath(this.getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
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
        Intent intent = new Intent(this.getContext(), PhotoSelectorActivity.class);
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
        IMAGE_DIR = IOUtils.getRootStoragePath(this.getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
        FileUtils.changeBitmapPath(path, fileName);
        SendBroadcasd2MEDIA.sendBroadcasd(getActivity(), IMAGE_DIR);
        Map<String, Object> map = new HashMap<>();
        map.put("path", fileName);
        datas.add(map);
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainTread(BaseMainThreadEvent event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGetBrokenTypeCall != null && !mGetBrokenTypeCall.isCanceled()) {
            mGetBrokenTypeCall.cancel();
        }
        if (mSubMitCall != null && !mSubMitCall.isCanceled()) {
            mSubMitCall.cancel();
        }
    }

    private void initCrossType() {
        initSpinnerSelected();
        //第一级菜单
        List<DefectType> firstLevel = DefectTypeDBHelper.getInstance().getDefectType(3);
        mFirstLevelAdapter = new CrossTypeAdapter(firstLevel, mReference.get(), R.layout.item_text_spinner);
        mCrossTypeSpinner.setAdapter(mFirstLevelAdapter);

        DefectType fistDefect = DefectTypeDBHelper.getInstance().getParentDefect(mLineCrossEntity.getCrossType());
        mCrossTypeSpinner.setSelection(firstLevel.indexOf(fistDefect));

        List<DefectType> secondLevels = DefectTypeDBHelper.getInstance().getChilds(fistDefect.getSysDefectTypeID().intValue());
        mSecondLevelAdapter = new CrossTypeAdapter(secondLevels, mReference.get(), R.layout.item_text_spinner);
        mCrossSubTypeSpinner.setAdapter(mSecondLevelAdapter);
        mCrossSubTypeSpinner.setSelection(secondLevels.indexOf(DefectTypeDBHelper.getInstance().getDefectType((long) mLineCrossEntity.getCrossType())));
        //        mCrossTypeSpinner.setSelection();
    }

    private void initSpinnerSelected() {
        mCrossTypeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DefectType firstLevel = mFirstLevelAdapter.getItem(position);
        List<DefectType> secondLevels = DefectTypeDBHelper.getInstance().getChilds(firstLevel.getSysDefectTypeID().intValue());
        if (mSecondLevelAdapter == null) {
            mSecondLevelAdapter = new CrossTypeAdapter(secondLevels, mReference.get(), R.layout.item_text_spinner);
            mCrossSubTypeSpinner.setAdapter(mSecondLevelAdapter);
        } else {
            mSecondLevelAdapter.onDataChange(secondLevels);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private double getDistance() {
        double distance;
        if (MyApplication.currentNearestTower == null) {
            return 0.00;
        }
        if (MyApplication.currentNearestTower.getDisplayOrder() < MyApplication.crossSecondTower.getDisplayOrder()) {
            distance = MyApplication.nearestDistance;
        } else {
            distance = MyApplication.nearSecondDistance;
        }
        return distance;
    }

    private Map<String, RequestBody> initParams() {
        Map<String, RequestBody> params = new HashMap<>();
        if (AppConstant.CURRENT_LOCATION != null) {
            double currentLatitude = AppConstant.CURRENT_LOCATION.latitude;
            double currentLongitude = AppConstant.CURRENT_LOCATION.longitude;
            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(currentLatitude, currentLongitude);
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), gps.latitude + "");
            params.put("Latitude", lat);
            RequestBody lng = RequestBody.create(MediaType.parse("multipart/form-data"), gps.longitude + "");
            params.put("Longitude", lng);
        } else {
            RequestBody lat = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
            params.put("Latitude", lat);
            RequestBody lng = RequestBody.create(MediaType.parse("multipart/form-data"), "0");
            params.put("Longitude", lng);
        }
        //交跨物类型
        DefectType type = mSecondLevelAdapter.getItem(mCrossSubTypeSpinner.getSelectedItemPosition());
        RequestBody brokeType = RequestBody.create(MediaType.parse("multipart/form-data"), type.getSysDefectTypeID() + "");
        params.put("CrossType", brokeType);

        RequestBody lineCrossId = RequestBody.create(MediaType.parse("multipart/form-data"), mLineCrossEntity.getPlatformId() + "");
        params.put("sysLineCrossId", lineCrossId);
        //距小号侧
        RequestBody company = RequestBody.create(MediaType.parse("multipart/form-data"), mDistance.getText().toString());
        params.put("DtoSmartTower", company);

        //净空距离
        RequestBody contact = RequestBody.create(MediaType.parse("multipart/form-data"), mAbsoluteHeight.getText().toString());
        params.put("Clearance", contact);

        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), mCrossDesc.getText().toString());
        params.put("Remark", remark);

        RequestBody crossAngle = RequestBody.create(MediaType.parse("multipart/form-data"), etCrossAngle.getText().toString());
        params.put("CrossAngle", crossAngle);

        RequestBody sysTaskId = RequestBody.create(MediaType.parse("multipart/form-data"), mLineCrossEntity.getSysTaskId() + "");
        params.put("sysTaskId", sysTaskId);

        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                RequestBody PlanDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
                params.put("PlanDailyPlanSectionIDs", PlanDailyPlanSectionIDs);
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                RequestBody PlanDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
                params.put("PlanDailyPlanSectionIDs", PlanDailyPlanSectionIDs);            }
        }else if (MyApplication.gridlineTaskStatus == 3) {
            RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mPlanPatrolExecutionId);
            params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
        }
        update(type);
        return params;
    }

    private void doSubMit() {
        if (mLineCrossEntity.getSysTaskId() == 0) {
            int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(mLineCrossEntity.getPlatformId(), AppConstant.CROSS_DEFECT_SHEET);
            if (sysWorkId != -1) {
                mLineCrossEntity.setSysTaskId(sysWorkId);
            }
        }


        if (StringUtils.isEmptyOrNull(mDistance.getText().toString())) {
            ToastUtil.show("请获取距最小侧距离");
            return;
        }

        if (StringUtils.isEmptyOrNull(mAbsoluteHeight.getText().toString())) {
            ToastUtil.show("请输入净空距离");
            return;
        }

        if (StringUtils.isEmptyOrNull(etCrossAngle.getText().toString())) {
            ToastUtil.show("请输入交跨角度");
            return;
        }

        if (Double.valueOf(etCrossAngle.getText().toString()) < 0 || Double.valueOf(etCrossAngle.getText().toString()) > 360) {
            ToastUtil.show("请输入正确的交跨角度");
            return;
        }

        if (StringUtils.isEmptyOrNull(mCrossDesc.getText().toString())) {
            ToastUtil.show("请输入交跨描述");
            return;
        }

        if (Double.valueOf(mDistance.getText().toString()) < 0 || Double.valueOf(mDistance.getText().toString()) > 10000) {
            ToastUtil.show("请输入正确的距最小侧距离");
            return;
        }

        if (Double.valueOf(mAbsoluteHeight.getText().toString()) < 0 || Double.valueOf(mAbsoluteHeight.getText().toString()) > 10000) {
            ToastUtil.show("请输入正确的净空距离");
            return;
        }

        if (mCrossSubTypeSpinner.getSelectedItemPosition() < 0) {
            ToastUtil.show("请选择正确的交跨物类型");
            return;
        }


        Map<String, RequestBody> params = initParams();

        mLineCrossEntity.setCreatedBy(AppConstant.currentUser.getUserId());
        mLineCrossEntity.setUpdatedTime(DateUtil.format(new Date()));
        mLineCrossEntity.setUploadFlag(0);

        //修改数据库
        LineCrossDBHelper.getInstance().updateByPlatFormId(mLineCrossEntity, 0);
        saveImagePaths(mLineCrossEntity);
        System.out.print(params);
        if (datas == null || datas.size() == 0) {
            mSubMitCall = RetrofitManager.getInstance().getService(ApiService.class).postCrossUpdate(params, null);
        } else {
            List<MultipartBody.Part> requestImgParts = new ArrayList<>();
            //外破图片
            for (int i = 0; i < datas.size(); i++) {
                File file = new File((String) datas.get(i).get("path"));
                RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CrossImage" + i, file.getName(), imgFile);
                requestImgParts.add(requestImgPart);
            }
            mSubMitCall = RetrofitManager.getInstance().getService(ApiService.class).postCrossUpdate(params, requestImgParts);
        }
        postEditCrossNet();
    }

    private void postEditCrossNet() {
        mProgressDialog.show();
        mSubMitCall.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    ToastUtil.show("提交成功");
                    mLineCrossEntity.setUploadFlag(1);
                    //修改状态
                    LineCrossDBHelper.getInstance().updateByPlatFormId(mLineCrossEntity, 1);
                    if (mLineCrossEntity.getSysTaskId() != 0) {
                        WorkSheetTaskDBHelper.getInstance().delete(mLineCrossEntity.getSysTaskId());
                        localBroadcastManager = LocalBroadcastManager.getInstance(EditCrossFragment.this.getContext());
                        Intent intent = new Intent(AppConstant.WORK_SHEET_NUM_DEL);
                        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        localBroadcastManager.sendBroadcast(intent);
                    }
                } else {
                    if (mLineCrossEntity.getSysTaskId() != 0) {
                        ToastUtil.show("工单已被其他用户处理");
                    } else {
                        //                        ToastUtil.show("提交到数据库");
                        retryPostEditCross();
                        return;
                    }
                }
                mProgressDialog.dismiss();
                reset();
                if (FLAG == -1) {
                    EventBus.getDefault().post(new ChangePageEvent(0));
                    EventBus.getDefault().post(new UpdateLineCrossList(false));
                } else if (FLAG == 1) {
                    Intent intent = new Intent(mReference.get(), MainActivity.class);
                    intent.putExtra("toValue", AppConstant.WORK_SHEET);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mReference.get(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                retryPostEditCross();
            }
        });
    }

    private void retryPostEditCross() {
        mProgressDialog.dismiss();
        DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postEditCrossNet();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //                ToastUtil.show("提交到数据库");
                reset();
                if (FLAG == -1) {
                    EventBus.getDefault().post(new ChangePageEvent(0));
                    EventBus.getDefault().post(new UpdateLineCrossList(false));
                } else if (FLAG == 1) {
                    Intent intent = new Intent(mReference.get(), MainActivity.class);
                    intent.putExtra("toValue", AppConstant.WORK_SHEET);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mReference.get(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_submit, R.id.btn_distance, R.id.tv_oldPic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                doSubMit();
                break;
            case R.id.btn_distance:
                DecimalFormat fmt = new DecimalFormat("0.##");
                mDistance.setText(fmt.format(getDistance()) + "");
                break;
            case R.id.iv_back:
                if (FLAG == -1) {
                    EventBus.getDefault().post(new ChangePageEvent(0));
                } else {
                    ((TourResultActivity) mReference.get()).finish();
                }
                break;
            case R.id.tv_oldPic:
                jumpToPhotoGrid();
                break;
        }
    }

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (FLAG == -1) {
                EventBus.getDefault().post(new ChangePageEvent(0));
            } else {
                ((TourResultActivity) mReference.get()).finish();
            }
        }
        return true;
    }

    private void jumpToPhotoGrid() {
        Intent photo = new Intent(this.getActivity(), PhotoGridActivity.class);
        photo.putExtra("path", zipPath);
        startActivity(photo);
    }

    private void saveImagePaths(LineCrossEntity mLineCrossEntity) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(8);
            imagePaths.setLocalId(mLineCrossEntity.getId() + "");
            imagePaths.setFatherPlatformId(mLineCrossEntity.getPlatformId() + "");
            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initDatas();
        } else {
            reset();
        }
    }

    private void reset() {
        datas.clear();
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }

    private void update(DefectType type) {
        mLineCrossEntity.setCrossType(type.getSysDefectTypeID().intValue());
        mLineCrossEntity.setCrossTypeName(type.getDefectName());
        mLineCrossEntity.setDtoSmartTower(Double.valueOf(mDistance.getText().toString()));
        mLineCrossEntity.setClearance(Double.valueOf(mAbsoluteHeight.getText().toString()));
        mLineCrossEntity.setRemark(mCrossDesc.getText().toString());
        mLineCrossEntity.setCrossAngle(Double.valueOf(etCrossAngle.getText().toString()));
        mLineCrossEntity.setCreatedTime(DateUtil.format(new Date()));
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                mLineCrossEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                mLineCrossEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            mLineCrossEntity.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        if (AppConstant.currentUser != null) {
            mLineCrossEntity.setCreatedBy(AppConstant.currentUser.getUserId());
        }

    }

    class CrossPicAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(final Integer... params) {
            //文件jia夹存在 不必要下载  // meizuo ;
            FileUtil.makeDir(zipPath);
            zipPath = zipPath + File.separator + params[0] + ".zip";
            File file = new File(zipPath.substring(0, zipPath.lastIndexOf(".")));
            mDownImage = RetrofitManager.getInstance().getService(ApiService.class).getImageZip(8, params[0]);
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
