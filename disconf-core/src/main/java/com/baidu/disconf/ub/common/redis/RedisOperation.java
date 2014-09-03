package com.baidu.disconf.ub.common.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis基本命令
 * 
 * @author Zhang Xu
 */
public interface RedisOperation {

    public Object get(String key) throws Exception;

    public boolean set(String key, Object value, Integer expiration)
            throws Exception;

    public boolean set(String key, Object value) throws Exception;

    public boolean add(String key, Object value, Integer expiration)
            throws Exception;

    public boolean add(String key, Object value) throws Exception;

    public boolean exists(String key) throws Exception;

    public boolean delete(String key);

    public boolean expire(String key, int seconds);

    public void hput(String key, String field, Serializable fieldValue)
            throws Exception;

    public Object hget(String key, String field);

    public boolean hdel(String key, String field) throws Exception;

    public Set<String> hKeys(String key) throws Exception;

    public List<Object> hValues(String key) throws Exception;

    public boolean hExists(String key, String field) throws Exception;

    public long hLen(String key) throws Exception;

    public Map<String, Object> hGetAll(String key) throws Exception;

    public void hmSet(String key, Map<String, Serializable> values)
            throws Exception;

    public List<Object> hmGet(String key, String... fields) throws Exception;

    public List<String> hmGetByStringSerializer(String key, String... fields)
            throws Exception;

    public boolean lpush(String key, Object value) throws Exception;

    public Object lpop(String key, Class<?> cls) throws Exception;

    public boolean rpush(String key, Object value) throws Exception;

    public Object rpop(String key, Class<?> cls) throws Exception;

    public Long incr(String key) throws Exception;
}
