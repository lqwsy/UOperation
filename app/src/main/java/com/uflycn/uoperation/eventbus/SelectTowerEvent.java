package com.uflycn.uoperation.eventbus;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/10.
 */
public class SelectTowerEvent extends BaseMainThreadEvent {
    private Bitmap bitmap;
    private int[][] singTowers;
    private ArrayList<int[]> mList;
    private Map<String,int[]> mMap;
    public SelectTowerEvent(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public SelectTowerEvent(Bitmap bitmap, int[][] singTowers) {
        this.bitmap = bitmap;
        this.singTowers = singTowers;
    }

    public SelectTowerEvent(Bitmap bitmap, ArrayList<int[]> mList) {
        this.bitmap = bitmap;
        this.mList = mList;
    }

  public SelectTowerEvent(Bitmap bitmap, Map<String,int[]> mMap) {
        this.bitmap = bitmap;
        this.mMap = mMap;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public int[][] getSingTowers() {
        return singTowers;
    }

    public ArrayList<int[]> getList() {
        return mList;
    }

    public Map<String, int[]> getMap() {
        return mMap;
    }
}
