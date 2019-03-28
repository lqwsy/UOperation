package com.uflycn.uoperation.ui.fragment.hiddendanger.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.uflycn.uoperation.R;
import com.uflycn.uoperation.ui.adapter.GridViewAddImgesAdpter;
import com.uflycn.uoperation.util.AddPhotoUtils;
import com.uflycn.uoperation.util.DateUtil;
import com.uflycn.uoperation.util.ToastUtil;
import com.uflycn.uoperation.widget.AddImageGridView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 棚膜管理-特巡
 */
public class SpecialTourActivity extends Activity {
    @BindView(R.id.tv_patrol_time)
    TextView tv_patrol_time;//巡视时间add_img_break
    @BindView(R.id.add_img_break)
    AddImageGridView addImgBreak;
    @BindView(R.id.tv_shed_time)
    TextView tvShedTime;
    private Date mCurrentDate;

    private final int MAX_IMAGE_LENGTH = 3;
    private final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择private static final
    private File tempFile;
    private List<Map<String, Object>> datas;
    private GridViewAddImgesAdpter gridViewAddImgesAdpter;
   private  String formatDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_special_tour);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        datas = new ArrayList<>();
        gridViewAddImgesAdpter = new GridViewAddImgesAdpter(datas, this);
        gridViewAddImgesAdpter.setMaxImages(MAX_IMAGE_LENGTH);
        addImgBreak.setAdapter(gridViewAddImgesAdpter);
        addImgBreak.getCodeResult(new AddImageGridView.AddCallBack() {
            @Override
            public void onClickButton(int code) {
                switch (code) {
                    case 1:
                        tempFile = AddPhotoUtils.camera(SpecialTourActivity.this);
                        break;
                    case 2:
                        AddPhotoUtils.gallery(SpecialTourActivity.this, gridViewAddImgesAdpter);
                        break;
                    default:
                        break;
                }
            }
        });


        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        mCurrentDate = calendar.getTime();
         formatDate = sf.format(mCurrentDate);

//        setTime(formatDate);

    }

    @OnClick({R.id.tv_patrol_time, R.id.tv_shed_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_patrol_time:
                tv_patrol_time.setText(formatDate);
                showTime("巡视时间",tv_patrol_time);
                break;
            case R.id.tv_shed_time:
                tvShedTime.setText(formatDate);
                showTime("敷棚时间",tvShedTime);
                break;
            default:
                break;
        }
    }

//    private void setTime(String formatDate) {
//        tv_patrol_time.setText(formatDate);
//        //点击后选中日期
//        tv_patrol_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//    }

    private void showTime(String title,final TextView getTextView) {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mCurrentDate = date;
                getTextView.setText(DateUtil.format(date, DateUtil.PATTERN_CLASSICAL_SIMPLE));
            }
        }).setType(new boolean[]{true, true, true, false, false, false})//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(20)
                .setTitleSize(20)//标题文字大小
                .setTitleText(title)//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setRangDate(startDate, endDate)
                .setDate(Calendar.getInstance())
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                        //处理代码
                        for (String path : paths) {
                            datas.add(AddPhotoUtils.photoPath(SpecialTourActivity.this, path));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
                        }
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:
                    // 从相机返回的数据
                    if (AddPhotoUtils.hasSdcard()) {
                        if (tempFile != null) {
                            datas.add(AddPhotoUtils.photoPath(SpecialTourActivity.this, tempFile.getPath()));
                            gridViewAddImgesAdpter.notifyDataSetChanged();
                        } else {
                            ToastUtil.show("相机异常请稍后再试！");
                        }
                    } else {
                        ToastUtil.show("未找到存储卡，无法存储照片！");
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
