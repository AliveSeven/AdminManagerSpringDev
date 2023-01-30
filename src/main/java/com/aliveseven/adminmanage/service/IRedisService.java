package com.aliveseven.adminmanage.service;

import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AliveSeven
 * @since 2023-1-30
 */
public interface IRedisService {

    void setString(String key, String value);

    String getString(String key);

    void flushRedis(String key);

    void deleteByPre(String prefix);

    Set<String> getKeysByPre(String prefix);
}
