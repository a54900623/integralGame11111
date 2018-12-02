package com.party.game.mobile.utils;

import com.alibaba.fastjson.JSONObject;
import com.party.game.common.utils.AESUtil;
import com.party.game.common.utils.Digests;
import com.party.game.common.utils.Encodes;
import com.party.game.mobile.web.dto.login.out.CurrentUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * 登陆权限管理工具类
 * party
 * Created by yifeng
 * on 2016/10/24 0024.
 */
public class RealmUtils {

    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    //默认密码
    public static final String DEFALT_PASSWORD = "123456";

    /**
     * 登陆加密
     *
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(password.getBytes(), salt, HASH_INTERATIONS);
        return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
    }


    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param password      密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        byte[] salt = Encodes.decodeHex(password.substring(0, 16));
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
    }

    /**
     * 登陆用户转token
     *
     * @param currentUser 登陆用户
     * @return token值
     */
    public static String getToken(CurrentUser currentUser) {
        String token = JSONObject.toJSONString(currentUser);
        token = AESUtil.encrypt(token, currentUser.getId());
        return token;
    }


    /**
     * 获取session
     *
     * @return
     */
    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException e) {

        }
        return null;
    }


    public static void main(String[] args) {
        boolean result = validatePassword("13364020152", "8de1b721d4d148812d33fe85b85f0110d3ac2c3817feaa622580f60e");
        System.out.println(result);
    }
}
