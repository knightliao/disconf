package com.baidu.disconf.client.support.registry.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.support.registry.Registry;

/**
 * Created by knightliao on 15/11/26.
 */
public class SimpleRegistry implements Registry {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleRegistry.class);

    @Override
    public <T> List<T> findByType(Class<T> type, boolean newInstance) {

        List<T> ret = new ArrayList<T>(1);

        try {
            ret.add(type.newInstance());
        } catch (InstantiationException e) {

            LOGGER.error("Failed to init " + type.getSimpleName() + " " + e.toString());

        } catch (IllegalAccessException e) {

            LOGGER.error("Failed to init " + type.getSimpleName() + " " + e.toString());
        }

        return ret;
    }

    @Override
    public <T> T getFirstByType(Class<T> type, boolean newInstance) {

        List<T> list = this.findByType(type, newInstance);
        if (list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public <T> T getFirstByType(Class<T> type, boolean newInstance, boolean withProxy) {
        return getFirstByType(type, newInstance);
    }
}
