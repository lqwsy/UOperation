package com.uflycn.uoperation.util;

import com.uflycn.uoperation.bean.ImagePaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/6.
 */
public class MoreImageUtils {

    /**
     * 将listMap 转换成List<>
     * @param datas
     * @param category
     * @param LocalId
     * @param PlatformId
     */
    public static List<ImagePaths> mapToList(List<Map<String, Object>> datas, int category, long LocalId, int PlatformId){
        List<ImagePaths> result = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImagePaths imagePath = new ImagePaths();
            imagePath.setCategory(category);
            imagePath.setLocalId(LocalId+"");
            imagePath.setFatherPlatformId(PlatformId+"");
            imagePath.setPath((String) datas.get(i).get("path"));
            result.add(imagePath);
        }
        return result;
    }
}
