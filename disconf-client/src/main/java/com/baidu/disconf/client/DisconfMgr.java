package com.baidu.disconf.client;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.addons.properties.ReloadConfigurationMonitor;
import com.baidu.disconf.client.config.ConfigMgr;
import com.baidu.disconf.client.config.DisClientConfig;
import com.baidu.disconf.client.core.DisconfCoreFactory;
import com.baidu.disconf.client.core.DisconfCoreMgr;
import com.baidu.disconf.client.scan.ScanFactory;
import com.baidu.disconf.client.scan.ScanMgr;
import com.baidu.disconf.client.store.DisconfStoreProcessorFactory;

/**
 * Disconf Client 总入口
 *
 * @author liaoqiqi
 * @version 2014-5-23
 */
public class DisconfMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DisconfMgr.class);

    // 本实例不能初始化两次
    private static boolean isFirstInit = false;
    private static boolean isSecondInit = false;

    // 扫描器
    private static ScanMgr scanMgr = null;

    // 核心处理器
    private static DisconfCoreMgr disconfCoreMgr = null;

    // timer
    private static Timer timer = new Timer();

    /**
     * 总入口
     */
    public synchronized static void start(String scanPackage) {

        firstScan(scanPackage);

        secondScan();
    }

    /**
     * 第一次扫描，静态扫描 for annotation config
     */
    public synchronized static void firstScan(String scanPackage) {

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

            // 扫描器
            scanMgr = ScanFactory.getScanMgr();

            // 第一次扫描并入库
            scanMgr.firstScan(scanPackage);

            // 获取数据/注入/Watch
            disconfCoreMgr = DisconfCoreFactory.getDisconfCoreMgr();
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
    public synchronized static void secondScan() {

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
            scanMgr.secondScan();

            // 注入数据至配置实体中
            disconfCoreMgr.inject2DisconfInstance();

        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }

        isSecondInit = true;

        //
        // start timer
        //
        startTimer();

        //
        LOGGER.info("Conf File Map: " + DisconfStoreProcessorFactory.getDisconfStoreFileProcessor().confToString());
        //
        LOGGER.info("Conf Item Map: " + DisconfStoreProcessorFactory.getDisconfStoreItemProcessor().confToString());

        LOGGER.info("******************************* DISCONF END *******************************");
    }

    /**
     * reloadable config file scan, for xml config
     */
    public synchronized static void reloadableScan(String filename) {

        if (!isFirstInit) {
            return;
        }

        if (!DisClientConfig.getInstance().ENABLE_DISCONF) {
            return;
        }

        //
        //
        //

        try {

            if (scanMgr != null) {
                scanMgr.reloadableScan(filename);
            }

            if (disconfCoreMgr != null) {
                disconfCoreMgr.processFile(filename);
            }

            LOGGER.debug("disconf reloadable file:" + filename);

        } catch (Exception e) {

            LOGGER.error(e.toString(), e);
        }
    }

    /**
     *
     */
    private static void startTimer() {
        if (timer != null) {
            timer.schedule(new ReloadConfigurationMonitor(), 2000, 1000);// 两秒后启动任务,每秒检查一次
        } else {
            LOGGER.error("timer is null");
        }
    }

    /**
     * @Description: 总关闭
     */
    public synchronized static void close() {

        try {

            // disconfCoreMgr
            LOGGER.info("******************************* DISCONF CLOSE *******************************");
            if (disconfCoreMgr != null) {
                disconfCoreMgr.release();
            }

            // timer
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

            // close, 必须将其设置为False,以便重新更新
            isFirstInit = false;
            isSecondInit = false;

        } catch (Exception e) {

            LOGGER.error("DisConfMgr close Failed.", e);
        }
    }

}
