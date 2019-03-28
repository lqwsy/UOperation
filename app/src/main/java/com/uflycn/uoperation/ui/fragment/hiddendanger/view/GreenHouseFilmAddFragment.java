package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.orhanobut.logger.Logger;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.base.DemoBaseFragment;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.Organizition;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.OrganizitionDbHelper;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.ui.adapter.SpinnerVcAdapter;
import com.uflycn.uoperation.ui.adapter.UspinnerAdapter;
import com.uflycn.uoperation.util.AddPhotoUtils;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.StringUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 新建棚膜fragment
 * 2019.3.21 lqw
 */
public class GreenHouseFilmAddFragment extends DemoBaseFragment {

    @BindView(R.id.add_img_break)
    AddImageGridView addImgBreak;//图片
    Unbinder unbinder;
    @BindView(R.id.sp_line_name)
    Spinner spLineName;//线路名称
    @BindView(R.id.sp_start_tower)
    Spinner spStartTower;//起始杆塔
    @BindView(R.id.sp_stop_tower)
    Spinner spStopTower;//终止杆塔
    @BindView(R.id.tv_find_time)
    TextView tvFindTime;//发现时间
    @BindView(R.id.sp_gh_class)
    Spinner spGhClass;//班组
    @BindView(R.id.sp_hidden_type)
    Spinner spHiddenType;//棚膜隐患类型
    @BindView(R.id.sp_hidden_level)
    Spinner spHiddenLevel;//棚膜隐患等级
    @BindView(R.id.et_hidden_name)
    EditText etHiddenName;//隐患名称
    @BindView(R.id.et_ad_regions)
    EditText etAdRegions;//行政区域
    @BindView(R.id.et_ground_distance)
    EditText etGroundDistance;//导线对地距离
    @BindView(R.id.et_find_person)
    EditText etFindPerson;//发现人
    @BindView(R.id.et_responsible)
    EditText etResponsible;//责任人
    @BindView(R.id.et_basic_information)
    EditText etBasicInformation;//基本情况
    @BindView(R.id.et_foreign_body_type)
    EditText etForeignBodyType;//异物种类
    @BindView(R.id.et_greenhouse_structure)
    EditText etGreenhouseStructure;//大棚结构
    @BindView(R.id.et_greenhouse_type)
    EditText etGreenhouseType;//大棚用途
    @BindView(R.id.et_binding_type)
    EditText etBindingType;//绑扎种类
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
    private List<Map<String, Object>> datas;
    private final int MAX_IMAGE_LENGTH = 3;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static finalsp_line_nametv_patrol_time
    private File tempFile;
    private List<SpinnerOption> spinnerOption;
    private ArrayAdapter<SpinnerOption> mLinerRegister;
    private Reference<Context> mContextReference;
    private String choose_LineID = "0";
    private UspinnerAdapter mTowerFromAdapter;//起始
    private UspinnerAdapter mTowerToAdapter;//终止
    private Date mCurrentDate;
    private String formatDate;
    private Tower startTower, endTower;
    private String mCurrentOrganizitionId, mCurrentItemDetail, mCurrentHiddenLevel;//当前的班组，隐患类型，隐患等级
    private ArrayAdapter<String> classAdapter, hiddenAdapter, HiddenLevelAdapter;//班组，隐患类型，隐患等级
    private SpinnerVcAdapter mHiddenTypeClassAdapter;//棚膜隐患类型
    private Reference<Context> mContextRef;
    List<MultipartBody.Part> requestImgParts = new ArrayList<>();


    @Override
    public int getLayoutID() {
        return R.layout.fragment_hd_gf_add;
    }

    @Override
    protected void initView() {
        mContextReference = new WeakReference<>(getContext());
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, getActivity());
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        addImgBreak.setAdapter(gridViewAddImgesAdpter);
        addImgBreak.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                switch (code) {
                    case 1:
                        tempFile = AddPhotoUtils.camera(GreenHouseFilmAddFragment.this);
                        break;
                    case 2:
                        AddPhotoUtils.gallery(GreenHouseFilmAddFragment.this, gridViewAddImgesAdpter);
                        break;
                    default:
                        break;
                }
            }
        });

        //<editor-fold desc="设置时间">
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mCurrentDate = calendar.getTime();
        formatDate = sf.format(mCurrentDate);
        //</editor-fold>

        setClass();//设置班组

        setHiddType();//设置隐患类型

        getLineName();//获取线路名

        getHiddenDangerLevel();//获取隐患等级

        setPicture();//设置图片

