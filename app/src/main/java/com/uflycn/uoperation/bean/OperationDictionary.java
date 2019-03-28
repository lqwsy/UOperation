package com.uflycn.uoperation.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */
public class OperationDictionary {
    private String Code;
    private String FullName;
    private String Id;
    private List<DictionaryChildren> Children;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<DictionaryChildren> getChildren() {
        return Children;
    }

    public void setChildren(List<DictionaryChildren> children) {
        Children = children;
    }
}
