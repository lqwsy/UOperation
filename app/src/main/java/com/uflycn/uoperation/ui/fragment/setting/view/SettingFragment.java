package com.uflycn.uoperation.ui.fragment.setting.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geo.device.data.DeviceManage;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.BaseFragment;
import com.uflycn.uoperation.bean.RegistrationInfo;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.service.UpdateService;
import com.uflycn.uoperation.ui.activity.ChangePwdActivity;
import com.uflycn.uoperation.ui.activity.DataLinkBlueToothActivity;
import com.uflycn.uoperation.ui.fragment.setting.contract.SettingContract;
import com.uflycn.uoperation.ui.fragment.setting.presenter.SettingPresenter;
import com.uflycn.uoperation.ui.main.view.MainActivity;
import com.uflycn.uoperation.util.AESUtils;
import com.uflycn.uoperation.util.DeviceUtils;
import com.uflycn.uoperation.util.IOUtils;
import com.uflycn.uoperation.util.ShareUtil;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.BlueToothDialog;
import com.uflycn.uoperation.widget.SimpleDlg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 系统设置
 * Created by xiaoyehai on 2017/9/4.
 */
public class SettingFragment extends BaseFragment<SettingPresenter> implements SettingContract.View {

    @BindView(R.id.tv_offline_map)
    TextView tvOfflineMap;

    @BindView(R.id.googleRadioButton)
    RadioButton rbGoogleMap;

    @BindView(R.id.gaodeRadioButton)
    RadioButton rbGaodeMap;

    @BindView(R.id.btnClearCache)
    Button btnClearCache;

    @BindView(R.id.btnUpdateVersion)
    Button btnCheckUpgrade;

    @BindView(R.id.btn_login_Out)
    Button btnLoinOut;

    @BindView(R.id.btn_route_planning)
    Button btnRoutePlanning;

    @BindView(R.id.txtVersion)
    TextView tvVersion;

    @BindView(R.id.tv_username)
    TextView mTvUsername;

    @BindView(R.id.tv_device_code)
    TextView mDeviceCodeTv;

    @BindView(R.id.tv_applicant)
    TextView mApplicantTv;

    @BindView(R.id.rb_self_location)
    RadioButton mRbSelfLocation;
    @BindView(R.id.rb_external_location)
    RadioButton mRbExternalLocation;
    @BindView(R.id.ll_location_setting)
    RadioGroup mRgLocationSetting;
    @BindView(R.id.btn_blue_tooth_setting)
    Button mBtnBlueToothSetting;

    Context ctx = null;
    private SimpleDlg mSimpleDlg;
    private Unbinder mUnBinder;

