package com.baidu.disconf.client.core.processor.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.baidu.disconf.client.store.DisconfStoreProcessor;
import com.baidu.disconf.client.utils.MyBeanUtils;
import com.baidu.disconf.client.utils.SpringContextUtil;

/**
 * @author liaoqiqi
 * @version 2014-8-4
 */
public class DisconfCoreProcessUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfCoreProcessUtils.class);

    /**
     * 调用此配置影响的回调函数
     */
    public static void callOneConf(DisconfStoreProcessor disconfStoreProcessor, String key) throws Exception {

        List<IDisconfUpdate> iDisconfUpdates = disconfStoreProcessor.getUpdateCallbackList(key);

        //
        // 获取回调函数列表
        //

        // CALL
        for (IDisconfUpdate iDisconfUpdate : iDisconfUpdates) {

            if (iDisconfUpdate != null) {

                LOGGER.info("start to call " + iDisconfUpdate.getClass());

                try {

                    iDisconfUpdate.reload();

                } catch (Exception e) {

                    LOGGER.error(e.toString(), e);
                }
            }
        }
    }

    /**
     * 获取Spring Bean
     *
     * @return
     */
    public static Object getSpringBean(Class<?> cls) throws Exception {

        if (SpringContextUtil.getApplicationContext() == null) {
            LOGGER.error("Spring Context is null. Cannot autowire " + cls.getCanonicalName());
            return null;
        }

        // spring 方式
        Object object = SpringContextUtil.getBean(cls);

        return MyBeanUtils.getTargetObject(object, cls);
    }
}
