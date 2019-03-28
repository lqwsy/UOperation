package com.uflycn.uoperation.widget;

import android.graphics.drawable.Drawable;

import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;

/**
 * Created by Administrator on 2017/9/18.
 */
public class ImageMarkerSymbol extends PictureMarkerSymbol{
    public ImageMarkerSymbol(Drawable drawable) {
        super(drawable);
    }

    @Override
    public void setHeight(float v) {
        super.setHeight(v);
    }

    @Override
    public void setWidth(float v) {
        super.setWidth(v);
    }


}
