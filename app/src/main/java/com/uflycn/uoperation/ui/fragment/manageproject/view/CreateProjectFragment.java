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
import android.widget.ImageView;
import android.widget.Spinner;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.CreateProjectEvent;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.adapter.SpinnerVcAdapter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenterImpl;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProjectFragment extends Fragment implements ProjectView {

    @BindView(R.id.et_line_name)
    EditText mEtLineName;
    @BindView(R.id.et_tower_from)
    EditText mEtTowerFrom;
    @BindView(R.id.et_tower_to)
    EditText mEtTowerTo;
    @BindView(R.id.et_project_leader)
    EditText mEtProjectLeader;
    @BindView(R.id.add_img_project)
    AddImageGridView add_img_project;
    @BindView(R.id.tv_old_img)
    ImageView tv_old_img;

    private Reference<Context> mContextReference;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.sp_voltage_class)
    Spinner mSpinnerVoltageClass;

    @BindView(R.id.et_project_name)
    EditText mProjectName;

    @BindView(R.id.et_project_description)
    EditText mProjectDescreption;

    private SpinnerVcAdapter mVoltageClassAdapter;
    private ProjectPresenter mProjectPresenter;

    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;

    private Unbinder mUnBinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_create_project, container, false);
        mUnBinder = ButterKnife.bind(this, content);
        EventBus.getDefault().register(this);
        mContextReference = new WeakReference<Context>(getActivity());
        mProjectPresenter = new ProjectPresenterImpl(this);
        mProjectPresenter.attach();
        initSpinnerSection();
        tv_old_img.setVisibility(View.GONE);
        initData();
        return content;
    }

    private void initData() {
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this.getContext());
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        add_img_project.setAdapter(gridViewAddImgesAdpter);
        add_img_project.getCodeResult(new AddImageGridView.AddCallBack() {
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


    @OnClick({R.id.btn_submit, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                subMitCreate();
                break;
            case R.id.iv_back:
                EventBus.getDefault().post(new ChangePageEvent(1));
                break;
        }
    }

    private void subMitCreate() {
        String lineName = mEtLineName.getText().toString().trim();
        String towerFrom = mEtTowerFrom.getText().toString().trim();
        String towerTo = mEtTowerTo.getText().toString().trim();
        String projectName = mProjectName.getText().toString().trim();
        String projectLeader = mEtProjectLeader.getText().toString().trim();
        String projectDescription = mProjectDescreption.getText().toString().trim();
        String createTime = DateUtil.format(new Date());
        String createUserName = AppConstant.currentUser.getRealName();
        if (StringUtils.isEmptyOrNull(lineName)) {
            ToastUtil.show("请输入线路名称");
            return;
        }
        if (StringUtils.isEmptyOrNull(towerFrom) || StringUtils.isEmptyOrNull(towerTo)) {
            ToastUtil.show("请输入起止杆塔");
            return;
        }
        if (StringUtils.isEmptyOrNull(projectName)) {
            ToastUtil.show("请输入项目名称");
            return;
        }
        if (StringUtils.isEmptyOrNull(projectLeader)) {
            ToastUtil.show("请输入项目负责人");
            return;
        }

        if (StringUtils.isEmptyOrNull(projectDescription)) {
            ToastUtil.show("请输入项目概述");
            return;
        }

        if (datas == null || datas.size() == 0) {
            ToastUtil.show("请上传项目照片");
            return;
        }


        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContextReference.get());
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


        createProject(lineName, towerFrom, towerTo, projectName, projectLeader, projectDescription, createTime, createUserName);

    }

    /**
     * 存储到数据库
     */
    private void createProject(String lineName, String towerFrom, String towerTo, String projectName, String projectLeader, String projectDescription, String createTime, String createUserName) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setGridLineName(lineName);
        projectEntity.setStartTowerNo(towerFrom);
        projectEntity.setEndTowerNo(towerTo);
        projectEntity.setProjectDescription(projectDescription);
        projectEntity.setCreatedTime(createTime);
        projectEntity.setCreatedBy(createUserName);
        projectEntity.setProjectManager(projectLeader);
        projectEntity.setProjectName(projectName);
        projectEntity.setVoltageClass(((ItemDetail) mSpinnerVoltageClass.getSelectedItem()).getItemDetailsId());
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                projectEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                projectEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            projectEntity.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        mProjectPresenter.createProject(projectEntity, datas);
    }

    private void initSpinnerSection() {
        mVoltageClassAdapter = new SpinnerVcAdapter(ItemDetailDBHelper.getInstance().getItem("电压等级"), mContextReference.get());
        mSpinnerVoltageClass.setAdapter(mVoltageClassAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mProjectPresenter.dettach();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseMainThreadEvent event) {
        if (event instanceof CreateProjectEvent) {
            handleCreateResult((CreateProjectEvent) event);
        }
    }

    private void handleCreateResult(CreateProjectEvent event) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (event.getResponseMesseage() == null || event.getResponseMesseage().equalsIgnoreCase("")) {
            ToastUtil.show("创建成功");
        }
        EventBus.getDefault().post(new ChangePageEvent(1));//跳转到列表页
        clearData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 清除fragment数据
     */
    public void clearData() {
        mEtLineName.setText("");
        mEtTowerFrom.setText("");
        mEtTowerTo.setText("");
        mProjectName.setText("");
        mEtProjectLeader.setText("");
        mProjectDescreption.setText("");
        mSpinnerVoltageClass.setSelection(0);
        datas.clear();
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }
}
