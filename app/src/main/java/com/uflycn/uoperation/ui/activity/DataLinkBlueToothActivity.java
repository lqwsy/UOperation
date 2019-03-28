package com.uflycn.uoperation.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.geo.base.CommonFunction;
import com.geo.base.GeoEventBaseActivity;
import com.geo.device.CorsManage.CorsClientManage;
import com.geo.device.CorsManage.SourecTableManage;
import com.geo.device.data.CommandSendManage;
import com.geo.device.data.ConfigNtripParameter;
import com.geo.device.data.GPSCommand;
import com.geo.device.data.GeoDeviceParse;
import com.geo.device.data.SendCommand;
import com.geo.device.event.DeviceUpdateEvent;
import com.geo.device.geoparse.EDataLinkType;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.util.ShareUtil;

import java.util.ArrayList;

public class DataLinkBlueToothActivity extends GeoEventBaseActivity
        implements OnClickListener {
    //    private String[] mlist_ip;
    //    private String[] mlist_port;
    private String[] mMountPoint_list = null;
    private boolean bIsFinish = true;
    private ProgressBar progressBarReceiverData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_datalink);

        initialUI();

        SourecTableManage.getInstance().ReadSourceTable();
        // 添加 默认进行初始化获取挂载点;
        mMountPoint_list = SourecTableManage.getInstance().getMountPointList();

        updateConnectUI(CorsClientManage.GetInstance().IsConnected());

        initSetData();
    }

    private void initialUI() {
        // TODO Auto-generated method stub
        // 初始化按钮的监听事件;
        Button temRegButton = null;
        temRegButton = (Button) findViewById(R.id.button_start);
        if (temRegButton != null) {
            temRegButton.setOnClickListener(this);
        }
        temRegButton = (Button) findViewById(R.id.button_getpointlist);
        if (temRegButton != null) {
            temRegButton.setOnClickListener(this);
        }
        temRegButton = (Button) findViewById(R.id.button_OK1);
        if (temRegButton != null) {
            temRegButton.setOnClickListener(this);
        }
        temRegButton = (Button) findViewById(R.id.ImageButton_back);
        if (temRegButton != null) {
            temRegButton.setOnClickListener(this);
        }

        ImageView temImageView = null;
        temImageView = (ImageView) findViewById(R.id.btn_select_IP);
        temImageView.setOnClickListener(this);
        temImageView = (ImageView) findViewById(R.id.btn_select_Port);
        temImageView.setOnClickListener(this);
        temImageView = (ImageView) findViewById(R.id.btn_select_mount);
        temImageView.setOnClickListener(this);

        progressBarReceiverData = (ProgressBar) findViewById(R.id.progressBar_SetRTK);
        progressBarReceiverData.setMax(5000);
        progressBarReceiverData.setProgress(0);

        //        mlist_ip = getResources().getStringArray(R.array.Service_IP);
        //        mlist_port = getResources().getStringArray(R.array.Service_Port);

        // 初始化的时候,读取蓝牙数据链的配置文件;
        ConfigNtripParameter.getInstance().ConfigRead();
    }

    private void initSetData() {
        // TODO Auto-generated method stub
        setTextViewText(R.id.edittext_IP, ShareUtil.getString(this, "BLUE_TOOTH_IP", "60.205.8.49"));
        setTextViewText(R.id.edittext_Port, ShareUtil.getString(this, "BLUE_TOOTH_PORT", "8002"));
        // cors账号和密码;
        setTextViewText(R.id.editText_user, ShareUtil.getString(this, "BLUE_TOOTH_USER", "qxbdow006"));
        setTextViewText(R.id.editText_password, ShareUtil.getString(this, "BLUE_TOOTH_PASSWORD", "19dabd0"));
        //        setTextViewText(R.id.edittext_mount, ConfigNtripParameter.getInstance().getMountPoint());
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button_start:
                onButtonStart();
                break;
            case R.id.button_getpointlist:
                onButtonGetMountPoint();
                break;
            case R.id.button_OK1:
                saveConfig();

                onSetDataLinkM5Bluetooth(true);
                break;
            case R.id.ImageButton_back:
                finish();
                break;
            case R.id.btn_select_IP:
                //                OnSelectIp();
                break;
            case R.id.btn_select_Port:
                //                OnSelectPort();
                break;
            case R.id.btn_select_mount:
                OnSelectMountPoint();
                break;
            default:
                break;
        }
    }

    private void onButtonGetMountPoint() {
        // TODO Auto-generated method stub
        String strIP = getTextViewString(R.id.edittext_IP);
        int nPort = CommonFunction.parseInt(getTextViewString(R.id.edittext_Port));

        // 手机网络获取挂载点的情况;
        CorsClientManage.GetInstance().setNetworkParameter(strIP, nPort);
        CorsClientManage.GetInstance().onUpdateMountPoint();
    }

    private void onButtonStart() {
        // TODO Auto-generated method stub
        if (CorsClientManage.GetInstance().IsConnected()) {
            CorsClientManage.GetInstance().Close();

            CorsClientManage.GetInstance().SetBoolStopCors(false);
            return;
        }

        String strIP = getTextViewString(R.id.edittext_IP);
        int nPort = CommonFunction.parseInt(getTextViewString(R.id.edittext_Port));
        String strUser = getTextViewString(R.id.editText_user);
        String strPassword = getTextViewString(R.id.editText_password);
        String strMountPoint = "RTCM32_GGB";


        CorsClientManage.GetInstance().setNetworkParameter(
                strIP, nPort, strUser, strPassword, strMountPoint);
        CorsClientManage.GetInstance().onCorsLogin();
        saveConfig();
    }

    private void onSetDataLinkM5Bluetooth(boolean isFinish) {
        bIsFinish = isFinish;
        // TODO Auto-generated method stub
        //做出修改;[2016/09/27 xucaixu]
        GeoDeviceParse.getInstance().mConfigWorkMode.DataLink.Type = EDataLinkType.ExtendSource;
        ArrayList<SendCommand> Send_Command = GPSCommand.getInstance().Cmd_Set_DataLink_Bluetooth(true);

        // 如果指令不为空, 就发送指令;
        if (Send_Command != null) {
            CommandSendManage.getInstance().setSendCommandList(Send_Command);
            CommandSendManage.getInstance().StartSendCommand();
        }
    }

    private void saveConfig() {
        // TODO Auto-generated method stub
        String strIP = getTextViewString(R.id.edittext_IP);
        int nPort = CommonFunction.parseInt(getTextViewString(R.id.edittext_Port));
        String strUser = getTextViewString(R.id.editText_user);
        String strPassword = getTextViewString(R.id.editText_password);
        String strMountPoint = "RTCM32_GGB";

        //此处将是否自动连接设置为true;[2017/2/16 xucaixu]
        ShareUtil.setString(this,"BLUE_TOOTH_IP",strIP);
        ShareUtil.setString(this,"BLUE_TOOTH_PORT",nPort+"");
        ShareUtil.setString(this,"BLUE_TOOTH_USER",strUser);
        ShareUtil.setString(this,"BLUE_TOOTH_PASSWORD",strPassword);
//        ConfigNtripParameter.getInstance().setAutoConnect(true);
//        ConfigNtripParameter.getInstance().setIP(strIP);
//        ConfigNtripParameter.getInstance().setPort(nPort);
//        ConfigNtripParameter.getInstance().setUser(strUser);
//        ConfigNtripParameter.getInstance().setPassword(strPassword);
//        ConfigNtripParameter.getInstance().setMountPoint(strMountPoint);
//        ConfigNtripParameter.getInstance().ConfigSave();
    }


    private void OnSelectMountPoint() {
        //当箭头图标被点击之后，执行的事件;
        if (mMountPoint_list == null || mMountPoint_list.length == 0) {
            // 提示挂载点列表为空 ;
            makeTextString(R.string.MountPoint_Is_None_Please_check, Gravity.CENTER);
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(mMountPoint_list, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setTextViewText(R.id.edittext_mount, mMountPoint_list[whichButton]);
                        dialog.dismiss();  //退出当前对话框; [2015/5/8 xucaixu]
                        return;
                    }
                }).create();

        alertDialog.setCanceledOnTouchOutside(true);                  //点击对话框以外的地方退出对话框;
        Window dialogWindow = alertDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        alertDialog.show();

        // 设置窗口的大小;
        alertDialog.getWindow().setLayout((int) (display.getWidth()), (int) (display.getHeight()));
    }

    private void updateConnectUI(boolean bConnect) {
        if (bConnect) {
            CorsClientManage.GetInstance().SetBoolStopCors(true);
            setTextViewText(R.id.button_start, getString(R.string.Stop));
            //连接成功CORS，就发送设置指令;(M5的情况)
            onSetDataLinkM5Bluetooth(false);
        } else {
            setTextViewText(R.id.button_start, getString(R.string.Start));
        }

        setEnabled(R.id.edittext_IP, !bConnect);
        setEnabled(R.id.edittext_Port, !bConnect);
        setEnabled(R.id.editText_user, !bConnect);
        setEnabled(R.id.editText_password, !bConnect);
        setEnabled(R.id.edittext_mount, !bConnect);

        setEnabled(R.id.btn_select_mount, !bConnect);
        setEnabled(R.id.btn_select_IP, !bConnect);
        setEnabled(R.id.btn_select_Port, !bConnect);

        setEnabled(R.id.button_getpointlist, !bConnect);
    }

    public void onEventMainThread(DeviceUpdateEvent.UpdateMountPointSuccessStatus obj) {
        if (obj == null) {
            return;
        }

        if (obj.IsGetMountPointSuccess()) {
            SourecTableManage.getInstance().SaveSourceTable();
            makeTextString(R.string.getlist_successed, Gravity.CENTER);
            mMountPoint_list = SourecTableManage.getInstance().getMountPointList();
        } else {
            makeTextString(R.string.point_get_failed_info, Gravity.CENTER);
        }
    }

    public void onEventMainThread(DeviceUpdateEvent.UpdateNetworkConnectStatus obj) {
        if (obj == null) {
            return;
        }

        Toast toast = Toast.makeText(this, obj.getStatus(), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        updateConnectUI(CorsClientManage.GetInstance().IsConnected());
    }

    int mnCountReceiver = 0;

    public void onEventMainThread(DeviceUpdateEvent.ReceiverNetworkData obj) {
        if (obj == null) {
            return;
        }
        mnCountReceiver += obj.getSize();
        mnCountReceiver = (mnCountReceiver % 5000);

        progressBarReceiverData.setProgress(mnCountReceiver);
    }

    public void onEventMainThread(DeviceUpdateEvent.CommandSendFinishStatus obj) {
        if (obj == null) {
            return;
        }

        if (!bIsFinish) {
            bIsFinish = true;
            return;
        }

        //提示指令已经发送成功；推出当前界面;
        Toast toast = Toast.makeText(getApplicationContext(), R.string.Bluetooth_datalink_set_seccess,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        finish();
    }
}
