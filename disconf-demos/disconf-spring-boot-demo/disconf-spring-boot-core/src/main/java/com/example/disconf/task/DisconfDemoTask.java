package com.example.disconf.task;

import com.example.disconf.config.SimpleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 定时输出配置修改
 */
public class DisconfDemoTask implements Runnable{

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfDemoTask.class);

    private SimpleConfig simpleConfig;

    public DisconfDemoTask(SimpleConfig simpleConfig){
        this.simpleConfig = simpleConfig;
    }

    public void run() {
        try {

            while (true) {
                Thread.sleep(5000);
                LOGGER.info("simple config ==> host:{}, port:{} ",simpleConfig.getHost(),simpleConfig.getPort());
            }

        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }
    }
}