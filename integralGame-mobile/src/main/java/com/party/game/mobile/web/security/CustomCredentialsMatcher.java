package com.party.game.mobile.web.security;

import com.party.game.mobile.utils.RealmUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 自定义登陆凭证验证
 * Created by yifeng
 *
 * @date 2017/2/14 0014
 * @time 14:07
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
