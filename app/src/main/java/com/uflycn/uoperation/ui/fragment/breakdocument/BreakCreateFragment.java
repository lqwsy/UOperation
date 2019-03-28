package com.uflycn.uoperation.ui.fragment.breakdocument;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.BaseRequestView;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenDocument;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.ListCallBack;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.UpdateBrokenListEvent;
import com.uflycn.uoperation.greendao.BrokenDocumentDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.greendao.UserDBHellper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.BrokenTypeAdapter;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.adapter.UspinnerAdapter;
import com.uflycn.uoperation.util.CallBackHandler;
import com.uflycn.uoperation.util.DateUtil;
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
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 外破建档
 * A simple {@link Fragment} subclass.
 */
public class BreakCreateFragment extends DemoBaseFragment implements BaseRequestView {
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

    @BindView(R.id.add_img_break)
    AddImageGridView add_img_break;

    private Spinner mSpinnerBreakFrom;
    private Spinner mSpinnerBreaktTo;
    private UspinnerAdapter mTowerFromAdapter;
    private UspinnerAdapter mTowerToAdapter;
    private Call<BaseCallBack<List<ListCallBack<BrokenDocument>>>> mSubMitCall;
    private CallBackHandler mHandler;
    private List<ItemDetail> mBrokenTypes;
    private BrokenTypeAdapter mTypeAdapter;
    private ProgressDialog mProgressDialog;
    private BrokenDocument mBrokenDocument;

    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_break_create;
    }

    @Override
    protected void initView() {
        mSpinnerBreakFrom = (Spinner) mRootView.findViewById(R.id.sp_break_tower_from);
        mSpinnerBreaktTo = (Spinner) mRootView.findViewById(R.id.sp_break_tower_to);
        mTypeAdapter = new BrokenTypeAdapter(new ArrayList<ItemDetail>(), mWeakReference.get(), R.layout.item_text_spinner);
        mBrokenTypeSpinner.setAdapter(mTypeAdapter);

        mProgressDialog = new ProgressDialog(mWeakReference.get());
        mProgressDialog.setMessage("正在提交");
        mProgressDialog.setCanceledOnTouchOutside(false);
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
        mHandler = new CallBackHandler(this);
        mBrokenTypes = new ArrayList<>();
        getBrokenType();

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
        IMAGE_DIR = IOUtils.getRootStoragePath(mWeakReference.get()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "外破";
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


    private void getBrokenType() {
        mBrokenTypes = ItemDetailDBHelper.getInstance().getItem("外破类型");
        updateList(mBrokenTypes);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubMitCall != null && !mSubMitCall.isCanceled()) {
            mSubMitCall.cancel();
        }
    }

    @Override
    public void updateList(List data) {
        if (data.size() == 0) {
            return;
        }
        if (mTypeAdapter == null) {
            mTypeAdapter = new BrokenTypeAdapter(data, mWeakReference.get(), R.layout.item_text_spinner);
            mBrokenTypeSpinner.setAdapter(mTypeAdapter);
        } else {
            mTypeAdapter.onDataChange(data);
        }
    }

    @Override
    public void handRequestErr(String message) {
        ToastUtil.show(mWeakReference.get(), message);
        Log.i("handRequestErr", "" + message);
    }


    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                doSubMit();
                break;
            default:
                break;
        }
    }

    private Map<String, RequestBody> initParams() {
        if (MyApplication.currentNearestTower == null) {
            return null;
        }

        Map<String, RequestBody> params = new HashMap<>();
        //外破类型
        int pos = mBrokenTypeSpinner.getSelectedItemPosition();
        RequestBody brokeType = RequestBody.create(MediaType.parse("multipart/form-data"), mBrokenTypes.get(pos).getItemDetailsId() + "");
        params.put("BrokenType", brokeType);

        //开始杆塔
        Tower from = (Tower) mSpinnerBreakFrom.getSelectedItem();
        RequestBody startTowerId = RequestBody.create(MediaType.parse("multipart/form-data"), from.getSysTowerID() + "");
        params.put("StartTowerId", startTowerId);

        //终止杆塔
        Tower to = (Tower) mSpinnerBreaktTo.getSelectedItem();
        RequestBody endTowerID = RequestBody.create(MediaType.parse("multipart/form-data"), to.getSysTowerID() + "");
        params.put("EndTowerId", endTowerID);

        String companys = mEtCompany.getText().toString();
        String contactPerson = mEtContactPerson.getText().toString();
        String phoneNum = mEtPhoneNum.getText().toString();
        String desc = mEtDescreiption.getText().toString();
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

            mBrokenDocument = create(pos, from, to, companys, contactPerson, phoneNum, desc);
            saveBrokenDocument(mBrokenDocument);
            return params;
        }
    }


    private void doSubMit() {
        //没有到位登记 或者到位登记的塔 不是最近的塔 提示先登记
        if (MyApplication.registeredTower == null
                || MyApplication.registeredTower.getSysTowerID() != MyApplication.registeredTower.getSysTowerID()) {
            ToastUtil.show(mWeakReference.get().getResources().getString(R.string.tip_regiter_location));
            return;
        }

        if (datas == null || datas.size() == 0) {
            ToastUtil.show("无外破照片，请先拍照");
            return;
        }

        Map<String, RequestBody> params = initParams();
        if (params == null) {
            ToastUtil.show("数据填写不完整,请输入缺少数据");
            return;
        }
        mProgressDialog.show();

        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        //外破图片
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("BrokenImage" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }

        String json = new Gson().toJson(mBrokenDocument);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);

        mSubMitCall = RetrofitManager.getInstance().getService(ApiService.class).postCreateDocument(body, requestImgParts);
        mSubMitCall.enqueue(new Callback<BaseCallBack<List<ListCallBack<BrokenDocument>>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<ListCallBack<BrokenDocument>>>> call, Response<BaseCallBack<List<ListCallBack<BrokenDocument>>>> response) {
                mProgressDialog.dismiss();

                if (response != null && response.body() != null && response.body().getCode() == 1) {
                    BrokenDocument brokenDocument = response.body().getData().get(0).getRecordData();
                    mBrokenDocument.setUploadFlag(1);
                    mBrokenDocument.setPlatformId(response.body().getData().get(0).getPlatformId());
                    mBrokenDocument.setDocmentNo(brokenDocument.getDocmentNo());
                    BrokenDocumentDBHelper.getInstance().updateById(mBrokenDocument);
                }
                clearDatas();
                handRequestErr("建档成功");
                EventBus.getDefault().post(new UpdateBrokenListEvent());
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<ListCallBack<BrokenDocument>>>> call, Throwable t) {
                mProgressDialog.dismiss();
                clearDatas();
                handRequestErr("建档成功");
                EventBus.getDefault().post(new UpdateBrokenListEvent());
            }
        });

    }

    //    @Override
    //    public void onAttach(Context context) {
    //        super.onAttach(context);
    //        EventBus.getDefault().register(this);
    //
    //    }
    //
    //    @Override
    //    public void onDetach() {
    //        super.onDetach();
    //        EventBus.getDefault().unregister(this);
    //    }


    //建档成功  清除数据
    private void clearDatas() {
        mTowerFromAdapter = null;
        mEtCompany.setText("");
        mEtContactPerson.setText("");
        mEtPhoneNum.setText("");
        mEtDescreiption.setText("");
        datas.clear();
        gridViewAddImgesAdpter.notifyDataSetChanged();
        mBrokenDocument = null;
        mBrokenTypeSpinner.setSelection(0);
    }


    private void saveBrokenDocument(BrokenDocument brokenDocument) {
        long id = BrokenDocumentDBHelper.getInstance().insertBroken(brokenDocument);
        mBrokenDocument.setId(id);
        saveImagePaths(brokenDocument);
    }

    /**
     * 将图片保存进数据库
     *
     * @param brokenDocument
     */
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


    private BrokenDocument create(int pos, Tower from, Tower to, String companys, String contactPerson, String phoneNum, String desc) {
        BrokenDocument brokenDocument = new BrokenDocument();
        int lineId = MyApplication.registeredTower.getSysGridLineId();
        String name = GridLineDBHelper.getInstance().getLine(lineId).getLineName();
        brokenDocument.setLineName(name);
        brokenDocument.setCreateDate(DateUtil.format(new Date()));
        brokenDocument.setUpdateTime(DateUtil.format(new Date()));
        brokenDocument.setBrokenType(mBrokenTypes.get(pos).getItemDetailsId());
        brokenDocument.setStartTowerId(from.getSysTowerID().intValue());
        brokenDocument.setStartTowerNo(from.getTowerNo());
        brokenDocument.setBrokenTypeName(mBrokenTypes.get(pos).getItemsName());
        brokenDocument.setEndTowerNo(to.getTowerNo());
        brokenDocument.setEndTowerId(to.getSysTowerID().intValue());
        brokenDocument.setCompany(companys);
        brokenDocument.setContactPerson(contactPerson);
        brokenDocument.setPhoneNo(phoneNum);
        brokenDocument.setRemark(desc);
        brokenDocument.setVoltageClass(GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId()).getVClass());

        if (AppConstant.currentUser != null) {
            if (AppConstant.currentUser.getUserId() == null || AppConstant.currentUser.getUserId().equals("")) {
                AppConstant.currentUser = UserDBHellper.getInstance().getUser(AppConstant.currentUser.getUserId());
            }
            brokenDocument.setSysUserId(AppConstant.currentUser.getUserId());
        }
        brokenDocument.setUploadFlag(0);//标识未上传
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                brokenDocument.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                brokenDocument.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            brokenDocument.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }

        return brokenDocument;
    }


    //
    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onEventMainThread(Object event) {
    //        if (event instanceof UpdateTowerEvent) {
    //            updateTower();
    //        }
    //    }

    @Override
    public void onResume() {
        super.onResume();
        updateTower();

    }

    private void updateTower() {
        if (mTowerFromAdapter == null && MyApplication.registeredTower != null) {
            int lineId = MyApplication.registeredTower.getSysGridLineId();
            //            String lineName = GridLineDBHelper.getInstance().getLine(lineId).getVClass() + " " + GridLineDBHelper.getInstance().getLine(lineId).getLineName();
            Gridline gridline = MyApplication.gridlines.get(lineId);
            if (gridline == null) {
                gridline = MyApplication.mTempLineMap.get(lineId);
            }

            String vclass = ItemDetailDBHelper.getInstance().getItem("电压等级", gridline.getVoltageClass());
            tvLineName.setText(vclass + gridline.getLineName());
            List<Tower> towerList = TowerDBHelper.getInstance().getLineTowers(lineId);
            mTowerFromAdapter = new UspinnerAdapter(towerList, mWeakReference.get());
            mSpinnerBreakFrom.setAdapter(mTowerFromAdapter);
            mTowerToAdapter = new UspinnerAdapter(towerList, mWeakReference.get());
            mSpinnerBreaktTo.setAdapter(mTowerToAdapter);
            try {
                int selectFrom = towerList.indexOf(MyApplication.currentNearestTower);
                mSpinnerBreakFrom.setSelection(selectFrom);
                int selectTo = towerList.indexOf(MyApplication.nearSecondTower);
                mSpinnerBreaktTo.setSelection(selectTo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}