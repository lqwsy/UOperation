package com.uflycn.uoperation.ui.fragment.DetectionManage;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.Gridline;
import com.uflycn.uoperation.bean.IceCover;
import com.uflycn.uoperation.bean.ItemDetail;
import com.uflycn.uoperation.bean.SpinnerOption;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.GridLineDBHelper;
import com.uflycn.uoperation.greendao.IceCoverDBHelper;
import com.uflycn.uoperation.greendao.ItemDetailDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.DialogUtils;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.util.ViewPagerFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IceDetectionFragment extends ViewPagerFragment {


    @BindView(R.id.tv_line_name)
    TextView mTvLineName;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.tv_tower_no)
    TextView mTvTowerNo;
    @BindView(R.id.spinner_ice_type)
    Spinner mSpinnerIceType;
    @BindView(R.id.et_ice_cover)
    EditText mEtIceCover;
    @BindView(R.id.et_ice_temp)
    EditText mEtIceTemp;
    @BindView(R.id.et_ice_humidity)
    EditText mEtIceHumidity;
    @BindView(R.id.layout_ice_cover)
    LinearLayout mLayoutIceCover;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;

    Call<BaseCallBack<String>> call3;

    @Override
    public View initView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_ice_detection, null);
    }

    @Override
    public void initData() {
        if (MyApplication.registeredTower != null) {
            mTvTowerNo.setText(MyApplication.registeredTower.getTowerNo());
            Gridline gridline = GridLineDBHelper.getInstance().getLine(MyApplication.registeredTower.getSysGridLineId());
            mTvLineName.setText(gridline.getLineName());
        }
        List<SpinnerOption> mIceType = new ArrayList<>();
        List<ItemDetail> items = ItemDetailDBHelper.getInstance().getItem("覆冰种类");
        for (int i = 0; i < items.size(); i++) {
            mIceType.add(new SpinnerOption(items.get(i).getItemDetailsId() + "", items.get(i).getItemsName()));
        }
        mIceType.add(0, new SpinnerOption("", "请选择覆冰种类"));
        ArrayAdapter<SpinnerOption> mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mIceType);
        mSpinnerIceType.setAdapter(mAdapter);
    }

    @OnClick({R.id.btn_submit})
    public void doClick(View v) {
        //        if (MyApplication.registeredTower == null || MyApplication.currentNearestTower.getSysTowerID() != MyApplication.registeredTower.getSysTowerID()) {
        //            ToastUtil.show(mWeakReference.get().getString(R.string.tip_far_awayfrom_tower));
        //            return;
        //        }
        if (MyApplication.registeredTower == null) {
            ToastUtil.show(getContext().getString(R.string.tip_far_awayfrom_tower));
            return;
        }
        // 提交覆冰检测数据
        submitIceCover();
    }

    /**
     * 提交覆冰检测数据
     */
    private void submitIceCover() {
        if (MyApplication.registeredTower != null) {
            if (TextUtils.isEmpty(mEtIceCover.getText()) || checkPoint(mEtIceCover)) {
                ToastUtil.show("请输入覆冰厚度！");
                return;
            }
            if (TextUtils.isEmpty(mEtIceHumidity.getText()) || checkPoint(mEtIceHumidity)) {
                ToastUtil.show("请输入湿度");
                return;
            }
            if (TextUtils.isEmpty(mEtIceTemp.getText()) || checkPoint(mEtIceTemp)) {
                ToastUtil.show("请输入温度");
                return;
            }
            if (mSpinnerIceType.getSelectedItemPosition() == 0) {
                ToastUtil.show("请选择覆冰种类");
                return;
            }
            final IceCover iceCover = createIceCover();
            IceCoverDBHelper.getInstance().insert(iceCover);
            postIceNet(iceCover);
        }
    }

    private void postIceNet(final IceCover iceCover) {
        mProgressDialog.show();
        Gson gson = new Gson();
        List<IceCover> list = new ArrayList<>();
        list.add(iceCover);
        String jsonStr = gson.toJson(list);
        Log.i("jsonStr", jsonStr);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonStr);
        call3 = RetrofitManager.getInstance().getService(ApiService.class)
                .postOptIceCover(body);
        call3.enqueue(new Callback<BaseCallBack<String>>() {
            @Override
            public void onResponse(Call<BaseCallBack<String>> call, Response<BaseCallBack<String>> response) {
                mProgressDialog.dismiss();
                BaseCallBack<String> body = response.body();
                if (body != null && body.getCode() == 1) {
                    iceCover.setUploadFlag(1);
                    IceCoverDBHelper.getInstance().updateById(iceCover);
                    ToastUtil.show("提交成功");
                } else {
                    DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            postIceNet(iceCover);
                        }
                    });
                }
                resetIceCover();
            }

            @Override
            public void onFailure(Call<BaseCallBack<String>> call, Throwable t) {
                //                ToastUtil.show("提交成功");
                mProgressDialog.dismiss();
                DialogUtils.showRetryPostDialog(getContext(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postIceNet(iceCover);
                    }
                });
                resetIceCover();
            }
        });
    }

    private void resetIceCover() {
        mEtIceCover.setText("");
        mEtIceTemp.setText("");
        mEtIceHumidity.setText("");
        mSpinnerIceType.setSelection(0);
    }

    private IceCover createIceCover() {
        IceCover iceCover = new IceCover();
        iceCover.setTowerId(MyApplication.registeredTower.getSysTowerID().intValue());
        iceCover.setSysUserId(AppConstant.currentUser.getUserId());
        iceCover.setUploadFlag(0);
        iceCover.setCreateDate(DateUtil.format(new Date()));
        iceCover.setSysUserId(AppConstant.currentUser.getUserId());
        if (!(TextUtils.isEmpty(mEtIceCover.getText()) || checkPoint(mEtIceCover))) {
            iceCover.setIceCoverHeight(Double.parseDouble(mEtIceCover.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mEtIceTemp.getText()) || checkPoint(mEtIceTemp))) {
            iceCover.setTemperature(Double.parseDouble(mEtIceTemp.getText().toString()));
        }
        if (!(TextUtils.isEmpty(mEtIceHumidity.getText()) || checkPoint(mEtIceHumidity))) {
            iceCover.setWetness(Double.parseDouble(mEtIceHumidity.getText().toString()));
        }
        iceCover.setIceType(((SpinnerOption) mSpinnerIceType.getAdapter().getItem(mSpinnerIceType.getSelectedItemPosition())).getValue());
        if (MyApplication.gridlineTaskStatus == 2) {
            if (MyApplication.registeredTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()) != null) {
                iceCover.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.registeredTower.getSysGridLineId()));
            } else if (MyApplication.currentNearestTower != null && MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()) != null) {
                iceCover.setPlanDailyPlanSectionIDs(MyApplication.mDayPlanLineMap.get(MyApplication.currentNearestTower.getSysGridLineId()));
            }
        }else if (MyApplication.gridlineTaskStatus==3){
            iceCover.setSysPatrolExecutionID(MyApplication.mPlanPatrolExecutionId);
        }
        return iceCover;
    }


    public boolean checkPoint(EditText editText) {
        if (editText.getText().toString().equals(".") || editText.getText().toString().equals("-") || editText.getText().toString().equals("-.")) {
            return true;
        }
        return false;
    }

    @Override
    public void onDismissDialog() {
        if (call3 != null && !call3.isCanceled()) {
            call3.cancel();
        }
    }

    @Override
    public void onDestroy() {
        if (call3 != null && !call3.isCanceled()) {
            call3.cancel();
        }
        super.onDestroy();
    }
}
