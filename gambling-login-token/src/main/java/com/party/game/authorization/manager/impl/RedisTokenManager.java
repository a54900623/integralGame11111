package com.party.game.authorization.manager.impl;


import com.party.game.common.redis.StringJedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 使用Redis存储Token
 *
 * @author ScienJus
 * @date 2015/10/26.
 */
@Service
public class RedisTokenManager extends AbstractTokenManager {

    /**
     * Redis中Key的前缀
     */
    private static final String REDIS_KEY_PREFIX = "AUTHORIZATION_KEY_";

    /**
     * Redis中Token的前缀
     */
    private static final String REDIS_TOKEN_PREFIX = "AUTHORIZATION_TOKEN_";


    @Autowired
    private StringJedis jedis;


    @Override
    protected void delSingleRelationshipByKey(String key) {
        String token = getToken(key);
        if (token != null) {
            delete(formatKey(key), formatToken(token));
        }
    }

    @Override
    public void delRelationshipByToken(String token) {
        if (singleTokenWithUser) {
            String key = getKey(token);
            delete(formatKey(key), formatToken(token));
        } else {
            delete(formatToken(token));
        }
    }

    @Override
    protected void createSingleRelationship(String key, String token) {
        String oldToken = get(formatKey(key));
        if (oldToken != null) {
            delete(formatToken(oldToken), formatKey(key));
        }
        if (null == tokenExpireSeconds) {
            //set(formatKey(key), token);
            set(formatToken(token), key);
        } else {
            //set(formatKey(key), token, tokenExpireSeconds);
            set(formatToken(token), key, tokenExpireSeconds);
        }

    }

    @Override
    protected void createMultipleRelationship(String key, String token) {
        if (null == tokenExpireSeconds) {
            set(formatToken(token), key);
        } else {
            set(formatToken(token), key, tokenExpireSeconds);
        }
    }

    @Override
    protected String getKeyByToken(String token) {
        return get(formatToken(token));
    }

    @Override
    protected void flushExpireAfterOperation(String key, String token) {
        if (singleTokenWithUser) {
            expire(formatKey(key), tokenExpireSeconds);
        }
        expire(formatToken(token), tokenExpireSeconds);
    }

    private String get(String key) {
        return jedis.getValue(key);
    }

    private void set(String key, String value, long expireSeconds) {
        jedis.setValue(key, value, expireSeconds);
    }

    private void set(String key, String value) {
        jedis.setValue(key, value);
    }

    private void expire(String key, long seconds) {
        jedis.expire(key, seconds);
    }

    private void delete(String... keys) {
        for (String key : keys) {
            jedis.delete(key);
        }
    }

    public String getToken(String key) {
        return get(formatKey(key));
    }

    private String formatKey(String key) {
        return REDIS_KEY_PREFIX.concat(key);
    }

    private String formatToken(String token) {
        return REDIS_TOKEN_PREFIX.concat(token);
    }
}
