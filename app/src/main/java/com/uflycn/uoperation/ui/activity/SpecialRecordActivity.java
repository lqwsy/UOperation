package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.BaseCallBack;
import com.uflycn.uoperation.bean.BrokenInspectRecord;
import com.uflycn.uoperation.greendao.BrokenInspectRecordDBHelper;
import com.uflycn.uoperation.http.ApiService;
import com.uflycn.uoperation.http.RetrofitManager;
import com.uflycn.uoperation.ui.adapter.BrokenRecordAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 特巡记录
 */
public class SpecialRecordActivity extends Activity {

    @BindView(R.id.list_broke_record)
    ListView mBrokeRecordList;
    private BrokenRecordAdapter mRecordAdapter;
    private int mDocumentId;
    private Call<BaseCallBack<List<BrokenInspectRecord>>> mRequestCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_special_record);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            mDocumentId = intent.getIntExtra("DocumentId", 0);
        }
    }

    @OnClick({R.id.iv_back})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }


    private void initDatas() {
        getBrokeRecordList();
    }


    private void getBrokeRecordList() {
        mRequestCall = RetrofitManager.getInstance().getService(ApiService.class).getBrokenRecordList(mDocumentId);
        mRequestCall.enqueue(new Callback<BaseCallBack<List<BrokenInspectRecord>>>() {
            @Override
            public void onResponse(Call<BaseCallBack<List<BrokenInspectRecord>>> call, Response<BaseCallBack<List<BrokenInspectRecord>>> response) {
                if (response == null || response.body() == null) {
                    getRecordFromDB();
                    return;
                }
                if (response.body().getCode() == 0) {
                    Log.e("requestErr", "getBrokeRecordList-onResponse: " + response.body().getMessage());
                    getRecordFromDB();
                } else {
                    refreshDatas(response.body().getData());
                    BrokenInspectRecordDBHelper.getInstance().insertCacheList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BaseCallBack<List<BrokenInspectRecord>>> call, Throwable t) {
                if (t != null) {
                    Log.e("requestErr", t.getMessage());
                }
                getRecordFromDB();
            }
        });
    }


    private void getRecordFromDB() {
        refreshDatas(BrokenInspectRecordDBHelper.getInstance().getAllCacheById(mDocumentId));
    }


    private void refreshDatas(List<BrokenInspectRecord> datas) {
        if (mRecordAdapter == null) {
            mRecordAdapter = new BrokenRecordAdapter(datas, this, R.layout.item_broken_record);
            mBrokeRecordList.setAdapter(mRecordAdapter);
        } else {
            mRecordAdapter.onDataChange(datas);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRequestCall != null && !mRequestCall.isCanceled()) {
            mRequestCall.cancel();
        }
    }
}
