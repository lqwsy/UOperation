package com.uflycn.uoperation.util;

import android.content.Context;

import com.geo.base.GeoResources;
import com.uflycn.uoperation.R;

public class ResourcesManage extends GeoResources {
	private Context mContext = null;
	public ResourcesManage(Context context) {
		mContext = context;
	}


	@Override
	public int getDrawable_gsv_bd() {
		return R.drawable.gsv_bd;
	}

	@Override
	public int getDrawable_gsv_gl() {
		return R.drawable.gsv_gl;
	}

	@Override
	public int getDrawable_gsv_gn() {
		return R.drawable.gsv_gn;
	}

	@Override
	public int getDrawable_gsv_gp() {
		return R.drawable.gsv_gp;
	}

	@Override
	public int getDrawable_gsv_sbas() {
		return R.drawable.gsv_sbas;
	}

	@Override
	public String getString_BASE_state() {
		return mContext.getString(R.string.BASE_state);
	}

	@Override
	public String getString_Connect_fail() {
		return mContext.getString(R.string.Connect_fail);
	}

	@Override
	public String getString_DIF3D_state() {
		return mContext.getString(R.string.DIF3D_state);
	}

	@Override
	public String getString_FIXED_state() {
		return mContext.getString(R.string.FIXED_state);
	}

	@Override
	public String getString_FLOAT_state() {
		return mContext.getString(R.string.FLOAT_state);
	}

	@Override
	public String getString_None_state() {
		return mContext.getString(R.string.None_state);
	}

	@Override
	public String getString_SINGLE_state() {
		return mContext.getString(R.string.SINGLE_state);
	}

	@Override
	public String getString_Set_Disconnect_Network() {
		return mContext.getString(R.string.Set_Disconnect_Network);
	}

	@Override
	public String getString_Set_Start_Get_MountPoint() {
		return mContext.getString(R.string.Set_Start_Get_MountPoint);
	}

	@Override
	public String getString_login_failed() {
		return mContext.getString(R.string.login_failed);
	}

	@Override
	public String getString_login_successed() {
		return mContext.getString(R.string.login_successed);
	}

