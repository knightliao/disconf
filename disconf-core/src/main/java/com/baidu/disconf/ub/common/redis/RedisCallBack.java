package com.baidu.disconf.ub.common.redis;

import java.util.List;

/**
 * Redis客户端调用callback接口
 * 
 * @author Zhang Xu
 */
public interface RedisCallBack<T> {

    /**
     * 具体操作实现接口
     * 
     * @param client
     * @param read
     *            是否为只读，true：查询到非空结果即返回，false：双写策略
     * @param key
     * @return boolean
     */
    boolean doInRedis(List<RedisClient> client, boolean read, Object key);

    /**
     * 操作类型
     * 
     * @return String
     */
    String getOptionType();

    /**
     * 返回结果
     * 
     * @return T
     */
    T getResult();

    /**
     * 返回异常
     * 
     * @return Exception
     */
    Exception getException();

}
