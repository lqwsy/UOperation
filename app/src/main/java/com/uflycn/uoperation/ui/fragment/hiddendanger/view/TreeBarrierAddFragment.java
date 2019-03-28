package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TreeBarrierBean;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.fragment.hiddendanger.model.ProgressRequestBody;
import com.uflycn.uoperation.ui.fragment.hiddendanger.presenter.HiddenDangerPresenter;
import com.uflycn.uoperation.ui.fragment.hiddendanger.presenter.HiddenDangerPresenterImp;
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
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 新建树障fragment
 * 2019.3.21 lqw
 */
public class TreeBarrierAddFragment extends DemoBaseFragment implements HiddenDangerView.TreeBarrierAddView {


    @BindView(R.id.aigv_tb_add_img)
    AddImageGridView tb_add_img;
    @BindView(R.id.sp_tb_line_name)
    Spinner lineName;
    @BindView(R.id.et_tb_small_tower_distance)
    EditText smallTowerDistance;
    @BindView(R.id.et_tb_tree_type)
    EditText treeType;
    @BindView(R.id.et_tb_tree_num)
    EditText treeNum;
    @BindView(R.id.et_tb_temperature)
    EditText temperature;
    @BindView(R.id.et_tb_clearance_distance)
    EditText clearanceDistance;
    @BindView(R.id.et_tb_vertical_distance)
    EditText verticalDistane;
    @BindView(R.id.et_tb_horizontal_distance)
    EditText horizontalDistance;
    @BindView(R.id.et_tb_describe)
    EditText describe;
    @BindView(R.id.sp_tb_from)
    Spinner startTower;
    @BindView(R.id.sp_tb_to)
    Spinner endTower;

    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private List<Map<String, Object>> datas;
    private final int MAX_IMAGE_LENGTH = 3;//最多可选择图片数
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 相册
    private File tempFile;
    List<ProgressRequestBody> mProgressRequestBodyArr;
    private HiddenDangerPresenter presenter;
    private List<Tower> towerList;
    private List<SpinnerOption> towerIdList;
    private List<SpinnerOption> lineNameList;
    private ArrayAdapter<SpinnerOption> lineNameAdapter;
    ArrayAdapter<SpinnerOption> towerAdapter;
    private ProgressDialog mProgressDialog;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_hd_tb_add;
    }

    @Override
    protected void initView() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在添加...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        initLine();
        initSpinner();
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, getActivity());
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        tb_add_img.setAdapter(gridViewAddImgesAdpter);
        tb_add_img.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                switch (code) {
                    case 1:
                        tempFile = AddPhotoUtils.camera(TreeBarrierAddFragment.this);
                        break;
                    case 2:
                        AddPhotoUtils.gallery(TreeBarrierAddFragment.this, gridViewAddImgesAdpter);
                        break;
                    default:
                        break;
                }
            }
        });
        mProgressRequestBodyArr = new ArrayList<>();
        presenter = new HiddenDangerPresenterImp(this);

    }

    //初始化下拉框
    private void initSpinner() {
        if (lineNameList == null || lineNameList.size() <= 0 || towerIdList == null || towerIdList.size() <= 0) {
            return;
        }
        lineNameAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, lineNameList);
        towerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, towerIdList);
        startTower.setAdapter(towerAdapter);
        endTower.setAdapter(towerAdapter);
        lineName.setAdapter(lineNameAdapter);
        lineName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                towerListDataChange(lineNameList.get(position).getValue());
                towerAdapter.notifyDataSetChanged();
                startTower.setSelection(0);
                endTower.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    /**
     * 初始化线路数据
     */
    private void initLine() {
        if (MyApplication.gridlineTaskStatus == 0) {
            return;
        }
        if (MyApplication.mLineIdNamePairs == null && MyApplication.mLineIdNamePairs.size() <= 0) {
            return;
        }
        List<Map.Entry<String, String>> lineList = new ArrayList<>(MyApplication.mLineIdNamePairs.entrySet());
        if (lineList == null && lineList.size() <= 0) {
            return;
        }
        lineNameList = new ArrayList<>();
        initLineNameList(lineList);
        if (lineNameList == null && lineNameList.size() <= 0) {
            return;
        }
        //默认显示第一条线路的塔数据
        towerIdList = new ArrayList<>();
        towerListDataChange(lineNameList.get(0).getValue());
    }

    /**
     * 拍照或相册回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                        //处理代码
                        for (String path : paths) {
                            datas.add(AddPhotoUtils.photoPath(TreeBarrierAddFragment.this, path));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
                        }
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:
                    // 从相机返回的数据
                    if (AddPhotoUtils.hasSdcard()) {
                        if (tempFile != null) {
                            datas.add(AddPhotoUtils.photoPath(TreeBarrierAddFragment.this, tempFile.getPath()));
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

    @OnClick(R.id.btn_tb_commit)
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btn_tb_commit:
                submit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPostSuccess(TreeBarrierBean treeBarrierBean) {
        mProgressDialog.dismiss();
        ToastUtil.show("上传成功！");
        reset();
    }

    @Override
    public void onPostFail(String msg) {
        mProgressDialog.dismiss();
        ToastUtil.show(msg);
    }

    @Override
    public void onDestroy() {
        presenter.cancelAll();
        super.onDestroy();
    }

    /**
     * 提交数据
     */
    private void submit() {
        try {
            if (lineNameList == null || lineNameList.size() <= 0 || towerList == null || towerList.size() <= 0 || towerIdList == null || towerIdList.size() <= 0) {
                ToastUtil.show("请开启线路巡视或我的任务！");
                return;
            }
            SpinnerOption startSO = (SpinnerOption) startTower.getSelectedItem();
            SpinnerOption endSO = (SpinnerOption) endTower.getSelectedItem();
            if (Integer.parseInt(endSO.getText()) - Integer.parseInt(startSO.getText()) != 1) {
                ToastUtil.show("请选择正确相邻的杆塔！");
                return;
            }
            int towerA_ID = Integer.parseInt(startSO.getValue());
            int towerB_ID = Integer.parseInt(endSO.getValue());
            String towerANo = startSO.getText();
            String towerBNo = endSO.getText();


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
            String lineNames = ((SpinnerOption) lineName.getSelectedItem()).getText();
            String sDescribe = lineNames + "在" + towerANo + "-" + towerBNo + "杆段内，有" + sType + "数量" + sNum + "棵，距离小号塔" + sTowerDistance + "米，净空距离" + sClearanceDistance + "米";
            describe.setText(sDescribe);

            if (StringUtils.isDoubleNull(AppConstant.CURRENT_LOCATION.latitude) || StringUtils.isDoubleNull(AppConstant.CURRENT_LOCATION.longitude)) {
                ToastUtil.show("无法获取定位，添加失败！");
                return;
            }

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

            DialogUtils.showAlert(getActivity(), "确认添加？", "确认添加提交该树障？",
                    "确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressDialog.show();
                            presenter.createTreeBarrierDefect(treeBarrierBean, requestImgParts);
                        }
                    }, "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
        } catch (Exception e) {
            ToastUtil.show("数据错误，添加失败");
        }

    }

    /**
     * 选择线路更改，修改对应塔数据
     */
    private void towerListDataChange(String lineID) {
        towerList = MyApplication.mAllTowersInMap.get(lineID);
        if (towerList != null && towerList.size() > 0) {
            towerIdList.clear();
            for (int i = 0; i < towerList.size(); i++) {
                Tower tower = towerList.get(i);
                towerIdList.add(new SpinnerOption(tower.getSysTowerID() + "", (i + 1) + ""));
            }
        }
    }

    /**
     * 初始化线路名称下拉框数据
     */
    private void initLineNameList(List<Map.Entry<String, String>> lineList) {
        if (lineList == null || lineList.size() <= 0) {
            return;
        }
        lineNameList.clear();
        for (int i = 0; i < lineList.size(); i++) {
            Map.Entry<String, String> entry = lineList.get(i);
            lineNameList.add(new SpinnerOption(entry.getKey(), entry.getValue()));
        }
    }


    /**
     * 添加后重置输入框
     */
    private void reset() {
        startTower.setSelection(0);
        endTower.setSelection(0);
        smallTowerDistance.setText("");
        treeType.setText("");
        treeNum.setText("");
        temperature.setText("");
        clearanceDistance.setText("");
        verticalDistane.setText("");
        horizontalDistance.setText("");
        describe.setText("");
        datas.clear();
        gridViewAddImgesAdpter.notifyDataSetChanged();
    }

}