    private ServiceConnection mConn = new ServiceConnection() {
        //final Context ctx = me.getContext();//getActivity().getApplicationContext();
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mUpdateService = ((UpdateService.MyBinder) service).getUpdateService();
            try {
                PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
                String fixedVersion = info.versionName.replace("v", "");
                mUpdateService.checkUpdate(fixedVersion, true);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private UpdateService mUpdateService;
    private boolean isBind = false;
    /**
     * 缓存路径
     */
    private final String CACHE_URL_GOOGLE = Environment.getExternalStorageDirectory().getPath()
            + File.separator + AppConstant.UFLY_STORAGE_PATH + File.separator + AppConstant.DIR_GOOGLE_CACHE;
    private final String CACHE_URL_GAODE = Environment.getExternalStorageDirectory().getPath()
            + File.separator + AppConstant.UFLY_STORAGE_PATH + File.separator + AppConstant.DIR_GAODE_CACHE;
    private final String CACHE_URL_LOG = Environment.getExternalStorageDirectory().getPath()
            + File.separator + AppConstant.APP_STORAGE_PATH + File.separator + AppConstant.DIR_LOG;
    private final String CACHE_URL_PHOTO = Environment.getExternalStorageDirectory().getPath()
            + File.separator + AppConstant.APP_STORAGE_PATH + File.separator + AppConstant.CAMERA_PHOTO_PATH;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_seeting;
    }

    @Override
    public SettingPresenter getPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected void initView() {
        String mapType = ShareUtil.getString(this.getContext(), AppConstant.MAP_TYPE_KEY, mRef.get().getResources().getString(R.string.map_type_google));
        if (mapType.equalsIgnoreCase(mRef.get().getResources().getString(R.string.map_type_google))) {
            rbGoogleMap.setChecked(true);
        } else {
            rbGaodeMap.setChecked(true);
        }
        if (this.ctx == null) {
            this.ctx = this.getActivity().getApplicationContext();
        }

        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            String fixedVersion = "v" + info.versionName;
            tvVersion.setText(fixedVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mDeviceCodeTv.setText(DeviceUtils.getFixedIMEI(mRef.get()));

        if (AppConstant.currentUser == null || AppConstant.currentUser.getRealName() == null) {
            mTvUsername.setText("未命名");

        } else {
            mTvUsername.setText(AppConstant.currentUser.getRealName());
        }

        RegistrationInfo registrationInfo = getRegInfo();
        if (registrationInfo != null) {
            mApplicantTv.setText(registrationInfo.getCompany() + "");
        } else {
            mApplicantTv.setText("未知");
        }
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @OnClick({R.id.iv_open_close_drawer, R.id.btnUpdateVersion, R.id.btnClearCache,
            R.id.googleRadioButton, R.id.gaodeRadioButton, R.id.btn_change_pwd, R.id.btn_clear_photo,
            R.id.btn_login_Out, R.id.btn_route_planning, R.id.btn_blue_tooth_setting, R.id.btn_diff_setting})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_close_drawer:
                ((MainActivity) mRef.get()).openLeftMenu();
                break;
            case R.id.googleRadioButton:
                ShareUtil.setString(this.getContext(), AppConstant.MAP_TYPE_KEY, mRef.get().getResources().getString(R.string.map_type_google));
                //SharedPreferencesUtil.saveDataToSharedPreferences(this.getActivity(), AppConstant.MAP_TYPE_KEY, -1, SharedPreferencesUtil.MAP_CONFIG);
                break;
            case R.id.gaodeRadioButton:
                ShareUtil.setString(this.getContext(), AppConstant.MAP_TYPE_KEY, mRef.get().getResources().getString(R.string.map_type_gaode));
                //SharedPreferencesUtil.saveDataToSharedPreferences(this.getActivity(), AppConstant.MAP_TYPE_KEY, 1, SharedPreferencesUtil.MAP_CONFIG);
                break;
            case R.id.btnClearCache:
                clearCache();
                break;
            case R.id.btn_route_planning:
                showRoutePlanning();
                break;

            case R.id.btnUpdateVersion:
                if (mUpdateService == null) {
                    Intent intent = new Intent(this.getActivity(), UpdateService.class);
                    isBind = ctx.bindService(intent, mConn, this.getActivity().BIND_AUTO_CREATE);
                } else {
                    PackageInfo info = null;
                    try {
                        info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    String fixedVersion = "";
                    if (info != null) {
                        fixedVersion = info.versionName.replace("v", "");
                    }
                    mUpdateService.checkUpdate(fixedVersion, true);
                }
                break;
            case R.id.btn_change_pwd:
                startActivity(new Intent(this.getActivity(), ChangePwdActivity.class));
                break;
            case R.id.btn_clear_photo:
                clearPhoto();
                break;
            case R.id.btn_login_Out:
                ((MainActivity) mRef.get()).clickLogout(null);
                break;
            case R.id.btn_blue_tooth_setting:
                showBlueToothSettingDialog();
                break;
            case R.id.btn_diff_setting:
                goToDiffSetting();
                break;
            default:
                break;
        }
    }

    private void goToDiffSetting() {
        if (!mRbExternalLocation.isChecked()) {
            ToastUtil.show("请先设置GPS数据来源为外部定位!");
            return;
        }
        if (!DeviceManage.GetInstance().isConncet()) {
            ToastUtil.show("请先连接蓝牙设备!");
            return;
        }
        startActivity(new Intent(mRef.get(), DataLinkBlueToothActivity.class));
    }

    private void showBlueToothSettingDialog() {
        if (!mRbExternalLocation.isChecked()) {
            ToastUtil.show("请先设置GPS数据来源为外部定位!");
            return;
        }
        BlueToothDialog dialog = new BlueToothDialog(mRef.get());
        dialog.show();
    }


    public RegistrationInfo getRegInfo() {
        String path = IOUtils.getRootStoragePath(mRef.get());
        String wholePath = path + AppConstant.REGISTER_KEY_PATH;
        File file = new File(wholePath + AppConstant.REGISTER_INFO_NAME);
        if (!file.exists()) {
            return null;
        }

        FileReader fr = null;
        BufferedReader br = null;
        RegistrationInfo registrationInfo = new RegistrationInfo();
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = null;
            int index = 0;
            while ((line = br.readLine()) != null) {
                if (index == 0) {
                    registrationInfo.setName(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 1) {
                    registrationInfo.setCompany(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 2) {
                    registrationInfo.setDeviceNo(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 3) {
                    registrationInfo.setProductName(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 4) {
                    registrationInfo.setApplicant(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 5) {
                    registrationInfo.setTel(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 6) {
                    registrationInfo.setEmail(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return registrationInfo;
    }


    private void showRoutePlanning() {
        final CharSequence[] items = getResources().getStringArray(R.array.route_planning);
        // arraylist to keep the selected items
        final ArrayList seletedItems = new ArrayList();
        boolean[] b = getcheckedItems();
        for (int i = 0; i < b.length; i++) {
            if (b[i])
                seletedItems.add(i);
        }
        AlertDialog dialog = new AlertDialog.Builder(mRef.get())
                .setTitle("请选择规划策略")
                .setMultiChoiceItems(items, b, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            seletedItems.add(indexSelected);
                        } else if (seletedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            seletedItems.remove(Integer.valueOf(indexSelected));
                        }
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here
                        if (seletedItems.contains(1) && seletedItems.contains(3)) {
                            ToastUtil.show("不走高速与高速优先不能同时选中");
                            return;
                        }
                        if (seletedItems.contains(2) && seletedItems.contains(3)) {
                            ToastUtil.show("高速优先与避免收费不能同时选中");
                            return;
                        }

                        if (seletedItems.contains(0)) {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.AVOID_CONGESTION, true);
                        } else {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.AVOID_CONGESTION, false);
                        }

                        if (seletedItems.contains(1)) {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.NOT_HIGH_SPEED, true);
                        } else {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.NOT_HIGH_SPEED, false);
                        }

                        if (seletedItems.contains(2)) {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.AVOID_CHARGES, true);
                        } else {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.AVOID_CHARGES, false);
                        }

                        if (seletedItems.contains(3)) {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.HIGH_SPEED_PRIORITY, true);
                        } else {
                            ShareUtil.setBoolean(mRef.get(), AppConstant.HIGH_SPEED_PRIORITY, false);
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                    }
                }).create();
        dialog.show();
    }

    private boolean[] getcheckedItems() {
        boolean[] b = new boolean[4];
        b[0] = ShareUtil.getBoolean(mRef.get(), AppConstant.AVOID_CONGESTION, true);
        b[1] = ShareUtil.getBoolean(mRef.get(), AppConstant.NOT_HIGH_SPEED, true);
        b[2] = ShareUtil.getBoolean(mRef.get(), AppConstant.AVOID_CHARGES, true);
        b[3] = ShareUtil.getBoolean(mRef.get(), AppConstant.HIGH_SPEED_PRIORITY, false);
        return b;
    }

    /**
     * 清空图片
     */
    private void clearPhoto() {
        if (mSimpleDlg == null) {
            SimpleDlg.Builder builder = new SimpleDlg.Builder();
            mSimpleDlg = builder.create(getContext());
            builder.setContentText("是否清空图片");
            builder.setOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.dlg_btn_left:
                            mSimpleDlg.dismiss();
                            break;
                        case R.id.dlg_btn_right:
                            RecursionDeleteFile(new File(CACHE_URL_PHOTO));
                            mSimpleDlg.dismiss();

                            break;
                    }

                }
            });
        }
        mSimpleDlg.show();
    }


    /**
     * 清除缓存
     */
    private void clearCache() {
        final Activity activity = this.getActivity();
        new AlertDialog.Builder(this.getActivity()).setTitle(getResources().getString(R.string.clear_cache_tip_title)).setMessage(getResources().getString(R.string.clear_cache_tip))
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                File file1 = new File(CACHE_URL_GOOGLE);
                                File file2 = new File(CACHE_URL_GAODE);
                                File file3 = new File(CACHE_URL_LOG);
                                if (file1.exists() == false && file2.exists() == false && file3.exists() == false) {
                                    Toast.makeText(activity, getResources().getString(R.string.clear_cache_successful), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                RecursionDeleteFile(file1);
                                RecursionDeleteFile(file2);
                                RecursionDeleteFile(file3);
                                Toast.makeText(activity, getResources().getString(R.string.clear_cache_success), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancle), null)
                .show();
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public void RecursionDeleteFile(File file) {
        try {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    RecursionDeleteFile(f);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file.getAbsolutePath().equals(CACHE_URL_PHOTO) & !file.exists()) {
            ToastUtil.show("图片清空完毕");
        }
    }

    /**
     * 销毁时调用的方法
     */
    @Override
    public void onDestroy() {
        if (mConn != null && isBind) {
            ctx.unbindService(mConn);
            isBind = false;
        }
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
