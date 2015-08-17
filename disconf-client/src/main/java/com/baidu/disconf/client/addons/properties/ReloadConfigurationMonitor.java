package com.baidu.disconf.client.addons.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
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
