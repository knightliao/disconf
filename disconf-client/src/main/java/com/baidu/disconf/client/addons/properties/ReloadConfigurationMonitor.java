package com.baidu.disconf.client.addons.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有两种监控方式
 * 1. 使用timer, 定时进行check
 * 2. 当事件触发时，直调用 ReloadConfigurationMonitor.reload
 */
public class ReloadConfigurationMonitor extends TimerTask {

    protected static final Logger logger = LoggerFactory.getLogger(ReloadConfigurationMonitor.class);

    private static List<ReconfigurableBean> reconfigurableBeans = new ArrayList<ReconfigurableBean>();

    public static void addReconfigurableBean(ReconfigurableBean reconfigurableBean) {
        reconfigurableBeans.add(reconfigurableBean);
    }

    public void run() {
        reload();
    }

    public static void reload() {

        for (ReconfigurableBean bean : reconfigurableBeans) {
            try {
                bean.reloadConfiguration();
            } catch (Exception e) {
                logger.warn("while reloading configuration of " + bean, e);
            }
        }
    }
}
