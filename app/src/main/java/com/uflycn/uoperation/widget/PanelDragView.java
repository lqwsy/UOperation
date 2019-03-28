package com.uflycn.uoperation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.uflycn.uoperation.R;


/**
 * Created by user on 2017/6/12.
 */
public class PanelDragView extends LinearLayout{
    private FrameLayout mDragPanel;
    private FrameLayout mDragContent;
    public PanelDragView(Context context) {
        this(context,null);
    }

    public PanelDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(!isInEditMode()){
            mDragContent = (FrameLayout)findViewById(R.id.drag_content);
            mDragPanel = (FrameLayout)findViewById(R.id.drag_panel);
        }
    }
    public void setContent(View content){
        mDragContent.removeAllViews();
        if(content!= null)
        mDragContent.addView(content);
        postInvalidate();
    }
    public void setPanel(View panel){
        mDragPanel.removeAllViews();
        mDragPanel.addView(panel);
    }


}
