package com.uflycn.uoperation.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.uflycn.uoperation.R;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailsActivity extends Activity {
    @BindView(R.id.tv_work_num)
    TextView mTvWorkNum;

    @BindView(R.id.tv_work_man)
    TextView mTvWorkMan;

    @BindView(R.id.tv_work_tel)
    TextView mTvWorkTel;

    @BindView(R.id.tv_work_time)
    TextView mTvWorkTime;

    @BindView(R.id.et_work_remark)
    TextView mTvWorkRemark;

    @BindView(R.id.tv_work_line)
    TextView mTvWorkLine;

    @BindView(R.id.tv_work_tower)
    TextView mTvWorkTower;

    @BindView(R.id.tv_work_type)
    TextView mTvWorkType;

    @BindView(R.id.tv_work_level)
    TextView mTvWorkLevel;

    @BindView(R.id.tv_work_des)
    TextView mTvWorkDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_details);


    }



    @OnClick({R.id.iv_back,R.id.btn_load_line,R.id.btn_play})
    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_load_line:

                break;
            case R.id.btn_play:

                break;
        }
    }
}
