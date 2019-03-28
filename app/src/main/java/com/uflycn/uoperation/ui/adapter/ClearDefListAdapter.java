package com.uflycn.uoperation.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.base.CommomViewHolder;
import com.uflycn.uoperation.base.CommonAdapter;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.WorksheetApanageTask;
import com.uflycn.uoperation.eventbus.RefreshListEvenBus;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.activity.ClearDefectListActivity;
import com.uflycn.uoperation.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2018/3/6.
 */
public class ClearDefListAdapter extends CommonAdapter<WorksheetApanageTask> {

    private Call<BaseCallBack<WorksheetApanageTask>> mCall;

    public ClearDefListAdapter(List<WorksheetApanageTask> list, Context context, int resID) {
        super(list, context, resID);
    }

    @Override
    public void fillData(int position,final CommomViewHolder holder) {
        final WorksheetApanageTask clearDefList = getItem(position);
        holder.setText(R.id.tv_sheek_num, clearDefList.getTaskNo());
        holder.setText(R.id.tv_sheek_ower, clearDefList.getManager());
        holder.setText(R.id.tv_sheek_state, clearDefList.getStatusString());
        holder.setText(R.id.tv_line_name, clearDefList.getLineName());

        if (clearDefList.getStatus()==5){
            holder.setText(R.id.btn_change_state, "商谈");
        }else{
            holder.setText(R.id.btn_change_state, "消缺");
        }

        holder.getView(R.id.btn_change_state).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clearDefList.getStatus() == 5){

                    final AlertDialog.Builder dialog = new AlertDialog.Builder(mContextRef.get());
                    dialog.setMessage("确定将编号为:"+clearDefList.getTaskNo()+"的工单状态转换为“已商谈”？");
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeState(clearDefList);
                        }
                    });
                    dialog.create().show();
                }else{
                    Intent intent = new Intent(mContextRef.get(), ClearDefectListActivity.class);
                    intent.putExtra("WorksheetApanageTask", clearDefList);
                    mContextRef.get().startActivity(intent);
                }

            }
        });
    }

    private void changeState(final WorksheetApanageTask task){
        mCall = RetrofitManager.getInstance().getService(ApiService.class).sendDiscussApanageTask(task.getSysApanageTaskId());
        mCall.enqueue(new Callback<BaseCallBack<WorksheetApanageTask>>() {
            @Override
            public void onResponse(Call<BaseCallBack<WorksheetApanageTask>> call, Response<BaseCallBack<WorksheetApanageTask>> response) {
                if (response == null || response.body() == null) {
                    ToastUtil.show("网络请求失败");
                    return;
                }
                if (response.body().getCode() == 1) {
                    task.setStatus(6);
                    task.setStatusString("已商谈");
                    ToastUtil.show("状态切换成功");

                    EventBus.getDefault().post(new RefreshListEvenBus());
                } else {
                    ToastUtil.show("状态切换失败");
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<WorksheetApanageTask>> call, Throwable t) {
                ToastUtil.show("网络连接失败");
            }
        });
    }
}
