package com.uflycn.uoperation.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uflycn.uoperation.R;


/**
 * Created by user on 2017/6/14.
 * 弹出框
 */
public class SimpleDlg extends Dialog {

    private SimpleDlg(Context context) {
        this(context, R.style.dialogWindowAnim);
    }

    private SimpleDlg(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private View container;
        private SimpleDlg mInstance;
        private EditText editText;


        public SimpleDlg create(Context context) {
            mInstance = new SimpleDlg(context);
            mInstance.setContentView(getDefaultContentView(context));
            return mInstance;
        }

        private View getDefaultContentView(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            container = inflater.inflate(R.layout.general_dialog, null);
            return container;
        }

        public EditText getEdit() {
            EditText getEditText = (EditText) container.findViewById(R.id.edt_content);
            return getEditText;
        }

        public void setContentText(String content) {
            TextView tvContent = (TextView) container.findViewById(R.id.dlg_content_tv);
            tvContent.setText(content);
        }

        public void setOnclickListener(View.OnClickListener listener) {
            Button cancel = (Button) container.findViewById(R.id.dlg_btn_left);
            Button conform = (Button) container.findViewById(R.id.dlg_btn_right);
            cancel.setOnClickListener(listener);
            conform.setOnClickListener(listener);
        }

        public void setTitle(String title) {
            TextView tvTitle = (TextView) container.findViewById(R.id.dlg_title_tv);
            tvTitle.setText(title);
        }


        public void setETVisible() {
            editText = (EditText) container.findViewById(R.id.edt_content);
            editText.setVisibility(View.VISIBLE);
        }

        public void setTvGone() {
            TextView textView = (TextView) container.findViewById(R.id.dlg_content_tv);
            textView.setVisibility(View.GONE);
        }

    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
    }
}
