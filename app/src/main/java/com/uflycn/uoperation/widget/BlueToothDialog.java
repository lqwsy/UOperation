package com.uflycn.uoperation.widget;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geo.base.GeoResources;
import com.geo.device.CorsManage.CorsClientManage;
import com.geo.device.CorsManage.SourecTableManage;
import com.geo.device.data.BluetoothDeviceNode;
import com.geo.device.data.BtListAdapter;
import com.geo.device.data.CommandSendManage;
import com.geo.device.data.ConfigDeviceManage;
import com.geo.device.data.ConfigNtripParameter;
import com.geo.device.data.DeviceManage;
import com.geo.device.data.GPSCommand;
import com.geo.device.data.SendCommand;
import com.geo.device.event.DeviceUpdateEvent;
import com.geo.device.io.ConnectListener;
import com.geo.device.io.ReceiverListener;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.util.ResourcesManage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import de.greenrobot.event.EventBus;

public class BlueToothDialog extends Dialog {

    private static final String TAG = BlueToothDialog.class.getSimpleName();
    private ListView mLvBlueTooth;
    private BtListAdapter mBtListAdapter = null;
    private ArrayList<BluetoothDeviceNode> mBluetoothArrayList = new ArrayList<BluetoothDeviceNode>();
    private BroadcastReceiver mBluetoothReceiver = null;
    private Button mBtnConnect;
    private LinearLayout mLlLoading;
    private TextView mTvLoading;

    public BlueToothDialog(@NonNull Context context) {
        this(context, 0);
    }

