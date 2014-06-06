package com.baidu.disconf2.demo.utils;

import java.util.Collection;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;

public class ReflectionsUtils {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ReflectionsUtils.class);

    /**
     * 打印出StoreMap的数据
     * 
     * @param reflections
     */
    public static void printSotreMap(Reflections reflections) {

        LOGGER.info("Now we will print store map......");

        Store store = reflections.getStore();
        Map<String/* indexName */, Multimap<String, String>> storeMap = store
                .getStoreMap();
        for (String indexName : storeMap.keySet()) {

            LOGGER.info("indexName:" + indexName);

            Multimap<String, String> multimap = storeMap.get(indexName);

            for (String firstName : multimap.keySet()) {
                Collection<String> lastNames = multimap.get(firstName);
                LOGGER.info("\t\t" + firstName + ": " + lastNames);
            }

        }
    }
}
