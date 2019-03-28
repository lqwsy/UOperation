package com.uflycn.uoperation.ui.fragment.manageproject.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.bean.ProjectInspection;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.EditProjectEvent;
import com.uflycn.uoperation.eventbus.InspectRecordEvent;
import com.uflycn.uoperation.eventbus.JumpEvent;
import com.uflycn.uoperation.eventbus.ProjectInspectEvent;
import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
import com.uflycn.uoperation.ui.activity.TourResultActivity;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenterImpl;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectRegisterFragment extends Fragment implements ProjectView {

    private ProjectEntity mProjectEntity;

    @BindView(R.id.tv_line_name)
    TextView mLineName;
    @BindView(R.id.tv_start_end_tower)
    TextView mTowerSection;
    @BindView(R.id.et_cross_description)
    EditText mDescription;
    @BindView(R.id.radiogroup)
    RadioGroup mProjectStaus;
    @BindView(R.id.add_img_register)
    AddImageGridView add_img_register;

    private Reference<Context> mContextReference;
    private ProgressDialog mProgressDialog;
    private ProjectPresenter mProjectPresenter;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;
    private int FLAG = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_project_register, container, false);
        ButterKnife.bind(this, contentView);
        mContextReference = new WeakReference<Context>(getActivity());
        EventBus.getDefault().register(this);
        //mContextReference = new WeakReference<Context>(getActivity());
        mProjectPresenter = new ProjectPresenterImpl(this);
        mProjectPresenter.attach();
        return contentView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mProjectPresenter.dettach();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onEventMainThread(BaseMainThreadEvent event) {
        if (event instanceof EditProjectEvent) {
            mProjectEntity = ((EditProjectEvent) event).getProjectEntity();
            initData();
        } else if (event instanceof ProjectInspectEvent) {
            handleCreateResult((ProjectInspectEvent) event);
        }
    }

    @Subscribe
    public void Jump(JumpEvent event) {
        mProjectEntity = event.getmProjectEntity();
        FLAG = event.getFlag();
        initData();
    }

    private void initData() {
        if (mProjectEntity.getProjectStatus() == 0) {
            mProjectStaus.check(R.id.rb_no_solve);
        } else {
            mProjectStaus.check(R.id.rb_solve);
        }
        mLineName.setText(mProjectEntity.getGridLineName());
        if (StringUtils.isEmptyOrNull(mProjectEntity.getStartTowerNo()) || StringUtils.isEmptyOrNull(mProjectEntity.getEndTowerNo())) {
            mTowerSection.setText(mProjectEntity.getTowerSection() + "");
        } else {
            mTowerSection.setText(mProjectEntity.getStartTowerNo() + " - " + mProjectEntity.getEndTowerNo());
        }

        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this.getContext());
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        add_img_register.setAdapter(gridViewAddImgesAdpter);
        add_img_register.getCodeResult(new AddImageGridView.AddCallBack() {
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
            IMAGE_DIR = IOUtils.getRootStoragePath(this.getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "项目";
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
        IMAGE_DIR = IOUtils.getRootStoragePath(this.getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "项目";
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


    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (FLAG == -1) {
                    EventBus.getDefault().post(new ChangePageEvent(1));
                } else {
                    ((TourResultActivity) mContextReference.get()).finish();
                }

                break;
            case R.id.btn_submit:
                subMitCreate();
                break;
        }
    }

    private void handleCreateResult(ProjectInspectEvent event) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (event.getResponseMesseage() == null || event.getResponseMesseage().equalsIgnoreCase("")) {
            ToastUtil.show("提交成功");
        }
        clearAllData();
        //刷新巡视登记 的记录

        if (FLAG != -1) {
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            intent.putExtra("toValue", AppConstant.WORK_SHEET);
            startActivity(intent);
        } else {
            UIUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new InspectRecordEvent(mProjectEntity));//跳转到列表页
                }
            }, 100);
            EventBus.getDefault().post(new ChangePageEvent(9));//跳转到列表页
        }
    }

    private void clearAllData() {
        datas.clear();
        gridViewAddImgesAdpter.notifyDataSetChanged();
        mDescription.setText("");
    }

    private void subMitCreate() {
        if (datas == null || datas.size() == 0) {
            ToastUtil.show("无巡视照片，请先拍照");
            return;
        }
        String description = mDescription.getText().toString().trim();
        if (StringUtils.isEmptyOrNull(description)) {
            ToastUtil.show("备注不能为空");
            return;
        }

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this.getContext());
            mProgressDialog.setMessage("正在提交...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mProjectPresenter.cancel();
                }
            });
        }
        mProgressDialog.show();


        ProjectInspection projectInspection = new ProjectInspection();
        projectInspection.setSysUserId(AppConstant.currentUser.getUserId());
        projectInspection.setRemark(description);
        projectInspection.setUploadFlag(0);
        int status = mProjectStaus.getCheckedRadioButtonId() == R.id.rb_no_solve ? 0 : 1;
        projectInspection.setProjectStatus(status);
        projectInspection.setCreateDate(DateUtil.format(new Date()));
        projectInspection.setSysProjectId(mProjectEntity.getPlatformId());//平台Id
        if (mProjectEntity.getSysBrokenDocumentId() != null) {//本地数据库Id
            projectInspection.setLocalProjectId(mProjectEntity.getSysBrokenDocumentId().intValue());
        }
        if (mProjectEntity.getSysTaskId() == 0) {
            int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(mProjectEntity.getPlatformId(), AppConstant.CROSS_DEFECT_SHEET);
            if (sysWorkId != -1) {
                projectInspection.setSysTaskId(sysWorkId);
                mProjectEntity.setSysTaskId(sysWorkId);
            }
        } else {
            projectInspection.setSysTaskId(mProjectEntity.getSysTaskId());
        }
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                projectInspection.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                projectInspection.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus ==3){
            projectInspection.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        mProjectPresenter.postInspectProject(projectInspection, datas, this.getContext());
    }


}
