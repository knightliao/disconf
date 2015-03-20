package com.baidu.disconf.client.mybeans;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;

/**
 * 进行配置文件托管的bean: 启动时自动下载此配置；更改配置时，亦会自动下载此配置。不会进行java bean类的注入。
 *
 * @author knightliao
 */
public class DisconfMgrJustHostFileBean implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    protected static final Logger log = LoggerFactory.getLogger(DisconfMgrJustHostFileBean.class);

    private Set<String> justHostFiles = new HashSet<String>();

    @Override
    public String toString() {
        return "DisconfMgrJustHostFileBean [justHostFiles=" + justHostFiles + "]";
    }

    public Set<String> getJustHostFiles() {
        return justHostFiles;
    }

    public void setJustHostFiles(Set<String> justHostFiles) {
        this.justHostFiles = justHostFiles;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        if (justHostFiles != null) {

            for (String file : justHostFiles) {
                if (file != null) {
                    String fileName = file.trim();
                    log.info("disconf no-reloadable file: " + fileName);
                    // 添加到配置文件托管列表里
                    DisconfCenterHostFilesStore.getInstance().addJustHostFile(fileName);
                }
            }
        }
    }

}