//        List<SpinnerOption> spinnerOptions = new ArrayList<>();

        //<editor-fold desc="起止杆塔">
        spStartTower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //</editor-fold>

        //<editor-fold desc="终止杆塔">
        spStopTower.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //</editor-fold>


    }

    private void setPicture() {
        //隐患图片
        for (int i = 0; i < datas.size(); i++) {
            File file = new File((String) datas.get(i).get("path"));
            RequestBody imgFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
            MultipartBody.Part requestImgPart = MultipartBody.Part.createFormData("CheckedPhoto" + i, file.getName(), imgFile);
            requestImgParts.add(requestImgPart);
        }
    }

    private void getHiddenDangerLevel() {
        final List<ItemDetail> itemDetails = ItemDetailDBHelper.getInstance().getItem("棚膜隐患等级");
        //设置spinner棚膜等级
        final List<String> hiddenitems = new ArrayList<>();
        for (ItemDetail itemDetail : itemDetails) {
            hiddenitems.add(itemDetail.getItemsName());
        }
        HiddenLevelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, hiddenitems);
        spHiddenLevel.setAdapter(HiddenLevelAdapter);
        spHiddenLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //更新当前的隐患类型id
                if (position == 0) {
                    mCurrentHiddenLevel = "";
                } else {
                    String hiddenType = itemDetails.get(position - 1).getItemDetailsId();
                    mCurrentHiddenLevel = hiddenType;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCurrentHiddenLevel = "";
    }

    private void getLineName() {
        //<editor-fold desc="获取线路名">
        spinnerOption = new ArrayList<>();
        for (Map.Entry entry : MyApplication.gridlines.entrySet()) {
            spinnerOption.add(new SpinnerOption(((Gridline) entry.getValue()).getSysGridLineID() + "", ((Gridline) entry.getValue()).getLineName()));
        }
        for (Map.Entry entry : MyApplication.mTempLineMap.entrySet()) {
            if (!MyApplication.gridlines.containsKey(entry.getKey())) {
                spinnerOption.add(new SpinnerOption(((Gridline) entry.getValue()).getSysGridLineID() + "", ((Gridline) entry.getValue()).getLineName()));
            }
        }
        //simple_spinner_dropdown_item
        mLinerRegister = new ArrayAdapter<>(mContextReference.get(), android.R.layout.simple_spinner_dropdown_item, spinnerOption);
        spLineName.setAdapter(mLinerRegister);
        spLineName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_LineID = mLinerRegister.getItem(position).getValue();
                initTowerSpinner(choose_LineID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //</editor-fold>
    }

    private void setHiddType() {
        final List<ItemDetail> itemDetails = ItemDetailDBHelper.getInstance().getItem("棚膜隐患类型");
        //设置spinner棚膜类型
        final List<String> hiddenitems = new ArrayList<>();
//        hiddenitems.add("棚膜隐患类型");
        for (ItemDetail itemDetail : itemDetails) {
            hiddenitems.add(itemDetail.getItemsName());
        }
        hiddenAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, hiddenitems);
        spHiddenType.setAdapter(hiddenAdapter);
        spHiddenType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //更新当前的隐患类型id
                if (position == 0) {
                    mCurrentItemDetail = "";
                } else {
                    String hiddenType = itemDetails.get(position - 1).getItemDetailsId();
                    mCurrentItemDetail = hiddenType;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCurrentItemDetail = "";
//        mHiddenTypeClassAdapter = new SpinnerVcAdapter(itemDetails, mContextRef.get());
//        spHiddenType.setAdapter(mHiddenTypeClassAdapter);

//        int position=itemDetails.indexOf(ItemDetailDBHelper.getInstance().getItemDetailByItemsName())

    }

    private void setClass() {
        //获取所有的班组，并过滤掉没有人员的班组
        final List<Organizition> teamList = OrganizitionDbHelper.getInstance().findAllTeam();
        //设置spinner班组
        final List<String> items = new ArrayList<>();
//        items.add("所有班组");
        for (Organizition organizition : teamList) {
            items.add(organizition.getFullName());
        }
        classAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
        spGhClass.setAdapter(classAdapter);
        spGhClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //更新当前的班组id
                if (position == 0) {
                    mCurrentOrganizitionId = "";
                } else {
                    String organizationId = teamList.get(position - 1).getOrganizationId();
                    mCurrentOrganizitionId = organizationId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mCurrentOrganizitionId = "";

    }

    @OnClick({R.id.tv_find_time, R.id.bt_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_find_time:
                tvFindTime.setText(formatDate);
                showTime("发现时间", tvFindTime);
                break;
            case R.id.bt_submit:
                submit();
                break;
            default:
                break;
        }
    }

    private void submit() {
        if (spLineName.getSelectedItemPosition() < 0) {
            ToastUtil.show("没有选择线路名称");
            return;
        } else if (StringUtils.isEmptyOrNull(tvFindTime.getText().toString())) {
            ToastUtil.show("没有选择时间");
        }
        String gridLineID = spinnerOption.get(spLineName.getSelectedItemPosition()).getValue();
//        startTower = (Tower) mTowerFromAdapter.getItem(spStartTower.getSelectedItemPosition());
        String getStartTower = ((Tower) mTowerFromAdapter.getItem(spStartTower.getSelectedItemPosition())).getTowerNo().toString();
        String getStopTower = ((Tower) mTowerToAdapter.getItem(spStopTower.getSelectedItemPosition())).getTowerNo().toString();
        String getClass = classAdapter.getItem(spGhClass.getSelectedItemPosition()).toString();
        String getHiddenType = hiddenAdapter.getItem(spHiddenType.getSelectedItemPosition()).toString();
        String getHiddenLevel = HiddenLevelAdapter.getItem(spHiddenLevel.getSelectedItemPosition()).toString();

        endTower = (Tower) mTowerToAdapter.getItem(spStopTower.getSelectedItemPosition());
        String findTime = tvFindTime.getText().toString();
        String HiddenName=etHiddenName.getText().toString();
        if (StringUtils.isEmptyOrNull(findTime)) {
            ToastUtil.show("没有发现时间");
            return;
        }
        if (datas == null || datas.size() == 0) {
            ToastUtil.show("请先就进行拍照");
            return;
        }
        if (StringUtils.isEmptyOrNull(HiddenName)){
            ToastUtil.show("没有输入隐患名称");
            return;
        }
//        if (spGhClass.getSelectedItemPosition() < 1) {
//            ToastUtil.show("没有选择班组");
//        }
        Logger.i("线路id:" + gridLineID + "，起始杆塔:" + getStartTower + "，终止杆塔:" + getStopTower +
                "，发现时间:" + findTime + "，班组:" + getClass + "，隐患类型:" + getHiddenType +
                "，隐患等级:" + getHiddenLevel);

    }

    //<editor-fold desc="起止终止杆塔编号数据获取">
    private void initTowerSpinner(String choose_lineID) {
        List<Tower> towerList = new ArrayList<>();
        caculateNearestDistance(GridLineDBHelper.getInstance().getLine(Long.parseLong(choose_lineID)));
        //        towerList.addAll(TowerDBHelper.getInstance().getLineTowers(Integer.parseInt(LineID)));
        if (MyApplication.mAllTowersInMap.get(choose_lineID) != null) {
            towerList.addAll(MyApplication.mAllTowersInMap.get(choose_lineID));
        } else if (MyApplication.mTempTowerMap.get(Integer.parseInt(choose_lineID)) != null) {
            towerList.addAll(MyApplication.mTempTowerMap.get(Integer.parseInt(choose_lineID)));
        }

        if (mTowerFromAdapter == null) {
            mTowerFromAdapter = new UspinnerAdapter(towerList, mContextReference.get());
            spStartTower.setAdapter(mTowerFromAdapter);
        } else {
            mTowerFromAdapter.onDataChange(towerList);
        }
        if (mTowerToAdapter == null) {
            mTowerToAdapter = new UspinnerAdapter(towerList, mContextReference.get());
            spStopTower.setAdapter(mTowerToAdapter);
        } else {
            mTowerToAdapter.onDataChange(towerList);
        }

    }

    private void caculateNearestDistance(Gridline line) {

    }
    //</editor-fold>

    private void showTime(String title, final TextView getTextView) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mCurrentDate = date;
                getTextView.setText(DateUtil.format(date, DateUtil.PATTERN_CLASSICAL_SIMPLE));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(20)
                .setTitleSize(20)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setRangDate(startDate, endDate)
                .setDate(Calendar.getInstance())
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }
//    private void initWorkType(Tower tower) {
//        List<TypeOfWork> typeOfWorks = MyApplication.mWorkTypeMap.get(tower.getSysTowerID().intValue());
//        List<SpinnerOption> list = new ArrayList<>();
//        for (int i = 0; i < typeOfWorks.size(); i++) {
//            SpinnerOption spinnerOption = new SpinnerOption(typeOfWorks.get(i).getTypeOfWork(), typeOfWorks.get(i).getTypeOfWorkString());
//            list.add(spinnerOption);
//        }
//
//    }

    @Override
    protected void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                        //处理代码
                        for (String path : paths) {
                            datas.add(AddPhotoUtils.photoPath(GreenHouseFilmAddFragment.this, path));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
                        }
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:
                    // 从相机返回的数据
                    if (AddPhotoUtils.hasSdcard()) {
                        if (tempFile != null) {
                            datas.add(AddPhotoUtils.photoPath(GreenHouseFilmAddFragment.this, tempFile.getPath()));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
