package com.aliveseven.adminmanage.service.impl;
import com.aliveseven.adminmanage.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements IRedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * set redis: string类型
     * @param key key
     * @param value value
     */
    public void setString(String key, String value){
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * get redis: string类型
     * @param key key
     * @return
     */
    public String getString(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 清空缓存
     * get redis: string类型
     * @param key key
     * @return
     */
    public void flushRedis(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 根据模糊查询的key
     * @param prefix
     * @return keys
     */
    public Set<String> getKeysByPre(String prefix){
        return stringRedisTemplate.keys(prefix + "*");
    }

    /**
     * 根据模糊查询的key进行批量删除
     * @param prefix
     * @return
     */
    public void deleteByPre(String prefix){
        Set<String> keys = stringRedisTemplate.keys(prefix + "*");
        stringRedisTemplate.delete(keys);
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) -1为无期限
     * @return true成功 false 失败
     */
    public void setStringWithTime(String key, String value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return
     */
    public long getTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }



}
