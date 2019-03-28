package com.uflycn.uoperation.ui.fragment.manageproject.view;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.ProjectEntity;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.ChangePageEvent;
import com.uflycn.uoperation.eventbus.EditProjectCallback;
import com.uflycn.uoperation.eventbus.EditProjectEvent;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.PhotoGridActivity;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.adapter.SpinnerVcAdapter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenter;
import com.uflycn.uoperation.ui.fragment.manageproject.presenter.ProjectPresenterImpl;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.FileUtil;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;
import com.xflyer.utils.ThreadPoolUtils;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProjectFragment extends Fragment implements ProjectView {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.sp_voltage_class)
    Spinner mSpVoltageClass;
    @BindView(R.id.et_line_name)
    EditText mEtLineName;
    @BindView(R.id.et_tower_from)
    EditText mEtTowerFrom;
    @BindView(R.id.et_tower_to)
    EditText mEtTowerTo;
    @BindView(R.id.et_project_name)
    EditText mEtProjectName;
    @BindView(R.id.et_project_leader)
    EditText mEtProjectLeader;
    @BindView(R.id.add_img_project)
    AddImageGridView add_img_project;


    private ProjectEntity mProjectEntity;

    @BindView(R.id.et_project_description)
    EditText mDescription;


    private SpinnerVcAdapter mVoltageClassAdapter;
    private Reference<Context> mContextRef;
    private ProgressDialog mProgressDialog;
    private ProjectPresenter mProjectPresenter;
    private Call<ResponseBody> mDownImage = null;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH = 3;
    private String zipPath;
    private ProgressDialog progressDialog;

    private Unbinder mUnBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_create_project, container, false);
        mUnBinder = ButterKnife.bind(this, contentView);
        EventBus.getDefault().register(this);
        mContextRef = new WeakReference<Context>(getActivity());
        mProjectPresenter = new ProjectPresenterImpl(this);
        mProjectPresenter.attach();
        mTvName.setText("项目修改");

        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("正在加载项目信息");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return contentView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mProjectPresenter.dettach();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BaseMainThreadEvent event) {
        if (event instanceof EditProjectEvent) {
            mProjectEntity = ((EditProjectEvent) event).getProjectEntity();
            initData();
        } else if (event instanceof EditProjectCallback) {
            handleUpdateResult((EditProjectCallback) event);
        }
    }

    private void initData() {
        mEtLineName.setText(mProjectEntity.getGridLineName());
        if (StringUtils.isEmptyOrNull(mProjectEntity.getStartTowerNo())) {
            mEtTowerFrom.setText(mProjectEntity.getTowerSection().substring(0, mProjectEntity.getTowerSection().indexOf("-") ));
        } else {
            mEtTowerFrom.setText(mProjectEntity.getStartTowerNo());
        }
        if (StringUtils.isEmptyOrNull(mProjectEntity.getEndTowerNo())) {
            mEtTowerTo.setText(mProjectEntity.getTowerSection().substring(mProjectEntity.getTowerSection().indexOf("-") + 1));
        }else {
            mEtTowerTo.setText(mProjectEntity.getEndTowerNo());
        }
        mEtProjectName.setText(mProjectEntity.getProjectName());
        mEtProjectLeader.setText(mProjectEntity.getProjectManager());
        mDescription.setText(mProjectEntity.getProjectDescription());
        List<ItemDetail> itemDetails = ItemDetailDBHelper.getInstance().getItem("电压等级");
        mVoltageClassAdapter = new SpinnerVcAdapter(itemDetails, mContextRef.get());
        mSpVoltageClass.setAdapter(mVoltageClassAdapter);

        int position = itemDetails.indexOf(ItemDetailDBHelper.getInstance().getItemDetailByItemsName(mProjectEntity.getVoltageClass()));
        mSpVoltageClass.setSelection(position);

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
        zipPath = IOUtils.getRootStoragePath(this.getContext()) + File.separator + AppConstant.CAMERA_PHOTO_PATH +
                File.separator + AppConstant.BREAK_PHOTO_PATH;
        ProjectAsyncTask breakPicAsyncTask = new ProjectAsyncTask();
        breakPicAsyncTask.execute(mProjectEntity.getPlatformId());
    }

    @OnClick({R.id.btn_submit, R.id.iv_back, R.id.tv_old_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(new ChangePageEvent(1));
                break;
            case R.id.btn_submit:
                submitEdit();
                break;
            case R.id.tv_old_img:
                jumpToPhotoGrid();
                break;
        }
    }

    private void jumpToPhotoGrid() {
        Intent photo = new Intent(this.getActivity(), PhotoGridActivity.class);
        photo.putExtra("path", zipPath);
        startActivity(photo);
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


    private void handleUpdateResult(EditProjectCallback event) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if (event.getMessage() == null || event.getMessage().equalsIgnoreCase("")) {

            ToastUtil.show("修改完成");
        }
        EventBus.getDefault().post(new ChangePageEvent(1));//跳转到列表页
    }

    private void submitEdit() {
        String lineName = mEtLineName.getText().toString().trim();
        String towerFrom = mEtTowerFrom.getText().toString().trim();
        String towerTo = mEtTowerTo.getText().toString().trim();
        String projectName = mEtProjectName.getText().toString().trim();
        String projectLeader = mEtProjectLeader.getText().toString().trim();
        String projectDescription = mDescription.getText().toString().trim();
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

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContextRef.get());
            mProgressDialog.setMessage("正在提交...");
            mProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mProjectPresenter.cancel();
                }
            });
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();


        editProject(lineName, towerFrom, towerTo, projectName, projectLeader, projectDescription, createTime, createUserName);

    }

    /**
     * 修改项目
     *
     * @param lineName
     * @param towerFrom
     * @param towerTo
     * @param projectName
     * @param projectLeader
     * @param projectDescription
     * @param createTime
     * @param createUserName
     */
    private void editProject(String lineName, String towerFrom, String towerTo, String projectName, String projectLeader, String projectDescription, String createTime, String createUserName) {
        //        ProjectEntity projectEntity = new ProjectEntity();
        mProjectEntity.setGridLineName(lineName);
        mProjectEntity.setStartTowerNo(towerFrom);
        mProjectEntity.setEndTowerNo(towerTo);
        mProjectEntity.setProjectDescription(projectDescription);
        mProjectEntity.setCreatedTime(createTime);
        mProjectEntity.setCreatedBy(createUserName);
        mProjectEntity.setProjectManager(projectLeader);
        mProjectEntity.setProjectName(projectName);
        mProjectEntity.setUploadFlag(0);
        mProjectEntity.setVoltageClass(((ItemDetail) mSpVoltageClass.getSelectedItem()).getItemDetailsId());
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                mProjectEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                mProjectEntity.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            mProjectEntity.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        mProjectPresenter.editProject(mProjectEntity, datas);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class ProjectAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(final Integer... params) {
            FileUtil.makeDir(zipPath);
            zipPath = zipPath + File.separator + params[0] + ".zip";
            File file = new File(zipPath.substring(0, zipPath.lastIndexOf(".")));


            mDownImage = RetrofitManager.getInstance().getService(ApiService.class).getImageZip(5, params[0]);
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
                    if (t != null) {
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
