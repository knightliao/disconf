package com.baidu.disconf.client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import com.baidu.disconf.client.store.inner.DisconfCenterHostFilesStore;
import com.github.knightliao.apollo.utils.common.StringUtil;

/**
 * 第一次扫描，静态扫描
 *
 * @author liaoqiqi
 * @version 2014-6-17
 */
public class DisconfMgrBean implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

    /*
     * 已经废弃了，不推荐使用
     */
    @Deprecated
    private Set<String> fileList = new HashSet<String>();

    public final static String SCAN_SPLIT_TOKEN = ",";

    /**
     *
     */
    private String scanPackage = null;

    public DisconfMgrBean() {
    }

    /**
     * 关闭
     */
    @Deprecated
    public void destory() {

        DisconfMgr.close();
    }

    public void destroy() {

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
        return Ordered.HIGHEST_PRECEDENCE - 1;
    }

    /**
     * 这个函数无法达到最高优先级，例如PropertyPlaceholderConfigurer
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * 第一次扫描<br/>
     * 在Spring内部的Bean定义初始化后执行，这样是最高优先级的
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        // 为了做兼容
        DisconfCenterHostFilesStore.getInstance().addJustHostFileSet(fileList);

        List<String> scanPackList = StringUtil.parseStringToStringList(scanPackage, SCAN_SPLIT_TOKEN);
        // unique
        Set<String> hs = new HashSet<String>();
        hs.addAll(scanPackList);
        scanPackList.clear();
        scanPackList.addAll(hs);

        // 进行扫描
        DisconfMgr.firstScan(scanPackList);
    }

    @Deprecated
    public Set<String> getFileList() {
        return fileList;
    }

    @Deprecated
    public void setFileList(Set<String> fileList) {
        this.fileList = fileList;
    }
}
