package com.uflycn.uoperation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.NaviInfo;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.app.MyApplication;
import com.uflycn.uoperation.eventbus.StopNaviEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by user on 2017/6/13.
 */
public class NaviInfoView extends RelativeLayout implements View.OnClickListener {

    private ImageView mRoadDes;
    private TextView mDisToNext;
    private TextView mNextRoadName;
    private TextView mRemaindis;

    private ImageView mStopNavi;
    private ImageView mResumeNavi;
   private boolean isNaving = true;

    private int[] resids = {R.drawable.sou0,R.drawable.sou0,R.drawable.sou2,R.drawable.sou3,R.drawable.sou4,R.drawable.sou5,R.drawable.sou6,R.drawable.sou7,R.drawable.sou8,R.drawable.sou9,R.drawable.sou10,
            R.drawable.sou11,R.drawable.sou12,R.drawable.sou13,R.drawable.sou14,R.drawable.sou15,R.drawable.sou16,R.drawable.sou17,R.drawable.sou18,R.drawable.sou20,};

    public NaviInfoView(Context context) {
        this(context,null);
    }

    public NaviInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(!isInEditMode()){
            mRoadDes = (ImageView)findViewById(R.id.autonavi_roadsign);
            mDisToNext = (TextView)findViewById(R.id.autonavi_nextRoadSignDisText);
            mNextRoadName = (TextView)findViewById(R.id.autonavi_nextRoadNameText);
            mRemaindis = (TextView)findViewById(R.id.autonavi_speed_mode);
            mStopNavi = (ImageView)findViewById(R.id.autonavi_close);
            mStopNavi.setOnClickListener(this);
            mResumeNavi = (ImageView)findViewById(R.id.autonavi_continue_navi);
            mResumeNavi.setOnClickListener(this);
        }
    }

    public void setData(NaviInfo info){
        int iconType = info.getIconType();
        Log.i("NaviInfo",iconType+"");
        mRoadDes.setImageResource(resids[iconType]);
        mDisToNext.setText(info.getCurStepRetainDistance()+"米后");
        mNextRoadName.setText(info.getNextRoadName());
        String remain = getDistance(info.getPathRetainDistance())+","+(int)info.getPathRetainTime()/60+"分钟";
        mRemaindis.setText(remain);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.autonavi_close:
                SimpleDlg.Builder builder =  new SimpleDlg.Builder();
                final SimpleDlg dlg = builder.create(getContext());
                builder.setContentText("确定退出导航？");
                builder.setOnclickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.dlg_btn_left:
                                dlg.dismiss();
                                break;
                            case R.id.dlg_btn_right:
                                EventBus.getDefault().post(new StopNaviEvent());
                                AMapNavi.getInstance(MyApplication.getContext()).stopNavi();
                                dlg.dismiss();
                                break;
                        }
                    }
                });
                dlg.show();
                break;
            case R.id.autonavi_continue_navi:
                if(isNaving){
                    AMapNavi.getInstance(getContext()).pauseNavi();
                    mResumeNavi.setImageResource(R.drawable.drive_map_icon_start_portrait_day);
                }else{
                    AMapNavi.getInstance(getContext()).resumeNavi();
                    mResumeNavi.setImageResource(R.drawable.autonavi_simulator_land_pause);
                }
                isNaving = !isNaving;
                break;
        }
    }


    private  String getDistance(int dis){
        if(dis<1000){
            return dis+"米";
        }else{
            return ((double)dis)/1000+"公里";
        }
    }
}
