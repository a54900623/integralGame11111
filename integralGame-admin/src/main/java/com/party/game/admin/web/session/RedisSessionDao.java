package com.party.admin.web.session;

import com.party.game.common.redis.SessionJedis;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * redis 实现session存储
 * Created by wei.li
 *
 * @date 2017/4/21 0021
 * @time 9:29
 */

@Component(value = "redisSessionDao")
public class RedisSessionDao extends AbstractSessionDAO {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionJedis sessionJedis;


    /**
     * 创建session
     *
     * @param session session
     * @return sessionId
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        try {
            sessionJedis.setValue(session.getId().toString(), session);
        } catch (Exception e) {
            logger.error("session创建异常", e.getMessage());
        }
        return sessionId;
    }

    /**
     * 获取session
     *
     * @param serializable sessionId
     * @return session 信息
     */
    @Override
    protected Session doReadSession(Serializable serializable) {
        Session session = null;

        try {
            session = sessionJedis.getValue(serializable.toString());
        } catch (Exception e) {
            logger.error("获取session异常", e.getMessage());
        }
        return session;
    }

    /**
     * 更新session信息
     *
     * @param session session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        try {
            sessionJedis.setValue(session.getId().toString(), session);
        } catch (Exception e) {
            logger.error("更新session信息异常{}", e.getMessage());
        }
    }

    /**
     * 删除session信息
     *
     * @param session session信息
     */
    @Override
    public void delete(Session session) {
        try {
            sessionJedis.delete(session.getId().toString());
        } catch (Exception e) {
            logger.error("删除session信息异常", e);
        }
    }

    /**
     * 获取存活的session
     *
     * @return session集合
     */
    @Override
    public Collection<Session> getActiveSessions() {
        return Collections.EMPTY_SET;
    }
}