    public BlueToothDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initGeo();
    }

    private void initView() {
        setContentView(R.layout.dialog_blue_tooth);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSearchBluetooth();
                dismiss();
                //                showLog();
            }
        });
        findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                onButtonSettingSearch();
                onButtonConnect();
            }
        });
        mLvBlueTooth = (ListView) findViewById(R.id.lv_blue_tooth);
        mLvBlueTooth.setAdapter(mBtListAdapter);
        mLvBlueTooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                }

                if (mBtListAdapter.GetSelected() == position) {
                    mBtListAdapter.setSelected(-1);
                    return;
                }

                // 添加 ; [2015/11/24 xucaixu]
                mBtListAdapter.setSelected(position);
            }
        });
        mBtnConnect = (Button) findViewById(R.id.btn_sure);
        mLlLoading = (LinearLayout) findViewById(R.id.ll_loading);
        mTvLoading = (TextView) findViewById(R.id.tv_loading);
    }


    private void showLog() {
        DeviceManage.GetInstance().RegReceiverListener(new ReceiverListener() {
            @Override
            public void OnReceiverCallBack(int nLength, byte[] data) {
                // 如果是显示就在界面上更新; [2017/02/14 xucaixu]
                String strDebugString = new String(data, 0, nLength);
                //                if (strDebugString.startsWith("$GPGGA")) {
                //                    Log.e("nate", "OnReceiverCallBack: '" + strDebugString);
                //                }
            }
        });
    }


    private void initGeo() {
        GeoResources.initInstance(new ResourcesManage(getContext()));

        String strConfigPath = Environment.getExternalStorageDirectory().getPath() + "/GeoLocationServer";
        File temFile = new File(strConfigPath);
        if (!temFile.exists()) {
            temFile.mkdir();
        }
        strConfigPath += "/Config";
        if (!temFile.exists()) {
            temFile.mkdir();
        }
        DeviceManage.GetInstance().initDeviceMange(getContext());
        CommandSendManage.getInstance();
        ConfigDeviceManage.GetInstance().setConfigPath(strConfigPath);
        ConfigDeviceManage.GetInstance().LoadConfig();
        // 初始化的时候,读取蓝牙数据链的配置文件;
        ConfigNtripParameter.getInstance().setConfigPath(strConfigPath);
        ConfigNtripParameter.getInstance().ConfigRead();
        SourecTableManage.getInstance().setConfigPath(strConfigPath);
        ConfigDeviceManage.GetInstance().setDeviceConnectType(0);
        DeviceManage.GetInstance().setDeviceConnectType(0);
        initbtdevicelist();
        onButtonSettingSearch();

        if (DeviceManage.GetInstance().isConncet()) {
            mBtnConnect.setText("断开");
        }
    }

    private void stopSearchBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private void onButtonSettingSearch() {
        // TODO Auto-generated method stub
        if (BluetoothAdapter.getDefaultAdapter() == null) {
            return;
        }
        searchBluetooth();
    }


    private void onButtonConnect() {
        // TODO Auto-generated method stub
        //根据当前状态,判断是否连接来处理当前的连接按钮的点击事件; [2016/5/3 xucaixu]
        if (!DeviceManage.GetInstance().isConncet()) {
            //蓝牙连接方式
            int nSelect = mBtListAdapter.GetSelected();
            if (nSelect < 0 || nSelect >= mBluetoothArrayList.size()) {
                return;
            }

            // 如果蓝牙没有打开,就给出提示; [2016/9/21 xucaixu]
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                BluetoothAdapter.getDefaultAdapter().enable();
            }
            mTvLoading.setText("正在连接…");
            mLlLoading.setVisibility(View.VISIBLE);
            //弹出正在连接中的对话框,并且连接仪器; [2016/5/13 xucaixu]
            BluetoothDeviceNode temDeviceNode = mBluetoothArrayList.get(nSelect);

            String strDevice = String.format(Locale.CHINESE, "%s|%s", temDeviceNode.strName, temDeviceNode.strAddress);

            //设置设备
            DeviceManage.GetInstance().setSelectedDevice(strDevice);

            //将当前点击的蓝牙设备设置到配置管理类中;
            ConfigDeviceManage.GetInstance().setBluetoothName(temDeviceNode.strName);
            ConfigDeviceManage.GetInstance().setBluetoothAddress(temDeviceNode.strAddress);

            //将连接的配置文件予以保存;
            ConfigDeviceManage.GetInstance().SaveConfig();

            //通信连接;
            DeviceManage.GetInstance().connect();
            DeviceManage.GetInstance().setConncetState(true);

        } else {
            //断开通信连接;
            DeviceManage.GetInstance().disConnect();
            DeviceManage.GetInstance().setConncetState(false);
            // 提示正在连接中;

        }
        updateUIForConnect(DeviceManage.GetInstance().isConncet());

    }

    private void updateUIForConnect(boolean connect) {
        if (connect) {
            // 断开;
            mBtnConnect.setText("断开");
        } else {
            // 连接;
            mBtnConnect.setText("连接");
        }
    }


    // 搜索蓝牙 ;
    private void searchBluetooth() {
        //蓝牙管理器
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return;
        }

        // 如果蓝牙没打开, 就打开蓝牙设备;
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();    //打开蓝牙;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //添加; [2016/9/21 xucaixu]
            searchBluetooth();
        }

        //正在搜索
        if (mBluetoothAdapter.isDiscovering()) {
            return;
        }

        mBluetoothArrayList.clear();
        mBtListAdapter.setSelected(-1);


        // 如果蓝牙不是在搜索设备,就执行蓝牙设备搜索 ;
        mBluetoothReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // 搜索设备时, 取得设备的MAC地址;
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    BluetoothDeviceNode temDeviceNode = new BluetoothDeviceNode();
                    temDeviceNode.strName = device.getName();

                    if (temDeviceNode.strName == null || temDeviceNode.strName.equals("")) {
                        temDeviceNode.strName = "Unknow Device";
                        return;
                    }
                    temDeviceNode.strAddress = device.getAddress();

                    boolean bFind = false;
                    //防止列表重复
                    for (int i = mBluetoothArrayList.size() - 1; i >= 0; i--) {
                        if (temDeviceNode.strAddress.compareTo(mBluetoothArrayList.get(i).strAddress) == 0) {
                            mBluetoothArrayList.set(i, temDeviceNode);
                            bFind = true;
                            break;
                        }
                    }
                    if (!bFind) {
                        mBluetoothArrayList.add(temDeviceNode);
                    }

                    mBtListAdapter.notifyDataSetChanged();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    // 设备搜索结束 ;
                    stopSearchBluetooth();
                }
            }
        };

        // 设置广播信息过滤 ;(添加搜索完成选项); [2016/10/28 xucaixu]
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        getContext().registerReceiver(mBluetoothReceiver, intentFilter);
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        mBluetoothAdapter.startDiscovery();
    }

    private void initbtdevicelist() {
        // TODO Auto-generated method stub
        // 不是蓝牙已经连接的情况, 执行以下环节: 得到搜索的蓝牙列表; [2015/4/27 xucaixu]
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            // 不支持蓝牙
            return;
        }

        // 添加上次连接的设备; [2016/2/17 xucaixu]
        String strLastDeviceName = ConfigDeviceManage.GetInstance().getBluetoothName();
        String strLastDeviceAddress = ConfigDeviceManage.GetInstance().getBluetoothAddress();

        mBluetoothArrayList.clear();
        int nFindIndex = -1;
        if (!strLastDeviceAddress.isEmpty()) {
            BluetoothDeviceNode temDeviceNode = new BluetoothDeviceNode();
            temDeviceNode.strName = strLastDeviceName;
            temDeviceNode.strAddress = strLastDeviceAddress;
            nFindIndex = 0;
            mBluetoothArrayList.add(temDeviceNode);
        }

        // 获取蓝牙以配对的设备列表
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        Iterator<BluetoothDevice> it = devices.iterator();
        while (it.hasNext()) {
            BluetoothDevice device = (BluetoothDevice) it.next();
            BluetoothDeviceNode temDeviceNode = new BluetoothDeviceNode();
            temDeviceNode.strName = device.getName();
            if (temDeviceNode.strName == null || temDeviceNode.strName.isEmpty()) {
                temDeviceNode.strName = "Unknow Device";
            }
            temDeviceNode.strAddress = device.getAddress();

            if (strLastDeviceAddress.compareTo(temDeviceNode.strAddress) == 0) {
                continue;
            }
            mBluetoothArrayList.add(temDeviceNode);
        }

        mBtListAdapter = new BtListAdapter(getContext(), mBluetoothArrayList, R.layout.doublelistitem,
                new String[]{"Device Name", "Device Address"},
                new int[]{R.id.l_text, R.id.r_text});

        mBtListAdapter.setSelected(nFindIndex);
        mLvBlueTooth.setAdapter(mBtListAdapter);
    }

    @Override
    public void onDetachedFromWindow() {
        if (BluetoothAdapter.getDefaultAdapter() != null &&
                BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
            stopSearchBluetooth();
        }
        if (mBluetoothReceiver != null) {
            getContext().unregisterReceiver(mBluetoothReceiver);
        }
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    /**
     * 连接设备返回的事件;
     *
     * @param eventStatus
     */
    public void onEventMainThread(DeviceUpdateEvent.DeviceConnectResultStatus eventStatus) {
        // 提示正在连接中; ssssss
        //        setVisibility(R.id.LinearLayout_search, View.GONE);
        updateUIForConnect(eventStatus.getIsConnectSuccess());
        // 连接成功,此时处理保存配置;
        if (eventStatus.getIsConnectSuccess()) {
            Log.e("xucaixu", "连接成功~~");
            Toast.makeText(getContext(), "连接成功", Toast.LENGTH_SHORT).show();
//            login();

            //获取需要发送的指令;
            // 判断当前蓝牙是否已经连接,根据蓝牙连接情况,选中更新;
            //            mTvLoading.setText("仪器初始化设置…");
            //            mLlLoading.setVisibility(View.VISIBLE);
            //            setCommand();
        } else {
            // 连接的状态:失败; [2016/4/18 xucaixu]
            Toast.makeText(getContext(), "蓝牙连接失败", Toast.LENGTH_SHORT).show();
            mLlLoading.setVisibility(View.GONE);
        }
    }

    private void setCommand() {
        ArrayList<SendCommand> Send_Command = null;
        if (ConfigDeviceManage.GetInstance().getDeviceConnectType() == 0) {
            Send_Command = GPSCommand.getInstance().Cmd_Read(true, "");
        } else {
            Send_Command = GPSCommand.getInstance().Cmd_Read(true, "geo2800");
        }

        CommandSendManage.getInstance().setSendCommandList(Send_Command);
        CommandSendManage.getInstance().StartSendCommand();
    }

    public void onEventMainThread(DeviceUpdateEvent.CommandSendFinishStatus obj) {
        if (obj == null) {
            return;
        }
        //实时更新UI;
        if (ConnectListener.CommanderStatus.SUCCESS == DeviceManage.GetInstance().getConnectedStatus()) {
            mLlLoading.setVisibility(View.GONE);
        }
    }


    /**
     * 断开连接的事件;
     *
     * @param obj
     */
    public void onEventMainThread(DeviceUpdateEvent.DeviceDisConnectResultStatus obj) {
        if (obj == null) {
            return;
        }
        //        Log.e("nate", "onEventMainThread: " + Thread.currentThread().getName());
        updateUIForConnect(obj.getDeviceDisConnectResultStatus());
    }


    public void onEventMainThread(DeviceUpdateEvent.UpdateLocationResultStatus obj) {
        if (obj == null) {
            return;
        }
        if (mLlLoading.getVisibility() == View.VISIBLE) {
            mLlLoading.setVisibility(View.GONE);
        }
//        ToastUtil.show("蓝牙连接成功");
    }


    private void login() {
        if (CorsClientManage.GetInstance().IsConnected()) {
            //            CorsClientManage.GetInstance().Close();
            //
            //            CorsClientManage.GetInstance().SetBoolStopCors(false);
            return;
        }
        String strIP = "60.205.8.49";
        int nPort = 8002;
        String strUser = "qxbdow006";
        String strPassword = "19dabd0";
        String strMountPoint = "RTCM32_GGB";

        CorsClientManage.GetInstance().setNetworkParameter(
                strIP, nPort, strUser, strPassword, strMountPoint);
        CorsClientManage.GetInstance().onCorsLogin();
    }
}
