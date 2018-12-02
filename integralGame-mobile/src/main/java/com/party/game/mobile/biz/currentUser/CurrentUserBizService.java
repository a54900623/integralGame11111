package com.party.game.mobile.biz.currentUser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.party.game.authorization.manager.impl.RedisTokenManager;
import com.party.game.authorization.repository.UserModelRepository;
import com.party.game.authorization.utils.Constant;
import com.party.game.common.utils.AESUtil;
import com.party.game.mobile.utils.RealmUtils;
import com.party.game.mobile.web.dto.login.out.CurrentUser;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆用户业务接口服务
 * party
 * Created by yifeng
 * on 2016/10/24 0024.
 */

@Service
public class CurrentUserBizService implements UserModelRepository {

    @Autowired
    private RedisTokenManager redisTokenManager;


    /**
     * 从token中获取当前登录用户
     *
     * @param token 当前用户id
     * @return
     */
    @Override
    public CurrentUser getCurrentUser(String token) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        String value = redisTokenManager.getKey(token);
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        token = AESUtil.decrypt(token, value);
        JSONObject jsonObject = JSON.parseObject(token);
        CurrentUser currentUser = JSONObject.toJavaObject(jsonObject, CurrentUser.class);
        return currentUser;
    }

    /**
     * 从token中获取当前登录用户
     *
     * @param token 当前用户token
     * @return
     */
    public CurrentUser getCurrentUserByToken(String token) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }
        String key = redisTokenManager.getKey(token);
        token = AESUtil.decrypt(token, key);
        JSONObject jsonObject = JSON.parseObject(token);
        CurrentUser currentUser = JSONObject.toJavaObject(jsonObject, CurrentUser.class);
        return currentUser;
    }


    /**
     * 获取当前登陆用户
     *
     * @param request 请求参数
     * @return 当前登陆用户
     */
    public CurrentUser getCurrentUser(HttpServletRequest request) {
        CurrentUser currentUser = this.getCurrentUser();
        if (null == currentUser) {
            String token = request.getHeader(Constant.HTTP_HEADER_NAME);
            return this.getCurrentUser(token);
        }
        return currentUser;
    }


    /**
     * 获取当前用户（shior）
     *
     * @return 当前登录用户
     */
    private CurrentUser getCurrentUser() {
        Session session = RealmUtils.getSession();
        if (null != session) {

        }
        return null;

    }
}
