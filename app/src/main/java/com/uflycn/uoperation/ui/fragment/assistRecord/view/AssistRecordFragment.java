package com.uflycn.uoperation.ui.fragment.assistRecord.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.AssistRecord;
import com.uflycn.uoperation.bean.FileList;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.GroundingLead;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.PullLine;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.TowerBrand;
import com.uflycn.uoperation.bean.TowerHead;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.AssistRecordDBHelper;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class AssistRecordFragment extends Fragment {

    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.tv_tower_no)
    TextView mTvTowerNo;
    @BindView(R.id.atv_tower_head)
    AddImageGridView mAtvTowerHead;
    @BindView(R.id.ll_tower_head)
    LinearLayout mLlTowerHead;
    @BindView(R.id.atv_tower_no)
    AddImageGridView mAtvTowerNo;
    @BindView(R.id.ll_tower_no)
    LinearLayout mLlTowerNo;
    @BindView(R.id.rb_same)
    RadioButton mRbSame;
    @BindView(R.id.rb_no_same)
    RadioButton mRbNoSame;
    @BindView(R.id.rg_is_same)
    RadioGroup mRgIsSame;
    @BindView(R.id.sp_terrain)
    Spinner mSpTerrain;
    @BindView(R.id.atv_down_conductor_1)
    AddImageGridView mAtvDownConductor1;
    @BindView(R.id.ll_down_conductor_1)
    LinearLayout mLlDownConductor1;
    @BindView(R.id.atv_down_conductor_2)
    AddImageGridView mAtvDownConductor2;
    @BindView(R.id.ll_down_conductor_2)
    LinearLayout mLlDownConductor2;
    @BindView(R.id.atv_down_conductor_3)
    AddImageGridView mAtvDownConductor3;
    @BindView(R.id.ll_down_conductor_3)
    LinearLayout mLlDownConductor3;
    @BindView(R.id.atv_down_conductor_4)
    AddImageGridView mAtvDownConductor4;
    @BindView(R.id.ll_down_conductor_4)
    LinearLayout mLlDownConductor4;
    @BindView(R.id.sp_defect_level)
    Spinner mSpDefectLevel;
    @BindView(R.id.rb_antiseptic)
    RadioButton mRbAntiseptic;
    @BindView(R.id.rb_no_antiseptic)
    RadioButton mRbNoAntiseptic;
    @BindView(R.id.rg_is_antiseptic)
    RadioGroup mRgIsAntiseptic;
    @BindView(R.id.atv_pull_pull_rods_1)
    AddImageGridView mAtvPullPullRods1;
    @BindView(R.id.ll_pull_pull_rods_1)
    LinearLayout mLlPullPullRods1;
    @BindView(R.id.atv_pull_pull_rods_2)
    AddImageGridView mAtvPullPullRods2;
    @BindView(R.id.ll_pull_pull_rods_2)
    LinearLayout mLlPullPullRods2;
    @BindView(R.id.atv_pull_pull_rods_3)
    AddImageGridView mAtvPullPullRods3;
    @BindView(R.id.ll_pull_pull_rods_3)
    LinearLayout mLlPullPullRods3;
    @BindView(R.id.atv_pull_pull_rods_4)
    AddImageGridView mAtvPullPullRods4;
    @BindView(R.id.ll_pull_pull_rods_4)
    LinearLayout mLlPullPullRods4;
    @BindView(R.id.atv_pull_pull_rods_5)
    AddImageGridView mAtvPullPullRods5;
    @BindView(R.id.ll_pull_pull_rods_5)
    LinearLayout mLlPullPullRods5;
    @BindView(R.id.atv_pull_pull_rods_6)
    AddImageGridView mAtvPullPullRods6;
    @BindView(R.id.ll_pull_pull_rods_6)
    LinearLayout mLlPullPullRods6;
    @BindView(R.id.atv_pull_pull_rods_7)
    AddImageGridView mAtvPullPullRods7;
    @BindView(R.id.ll_pull_pull_rods_7)
    LinearLayout mLlPullPullRods7;
    @BindView(R.id.atv_pull_pull_rods_8)
    AddImageGridView mAtvPullPullRods8;
    @BindView(R.id.ll_pull_pull_rods_8)
    LinearLayout mLlPullPullRods8;
    @BindView(R.id.sp_defect_level_pull)
    Spinner mSpDefectLevelPull;
    @BindView(R.id.rb_antiseptic_pull)
    RadioButton mRbAntisepticPull;
    @BindView(R.id.rb_no_antiseptic_pull)
    RadioButton mRbNoAntisepticPull;
    @BindView(R.id.rg_is_antiseptic_pull)
    RadioGroup mRgIsAntisepticPull;
    @BindView(R.id.rg_evaluate)
    RadioGroup mRgEvaluate;
    @BindView(R.id.et_evaluate)
    EditText mEtEvaluate;
    private File tempFile;
    private String IMAGE_DIR;
    private final int PHOTO_REQUEST_CAREMA_TOWER_HEAD = 11;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_TOWER_HEAD = 12;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_TOWER_NO = 21;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_TOWER_NO = 22;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR1 = 311;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR1 = 312;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR2 = 321;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR2 = 322;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR3 = 331;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR3 = 332;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR4 = 341;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR4 = 342;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_1 = 411;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_1 = 412;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_2 = 421;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_2 = 422;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_3 = 431;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_3 = 432;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_4 = 441;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_4 = 442;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_5 = 451;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_5 = 452;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_6 = 461;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_6 = 462;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_7 = 471;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_7 = 472;// 从相册中选择
    private final int PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_8 = 481;// 杆塔头拍照
    private final int PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_8 = 482;// 从相册中选择
    private List<Map<String, Object>> mTowerHeadList = new ArrayList<>();
    private List<Map<String, Object>> mTowerNoList = new ArrayList<>();
    private List<Map<String, Object>> mDownConductor1List = new ArrayList<>();
    private List<Map<String, Object>> mDownConductor2List = new ArrayList<>();
    private List<Map<String, Object>> mDownConductor3List = new ArrayList<>();
    private List<Map<String, Object>> mDownConductor4List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods1List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods2List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods3List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods4List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods5List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods6List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods7List = new ArrayList<>();
    private List<Map<String, Object>> mPullPullRods8List = new ArrayList<>();
    private GridViewAddImgesAdpter mTowerHeadAdapter;
    private GridViewAddImgesAdpter mTowerNoAdapter;
    private GridViewAddImgesAdpter mDownConductor1Adapter;
    private GridViewAddImgesAdpter mDownConductor2Adapter;
    private GridViewAddImgesAdpter mDownConductor3Adapter;
    private GridViewAddImgesAdpter mDownConductor4Adapter;
    private GridViewAddImgesAdpter mPullPullRods1Adapter;
    private GridViewAddImgesAdpter mPullPullRods2Adapter;
    private GridViewAddImgesAdpter mPullPullRods3Adapter;
    private GridViewAddImgesAdpter mPullPullRods4Adapter;
    private GridViewAddImgesAdpter mPullPullRods5Adapter;
    private GridViewAddImgesAdpter mPullPullRods6Adapter;
    private GridViewAddImgesAdpter mPullPullRods7Adapter;
    private GridViewAddImgesAdpter mPullPullRods8Adapter;
    private List<String> mDefectList;
    private List<String> mPhotoList = new ArrayList<>();
    private Gson mGson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assist_record, null);
        ButterKnife.bind(this, view);
        mDefectList = Arrays.asList(UIUtils.getStringArray(R.array.defect_level));
        mGson = new Gson();
        initView();
        return view;
    }

    private void initView() {
        if (MyApplication.registeredTower != null) {
            mTvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
            Gridline gridline = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
            mTvLineName.setText(gridline.getLineName());
        }

        //杆塔头
        mTowerHeadAdapter = new GridViewAddImgesAdpter(mTowerHeadList, getContext());
        initGridView(mAtvTowerHead, mTowerHeadAdapter, PHOTO_REQUEST_CAREMA_TOWER_HEAD, PHOTO_REQUEST_GALLERY_TOWER_HEAD, 1);
        //杆号牌
        mTowerNoAdapter = new GridViewAddImgesAdpter(mTowerNoList, getContext());
        initGridView(mAtvTowerNo, mTowerNoAdapter, PHOTO_REQUEST_CAREMA_TOWER_NO, PHOTO_REQUEST_GALLERY_TOWER_NO, 1);
        mSpTerrain.setAdapter(getAdapter("地形"));
        //接地引下线
        mDownConductor1Adapter = new GridViewAddImgesAdpter(mDownConductor1List, getContext());
        initGridView(mAtvDownConductor1, mDownConductor1Adapter, PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR1, PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR1, 3);
        mDownConductor2Adapter = new GridViewAddImgesAdpter(mDownConductor2List, getContext());
        initGridView(mAtvDownConductor2, mDownConductor2Adapter, PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR2, PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR2, 3);
        mDownConductor3Adapter = new GridViewAddImgesAdpter(mDownConductor3List, getContext());
        initGridView(mAtvDownConductor3, mDownConductor3Adapter, PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR3, PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR3, 3);
        mDownConductor4Adapter = new GridViewAddImgesAdpter(mDownConductor4List, getContext());
        initGridView(mAtvDownConductor4, mDownConductor4Adapter, PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR4, PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR4, 3);
        mSpDefectLevel.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mDefectList));

        //拉线拉棒
        mPullPullRods1Adapter = new GridViewAddImgesAdpter(mPullPullRods1List, getContext());
        initGridView(mAtvPullPullRods1, mPullPullRods1Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_1, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_1, 6);
        mPullPullRods2Adapter = new GridViewAddImgesAdpter(mPullPullRods2List, getContext());
        initGridView(mAtvPullPullRods2, mPullPullRods2Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_2, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_2, 6);
        mPullPullRods3Adapter = new GridViewAddImgesAdpter(mPullPullRods3List, getContext());
        initGridView(mAtvPullPullRods3, mPullPullRods3Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_3, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_3, 6);
        mPullPullRods4Adapter = new GridViewAddImgesAdpter(mPullPullRods4List, getContext());
        initGridView(mAtvPullPullRods4, mPullPullRods4Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_4, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_4, 6);
        mPullPullRods5Adapter = new GridViewAddImgesAdpter(mPullPullRods5List, getContext());
        initGridView(mAtvPullPullRods5, mPullPullRods5Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_5, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_5, 6);
        mPullPullRods6Adapter = new GridViewAddImgesAdpter(mPullPullRods6List, getContext());
        initGridView(mAtvPullPullRods6, mPullPullRods6Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_6, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_6, 6);
        mPullPullRods7Adapter = new GridViewAddImgesAdpter(mPullPullRods7List, getContext());
        initGridView(mAtvPullPullRods7, mPullPullRods7Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_7, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_7, 6);
        mPullPullRods8Adapter = new GridViewAddImgesAdpter(mPullPullRods8List, getContext());
        initGridView(mAtvPullPullRods8, mPullPullRods8Adapter, PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_8, PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_8, 6);
        mSpDefectLevelPull.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mDefectList));

    }

    private void initGridView(AddImageGridView view, final GridViewAddImgesAdpter adpter,
                              final int photoCode, final int galleryCode, int photoCount) {
        adpter.setMaxImages(photoCount);
        view.setAdapter(adpter);
        view.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                switch (code) {
                    case 1:
                        camera(photoCode);
                        break;
                    case 2:
                        gallery(adpter, galleryCode);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    /**
     * 拍照
     *
     * @param requestCode
     */
    public void camera(int requestCode) {
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            IMAGE_DIR = IOUtils.getRootStoragePath(getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "辅助记录";
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
            startActivityForResult(intent, requestCode);
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
     *
     * @param adpter
     * @param requestCode
     */
    public void gallery(GridViewAddImgesAdpter adpter, int requestCode) {
        Intent intent = new Intent(getContext(), PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        int nowNeed = adpter.getMaxImages() - adpter.getCount() + 1;
        intent.putExtra("limit", nowNeed);//number是选择图片的数量
        startActivityForResult(intent, requestCode);
    }

    private ArrayAdapter<SpinnerOption> getAdapter(String type) {
        List<ItemDetail> itemDetails = ItemDetailDBHelper.getInstance().getItem(type);
        List<SpinnerOption> list = new ArrayList<>();
        for (int i = 0; i < itemDetails.size(); i++) {
            SpinnerOption spinnerOption = new SpinnerOption(itemDetails.get(i).getItemDetailsId(), itemDetails.get(i).getItemsName());
            list.add(spinnerOption);
        }
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY_TOWER_HEAD:
                    updateAdapterFromPhoto(data, mTowerHeadList, mTowerHeadAdapter);
                    break;
                case PHOTO_REQUEST_CAREMA_TOWER_HEAD:
                    updateAdapterFromGrallery(mTowerHeadList, mTowerHeadAdapter);
                    break;
                case PHOTO_REQUEST_GALLERY_TOWER_NO:
                    updateAdapterFromPhoto(data, mTowerNoList, mTowerNoAdapter);
                    break;
                case PHOTO_REQUEST_CAREMA_TOWER_NO:
                    updateAdapterFromGrallery(mTowerNoList, mTowerNoAdapter);
                    break;
                case PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR1:
                    updateAdapterFromPhoto(data, mDownConductor1List, mDownConductor1Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR1:
                    updateAdapterFromGrallery(mDownConductor1List, mDownConductor1Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR2:
                    updateAdapterFromPhoto(data, mDownConductor2List, mDownConductor2Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR2:
                    updateAdapterFromGrallery(mDownConductor2List, mDownConductor2Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR3:
                    updateAdapterFromPhoto(data, mDownConductor3List, mDownConductor3Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR3:
                    updateAdapterFromGrallery(mDownConductor3List, mDownConductor3Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_DOWN_CONDUCTOR4:
                    updateAdapterFromPhoto(data, mDownConductor4List, mDownConductor4Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_DOWN_CONDUCTOR4:
                    updateAdapterFromGrallery(mDownConductor4List, mDownConductor4Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_1:
                    updateAdapterFromPhoto(data, mPullPullRods1List, mPullPullRods1Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_1:
                    updateAdapterFromGrallery(mPullPullRods1List, mPullPullRods1Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_2:
                    updateAdapterFromPhoto(data, mPullPullRods2List, mPullPullRods2Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_2:
                    updateAdapterFromGrallery(mPullPullRods2List, mPullPullRods2Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_3:
                    updateAdapterFromPhoto(data, mPullPullRods3List, mPullPullRods3Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_3:
                    updateAdapterFromGrallery(mPullPullRods3List, mPullPullRods3Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_4:
                    updateAdapterFromPhoto(data, mPullPullRods4List, mPullPullRods4Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_4:
                    updateAdapterFromGrallery(mPullPullRods4List, mPullPullRods4Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_5:
                    updateAdapterFromPhoto(data, mPullPullRods5List, mPullPullRods5Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_5:
                    updateAdapterFromGrallery(mPullPullRods5List, mPullPullRods5Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_6:
                    updateAdapterFromPhoto(data, mPullPullRods6List, mPullPullRods6Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_6:
                    updateAdapterFromGrallery(mPullPullRods6List, mPullPullRods6Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_7:
                    updateAdapterFromPhoto(data, mPullPullRods7List, mPullPullRods7Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_7:
                    updateAdapterFromGrallery(mPullPullRods7List, mPullPullRods7Adapter);
                    break;
                case PHOTO_REQUEST_GALLERY_PULL_PULL_RODS_8:
                    updateAdapterFromPhoto(data, mPullPullRods8List, mPullPullRods8Adapter);
                    break;
                case PHOTO_REQUEST_CAREMA_PULL_PULL_RODS_8:
                    updateAdapterFromGrallery(mPullPullRods8List, mPullPullRods8Adapter);
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateAdapterFromGrallery(List<Map<String, Object>> list, GridViewAddImgesAdpter adpter) {
        if (hasSdcard()) {
            if (tempFile != null) {
                photoPath(tempFile.getPath(), list, adpter);
            } else {
                ToastUtil.show("相机异常请稍后再试！");
            }
        } else {
            ToastUtil.show("未找到存储卡，无法存储照片！");
        }
    }

    private void updateAdapterFromPhoto(Intent data, List<Map<String, Object>> list, GridViewAddImgesAdpter adpter) {
        if (data != null) {
            List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
            //处理代码
            for (String path : paths) {
                photoPath(path, list, adpter);
            }
        }
    }

    /**
     * 对于文件进行剪切。展示
     *
     * @param path
     */
    public void photoPath(String path, List<Map<String, Object>> list, GridViewAddImgesAdpter adpter) {
        IMAGE_DIR = IOUtils.getRootStoragePath(getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "辅助记录";
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
        FileUtils.changeBitmapPath(path, fileName);
        SendBroadcasd2MEDIA.sendBroadcasd(getContext(), IMAGE_DIR);
        Map<String, Object> map = new HashMap<>();
        map.put("path", fileName);
        list.add(map);
        adpter.notifyDataSetChanged();
        if (adpter == mPullPullRods1Adapter) {
            refreshGridView(adpter, mLlPullPullRods1);
        } else if (adpter == mPullPullRods2Adapter) {
            refreshGridView(adpter, mLlPullPullRods2);
        } else if (adpter == mPullPullRods3Adapter) {
            refreshGridView(adpter, mLlPullPullRods3);
        } else if (adpter == mPullPullRods4Adapter) {
            refreshGridView(adpter, mLlPullPullRods4);
        } else if (adpter == mPullPullRods5Adapter) {
            refreshGridView(adpter, mLlPullPullRods5);
        } else if (adpter == mPullPullRods6Adapter) {
            refreshGridView(adpter, mLlPullPullRods6);
        } else if (adpter == mPullPullRods7Adapter) {
            refreshGridView(adpter, mLlPullPullRods7);
        } else if (adpter == mPullPullRods8Adapter) {
            refreshGridView(adpter, mLlPullPullRods8);
        }
    }

    private void refreshGridView(final GridViewAddImgesAdpter adpter, final LinearLayout view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        int count = adpter.getCount();
        if (count == 3) {
            params.height = UIUtils.dp2px(80);
        } else if (count >= 5) {
            params.height = UIUtils.dp2px(160);
        } else {
            params.height = UIUtils.dp2px(80 * (count / 3 + 1));
        }
        view.setLayoutParams(params);
        adpter.setListener(new GridViewAddImgesAdpter.onRemovePhotoListener() {
            @Override
            public void onRemovePhoto() {
                refreshGridView(adpter, view);
            }
        });
    }


    @OnClick(R.id.btn_submit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (MyApplication.registeredTower == null) {
            ToastUtil.show("请先到位登记");
            return;
        }
        if (mTowerHeadList.size() == 0) {
            ToastUtil.show("请添加杆塔头图片");
            return;
        }
        if (mTowerNoList.size() == 0) {
            ToastUtil.show("请添加杆号牌图片");
            return;
        }
        if (mSpTerrain.getSelectedItem()==null){
            ToastUtil.show("请选择地形数据");
            return;
        }
        if (mSpDefectLevel.getSelectedItem()==null||mSpDefectLevelPull.getSelectedItem()==null){
            ToastUtil.show("请选择缺陷等级");
            return;
        }
        if (mDownConductor1List.size() == 0 && mDownConductor2List.size() == 0
                && mDownConductor3List.size() == 0 && mDownConductor4List.size() == 0) {
            ToastUtil.show("请添加接地引下线图片");
            return;
        }
        if (mPullPullRods1List.size() == 0 && mPullPullRods2List.size() == 0 &&
                mPullPullRods3List.size() == 0 && mPullPullRods4List.size() == 0 &&
                mPullPullRods5List.size() == 0 && mPullPullRods6List.size() == 0 &&
                mPullPullRods7List.size() == 0 && mPullPullRods8List.size() == 0) {
            ToastUtil.show("请添加拉线.拉棒图片");
            return;
        }

        AssistRecord assistRecord = createAssistRecord();
        saveAssistRecord(assistRecord);
        EventBus.getDefault().post(assistRecord);
        ToastUtil.show("数据移交后台提交");
        reset();
    }

    private void reset() {
        mPhotoList.clear();
        mTowerHeadList.clear();
        mTowerNoList.clear();
        mDownConductor1List.clear();
        mDownConductor2List.clear();
        mDownConductor3List.clear();
        mDownConductor4List.clear();
        mPullPullRods1List.clear();
        mPullPullRods2List.clear();
        mPullPullRods3List.clear();
        mPullPullRods4List.clear();
        mPullPullRods5List.clear();
        mPullPullRods6List.clear();
        mPullPullRods7List.clear();
        mPullPullRods8List.clear();
        mRgIsSame.check(R.id.rb_same);
        mRgEvaluate.check(R.id.rb_evaluate_good);
        mRgIsAntiseptic.check(R.id.rb_antiseptic);
        mRgIsAntisepticPull.check(R.id.rb_antiseptic_pull);
        mSpTerrain.setSelection(0);
        mSpDefectLevelPull.setSelection(0);
        mSpDefectLevel.setSelection(0);
        mEtEvaluate.setText("");
        mTowerNoAdapter.notifyDataSetChanged();
        mTowerHeadAdapter.notifyDataSetChanged();
        mDownConductor1Adapter.notifyDataSetChanged();
        mDownConductor2Adapter.notifyDataSetChanged();
        mDownConductor3Adapter.notifyDataSetChanged();
        mDownConductor4Adapter.notifyDataSetChanged();
        mPullPullRods1Adapter.notifyDataSetChanged();
        mPullPullRods2Adapter.notifyDataSetChanged();
        mPullPullRods3Adapter.notifyDataSetChanged();
        mPullPullRods4Adapter.notifyDataSetChanged();
        mPullPullRods5Adapter.notifyDataSetChanged();
        mPullPullRods6Adapter.notifyDataSetChanged();
        mPullPullRods7Adapter.notifyDataSetChanged();
        mPullPullRods8Adapter.notifyDataSetChanged();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLlPullPullRods1.getLayoutParams();
        params.height = UIUtils.dp2px(80);
        mLlPullPullRods1.setLayoutParams(params);
        mLlPullPullRods2.setLayoutParams(params);
        mLlPullPullRods3.setLayoutParams(params);
        mLlPullPullRods4.setLayoutParams(params);
        mLlPullPullRods5.setLayoutParams(params);
        mLlPullPullRods6.setLayoutParams(params);
        mLlPullPullRods7.setLayoutParams(params);
        mLlPullPullRods8.setLayoutParams(params);
    }


    private AssistRecord createAssistRecord() {
        AssistRecord assistRecord = new AssistRecord();
        assistRecord.setRemark(mEtEvaluate.getText().toString());
        assistRecord.setIsAgreement(mRgIsSame.getCheckedRadioButtonId() == R.id.rb_same);
        assistRecord.setGroundingLeadServerityLevel(mSpDefectLevel.getSelectedItemPosition() + 1);
        assistRecord.setGroundingLeadIsInsulate(mRgIsAntiseptic.getCheckedRadioButtonId() == R.id.rb_antiseptic);
        assistRecord.setPullLineIsInsulate(mRgIsAntisepticPull.getCheckedRadioButtonId() == R.id.rb_antiseptic_pull);
        assistRecord.setPullLineServerityLevel(mSpDefectLevelPull.getSelectedItemPosition() + 1);
        assistRecord.setTopography(((SpinnerOption) mSpTerrain.getSelectedItem()).getValue());
        assistRecord.setSysTowerID(MyApplication.registeredTower.getSysTowerID().intValue());
        if (mRgEvaluate.getCheckedRadioButtonId() == R.id.rb_evaluate_good) {
            assistRecord.setAssess(1);
        } else if (mRgEvaluate.getCheckedRadioButtonId() == R.id.rb_evaluate_general) {
            assistRecord.setAssess(2);
        } else {
            assistRecord.setAssess(3);
        }
        FileList fileList = new FileList();
        List<TowerBrand> towerBrands = new ArrayList<>();
        for (int i = 0; i < mTowerNoList.size(); i++) {
            TowerBrand towerBrand = new TowerBrand(getFileName((String) mTowerNoList.get(i).get("path")));
            towerBrands.add(towerBrand);
            mPhotoList.add((String) mTowerNoList.get(i).get("path"));
            fileList.setTowerBrand(towerBrands);
        }
        List<TowerHead> towerHeads = new ArrayList<>();
        for (int i = 0; i < mTowerHeadList.size(); i++) {
            TowerHead towerHead = new TowerHead(getFileName((String) mTowerHeadList.get(i).get("path")));
            towerHeads.add(towerHead);
            mPhotoList.add((String) mTowerHeadList.get(i).get("path"));
            fileList.setTowerHead(towerHeads);
        }
        List<GroundingLead> groundingLeads = new ArrayList<>();
        initGroundingLead(mDownConductor1List, 101, groundingLeads);
        initGroundingLead(mDownConductor2List, 102, groundingLeads);
        initGroundingLead(mDownConductor3List, 103, groundingLeads);
        initGroundingLead(mDownConductor4List, 104, groundingLeads);
        fileList.setGroundingLead(groundingLeads);

        List<PullLine> pullLines = new ArrayList<>();
        initPullLine(mPullPullRods1List, 201, pullLines);
        initPullLine(mPullPullRods2List, 202, pullLines);
        initPullLine(mPullPullRods3List, 203, pullLines);
        initPullLine(mPullPullRods4List, 204, pullLines);
        initPullLine(mPullPullRods5List, 205, pullLines);
        initPullLine(mPullPullRods6List, 206, pullLines);
        initPullLine(mPullPullRods7List, 207, pullLines);
        initPullLine(mPullPullRods8List, 208, pullLines);
        fileList.setPullLine(pullLines);
        assistRecord.setFileList(fileList);
        assistRecord.setFileListJson(mGson.toJson(fileList));
        return assistRecord;
    }

    private void initGroundingLead(List<Map<String, Object>> list, int category, List<GroundingLead> groundingLeads) {
        for (int i = 0; i < list.size(); i++) {
            GroundingLead groundingLead = new GroundingLead(getFileName((String) list.get(i).get("path")), category);
            groundingLeads.add(groundingLead);
            mPhotoList.add((String) list.get(i).get("path"));
        }
    }

    private void initPullLine(List<Map<String, Object>> list, int category, List<PullLine> pullLines) {
        for (int i = 0; i < list.size(); i++) {
            PullLine groundingLead = new PullLine(getFileName((String) list.get(i).get("path")), category);
            pullLines.add(groundingLead);
            mPhotoList.add((String) list.get(i).get("path"));
        }
    }

    private void saveAssistRecord(AssistRecord assistRecord) {
        long id = AssistRecordDBHelper.getInstance().insert(assistRecord);
        assistRecord.setId(id);
        saveImagePaths(assistRecord);
    }


    private void saveImagePaths(AssistRecord assistRecord) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < mPhotoList.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(15);
            imagePaths.setLocalId(assistRecord.getId() + "");
            imagePaths.setPath(mPhotoList.get(i));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }


    private String getFileName(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.getName();
        } else {
            return "";
        }
    }
}
