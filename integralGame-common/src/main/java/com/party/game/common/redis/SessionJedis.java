package com.party.game.common.redis;

import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

/**
 * Created by wei.li
 *
 * @date 2017/4/21 0021
 * @time 9:36
 */

@Service
public class SessionJedis extends BaseRedisDao<Session> {

    /**
     * 缓存session
     * @param key 缓存 key
     * @param session session
     */
    @Override
    public void setValue(String key, Session session) {
        super.setValue(key, session);
    }

    /**
     * 获取缓存session
     * @param key 缓存key
     * @return session
     */
    @Override
    public Session getValue(String key) {
        return super.getValue(key);
    }

    /**
     * 删除缓存对象
     * @param key 缓存key
     */
    @Override
    public void delete(String key) {
        super.delete(key);
    }
}
