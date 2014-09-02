package com.baidu.disconf.ub.common.redis;

import java.util.List;

import org.apache.log4j.Logger;

import com.github.knightliao.apollo.utils.common.RandomUtil;

/**
 * 提供HA特性的Redis客户端调用callback抽象类
 * 
 * @author Zhang Xu
 */
public abstract class BaseRedisCallBack<T> implements RedisCallBack<T> {

    private static final Logger LOG = Logger.getLogger(BaseRedisCallBack.class);

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

        // 获取随机不重复的序列
        List<Integer> randomIndexs = RandomUtil.randomSerial(clients.size());

        boolean success = false;
        for (Integer index : randomIndexs) {
            RedisClient client = clients.get(index);
            long start = System.currentTimeMillis();
            try {
                result = doOperation(client);
                long end = System.currentTimeMillis();
                LOG.info("[RedisCache:" + getOptionType() + "]" + " <key:"
                        + key + "> <client: " + client.getCacheName()
                        + "> <server: " + client.getRedisServer()
                        + "> success ! (use " + (end - start) + " ms)");
                if (read) { // read=true，读取出非空即返回，否则双写
                    if (result == null) {
                        // retry another client
                        LOG.info("[RedisCache:" + getOptionType() + "]"
                                + " <key:" + key + "> <client: "
                                + client.getCacheName() + "> <server: "
                                + client.getRedisServer()
                                + "> null result... (use " + (end - start)
                                + " ms)");
                        continue;
                    }
                    return true;
                }
                success = success || true;
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
