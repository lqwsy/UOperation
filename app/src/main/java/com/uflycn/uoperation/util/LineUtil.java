package com.uflycn.uoperation.util;

import android.text.TextUtils;
import android.util.Log;

import com.uflycn.uoperation.bean.Organizition;
import com.uflycn.uoperation.bean.User;
import com.uflycn.uoperation.bean.UserRole;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.greendao.OrganizitionDbHelper;
import com.uflycn.uoperation.greendao.RoleMenuDataDBHelp;
import com.uflycn.uoperation.greendao.UserRoleDBHelp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by UF_PC on 2018/6/21.
 */

public class LineUtil {

    /**
     * 查找这个人得角色，通过角色查找角色权限，通过权限查找相应得班组，将所有有权限看到得班组组成list
     *
     * @return
     */
    public static List<String> getOrganizitionList() {
        List<String> organizitions = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();
        if (AppConstant.currentUser != null) {
            User user = AppConstant.currentUser;
            //通过UserID在UserRole中找到RoleId
            if (user.getUserId() != null) {
                //一个用户可以又多个角色   userRoles当前用户得所有角色
                userRoles.addAll(UserRoleDBHelp.getInstance().getRoleIdByUserID(user.getUserId()));
                if (userRoles.size() == 0) {
                    //这里直接返回得是个没东西得班组列表，没有角色得用户 查不到任何线路
                    return organizitions;
                }
                //拿到这个人最大得权限  5 除外
                int max = getMaxAccess(userRoles);
                organizitions = getOrg(max, userRoles);
            }
        }
        return organizitions;
    }

    private static List<String> getOrg(int max, List<UserRole> userRoles) {
        List<String> organizitions = new ArrayList<>();
        switch (max) {
            case 2:
                //本部门
                organizitions.addAll(getSelf());
                break;
            case 3:
                //自己领导得部门
                organizitions.addAll(getManager());
                break;
            case 4:
                //本部门及自己领导得部门
                organizitions.addAll(getSelf());
                organizitions.addAll(getManager());
                break;
            case 9:
                //所有
                organizitions = null;
                break;
            case 8:
                //指定部门
                organizitions.addAll(getSpecify(userRoles));
                break;
            case 6:
                //本部门及子部门
                organizitions.addAll(getSelfAndSubOrg());
                break;
            default:
                break;
        }
        return organizitions;
    }


    private static List<String> getSpecify(List<UserRole> userRoles) {
        String organizition = "";
        for (UserRole userRole : userRoles) {
            organizition = RoleMenuDataDBHelp.getInstance().getOrganizationByRoleId(userRole.getRoleId(), "5d2be003-35c0-450d-ae71-ee20b1e3afe3");
            if (!StringUtils.isEmptyOrNull(organizition)) {
                break;
            }
        }
        if (TextUtils.isEmpty(organizition)) {
            return new ArrayList<>();
        }
        String[] o = organizition.split(",");
        return Arrays.asList(o);
    }

    private static List<String> getSelf() {
        List<String> organizitions = new ArrayList<>();
        Organizition organizition = OrganizitionDbHelper.getInstance().getOrganizition(AppConstant.currentUser.getDepartmentId());
        if (organizition != null) {
            organizitions.add(organizition.getOrganizationId());
        }
        return organizitions;
    }

    private static List<String> getManager() {
        List<String> organizitions = new ArrayList<>();
        List<String> org = OrganizitionDbHelper.getInstance().findManagerOrg(AppConstant.currentUser.getUserId());
        if (org != null || org.size() != 0) {
            organizitions.addAll(org);
        }
        return organizitions;
    }

    /**
     * 获取本部门和子部门
     *
     * @return
     */
    public static List<String> getSelfAndSubOrg() {
        List<String> organizitions = new ArrayList<String>();
        List<String> org = OrganizitionDbHelper.getInstance().getSelfAndSubOrg(AppConstant.currentUser.getDepartmentId());
        if (org != null || org.size() != 0) {
            organizitions.addAll(org);
        }
        StringBuffer sb = new StringBuffer();
        for (String str : organizitions) {
            Log.i("database", "orgId : " + str);
            sb.append(str).append("\n");
        }
        //        ToastUtil.show(sb.toString());
        return organizitions;
    }

    private static int getMaxAccess(List<UserRole> userRoles) {
        List<Integer> accessScope = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            if (RoleMenuDataDBHelp.getInstance().getAccessScopeByRoleId(userRole.getRoleId()) != null) {
                accessScope.add(RoleMenuDataDBHelp.getInstance().getAccessScopeByRoleId(userRole.getRoleId()));
            }
        }
        Integer max = 2;
        for (Integer i : accessScope) {
            if (i == null) {
                continue;
            }
            if (i > max) {
                max = i;
            }
        }
        return max;
    }
}
