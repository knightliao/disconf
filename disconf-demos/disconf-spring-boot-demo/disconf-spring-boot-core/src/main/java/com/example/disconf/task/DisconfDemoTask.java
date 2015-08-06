package com.example.disconf.task;

import com.example.disconf.config.SimpleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 演示分布式配置文件、分布式配置的更新Demo
 *
 * @author liaoqiqi
 * @version 2014-6-17
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