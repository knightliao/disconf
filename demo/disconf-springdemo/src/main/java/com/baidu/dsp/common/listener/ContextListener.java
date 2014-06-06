package com.baidu.dsp.common.listener;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.baidu.disconf2.demo.main.AnnotationScan;
import com.baidu.disconf2.demo.main.Inject;

public class ContextListener extends ContextLoaderListener {

    private static final Logger logger = LoggerFactory
            .getLogger(ContextListener.class);

    private static boolean isInitialized = false;

    public void contextDestroyed(ServletContextEvent arg0) {
        super.contextDestroyed(arg0);
    }

    public void contextInitialized(ServletContextEvent arg0) {

        super.contextInitialized(arg0);

        logger.info("start to load ContextListener");

        //
        // 测试Annotation
        //

        AnnotationScan.printAnnotation();

        Inject.inject2ConfClass(10);

        isInitialized = true;
        logger.info("end of ContextListener");
    }

    public static boolean isInitialized() {
        return isInitialized;
    }

}
