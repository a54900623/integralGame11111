package com.party.game.mobile.web.dto.login.out;

/**
 * 登陆用户
 * party
 * Created by wei.li
 * on 2016/9/26 0026.
 */
public class CurrentUser {

    //主键
    private String id;

    //第三方登录id
    private String openId;

    //登陆名
    private String loginName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
