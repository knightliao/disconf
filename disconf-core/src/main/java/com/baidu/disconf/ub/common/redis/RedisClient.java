package com.baidu.disconf.ub.common.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

import com.github.knightliao.apollo.utils.data.JsonUtils;

/**
 * ClassName: RedisConnection <br>
 * 
 * Function: 封装Jedis API，提供redis调用的操作
 * 
 * @author Zhang Xu
 */
public class RedisClient implements RedisOperation, InitializingBean,
        DisposableBean {

    protected final static Logger LOG = LoggerFactory
            .getLogger(RedisClient.class);

    private String cacheName = "default";

    private String redisServerHost = "localhost";

    private int redisServerPort = Protocol.DEFAULT_PORT;

    private String redisAuthKey;

    private JedisPool jedisPool;

    private int timeout = Protocol.DEFAULT_TIMEOUT;

    private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;

    private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;

    private boolean testOnBorrow = GenericObjectPool.DEFAULT_TEST_ON_BORROW;

    private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;

    private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;

    private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;

    private boolean testWhileIdle = GenericObjectPool.DEFAULT_TEST_WHILE_IDLE;

    private long timeBetweenEvictionRunsMillis = GenericObjectPool.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

    private int numTestsPerEvictionRun = GenericObjectPool.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

    private long minEvictableIdleTimeMillis = GenericObjectPool.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

    private long softMinEvictableIdleTimeMillis = GenericObjectPool.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

    private boolean lifo = GenericObjectPool.DEFAULT_LIFO;

    /**
     * If the pool in ObjectPool is exhausted (no available idle instances and
     * no capacity to create new ones), this method will either block
     * (WHEN_EXHAUSTED_BLOCK == 1), throw a NoSuchElementException
     * (WHEN_EXHAUSTED_FAIL == 0), or grow (WHEN_EXHAUSTED_GROW == 2 - ignoring
     * maxActive). The length of time that this method will block when
     * whenExhaustedAction == WHEN_EXHAUSTED_BLOCK is determined by the maxWait
     * property.
     */
    private byte whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;

    /**
     * init the JedisPoolObjectFactory and ObjectPool
     */
    // @PostConstruct
    public void afterPropertiesSet() {

        GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
        // maxIdle为负数时，不对pool size大小做限制，此处做限制，防止保持过多空闲redis连接
        if (this.maxIdle >= 0) {
            poolConfig.maxIdle = this.maxIdle;
        }
        poolConfig.maxWait = this.maxWait;
        if (this.whenExhaustedAction >= 0 && this.whenExhaustedAction < 3) {
            poolConfig.whenExhaustedAction = this.whenExhaustedAction;
        }
        poolConfig.testOnBorrow = this.testOnBorrow;
        poolConfig.minIdle = this.minIdle;
        poolConfig.maxActive = this.maxActive;
        poolConfig.testOnReturn = this.testOnReturn;
        poolConfig.testWhileIdle = this.testWhileIdle;
        poolConfig.timeBetweenEvictionRunsMillis = this.timeBetweenEvictionRunsMillis;
        poolConfig.numTestsPerEvictionRun = this.numTestsPerEvictionRun;
        poolConfig.minEvictableIdleTimeMillis = this.minEvictableIdleTimeMillis;
        poolConfig.softMinEvictableIdleTimeMillis = this.softMinEvictableIdleTimeMillis;
        poolConfig.lifo = this.lifo;

        if (StringUtils.isEmpty(redisAuthKey)) {

            LOG.info("use no auth mode for " + redisServerHost);
            jedisPool = new JedisPool(poolConfig, redisServerHost,
                    redisServerPort, timeout);
        } else {
            jedisPool = new JedisPool(poolConfig, redisServerHost,
                    redisServerPort, timeout, redisAuthKey);
        }

        onAfterInit(redisServerHost, redisServerPort);
    }

    protected void onAfterInit(String host, int port) {
        LOG.info("New Jedis pool <client: " + this.getCacheName()
                + "> <server: " + this.getRedisServer() + "> object created.");
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * get value<br>
     * return null if key did not exist
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public Object get(String key) throws Exception {
        byte[] data = null;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            long begin = System.currentTimeMillis();
            data = jedis.get(SafeEncoder.encode(key));
            long end = System.currentTimeMillis();
            LOG.info("getValueFromCache " + key + " ,spends: " + (end - begin)
                    + " millionseconds.");
        } catch (Exception e) {
            // do jedis.quit() and jedis.disconnect()
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return this.deserialize(data);
    }

    /**
     * value set<br>
     * The string can't be longer than 1073741824 bytes (1 GB).
     * 
     * @param key
     * @param value
     * @param expiration
     *            超时时间
     * @return false if redis did not execute the option
     */
    public boolean set(String key, Object value, Integer expiration)
            throws Exception {
        String result = "";
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();

            long begin = System.currentTimeMillis();
            if (expiration > 0) {
                result = jedis.setex(SafeEncoder.encode(key), expiration,
                        serialize(value));
            } else {
                result = jedis.set(SafeEncoder.encode(key), serialize(value));
            }
            long end = System.currentTimeMillis();
            LOG.info("set key:" + key + " spends: " + (end - begin)
                    + " millionseconds.");
            return "OK".equalsIgnoreCase(result);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }

    }

    /**
     * set a value without expiration
     * 
     * @param key
     * @param value
     * @return false if redis did not execute the option
     * @throws Exception
     */
    public boolean set(String key, Object value) throws Exception {
        return this.set(key, value, -1);
    }

    /**
     * add if not exists
     * 
     * @param key
     * @param value
     * @param expiration
     * @return false if redis did not execute the option
     * @throws Exception
     */
    public boolean add(String key, Object value, Integer expiration)
            throws Exception {
        Jedis jedis = null;

        try {

            jedis = this.jedisPool.getResource();
            long begin = System.currentTimeMillis();
            // 操作setnx与expire成功返回1，失败返回0，仅当均返回1时，实际操作成功
            Long result = jedis
                    .setnx(SafeEncoder.encode(key), serialize(value));
            if (expiration > 0) {
                result = result & jedis.expire(key, expiration);
            }
            long end = System.currentTimeMillis();
            if (result == 1L) {
                LOG.info("add key:" + key + " spends: " + (end - begin)
                        + " millionseconds.");
            } else {
                LOG.info("add key: " + key
                        + " failed, key has already exists! ");
            }

            return result == 1L;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * add if not exists
     * 
     * @param key
     * @param value
     * @return false if redis did not execute the option
     * @throws Exception
     */
    public boolean add(String key, Object value) throws Exception {
        return this.add(key, value, -1);
    }

    /**
     * Test if the specified key exists.
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public boolean exists(String key) throws Exception {
        boolean isExist = false;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            isExist = jedis.exists(SafeEncoder.encode(key));

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return isExist;
    }

    /**
     * Remove the specified keys.
     * 
     * @param key
     * @return false if redis did not execute the option
     */
    public boolean delete(String key) {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            jedis.del(SafeEncoder.encode(key));
            LOG.info("delete key:" + key);

            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * Remove the specified keys.
     * 
     * @param key
     * @return false if redis did not execute the option
     */
    public boolean expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            jedis.expire(SafeEncoder.encode(key), seconds);
            LOG.info("expire key:" + key + " time after " + seconds
                    + " seconds.");

            return true;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return false;
    }

    /**
     * Delete all the keys of all the existing databases, not just the currently
     * selected one.
     * 
     * @return false if redis did not execute the option
     */
    public boolean flushall() {
        String result = "";
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            result = jedis.flushAll();
            LOG.info("redis client name: " + this.getCacheName() + " flushall.");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return "OK".equalsIgnoreCase(result);
    }

    public void shutdown() {
        try {
            this.jedisPool.destroy();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Get the bytes representing the given serialized object.
     */
    protected byte[] serialize(Object o) {
        if (o == null) {
            // throw new NullPointerException("Can't serialize null");
            return new byte[0];
        }
        byte[] rv = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(o);
            os.close();
            bos.close();
            rv = bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Non-serializable object", e);
        }
        return rv;
    }

    /**
     * Get the object represented by the given serialized bytes.
     */
    protected Object deserialize(byte[] in) {
        Object rv = null;
        try {
            if (in != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(in);
                ObjectInputStream is = new ObjectInputStream(bis);
                rv = is.readObject();
                is.close();
                bis.close();
            }
        } catch (IOException e) {
            LOG.warn("Caught IOException decoding %d bytes of data", e);
        } catch (ClassNotFoundException e) {
            LOG.warn("Caught CNFE decoding %d bytes of data", e);
        }
        return rv;
    }

    /**
     * Get the bytes representing the given serialized object.
     */
    protected byte[] jsonSerialize(Object o) {
        byte[] res = null;
        try {
            res = JsonUtils.toJson(o).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("JsonSerialize object fail ", e);
        }
        return res;
    }

    /**
     * Get the object represented by the given serialized bytes.
     */
    protected Object jsonDeserialize(byte[] in, Class<?> cls) {
        if (in == null || in.length == 0) {
            return null;
        }
        Object res = null;
        try {
            res = JsonUtils.json2Object(new String(in, "utf-8"), cls);
        } catch (UnsupportedEncodingException e) {
            LOG.error("DeSerialize object fail ", e);
        }
        return res;
    }

    public void hput(String key, String field, Serializable fieldValue)
            throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            jedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(field),
                    serialize(fieldValue));
            LOG.info("hset key:" + key + " field:" + field);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public Object hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            byte[] value = jedis.hget(SafeEncoder.encode(key),
                    SafeEncoder.encode(field));
            LOG.info("hget key:" + key + " field:" + field);

            return deserialize(value);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return null;
    }

    public boolean hdel(String key, String field) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            long value = jedis.hdel(SafeEncoder.encode(key),
                    SafeEncoder.encode(field));
            LOG.info("hget key:" + key + " field:" + field);

            return value == 1;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public Set<String> hKeys(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            Set<byte[]> hkeys = jedis.hkeys(SafeEncoder.encode(key));
            LOG.info("hkeys key:" + key);
            if (CollectionUtils.isEmpty(hkeys)) {
                return new HashSet<String>(1);
            } else {
                Set<String> keys = new HashSet<String>(hkeys.size());
                for (byte[] bb : hkeys) {
                    keys.add(SafeEncoder.encode(bb));
                }
                return keys;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public List<Object> hValues(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            List<byte[]> hvals = jedis.hvals(SafeEncoder.encode(key));
            LOG.info("hvals key:" + key);
            if (CollectionUtils.isEmpty(hvals)) {
                return new ArrayList<Object>(1);
            } else {
                List<Object> ret = new ArrayList<Object>(hvals.size());
                for (byte[] bb : hvals) {
                    ret.add(deserialize(bb));
                }
                return ret;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public boolean hExists(String key, String field) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            boolean ret = jedis.hexists(SafeEncoder.encode(key),
                    SafeEncoder.encode(field));
            LOG.info("hexists key:" + key + " field:" + field);

            return ret;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public long hLen(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            long ret = jedis.hlen(SafeEncoder.encode(key));
            LOG.info("hlen key:" + key);

            return ret;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    private Map<String, Object> decodeMap(final Map<byte[], byte[]> values) {
        if (MapUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<byte[], byte[]> copy = new HashMap<byte[], byte[]>(values);
        Iterator<Entry<byte[], byte[]>> iterator = copy.entrySet().iterator();
        Map<String, Object> ret = new HashMap<String, Object>();
        while (iterator.hasNext()) {
            Entry<byte[], byte[]> next = iterator.next();
            ret.put(SafeEncoder.encode(next.getKey()),
                    deserialize(next.getValue()));
        }

        return ret;
    }

    public Map<String, Object> hGetAll(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            Map<byte[], byte[]> hgetAll = jedis
                    .hgetAll(SafeEncoder.encode(key));
            LOG.info("hgetAll key:" + key);

            return decodeMap(hgetAll);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    private Map<byte[], byte[]> encodeMap(final Map<String, Serializable> values) {
        if (MapUtils.isEmpty(values)) {
            return Collections.emptyMap();
        }
        Map<String, Serializable> copy = new HashMap<String, Serializable>(
                values);
        Iterator<Entry<String, Serializable>> iterator = copy.entrySet()
                .iterator();
        Map<byte[], byte[]> ret = new HashMap<byte[], byte[]>();
        while (iterator.hasNext()) {
            Entry<String, Serializable> next = iterator.next();
            ret.put(SafeEncoder.encode(next.getKey()),
                    serialize(next.getValue()));
        }

        return ret;
    }

    public void hmSet(String key, Map<String, Serializable> values)
            throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            jedis.hmset(SafeEncoder.encode(key), encodeMap(values));
            LOG.info("hmSet key:" + key + " field:" + values.keySet());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    private byte[][] encodeArray(final String[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return new byte[0][0];
        }
        int len = array.length;
        List<byte[]> list = new ArrayList<byte[]>(len);
        for (int i = 0; i < len; i++) {
            list.add(SafeEncoder.encode(array[i]));
        }
        return list.toArray(new byte[len][0]);
    }

    public List<Object> hmGet(String key, String... fields) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            List<byte[]> hmget = jedis.hmget(SafeEncoder.encode(key),
                    encodeArray(fields));
            LOG.info("hmGet key:" + key + " fields:" + Arrays.toString(fields));
            if (CollectionUtils.isEmpty(hmget)) {
                return new ArrayList<Object>(1);
            } else {
                List<Object> ret = new ArrayList<Object>(hmget.size());
                for (byte[] bb : hmget) {
                    ret.add(deserialize(bb));
                }
                return ret;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public List<String> hmGetByStringSerializer(String key, String... fields)
            throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            List<String> hmget = jedis.hmget(key, fields);
            LOG.info("hmGet key:" + key + " fields:" + Arrays.toString(fields));
            if (CollectionUtils.isEmpty(hmget)) {
                return new ArrayList<String>(1);
            } else {
                return hmget;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    public void destroy() throws Exception {
        this.jedisPool.destroy();

    }

    public String getRedisServerHost() {
        return redisServerHost;
    }

    public void setRedisServerHost(String redisServerHost) {
        this.redisServerHost = redisServerHost;
    }

    public String getRedisAuthKey() {
        return redisAuthKey;
    }

    public void setRedisAuthKey(String redisAuthKey) {
        this.redisAuthKey = redisAuthKey;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public byte getWhenExhaustedAction() {
        return whenExhaustedAction;
    }

    public void setWhenExhaustedAction(byte whenExhaustedAction) {
        this.whenExhaustedAction = whenExhaustedAction;
    }

    public int getRedisServerPort() {
        return redisServerPort;
    }

    public void setRedisServerPort(int redisServerPort) {
        this.redisServerPort = redisServerPort;
    }

    /**
     * @return the testOnBorrow
     */
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * @param testOnBorrow
     *            the testOnBorrow to set
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * @return the minIdle
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * @param minIdle
     *            the minIdle to set
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    /**
     * @return the maxActive
     */
    public int getMaxActive() {
        return maxActive;
    }

    /**
     * @param maxActive
     *            the maxActive to set
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    /**
     * @return the testOnReturn
     */
    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    /**
     * @param testOnReturn
     *            the testOnReturn to set
     */
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    /**
     * @return the testWhileIdle
     */
    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    /**
     * @param testWhileIdle
     *            the testWhileIdle to set
     */
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    /**
     * @return the timeBetweenEvictionRunsMillis
     */
    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * @param timeBetweenEvictionRunsMillis
     *            the timeBetweenEvictionRunsMillis to set
     */
    public void setTimeBetweenEvictionRunsMillis(
            long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * @return the numTestsPerEvictionRun
     */
    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    /**
     * @param numTestsPerEvictionRun
     *            the numTestsPerEvictionRun to set
     */
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    /**
     * @return the minEvictableIdleTimeMillis
     */
    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    /**
     * @param minEvictableIdleTimeMillis
     *            the minEvictableIdleTimeMillis to set
     */
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * @return the softMinEvictableIdleTimeMillis
     */
    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    /**
     * @param softMinEvictableIdleTimeMillis
     *            the softMinEvictableIdleTimeMillis to set
     */
    public void setSoftMinEvictableIdleTimeMillis(
            long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    /**
     * @return the lifo
     */
    public boolean isLifo() {
        return lifo;
    }

    /**
     * @param lifo
     *            the lifo to set
     */
    public void setLifo(boolean lifo) {
        this.lifo = lifo;
    }

    public String getRedisServer() {
        return redisServerHost + ":" + redisServerPort;
    }

    public static void main(String[] args) throws Exception {

        final RedisClient client = new RedisClient();
        client.setRedisServerHost("10.81.31.95");
        client.setRedisServerPort(16379);
        // client.setRedisAuthKey("123456");
        client.afterPropertiesSet();
        client.flushall();

        // test hash set
        String key = "SessionKey";
        client.hput(key, "F", "100");
        Object value = client.hget(key, "F");
        if ("100".equals(value)) {
            System.out.println("hput ok");
            System.out.println("hget ok");
        }
        // test hdel, hexist
        boolean exist = client.hExists(key, "F");
        if (exist) {
            System.out.println("hexist ok");
        }

        client.hdel(key, "F");
        exist = client.hExists(key, "F");
        if (!exist) {
            System.out.println("hdel ok");
            System.out.println("hexist ok");
        }

        // test expire

        client.hput(key, "B", "10000");
        client.expire(key, 1);
        exist = client.hExists(key, "B");
        if (exist) {
            System.out.println("hexist ok");
        }

        try {
            Thread.sleep(1500);
        } catch (Exception e) {
        }
        exist = client.hExists(key, "B");
        if (!exist) {
            System.out.println("expire ok");
        }

        /**
         * test set
         */
        System.out.println("test method set:");
        System.out.println("client.set(\"mykey\", \"value\"): "
                + client.set("mykey", "value"));
        System.out.println("client.get(\"mykey\"): " + client.get("mykey"));
        System.out.println("client.set(\"mykey\", \"valuemodified\"): "
                + client.set("mykey", "valuemodified"));
        System.out.println("client.get(\"mykey\"): " + client.get("mykey"));

        /**
         * test null value
         */
        System.out.println("test value is null:");
        System.out.println("client.set(\"nullkey\", null): "
                + client.set("nullkey", null));
        System.out.println("client.get(\"nullkey\"): " + client.get("nullkey"));
        System.out.println("client.exists(\"nullkey\"): "
                + client.exists("nullkey"));

        /**
         * test add
         */
        System.out.println("test method add:");
        System.out.println("client.add(\"mykeyadd\", \"value\"): "
                + client.add("mykeyadd", "value"));
        System.out.println("client.get(\"mykeyadd\"): "
                + client.get("mykeyadd"));
        System.out.println("client.add(\"mykeyadd\", \"valuemodified\"): "
                + client.add("mykeyadd", "valuemodified"));
        System.out.println("client.get(\"mykeyadd\"): "
                + client.get("mykeyadd"));

        /**
         * test exists
         */
        System.out.println("test method exists:");
        System.out
                .println("client.exists(\"mykey\") " + client.exists("mykey"));
        System.out.println("client.exists(\"mykeynotexist\"): "
                + client.exists("mykeynotexist"));

        System.out.println("test string about \"nil\":");
        System.out.println("client.set(\"nilkey\", \"nil\"): "
                + client.set("nilkey", "nil"));
        System.out.println("client.get(\"nilkey\"): " + client.get("nilkey"));
        System.out.println("get a key not exist(return \"nil\" from redis): "
                + client.get("nilkey2"));

        /**
         * test set list
         */
        System.out.println("test set List:");
        List<String> alist = new ArrayList<String>();
        alist.add("a");
        alist.add("b");
        alist.add("c");
        System.out.println("set list: " + client.set("listkey", alist));
        Object o = client.get("listkey");
        System.out.println("get list: " + o);

        /**
         * test set Set
         */
        System.out.println("test set Set:");
        Set<String> emptySet = new HashSet<String>();
        System.out.println("set a empty HashSet: "
                + client.set("emptysetkey", emptySet));
        Object emptySetO = client.get("emptysetkey");
        System.out.println("get a empty HashSet: " + emptySetO);

        Set<String> set = new HashSet<String>();
        set.add("a");
        set.add("b");
        set.add("c");
        System.out.println("set a HashSet: " + client.set("setkey", set));
        Object setO = client.get("setkey");
        System.out.println("get a HashSet: " + setO);

        /**
         * test concurrent
         */
        System.out.println("test concurrent: ");
        final CountDownLatch concurrent = new CountDownLatch(1);
        for (int i = 0; i < 5000; i++) {
            final int j = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("Thread " + j + " ok!");
                        concurrent.await();
                        System.out.println("client.set(\"testConCurrent"
                                + j
                                + ",testConCurrent"
                                + j
                                + "\"): "
                                + client.set("testConCurrent" + j,
                                        "testConCurrent" + j));
                        System.out.println("client.get(\"testConCurrent" + j
                                + ")" + client.get("testConCurrent" + j));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        concurrent.countDown();

    }

    @Override
    public boolean lpush(String key, Object value) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();

            long begin = System.currentTimeMillis();
            jedis.lpush(SafeEncoder.encode(key), jsonSerialize(value));
            long end = System.currentTimeMillis();
            LOG.info("lpush key:" + key + " spends: " + (end - begin)
                    + " millionseconds.");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return true;
    }

    public Object lpop(String key, Class<?> cls) throws Exception {
        byte[] data = null;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            long begin = System.currentTimeMillis();
            data = jedis.lpop(SafeEncoder.encode(key));
            long end = System.currentTimeMillis();
            LOG.info("getValueFromCache spends: " + (end - begin)
                    + " millionseconds.");
        } catch (Exception e) {
            // do jedis.quit() and jedis.disconnect()
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return this.jsonDeserialize(data, cls);
    }

    public boolean rpush(String key, Object value) throws Exception {
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();

            long begin = System.currentTimeMillis();
            jedis.rpush(SafeEncoder.encode(key), jsonSerialize(value));
            long end = System.currentTimeMillis();
            LOG.info("rpush key:" + key + " spends: " + (end - begin)
                    + " millionseconds.");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
        return true;
    }

    public Object rpop(String key, Class<?> cls) throws Exception {
        byte[] data = null;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            long begin = System.currentTimeMillis();
            data = jedis.rpop(SafeEncoder.encode(key));
            long end = System.currentTimeMillis();
            LOG.info("getValueFromCache spends: " + (end - begin)
                    + " millionseconds.");
        } catch (Exception e) {
            // do jedis.quit() and jedis.disconnect()
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return this.jsonDeserialize(data, cls);
    }

    public Long incr(String key) throws Exception {
        Long data = null;
        Jedis jedis = null;
        try {
            jedis = this.jedisPool.getResource();
            long begin = System.currentTimeMillis();
            data = jedis.incr(SafeEncoder.encode(key));
            long end = System.currentTimeMillis();
            LOG.info("getValueFromCache spends: " + (end - begin)
                    + " millionseconds.");
        } catch (Exception e) {
            // do jedis.quit() and jedis.disconnect()
            this.jedisPool.returnBrokenResource(jedis);
            throw e;
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }

        return data;
    }

}
