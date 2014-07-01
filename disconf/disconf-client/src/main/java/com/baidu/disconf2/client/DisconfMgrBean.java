package com.baidu.disconf2.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * 第一次扫描，静态扫描
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
     * 关闭
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

    /**
     * 第一次扫描
     */
    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {

        DisconfMgr.firstScan(scanPackage);
    }
}
