package com.example.disconf.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.disconf.demo.config.JedisConfig;
import com.example.disconf.demo.service.BaoBaoService;
import com.example.disconf.demo.service.SimpleRedisService;
import com.example.disconf.demo.service.SimpleStaticService;

/**
 * 演示分布式配置文件、分布式配置的更新Demo
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
@Service
public class DisconfDemoTask {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfDemoTask.class);

    @Autowired
    private BaoBaoService baoBaoService;

    @Autowired
    private SimpleRedisService simpleRedisService;

    @Autowired
    private JedisConfig jedisConfig;

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

                // LOGGER.info("redis( " + jedisConfig.getHost() + ","
                // + jedisConfig.getPort() + ")  get key: " + REDIS_KEY
                // + " , " + simpleRedisService.getKey(REDIS_KEY));

                LOGGER.info("redis( " + jedisConfig.getHost() + "," + jedisConfig.getPort() + ")");

                LOGGER.info("static file data:" + SimpleStaticService.getStaticFileData());

                LOGGER.info("static item data:" + SimpleStaticService.getStaticItem());

            }

        } catch (Exception e) {

            LOGGER.error(e.toString(), e);
        }

        return 0;
    }
}
