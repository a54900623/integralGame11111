package com.party.game.admin.web.security;

import com.party.game.admin.web.util.RealmUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 自定义登陆凭证验证
 * party
 * Created by wei.li
 * on 2016/10/28 0028.
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        UsernamePasswordToken passwordToken = (UsernamePasswordToken) token;
        //账户凭证
        String accountCredentials = (String) getCredentials(info);
        //令牌凭证
        String password = String.valueOf(passwordToken.getPassword());

        return RealmUtils.validatePassword(password, accountCredentials);
    }
}
