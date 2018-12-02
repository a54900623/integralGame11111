package com.party.game.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by wei.li
 *
 * @date 2017/6/20 0020
 * @time 10:41
 */

@Service
public class RedisLock {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static final long TOME_OUT = 80000;

    private static final long EXPIRE = 60;

    private static final String PRE = "lock";

    private static final String VALUE = "true";

    /**
     * 获取锁
     * @param key key
     * @param timeout 时间范围内轮询锁
     * @param expire 设置锁超时时间
     * @return 是否获取锁（true/false）
     */
    public synchronized boolean lock(String key, long timeout, long expire){
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        long now = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - now < timeout){
                boolean result = connection.setNX((PRE + key).getBytes(), VALUE.getBytes());
                if (result){

                    redisTemplate.expire(key, expire, TimeUnit.SECONDS);
                    return true;
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            unlock(PRE + key);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取锁
     * @param key key
     * @return 是否获取锁（true/false）
     */
    public synchronized boolean lock(String key){
        return this.lock(key, TOME_OUT, EXPIRE);
    }

    /**
     * 释放锁
     * @param key key
     */
    public synchronized void unlock(String key){
        if (lock(key)){
            redisTemplate.delete(PRE + key);
        }
    }

}
