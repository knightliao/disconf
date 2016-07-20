package com.baidu.disconf.client;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.core.DisconfCoreFactory;
import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.client.scan.ScanFactory;
import com.baidu.disconf.client.scan.ScanMgr;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;
import com.baidu.disconf.client.support.registry.Registry;
import com.baidu.disconf.client.support.registry.RegistryFactory;

/**
 * Disconf Client 总入口
 *
 * @author liaoqiqi
 * @version 2014-5-23
 */
public class DisconfMgr implements ApplicationContextAware {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfMgr.class);

    // 本实例不能初始化两次
    private boolean isFirstInit = false;
    private boolean isSecondInit = false;

    // application context
    private ApplicationContext applicationContext;

    // 核心处理器
    private DisconfCoreMgr disconfCoreMgr = null;

    // scan mgr
    private ScanMgr scanMgr = null;

    protected static final DisconfMgr INSTANCE = new DisconfMgr();

    public static DisconfMgr getInstance() {
        return INSTANCE;
    }

    private DisconfMgr() {

    }

    /**
     * 总入口
     */
    public synchronized void start(List<String> scanPackageList) {

        firstScan(scanPackageList);

        secondScan();
    }

    /**
     * 第一次扫描，静态扫描 for annotation config
     */
    protected synchronized void firstScan(List<String> scanPackageList) {

        // 该函数不能调用两次
        if (isFirstInit) {
            LOGGER.info("DisConfMgr has been init, ignore........");
            return;
        }

        //
        //
        //

        try {

            // 导入配置
            ConfigMgr.init();

            LOGGER.info("******************************* DISCONF START FIRST SCAN *******************************");

            // registry
            Registry registry = RegistryFactory.getSpringRegistry(applicationContext);

            // 扫描器
            scanMgr = ScanFactory.getScanMgr(registry);

            // 第一次扫描并入库
            scanMgr.firstScan(scanPackageList);

            // 获取数据/注入/Watch
            disconfCoreMgr = DisconfCoreFactory.getDisconfCoreMgr(registry);
            disconfCoreMgr.process();

            //
            isFirstInit = true;

            LOGGER.info("******************************* DISCONF END FIRST SCAN *******************************");

        } catch (Exception e) {

            LOGGER.error(e.toString(), e);
        }
    }

    /**
     * 第二次扫描, 动态扫描, for annotation config
     */
    protected synchronized void secondScan() {

        // 该函数必须第一次运行后才能运行
        if (!isFirstInit) {
            LOGGER.info("should run First Scan before Second Scan.");
            return;
        }

        // 第二次扫描也只能做一次
        if (isSecondInit) {
            LOGGER.info("should not run twice.");
            return;
        }

        LOGGER.info("******************************* DISCONF START SECOND SCAN *******************************");

        try {

            // 扫描回调函数
            if (scanMgr != null) {
                scanMgr.secondScan();
            }

            // 注入数据至配置实体中
            // 获取数据/注入/Watch
            if (disconfCoreMgr != null) {
                disconfCoreMgr.inject2DisconfInstance();
            }

        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }

        isSecondInit = true;

        //
        // 不开启 则不要打印变量map
        //
        if (DisClientConfig.getInstance().ENABLE_DISCONF) {

            //
            String data = DisconfStoreProcessorFactory.getDisconfStoreFileProcessor()
                    .confToString();
            if (!StringUtils.isEmpty(data)) {
                LOGGER.info("Conf File Map: {}", data);
            }

            //
            data = DisconfStoreProcessorFactory.getDisconfStoreItemProcessor()
                    .confToString();
            if (!StringUtils.isEmpty(data)) {
                LOGGER.info("Conf Item Map: {}", data);
            }
        }
        LOGGER.info("******************************* DISCONF END *******************************");
    }

    /**
     * reloadable config file scan, for xml config
     */
    public synchronized void reloadableScan(String fileName) {

        if (!isFirstInit) {
            return;
        }

        if (DisClientConfig.getInstance().ENABLE_DISCONF) {
            try {

                if (!DisClientConfig.getInstance().getIgnoreDisconfKeySet().contains(fileName)) {

                    if (scanMgr != null) {
                        scanMgr.reloadableScan(fileName);
                    }

                    if (disconfCoreMgr != null) {
                        disconfCoreMgr.processFile(fileName);
                    }
                    LOGGER.debug("disconf reloadable file: {}", fileName);
                }

            } catch (Exception e) {

                LOGGER.error(e.toString(), e);
            }
        }
    }

    /**
     * @Description: 总关闭
     */
    public synchronized void close() {

        try {

            // disconfCoreMgr
            LOGGER.info("******************************* DISCONF CLOSE *******************************");
            if (disconfCoreMgr != null) {
                disconfCoreMgr.release();
            }

            // close, 必须将其设置为False,以便重新更新
            isFirstInit = false;
            isSecondInit = false;

        } catch (Exception e) {

            LOGGER.error("DisConfMgr close Failed.", e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
