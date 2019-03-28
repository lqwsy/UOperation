package com.uflycn.uoperation.ui.fragment.checkresult;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.LineCrossEntity;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.UpdateLineCrossList;
import com.uflycn.uoperation.eventbus.UpdateTowerEvent;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.LineCrossDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.CrossTypeAdapter;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 交跨登记
 */
public class CrossRegisterFragment extends DemoBaseFragment {

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

    private CrossTypeAdapter mFirstLevelAdapter;//第一级菜单
    private CrossTypeAdapter mSecondLevelAdapter;
    private Call<BaseCallBack<List<ListCallBack<String>>>> mSubMitCall;
    private ProgressDialog mProgressDialog;
    private LineCrossEntity mLineCrossEntity;

    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;

    private boolean FLAG_SUBMIT = true;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_cross_register;
    }

    @Override
    protected void initView() {
        mProgressDialog = new ProgressDialog(mWeakReference.get());
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        String towerNo = "";
        if (MyApplication.isRegisterAuto) {
            //自动获取到位登记的情况下
            if (MyApplication.currentNearestTower != null && MyApplication.crossSecondTower != null) {
                towerNo = MyApplication.currentNearestTower.getTowerNo() + "-" + MyApplication.crossSecondTower.getTowerNo();
                tvTowerNum.setText(towerNo);
                String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.currentNearestTower.getSysGridLineId() + "");
                if (StringUtils.isEmptyOrNull(lineName)) {
                    lineName = MyApplication.mTempLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()).getLineName();
                }
                tvGridName.setText(lineName);
            } else if (MyApplication.currentNearestTower != null) {
                towerNo = MyApplication.currentNearestTower.getTowerNo();
                tvTowerNum.setText(towerNo);
                String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.currentNearestTower.getSysGridLineId() + "");
                if (StringUtils.isEmptyOrNull(lineName)) {
                    lineName = MyApplication.mTempLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()).getLineName();
                }
                tvGridName.setText(lineName);
            } else {
                ToastUtil.show("请先进行到位登记");
            }

        } else {
            if (MyApplication.registeredTower != null && MyApplication.nearSecondTower != null) {
                towerNo = MyApplication.registeredTower.getTowerNo() + "-" + MyApplication.nearSecondTower.getTowerNo();
                tvTowerNum.setText(towerNo);
                String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.registeredTower.getSysGridLineId() + "");
                if (StringUtils.isEmptyOrNull(lineName)) {
                    lineName = MyApplication.mTempLineMap.get(MyApplication.registeredTower.getSysGridLineId()).getLineName();
                }
                tvGridName.setText(lineName);
            } else if (MyApplication.registeredTower != null) {
                towerNo = MyApplication.registeredTower.getTowerNo();
                tvTowerNum.setText(towerNo);
                String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.registeredTower.getSysGridLineId() + "");
                if (StringUtils.isEmptyOrNull(lineName)) {
                    lineName = MyApplication.mTempLineMap.get(MyApplication.registeredTower.getSysGridLineId()).getLineName();
                }
                tvGridName.setText(lineName);
            } else {
                ToastUtil.show("请先进行到位登记");
            }

        }


        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mSubMitCall != null && !mSubMitCall.isCanceled()) {
                    mSubMitCall.cancel();
                }
            }
        });
    }

    @Override
    protected void initData() {
        getCrossType();
        datas = new ArrayList<>();
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
    }


    private void getCrossType() {
        //第一级菜单
        List<DefectType> firstLevel = DefectTypeDBHelper.getInstance().getDefectType(3);
        mCrossTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DefectType firstLevel = mFirstLevelAdapter.getItem(position);
                List<DefectType> secondLevels = DefectTypeDBHelper.getInstance().getChilds(firstLevel.getSysDefectTypeID().intValue());
                if (mSecondLevelAdapter == null) {
                    mSecondLevelAdapter = new CrossTypeAdapter(secondLevels, mWeakReference.get(), R.layout.item_text_spinner);
                    mCrossSubTypeSpinner.setAdapter(mSecondLevelAdapter);
                } else {
                    mSecondLevelAdapter.onDataChange(secondLevels);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mFirstLevelAdapter = new CrossTypeAdapter(firstLevel, mWeakReference.get(), R.layout.item_text_spinner);
        mCrossTypeSpinner.setAdapter(mFirstLevelAdapter);
    }

    /**
     * 拍照
     */
    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            IMAGE_DIR = IOUtils.getRootStoragePath(mWeakReference.get()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
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
        Intent intent = new Intent(this.getActivity(), PhotoSelectorActivity.class);
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
        IMAGE_DIR = IOUtils.getRootStoragePath(mWeakReference.get()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "交跨";
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

    @OnClick({R.id.iv_back, R.id.btn_submit, R.id.btn_distance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                synchronized (this) {
                    doSubMit();
                }
                break;
            case R.id.btn_distance:
                DecimalFormat fmt = new DecimalFormat("0.##");
                mDistance.setText(fmt.format(getDistance()) + "");
                break;
            case R.id.iv_back:
                EventBus.getDefault().post(new ChangePageEvent(0));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubMitCall != null && !mSubMitCall.isCanceled()) {
            mSubMitCall.cancel();
        }
    }

    private void doSubMit() {
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(mWeakReference.get().getString(R.string.tip_far_awayfrom_tower));
            return;
        }
        postCrossLine();
    }


    private void postCrossLine() {
        if (MyApplication.registeredTower.equals(MyApplication.crossSecondTower)) {
            ToastUtil.show("当前到位登记塔与第二近塔相同，请检查位置信息");
            return;
        }

        //交跨图片
        if (datas == null || datas.size() == 0) {
            ToastUtil.show("请提供交跨照片");
            return;
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

        //交跨物类型
        int pos = mCrossSubTypeSpinner.getSelectedItemPosition();
        if (pos < 0) {
            ToastUtil.show("请选择正确的交跨物类型");
            return;
        }


        double currentLatitude = AppConstant.CURRENT_LOCATION.latitude;
        double currentLongitude = AppConstant.CURRENT_LOCATION.longitude;
        LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(currentLatitude, currentLongitude);

        Gridline line = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
        mLineCrossEntity = new LineCrossEntity();
        mLineCrossEntity.setUploadFlag(0);
        mLineCrossEntity.setCrossLatitude(gps.latitude + "");
        mLineCrossEntity.setCrossLongitude(gps.longitude + "");
        mLineCrossEntity.setLineName(line.getLineName());
        mLineCrossEntity.setVoltageClass(line.getVoltageClass());

        mLineCrossEntity.setStartTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
        mLineCrossEntity.setStartTower(MyApplication.registeredTower.getTowerNo());
        if (MyApplication.crossSecondTower == null) {
            mLineCrossEntity.setEndTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
            mLineCrossEntity.setEndTower(MyApplication.registeredTower.getTowerNo());
        } else {
            mLineCrossEntity.setEndTowerId(MyApplication.crossSecondTower.getSysTowerID().intValue());
            mLineCrossEntity.setEndTower(MyApplication.crossSecondTower.getTowerNo());
        }


        mLineCrossEntity.setCrossStatus("存在");
        mLineCrossEntity.setCrossType(mSecondLevelAdapter.getItem(pos).getSysDefectTypeID().intValue());
        mLineCrossEntity.setCrossTypeName(mSecondLevelAdapter.getItem(pos).getDefectName());
        mLineCrossEntity.setDtoSmartTower(Double.valueOf(mDistance.getText().toString()));
        mLineCrossEntity.setClearance(Double.valueOf(mAbsoluteHeight.getText().toString()));
        mLineCrossEntity.setRemark(mCrossDesc.getText().toString());
        mLineCrossEntity.setCrossAngle(Double.valueOf(etCrossAngle.getText().toString()));
        mLineCrossEntity.setCreatedTime(DateUtil.format(new Date()));
        mLineCrossEntity.setCreatedBy(AppConstant.currentUser.getUserId());
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                mLineCrossEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                mLineCrossEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            mLineCrossEntity.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        //保存到数据库
        LineCrossDBHelper.getInstance().insert(mLineCrossEntity);
        saveImagePaths(mLineCrossEntity);
        postCrossNet();
    }

    private void postCrossNet() {
        mProgressDialog.show();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mLineCrossEntity);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);

        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        //外破图片
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CrossImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        mSubMitCall = RetrofitManager.getInstance().getService(ApiService.class).postCrossRegister(body, requestImgParts);
        mSubMitCall.enqueue(new Callback<BaseCallBack<List<ListCallBack<String>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<String>>>> call, Response<BaseCallBack<List<ListCallBack<String>>>> response) {
                mProgressDialog.dismiss();
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    ToastUtil.show("提交成功");
                    mLineCrossEntity.setUploadFlag(1);
                    mLineCrossEntity.setPlatformId(response.body().getData().get(0).getPlatformId());
                    LineCrossDBHelper.getInstance().updateById(mLineCrossEntity);
                } else {
                    //                    ToastUtil.show("提交到数据库");
                    retryCrossPost();
                }
                EventBus.getDefault().post(new ChangePageEvent(0));
                EventBus.getDefault().post(new UpdateLineCrossList(false));
                resetAllData();
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<String>>>> call, Throwable t) {
                //                ToastUtil.show("提交到数据库");
                retryCrossPost();
            }
        });
    }

    private void retryCrossPost() {
        DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postCrossNet();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resetAllData();
                mProgressDialog.dismiss();
                EventBus.getDefault().post(new ChangePageEvent(0));
                EventBus.getDefault().post(new UpdateLineCrossList(false));
            }
        });
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(BaseMainThreadEvent event) {
        if (event instanceof UpdateTowerEvent) {
            refreshTower(((UpdateTowerEvent) event).getCurrentNearestTower());
        }
    }

    private void refreshTower(Tower tower) {
        if (tower == null || MyApplication.registeredTower == null) {
            return;
        }
        String towerNo = MyApplication.registeredTower.getTowerNo() + "-" + MyApplication.nearSecondTower.getTowerNo();
        tvTowerNum.setText(towerNo);
        String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.registeredTower.getSysGridLineId() + "");
        if (StringUtils.isEmptyOrNull(lineName)) {
            lineName = MyApplication.mTempLineMap.get(MyApplication.registeredTower.getSysGridLineId()).getLineName();
        }
        tvGridName.setText(lineName);
    }

    private double getDistance() {
        double distance;
        if (MyApplication.currentNearestTower == null || MyApplication.crossSecondTower == null) {
            return 0.00;
        }
        if (MyApplication.currentNearestTower.getDisplayOrder() < MyApplication.crossSecondTower.getDisplayOrder()) {
            distance = MyApplication.nearestDistance;
        } else {
            distance = MyApplication.nearSecondDistance;
        }
        return distance;
    }


    private void resetAllData() {
        datas.clear();
        gridViewAddImgesAdpter.notifyDataSetChanged();
        mDistance.setText("");
        mAbsoluteHeight.setText("");
        mCrossDesc.setText("");
        etCrossAngle.setText("");
    }
}
