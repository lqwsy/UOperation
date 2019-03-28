package com.uflycn.uoperation.util;

import com.uflycn.uoperation.service.UpdateService;

public class ProjectManageUtil {


    public static boolean isShanDong() {
        return UpdateService.APP_NAME.contains("山东");
    }

    public static boolean isGanZhou() {
        return UpdateService.APP_NAME.contains("赣州");
    }

    public static boolean isGanXi() {
        return UpdateService.APP_NAME.contains("赣西");
    }

    public static boolean isJiuJiang() {
        return UpdateService.APP_NAME.contains("九江");
    }

    public static boolean isNingBo() {
        return UpdateService.APP_NAME.contains("宁波");
    }

    public static boolean isNanChang() {
        return UpdateService.APP_NAME.contains("南昌");
    }

    public static boolean isYiChun() {
        return UpdateService.APP_NAME.contains("宜春");
    }

    public static boolean isYongChuan() {
        return UpdateService.APP_NAME.contains("永川");
    }

    public static boolean isPingXiang(){
        return UpdateService.APP_NAME.contains("萍乡");
    }


}
