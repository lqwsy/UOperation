package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.WorksheetApanageTask;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.RefreshListEvenBus;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.BatchDefectAdapter;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.adapter.SimpleDefectBean;
import com.uflycn.uoperation.util.FileUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClearDefectListActivity extends Activity {
    @BindView(R.id.tv_top_title)
    TextView mTvTitle;

    @BindView(R.id.tv_line_name)
    TextView mTvLineName;

    @BindView(R.id.tv_tower_from_to)
    TextView mTvTower;

    @BindView(R.id.lv_clear_def)
    ListView mLvClearDef;

    private Context mContext;
    private AddImageGridView add_img_defect;
    private AddImageGridView add_img_work;
    private AddImageGridView add_img_ok;
    private ProgressDialog mProgressDialog;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private String IMAGE_DIR;
    private final int MAX_IMAGE_LENGTH_ADD = 3;
    private static int PIC_FLAG = 0;//1 消缺图  2 工做单  3 补偿协议
    private BatchDefectAdapter batchDefectAdapter;
    private List<SimpleDefectBean> mSimpleDefectBean;

    private GridViewAddImgesAdpter DefectAdapter;
    private GridViewAddImgesAdpter WorkAdapter;
    private GridViewAddImgesAdpter OkAdapter;
    private List<Map<String, Object>> defectDatas;
    private List<Map<String, Object>> workDatas;
    private List<Map<String, Object>> okDatas;
    private Call<BaseCallBack<String>> mCall;
    private WorksheetApanageTask worksheetApanageTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_clear_defect_list);
        ButterKnife.bind(this);
        mContext = this;
        worksheetApanageTask = (WorksheetApanageTask) getIntent().getSerializableExtra("WorksheetApanageTask");
        initView();
        initData();
    }

    private void initView() {
        View mListViewFoot = LayoutInflater.from(this).inflate(R.layout.layout_clear_defect_list_foot, null);
        add_img_defect = (AddImageGridView) mListViewFoot.findViewById(R.id.add_img_defect);
        add_img_work = (AddImageGridView) mListViewFoot.findViewById(R.id.add_img_work);
        add_img_ok = (AddImageGridView) mListViewFoot.findViewById(R.id.add_img_ok);
        mLvClearDef.addFooterView(mListViewFoot);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mTvTitle.setText("编号:" + worksheetApanageTask.getTaskNo());
        mTvLineName.setText(worksheetApanageTask.getLineName());
        mTvTower.setText(worksheetApanageTask.getTowerA_No() + " - " + worksheetApanageTask.getTowerB_No());


    }

    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                doSubmit();
                break;
        }
    }

    private void doSubmit() {
        if (defectDatas == null || defectDatas.size() == 0
                || workDatas == null || workDatas.size() == 0 ||
                okDatas == null || okDatas.size() == 0) {
            ToastUtil.show("未上传照片！");
            return;
        }
        mProgressDialog.show();

        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        requestImgParts.addAll(creatImg(defectDatas, "Clear_"));
        requestImgParts.addAll(creatImg(workDatas, "Worksheet_"));
        requestImgParts.addAll(creatImg(okDatas, "Agreement_"));

        String json = "{\"sysApanageTaskId\":" + worksheetApanageTask.getSysApanageTaskId() + "}";
        requestImgParts.add(MultipartBody.Part.createFormData("JsonData", json));

        mCall = RetrofitManager.getInstance().getService(ApiService.class).clearApanageTask(requestImgParts);
        mCall.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                if (response.body() == null) {
                    return;
                }
                if (response.body() != null && response.body().getCode() == 1) {
                    ToastUtil.show("消缺成功");
                    EventBus.getDefault().post(new RefreshListEvenBus());
                    finish();
                }

            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                ToastUtil.show("网络连接失败");
            }
        });

    }

    private List<MultipartBody.Part> creatImg(List<Map<String, Object>> datas, String name) {
        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData(name + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
        return requestImgParts;
    }

    private void initData() {
        mSimpleDefectBean = new ArrayList<>();
        initListData();
        batchDefectAdapter = new BatchDefectAdapter(mSimpleDefectBean, mContext, R.layout.item_batch_defcet);
        mLvClearDef.setAdapter(batchDefectAdapter);
        addPic();
    }

    private void initListData() {
        if (worksheetApanageTask.getTreeDefectPointBeen() != null) {
            for (int i = 0; i < worksheetApanageTask.getTreeDefectPointBeen().size(); i++) {
                SimpleDefectBean simpleDefectBean = new SimpleDefectBean();
                simpleDefectBean.setGridLine(worksheetApanageTask.getLineName());
                simpleDefectBean.setRemark(worksheetApanageTask.getTreeDefectPointBeen().get(i).getRemark());
                mSimpleDefectBean.add(simpleDefectBean);
            }
        }
        if (worksheetApanageTask.getTowerDefectList() != null) {
            for (int i = 0; i < worksheetApanageTask.getTowerDefectList().size(); i++) {
                SimpleDefectBean simpleDefectBean = new SimpleDefectBean();
                simpleDefectBean.setGridLine(worksheetApanageTask.getLineName());
                simpleDefectBean.setRemark(worksheetApanageTask.getTowerDefectList().get(i).getRemark());
                mSimpleDefectBean.add(simpleDefectBean);
            }
        }
    }


    private void addPic() {
        defectDatas = new ArrayList<>();
        workDatas = new ArrayList<>();
        okDatas = new ArrayList<>();
        DefectAdapter = new GridViewAddImgesAdpter(defectDatas, this);
        DefectAdapter.setMaxImages(MAX_IMAGE_LENGTH_ADD);
        WorkAdapter = new GridViewAddImgesAdpter(workDatas, this);
        WorkAdapter.setMaxImages(MAX_IMAGE_LENGTH_ADD);
        OkAdapter = new GridViewAddImgesAdpter(okDatas, this);
        OkAdapter.setMaxImages(MAX_IMAGE_LENGTH_ADD);
        add_img_defect.setAdapter(DefectAdapter);
        add_img_defect.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                PIC_FLAG = 1;
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
        add_img_work.setAdapter(WorkAdapter);
        add_img_work.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                PIC_FLAG = 2;
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
        add_img_ok.setAdapter(OkAdapter);
        add_img_ok.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                PIC_FLAG = 3;
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
            IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "清障工单";
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
        int nowNeed = 3;
        switch (PIC_FLAG) {
            case 0:
                ToastUtil.show("有问题？？");
                break;
            case 1:
                nowNeed = DefectAdapter.getMaxImages() - DefectAdapter.getCount() + 1;
                break;
            case 2:
                nowNeed = WorkAdapter.getMaxImages() - WorkAdapter.getCount() + 1;
                break;
            case 3:
                nowNeed = OkAdapter.getMaxImages() - OkAdapter.getCount() + 1;
                break;
        }
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
        IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + AppConstant.CAMERA_PHOTO_PATH + File.separator + "清障工单";
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
        FileUtils.changeBitmapPath(path, fileName);
        SendBroadcasd2MEDIA.sendBroadcasd(this, IMAGE_DIR);
        Map<String, Object> map = new HashMap<>();
        map.put("path", fileName);

        switch (PIC_FLAG) {
            case 0:
                ToastUtil.show("有问题？？");
                break;
            case 1:
                defectDatas.add(map);
                DefectAdapter.notifyDataSetChanged();
                break;
            case 2:
                workDatas.add(map);
                WorkAdapter.notifyDataSetChanged();
                break;
            case 3:
                okDatas.add(map);
                OkAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }
}
