package com.uflycn.uoperation.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/6.
 */
@Entity(nameInDb = "operation_ImagePaths")
public class ImagePaths implements Serializable {
    //主键id

    @Id(autoincrement = true)
    private Long Id;
    //种类
    private int Category;
    //本地ID
    private String LocalId;
    //平台ID
    private String fatherPlatformId;
    //图片地址
    private String Path;
    public String getPath() {
        return this.Path;
    }
    public void setPath(String Path) {
        this.Path = Path;
    }
    public String getFatherPlatformId() {
        return this.fatherPlatformId;
    }
    public void setFatherPlatformId(String fatherPlatformId) {
        this.fatherPlatformId = fatherPlatformId;
    }
    public String getLocalId() {
        return this.LocalId;
    }
    public void setLocalId(String LocalId) {
        this.LocalId = LocalId;
    }
    public int getCategory() {
        return this.Category;
    }
    public void setCategory(int Category) {
        this.Category = Category;
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    @Generated(hash = 165219198)
    public ImagePaths(Long Id, int Category, String LocalId,
            String fatherPlatformId, String Path) {
        this.Id = Id;
        this.Category = Category;
        this.LocalId = LocalId;
        this.fatherPlatformId = fatherPlatformId;
        this.Path = Path;
    }
    @Generated(hash = 1508856819)
    public ImagePaths() {
    }
  
}
