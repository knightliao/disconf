package com.baidu.disconf.client.watch.impl;

import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf.client.common.model.DisConfCommonModel;
import com.baidu.disconf.client.config.inner.DisClientComConfig;
import com.baidu.disconf.client.core.processor.DisconfCoreProcessor;
import com.baidu.disconf.client.watch.WatchMgr;
import com.baidu.disconf.client.watch.inner.DisconfSysUpdateCallback;
import com.baidu.disconf.client.watch.inner.NodeWatcher;
import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.core.common.path.ZooPathMgr;
import com.baidu.disconf.core.common.utils.ZooUtils;
import com.baidu.disconf.core.common.zookeeper.ZookeeperMgr;

/**
 * Watch 模块的一个实现
 *
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class WatchMgrImpl implements WatchMgr {

    protected static final Logger LOGGER = LoggerFactory.getLogger(WatchMgrImpl.class);

    /**
     * zoo prefix
     */
    private String zooUrlPrefix;

    /**
     *
     */
    private boolean debug;

    /**
     * @Description: 获取自己的主备类型
     */
    public void init(String hosts, String zooUrlPrefix, boolean debug) throws Exception {

        this.zooUrlPrefix = zooUrlPrefix;
        this.debug = debug;

        // init zookeeper
        ZookeeperMgr.getInstance().init(hosts, zooUrlPrefix, debug);
    }

    /**
     * 新建监控
     *
     * @throws Exception
     */
    private String makeMonitorPath(DisConfigTypeEnum disConfigTypeEnum, DisConfCommonModel disConfCommonModel,
                                   String key, String value) throws Exception {

        // 应用根目录
        /*
            应用程序的 Zoo 根目录
        */
        String clientRootZooPath = ZooPathMgr.getZooBaseUrl(zooUrlPrefix, disConfCommonModel.getApp(),
                disConfCommonModel.getEnv(),
                disConfCommonModel.getVersion());
        ZookeeperMgr.getInstance().makeDir(clientRootZooPath, ZooUtils.getIp());

        // 监控路径
        String monitorPath;
        if (disConfigTypeEnum.equals(DisConfigTypeEnum.FILE)) {

            // 新建Zoo Store目录
            String clientDisconfFileZooPath = ZooPathMgr.getFileZooPath(clientRootZooPath);
            makePath(clientDisconfFileZooPath, ZooUtils.getIp());

            monitorPath = ZooPathMgr.joinPath(clientDisconfFileZooPath, key);

        } else {

            // 新建Zoo Store目录
            String clientDisconfItemZooPath = ZooPathMgr.getItemZooPath(clientRootZooPath);
            makePath(clientDisconfItemZooPath, ZooUtils.getIp());
            monitorPath = ZooPathMgr.joinPath(clientDisconfItemZooPath, key);
        }

        // 先新建路径
        makePath(monitorPath, "");

        // 新建一个代表自己的临时结点
        makeTempChildPath(monitorPath, value);

        return monitorPath;
    }

    /**
     * 创建路径
     */
    private void makePath(String path, String data) {

        ZookeeperMgr.getInstance().makeDir(path, data);
    }

    /**
     * 在指定路径下创建一个临时结点
     */
    private void makeTempChildPath(String path, String data) {

        String finerPrint = DisClientComConfig.getInstance().getInstanceFingerprint();

        String mainTypeFullStr = path + "/" + finerPrint;
        try {
            ZookeeperMgr.getInstance().createEphemeralNode(mainTypeFullStr, data, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            LOGGER.error("cannot create: " + mainTypeFullStr + "\t" + e.toString());
        }
    }

    /**
     * 监控路径,监控前会事先创建路径,并且会新建一个自己的Temp子结点
     */
    public void watchPath(DisconfCoreProcessor disconfCoreMgr, DisConfCommonModel disConfCommonModel, String keyName,
                          DisConfigTypeEnum disConfigTypeEnum, String value) throws Exception {

        // 新建
        String monitorPath = makeMonitorPath(disConfigTypeEnum, disConfCommonModel, keyName, value);

        // 进行监控
        NodeWatcher nodeWatcher =
                new NodeWatcher(disconfCoreMgr, monitorPath, keyName, disConfigTypeEnum, new DisconfSysUpdateCallback(),
                        debug);
        nodeWatcher.monitorMaster();
    }

    @Override
    public void release() {

        try {
            ZookeeperMgr.getInstance().release();
        } catch (InterruptedException e) {

            LOGGER.error(e.toString());
        }
    }

}
