package com.baidu.disconf.ub.common.redis;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 * ClassName: RedisCacheManager <br>
 * 
 * Function: 提供HA特性的Redis client管理类，建议配置为单例使用，供其他模块使用
 * 
 * @author Zhang Xu
 */
public class RedisCacheManager {

    private static final Logger LOG = Logger.getLogger(RedisCacheManager.class);

    // 该管理类中维护的client连接集合，每个client均是连接池化的
    private List<RedisClient> clientList;

    // 重试是指当集群中所有的服务都暂时不可用时的retry times，默认为2
    private int retryTimes = DEFALUT_RETRY_TIMES;
    private static final int DEFALUT_RETRY_TIMES = 2;

    private <T> T execute(RedisCallBack<T> redisCallBack,
            List<RedisClient> clients, Object key, boolean read) {
        for (int i = 0; i < getRetryTimes(); i++) {
            boolean result = redisCallBack.doInRedis(clients, read, key);
            if (result) {
                return redisCallBack.getResult();
            }
        }

        // throw Runtime exception
        if (redisCallBack.getException() != null) {
            throw new RuntimeException(redisCallBack.getException()
                    .getMessage(), redisCallBack.getException());
        }
        return null;
    }

    public void shutdown() {
        if (clientList == null) {
            return;
        }
        for (RedisClient connection : clientList) {
            try {
                connection.shutdown();
            } catch (Exception e) {
                LOG.debug(e.getMessage(), e);
            }
        }

    }

    public String put(final Object key, final Object obj) {
        return this.put(key, -1, obj);
    }

    public String put(final Object key, final Integer expiration,
            final Object obj) {

        List<RedisClient> clients = this.getClients(key);
        String cacheName = null;
        if (CollectionUtils.isNotEmpty(clients)) {
            cacheName = clients.get(0).getCacheName();
            this.execute(new BaseRedisCallBack<Boolean>() {
                public Boolean doOperation(RedisClient client) throws Exception {
                    return client.set(key.toString(), obj, expiration);
                }

                public String getOptionType() {
                    return "PUT";
                }
            }, clients, key, false);
        }
        return cacheName;
    }

