//package com.uflycn.uoperation.ui.activity;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Looper;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.baoyz.swipemenulistview.SwipeMenu;
//import com.baoyz.swipemenulistview.SwipeMenuCreator;
//import com.baoyz.swipemenulistview.SwipeMenuItem;
//import com.baoyz.swipemenulistview.SwipeMenuListView;
//import com.uflycn.uoperation.R;
//import com.uflycn.uoperation.app.MyApplication;
//import com.uflycn.uoperation.bean.BaseCallBack;
//import com.uflycn.uoperation.bean.DefectDeleteRecord;
//import com.uflycn.uoperation.bean.ImagePaths;
//import com.uflycn.uoperation.bean.SpinnerOption;
//import com.uflycn.uoperation.bean.Tower;
//import com.uflycn.uoperation.bean.TreeDefectPointBean;
//import com.uflycn.uoperation.constant.AppConstant;
//import com.uflycn.uoperation.greendao.DefectDeleteDBHelper;
//import com.uflycn.uoperation.greendao.TowerDBHelper;
//import com.uflycn.uoperation.greendao.TreeDefectDBHelper;
//import com.uflycn.uoperation.greendao.WorkSheetTaskDBHelper;
//import com.uflycn.uoperation.http.ApiService;
//import com.uflycn.uoperation.http.RetrofitManager;
//import com.uflycn.uoperation.ui.adapter.BatchDefectAdapter;
//import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
//import com.uflycn.uoperation.util.DateUtil;
//import com.uflycn.uoperation.util.FileUtils;
//import com.uflycn.uoperation.util.IOUtils;
//import com.uflycn.uoperation.util.SaveImagePathsAsync;
//import com.uflycn.uoperation.util.SendBroadcasd2MEDIA;
//import com.uflycn.uoperation.util.StringUtils;
//import com.uflycn.uoperation.util.ToastUtil;
//import com.uflycn.uoperation.util.UIUtils;
//import com.uflycn.uoperation.widget.AddImageGridView;
//import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class BatchDefcetActivity extends Activity implements AdapterView.OnItemSelectedListener {
//
//    private static final String TAG = "BatchDefcetActivity";
//
//    @BindView(R.id.tv_line_name)
//    TextView mTvLineName;
//
//    @BindView(R.id.sp_break_tower_from)
//    Spinner mSpTowerFromTo;
//
//    @BindView(R.id.lv_tree_defect)
//    SwipeMenuListView mLvTreeDefect;
//
//
//    private AddImageGridView add_img_defect;
//    private EditText et_solve_defect_remark;
//
//    private Context mContext;
//    private BatchDefectAdapter batchDefectAdapter;
//    private List<TreeDefectPointBean> mTreeDefects;
//    private List<SpinnerOption> mSpTowerNo;
//    private List<Tower> mTowers;
//    private Call<BaseCallBack<String>> mSubmitCall;
//
//    private ProgressDialog mProgressDialog;
//    private GridViewAddImgesAdpter addGridViewAdapter;
//    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
//    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
//    private File tempFile;
//    private String IMAGE_DIR;
//    private final int MAX_IMAGE_LENGTH_ADD = 3;
//    private List<Map<String, Object>> datas;
//    private List<String> sysIdList = new ArrayList<>();
//    private String tower2;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_batch_defcet);
//        ButterKnife.bind(this);
//        mContext = this;
//
//
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        View mListViewFoot = LayoutInflater.from(this).inflate(R.layout.layout_defcet_foot, null);
//        add_img_defect = (AddImageGridView) mListViewFoot.findViewById(R.id.add_img_defect);
//        et_solve_defect_remark = (EditText) mListViewFoot.findViewById(R.id.et_solve_defect_remark);
//        mLvTreeDefect.addFooterView(mListViewFoot);
//        //设置线路名称
//        if (MyApplication.specialRegisteredStartTower != null && MyApplication.specialRegisteredEndTower != null){
//            String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.specialRegisteredStartTower.getSysGridLineId() + "");
//            mTvLineName.setText(lineName);
//            mTowers = TowerDBHelper.getInstance().getLineTowers(MyApplication.specialRegisteredStartTower.getSysGridLineId());
//        }else if (MyApplication.isRegisterAuto == true) {
//            if (MyApplication.currentNearestTower != null) {
//                String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.currentNearestTower.getSysGridLineId() + "");
//                mTvLineName.setText(lineName);
//                mTowers = TowerDBHelper.getInstance().getLineTowers(MyApplication.currentNearestTower.getSysGridLineId());
//            } else {
//                ToastUtil.show("请先进行到位登记");
//            }
//        } else {
//            if (MyApplication.registeredTower != null) {
//                String lineName = MyApplication.mLineIdNamePairs.get(MyApplication.registeredTower.getSysGridLineId() + "");
//                mTvLineName.setText(lineName);
//                mTowers = TowerDBHelper.getInstance().getLineTowers(MyApplication.registeredTower.getSysGridLineId());
//            } else {
//                ToastUtil.show("请先进行到位登记");
//            }
//        }
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setMessage("正在提交...");
//        mProgressDialog.setCanceledOnTouchOutside(false);
//
//        mSpTowerFromTo.setOnItemSelectedListener(this);
//        mLvTreeDefect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_batch);
//                if (!checkBox.isChecked()){
//                    sysIdList.add(mTreeDefects.get(position).getSysTreeDefectPointID()+"");
//                    mTreeDefects.get(position).setSelect(true);
//                    removeDuplicate(sysIdList);
//                }else{
//                    mTreeDefects.get(position).setSelect(false);
//                    sysIdList.remove(mTreeDefects.get(position).getSysTreeDefectPointID()+"");
//                }
////                checkBox.setChecked(!checkBox.isChecked());
//                checkBox.toggle();
//            }
//        });
//    }
//
//    public void removeDuplicate(List list)  {
//        HashSet h = new  HashSet(list);
//        list.clear();
//        list.addAll(h);
//    }
//
//    private void initData() {
//        mTreeDefects = new ArrayList<>();
//        batchDefectAdapter = new BatchDefectAdapter(mTreeDefects, mContext, R.layout.item_batch_defcet);
//        initListViewCreater();
//        mLvTreeDefect.setAdapter(batchDefectAdapter);
//
//        mSpTowerNo = new ArrayList<>();
//        mSpTowerNo.add(new SpinnerOption("", "请选择杆塔区间"));
//        if (mTowers == null) {
//            return;
//        }
//
//        if (MyApplication.isRegisterAuto == true) {
//            if (MyApplication.currentNearestTower != null) {
//                for (int i = 0; i < mTowers.size() - 1; i++) {
//                    Tower from = mTowers.get(i);
//                    Tower to = mTowers.get(i + 1);
//                    if (from.equals(MyApplication.currentNearestTower)||to.equals(MyApplication.currentNearestTower))
//                        mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
//                }
//            }
//        } else {
//            if (MyApplication.registeredTower != null) {
//                for (int i = 0; i < mTowers.size() - 1; i++) {
//                    Tower from = mTowers.get(i);
//                    Tower to = mTowers.get(i + 1);
//                    if (from.equals(MyApplication.registeredTower)||to.equals(MyApplication.registeredTower))
//                        mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
//                }
//            }
//        }
//        //如果是特殊巡视 就特殊处理
//        if (MyApplication.specialRegisteredStartTower != null){
//            mSpTowerNo.clear();
//            mSpTowerNo.add(new SpinnerOption("", "请选择杆塔区间"));
//            for (int i = 0; i < mTowers.size() - 1; i++) {
//                Tower from = mTowers.get(i);
//                Tower to = mTowers.get(i + 1);
//                if (from.equals(MyApplication.specialRegisteredStartTower)||to.equals(MyApplication.specialRegisteredStartTower)){
//                    while(!from.equals(MyApplication.specialRegisteredEndTower)){
//                        if (i == mTowers.size()-2){
//                            break;
//                        }
//                        mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
//                        i = i+1;
//                        from = mTowers.get(i);
//                        to = mTowers.get(i + 1);
//                    }
//                    mSpTowerNo.add(new SpinnerOption(from.getSysTowerID() + "", from.getTowerNo() + " - " + to.getTowerNo()));
//                }
//            }
//        }
//
//
//        ArrayAdapter<SpinnerOption> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, mSpTowerNo);
//        mSpTowerFromTo.setAdapter(adapter);
//
//        datas = new ArrayList();
//        addGridViewAdapter = new GridViewAddImgesAdpter(datas, this);
//        addGridViewAdapter.setMaxImages(MAX_IMAGE_LENGTH_ADD);
//        add_img_defect.setAdapter(addGridViewAdapter);
//        add_img_defect.getCodeResult(new AddImageGridView.AddCallBack() {
//            @Override
//            public void onClickButton(int code) {
//                switch (code) {
//                    case 1:
//                        camera();
//                        break;
//                    case 2:
//                        gallery();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mTreeDefects = TreeDefectDBHelper.getInstance().getTreeDefectPointBy2(mTvLineName.getText().toString(), tower2);
//        batchDefectAdapter.onDataChange(mTreeDefects);
//    }
//
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        sysIdList.clear();
//        tower2 = mSpTowerNo.get(position).getText();
//        if (mTreeDefects.size()>0){
//            mLvTreeDefect.removeAllViewsInLayout();
//            //mLvTreeDefect.removeAllViews();
//        }
//        mTreeDefects = TreeDefectDBHelper.getInstance().getTreeDefectPointBy2(mTvLineName.getText().toString(), tower2);
//        batchDefectAdapter.onDataChange(mTreeDefects);
//        for (TreeDefectPointBean treeDefectPointBean : mTreeDefects){
//            sysIdList.add(treeDefectPointBean.getSysTreeDefectPointID()+"");
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//
//    private void initListViewCreater() {
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//                SwipeMenuItem uploadItem = new SwipeMenuItem(
//                        mContext);
//                uploadItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0xcb, 0x5b)));
//                uploadItem.setWidth(UIUtils.dp2px(90));
//                uploadItem.setTitle("消缺");
//                uploadItem.setTitleSize(18);
//                uploadItem.setTitleColor(Color.WHITE);
//                menu.addMenuItem(uploadItem);
//            }
//        };
//        mLvTreeDefect.setMenuCreator(creator);
//        mLvTreeDefect.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        Intent intent = new Intent(mContext, SolveActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("channelDefectBean", mTreeDefects.get(position));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtras(bundle);
//                        mContext.startActivity(intent);
//                        break;
//
//                }
//                return false;
//            }
//        });
//    }
//
//
//    @OnClick({R.id.btn_submit, R.id.iv_back})
//    public void sumbit(View view) {
//        switch (view.getId()) {
//            case R.id.btn_submit:
//                doSumbit();
//                break;
//            case R.id.iv_back:
//                finish();
//                break;
//        }
//    }
//
//    private void doSumbit() {
//
//        if (et_solve_defect_remark.getText().toString().trim().isEmpty()) {
//            ToastUtil.show("请先填写消缺备注");
//            return;
//        }
//        if (datas == null || datas.size() == 0) {
//            ToastUtil.show("请先进行拍照");
//            return;
//        }
//        final DefectDeleteRecord record = create();
//        if (record == null||record.getSysDefectId().isEmpty()) {
//            ToastUtil.show("此区间暂无可以消除的树障缺陷");
//            return;
//        }
//        Map<String, RequestBody> params = initParams();
//        List<ImagePaths> imagePath = new ArrayList<>();
//
//        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(BatchDefcetActivity.this);
//        int result = DefectDeleteDBHelper.getInstance().insert(record);
//
//        if (result == -1) {
//            normalDialog.setTitle("提示");
//            normalDialog.setMessage("已经提交过消缺请求，请勿重复提交。");
//            normalDialog.setCancelable(true);
//            normalDialog.setPositiveButton("取消",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            BatchDefcetActivity.this.finish();
//                        }
//                    });
//            normalDialog.show();
//
//            try {
//                Looper.loop();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        mProgressDialog.show();
//
//        for (int i = 0; i < datas.size(); i++) {
//            ImagePaths imagePaths = new ImagePaths();
//            imagePaths.setCategory(14);
//            imagePaths.setLocalId(StringUtils.listToString(sysIdList, ","));
//            imagePaths.setFatherPlatformId(StringUtils.listToString(sysIdList, ","));
//            imagePaths.setPath((String) datas.get(i).get("path"));
//            imagePath.add(imagePaths);
//        }
//
//        for (String id : sysIdList){
//            TreeDefectDBHelper.getInstance().deleteTreePointDefectById(id);
//        }
//
//        SaveImagePathsAsync saveImagePathsAsync = new SaveImagePathsAsync();
//        saveImagePathsAsync.execute(imagePath);
//        List<MultipartBody.Part> requestImgParts = new ArrayList<>();
//
//        for (int i = 0; i < datas.size(); i++) {
//            File file = new File((String) datas.get(i).get("path"));
//            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
//            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CheckedPhoto" + i, file.getName(), imgFile);
//            requestImgParts.add(requestImgPart);
//        }
//        mSubmitCall = RetrofitManager.getInstance().getService(ApiService.class).postClearTreeDefect(params, requestImgParts);
//        mSubmitCall.enqueue(new Callback<BaseCallBack<String>>() {
//            @Override
//            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
//                BaseCallBack<String> body = response.body();
//                if (body == null) {
//                    mProgressDialog.dismiss();
//                    return;
//                }
//
//                if (body != null && body.getCode() == 1) {
//                    ToastUtil.show("提交成功");
//                    AppConstant.isDisDefeect = true;
//                    record.setUploadFlag(1);
//                    DefectDeleteDBHelper.getInstance().update(record);
//
//                } else if (response.code() == 401) {
//                    ToastUtil.show("提交到数据库,请重新登陆");
//                }else{
//                    ToastUtil.show("异常状态:00");
//                }
//                mProgressDialog.dismiss();
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
//                ToastUtil.show("提交到数据库");
//                mProgressDialog.dismiss();
//                finish();
//            }
//        });
//    }
//
//    private Map<String,RequestBody> initParams() {
//        Map<String, RequestBody> params = new HashMap<>();
//        String id = StringUtils.listToString(sysIdList, ",");
//        RequestBody defectId = RequestBody.create(MediaType.parse("multipart/form-data"), id);
//        params.put("sysDefectId", defectId);
//
//        //remark
//        RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), et_solve_defect_remark.getText().toString().trim());
//        params.put("Remark", remark);
//
//        return params;
//    }
//
//    private DefectDeleteRecord create() {
//        DefectDeleteRecord record = new DefectDeleteRecord();
//        record.setCreateDate(DateUtil.format(new Date()));
//        String id = "";
//        //后台接受的是id之间以逗号隔开
//        id = StringUtils.listToString(sysIdList, ",");
//        record.setLocalDefectId(id);
//        record.setDefectCategory(1);
//        record.setSysDefectId(id);
//        record.setRemark(et_solve_defect_remark.getText().toString().trim());
//        record.setUploadFlag(0);
//        int sysWorkId = WorkSheetTaskDBHelper.getInstance().queryWorkSheetTaskId(id + "", 3);
//        if (sysWorkId != -1) {
//            record.setSysTaskId(sysWorkId);
//        }
//        return record;
//    }
//
//
//    /**
//     * 拍照
//     */
//    public void camera() {
//        // 判断存储卡是否可以用，可用进行存储
//        if (hasSdcard()) {
//            IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + "缺陷";
//            File dir = new File(IMAGE_DIR);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//            String fileName = IMAGE_DIR + File.separator +  System.currentTimeMillis() + ".jpg";
//            tempFile = new File(fileName);
//            //从文件中创建uri
//            Uri uri = Uri.fromFile(tempFile);
//            Intent intent = new Intent();
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
//        } else {
//            ToastUtil.show("未找到存储卡，无法拍照！");
//        }
//    }
//
//    /**
//     * 判断sdcard是否被挂载
//     */
//    public boolean hasSdcard() {
//        return Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED);
//    }
//
//    /**
//     * 从相册获取2
//     */
//    public void gallery() {
//        Intent intent = new Intent(this, PhotoSelectorActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        int nowNeed = addGridViewAdapter.getMaxImages() - addGridViewAdapter.getCount() + 1;
//        intent.putExtra("limit", nowNeed);//number是选择图片的数量
//        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case PHOTO_REQUEST_GALLERY:
//                    if (data != null) {
//                        List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
//                        //处理代码
//                        for (String path : paths) {
//                            photoPath(path);
//                        }
//                    }
//                    break;
//                case PHOTO_REQUEST_CAREMA:
//                    // 从相机返回的数据
//                    if (hasSdcard()) {
//                        if (tempFile != null) {
//                            photoPath(tempFile.getPath());
//                        } else {
//                            ToastUtil.show("相机异常请稍后再试！");
//                        }
//                    } else {
//                        ToastUtil.show("未找到存储卡，无法存储照片！");
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    /**
//     * 对于文件进行剪切。展示
//     *
//     * @param path
//     */
//    public void photoPath(String path) {
//        IMAGE_DIR = IOUtils.getRootStoragePath(this) + File.separator + "缺陷";
//        File dir = new File(IMAGE_DIR);
//        if (!dir.exists()) {
//            dir.mkdir();
//        }
//        String fileName = IMAGE_DIR + File.separator + System.currentTimeMillis() + ".jpg";
//        FileUtils.changeBitmapPath(path, fileName);
//        SendBroadcasd2MEDIA.sendBroadcasd(this, IMAGE_DIR);
//        Map<String, Object> map = new HashMap<>();
//        map.put("path", fileName);
//        datas.add(map);
//        addGridViewAdapter.notifyDataSetChanged();
//    }
//
//}
