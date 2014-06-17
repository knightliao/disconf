package com.baidu.disconf2.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf2.demo.model.BaoBaoService;
import com.baidu.disconf2.demo.model.SimpleRedisService;

/**
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
@Service
public class DisconfDemoTask {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfDemoTask.class);

    @Autowired
    private BaoBaoService baoBaoService;

    @Autowired
    private SimpleRedisService simpleRedisService;

    private static final String REDIS_KEY = "disconf_key";

    /**
     * 
     */
    public int run() {

        try {

            while (true) {

                LOGGER.info("baobao--baifa: " + baoBaoService.calcBaiFa());
                LOGGER.info("baobao--yuerbao: " + baoBaoService.calcYuErBao());

                Thread.sleep(5000);

                LOGGER.info("redis get key: " + REDIS_KEY + " , "
                        + simpleRedisService.getKey(REDIS_KEY));
            }

        } catch (Exception e) {

            LOGGER.error(e.toString(), e);
        }

        return 0;
    }
}
