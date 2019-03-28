package com.uflycn.uoperation.bean;

/**
 * Created by Administrator on 2017/9/6.
 */
public class LoginCallbackEntity {
    private User User;
    private String Token;

    public String getTOKEN() {
        return Token;
    }

    public void setTOKEN(String TOKEN) {
        this.Token = TOKEN;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }
}
