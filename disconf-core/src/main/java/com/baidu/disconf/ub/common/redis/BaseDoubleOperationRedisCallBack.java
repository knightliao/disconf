package com.baidu.disconf.ub.common.redis;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * 针对多个redis实例多写多读
 * 
 * @author wangxj
 * 
 * @param <T>
 */
public abstract class BaseDoubleOperationRedisCallBack<T> implements
        RedisCallBack<T> {

    private static final Logger LOG = Logger
            .getLogger(BaseDoubleOperationRedisCallBack.class);

    private Exception e;

    private T result;

    public T getResult() {
        return result;
    }

    public Exception getException() {
        return e;
    }

    protected abstract T doOperation(RedisClient client) throws Exception;

    public final boolean doInRedis(List<RedisClient> clients, boolean read,
            Object key) {

        // 对所有的实例都执行一次操作，但只是返回第一个成功操作redis的操作返回值

        boolean success = false;
        T tmpResult = null;
        for (RedisClient client : clients) {
            long start = System.currentTimeMillis();
            try {
                tmpResult = doOperation(client);
                long end = System.currentTimeMillis();
                LOG.info("[RedisCache:" + getOptionType() + "]" + " <key:"
                        + key + "> <client: " + client.getCacheName()
                        + "> <server: " + client.getRedisServer()
                        + "> success ! (use " + (end - start) + " ms)");
                if (read && tmpResult == null) {
                    LOG.info("[RedisCache:" + getOptionType() + "]" + " <key:"
                            + key + "> <client: " + client.getCacheName()
                            + "> <server: " + client.getRedisServer()
                            + "> null result... (use " + (end - start) + " ms)");
                }
                if ((!read) || (result == null && tmpResult != null)) {
                    result = tmpResult;
                    success = success || true;
                }
            } catch (Exception e) {
                success = success || false;
                this.e = e;
                long end = System.currentTimeMillis();
                LOG.error("[[RedisCache:" + getOptionType() + "]" + " <key:"
                        + key + "> <client: " + client.getCacheName()
                        + "> <server: " + client.getRedisServer()
                        + "> fail ! (use " + (end - start) + " ms)");
            }
        }

        return success;
    }

}
