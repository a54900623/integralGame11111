package com.party.game.mobile.web.dto.login.input;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 登陆输入视图
 * party
 * Created by wei.li
 * on 2016/9/22 0022.
 */
public class LoginInput {

    //用户名
    @NotBlank(message = "用户名不能为空")
    private String username;

    //密码
    @NotBlank(message = "密码不能为空")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
