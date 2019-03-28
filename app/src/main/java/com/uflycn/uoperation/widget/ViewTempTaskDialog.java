package com.uflycn.uoperation.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.uflycn.uoperation.R;
import com.uflycn.uoperation.bean.SectionList;
import com.uflycn.uoperation.bean.TempTask;
import com.uflycn.uoperation.util.UIUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Xiong on 2017/9/28.
 */
public class ViewTempTaskDialog extends Dialog {

    private Reference<Context> mContextRef;

    public ViewTempTaskDialog(Context context) {
        this(context, R.style.Dialog);
    }

    public ViewTempTaskDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContextRef = new WeakReference<>(context);
        initView();
    }

    protected ViewTempTaskDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView() {
        View contentView = LayoutInflater.from(mContextRef.get()).inflate(R.layout.dialog_temp_task_detail, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dp2px(300),UIUtils.dp2px(150));
        setContentView(contentView, params);
    }

    public void setData(final TempTask tempTask) {
        TextView tv_title = (TextView) this.findViewById(R.id.tv_task_title);
        tv_title.setText(tempTask.getTitle());

        TextView tv = (TextView) this.findViewById(R.id.tv_task_detail);
        tv.setText(tempTask.getMessageContent());

        TableLayout table = (TableLayout) this.findViewById(R.id.tl_detail);
        appendNewRow(table, tempTask.getSectionList());

/*
*        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sectionLists.size(); i++) {
                    sb.append(sectionLists.get(i).getVoltageClass() + sectionLists.get(i).getLineName());
                    sb.append("\t");
                    sb.append(sectionLists.get(i).getStartTower() + " - " + sectionLists.get(i).getEndTower());
                    sb.append("\n\r");
                }
                subscriber.onNext(sb.toString());
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                        tv.setText(s);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });*/
    }


    private void appendNewRow(final TableLayout table, List<SectionList> sectionList) {


        for (int i = 0; i < sectionList.size(); i++) {
            TableRow row = new TableRow(getContext());
            row.setId(i);
            TextView tvVClass = new TextView(getContext());
            tvVClass.setText(sectionList.get(i).getVoltageClass());
            tvVClass.setPadding(3, 3, 3, 3);
            tvVClass.setWidth(40);
            tvVClass.setGravity(Gravity.CENTER);

            TextView tvLineName = new TextView(getContext());
            tvLineName.setText(sectionList.get(i).getLineName());
            tvLineName.setPadding(3, 3, 3, 3);
            tvLineName.setWidth(40);
            tvLineName.setGravity(Gravity.CENTER);

            TextView tvTowerNo = new TextView(getContext());
            tvTowerNo.setText(sectionList.get(i).getStartTower() + " - " + sectionList.get(i).getEndTower());
            tvTowerNo.setPadding(3, 3, 3, 3);
            tvTowerNo.setWidth(40);
            tvTowerNo.setGravity(Gravity.CENTER);

            row.addView(tvVClass);
            row.addView(tvLineName);
            row.addView(tvTowerNo);

            table.addView(row);
        }

    }
}