    public Object get(final Object key) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    return client.get(key.toString());
                }

                public String getOptionType() {
                    return "GET";
                }
            }, clients, key, true);
        }
        return null;
    }

    public String remove(final Object key) {
        List<RedisClient> clients = this.getClients(key);
        String cacheName = null;
        if (CollectionUtils.isNotEmpty(clients)) {
            cacheName = clients.get(0).getCacheName();
            this.execute(new BaseRedisCallBack<Boolean>() {
                public Boolean doOperation(RedisClient client) throws Exception {
                    return client.delete(key.toString());
                }

                public String getOptionType() {
                    return "REMOVE";
                }
            }, clients, key, false);
        }
        return cacheName;
    }

    public String replace(final Object key, final Object obj) {
        return this.replace(key, -1, obj);
    }

    public String replace(final Object key, final Integer expiration,
            final Object obj) {
        return put(key, expiration, obj);
    }

    public boolean existsKey(final String key) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Boolean>() {
                public Boolean doOperation(RedisClient client) throws Exception {
                    return client.exists(key);
                }

                public String getOptionType() {
                    return "EXIST";
                }
            }, clients, key, true);
        }
        return false;
    }

    public boolean extendTime(final String key, final Integer expirationMs) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {

            return this.execute(new BaseRedisCallBack<Boolean>() {
                public Boolean doOperation(RedisClient client) throws Exception {
                    return client.expire(key, expirationMs / 1000);
                }

                public String getOptionType() {
                    return "EXPIRE";
                }
            }, clients, key, false);
        }
        return false;
    }

    public void hput(final String key, final String field,
            final Serializable fieldValue) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            this.execute(new BaseRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    client.hput(key, field, fieldValue);
                    return null;
                }

                public String getOptionType() {
                    return "HPUT";
                }
            }, clients, key, false);
        }
    }

    public Object hget(final String key, final String field) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    return client.hget(key, field);
                }

                public String getOptionType() {
                    return "HGET";
                }
            }, clients, key, true);
        }
        return null;
    }

    public boolean hdel(final String key, final String field) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Boolean>() {
                public Boolean doOperation(RedisClient client) throws Exception {
                    return client.hdel(key, field);
                }

                public String getOptionType() {
                    return "HDEL";
                }
            }, clients, key, false);
        }
        return false;
    }

    public Set<String> hKeys(final String key) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Set<String>>() {
                public Set<String> doOperation(RedisClient client)
                        throws Exception {
                    return client.hKeys(key);
                }

                public String getOptionType() {
                    return "HKEYS";
                }
            }, clients, key, true);
        }
        return Collections.emptySet();
    }

    public List<Object> hValues(final String key) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<List<Object>>() {
                public List<Object> doOperation(RedisClient client)
                        throws Exception {
                    return client.hValues(key);
                }

                public String getOptionType() {
                    return "HVALUES";
                }
            }, clients, key, true);
        }
        return Collections.emptyList();
    }

    public boolean hExists(final String key, final String field) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Boolean>() {
                public Boolean doOperation(RedisClient client) throws Exception {
                    return client.hExists(key, field);
                }

                public String getOptionType() {
                    return "HEXISTS";
                }
            }, clients, key, true);
        }
        return false;
    }

    public long hLen(final String key) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Long>() {
                public Long doOperation(RedisClient client) throws Exception {
                    return client.hLen(key);
                }

                public String getOptionType() {
                    return "HLEN";
                }
            }, clients, key, true);
        }
        return 0;
    }

    public void hmSet(final String key, final Map<String, Serializable> values) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            this.execute(new BaseRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    client.hmSet(key, values);
                    return null;
                }

                public String getOptionType() {
                    return "HMSET";
                }
            }, clients, key, false);
        }
    }

    public List<Object> hmGet(final String key, final String... fields) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<List<Object>>() {
                public List<Object> doOperation(RedisClient client)
                        throws Exception {
                    return client.hmGet(key, fields);
                }

                public String getOptionType() {
                    return "HMGET";
                }
            }, clients, key, true);
        }
        return Collections.emptyList();
    }

    public List<String> hmGetByStringSerializer(final String key,
            final String... fields) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<List<String>>() {
                public List<String> doOperation(RedisClient client)
                        throws Exception {
                    return client.hmGetByStringSerializer(key, fields);
                }

                public String getOptionType() {
                    return "HMGET-STRING_SERIAL";
                }
            }, clients, key, true);
        }
        return Collections.emptyList();
    }

    public Map<String, Object> hGetAll(final String key) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseRedisCallBack<Map<String, Object>>() {
                public Map<String, Object> doOperation(RedisClient client)
                        throws Exception {
                    return client.hGetAll(key);
                }

                public String getOptionType() {
                    return "HGETALL";
                }
            }, clients, key, true);
        }
        return Collections.emptyMap();
    }

    public void lpush(final String key, final Object value) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            this.execute(new BaseDoubleOperationRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    client.lpush(key, value);
                    return null;
                }

                public String getOptionType() {
                    return "lpush";
                }
            }, clients, key, false);
        }
    }

    public void rpush(final String key, final Object value) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            this.execute(new BaseDoubleOperationRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    client.rpush(key, value);
                    return null;
                }

                public String getOptionType() {
                    return "rpush";
                }
            }, clients, key, false);
        }
    }

    public Object lpop(final String key, final Class<?> cls) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseDoubleOperationRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    return client.lpop(key, cls);
                }

                public String getOptionType() {
                    return "lpop";
                }
            }, clients, key, true);
        }
        LOG.error("there are no redis client to use");
        return null;
    }

    public Object rpop(final String key, final Class<?> cls) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseDoubleOperationRedisCallBack<Object>() {
                public Object doOperation(RedisClient client) throws Exception {
                    return client.rpop(key, cls);
                }

                public String getOptionType() {
                    return "rpop";
                }
            }, clients, key, true);
        }
        LOG.error("there are no redis client to use");
        return null;
    }

    public Long incr(final String key) {
        List<RedisClient> clients = this.getClients(key);
        if (CollectionUtils.isNotEmpty(clients)) {
            return this.execute(new BaseDoubleOperationRedisCallBack<Long>() {
                public Long doOperation(RedisClient client) throws Exception {
                    return client.incr(key);
                }

                public String getOptionType() {
                    return "incr";
                }
            }, clients, key, true);
        }
        return 0L;
    }

    private int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        if (retryTimes < 1) {
            retryTimes = DEFALUT_RETRY_TIMES;
        }
        LOG.warn("set RedisCacheManager retry times to " + retryTimes);
        this.retryTimes = retryTimes;
    }

    public List<RedisClient> getClientList() {
        return clientList;
    }

    public List<RedisClient> getClients(Object key) {
        return getClientList();
    }

    public void setClientList(List<RedisClient> clientList) {
        this.clientList = clientList;
    }

}
