package com.uflycn.uoperation.widget;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.TowerChange;
import com.uflycn.uoperation.bean.VirtualTower;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.eventbus.UpdateLocationEvent;
import com.uflycn.uoperation.greendao.TowerChangeDBHelper;
import com.uflycn.uoperation.greendao.VirtualTowerDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.ToastUtil;
import com.xflyer.entity.LatLngInfo;
import com.xflyer.utils.CoordinateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/9/20.
 */
public class DialogUpdateTower extends Dialog implements View.OnClickListener {
    private View contentView;
    private Reference<Context> mContextRef;
    private Long mTowerid;
    private Call<BaseCallBack<TowerChange>> mUpdateTowerRequest;
    private String mTowerNum;
    private ProgressDialog mProgressDialog;

    public DialogUpdateTower(Context context) {
        this(context, R.style.Dialog);
    }

    public DialogUpdateTower(Context context, int themeResId) {
        super(context, themeResId);
        mContextRef = new WeakReference<>(context);
        initView();
    }

    private void initView() {
        contentView = LayoutInflater.from(mContextRef.get()).inflate(R.layout.dialog_update_tower, null);
        contentView.findViewById(R.id.img_close).setOnClickListener(this);
        contentView.findViewById(R.id.btn_update_tower).setOnClickListener(this);
        setContentView(contentView);
    }

    private void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContextRef.get());
            mProgressDialog.setMessage("正在提交变更，请等待...");
        }
        mProgressDialog.show();
    }

    public void setDatas(Map<String, Object> attrs) {
        double lat = (double) attrs.get(AppConstant.TOWER_LAT_KEY);
        double lng = (double) attrs.get(AppConstant.TOWER_LNG_KEY);
        double altitude = (double) attrs.get(AppConstant.TOWER_ALTITUDE_KEY);
        mTowerNum = (String) attrs.get(AppConstant.TOWER_NUM_KEY);
        String lineName = (String) attrs.get(AppConstant.TOWER_LINENAME_KEY);
        ((TextView) contentView.findViewById(R.id.tv_line_name)).setText(lineName);
        ((TextView) contentView.findViewById(R.id.tv_tower_num)).setText(mTowerNum + "号杆塔");
        mTowerid = (Long) attrs.get(AppConstant.TOWER_ID_KEY);
        ((EditText) contentView.findViewById(R.id.tv_tower_lat)).setText(lat + "");
        ((EditText) contentView.findViewById(R.id.tv_tower_lng)).setText(lng + "");
        ((EditText) contentView.findViewById(R.id.tv_tower_altitude)).setText(altitude + "");
        updaCurrentLocation();
        contentView.findViewById(R.id.btn_use_current).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_tower:
                postUpdateTower();
                break;
            case R.id.img_close:
                dismiss();
                break;
            case R.id.btn_use_current:
                setCurrentLocation();
                break;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mUpdateTowerRequest != null && !mUpdateTowerRequest.isCanceled()) {
            mUpdateTowerRequest.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

    private void postUpdateTower() {
        EditText etLat = (EditText) contentView.findViewById(R.id.tv_tower_lat);
        EditText etLng = (EditText) contentView.findViewById(R.id.tv_tower_lng);
        EditText etAlt = (EditText) contentView.findViewById(R.id.tv_tower_altitude);
        if (TextUtils.isEmpty(etLat.getText())) {
            ToastUtil.show("纬度不能为空");
            return;
        }
        if (TextUtils.isEmpty(etLng.getText())) {
            ToastUtil.show("经度不能为空");
            return;
        }
        if (TextUtils.isEmpty(etAlt.getText())) {
            ToastUtil.show("高度不能为空");
            return;
        }
        final double lat = Double.parseDouble(etLat.getText().toString());
        final double lng = Double.parseDouble(etLng.getText().toString());
        final double altitude = Double.parseDouble(etAlt.getText().toString());
        final TowerChange towerChange = new TowerChange();
        towerChange.setUploadFlag(0);
        towerChange.setCreateDate(DateUtil.format(new Date()));
        towerChange.setLatitude(lat);
        towerChange.setLongitude(lng);
        towerChange.setSysTowerId(mTowerid.intValue());
        towerChange.setAltitude(altitude);
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null) {
                towerChange.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else {
                towerChange.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineSb.toString());
            }
        } else if (MyApplication.gridlineTaskStatus == 3) {
            towerChange.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        showDialog();
        TowerChangeDBHelper.getInstance().insert(towerChange);
        mUpdateTowerRequest = RetrofitManager.getInstance().getService(ApiService.class).postUpdateTower(lng, lat, altitude, mTowerid.intValue(),
                towerChange.getSysPatrolExecutionID(), towerChange.getPlanDailyPlanSectionIDs());
        mUpdateTowerRequest.enqueue(new Callback<BaseCallBack<TowerChange>>() {
            @Override
            public void onResponse(Call<BaseCallBack<TowerChange>> call, Response<BaseCallBack<TowerChange>> response) {
                if (response == null || response.body() == null) {
                    ToastUtil.show(mContextRef.get(), "变更登记提交失败，请重新登录");
                    mProgressDialog.dismiss();
                    return;
                }
                if (response.body().getCode() == 0 || response.body().getData() == null) {
                    ToastUtil.show(mContextRef.get(), response.body().getMessage());
                    mProgressDialog.dismiss();
                    return;
                }
                towerChange.setUploadFlag(1);
                TowerChangeDBHelper.getInstance().updateTowerChange(towerChange);

                VirtualTower virtualTower = new VirtualTower();
                virtualTower.setTowerId(mTowerid);
                virtualTower.setLongitude(lng);
                virtualTower.setLatitude(lat);
                virtualTower.setAltitude(altitude);
                virtualTower.setStatus(0);

                VirtualTowerDBHelper.getInstance().insertOrUpdate(virtualTower);

                ToastUtil.show(mContextRef.get(), "变更登记提交成功");
                mProgressDialog.dismiss();

                dismiss();
            }

            @Override
            public void onFailure(Call<BaseCallBack<TowerChange>> call, Throwable t) {
                ToastUtil.show(mContextRef.get(), "变更登记提交失败，请检查网络");
                mProgressDialog.dismiss();
                if (t != null) {
                    Log.e("RequestErr", t.getMessage());
                }
            }
        });
    }

    private void setCurrentLocation() {
        if (AppConstant.CURRENT_LOCATION != null) {
            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
            ((EditText) contentView.findViewById(R.id.tv_tower_lat)).setText(gps.latitude + "");
            ((EditText) contentView.findViewById(R.id.tv_tower_lng)).setText(gps.longitude + "");
            ((EditText) contentView.findViewById(R.id.tv_tower_altitude)).setText(AppConstant.CURRENT_LOCATION.altitude + "");
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThread(UpdateLocationEvent event) {
        updaCurrentLocation();
    }

    private void updaCurrentLocation() {
        if (AppConstant.CURRENT_LOCATION != null) {
            LatLngInfo gps = CoordinateUtils.gcj_To_Gps84(AppConstant.CURRENT_LOCATION.latitude, AppConstant.CURRENT_LOCATION.longitude);
            ((TextView) contentView.findViewById(R.id.tv_location_lng)).setText(gps.longitude + "");
            ((TextView) contentView.findViewById(R.id.tv_location_lat)).setText(gps.latitude + "");
            ((TextView) contentView.findViewById(R.id.tv_location_altitude)).setText(AppConstant.CURRENT_LOCATION.altitude + "");
        }
    }

}
