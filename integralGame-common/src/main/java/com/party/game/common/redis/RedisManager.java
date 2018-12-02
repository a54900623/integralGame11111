package com.party.game.common.redis;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.party.game.common.utils.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by yifeng
 *
 * @date 2017/6/26 0026
 * @time 19:45
 */
@Service
public class RedisManager<K, V> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    protected static Logger logger = LoggerFactory.getLogger(BaseRedisDao.class);

    //操作异常返回码
    private static final long CODE = 99999;
    /**
     * 缓存对象
     * @param key 缓存 key
     * @param value 缓存 value
     */
    public void setValue(String pre, K key, V value) {
        try {
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.hSet(SerializeUtils.serialize(pre),
                            SerializeUtils.serialize(key),
                            SerializeUtils.serialize(value));
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("缓存对象异常", e);
        }
    }


    /**
     * 获取缓存对象
     * @param key 缓存key
     * @return 缓存对象
     */
    public V getValue(String pre, K key){
        try {
           return  redisTemplate.execute(new RedisCallback<V>() {
               @Override
               public V doInRedis(RedisConnection redisConnection) throws DataAccessException {
                   byte[] result =  redisConnection.hGet(SerializeUtils.serialize(pre), SerializeUtils.serialize(key));
                   return (V) SerializeUtils.deserialize(result);
               }
           });

        } catch (Exception e) {
            logger.error("获取缓存对象异常", e);
        }
        return null;
    }

    /**
     * 删除
     * @param pre
     * @param key
     */
    public void remove(String pre, K key){
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hDel(SerializeUtils.serialize(pre), SerializeUtils.serialize(key));
            }
        });
    }

    /**
     * 长度
     * @param pre
     * @return
     */
    public Long size(String pre){
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hLen(SerializeUtils.serialize(pre));
            }
        });
    }


    /**
     * 清空DB
     * @return 清空结果
     */
    public void flushDB(String pre) {
        redisTemplate.delete(pre);
    }


    /**
     * 缓存key集合
     * @param pre
     * @return
     */
    public Set<K> keys(String pre){
        Set<K> valus = Sets.newHashSet();
        Set keys = redisTemplate.execute(new RedisCallback<Set>() {
            @Override
            public Set doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hKeys(SerializeUtils.serialize(pre));
            }
        });
        for (Object o : keys){
            valus.add((K) o);
        }
        return valus;
    }

    /**
     * 缓存value集合
     * @param pre
     * @return
     */
    public List<V> valus(String pre){
        List<V> values = Lists.newArrayList();
        List valueList = redisTemplate.execute(new RedisCallback<List>() {
            @Override
            public List doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hVals(SerializeUtils.serialize(pre));
            }
        });
        for (Object o : valueList){
            values.add((V) o);
        }
        return values;
    }
}
