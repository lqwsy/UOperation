package com.uflycn.uoperation.ui.fragment.dayplan.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.DayPlan;
import com.uflycn.uoperation.bean.DayPlanDetail;
import com.uflycn.uoperation.bean.DayPlanInfo;
import com.uflycn.uoperation.bean.ImagePaths;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.StopAllTaskEvent;
import com.uflycn.uoperation.greendao.DayPlanDetailDBHelper;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.fragment.dayplan.presenter.DayPlanPresenterImp;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SaveImagePathsAsync;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 日计划关闭详情页面
 */
public class DayPlanWorkDetailActivity extends Activity implements DayPlanView.DayPlanWorkDetailView {

    @BindView(R.id.tv_plan_date)
    TextView mTvPlanDate;
    @BindView(R.id.rg_time_type)
    RadioGroup mRgTimeType;
    @BindView(R.id.tv_work_type)
    TextView mTvWorkType;
    @BindView(R.id.tv_work_content)
    TextView mTvWorkContent;
    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.tv_tour_towers)
    TextView mTourTowers;
    @BindView(R.id.rg_work_state)
    RadioGroup mRgWorkState;
    @BindView(R.id.add_img_work)
    AddImageGridView mAddImgWork;
    @BindView(R.id.ll_work_img)
    LinearLayout mLlWorkImg;
    @BindView(R.id.et_work_remark)
    EditText mEtWorkRemark;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    private DayPlan mDayPlan;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private final int MAX_IMAGE_LENGTH = 3;
    private File tempFile;
    private String IMAGE_DIR;
    private List<Map<String, Object>> datas;
    private DayPlanPresenterImp mPresenter;

    private ProgressDialog mProgressDialog;
    private int mIsFinish = 1;

    public static Intent newInstance(Context context, DayPlan dayPlan) {
        Intent intent = new Intent(context, DayPlanWorkDetailActivity.class);
        intent.putExtra("dayPlan", dayPlan);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_day_plan_detail);
        ButterKnife.bind(this);
        mDayPlan = (DayPlan) getIntent().getSerializableExtra("dayPlan");
        mPresenter = new DayPlanPresenterImp(this);
        initView();
        initData();
    }

    private void initData() {
        showDialog("加载中..");
        mPresenter.getDailyTaskInfoFromResponse(mDayPlan.getSysDailyPlanSectionID());
    }

    @OnClick({R.id.iv_open_close_drawer, R.id.btn_submit})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.iv_open_close_drawer:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }

    }

    private void submit() {
        if (datas.size() == 0) {
            ToastUtil.show("工作图片不能为空!");
            return;
        }
        if (StringUtils.isEmptyOrNull(mEtWorkRemark.getText().toString())) {
            ToastUtil.show("工作备注不能为空!");
            return;
        }
        DayPlanDetail dayPlanDetail = new DayPlanDetail();
        dayPlanDetail.setStatus(mIsFinish);
        dayPlanDetail.setSysDailyPlanSectionID(mDayPlan.getSysDailyPlanSectionID());
        dayPlanDetail.setWorkNote(mEtWorkRemark.getText().toString());

        dayPlanDetail.setId(DayPlanDetailDBHelper.getInstance().insert(dayPlanDetail));
        saveImagePaths(dayPlanDetail);
        showDialog("提交中..");
        postToNet(dayPlanDetail, datas);
    }

    private void postToNet(DayPlanDetail dayPlanDetail, List<Map<String, Object>> datas) {
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("ImageData" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        mPresenter.postDayPlanDetailToNet(dayPlanDetail, requestImgParts);
    }


    private void initView() {
        //头部
        int topType = mDayPlan.getIsFromTopType();
        switch (topType) {
            case DayPlanFragment.TOP_TYPE_DAY:
                mRgTimeType.check(R.id.rb_day);
                break;
            case DayPlanFragment.TOP_TYPE_WEEK:
                mRgTimeType.check(R.id.rb_week);
                break;
            case DayPlanFragment.TOP_TYPE_MONTH:
                mRgTimeType.check(R.id.rb_month);
                break;
            case DayPlanFragment.TOP_TYPE_YEAR:
                mRgTimeType.check(R.id.rb_year);
                break;
        }
        //让所有rb都不可点
        for (int i = 0; i < mRgTimeType.getChildCount(); i++) {
            mRgTimeType.getChildAt(i).setEnabled(false);
        }
        //设置基本信息
        mTvPlanDate.setText(mDayPlan.getStartDateString());
        mTvWorkType.setText(mDayPlan.getTypeOfWorkString());
        mTvWorkContent.setText(mDayPlan.getJobContent());
        //设置
        mRgWorkState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_has_finish) {
//                    mLlWorkImg.setVisibility(View.VISIBLE);
                    mIsFinish = 1;
                } else if (checkedId == R.id.rb_no_finish) {
//                    mLlWorkImg.setVisibility(View.GONE);
                    mIsFinish = 0;
                }
            }
        });
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        mAddImgWork.setAdapter(gridViewAddImgesAdpter);
        mAddImgWork.getCodeResult(new AddImageGridView.AddCallBack() {
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
            IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "每日任务";
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


    private void saveImagePaths(DayPlanDetail dayPlanDetail) {
        List<ImagePaths> imagePath = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePaths = new ImagePaths();
            imagePaths.setCategory(15);
            imagePaths.setLocalId(dayPlanDetail.getId() + "");
            imagePaths.setPath((String) datas.get(i).get("path"));
            imagePath.add(imagePaths);
        }
        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
        saveImagePathsAsync.execute(imagePath);
    }

    @Override
    public void onCloseDayPlan(DayPlanDetail dayPlanDetail) {
        missDialog();
        ToastUtil.show("提交成功");
        MyApplication.gridlines.clear();
        MyApplication.mDayPlanLineMap.clear();
        MyApplication.mOpenDayPlanLineMap.clear();
        MyApplication.mCurrentDayPlan = null;
        MyApplication.gridlineTaskStatus = 0;
        EventBus.getDefault().post(new StopAllTaskEvent());
        Intent intent = getIntent();
        intent.putExtra("IsFinish", true);
        setResult(1, intent);
        finish();
    }

    @Override
    public void onShowMessage(String msg) {
        missDialog();
        ToastUtil.show(msg);
    }

    /**
     * 更新信息
     */
    @Override
    public void onRefreshData(List<DayPlanInfo> infoList) {
        missDialog();
        DayPlanInfo dayPlanInfo = infoList.get(0);
        mTvLineName.setText(dayPlanInfo.getVoltageClass() + "  " + dayPlanInfo.getLineName());
        mTourTowers.setText(dayPlanInfo.getPatrolTowerNos());
    }

    public void showDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    public void missDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
