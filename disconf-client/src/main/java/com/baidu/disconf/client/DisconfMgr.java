package com.baidu.disconf.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.core.DisconfCoreFactory;
import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.client.scan.ScanFactory;
import com.baidu.disconf.client.scan.ScanMgr;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;

/**
 * 
 * Disconf Client 总入口
 * 
 * @author liaoqiqi
 * @version 2014-5-23
 */
public class DisconfMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DisconfMgr.class);

    // 本实例不能初始化两次
    private static boolean isFirstInit = false;
    private static boolean isSecondeInit = false;

    // 扫描器
    private static ScanMgr scanMgr = null;

    // 核心处理器
    private static DisconfCoreMgr disconfCoreMgr = null;

    /**
     * 总入口
     * 
     * @param scanPackage
     */
    public synchronized static void start(String scanPackage) {

        firstScan(scanPackage);

        secondScan();
    }

    /**
     * 第一次扫描，静态扫描
     * 
     * @param scanPackage
     */
    public synchronized static void firstScan(String scanPackage) {

        // 该函数不能调用两次
        if (isFirstInit == true) {
            LOGGER.info("DisConfMgr has been init, ignore........");
            return;
        }

        //
        //
        //

        LOGGER.info("******************************* DISCONF START FIRST SCAN *******************************");

        try {

            // 导入配置
            ConfigMgr.init();

            // 是否开启远程配置
            if (DisClientConfig.getInstance().ENABLE_REMOTE_CONF == false) {
                LOGGER.info("ENABLE_REMOTE_CONF==0, we use Local Configuration.");
                return;
            }

            // 扫描器
            scanMgr = ScanFactory.getScanMgr();

            // 第一次扫描并入库
            scanMgr.firstScan(scanPackage);

            // 获取数据/注入/Watch
            disconfCoreMgr = DisconfCoreFactory.getDisconfCoreMgr();
            disconfCoreMgr.process();

            //
            isFirstInit = true;

        } catch (Exception e) {

            LOGGER.error(e.toString(), e);
        }
    }

    /**
     * 第二次扫描, 动态扫描
     * 
     * @param scanPackage
     */
    public synchronized static void secondScan() {

        // 该函数必须第一次运行后才能运行
        if (isFirstInit == false) {
            LOGGER.info("should run First Scan before Second Scan.");
            return;
        }

        // 第二次扫描也只能做一次
        if (isSecondeInit == true) {
            LOGGER.info("should not run twice.");
            return;
        }

        LOGGER.info("******************************* DISCONF START SECOND SCAN *******************************");

        try {

            // 扫描回调函数
            scanMgr.secondScan();

            // 注入数据至配置实体中
            disconfCoreMgr.inject2DisconfInstance();

        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }

        isSecondeInit = true;

        //
        LOGGER.info("Conf File Map: "
                + DisconfStoreProcessorFactory.getDisconfStoreFileProcessor()
                        .confToString());
        //
        LOGGER.info("Conf Item Map: "
                + DisconfStoreProcessorFactory.getDisconfStoreItemProcessor()
                        .confToString());

        LOGGER.info("******************************* DISCONF END *******************************");
    }

    /**
     * 
     * @Description: 总关闭
     * 
     * @return void
     * @author liaoqiqi
     * @date 2013-6-14
     */
    public synchronized static void close() {

        try {

            // disconfCoreMgr
            LOGGER.info("=============== disconfCoreMgr close =================");
            disconfCoreMgr.release();

            // close, 必须将其设置为False,以便重新更新
            isFirstInit = false;
            isSecondeInit = false;

        } catch (Exception e) {

            LOGGER.error("DisConfMgr close Failed.", e);
        }
    }

}
