package com.uflycn.uoperation.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.uflycn.uoperation.R;


/**
 * Created by user on 2017/6/9.
 */
public class AnimDialog extends Dialog{
    public AnimDialog(Context context) {
        this(context, R.style.dialogWindowAnim);
    }

    public AnimDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void showDialog(int layoutResID, int x, int y){
        setContentView(layoutResID);

        windowDeploy(x, y);
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(true);
        show();
    }

    public void windowDeploy(int x, int y){
        Window window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
//        window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移
//            wl.alpha = 0.6f; //设置透明度
//            wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }
}
