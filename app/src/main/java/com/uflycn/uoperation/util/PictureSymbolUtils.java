package com.uflycn.uoperation.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Ryan on 2016/8/4.
 */
public class PictureSymbolUtils {


    public static class Builder{
        private int color = Color.BLUE;

        private int textSize = 22;


        public PictureSymbolUtils.Builder setTextColor(int color){
            this.color = color;
            return  this;
        }

        public PictureSymbolUtils.Builder setTextSize(int size) {
            this.textSize = size;
            return this;
        }


        /**
         * 把文字转为drawable
         * @param text
         * @return
         */
        public Drawable createMapBitMap(String text) {

            Paint paint = new Paint();
            paint.setColor(color);
            paint.setTextSize(textSize);
            paint.setAntiAlias(true);
            paint.setTextAlign(Paint.Align.CENTER);

            float textLength = paint.measureText(text);
            Paint.FontMetrics fm = paint.getFontMetrics();

            int width = (int) textLength + UIUtils.dp2px(10);
            int height = (int) Math.ceil(fm.bottom - fm.top) +  UIUtils.dp2px(10);

            Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas cv = new Canvas(newb);
            cv.drawColor(Color.parseColor("#00000000"));

            cv.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                    | Paint.FILTER_BITMAP_FLAG));

            cv.drawText(text, width / 2, height/2, paint);

            cv.save(Canvas.ALL_SAVE_FLAG);// 保存
            cv.restore();// 存储

            return new BitmapDrawable(newb);

        }
    }

}
