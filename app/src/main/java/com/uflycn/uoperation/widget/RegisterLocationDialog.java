package com.uflycn.uoperation.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.InPlace;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.bean.Tower;
import com.uflycn.uoperation.bean.TypeOfWork;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.BaseMainThreadEvent;
import com.uflycn.uoperation.eventbus.CloseDialogEvent;
import com.uflycn.uoperation.eventbus.RegisterLocationEvent;
import com.uflycn.uoperation.eventbus.UpdateTowerEvent;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.InPlaceDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.greendao.TowerDBHelper;
import com.uflycn.uoperation.ui.adapter.UspinnerAdapter;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.ProjectManageUtil;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.UIUtils;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;
import com.xflyer.utils.LatLngUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by diyang on 2017/9/23.
 */
public class RegisterLocationDialog extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private Reference<Context> mContextReference;
    private View mContentView;
    private RadioGroup mInspectType;
    private LinearLayout mInspectSection;//巡检区段
    private LinearLayout mLyTowerNo;
    private Spinner mSpinnerInspectFrom;//起止杆塔
    private Spinner mSpinnerInspectTo;//终止杆塔
    private Spinner mSpinnerTowerRegister;//杆塔编号
    private Spinner mSpinnerLineRegister;
    private UspinnerAdapter mTowerRegister;
    private ArrayAdapter<SpinnerOption> mLinerRegister;
    private List<SpinnerOption> spinnerOption;
    private UspinnerAdapter mTowerFromAdapter;
    private UspinnerAdapter mTowerToAdapter;
    private String mCurrentType;
    private String choose_LineID = "0";
    private int mRgChoose = 2;//1特殊巡视，2其他巡视
    private LinearLayout mLlWorkProject;
    private RelativeLayout mRlPatrol;

    private Spinner mSpWorkproject;//作业项目

    public RegisterLocationDialog(Context context) {
        this(context, R.style.Dialog);
    }

    public RegisterLocationDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContextReference = new WeakReference<>(context);
        initViews();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {
        MyApplication.specialRegisteredStartTower = null;
        MyApplication.specialRegisteredEndTower = null;
        if (AppConstant.isChooseRegister) {
            mContentView = LayoutInflater.from(mContextReference.get()).inflate(R.layout.dialog_register_loaction, null);
            mSpinnerTowerRegister = (Spinner) mContentView.findViewById(R.id.sp_tower_register);//杆塔编号
            mSpinnerLineRegister = (Spinner) mContentView.findViewById(R.id.sp_Line_register);
            initSpinnerLineRegister();
            if (ProjectManageUtil.isShanDong()) {
                initShanDong();
            }
        } else {
            mContentView = LayoutInflater.from(mContextReference.get()).inflate(R.layout.dialog_register_loaction_two, null);
        }


        mLyTowerNo = (LinearLayout) mContentView.findViewById(R.id.layout_tower_no);
        Button btnCloseDialog = (Button) mContentView.findViewById(R.id.btn_close_dialog);
        Button btnSubMit = (Button) mContentView.findViewById(R.id.btn_reg_loaction);
        btnCloseDialog.setOnClickListener(this);
        btnSubMit.setOnClickListener(this);
        mInspectSection = (LinearLayout) mContentView.findViewById(R.id.llayout_tower_from_to);
        mSpinnerInspectFrom = (Spinner) mContentView.findViewById(R.id.sp_tower_from);//起止杆塔
        mSpinnerInspectFrom.setDropDownVerticalOffset(UIUtils.dp2px(30));
        mSpinnerInspectTo = (Spinner) mContentView.findViewById(R.id.sp_tower_to);//终止杆塔
        mInspectType = (RadioGroup) mContentView.findViewById(R.id.radiogroup);
        mInspectType.setOnCheckedChangeListener(this);

        if (AppConstant.LAST_INSPECTION_TYPE_ID != 0) {
            mInspectType.check(AppConstant.LAST_INSPECTION_TYPE_ID);
        }

        if (!AppConstant.isChooseRegister) {
            //如果不需要  直接开启
            initSpinnerSection();
        }
        setContentView(mContentView);
    }

    private void initShanDong() {
        if (MyApplication.gridlineTaskStatus != 3) {
            return;
        }
        mLlWorkProject = (LinearLayout) mContentView.findViewById(R.id.ll_work_project);
        mSpWorkproject = (Spinner) mContentView.findViewById(R.id.sp_work_project);//作业项目
        mRlPatrol = (RelativeLayout) mContentView.findViewById(R.id.rl_patrol);
        mRlPatrol.setVisibility(View.GONE);
        mLlWorkProject.setVisibility(View.VISIBLE);

    }


    private synchronized void initSpinnerLineRegister() {
        spinnerOption = new ArrayList<>();
        for (Map.Entry entry : MyApplication.gridlines.entrySet()) {
            spinnerOption.add(new SpinnerOption(((Gridline) entry.getValue()).getSysGridLineID() + "", ((Gridline) entry.getValue()).getLineName()));
        }
        for (Map.Entry entry : MyApplication.mTempLineMap.entrySet()) {
            if (!MyApplication.gridlines.containsKey(entry.getKey())) {
                spinnerOption.add(new SpinnerOption(((Gridline) entry.getValue()).getSysGridLineID() + "", ((Gridline) entry.getValue()).getLineName()));
            }
        }
        mLinerRegister = new ArrayAdapter<>(mContextReference.get(), android.R.layout.simple_spinner_dropdown_item, spinnerOption);
        mSpinnerLineRegister.setAdapter(mLinerRegister);
        mSpinnerLineRegister.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_LineID = mLinerRegister.getItem(position).getValue();
                updateWorkType();
                initTowerSpinner(choose_LineID, mRgChoose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSpinnerTowerRegister.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initWorkType((Tower) mSpinnerTowerRegister.getSelectedItem());//获取杆塔编号
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (MyApplication.registeredTower != null) {
            for (int i = 0; i < spinnerOption.size(); i++) {
                if (spinnerOption.get(i).getValue().equals(MyApplication.registeredTower.getSysGridLineId() + "")) {
                    mSpinnerLineRegister.setSelection(i);
                }
            }
        }
    }

    private void updateWorkType() {
        if (!ProjectManageUtil.isShanDong() || MyApplication.gridlineTaskStatus != 3) {
            return;
        }
        mSpWorkproject.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_plan_record_register, new ArrayList<>()));//作业项目
    }

    private void initWorkType(Tower tower) {
        if (!ProjectManageUtil.isShanDong() || MyApplication.gridlineTaskStatus != 3) {
            return;
        }
        List<TypeOfWork> typeOfWorks = MyApplication.mWorkTypeMap.get(tower.getSysTowerID().intValue());
        //        String workTypes = MyApplication.mWorkTypeMap.get(Integer.parseInt(choose_lineID));
        //        String[] w = workTypes.split(",");
        List<SpinnerOption> list = new ArrayList<>();
        for (int i = 0; i < typeOfWorks.size(); i++) {
            SpinnerOption spinnerOption = new SpinnerOption(typeOfWorks.get(i).getTypeOfWork(), typeOfWorks.get(i).getTypeOfWorkString());
            list.add(spinnerOption);
        }
        mSpWorkproject.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_plan_record_register, list));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close_dialog:
                dismiss();
                break;
            case R.id.btn_reg_loaction:
                regiterLoaction();
                break;
            default:
                break;
        }
    }

    private void regiterLoaction() {

        if (AppConstant.CURRENT_LOCATION == null || MyApplication.currentNearestTower == null) {
            ToastUtil.show("定位失败或者没有开启巡视任务");
            return;
        }
        if (AppConstant.currentUser == null) {
            ToastUtil.show("当前用户错误");
            return;
        }
        Tower from = MyApplication.currentNearestTower;
        Tower to = null;
        if (mInspectType.getCheckedRadioButtonId() == R.id.rb_state_tour) {
            mCurrentType = ItemDetailDBHelper.getInstance().getTourItemDetail("状态巡视").getItemDetailsId();
            if (AppConstant.isChooseRegister) {
                from = isChooseTower(from);
            } else {
                MyApplication.registeredTower = from;
            }
        } else if (mInspectType.getCheckedRadioButtonId() == R.id.rb_special_tour) {//特殊巡视
            from = (Tower) mTowerFromAdapter.getItem(mSpinnerInspectFrom.getSelectedItemPosition());
            to = (Tower) mTowerToAdapter.getItem(mSpinnerInspectTo.getSelectedItemPosition());
            if (from == null || to == null) {
                ToastUtil.show("到位登记失败");
                return;
            }
            try {
                if (Integer.valueOf(from.getTowerNo()) < Integer.valueOf(to.getTowerNo())) {
                    MyApplication.specialRegisteredStartTower = from;
                    MyApplication.specialRegisteredEndTower = to;
                } else {
                    MyApplication.specialRegisteredStartTower = to;
                    MyApplication.specialRegisteredEndTower = from;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                MyApplication.specialRegisteredStartTower = from;
                MyApplication.specialRegisteredEndTower = to;
            }
            mCurrentType = ItemDetailDBHelper.getInstance().getTourItemDetail("特殊巡视").getItemDetailsId();
        } else if (mInspectType.getCheckedRadioButtonId() == R.id.rb_falut_tour) {
            //            mCurrentType = ItemDetailDBHelper.getInstance().getItemDetailByItemsName("故障巡视").getItemDetailsId();
            mCurrentType = ItemDetailDBHelper.getInstance().getTourItemDetail("故障巡视").getItemDetailsId();
            if (AppConstant.isChooseRegister) {
                from = isChooseTower(from);
            } else {
                MyApplication.registeredTower = from;
            }
        } else if (mInspectType.getCheckedRadioButtonId() == R.id.rb_defect_tour) {
            ItemDetail itemDetail = ItemDetailDBHelper.getInstance().getTourItemDetail("消缺");
            if (itemDetail == null) {
                ToastUtil.show("暂未开启此功能");
                return;
            }
            mCurrentType = itemDetail.getItemDetailsId();
            if (AppConstant.isChooseRegister) {
                from = isChooseTower(from);
            } else {
                MyApplication.registeredTower = from;
            }
        } else if (mInspectType.getCheckedRadioButtonId() == R.id.rb_check_tour) {
            ItemDetail itemDetail = ItemDetailDBHelper.getInstance().getTourItemDetail("检测");
            if (itemDetail == null) {
                ToastUtil.show("暂未开启此功能");
                return;
            }
            mCurrentType = itemDetail.getItemDetailsId();
            if (AppConstant.isChooseRegister) {
                from = isChooseTower(from);
            } else {
                MyApplication.registeredTower = from;
            }
        }
        if (AppConstant.LAST_INSPECTION_TYPE_ID != mInspectType.getCheckedRadioButtonId()) {
            AppConstant.LAST_INSPECTION_TYPE_ID = mInspectType.getCheckedRadioButtonId();
        }
        if (from == null) {
            ToastUtil.show("到位登记失败");
            return;
        }
        if (ProjectManageUtil.isShanDong() && MyApplication.gridlineTaskStatus == 3) {
            if (mSpWorkproject.getSelectedItem() == null) {
                ToastUtil.show("请先选择工作项目");
                return;
            }
        }
        InPlace inPlace = createInplace(from, to);//杆塔和
        EventBus.getDefault().post(new RegisterLocationEvent(inPlace));//发送请求
        saveInplace(inPlace);//数据库保存到位登记
    }

    private Tower isChooseTower(Tower from) {
        //如果用户自己选择了塔 那么这里将from赋值为用户选择的塔
        if (mSpinnerLineRegister.getSelectedItemPosition() < 0) {
            ToastUtil.show("没有选择任何塔");
            return null;
        }
        if (mSpinnerTowerRegister.getSelectedItemPosition() != 0) {
            from = (Tower) mTowerRegister.getItem(mSpinnerTowerRegister.getSelectedItemPosition());
            MyApplication.isRegisterAuto = false;
        } else {
            //            MyApplication.currentNearestTower


            String gridLineID = spinnerOption.get(mSpinnerLineRegister.getSelectedItemPosition()).getValue();
            caculateNearestDistance(GridLineDBHelper.getInstance().getLine(Long.parseLong(gridLineID)));
            from = TowerDBHelper.getInstance().getTower(MyApplication.currentNearestTower.getSysTowerID());
            MyApplication.registeredTower = from;
        }
        return from;
    }

    private InPlace createInplace(Tower from, Tower to) {
        InPlace inPlace = new InPlace();
        double lat = AppConstant.CURRENT_LOCATION.latitude;
        double lng = AppConstant.CURRENT_LOCATION.longitude;
        double altitude = AppConstant.CURRENT_LOCATION.altitude;
        LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(lat, lng);
        inPlace.setPatrolType(mCurrentType);//巡视种类
        if (AppConstant.currentUser != null) {
            inPlace.setSysUserId(AppConstant.currentUser.getUserId());//用户id
        }

        inPlace.setSysTowerId(from.getSysTowerID().intValue());//开始杆塔
        if (to != null) {//区段
            inPlace.setNearTowerId(to.getSysTowerID().intValue());//结束杆塔
        }
        inPlace.setLongitude(gps.longitude);//经度
        inPlace.setLatitude(gps.latitude);//维度
        inPlace.setAltitude(altitude);//高度
        inPlace.setCreateDate(DateUtil.format(new Date()));
        inPlace.setUploadFlag(0);
        if (MyApplication.gridlineTaskStatus == 2) {
            inPlace.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(from.getSysGridLineId()));
        } else if (MyApplication.gridlineTaskStatus == 3) {
            inPlace.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
            inPlace.setPatrolType(((SpinnerOption) mSpWorkproject.getSelectedItem()).getValue());
        }

        return inPlace;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTowerNum(BaseMainThreadEvent event) {
        if (event instanceof UpdateTowerEvent) {
            if (!AppConstant.isChooseRegister) {
                ((TextView) mContentView.findViewById(R.id.tv_tower_no)).setText("杆塔编号" + MyApplication.currentNearestTower.getTowerNo());
            }
            if (mTowerToAdapter != null && mTowerToAdapter.getCount() > 0) {
                Tower tower = (Tower) mTowerFromAdapter.getItem(0);
                if (tower.getSysGridLineId() != ((UpdateTowerEvent) event).getCurrentNearestTower().getSysGridLineId()) {//如果线路改变了
                    //加上就是死循环。能运行就别加了
                    //initList();
                }
            } else {
                //initList();
            }
        } else if (event instanceof CloseDialogEvent) {
            dismiss();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_special_tour://特殊巡视
                mInspectSection.setVisibility(View.VISIBLE);
                if (AppConstant.isChooseRegister) {
                    mSpinnerTowerRegister.setVisibility(View.GONE);
                }
                mRgChoose = 1;
                mLyTowerNo.setVisibility(View.GONE);
                initList();
                break;
            default:
                mInspectSection.setVisibility(View.GONE);
                if (AppConstant.isChooseRegister) {
                    mSpinnerTowerRegister.setVisibility(View.VISIBLE);
                }
                mLyTowerNo.setVisibility(View.VISIBLE);
                mRgChoose = 2;
                initList();
                break;
        }
    }

    private void initList() {
        if (AppConstant.isChooseRegister) {
            initTowerSpinner(choose_LineID, mRgChoose);
        } else {
            initSpinnerSection();
        }
    }

    private void initSpinnerSection() {
        if (MyApplication.currentNearestTower != null) {
            List<Tower> towerList = new ArrayList<>();
            towerList.addAll(MyApplication.mAllTowersInMap.get(MyApplication.currentNearestTower.getSysGridLineId() + ""));
            if (mTowerFromAdapter == null) {
                mTowerFromAdapter = new UspinnerAdapter(towerList, mContextReference.get());
                mSpinnerInspectFrom.setAdapter(mTowerFromAdapter);
            } else {
                mTowerFromAdapter.onDataChange(towerList);
            }
            if (mTowerToAdapter == null) {
                mTowerToAdapter = new UspinnerAdapter(towerList, mContextReference.get());
                mSpinnerInspectTo.setAdapter(mTowerToAdapter);
            } else {
                mTowerToAdapter.onDataChange(towerList);
            }
        }
    }

    public void caculateNearestDistance(Gridline gridLine) {

        //不知道前辈写的啥 就是拿来改变MyApplication.currentNearestTower
        List<Tower> towerList = new ArrayList<Tower>();
        if (gridLine == null) {
            return;
        }
        if (MyApplication.gridlineTaskStatus == 2 || MyApplication.gridlineTaskStatus == 3) {
            towerList = MyApplication.mAllTowersInMap.get(gridLine.getSysGridLineID() + "");
        } else {
            towerList = TowerDBHelper.getInstance().getLineTowers(Integer.valueOf(gridLine.getSysGridLineID() + ""));
        }

        if (towerList == null || towerList.size() == 0) {
            MyApplication.registeredTower = null;
            MyApplication.currentNearestTower = null;
            MyApplication.nearestDistance = 0;
            MyApplication.nearSecondDistance = 0;
            MyApplication.nearSecondTower = null;
            return;
        }
        double minDistance = Double.MAX_VALUE;
        double distance;
        Tower nereastTower = null;
        for (int i = 0; i < towerList.size(); i++) {
            distance = getDistance(towerList.get(i));
            if (minDistance > distance) {
                minDistance = distance;
                nereastTower = towerList.get(i);//最近的杆塔
            }
        }
        if (nereastTower == null) {
            return;
        }
        if (MyApplication.currentNearestTower == null || MyApplication.currentNearestTower.getSysTowerID() != nereastTower.getSysTowerID()) {
            MyApplication.currentNearestTower = nereastTower;
        }
        MyApplication.nearestDistance = minDistance;
        EventBus.getDefault().post(new UpdateTowerEvent(nereastTower));//更新最近的杆塔
    }

    //根据Gps坐标获取距离
    private double getDistance(Tower tower) {
        double latitude = tower.getLatitude();
        double longitude = tower.getLongitude();
        double currentLatitude = 0;
        double currentLongitude = 0;
        if (AppConstant.CURRENT_LOCATION != null) {
            currentLatitude = AppConstant.CURRENT_LOCATION.latitude;
            currentLongitude = AppConstant.CURRENT_LOCATION.longitude;
        }

        LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(currentLatitude, currentLongitude);
        return LatLngUtils.getDistance(latitude, longitude, gps.latitude, gps.longitude);
    }

    private void initTowerSpinner(String LineID, int typeID) {
        List<Tower> towerList = new ArrayList<>();
        //
        caculateNearestDistance(GridLineDBHelper.getInstance().getLine(Long.parseLong(LineID)));
        //        towerList.addAll(TowerDBHelper.getInstance().getLineTowers(Integer.parseInt(LineID)));
        if (MyApplication.mAllTowersInMap.get(LineID) != null) {
            towerList.addAll(MyApplication.mAllTowersInMap.get(LineID));
        } else if (MyApplication.mTempTowerMap.get(Integer.parseInt(LineID)) != null) {
            towerList.addAll(MyApplication.mTempTowerMap.get(Integer.parseInt(LineID)));
        }
        if (typeID == 1) {
            if (mTowerFromAdapter == null) {
                mTowerFromAdapter = new UspinnerAdapter(towerList, mContextReference.get());
                mSpinnerInspectFrom.setAdapter(mTowerFromAdapter);
            } else {
                mTowerFromAdapter.onDataChange(towerList);
            }
            if (mTowerToAdapter == null) {
                mTowerToAdapter = new UspinnerAdapter(towerList, mContextReference.get());
                mSpinnerInspectTo.setAdapter(mTowerToAdapter);
            } else {
                mTowerToAdapter.onDataChange(towerList);
            }
        } else {
            Tower tower = new Tower();
            if (MyApplication.currentNearestTower != null) {
                Log.d("nate", "initTowerSpinner: " + "最近塔:" + MyApplication.currentNearestTower.getTowerNo());
                tower.setTowerNo("最近塔:" + MyApplication.currentNearestTower.getTowerNo());
                tower.setSysTowerID(MyApplication.currentNearestTower.getSysTowerID());
                Log.d("nate", "initTowerSpinner: " + tower.getTowerNo());
            }
            towerList.add(0, tower);
            if (mTowerRegister == null) {
                mTowerRegister = new UspinnerAdapter(towerList, mContextReference.get());
                mSpinnerTowerRegister.setAdapter(mTowerRegister);
            } else {
                mTowerRegister.onDataChange(towerList);
            }
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = AppConstant.ScreenWidth * 5 / 6;
        getWindow().setAttributes(params);
    }


    private void saveInplace(InPlace inPlace) {
        InPlaceDBHelper.getInstance().insertInplace(inPlace);
        ToastUtil.show("到位登记成功");
    }

}
