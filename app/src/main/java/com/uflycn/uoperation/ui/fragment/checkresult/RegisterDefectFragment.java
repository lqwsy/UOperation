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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.DefectBean;
import com.uflycn.uoperation.bean.DefectType;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.UpdateDefectList;
import com.uflycn.uoperation.eventbus.UpdateTowerEvent;
import com.uflycn.uoperation.greendao.DefectBeanDBHelper;
import com.uflycn.uoperation.greendao.DefectTypeDBHelper;
import com.uflycn.uoperation.greendao.ImagePathsDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 缺陷登记
 * Created by xiaoyehai on 2017/9/8.
 */
public class RegisterDefectFragment extends DemoBaseFragment {
    @BindView(R.id.et_defect_describe)
    EditText defectDesc;

    @BindView(R.id.spinner_defect_class)
    Spinner defectClassSpinner;

    @BindView(R.id.spinner_defect_element)
    Spinner defectElementSpinner;

    @BindView(R.id.spinner_defect_part)
    Spinner defectPartSpinner;

    @BindView(R.id.spinner_defect_type)
    Spinner defectTypeSpinner;

    @BindView(R.id.spinner_defect_present)
    Spinner defectPresentSpinner;

    @BindView(R.id.tv_line_name)
    TextView tvLineName;

    @BindView(R.id.tv_tower_serial)
    TextView tvTowerSeril;

    @BindView(R.id.btn_jump_defect_list)
    Button btnDefectList;

    @BindView(R.id.add_img_break)
    AddImageGridView add_img_break;

    private String strDescritionPrefix = "";
    private ProgressDialog mProgressDialog;
    private Call<BaseCallBack<List<DefectType>>> mRequestCall;
    private boolean needRefresh = false;

    private List<Map<String, Object>> datas; // 缺陷图片字典链
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;

    private Call<BaseCallBack<List<ListCallBack>>> mCall;
    private boolean FLAG_SUBMIT = true;
    private Call<BaseCallBack<ListCallBack>> mRegisterDefectCall = null; // 缺陷提交的返回引用

