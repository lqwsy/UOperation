package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.TreeBarrierBean;
import com.uflycn.uoperation.bean.TreeDefectPointBean;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.fragment.hiddendanger.model.ProgressRequestBody;
import com.uflycn.uoperation.ui.fragment.hiddendanger.presenter.HiddenDangerPresenter;
import com.uflycn.uoperation.util.AddPhotoUtils;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
/**
 * 更新树障
 * */
public class TreeBarrierUpdateActivity extends Activity {

    @BindView(R.id.aigv_tb_update_add_img)
    AddImageGridView tb_add_img;
    @BindView(R.id.et_tb_update_line_name)
    EditText lineName;
    @BindView(R.id.et_tb_update_small_tower_distance)
    EditText smallTowerDistance;
    @BindView(R.id.et_tb_update_tree_type)
    EditText treeType;
    @BindView(R.id.et_tb_update_tree_num)
    EditText treeNum;
    @BindView(R.id.et_tb_update_temperature)
    EditText temperature;
    @BindView(R.id.et_tb_update_clearance_distance)
    EditText clearanceDistance;
    @BindView(R.id.et_tb_update_vertical_distance)
    EditText verticalDistane;
    @BindView(R.id.et_tb_update_horizontal_distance)
    EditText horizontalDistance;
    @BindView(R.id.et_tb_update_describe)
    EditText describe;
    @BindView(R.id.et_tb_update_tower_from)
    EditText startTower;
    @BindView(R.id.et_tb_update_tower_to)
    EditText endTower;
    @BindView(R.id.iv_tb_update_back)
    ImageView back;

    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private List<Map<String, Object>> datas;
    private final int MAX_IMAGE_LENGTH = 3;//最多可选择图片数
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 相册
    private File tempFile;
    List<ProgressRequestBody> mProgressRequestBodyArr;
    private HiddenDangerPresenter presenter;
    private TreeDefectPointBean treeDefectPointBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏,无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tree_barrier_update);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        //从intent读取数据，没有数据则不更新
        treeDefectPointBean = (TreeDefectPointBean) getIntent().getExtras().getSerializable("treeDefectPointBean");
        if (treeDefectPointBean == null) {
            ToastUtil.show("读取数据失败！");
            return;
        }

        //初始化
        lineName.setText(treeDefectPointBean.getLineName());
        startTower.setText(treeDefectPointBean.getTowerA_Name());
        endTower.setText(treeDefectPointBean.getTowerB_Name());
        smallTowerDistance.setText(treeDefectPointBean.getDistanceFromTower()+"");
        treeType.setText(treeDefectPointBean.getTreeSeed());
        treeNum.setText(treeDefectPointBean.getTreeSeedNumber());
        temperature.setText(""+treeDefectPointBean.getTemperature());
        clearanceDistance.setText(treeDefectPointBean.getDistanceFromLine()+"");
        verticalDistane.setText(treeDefectPointBean.getDistanceFromLineV()+"");
        horizontalDistance.setText(treeDefectPointBean.getDistanceFromLineH()+"");
        describe.setText(treeDefectPointBean.getRemark());

        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        tb_add_img.setAdapter(gridViewAddImgesAdpter);
        tb_add_img.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                switch (code) {
                    case 1:
                        tempFile = AddPhotoUtils.camera(TreeBarrierUpdateActivity.this);
                        break;
                    case 2:
                        AddPhotoUtils.gallery(TreeBarrierUpdateActivity.this, gridViewAddImgesAdpter);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                        //处理代码
                        for (String path : paths) {
                            datas.add(AddPhotoUtils.photoPath(this, path));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
                        }
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:
                    // 从相机返回的数据
                    if (AddPhotoUtils.hasSdcard()) {
                        if (tempFile != null) {
                            datas.add(AddPhotoUtils.photoPath(this, tempFile.getPath()));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
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

    @OnClick({R.id.btn_tb_update_commit,R.id.iv_tb_update_back})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_update_commit:
                submit();
                break;
            case R.id.iv_tb_update_back:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
        try {
            int towerA_ID = treeDefectPointBean.getTowerA_Id();
            int towerB_ID = treeDefectPointBean.getTowerB_Id();

            String sTowerDistance = smallTowerDistance.getText().toString().trim();
            if (StringUtils.isEmptyOrNull(sTowerDistance) || !StringUtils.isFloatString(sTowerDistance)) {
                ToastUtil.show("请输入正确的小号塔距离！");
                return;
            }
            String sType = treeType.getText().toString().trim();
            if (StringUtils.isEmptyOrNull(sType)) {
                ToastUtil.show("请输入树种名称！");
                return;
            }
            String sNum = treeNum.getText().toString().trim();
            if (StringUtils.isEmptyOrNull(sNum) || !StringUtils.isIntString(sNum)) {
                ToastUtil.show("请输入正确的数量整数！");
                return;
            }
            String sTemp = temperature.getText().toString().trim();
            if (StringUtils.isEmptyOrNull(sTemp) || !StringUtils.isFloatString(sTemp)) {
                ToastUtil.show("请输入正确的温度数值！");
                return;
            }
            String sClearanceDistance = clearanceDistance.getText().toString().trim();
            if (StringUtils.isEmptyOrNull(sClearanceDistance) || !StringUtils.isFloatString(sClearanceDistance)) {
                ToastUtil.show("请输入正确的净空距离！");
                return;
            }
            String sVerticalDistance = verticalDistane.getText().toString().trim();
            if (StringUtils.isEmptyOrNull(sVerticalDistance) || !StringUtils.isFloatString(sVerticalDistance)) {
                ToastUtil.show("请输入正确的垂直距离！");
                return;
            }
            String sHorizontalDistance = horizontalDistance.getText().toString().trim();
            if (StringUtils.isEmptyOrNull(sHorizontalDistance) || !StringUtils.isFloatString(sHorizontalDistance)) {
                ToastUtil.show("请输入正确的水平距离！");
                return;
            }
            String sDescribe = lineName.getText().toString().trim() + " " + sTowerDistance + " " + sType + " " + sNum + " " + sClearanceDistance;
            describe.setText(sDescribe);

            final TreeBarrierBean treeBarrierBean = new TreeBarrierBean();
            treeBarrierBean.setTowerA_ID(towerA_ID);
            treeBarrierBean.setTowerB_ID(towerB_ID);
            treeBarrierBean.setDefectLevel(1);
            treeBarrierBean.setLatitude((float) AppConstant.CURRENT_LOCATION.latitude);
            treeBarrierBean.setLongitude((float) AppConstant.CURRENT_LOCATION.longitude);
            treeBarrierBean.setDistanceFromTower(Float.valueOf(sTowerDistance));
            treeBarrierBean.setTemperature(Float.valueOf(sTemp));
            treeBarrierBean.setDistanceFromLine(Float.valueOf(sClearanceDistance));
            treeBarrierBean.setDistanceFromLineH(Float.valueOf(sHorizontalDistance));
            treeBarrierBean.setDistanceFromLineV(Float.valueOf(sVerticalDistance));
            treeBarrierBean.setTreeSeed(sType);
            treeBarrierBean.setTreeSeedNumber(sNum);

            //树障图片
            if (datas == null || datas.size() <= 0) {
                ToastUtil.show("请选择图片！");
                return;
            }

            final List<MultipartBody.Part> requestImgParts = new ArrayList<>();
            for (int i = 0; i < datas.size(); i++) {
                File file = new File((String) datas.get(i).get("path"));
                RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                ProgressRequestBody uploadBody = new ProgressRequestBody(imgFile);
                mProgressRequestBodyArr.add(uploadBody);
                MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("DefectPhoto" + i, file.getName(), uploadBody);
                requestImgParts.add(requestImgPart);
            }

            DialogUtils.showAlert(this, "确认添加？", "确认添加提交该树障？",
                    "确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //更新接口
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
        } catch (Exception e) {
            Log.d("lqwtest", "TreeBarrierAddFragment submit error = ", e);
            ToastUtil.show("数据错误，添加失败");
        }

    }

}
