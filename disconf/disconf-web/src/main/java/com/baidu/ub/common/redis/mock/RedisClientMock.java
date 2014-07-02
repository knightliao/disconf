/**
 * Project  : dsp-core
 * Package  : com.baidu.ub.common.redis.mock
 * FileName : RedisClientMock.java
 * Copyright (c) 2014, Baidu Inc. All rights reserved.
 */
package com.baidu.ub.common.redis.mock;

import org.slf4j.Logger;

import com.baidu.ub.common.log.AopLogFactory;
import com.baidu.ub.common.redis.RedisClient;


/**
 * @description : RedisClientMock
 * 
 * @author      : WuNing
 * @email       : Wuning01@baidu.com
 * @date        : 2014年3月17日 下午5:20:23
 */

public class RedisClientMock extends RedisClient {
    private static final Logger logger = AopLogFactory.getLogger(RedisClientMock.class);
            
    /**
     * mock掉一些需要的函数
     * 后续按需要添加
     */
    
    @Override
    public boolean rpush(String key, Object value) throws Exception {
        logger.info("[Redis Client Mock : RPUSH] key:" + key + " value:" + value);
        return true;
    }
}
