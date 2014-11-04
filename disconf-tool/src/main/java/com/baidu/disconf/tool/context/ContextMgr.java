/**
 * Project  : disconf-client
 * Package  : com.baidu.disconf.client.context
 * FileName : ContextMgr.java
 * Copyright (c) 2014, Baidu Inc. All rights reserved.
 */
package com.baidu.disconf.tool.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.apollo.redis.RedisClient;
import com.github.knightliao.apollo.utils.data.JsonUtils;

/**
 * @description : ContextMgr 通过一个Redis服务器保存主机的Context信息
 * 
 * @author : WuNing
 * @email : Wuning01@baidu.com
 * @date : 2014年7月29日 下午3:56:11
 */

public class ContextMgr {

    Logger logger = LoggerFactory.getLogger(ContextMgr.class);

    RedisClient client = null;

    /**
     * 
     * @description: 通过一个Redis服务，初始化ContextMgr
     * 
     * @param redisServer
     * @param port
     * @param authKey
     * @param timeOut
     */
    public ContextMgr(String redisServer, int port, String authKey, int timeOut) {

        client = new RedisClient();
        client.setRedisServerHost(redisServer);
        client.setRedisServerPort(port);
        client.setRedisAuthKey(authKey);
        client.setTimeout(timeOut);

        client.afterPropertiesSet();
        client.flushall();
    }

    /**
     * 远程保存一个环境变量的值
     * 
     * @param key
     * @param obj
     */
    public void save(String key, String field, Object obj) {
        String str = JsonUtils.toJson(obj);
        try {
            client.hput(key, field, str);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("[Disconf Context Save]" + key + ":" + field + " : " + str);
    }

    /**
     * 加载一个环境变量的值 当值加载失败后，返回默认值
     * 
     * @param key
     * @param clz
     * @param defaultVal 默认值，可以为空，要求类型就是clz，
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object load(String key, String field, Class clz, Object defaultVal) {

        String jsonStr = (String) client.hget(key, field);

        logger.info("[Disconf Context Load]" + key + ":" + field + " : " + jsonStr);

        Object ret = JsonUtils.json2Object(jsonStr, clz);

        // json2Object return "" when error happens
        if ("".equals(ret)) {
            ret = defaultVal;
        }

        return ret;
    }

}
