package com.party.game.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存基类
 * party
 * Created by wei.li
 * on 2016/9/9 0009.
 */
public class BaseRedisDao<T> {

    @Autowired
    private RedisTemplate<String, T> redisTemplate;

    protected static Logger logger = LoggerFactory.getLogger(BaseRedisDao.class);

    //操作异常返回码
    private static final long CODE = 99999;
    /**
     * 缓存对象
     * @param key 缓存 key
     * @param t 缓存 value
     */
    public void setValue(String key, T t) {
        try {
            redisTemplate.opsForValue().set(key, t);
        } catch (Exception e) {
            logger.error("缓存对象异常", e);
        }
    }

    /**
     * 缓存对象
     * @param key 缓存 key
     * @param t 缓存 value
     * @param timeout 过期时间
     */
    public void setValue(String key, T t, long timeout){
        redisTemplate.opsForValue().set(key, t, timeout, TimeUnit.SECONDS);
    }


    /**
     * 获取缓存对象
     * @param key 缓存key
     * @return 缓存对象
     */
    public T getValue(String key){
        try {
           return   redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("获取缓存对象异常", e);
        }
        return null;
    }

    /**
     * 缓存一个对象
     * @param key 缓存key
     * @param t 缓存对象
     * @return 缓存返回状态值
     */
    public long listPush(String key, T t){
        try {
            return redisTemplate.opsForList().leftPush(key, t);
        } catch (Exception e) {
           logger.error("缓存一个对象异常", e);
        }
        return CODE;
    }

    /**
     * 缓存多个对象
     * @param key 缓存key
     * @param t 缓存对象
     * @return 缓存返回状态值
     */
    public long listPushAll(String key, List<T> t){
        try {
            return redisTemplate.opsForList().leftPushAll(key, t);
        } catch (Exception e) {
            logger.error("缓存多个对象异常", e);
        }
        return CODE;
    }

    /**
     * 移除从表头到表尾，最先发现的多对象
     * @param key 缓存key
     * @param count 数量
     * @param t 缓存对象
     * @return 缓存返回值
     */
    public long listRemove(String key, long count,  T t){
        try {
            return redisTemplate.opsForList().remove(key, count, t);
        } catch (Exception e) {
            logger.error("移除对象异常", e);
        }
        return CODE;
    }

    /**
     * 返回列表 key 中，下标为 index 的元素。
     * @param key 缓存key
     * @param index 缓存下标
     * @return 缓存对象
     */
    public T listIndex(String key, long index){
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error("查询对象异常", e);
        }
        return null;
    }

    /**
     * 返回列表 key 中指定区间内的元素
     * @param key 缓存key
     * @param star 偏移量 star
     * @param stop 偏移量 stop
     * @return 缓存列表
     */
    public List<T> listRang(String key, long star, long stop){
        try {
            return redisTemplate.opsForList().range(key, star, stop);
        } catch (Exception e) {
            logger.error("查询对象异常", e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 获取所以缓存列表
     * @param key 缓存key
     * @return 缓存列表
     */
    public List<T> listAll(String key){
        try {
            return redisTemplate.opsForList().range(key, 0l, -1l);
        } catch (Exception e) {
            logger.error("查询对象异常",e);
        }
        return Collections.EMPTY_LIST;
    }


    /**
     * 删除缓存
     * @param key 缓存key
     */
    public void delete(String key){
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("删除缓存异常", e);
        }
    }

    /**
     * 延长缓存时间
     * @param key 缓存key
     * @param seconds 时间（秒）
     */
    public void expire(String key, long seconds){
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }
}
