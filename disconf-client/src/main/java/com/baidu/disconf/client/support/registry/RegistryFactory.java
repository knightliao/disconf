package com.baidu.disconf.client.support.registry;

import org.springframework.context.ApplicationContext;

import com.baidu.disconf.client.support.registry.impl.SpringRegistry;

/**
 * Created by knightliao on 15/11/26.
 */
public class RegistryFactory {

    /**
     *
     */
    public static Registry getSpringRegistry(ApplicationContext applicationContext) throws Exception {

        SpringRegistry registry = new SpringRegistry();
        registry.setApplicationContext(applicationContext);

        return registry;
    }
}
