package com.baidu.disconf2.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * 可用于Spring的配置
 * 
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class DisconfMgrBean implements BeanFactoryPostProcessor,
        PriorityOrdered {

    /**
     * 
     */
    private String scanPackage = null;

    /**
     * 
     */
    public void destory() {

        DisconfMgr.close();
    }

    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        DisconfMgr.firstScan(scanPackage);
    }

}