	@Override
	public String GetNetworkStatus(int nStatus) {

		String strStatus = "";

		if (nStatus == 1)
		{
			strStatus =mContext.getString(R.string.SYS_INCORRECT_COMMAND);
		}
		else if (nStatus == 2)
		{
			strStatus = mContext.getString(R.string.SYS_INCORRECT_PATH);
		}
		else if (nStatus == 3)
		{
			strStatus = mContext.getString(R.string.SYS_UNSUPPORT_COMMAND);
		}
		else if (nStatus == 4)
		{
			strStatus =mContext.getString(R.string.SYS_UNSUPPORTED_COMMAND_IN_CURRENT_MODE);
		}
		else if (nStatus == 5)
		{
			strStatus = mContext.getString(R.string.SYS_INCORRECT_PARAM);
		}
		else if (nStatus == 6)
		{
			strStatus = mContext.getString(R.string.SYS_COMMAND_FAILED);
		}
		else if (nStatus == 7)
		{
			strStatus = mContext.getString(R.string.SYS_PRE_COMMMAND_PROCESSING);
		}
		else if (nStatus == 8)
		{
			strStatus = mContext.getString(R.string.SYS_UNSUPPORTED_COMMAND_IN_THIS_MODEL);
		}
		else if (nStatus == 101)
		{
			strStatus = mContext.getString(R.string.SYS_DISK_INIT_ERROR);
		}
		else if (nStatus == 102)
		{
			strStatus = mContext.getString(R.string.SYS_OEM_INIT_ERROR);
		}
		else if (nStatus == 103)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_INIT_ERROR);
		}
		else if (nStatus == 104)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_INIT_ERROR);
		}
		else if (nStatus == 110)
		{
			strStatus = mContext.getString(R.string.SYS_REGISTRATION_CODE_ERROR);
		}
		else if (nStatus == 111)
		{
			strStatus = mContext.getString(R.string.SYS_REGISTRATION_CODE_EXPIRED);
		}
		else if (nStatus == 120)
		{
			strStatus = mContext.getString(R.string.SYS_RECORDING_CANNOT_START);
		}
		else if (nStatus == 121)
		{
			strStatus = mContext.getString(R.string.SYS_SATELLITE_NOT_MEET);
		}
		else if (nStatus == 122)
		{
			strStatus = mContext.getString(R.string.SYS_NO_RECORD_CANNOT_STOP);
		}
		else if (nStatus == 123)
		{
			strStatus = mContext.getString(R.string.SYS_OEM_CMD_OVERFLOW);
		}
		else if (nStatus == 201)
		{
			strStatus = mContext.getString(R.string.SYS_FS_DISK_ERROR);
		}
		else if (nStatus == 202)
		{
			strStatus = mContext.getString(R.string.SYS_FS_INTI_ERROR);
		}
		else if (nStatus == 203)
		{
			strStatus = mContext.getString(R.string.SYS_FS_NOT_READY);
		}
		else if (nStatus == 204)
		{
			strStatus = mContext.getString(R.string.SYS_FS_NO_FILE);
		}
		else if (nStatus == 205)
		{
			strStatus = mContext.getString(R.string.SYS_FS_NO_PATH);
		}
		else if (nStatus == 206)
		{
			strStatus = mContext.getString(R.string.SYS_FS_INVALID_NAME);
		}
		else if (nStatus == 207)
		{
			strStatus = mContext.getString(R.string.SYS_FS_DENIED);
		}
		else if (nStatus == 208)
		{
			strStatus = mContext.getString(R.string.SYS_FS_EXIST);
		}
		else if (nStatus == 209)
		{
			strStatus = mContext.getString(R.string.SYS_FS_INVALID_OBJECT);
		}
		else if (nStatus == 210)
		{
			strStatus = mContext.getString(R.string.SYS_FS_WRITE_PROTECTED);
		}
		else if (nStatus == 211)
		{
			strStatus = mContext.getString(R.string.SYS_FS_INVALID_DRIVE);
		}
		else if (nStatus == 212)
		{
			strStatus = mContext.getString(R.string.SYS_FS_NOT_ENABLED);
		}
		else if (nStatus == 213)
		{
			strStatus = mContext.getString(R.string.SYS_FS_NO_FILESYSTEM);
		}
		else if (nStatus == 214)
		{
			strStatus = mContext.getString(R.string.SYS_FS_MKFS_ABORTED);
		}
		else if (nStatus == 215)
		{
			strStatus = mContext.getString(R.string.SYS_FS_TIMEOUT);
		}
		else if (nStatus == 216)
		{
			strStatus = mContext.getString(R.string.SYS_FS_LOCKED);
		}
		else if (nStatus == 217)
		{
			strStatus = mContext.getString(R.string.SYS_FS_NOT_ENOUGH_CORE);
		}
		else if (nStatus == 218)
		{
			strStatus = mContext.getString(R.string.SYS_FS_TOO_MANY_OPEN_FILES);
		}
		else if (nStatus == 300)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_OK);
		}
		else if (nStatus == 301)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_NOT_SUPPORTED);
		}
		else if (nStatus == 302)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_INT_ERROR);
		}
		else if (nStatus == 303)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_NOT_READY);
		}
		else if (nStatus == 304)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_COMMAND_PENDING);
		}
		else if (nStatus == 305)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_COMMAND_TIMEOUT);
		}
		else if (nStatus == 306)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_COMMAND_ERROR);
		}
		else if (nStatus == 307)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_UNKNOW_COMMAND);
		}
		else if (nStatus == 308)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_FIFO_OVERFLOW);
		}
		else if (nStatus == 309)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_TX_ERROR);
		}
		else if (nStatus == 310)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_TASK_ERROR);
		}
		else if (nStatus == 311)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_FREQ_OUT_OF_RANGE);
		}
		else if (nStatus == 312)
		{
			strStatus = mContext.getString(R.string.SYS_RADIO_VERIFY_ERROR);
		}
		else if (nStatus == 400)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_OK);
		}
		else if (nStatus == 401)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_INT_ERR);
		}
		else if (nStatus == 402)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_NOT_READY);
		}
		else if (nStatus == 403)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_SIM_NOT_READY);
		}
		else if (nStatus == 404)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_NETWORK_ERROR);
		}
		else if (nStatus == 405)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_REGISTER_ERROR);
		}
		else if (nStatus == 406)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_COMMAND_PENDING);
		}
		else if (nStatus == 407)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_COMMAND_ERROR);
		}
		else if (nStatus == 408)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_UNKNOW_COMMAND);
		}
		else if (nStatus == 409)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_CONNECTING);
		}
		else if (nStatus == 410)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_CONNECT_ERROR);
		}
		else if (nStatus == 411)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_CONNECT_TIMEOUT);
		}
		else if (nStatus == 412)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_WRITE_ERROR);
		}
		else if (nStatus == 413)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_NET_NOT_READY);
		}
		else if (nStatus == 414)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_SMS_ERROR);
		}
		else if (nStatus == 415)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_SETMODE_FAIL);
		}
		else if (nStatus == 416)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_SETMODE_ERR);
		}
		else if (nStatus == 417)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_NO_MOUNTPOINTLIST);
		}
		else if (nStatus == 418)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_CONNECTED);
		}
		else if (nStatus == 419)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_RECONNECT);
		}
		else if (nStatus == 420)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_NTRIP_UNAUTHORIZED);
		}
		else if (nStatus == 421)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_MOUNTPOINT_UNACCEPTED);
		}
		// 0423：模块忙 SYS_GPRS_BUSY
		else if (nStatus == 422)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_NETWORK_ROAMING_FORBID);
		}
		else if (nStatus == 423)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_BUSY);
		}
		else if (nStatus == 430)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_INIT);
		}
		else if (nStatus == 431)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_READY);
		}
		else if (nStatus == 432)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_INITFAIL);
		}
		else if (nStatus == 433)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_CONNECT);
		}
		else if (nStatus == 434)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_CONNECTFAIL);
		}
		else if (nStatus == 435)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_CONNECTED);
		}
		else if (nStatus == 436)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_PROTOCOLFAIL);
		}
		else if (nStatus == 437)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_STAT_TRANSFER);
		}
		else if (nStatus == 440)
		{
			strStatus = mContext.getString(R.string.SYS_GPRS_POWEROFF);
		}
		else if (nStatus == 501)
		{
			strStatus = mContext.getString(R.string.SYS_STATIC_NOT_REC);
		}
		else if (nStatus == 502)
		{
			strStatus = mContext.getString(R.string.SYS_STATIC_IN_REC);
		}
		else if (nStatus == 503)
		{
			strStatus = mContext.getString(R.string.SYS_STATIC_PENDING_REC);
		}
		else if (nStatus == 504)
		{
			strStatus = mContext.getString(R.string.SYS_DYNAMIC_NOT_REC);
		}
		else if (nStatus == 505)
		{
			strStatus = mContext.getString(R.string.SYS_DYNAMIC_IN_REC);
		}
		else if (nStatus == 506)
		{
			strStatus = mContext.getString(R.string.SYS_DYNAMIC_PENDING_REC);
		}
		else if (nStatus == 507)
		{
			strStatus = mContext.getString(R.string.SYS_STATIC_RECORD_ERROR);
		}
		else if (nStatus == 508)
		{
			strStatus = mContext.getString(R.string.SYS_NEED_FOR_UPGRADE);
		}
		else if (nStatus == 509)
		{
			strStatus = mContext.getString(R.string.SYS_STATIC_FAIL_PDOP);
		}
		else if (nStatus == 510)
		{
			strStatus = mContext.getString(R.string.SYS_STATIC_FILENAME_ERR);
		}
		else if (nStatus == 601)
		{
			strStatus = mContext.getString(R.string.SYS_BASE_PENDING);
		}
		else if (nStatus == 602)
		{
			strStatus = mContext.getString(R.string.SYS_BASE_TANSMIT);
		}
		else if (nStatus == 603)
		{
			strStatus = mContext.getString(R.string.SYS_BASE_STOP);
		}
		else if (nStatus == 604)
		{
			strStatus = mContext.getString(R.string.SYS_ROVER_SINGLE);
		}
		else if (nStatus == 605)
		{
			strStatus = mContext.getString(R.string.SYS_ROVER_FIX);
		}
		else if (nStatus == 610)
		{
			strStatus = mContext.getString(R.string.SYS_BASE_FAIL_PDOP);
		}
		else if (nStatus == 10001)
		{
			strStatus = mContext.getString(R.string.SYS_OS_TIMEOUT);
		}
		else if (nStatus == 10002)
		{
			strStatus = mContext.getString(R.string.SYS_OS_MAILBOX_FULL);
		}
		else if (nStatus == 10003)
		{
			strStatus = mContext.getString(R.string.SYS_OS_QUEUE_FULL);
		}
		else if (nStatus == 10004)
		{
			strStatus = mContext.getString(R.string.SYS_OS_QUEUE_EMPTY);
		}
		else if (nStatus == 10005)
		{
			strStatus = mContext.getString(R.string.SYS_OS_MEMORY_FULL);
		}
		else if (nStatus == 10010)
		{
			strStatus = mContext.getString(R.string.SYS_OS_ERR_TIMEOUT);
		}

		return strStatus;
	}


	@Override
	public String getString_Get_GGA_Remote_Address() {
		return mContext.getString(R.string.Get_GGA_Remote_Address);
	}


	@Override
	public String getString_Get_GGA_Remote_Port() {
		return mContext.getString(R.string.Get_GGA_Remote_Port);
	}


	@Override
	public String getString_Get_GGA_Remote_Upload() {
		return mContext.getString(R.string.Get_GGA_Remote_Upload);
	}


	@Override
	public String getString_Get_WIFI_Scanlist() {
		return mContext.getString(R.string.Get_WIFI_Scanlist);
	}


	@Override
	public String getString_Set_APN_Name() {
		return mContext.getString(R.string.Set_APN_Name);
	}


	@Override
	public String getString_Set_APN_Password() {
		return mContext.getString(R.string.Set_APN_Password);
	}


	@Override
	public String getString_Set_APN_User() {
		return mContext.getString(R.string.Set_APN_User);
	}


	@Override
	public String getString_Set_Ant_Height() {
		return mContext.getString(R.string.Set_Ant_Height);
	}


	@Override
	public String getString_Set_Ant_Measure() {
		return mContext.getString(R.string.Set_Ant_Measure);
	}


	@Override
	public String getString_Set_Auto_Connect() {
		return mContext.getString(R.string.Set_Auto_Connect);
	}


	@Override
	public String getString_Set_Auto_Rec() {
		return mContext.getString(R.string.Set_Auto_Rec);
	}


	@Override
	public String getString_Set_Base_Auto_Start() {
		return mContext.getString(R.string.Set_Base_Auto_Start);
	}


	@Override
	public String getString_Set_Base_Delay_Start() {
		return mContext.getString(R.string.Set_Base_Delay_Start);
	}


	@Override
	public String getString_Set_Base_ID() {
		return mContext.getString(R.string.Set_Base_ID);
	}


	@Override
	public String getString_Set_Base_Mode() {
		return mContext.getString(R.string.Set_Base_Mode);
	}


	@Override
	public String getString_Set_Base_Output() {
		return mContext.getString(R.string.Set_Base_Output);
	}


	@Override
	public String getString_Set_Base_REPEAT_Mode() {
		return mContext.getString(R.string.Set_Base_REPEAT_Mode);
	}


	@Override
	public String getString_Set_Base_SINGLE_Mode() {
		return mContext.getString(R.string.Set_Base_SINGLE_Mode);
	}


	@Override
	public String getString_Set_Base_Start() {
		return mContext.getString(R.string.Set_Base_Start);
	}


	@Override
	public String getString_Set_BestPosa_Output() {
		return mContext.getString(R.string.Set_BestPosa_Output);
	}


	@Override
	public String getString_Set_Cors_Address() {
		return mContext.getString(R.string.Set_Cors_Address);
	}


	@Override
	public String getString_Set_Cors_Group() {
		return mContext.getString(R.string.Set_Cors_Group);
	}


	@Override
	public String getString_Set_Cors_Group_small() {
		return mContext.getString(R.string.Set_Cors_Group_small);
	}


	@Override
	public String getString_Set_Cors_MountPoint() {
		return mContext.getString(R.string.Set_Cors_MountPoint);
	}


	@Override
	public String getString_Set_Cors_Port() {
		return mContext.getString(R.string.Set_Cors_Port);
	}


	@Override
	public String getString_Set_Cors_User() {
		return mContext.getString(R.string.Set_Cors_User);
	}


	@Override
	public String getString_Set_Cur_DataLink() {
		return mContext.getString(R.string.Set_Cur_DataLink);
	}


	@Override
	public String getString_Set_Cur_DataLink_Blue() {
		return mContext.getString(R.string.Set_Cur_DataLink_Blue);
	}


	@Override
	public String getString_Set_Cur_DataLink_DUAL() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Cur_DataLink_DUAL);
	}


	@Override
	public String getString_Set_Cur_DataLink_Ext() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Cur_DataLink_Ext);
	}


	@Override
	public String getString_Set_Cur_DataLink_Network() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Cur_DataLink_Network);
	}


	@Override
	public String getString_Set_Cur_DataLink_UHF() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Cur_DataLink_UHF);
	}


	@Override
	public String getString_Set_Data_Output() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Data_Output);
	}


	@Override
	public String getString_Set_Data_Output_List() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Data_Output_List);
	}


	@Override
	public String getString_Set_Diff_Mode() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Diff_Mode);
	}


	@Override
	public String getString_Set_Ext_Band() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Ext_Band);
	}


	@Override
	public String getString_Set_Fixed_Life() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Fixed_Life);
	}


	@Override
	public String getString_Set_GGA_Remote_Address() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_GGA_Remote_Address);
	}


	@Override
	public String getString_Set_GGA_Remote_Port() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_GGA_Remote_Port);
	}


	@Override
	public String getString_Set_Getall_Output() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Getall_Output);
	}


	@Override
	public String getString_Set_Height_jiezhi() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Height_jiezhi);
	}


	@Override
	public String getString_Set_NetWork_Relay() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_NetWork_Relay);
	}


	@Override
	public String getString_Set_Network_Mode() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Network_Mode);
	}


	@Override
	public String getString_Set_Network_Reset() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Network_Reset);
	}


	@Override
	public String getString_Set_PDOP_Lim() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_PDOP_Lim);
	}


	@Override
	public String getString_Set_Point_Name() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Point_Name);
	}


	@Override
	public String getString_Set_Radio_Channel() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Radio_Channel);
	}


	@Override
	public String getString_Set_Radio_Frequency() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Radio_Frequency);
	}


	@Override
	public String getString_Set_Radio_MODE() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Radio_MODE);
	}


	@Override
	public String getString_Set_Radio_Power() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Radio_Power);
	}


	@Override
	public String getString_Set_ReConnect_Network() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_ReConnect_Network);
	}


	@Override
	public String getString_Set_Record_Interval() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Record_Interval);
	}


	@Override
	public String getString_Set_Record_Raw() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Record_Raw);
	}


	@Override
	public String getString_Set_Rover_Mode() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Rover_Mode);
	}


	@Override
	public String getString_Set_SBAS_Diff_Mode() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_SBAS_Diff_Mode);
	}


	@Override
	public String getString_Set_SNR_Output() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_SNR_Output);
	}


	@Override
	public String getString_Set_Start_Rec() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Start_Rec);
	}


	@Override
	public String getString_Set_Static_Mode_Output() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Static_Mode_Output);
	}


	@Override
	public String getString_Set_Upload_GGA_Interval() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Upload_GGA_Interval);
	}


	@Override
	public String getString_Set_Used_BeiDou() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Used_BeiDou);
	}


	@Override
	public String getString_Set_Used_Glonass() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Used_Glonass);
	}


	@Override
	public String getString_Set_Used_Gps() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Used_Gps);
	}


	@Override
	public String getString_Set_Used_LBAND() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Used_LBAND);
	}


	@Override
	public String getString_Set_Used_SBAS() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_Used_SBAS);
	}


	@Override
	public String getString_Set_WIFI_Mode() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_WIFI_Mode);
	}


	@Override
	public String getString_Set_WIFI_Password() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_WIFI_Password);
	}


	@Override
	public String getString_Set_WIFI_SName() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_WIFI_SName);
	}


	@Override
	public String getString_Set_WIFI_SPassword() {
		// TODO Auto-generated method stub
		return mContext.getString(R.string.Set_WIFI_SPassword);
	}
}
