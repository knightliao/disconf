package com.baidu.dsp.common.listener;

import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        logger.info("Load StartupListener start...");

        try {

            Locale.setDefault(Locale.SIMPLIFIED_CHINESE);

        } catch (Throwable t) {
            logger.error(t.getMessage(), t);

            System.exit(-1);
        }

        logger.info("Load StartupListener end...");
    }
}