    List<ProgressRequestBody> mProgressRequestBodyArr;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_register_defect;
    }

    @Override
    protected void initView() {
        mProgressDialog = new ProgressDialog(mWeakReference.get());
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mCall != null && !mCall.isCanceled()) {
                    mCall.cancel();
                }

                // 取消网络请求
                if (mRegisterDefectCall != null && !mRegisterDefectCall.isCanceled()) {
                    mRegisterDefectCall.cancel();
                    Log.e("close stream", "------------------拦截：取消");
                    //                    RetrofitManager.getInstance().setEffected(false);
                    //                    RetrofitManager.getInstance().getClient().dispatcher().cancelAll();
                    //                    destroy();
                }
            }
        });
        refreshTower();

    }

    @Override
    protected void initData() {
        getdefectType(defectClassSpinner, 0, 1);

        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this.getContext());
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
    }

    /**
     * 拍照
     */
    public void camera() {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            IMAGE_DIR = IOUtils.getRootStoragePath(mWeakReference.get()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "缺陷";
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

    //<editor-fold desc="对于文件进行剪切。展示">
    /**
     * 对于文件进行剪切。展示
     *
     * @param path
     */
    public void photoPath(String path) {
        IMAGE_DIR = IOUtils.getRootStoragePath(mWeakReference.get()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "缺陷";
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
    //</editor-fold>


    //<editor-fold desc="初始化封装缺陷描述">
    /**
     * 初始化封装缺陷描述
     */
    private void InitDescription() {
        refreshTower();
        String strDefectClass = ((SpinnerOption) defectClassSpinner.getSelectedItem()).getText();
        String strDefectYJ = ((SpinnerOption) defectElementSpinner.getSelectedItem()).getText();
        String strDefectPart = ((SpinnerOption) defectPartSpinner.getSelectedItem()).getText();
        String strDefectType = ((SpinnerOption) defectTypeSpinner.getSelectedItem()).getText();
        String strDefectBX = ((SpinnerOption) defectPresentSpinner.getSelectedItem()).getText();
        defectDesc.setText(strDescritionPrefix + "," + strDefectClass + "," + strDefectYJ + "," +
                strDefectPart + "," + strDefectType + "," + strDefectBX);
    }
    //</editor-fold>


    //<editor-fold desc="获取缺陷类型">
    /**
     * 获取缺陷类型
     */
    private void getdefectType(final Spinner spinner, int iParentId, final int level) {
        List<DefectType> data = null;
        if (iParentId == 0) {
            data = DefectTypeDBHelper.getInstance().getDefectType(1);
        } else {
            data = DefectTypeDBHelper.getInstance().getChilds(iParentId);
        }
        List<SpinnerOption> mDefectClass = new ArrayList<SpinnerOption>();
        for (int i = 0; i < data.size(); i++) {
            mDefectClass.add(new SpinnerOption(data.get(i).getSysDefectTypeID() + "", data.get(i).getDefectName()));
        }

        ArrayAdapter<SpinnerOption> mAdapter = new ArrayAdapter<SpinnerOption>(mWeakReference.get(), android.R.layout.simple_spinner_dropdown_item, mDefectClass);
        spinner.setAdapter(mAdapter);

        //缺陷元件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strParentId = ((SpinnerOption) spinner.getSelectedItem()).getValue();
                if (strParentId != null && !strParentId.equals("")) {
                    int iParentId = Integer.parseInt(strParentId);
                    Spinner spinner1 = null;
                    if (level == 1) {
                        spinner1 = defectElementSpinner;
                    } else if (level == 2) {
                        spinner1 = defectPartSpinner;
                    } else if (level == 3) {
                        spinner1 = defectTypeSpinner;
                    } else if (level == 4) {
                        spinner1 = defectPresentSpinner;
                    }
                    if (spinner1 != null) {
                        getdefectType(spinner1, iParentId, level + 1);
                    } else {
                        //回传写缺陷描述的值。其值等于
                        InitDescription();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //</editor-fold>

    @OnClick({R.id.btn_submit, R.id.btn_jump_defect_list, R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit: //提交
                synchronized (this) {
                    doSubmit();
                }
                break;
            case R.id.iv_back:
            case R.id.btn_jump_defect_list:
                jumpToDefectList();
                break;
            default:
                break;
        }
    }

    private void jumpToDefectList() {
        EventBus.getDefault().post(new ChangePageEvent(0));
        EventBus.getDefault().post(new UpdateDefectList(null));
    }



    //<editor-fold desc="提交">
    private void doSubmit() {
        if (MyApplication.registeredTower == null) {
            ToastUtil.show("未进行到位登记");
            return;
        }
        Map<String, RequestBody> params = initParams();
        if (params == null) {
            return;
        }

        if (datas == null || datas.size() == 0) {
            ToastUtil.show("无缺陷照片，请先拍照");
            return;
        }

        if (defectDesc.getText().toString().trim().isEmpty()) {
            ToastUtil.show("未填写缺陷描述");
            return;
        }

        //
        final DefectBean defectBean = createDefect();

        long id = DefectBeanDBHelper.getInstance().insert(defectBean);
        defectBean.setId(id);
        // 放到网络请求进行处理
        //        saveImagePaths(defectBean);
        postDefectNet(params, defectBean);

    }
    //</editor-fold>


    //<editor-fold desc="网络请求">
    private void postDefectNet(final Map<String, RequestBody> params, final DefectBean defectBean) {
        mProgressDialog.show();

        //        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        //        //缺陷图片
        //        for (int i = 0; i < datas.size(); i++) {
        //            File file = new File((String) datas.get(i).get("path"));
        //            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        //            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("DefectPhoto" + i, file.getName(), imgFile);
        //            requestImgParts.add(requestImgPart);
        //        }

        mProgressRequestBodyArr = new ArrayList<ProgressRequestBody>();
        /**
         * 添加截断流得控制
         */
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        //缺陷图片
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            ProgressRequestBody uploadBody = new ProgressRequestBody(imgFile);
            mProgressRequestBodyArr.add(uploadBody);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("DefectPhoto" + i, file.getName(), uploadBody);
            requestImgParts.add(requestImgPart);
        }

        mRegisterDefectCall = RetrofitManager.getInstance().getService(ApiService.class).postAddTowerDefect(params, requestImgParts);
        mRegisterDefectCall.enqueue(new Callback<BaseCallBack<ListCallBack>>() {

            @Override
            public void onResponse(Call<BaseCallBack<ListCallBack>> call, Response<BaseCallBack<ListCallBack>> response) {
                if (response == null || response.body() == null) {
                    retryPostDefect(params, defectBean);
                    return;
                }
                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    defectBean.setUploadFlag(1);
                    defectBean.setSysTowerDefectId(response.body().getData().getPlatformId());
                    DefectBeanDBHelper.getInstance().updateById(defectBean);
                } else {
                    retryPostDefect(params, defectBean);
                    return;
                }
                // 保存依赖于defectBean的图片路径到数据库
                saveImagePaths(defectBean);
                datas = null;
                gridViewAddImgesAdpter.notifyDataSetChanged();
                mProgressDialog.dismiss();
                ToastUtil.show("提交成功");
                resetData();
                jumpToDefectList();
            }

            @Override
            public void onFailure(Call<BaseCallBack<ListCallBack>> call, Throwable t) {
               /* boolean result = mRegisterDefectCall == call;
                mProgressDialog.dismiss();
                if (mRegisterDefectCall.isCanceled()) {
                    ToastUtil.show("缺陷登记提交已取消");
                    // 删除defectBean对应的数据库记录
                    DefectBeanDBHelper.getInstance().deleteDefectBean(defectBean);
                } else {
                    ToastUtil.show("提交成功");
                    // 保存依赖于defectBean的图片路径到数据库
                    saveImagePaths(defectBean);
                    resetData();
                    jumpToDefectList();
                }*/
                retryPostDefect(params, defectBean);
            }
        });
    }
    //</editor-fold>

    private void retryPostDefect(final Map<String, RequestBody> params, final DefectBean defectBean) {
        mProgressDialog.dismiss();
        DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //重新发送请求
                postDefectNet(params, defectBean);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //稍后自动
                // 保存依赖于defectBean的图片路径到数据库
                saveImagePaths(defectBean);
                resetData();
                jumpToDefectList();
            }
        });
    }

    private Map<String, RequestBody> initParams() {
        Map<String, RequestBody> params = new HashMap<>();

        if (MyApplication.registeredTower == null) {
            return null;
        } else {
            RequestBody towerId;
            if (MyApplication.isRegisterAuto == true) {
                towerId = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.currentNearestTower.getSysTowerID() + "");
            } else {
                towerId = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.registeredTower.getSysTowerID() + "");
            }
            params.put("TowerId", towerId);
            try {
                String mDefectTypeId = ((SpinnerOption) defectTypeSpinner.getSelectedItem()).getValue();
                if (defectPresentSpinner.getSelectedItem() != null) {
                    mDefectTypeId = ((SpinnerOption) defectPresentSpinner.getSelectedItem()).getValue();
                }

                RequestBody defectType = RequestBody.create(MediaType.parse("multipart/form-data"), mDefectTypeId);
                params.put("DefectType", defectType);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            String defectRemark = defectDesc.getText().toString();
            //remark
            RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), defectRemark);
            params.put("Remark", remark);
            if (AppConstant.CURRENT_LOCATION != null) {
                double currentLatitude = AppConstant.CURRENT_LOCATION.latitude;
                double currentLongitude = AppConstant.CURRENT_LOCATION.longitude;
                LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(currentLatitude, currentLongitude);

                RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), gps.latitude + "");
                params.put("Latitude", latitude);

                RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), gps.longitude + "");
                params.put("Longitude", longitude);

            }
            if (defectClassSpinner.getSelectedItemPosition() == 0 ||
                    defectClassSpinner.getSelectedItemPosition() == 1 ||
                    defectClassSpinner.getSelectedItemPosition() == 3 ||
                    defectClassSpinner.getSelectedItemPosition() == 4) {
            } else {
                if (MyApplication.nearSecondTower != null) {
                    RequestBody nearTowerID = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.nearSecondTower.getSysTowerID() + "");
                    params.put("NearTowerID", nearTowerID);
                }
            }
            if (MyApplication.gridlineTaskStatus == 2) {
                RequestBody PlanDailyPlanSectionIDs = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
                params.put("PlanDailyPlanSectionIDs", PlanDailyPlanSectionIDs);
            } else if (MyApplication.gridlineTaskStatus == 3) {
                RequestBody PlanPatrolExecutionId = RequestBody.create(MediaType.parse("multipart/form-data"), MyApplication.mPlanPatrolExecutionId);
                params.put("sysPatrolExecutionID", PlanPatrolExecutionId);
            }

            return params;
        }

    }

    private DefectBean createDefect() {
        DefectBean defectBean = new DefectBean();
        defectBean.setUploadFlag(0);
        defectBean.setFoundedTime(DateUtil.format(new Date()));
        Tower tower = new Tower();
        if (MyApplication.isRegisterAuto == true) {
            tower = MyApplication.currentNearestTower;
        } else {
            tower = MyApplication.registeredTower;
        }
        defectBean.setSysTowerID(tower.getSysTowerID().intValue());//最近塔

        defectBean.setTowerNo(tower.getTowerNo());
        defectBean.setLineName(tvLineName.getText().toString().trim());
        defectBean.setDefectCategoryString("人巡");
        defectBean.setDefectCategory("1");
        if (defectClassSpinner.getSelectedItemPosition() == 0 ||
                defectClassSpinner.getSelectedItemPosition() == 1 ||
                defectClassSpinner.getSelectedItemPosition() == 3 ||
                defectClassSpinner.getSelectedItemPosition() == 4) {
        } else {
            if (MyApplication.nearSecondTower != null) {
                defectBean.setNearTowerID(MyApplication.nearSecondTower.getSysTowerID().intValue());//区段塔
                defectBean.setNearTowerNo(MyApplication.nearSecondTower.getTowerNo());
            }
        }

        defectBean.setRemark(defectDesc.getText().toString());
        String mDefectTypeId = ((SpinnerOption) defectTypeSpinner.getSelectedItem()).getValue();
        if (defectPresentSpinner.getSelectedItem() != null) {
            mDefectTypeId = ((SpinnerOption) defectPresentSpinner.getSelectedItem()).getValue();
        }
        defectBean.setImageCategory("TowerDefect");
        defectBean.setDefectTypeId(mDefectTypeId);
        if (AppConstant.CURRENT_LOCATION != null) {
            double currentLatitude = AppConstant.CURRENT_LOCATION.latitude;
            double currentLongitude = AppConstant.CURRENT_LOCATION.longitude;
            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(currentLatitude, currentLongitude);
            defectBean.setLatitude(gps.latitude);
            defectBean.setLongitude(gps.longitude);
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            defectBean.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
        } else if (MyApplication.gridlineTaskStatus == 3) {
            defectBean.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }


        return defectBean;
    }

    //<editor-fold desc="保存采集到的图片">
    /**
     * 保存采集到的图片
     */
    private void saveImagePaths(DefectBean defectBean) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(10);
            imagePaths.setLocalId(defectBean.getId() + "");
            imagePaths.setFatherPlatformId(defectBean.getSysTowerDefectId() + "");
            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        //        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        //        saveImagePathsAsync.execute(imagePath);

        // 由于图片记录插入到数据库的工作量不多，而且可以减少activity的释放与数据库的异步处理
        // 直接操作数据库即可
        ImagePathsDBHelper.getInstance().insert(imagePath);
    }
    //</editor-fold>

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initView();
            if (needRefresh) {
                initData();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRequestCall != null && !mRequestCall.isCanceled()) {
            mRequestCall.cancel();
        }
        if (mRegisterDefectCall != null) {
            mRegisterDefectCall.cancel();
            mRegisterDefectCall = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    private void refreshTower() {
        Tower tower_regis = new Tower();
        if (MyApplication.isRegisterAuto) {
            //自动获取到位登记的情况下
            if (MyApplication.currentNearestTower != null) {
                tower_regis = MyApplication.currentNearestTower;
            }
        } else {
            if (MyApplication.registeredTower != null) {
                tower_regis = MyApplication.registeredTower;
            }
        }
        if (tower_regis.getTowerNo() != null) {
            String towerNo = tower_regis.getTowerNo();
            if (MyApplication.nearSecondTower != null) {
                towerNo = showTowerId(towerNo, MyApplication.nearSecondTower.getTowerNo());
                tvTowerSeril.setText(towerNo);
            }
            if (defectClassSpinner.getSelectedItemPosition() == 0 ||
                    defectClassSpinner.getSelectedItemPosition() == 1 ||
                    defectClassSpinner.getSelectedItemPosition() == 3 ||
                    defectClassSpinner.getSelectedItemPosition() == 4) {
                towerNo = tower_regis.getTowerNo();
                tvTowerSeril.setText(towerNo);
            }
            String lineName = MyApplication.mLineIdNamePairs.get(tower_regis.getSysGridLineId() + "");
            if (StringUtils.isEmptyOrNull(lineName)) {
                lineName = MyApplication.mTempLineMap.get(tower_regis.getSysGridLineId()).getLineName();
            }
            strDescritionPrefix = lineName + " [" + towerNo + "]号杆塔";
            tvLineName.setText(lineName);
            btnDefectList.setVisibility(View.VISIBLE);
        } else {
            btnDefectList.setVisibility(View.GONE);
        }
    }

    private String showTowerId(String towerNo, String nearTowerNo) {
        try {
            int tower = Integer.valueOf(towerNo);
            int nearTower = Integer.valueOf(nearTowerNo);
            if (tower < nearTower) {
                towerNo = towerNo + "-" + nearTowerNo;
            } else {
                towerNo = nearTowerNo + "-" + towerNo;
            }
        } catch (Exception e) {
            towerNo = towerNo + "-" + nearTowerNo;
        }
        return towerNo;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThread(BaseMainThreadEvent event) {
        if (event instanceof UpdateTowerEvent) {
            refreshTower();
        }
    }

    private void resetData() {
        needRefresh = true;
        if (datas != null) {
            datas.clear();
        }
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    public void destroy() {
        //        if (daemonExecutor != null && daemonExecutor instanceof ThreadPoolExecutor) {
        //            ThreadPoolExecutor tpe = (ThreadPoolExecutor) daemonExecutor;
        //            tpe.shutdown();
        //        }

        OkHttpClient okHttpClient = RetrofitManager.getInstance().getClient();
        if (okHttpClient != null) {
            ConnectionPool connectionPool = okHttpClient.connectionPool();
            connectionPool.evictAll();
            //            Log.e("OKHTTP connections iddle: {}, all: {}", connectionPool.idleConnectionCount() + "," + connectionPool.connectionCount());
            ExecutorService executorService = okHttpClient.dispatcher().executorService();
            executorService.shutdown();
            try {
                executorService.awaitTermination(3, TimeUnit.MINUTES);
                //                Log.e("OKHTTP ExecutorService closed.","finish");
            } catch (InterruptedException e) {
                //                Log.e("InterruptedException on destroy()", e.toString());
            }
        }
    }

    class ProgressRequestBody extends RequestBody {
        private boolean isEffective = true;
        //实际待包装的请求体
        private final RequestBody requestBody;
        //包装完成的BufferedSink
        private BufferedSink bufferedSink;

        public ProgressRequestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
        }

        @Override
        public long contentLength() throws IOException {
            return requestBody.contentLength();
        }

        @Override
        public MediaType contentType() {
            return requestBody.contentType();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            Log.e("close stream", "=====================");
            if (bufferedSink == null) {
                //开始包装
                bufferedSink = Okio.buffer(sink(sink));
            }
            if (!isEffective) {
                Log.e("close stream", "&&&&&&&&&&&&&");
                bufferedSink.close();
                return;
            }
            //写入
            requestBody.writeTo(bufferedSink);
            if (!isEffective) {
                Log.e("close stream", "&&&&&&&&&&&&&");
                bufferedSink.close();
                return;
            }
            bufferedSink.flush();
        }

        public void close() {
            isEffective = false;
            if (bufferedSink != null) {
                try {
                    bufferedSink.close();
                    Log.e("close stream", "----- &&&&&&&&&&&&&");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("close stream", "+++++ &&&&&&&&&&&&&");
                }
            }
        }

        /**
         * 写入，回调进度接口
         */

        private Sink sink(Sink sink) {
            return new ForwardingSink(sink) {
                //当前写入字节数
                long byteWriteed = 0L;
                //总得字节数
                long contentBytes = 0L;

                @Override
                public void write(Buffer source, long byteCount) throws IOException {
                    super.write(source, byteCount);
                    //                    if(mHandler != null && mListener != null){
                    //                        if(contentBytes == 0L){
                    //                            contentBytes = contentLength();
                    //                        }
                    //                        byteWriteed += byteCount;
                    //                        mListener.onProgress(byteWriteed, contentBytes, byteWriteed == contentBytes);
                    //                    }
                }
            };
        }
    }

}


