package com.uflycn.uoperation.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.uflycn.uoperation.R;

/**
 * Created by Administrator on 2017/11/2.
 */
public class AddImageGridView extends GridView implements AdapterView.OnItemClickListener{
    private Dialog dialog;
    private int CODE_RESULT = 0;
    private int POSITION = -1;

    public AddImageGridView(Context context) {
        super(context);
        setOnItemClickListener(this);
    }

    public AddImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemClickListener(this);
    }

    public AddImageGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showDialog();
    }
    public void showDialog() {
        View localView = LayoutInflater.from(getContext()).inflate(
                R.layout.dialog_add_picture, null);
        TextView tv_camera = (TextView) localView.findViewById(R.id.tv_camera);
        TextView tv_gallery = (TextView) localView.findViewById(R.id.tv_gallery);
        TextView tv_cancel = (TextView) localView.findViewById(R.id.tv_cancel);
        dialog = new Dialog(getContext(), R.style.custom_dialog);
        dialog.setContentView(localView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        // 设置全屏
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        tv_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 拍照
                CODE_RESULT = 1;
                addCallBack.onClickButton(CODE_RESULT);
            }
        });

        tv_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 从系统相册选取照片
                CODE_RESULT = 2;
                addCallBack.onClickButton(CODE_RESULT);
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Integer.MAX_VALUE:表示int类型能够表示的最大值，值为2的31次方-1
        //>>2:右移N位相当于除以2的N的幂
        //MeasureSpec.AT_MOST：子布局可以根据自己的大小选择任意大小的模式
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    public void getCodeResult(AddCallBack code){
        addCallBack = code;
    }

    AddCallBack addCallBack = null;

    /**
     * 一定一个接口
     */
    public interface AddCallBack{
        public void onClickButton(int code);
    }

}
