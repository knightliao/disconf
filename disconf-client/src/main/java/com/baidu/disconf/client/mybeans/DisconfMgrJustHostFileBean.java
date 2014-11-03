package com.baidu.disconf.client.mybeans;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;

/**
 * 只是进行配置文件托管的bean
 * 
 * @author knightliao
 * 
 */
public class DisconfMgrJustHostFileBean implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    //
    // 只是进行托管的配置文件（不会注入，只负责动态推送）
    //
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
        // TODO Auto-generated method stub

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        if (justHostFiles != null) {
            DisconfCenterHostFilesStore.getInstance().addJustHostFileSet(justHostFiles);
        }

    }

}
