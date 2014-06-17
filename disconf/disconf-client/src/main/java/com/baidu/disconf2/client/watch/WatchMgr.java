package com.baidu.disconf2.client.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.disconf2.client.config.inner.DisClientConfig;
import com.baidu.disconf2.client.config.inner.DisClientSysConfig;
import com.baidu.disconf2.client.fetcher.FetcherMgr;
import com.baidu.disconf2.client.watch.inner.DisconfSysUpdateCallback;
import com.baidu.disconf2.client.watch.inner.NodeWatcher;
import com.baidu.disconf2.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf2.core.common.path.DisconfWebPathMgr;
import com.baidu.disconf2.core.common.path.ZooPathMgr;
import com.baidu.disconf2.core.common.utils.ZooUtils;
import com.baidu.disconf2.core.common.zookeeper.ZookeeperMgr;

/**
 * Watch ZOO 模块, 依赖 FetcherMgr模块
 * 
 * @author liaoqiqi
 * @version 2014-6-10
 */
public class WatchMgr {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(WatchMgr.class);

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static WatchMgr instance = new WatchMgr();
    }

    public static WatchMgr getInstance() {
        return SingletonHolder.instance;
    }

    private WatchMgr() {

    }

    /**
     * 应用程序的 Zoo 根目录
     */
    private String clientRootZooPath = "";

    /**
     * 应用程序的Zoo 配置文件 目录
     */
    private String clientDisconfFileZooPath = "";

    /**
     * 应用程序的Zoo 配置项 目录
     */
    private String clientDisconfItemZooPath = "";

    /**
     * Zoo hosts
     */
    private String hosts = "";

    private boolean isInit = false;

    /**
     * 
     * @Description: 获取自己的主备类型
     * 
     * @return void
     * @author liaoqiqi
     * @date 2013-6-16
     */
    public void init() throws Exception {

        // 下载模块必须先初始化
        if (!FetcherMgr.isInit()) {
            throw new Exception("FetcherMgr should be init before WatchMgr");
        }

        // 获取 Zoo Hosts
        try {
            hosts = FetcherMgr
                    .getValueFromServer(DisconfWebPathMgr
                            .getZooHostsUrl(DisClientSysConfig.getInstance().CONF_SERVER_ZOO_ACTION));
        } catch (Exception e) {
            throw new Exception("cannot get zoo hosts", e);
        }

        // init zookeeper
        ZookeeperMgr.getInstance().init(hosts,
                DisClientSysConfig.getInstance().ZOOKEEPER_URL_PREFIX);

        // 新建初始化目录
        setupPath();

        this.isInit = true;
    }

    /**
     * 
     * @throws Exception
     */
    private void setupPath() throws Exception {

        // 应用根目录
        this.clientRootZooPath = ZooPathMgr.getZooBaseUrl(
                DisClientSysConfig.getInstance().ZOOKEEPER_URL_PREFIX,
                DisClientConfig.getInstance().APP,
                DisClientConfig.getInstance().ENV,
                DisClientConfig.getInstance().VERSION);
        ZookeeperMgr.getInstance().makeDir(clientRootZooPath, ZooUtils.getIp());

        // 新建Zoo Store目录
        this.clientDisconfFileZooPath = ZooPathMgr
                .getFileZooPath(clientRootZooPath);
        ZookeeperMgr.getInstance().makeDir(clientDisconfFileZooPath,
                ZooUtils.getIp());

        // 新建Zoo Store目录
        this.clientDisconfItemZooPath = ZooPathMgr
                .getItemZooPath(clientRootZooPath);
        ZookeeperMgr.getInstance().makeDir(clientDisconfItemZooPath,
                ZooUtils.getIp());
    }

    /**
     * 创建路径
     * 
     * @param path
     */
    public void makePath(String path, String data) {

        ZookeeperMgr.getInstance().makeDir(path, data);
    }

    /**
     * 监控路径,监控前会事先创建路径
     */
    public void watchPath(String monitorPath, String keyName,
            DisConfigTypeEnum disConfigTypeEnum, String value) {

        // 先新建路径
        makePath(monitorPath, value);

        // 进行监控
        NodeWatcher nodeWatcher = new NodeWatcher(monitorPath, keyName,
                disConfigTypeEnum, new DisconfSysUpdateCallback());
        nodeWatcher.monitorMaster();
    }

    /**
     * 
     * @return
     */
    public boolean isInit() {
        return isInit;
    }

    //
    //
    //

    public String getClientDisconfFileZooPath() {
        return clientDisconfFileZooPath;
    }

    public void setClientDisconfFileZooPath(String clientDisconfFileZooPath) {
        this.clientDisconfFileZooPath = clientDisconfFileZooPath;
    }

    public String getClientDisconfItemZooPath() {
        return clientDisconfItemZooPath;
    }

    public void setClientDisconfItemZooPath(String clientDisconfItemZooPath) {
        this.clientDisconfItemZooPath = clientDisconfItemZooPath;
    }
}
